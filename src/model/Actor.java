package model;

import java.util.HashSet;
import java.util.Set;

import config.Constant;

public class Actor extends Sprite {
	private static int _id = 0;
	private int agvID;
	private double expectedTime;
	public Set<Actor> collidedActors;
	public double x, y;

	public Actor(double x, double y, String texture) {
		super(x, y, texture);
		if (texture == "agv") {
			Actor._id++;
			this.agvID = Actor._id;
		} else {
			this.agvID = -1;
		}
		this.collidedActors = new HashSet<Actor>();
	}

	public int getAgvID() {
		return this.agvID;
	}

	public double getExpectedTime() {
		return this.expectedTime;
	}

	public void estimateArrivalTime(double startX, double startY, double endX, double endY) {
		this.expectedTime = Math
				.floor(Math.sqrt((endX - startX) * (endX - startX) + (endY - startY) * (endX - startX)) * 0.085);
	}

	public void writeDeadline(TextClass table) {
		if (this.agvID != -1) {
			var enter = "";
			if (table.text.length() > 0)
				enter = "\n";
			table.text = "DES_" + this.agvID + ": " + Constant.secondsToHMS(this.expectedTime) + " ± "
					+ Constant.DURATION + enter + table.text;
		}
	}

	public void eraseDeadline(TextClass table) {
		if (this.agvID != -1) {
			var enter = "";
			if (table.text.length() > 0)
				enter = "\n";
			String erasedStr = "DES_" + this.agvID + ": " + Constant.secondsToHMS(this.expectedTime) + " ± "
					+ Constant.DURATION + enter;
			table.text = table.text.replace(erasedStr, "");
		}
	}

	public void freeze(Actor actor) {
		if (this.collidedActors == null) {
			this.collidedActors = new HashSet<>();
		}
		if (!this.collidedActors.contains(actor)) {
			this.collidedActors.add(actor);
		}
	}

}