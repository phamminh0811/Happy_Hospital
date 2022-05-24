package model;

import java.util.Set;
import java.util.Stack;

import algorithm.AStar;

public class Graph {
	public Node2D[][] nodes;
	public int width;
	public int height;
	public Agent[] agents;
	public double[][] busy;
	public Position[] pathPos;
	private Set<AutoAgv> autoAgvs;
	private Agv agv;

	public Graph(int width, int height, Position[][][] danhsachke, Position[] pathPos) {
		this.width = width;
		this.height = height;
		this.pathPos = pathPos;
		this.nodes = new Node2D[width][];
		for (int i = 0; i < width; i++) {
			this.nodes[i] = new Node2D[height];
			for (int j = 0; j < height; j++) {
				this.nodes[i][j] = new Node2D(i, j);
			}
		}
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				for (int k = 0; k < danhsachke[i][j].length; k++) {
					Position nutke = danhsachke[i][j][k];
					this.nodes[i][j].setNeighbor(this.nodes[(int) nutke.x][(int) nutke.y]);
				}
			}
		}
		for (Position p : pathPos) {
			this.nodes[(int) p.x][(int) p.y].setState(StateOfNode2D.EMPTY);
		}
		this.busy = new double[52][];
		for (int i = 0; i < 52; i++) {
			this.busy[i] = new double[28];
			for (int j = 0; j < 28; j++) {
				if (this.nodes[i][j].state == StateOfNode2D.EMPTY) {
					this.busy[i][j] = 0;
				} else {
					this.busy[i][j] = 2;
				}
			}
		}
	}

	public void setAutoAgvs(Set<AutoAgv> agvs) {
		this.autoAgvs = agvs;
	}

	public Set<AutoAgv> getAutoAgvs() {
		return this.autoAgvs;
	}

	public void setMAgv(Agv agv) {
		this.agv = agv;
	}

	public void setAgent(Agent[] agents) {
		for (Position p : this.pathPos) {
			this.nodes[(int) p.x][(int) p.y].setState(StateOfNode2D.EMPTY);
		}
		this.busy = new double[52][];
		for (int i = 0; i < 52; i++) {
			this.busy[i] = new double[28];
			for (int j = 0; j < 28; j++) {
				if (this.nodes[i][j].state == StateOfNode2D.EMPTY) {
					this.busy[i][j] = 0;
				} else {
					this.busy[i][j] = 2;
				}
			}
		}
		this.agents = agents;
	}

	public void updateState() {
		int cur[][] = new int[52][28];
		for (int i = 0; i < 52; i++) {
			for (int j = 0; j < 28; j++) {
				cur[i][j] = 0;
			}
		}
		for (int i = 0; i < this.agents.length; i++) {
			Agent agent = this.agents[i];
			if (true) {
				int xl = (int) Math.floor(agent.x / 32);
				int xr = (int) Math.floor((agent.x + 31) / 32);
				int yt = (int) Math.floor(agent.y / 32);
				int yb = (int) Math.floor((agent.y + 31) / 32);
				cur[xl][yt] = 1;
				cur[xl][yb] = 1;
				cur[xr][yt] = 1;
				cur[xr][yb] = 1;
			}
		}
		for (int i = 0; i < 52; i++) {
			for (int j = 0; j < 28; j++) {
				if (this.busy[i][j] == 2)
					continue;
				else if (this.busy[i][j] == 0) {
					if (cur[i][j] == 0)
						continue;
					this.nodes[i][j].setState(StateOfNode2D.BUSY);
					this.busy[i][j] = 1;
				} else {
					if (cur[i][j] == 1)
						continue;
					this.nodes[i][j].setState(StateOfNode2D.EMPTY);
					this.busy[i][j] = 0;
				}
			}
		}
	}

	public void removeAgent(Agent agent) {
		int i = (int) agent.x / 32;
		int j = (int) agent.y / 32;
		this.nodes[i][j].setState(StateOfNode2D.EMPTY);
		this.busy[i][j] = 0;
	}

	public Stack<Node2D> calPathAStar(Node2D start, Node2D end) {
		Stack<Node2D> openSet = new Stack<>();
		Stack<Node2D> closeSet = new Stack<>();
		Stack<Node2D> path = new Stack<>();
		double astar_f[][] = new double[width][height];
		double astar_g[][] = new double[width][height];
		double astar_h[][] = new double[width][height];
		Node2D previous[][] = new Node2D[width][height];
		for (int i = 0; i < this.width; i++) {
			for (int j = 0; j < this.height; j++) {
				astar_f[i][j] = 0;
				astar_g[i][j] = 0;
				astar_h[i][j] = 0;
			}
		}
		int lengthOfPath = 1;
		openSet.push(this.nodes[(int) start.x][(int) start.y]);
		while (openSet.size() > 0) {
			int winner = 0;
			for (int i = 0; i < openSet.size(); i++) {
				if (astar_f[(int) openSet.elementAt(i).x][(int) openSet.elementAt(
						i).y] < astar_f[(int) openSet.elementAt(winner).x][(int) openSet.elementAt(winner).y]) {
					winner = i;
				}
			}
			Node2D current = openSet.elementAt(winner);
			if (openSet.elementAt(winner).equal(end)) {
				Node2D cur = this.nodes[(int) end.x][(int) end.y];
				path.push(cur);
				while (previous[(int) cur.x][(int) cur.y] != null) {
					path.push(previous[(int) cur.x][(int) cur.y]);
					cur = previous[(int) cur.x][(int) cur.y];
				}
				path = AStar.reverse(path);
				// console.assert(lengthOfPath == path.length, "path has length: " + path.length
				// + " instead of " + lengthOfPath);
				return path;
			}
			openSet.removeElementAt(winner);
			closeSet.push(current);
			Node2D neighbors[] = { current.nodeN, current.nodeE, current.nodeS, current.nodeW, current.nodeVN,
					current.nodeVE, current.nodeVS, current.nodeVW };
			for (int i = 0; i < neighbors.length; i++) {
				Node2D neighbor = neighbors[i];
				if (neighbor != null) {
					int timexoay = 0;
					if (previous[(int) current.x][(int) current.y] != null
							&& neighbor.x != previous[(int) current.x][(int) current.y].x
							&& neighbor.y != previous[(int) current.x][(int) current.y].y) {
						timexoay = 1;
					}
					double tempG = astar_g[(int) current.x][(int) current.y] + 1 + current.getW() + timexoay;
					if (!this.isInclude(neighbor, closeSet)) {
						if (this.isInclude(neighbor, openSet)) {
							if (tempG < astar_g[(int) neighbor.x][(int) neighbor.y]) {
								astar_g[(int) neighbor.x][(int) neighbor.y] = tempG;
							}
						} else {
							astar_g[(int) neighbor.x][(int) neighbor.y] = tempG;
							openSet.push(neighbor);
							lengthOfPath++;
						}
						astar_h[(int) neighbor.x][(int) neighbor.y] = this.heuristic(neighbor, end);
						astar_f[(int) neighbor.x][(int) neighbor.y] = astar_h[(int) neighbor.x][(int) neighbor.y]
								+ astar_g[(int) neighbor.x][(int) neighbor.y];
						previous[(int) neighbor.x][(int) neighbor.y] = current;
					} else {
						if (tempG < astar_g[(int) neighbor.x][(int) neighbor.y]) {
							openSet.push(neighbor);
							int index = closeSet.indexOf(neighbor);
							if (index > -1) {
								closeSet.removeElementAt(index);
							}
						}
					}
				}
			}
		}
		System.out.println("Path not found!");
	    return null;
	}

	public boolean isInclude(Node2D node, Stack<Node2D> nodes) {
		for (int i = 0; i < nodes.size(); i++) {
			if (node.equal(nodes.elementAt(i)))
				return true;
		}
		return false;
	}

	public double heuristic(Node2D node1, Node2D node2) {
		return Math.abs(node1.x - node2.x) + Math.abs(node1.y - node2.y);
	}
}
