package classes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

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

	public Graph(int width, int height, ArrayList<ArrayList<ArrayList<Position>>> adjacencyList,
			ArrayList<Position> pathPos) {
		this.width = width;
		this.height = height;
		this.nodes = new Node2D[width][height];
		this.pathPos = pathPos;
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				this.nodes[i][j] = new Node2D(i, j, false);
			}
		}

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				for (int k = 0; k < adjacencyList.get(i).get(j).size(); k++) {
					Position adjacencyNode = adjacencyList.get(i).get(j).get(k);
					this.nodes[i][j].setNeighbor(this.nodes[(int) adjacencyNode.x][(int) adjacencyNode.y]);
//					ArrayList<Node2D> neighbors = new ArrayList<>(Arrays.asList(this.nodes[i][j].nodeN, this.nodes[i][j].nodeE, this.nodes[i][j].nodeS,
//							this.nodes[i][j].nodeW, this.nodes[i][j].nodeVN, this.nodes[i][j].nodeVE, this.nodes[i][j].nodeVS, this.nodes[i][j].nodeVW));
//					System.out.println(neighbors);
//					Correct but move to neighbors in calPathAStar went wrong
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
		for (int i = 0; i < this.agents.size(); i++) {
			Agent agent = this.agents.get(i);
			if (agent.active) {
				int xl = (int) Math.floor(agent.getTranslateX() / 20);
				int xr = (int) Math.floor((agent.getTranslateX() + 19) / 20);
				int yt = (int) Math.floor(agent.getTranslateY() / 20);
				int yb = (int) Math.floor((agent.getTranslateY() + 19) / 20);
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
				if (astar_f[openSet.get(i).x][openSet.get(i).y] < astar_f[openSet.get(winner).x][openSet.get(winner).y]) {
					winner = i;
//	FUNCTION NOT CALL				
					System.out.println(winner);
				}
			}
			Node2D current = openSet.get(winner);
			if (openSet.get(winner).equal(end)) {
//	FUNCTION NOT CALL
				Node2D cur = this.nodes[end.x][end.y];
				path.add(cur);
				while (previous[cur.x][cur.y] != null) {
					path.add(previous[cur.x][cur.y]);
					cur = previous[cur.x][cur.y];
				}
				Collections.reverse(path);
				System.out.println("path has length: " + path.size() + " instead of " + lengthOfPath);
				return path;
			}
//			System.out.println("openSet: " + openSet);
//			System.out.println("winner:  " + winner);
			openSet.remove(winner);
			closeSet.add(current);
//			System.out.println("closeSet before removing:  " + closeSet);
			ArrayList<Node2D> neighbors = new ArrayList<>(Arrays.asList(current.nodeN, current.nodeE, current.nodeS,
					current.nodeW, current.nodeVN, current.nodeVE, current.nodeVS, current.nodeVW));r
			System.out.println(neighbors);
//			System.out.println(neighbors);
//			At the fourth loop, nodeN of neighbors should not be null, it should be 4 12
			for (Node2D neighbor : neighbors) {
				System.out.println(neighbor);
				if (neighbor != null) { // <-----
					int timexoay = 0;
					if (previous[current.x][current.y] != null && neighbor.x != previous[current.x][current.y].x
							&& neighbor.y != previous[current.x][current.y].y) {
//	FUNCTION NOT CALL
						timexoay = 1;
					}
					double tempG = astar_g[current.x][current.y] + 1 + current.getW() + timexoay;
//					tempG should be 1,2,3,5,5,6,7,..,24,24,..26,28,28,6,6,12,16,20 but stop at 5
					
					if (!this.isInclude(neighbor, closeSet)) {
						if (this.isInclude(neighbor, openSet)) {
//	FUNCTION NOT CALL
							if (tempG < astar_g[neighbor.x][neighbor.y]) {
								astar_g[neighbor.x][neighbor.y] = tempG;
							}
						} else {
							astar_g[neighbor.x][neighbor.y] = tempG;
//							System.out.println(astar_g[neighbor.x][neighbor.y]);
							openSet.add(neighbor);
							lengthOfPath++;
						}
						astar_h[neighbor.x][neighbor.y] = this.heuristic(neighbor, end);
//  heuristic function false: should be 17,16,15,... instead of 13,12,11
						astar_f[neighbor.x][neighbor.y] = astar_h[neighbor.x][neighbor.y]
								+ astar_g[neighbor.x][neighbor.y];
//						System.out.println("Astar f:"+ astar_f[neighbor.x][neighbor.y]);
						previous[neighbor.x][neighbor.y] = current;
					} else {
//	FUNCTION NOT CALL
						if (tempG < astar_g[neighbor.x][neighbor.y]) {
							openSet.add(neighbor);
							int index = closeSet.indexOf(neighbor);
							if (index > -1) {
								closeSet.remove(index);
							}
						}
					}
				}
				
//				System.out.println("closeSet at for loop:  " + closeSet);
			}
//			System.out.println("closeSet at while loop:  "  + closeSet);
		}
//		Everything stop at while loop 4 times cause openset is empty
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
