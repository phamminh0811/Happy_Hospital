package classes.tilemaps;

import static config.Config.TILE_HEIGHT;
import static config.Config.TILE_WIDTH;

import java.util.ArrayList;
import java.util.List;

import application.MainScene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

public class Tile extends ImageView {
	public enum Direction{
		TOP, BOTTOM, LEFT, RIGHT, NONE
	}
	public Direction direction;
	public String name;
	public int tileX = 0;
	public int tileY = 0;
	public double x;
	public double y;

	public Tile(Image img, String name) {
		super(img);
		this.name = name;
	}
	public Tile(int x, int y, Image img, String name) {
		this(img, name);
		this.setPosition(x, y);
	}
	public void setPosition(int x, int y) {
		this.tileX = x;
		this.tileY = y;
		this.x = this.tileX * TILE_WIDTH;
		this.y = this.tileY * TILE_HEIGHT;
	}

	public void place(GridPane gridPane) {
		gridPane.add(this, tileX, tileY);
	}

	@Override
	public String toString() {
		return "Tile [direction=" + direction + ", name=" + name + ", tileX=" + tileX + ", tileY=" + tileY + ", x="
				+ x + ", y=" + y + "]";
	}

}
