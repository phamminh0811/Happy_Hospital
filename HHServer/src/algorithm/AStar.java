package algorithm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Stack;

import classes.Position;

class Spot {
	public int i;
	public int j;
	public double f;
	public double g;
	public double h;
	public Stack<Spot> neighbors = new Stack<Spot>();
	public Spot previous;

	public Spot(int i, int j) {
		this.i = i;
		this.j = j;
		this.f = 0;
		this.g = 0;
		this.h = 0;
	}

	public void addNeighbors(Stack<Spot> ableSpot) {
		for (int k = 0; k < ableSpot.size(); k++) {
			if (this.i + 1 == ableSpot.elementAt(k).i && this.j == ableSpot.elementAt(k).j) {
				this.neighbors.add(ableSpot.elementAt(k));
			} else if (this.i == ableSpot.elementAt(k).i && this.j + 1 == ableSpot.elementAt(k).j) {
				this.neighbors.add(ableSpot.elementAt(k));
			} else if (this.i - 1 == ableSpot.elementAt(k).i && this.j == ableSpot.elementAt(k).j) {
				this.neighbors.add(ableSpot.elementAt(k));
			} else if (this.i == ableSpot.elementAt(k).i && this.j - 1 == ableSpot.elementAt(k).j) {
				this.neighbors.add(ableSpot.elementAt(k));
			}
		}
	}

	public boolean equal(Spot spot) {
		if (this.i == spot.i && this.j == spot.j)
			return true;
		return false;
	}

	@Override
	public String toString() {
		return (this.i + ", " + this.j);
	}
}

public class AStar {
	public int width;
	public int height;
	public Spot start;
	public Spot end;
	public Stack<Spot> ableSpot = new Stack<Spot>();
	public Spot[][] grid;
	public Stack<Spot> path = new Stack<Spot>();

	public AStar(int width, int height, Position startPos, Position endPos, ArrayList<Position> groundPos) {
		this.width = width;
		this.height = height;
		this.start = new Spot((int) startPos.x, (int) startPos.y);
		this.end = new Spot((int) endPos.x, (int) endPos.y);

		this.grid = new Spot[width][height];
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				this.grid[i][j] = new Spot(i, j);
			}
		}
		for (int i = 0; i < groundPos.size(); i++) {
			this.ableSpot.push(this.grid[(int) groundPos.get(i).x][(int) groundPos.get(i).y]);
		}
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				this.grid[i][j].addNeighbors(this.ableSpot);
			}
		}
	}

	private double heuristic(Spot spot1, Spot spot2) {
		return Math.abs(spot1.i - spot2.i) + Math.abs(spot1.j - spot2.j);
	}

	private double heuristic2(Spot spot1, Spot spot2) {
		return Math.sqrt((spot1.i - spot2.i) * (spot1.i - spot2.i) + (spot1.j - spot2.j) * (spot1.j - spot2.j));
	}

	private boolean isInclude(Spot spot, ArrayList<Spot> closeSet) {
		for (int x = 0; x < closeSet.size(); x++) {
			if (spot.i == closeSet.get(x).i && spot.j == closeSet.get(x).j)
				return true;
		}
		return false;
	}

	public ArrayList<Position> cal() {
		ArrayList<Spot> openSet = new ArrayList<Spot>();
		ArrayList<Spot> closeSet = new ArrayList<Spot>();
		openSet.add(this.grid[this.start.i][this.start.j]);
		while (!openSet.isEmpty()) {
			int winner = 0;
			for (int i = 0; i < openSet.size(); i++) {
				if (openSet.get(i).f < openSet.get(winner).f) {
					winner = i;
				}
			}
			Spot current = openSet.get(winner);
			if (openSet.get(winner).equal(this.end)) {
				
				Spot cur = this.grid[this.end.i][this.end.j];
				this.path.push(cur);
				
				while (cur.previous != null) {
					this.path.push(cur.previous);
					cur = cur.previous;
				}
				
				Collections.reverse(this.path);
				ArrayList<Position> result = new ArrayList<Position>();
				for (int k = 0; k < this.path.size(); k++) {
					result.add(new Position(this.path.elementAt(k).i, this.path.elementAt(k).j));
				}
				return result;
			}
			openSet.remove(winner);
			closeSet.add(current);
			Stack<Spot> neighbors = current.neighbors;
			for (int i = 0; i < neighbors.size(); i++) {
				Spot neighbor = neighbors.elementAt(i);
				if (!this.isInclude(neighbor, closeSet)) {
					double tempG = current.g + 1;
					if (this.isInclude(neighbor, openSet)) {
						if (tempG < neighbor.g) {
							neighbor.g = tempG;
						}
					} else {
						neighbor.g = tempG;
						openSet.add(neighbor);
					}

					neighbor.h = this.heuristic2(neighbor, this.end);
					neighbor.f = neighbor.h + neighbor.g;
					neighbor.previous = current;
				} else {
					double tempG = current.g + 1;
					if (tempG < neighbor.g) {
						openSet.add(neighbor);
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