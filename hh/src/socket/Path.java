package socket;

import java.io.Serializable;
import java.util.ArrayList;

import classes.Node2D;
import classes.Position;

public class Path implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public ArrayList<Position> path;
	public Path(ArrayList<Position> path) {
		this.path = path;
	}
}
