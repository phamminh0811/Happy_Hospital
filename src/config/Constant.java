package config;

import java.util.Set;

import model.Actor;
import model.Node2D;

public class Constant {

	public static enum ModeOfPathPlanning {
		FRANSEN, PROPOSE
	}

	public static double DURATION = 4;
	public static double SAFE_DISTANCE = 46;

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
		var h = Math.floor(seconds % (3600 * 24) / 3600);
		var m = Math.floor(seconds % 3600 / 60);
		var s = Math.floor(seconds % 60);

		var hDisplay = h >= 10 ? h : ("0" + h);
		var mDisplay = m >= 10 ? m : ("0" + m);
		var sDisplay = s >= 10 ? s : ("0" + s);
		return hDisplay + ":" + mDisplay + ":" + sDisplay;
	}

	public static boolean validDestination(double destX, double destY, double x, double y) {
		if ((destY == 14 || destY == 13) && ((destX >= 0 && destX <= 5) || (destX >= 45 && destX <= 50)))
			return false;
		var d = Math.sqrt((destX - x) * (destX - x) + (destY - y) * (destY - y));
		if (d * 32 < 10)
			return false;
		return true;
	}

	public static double minDistance(Actor actor, Set<Actor> otherActors) {
		double dist = Double.POSITIVE_INFINITY;
		for (Actor element : otherActors) {
			double smaller = Math.sqrt(
					(element.x - actor.x) * (element.x - actor.x) + (element.y - actor.y) * (element.y - actor.y));
			if (dist > smaller)
				dist = smaller;
		}
		return dist;
	}
	public static double numberOfEdges(int width, int height, Node2D[][] nodes) {
        int count = 0;
        for(int i = 0; i < width; i++) {
            for(int j = 0; j < height; j++) {
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
