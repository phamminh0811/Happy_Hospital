package socket;

import java.io.Serializable;
import java.util.ArrayList;

import classes.Node2D;
import classes.Position;

public class Message implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int width;
	private int height;
	private Position startPos;
	private Position endPos;
	private ArrayList<Position> groundPos;
	public Message(int width, int height, Position startPos, Position endPos, ArrayList<Position> groundPos) {
		this.width = width;
		this.height = height;
		this.startPos = startPos;
		this.endPos = endPos;
		this.groundPos = groundPos;
	}
	
	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public Position getStartPos() {
		return startPos;
	}

	public void setStartPos(Position startPos) {
		this.startPos = startPos;
	}

	public Position getEndPos() {
		return endPos;
	}

	public void setEndPos(Position endPos) {
		this.endPos = endPos;
	}

	public ArrayList<Position> getGroundPos() {
		return groundPos;
	}

	public void setGroundPos(ArrayList<Position> groundPos) {
		this.groundPos = groundPos;
	}

	@Override
	public String toString() {
		return "Message [width=" + width + ", height=" + height + ", start=" + startPos + ", end=" + endPos + "]";
	}
}