package classes;

import java.util.ArrayList;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;

public class KeyHandler {
	public Scene scene;
	public ArrayList<String> inputList = new ArrayList<String>();

	public KeyHandler(Scene scene) {
		this.scene = scene;
	}
	public void listen() {
		scene.setOnKeyPressed(e -> {
			KeyCode key = e.getCode();
			if (!inputList.contains(key.toString())) {
				inputList.add(key.toString());
				if(key.toString() == "W") inputList.remove("S");
				else if(key.toString() == "S") inputList.remove("W");
				else if(key.toString() == "A") inputList.remove("D");
				else if(key.toString() == "D") inputList.remove("A");
			}
		});
		scene.setOnKeyReleased(e -> {
			KeyCode key = e.getCode();
			inputList.remove(key.toString());
		});
	}
	public boolean up() {
		return this.inputList.contains("W");
	}
	public boolean down() {
		return this.inputList.contains("S");
	}
	public boolean left() {
		return this.inputList.contains("A");
	}
	public boolean right() {
		return this.inputList.contains("D");
	}
}
