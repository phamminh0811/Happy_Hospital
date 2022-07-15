package algorithm;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

import classes.Position;
import socket.Message;
import socket.Path;

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
	private ArrayList<Position> groundPos;
	private Position startPos, endPos;
	public AStar(int width, int height, Position startPos, Position endPos, ArrayList<Position> groundPos) {
		this.width = width;
		this.height = height;
		this.start = new Spot((int) startPos.x, (int) startPos.y);
		this.end = new Spot((int) endPos.x, (int) endPos.y);
		this.groundPos = groundPos;
		this.startPos = startPos;
		this.endPos = endPos;
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
	
	public ArrayList<Position> cal() {
		Path recvMsg = null;
		Socket socket = null;
		try {
			socket = new Socket("localhost", 3100);
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
			Message msg = new Message(52, 28, this.startPos, this.endPos, this.groundPos);
			
			oos.writeObject(msg);
			try {
				recvMsg = (Path) ois.readObject();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ois.close();
			oos.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return recvMsg.path;
	}
}
