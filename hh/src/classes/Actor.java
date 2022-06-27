package classes;

import java.util.HashSet;
import java.util.Set;

import application.Main;
import config.Constant;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

public class Actor extends Sprite {
	private static int _id = 0;
	private int agvID;
	private double expectedTime;
	public Set<Actor> collidedActors;

	public Actor(Main scene, int x, int y, String texture) {
		super(scene, x, y, texture);
		scene.add(this);
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

	public void writeDeadline(VBox table) {
		if (this.agvID != -1) {
			Label text = new Label(
					"DES_" + this.agvID + ": " + Constant.secondsToHMS(this.expectedTime) + " ± " + Constant.DURATION);
			text.setStyle("-fx-font-size: 14");
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
		if (this.collidedActors == null) {
			this.collidedActors = new HashSet<>();
		}
		if (!this.collidedActors.contains(actor)) {
			this.collidedActors.add(actor);
		}
	}

}