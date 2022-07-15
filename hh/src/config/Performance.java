package config;

import application.MainScene;

public class Performance {
	public static double now() {
		return System.currentTimeMillis() - MainScene.startTimeMilis;
	}
}
