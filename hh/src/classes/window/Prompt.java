package classes.window;

import javafx.scene.control.Alert;

public class Prompt extends Alert{
	public Prompt(AlertType type, String title, String msg) {
		super(type);
		this.setTitle(title);
		this.setContentText(msg);
		this.setHeaderText(null);
	}
	public static Prompt error(String title, String msg) {
		return new Prompt(AlertType.ERROR, title, msg);
	}
	public static Prompt confirm(String title, String msg) {
		return new Prompt(AlertType.CONFIRMATION, title, msg);
	}
}
