package model;

import java.util.ArrayList;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;

public class KeyHandler {
	public Scene scene;
	public static ArrayList<String> inputList = new ArrayList<String>();

	public KeyHandler(Scene scene) {
		this.scene = scene;
	}
	public void listen() {
		scene.setOnKeyPressed(e -> {
			KeyCode key = e.getCode();
			if (!inputList.contains(key.toString()))
				inputList.add(key.toString());
		});
		scene.setOnKeyReleased(e -> {
			KeyCode key = e.getCode();
			inputList.remove(key.toString());
		});
	}
}
