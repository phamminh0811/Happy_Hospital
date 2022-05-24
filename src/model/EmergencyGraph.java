package model;

import java.util.Stack;

import algorithm.AStar;
import config.Constant;

public class EmergencyGraph extends Graph {
	public Node2D[][] virtualNodes;

	public EmergencyGraph(int width, int height, Position[][][] danhsachke, Position[] pathPos) {
		super(width, height, danhsachke, pathPos);
		this.virtualNodes = new Node2D[width][height];
		for(int i=0;i<width;i++) {
			for(int j=0;j<height;j++) {
				for(int k=0;k<danhsachke[i][j].length;k++) {
					Position nutke = danhsachke[i][j][k];
					this.nodes[i][j].setNeighbor(this.virtualNodes[(int)nutke.x][(int)nutke.y]);
					this.virtualNodes[i][j].setNeighbor(this.virtualNodes[(int) nutke.x][(int) nutke.y]);
		            this.virtualNodes[i][j].setNeighbor(this.nodes[(int) nutke.x][(int) nutke.y]);
				}
			}
		}
		for(Position p: pathPos) {
			this.virtualNodes[(int) p.x][(int) p.y].setState(StateOfNode2D.EMPTY);
		}
	}

	public void updateState() {
		super.updateState();
		for (int j = 0; j < this.width; j++) {
			for (int k = 0; k < this.height; k++) {
				int x = (int) this.nodes[j][k].x;
				int y = (int) this.nodes[j][k].y;
				this.nodes[j][k].setWeight(0);
				this.virtualNodes[j][k].setWeight(0);
				for (int i = 0; i < this.agents.length; i++) {
					int dist = (int) Math
							.sqrt(Math.pow((x - this.agents[i].x), 2) + Math.pow((y - this.agents[i].y), 2));
					if (dist / this.agents[i].speed < Constant.DELTA_T()) {
						this.nodes[j][k].setWeight(this.nodes[j][k].getWeight() + 1);
					}
				}
				if (this.getAutoAgvs() != null) {
					for (AutoAgv item : this.getAutoAgvs())
						if (item.path != null) {
							for (int i = 0; i < item.path.size(); i++) {
								if (item.path.elementAt(i).isVirtualNode) {
									if (item.path.elementAt(i).x == this.virtualNodes[j][k].x
											&& item.path.elementAt(i).y == this.virtualNodes[j][k].y) {
										this.virtualNodes[j][k].setWeight(this.virtualNodes[j][k].getWeight() + 1);
									}
								} else {
									if (item.path.elementAt(i).equal(this.nodes[j][k])) {
										this.nodes[j][k].setWeight(this.nodes[j][k].getWeight() + 1);
									}
								}
							}
						}
				}
			}
		}
	}

	public Stack<Node2D> calPathAStar(Node2D start, Node2D end) {
		/**
		 * Khoi tao cac bien trong A*
		 */
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
					if (!this.isInclude(neighbor, closeSet)) {
						int timexoay = 0;
						if (previous[(int) current.x][(int) current.y] != null
								&& neighbor.x != previous[(int) current.x][(int) current.y].x
								&& neighbor.y != previous[(int) current.x][(int) current.y].y) {
							timexoay = 1;
						}
						double tempG = astar_g[(int) current.x][(int) current.y] + 1 + current.getW() + timexoay;
						if (super.isInclude(neighbor, openSet)) {
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
					} // end of if (!this.isInclude(neighbor, closeSet)) {
				}
			}
		} // end of while (openSet.length > 0)
		System.out.println("Path not found!");
		return null;
	}
}
