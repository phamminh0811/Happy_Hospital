package algorithm;

import java.util.ArrayList;
import java.util.Stack;

import model.Position;

class Spot {
	public int i;
	public int j;
	public double f;
	public double g;
	public double h;
	public Stack<Spot> neighbors;
	public Spot previous;

	public Spot(int i, int j) {
		this.i = i;
		this.j = j;
		this.f = 0;
		this.g = 0;
		this.h = 0;
	}

	public void addNeighbors(Spot[] ableSpot) {
		for (int k = 0; k < ableSpot.length; k++) {
			if (this.i + 1 == ableSpot[k].i && this.j == ableSpot[k].j) {
				this.neighbors.add(ableSpot[k]);
			} else if (this.i == ableSpot[k].i && this.j + 1 == ableSpot[k].j) {
				this.neighbors.add(ableSpot[k]);
			} else if (this.i - 1 == ableSpot[k].i && this.j == ableSpot[k].j) {
				this.neighbors.add(ableSpot[k]);
			} else if (this.i == ableSpot[k].i && this.j - 1 == ableSpot[k].j) {
				this.neighbors.add(ableSpot[k]);
			}
		}
	}

	public boolean equal(Spot spot) {
		if (this.i == spot.i && this.j == spot.j)
			return true;
		return false;
	}
}

public class AStar {
	public int width;
	public int height;
	public Spot start;
	public Spot end;
	public ArrayList<Spot> ableSpot;
	public Spot[][] grid;
	public Stack<Spot> path;

	public AStar(int width, int height, Position startPos, Position endPos, Stack<Position> groundPos) {
		this.width = width;
		this.height = height;
		this.start = new Spot((int)startPos.x, (int)startPos.y);
		this.end = new Spot((int)endPos.x, (int)endPos.y);

		this.grid = new Spot[width][height];
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				this.grid[i][j] = new Spot(i, j);
			}
		}

		this.ableSpot = new ArrayList<>();
		for (int i = 0; i < groundPos.size(); i++) {
			this.ableSpot.add(this.grid[(int)groundPos.elementAt(i).x][(int)groundPos.elementAt(i).y]);
		}

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				this.grid[i][j].addNeighbors((Spot[]) this.ableSpot.toArray());
			}
		}
	}

	private double heuristic(Spot spot1, Spot spot2) {
		return Math.abs(spot1.i - spot2.i) + Math.abs(spot1.j - spot2.j);
	}

	private double heuristic2(Spot spot1, Spot spot2) {
		return Math.sqrt((spot1.i - spot2.i) * (spot1.i - spot2.i) + (spot1.j - spot2.j) * (spot1.j - spot2.j));
	}

	private boolean isInclude(Stack<Spot> spots, Spot spot) {
		for (int i = 0; i < spots.size(); i++) {
			if (spot.i == spots.elementAt(i).i && spot.j == spots.elementAt(i).j)
				;
			return true;
		}
		return false;
	}

	public static <T> Stack<T> reverse(Stack<T> path) {
		Stack<T> temp = new Stack<>();
		while (path.size() != 0) {
			T t = path.pop();
			temp.push(t);
		}
		return temp;
	}

	public Stack<Position> cal() {
		Stack<Spot> openSet = new Stack<>();
		Stack<Spot> closeSet = new Stack<>();
		openSet.push(this.grid[this.start.i][this.start.j]);
		while (openSet.size() > 0) {
			int winner = 0;
			for (int i = 0; i < openSet.size(); i++) {
				if (openSet.elementAt(i).f < openSet.elementAt(winner).f) {
					winner = i;
				}
			}
			Spot current = openSet.elementAt(winner);
			if (openSet.elementAt(winner).equal(this.end)) {
				Spot cur = this.grid[this.end.i][this.end.j];
				this.path.push(cur);
				while (cur.previous != null) {
					this.path.push(cur.previous);
					cur = cur.previous;
				}
				this.path = reverse(this.path);
				Stack<Position> result = new Stack<>();
				for (int k = 0; k < this.path.size(); k++) {
					result.push(new Position(this.path.elementAt(k).i, this.path.elementAt(k).j));
				}
				return result;
			}
			openSet.remove(winner);
			closeSet.push(current);

			Stack<Spot> neighbors = current.neighbors;
			for (int i = 0; i < neighbors.size(); i++) {
				Spot neighbor = neighbors.elementAt(i);
				if (!this.isInclude(closeSet, neighbor)) {
					double tempG = current.g + 1;
					if (this.isInclude(openSet, neighbor)) {
						if (tempG < neighbor.g) {
							neighbor.g = tempG;
						}
					} else {
						neighbor.g = tempG;
						openSet.push(neighbor);
					}

					neighbor.h = this.heuristic2(neighbor, this.end);
					neighbor.f = neighbor.h + neighbor.g;
					neighbor.previous = current;
				} else {
					double tempG = current.g + 1;
					if (tempG < neighbor.g) {
						openSet.push(neighbor);
						int index = closeSet.indexOf(neighbor);
						if (index > -1) {
							closeSet.remove(index);
						}
					}
				}
			}
		}
		System.out.println("Path not found!");
	    return null;
	}
}
