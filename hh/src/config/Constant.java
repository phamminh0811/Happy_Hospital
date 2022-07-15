package config;

import java.util.ArrayList;
import java.util.Set;

import classes.Actor;
import classes.Node2D;
import classes.Vector2;

public class Constant {
	public static enum ModeOfPathPlanning {
		FRANSEN, PROPOSE
	}

	public final static double DURATION = 4;
	public final static double SAFE_DISTANCE = 46;

	public static double getLateness(double x) {
		return 5 * x;
	}

	public static double DELTA_T() {
		return 10;
	}

	public static ModeOfPathPlanning MODE() {
		return ModeOfPathPlanning.FRANSEN;
	}

	public static String secondsToHMS(double seconds) {
		int h = (int) Math.floor(seconds % (3600 * 24) / 3600);
		int m = (int) Math.floor(seconds % 3600 / 60);
		int s = (int) Math.floor(seconds % 60);

		String hDisplay = h >= 10 ? String.valueOf(h) : ("0" + h);
		String mDisplay = m >= 10 ? String.valueOf(m) : ("0" + m);
		String sDisplay = s >= 10 ? String.valueOf(s) : ("0" + s);
		return hDisplay + ":" + mDisplay + ":" + sDisplay;
	}

	public static boolean validDestination(double destX, double destY, int x, int y){
        if((destY == 14 || destY == 13) && ((destX >= 0 && destX <= 5) || (destX >= 45 && destX <= 50)))
            return false; 
        var d = Math.sqrt((destX - x)*(destX - x) + (destY - y)*(destY-y));
        if(d*32 < 10)
            return false;
        return true;
    }

	public static double minDistance(Actor actor,  ArrayList<Actor> otherActors) {
		double dist = Double.POSITIVE_INFINITY;
		for (Actor element : otherActors) {
			double smaller = Vector2.distance(actor.getPosition(), element.getPosition());
			if (dist > smaller)
				dist = smaller;
		}
		return dist;
	}

	public static double numberOfEdges(int width, int height, Node2D[][] nodes) {
		int count = 0;
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				count += (nodes[i][j].nodeE != null) ? 1 : 0;
				count += (nodes[i][j].nodeS != null) ? 1 : 0;
				count += (nodes[i][j].nodeW != null) ? 1 : 0;
				count += (nodes[i][j].nodeN != null) ? 1 : 0;
				count += (nodes[i][j].nodeVE != null) ? 1 : 0;
				count += (nodes[i][j].nodeVS != null) ? 1 : 0;
				count += (nodes[i][j].nodeVW != null) ? 1 : 0;
				count += (nodes[i][j].nodeVN != null) ? 1 : 0;
			}
		}
		return count;
	}
}
