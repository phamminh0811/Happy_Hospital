package classes;

import static config.Config.TILE_HEIGHT;
import static config.Config.TILE_WIDTH;

import java.util.ArrayList;
import java.util.List;

import application.Main;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

public class Tile extends ImageView {
	public String direction;
	public String name;
	public int xPlace = 0;
	public int yPlace = 0;
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
		this.xPlace = x;
		this.yPlace = y;
		this.x = this.xPlace * TILE_WIDTH;
		this.y = this.yPlace * TILE_HEIGHT;
	}

	public void place(GridPane gridPane) {
		gridPane.add(this, xPlace, yPlace);
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public String getDirection() {
		return this.direction;
	}

	@Override
	public String toString() {
		return "Tile [direction=" + direction + ", name=" + name + ", xPlace=" + xPlace + ", yPlace=" + yPlace + ", x="
				+ x + ", y=" + y + "]";
	}

}
