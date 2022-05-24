package model;

import javafx.scene.paint.Color;

public class Tile extends Sprite{
	public String direction;
	public Tile(double x, double y, double width, double height, Color color, String direction) {
		super(x, y, width, height, color);
		this.direction = direction;
	}
	public Tile(double x, double y, String fileName, String direction) {
		super(x, y, fileName);
		this.direction = direction;
	}
}
