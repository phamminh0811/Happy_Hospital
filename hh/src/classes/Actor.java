package classes;

import java.util.ArrayList;

import application.MainScene;
import config.Constant;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class Actor extends Sprite {
	private static int _id = 0;
	private int agvID;
	private double expectedTime = 0;
	public ArrayList<Actor> collidedActors;

	public Actor(MainScene scene, int x, int y, String texture) {
		super(scene, x, y, texture);
		scene.add(this);
		if (texture == "agv") {
			Actor._id++;
			this.agvID = Actor._id;
		} else {
			this.agvID = -1;
		}
		this.collidedActors = new ArrayList<>();
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

	public void writeDeadline(VBox table) {
		if (this.agvID != -1) {
			Label text = new Label(
					"DES_" + this.agvID + ": " + Constant.secondsToHMS(this.expectedTime) + " ± " + Constant.DURATION);
			text.getStyleClass().add("time-table");
			table.getChildren().add(text);
		}
	}

	public void eraseDeadline(VBox table) {
		if (this.agvID != -1) {
			String erasedStr = "DES_" + this.agvID + ": " + Constant.secondsToHMS(this.expectedTime) + " ± "
					+ Constant.DURATION;
			table.getChildren().removeIf(e -> ((Label) e).getText().equals(erasedStr));
		}
	}

	public void freeze(Actor actor) {
		if (collidedActors.contains(actor))
			this.collidedActors.add(actor);
	}

}