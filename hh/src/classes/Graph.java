package classes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import static config.Config.*;

public class Graph {
	public Node2D[][] nodes;
	public int width;
	public int height;
	public List<Agent> agents = new CopyOnWriteArrayList<>();
	public int[][] busy;
	private ArrayList<Position> doorPos;
	private ArrayList<Position> groundPos;
	public ArrayList<Position> pathPos;
	private Set<AutoAgv> autoAgvs;
	private Agv agv;
	private ArrayList<ArrayList<ArrayList<Position>>> adjacencyList;
	public Graph(int width, int height, ArrayList<ArrayList<ArrayList<Position>>> adjacencyList,
			ArrayList<Position> pathPos) {
		this.width = width;
		this.height = height;
		this.nodes = new Node2D[width][height];
		this.pathPos = pathPos;
		this.adjacencyList = adjacencyList;
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				this.nodes[i][j] = new Node2D(i, j);
			}
		}
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				for (int k = 0; k < adjacencyList.get(i).get(j).size(); k++) {
					Position adjacencyNode = adjacencyList.get(i).get(j).get(k);
					this.nodes[i][j].setNeighbor(this.nodes[(int) adjacencyNode.x][(int) adjacencyNode.y]);
				}
			}
		}
		for (Position p : pathPos) {
			this.nodes[(int) p.x][(int) p.y].setState(StateOfNode2D.EMPTY);
		}

		this.busy = new int[52][28];
		for (int i = 0; i < 52; i++) {
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
//
//	public void addAutoAgv(AutoAgv autoAgv) {
//		if (!this.autoAgvs.isEmpty()){
//			this.autoAgvs.add(autoAgv);
//		}
//	}
	
	public Set<AutoAgv> getAutoAgvs() {
		return this.autoAgvs;
	}

	public void setMAgv(Agv agv) {
		this.agv = agv;
	}

	public void setAgents(List<Agent> agents2) {
		for (Position p : this.pathPos) {
			this.nodes[(int) p.x][(int) p.y].setState(StateOfNode2D.EMPTY);
		}
		this.busy = new int[52][28];
		for (int i = 0; i < 52; i++) {
			for (int j = 0; j < 28; j++) {
				if (this.nodes[i][j].state == StateOfNode2D.EMPTY) {
					this.busy[i][j] = 0;
				} else {
					this.busy[i][j] = 2;
				}
			}
		}
		this.agents = agents2;
	}

	public void updateState() {
		int[][] cur = new int[52][28];
		for (int i = 0; i < 52; i++) {
			for (int j = 0; j < 28; j++) {
				cur[i][j] = 0;
			}
		}
		if (this.agv != null) {
			int agv_xl = (int) Math.floor(this.agv.getTranslateX() / TILE_WIDTH);
			int agv_xr = (int) Math.floor((this.agv.getTranslateX() + TILE_WIDTH-1) / TILE_WIDTH);
			int agv_yt = (int) Math.floor(this.agv.getTranslateY() / TILE_HEIGHT);
			int agv_yb = (int) Math.floor((this.agv.getTranslateY() + TILE_HEIGHT-1) / TILE_HEIGHT);
			
			cur[agv_xl][agv_yt] = 1;
			cur[agv_xl][agv_yb] = 1;
			cur[agv_xr][agv_yt] = 1;
			cur[agv_xr][agv_yb] = 1;
		}
		for (int i = 0; i < this.agents.size(); i++) {
			Agent agent = this.agents.get(i);
			if (agent.active) {
				int xl = (int) Math.floor(agent.getTranslateX() / TILE_WIDTH);
				int xr = (int) Math.floor((agent.getTranslateX() + TILE_WIDTH-1) / TILE_WIDTH);
				int yt = (int) Math.floor(agent.getTranslateY() / TILE_HEIGHT);
				int yb = (int) Math.floor((agent.getTranslateY() + TILE_HEIGHT-1) / TILE_HEIGHT);
				cur[xl][yt] = 1;
				cur[xl][yb] = 1;
				cur[xr][yt] = 1;
				cur[xr][yb] = 1;
			}
		}
		
		for (int i = 0; i < 52; i++) {
			for (int j = 0; j < 28; j++) {
				if (this.busy[i][j] == 2) {
					continue;
				} else if (this.busy[i][j] == 0) {
					if ((cur[i][j] == 0))
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
		int i = agent.x;
		int j = agent.y;
		this.nodes[i][j].setState(StateOfNode2D.EMPTY);
		this.busy[i][j] = 0;
	}

	public ArrayList<Node2D> calPathAStar(Node2D start, Node2D end) {
		ArrayList<Node2D> openSet = new ArrayList<>();
		ArrayList<Node2D> closeSet = new ArrayList<>();
		ArrayList<Node2D> path = new ArrayList<>();
		double[][] astar_f = new double[this.width][this.height];
		double[][] astar_g = new double[this.width][this.height];
		int[][] astar_h = new int[this.width][this.height];
		Node2D[][] previous = new Node2D[this.width][this.height];
		for (int i = 0; i < this.width; i++) {
			for (int j = 0; j < this.height; j++) {
				astar_f[i][j] = 0;
				astar_g[i][j] = 0;
				astar_h[i][j] = 0;
			}
		}
		int lengthOfPath = 1;
		openSet.add(this.nodes[start.x][start.y]);
		while (openSet.size() > 0) {
			int winner = 0;
			for (int i = 0; i < openSet.size(); i++) {
				if (astar_f[openSet.get(i).x][openSet.get(i).y] < astar_f[openSet.get(winner).x][openSet
						.get(winner).y]) {
					winner = i;
				}
			}
			Node2D current = openSet.get(winner);
			if (openSet.get(winner).equal(end)) {
				Node2D cur = this.nodes[end.x][end.y];
				path.add(cur);
				while (previous[cur.x][cur.y] != null) {
					path.add(previous[cur.x][cur.y]);
					cur = previous[cur.x][cur.y];
				}
				Collections.reverse(path);
				return path;
			}
			openSet.remove(winner);
			closeSet.add(current);
			ArrayList<Node2D> neighbors = new ArrayList<>(Arrays.asList(current.nodeN, current.nodeE, current.nodeS,
					current.nodeW, current.nodeVN, current.nodeVE, current.nodeVS, current.nodeVW));
			for (Node2D neighbor : neighbors) {
				if (neighbor != null) { // <-----
					int timexoay = 0;
					if (previous[current.x][current.y] != null && neighbor.x != previous[current.x][current.y].x
							&& neighbor.y != previous[current.x][current.y].y) {
						timexoay = 1;
					}
					double tempG = astar_g[current.x][current.y] + 1 + current.getW() + timexoay;
					if (!this.isInclude(neighbor, closeSet)) {
						if (this.isInclude(neighbor, openSet)) {
							if (tempG < astar_g[neighbor.x][neighbor.y]) {
								astar_g[neighbor.x][neighbor.y] = tempG;
							}
						} else {
							astar_g[neighbor.x][neighbor.y] = tempG;
							openSet.add(neighbor);
							lengthOfPath++;
						} //
						astar_h[neighbor.x][neighbor.y] = this.heuristic(neighbor, end);
						astar_f[neighbor.x][neighbor.y] = astar_h[neighbor.x][neighbor.y]
								+ astar_g[neighbor.x][neighbor.y];
						previous[neighbor.x][neighbor.y] = current;
					} else {
						if (tempG < astar_g[neighbor.x][neighbor.y]) {
							openSet.add(neighbor);
							int index = closeSet.indexOf(neighbor);
							if (index > -1) {
								closeSet.remove(index);
							}
						}
					}
				}
			}
		}
		System.out.println("Path not found!");
		return null;
	}

	public boolean isInclude(Node2D node, ArrayList<Node2D> nodes) {
		for (Node2D node2D : nodes) {
			if (node.equal(node2D))
				return true;
		}
		return false;
	}

	public int heuristic(Node2D node1, Node2D node2) {
		return Math.abs(node1.x - node2.x) + Math.abs(node1.y - node2.y);
	}
}
