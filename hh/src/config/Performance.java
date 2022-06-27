package config;

import application.Main;

public class Performance {
	public static double now() {
		return System.currentTimeMillis() - Main.startTimeMilis;
	}
}
