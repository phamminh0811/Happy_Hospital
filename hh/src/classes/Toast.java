package classes;

import static config.Config.WINDOW_HEIGHT;

import application.MainScene;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.util.Duration;

public class Toast {
	MainScene scene;
	Timeline timeLine;

	public Toast(MainScene scene, String msg, long duration) {
		this.scene = scene;
		Label toast = new Label(msg);
		toast.setTranslateY(WINDOW_HEIGHT / 2 - 100);
		toast.getStyleClass().add("toast");
		toast.setOpacity(0);
		this.scene.stackPane.getChildren().add(toast);

		this.timeLine = new Timeline();
		this.timeLine.setCycleCount(2);
		this.timeLine.setAutoReverse(true);
		KeyValue start = new KeyValue(toast.opacityProperty(), 1);
		KeyValue showing = new KeyValue(toast.opacityProperty(), 1);
		KeyFrame frame1 = new KeyFrame(Duration.millis(duration/3), start);
		KeyFrame frame2 = new KeyFrame(Duration.millis(duration/6), showing);
		this.timeLine.getKeyFrames().addAll(frame1, frame2);
	}

	public void play() {
		this.timeLine.play();
		this.clear();
	}
	public void clear() {
		this.scene.stackPane.getChildren().remove(this);
	}

}
