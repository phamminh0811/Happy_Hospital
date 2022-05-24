package model;

import javafx.scene.input.KeyCode;
import static config.Config.*;

import java.util.ArrayList;
import java.util.List;

import application.Main;

public class Agv extends Actor {
	private Text text;
	private KeyCode keyW;
	private KeyCode keyA;
	private KeyCode keyS;
	private KeyCode keyD;
	public boolean isImmortal = false;
	private boolean isDisable = false;
	private double desX;
	private double desY;

	private Text desText;

	public Agv() {
		super(0, 0, "file:sprites/agv-1.png");
	}

	public Agv(double x, double y, double desX, double desY) {
		super(x, y, "agv");
		this.desX = desX;
		this.desY = desY;
		this.text = new Text(this.position.x, this.position.x - this.getBoundary().getHeight() * 0.5, "AGV", 16);
		this.desText = new Text(this.desX, this.desY, "DES", 16);
		this.estimateArrivalTime(x, y, desX, desY);
	}

	private List<Tile> getTilesWithin() {
		List<Tile> tilesWithin = new ArrayList<>();
		int x_th = (int) (this.position.y / TILE_HEIGHT);
		int y_th = (int) (this.position.x / TILE_WIDTH);
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				Tile t = Main.tiles[x_th + i][y_th + j];
				if (this.overlap(t))
					tilesWithin.add(t);
			}
		}
		return tilesWithin;
	}

	public void update() {
		this.velocity = Vector2.zero;
		if (this.isDisable)
			return;
		boolean t = true, l = true, b = true, r = true;
		List<Tile> tiles = this.getTilesWithin();
		for (Tile tile: tiles) {
			if (tile.direction == "any") {
				b = true;
				t = true;
				l = true;
				r = true;
				System.out.println("any");
			}
			if (tile.direction == "top") {
				b = false;
				if (KeyHandler.inputList.contains("DOWN"))
					System.out.println("invalid move");
				System.out.println("top");
			}
			if (tile.direction == "left") {
				r = false;
				if (KeyHandler.inputList.contains("RIGHT"))
					System.out.println("invalid move");
				System.out.println("left");
			}
			if (tile.direction == "bottom") {
				t = false;
				if (KeyHandler.inputList.contains("UP"))
					System.out.println("invalid move");
				System.out.println("bottom");
			}
			if (tile.direction == "right") {
				l = false;
				if (KeyHandler.inputList.contains("LEFT"))
					System.out.println("invalid move");
				System.out.println("right");
			}
		}
		if (KeyHandler.inputList.contains("UP")) {
			this.velocity.y = t ? (-1) : 0;
		} else
			this.velocity.y = 0;
		if (KeyHandler.inputList.contains("LEFT")) {
			this.velocity.x = l ? (-1) : 0;
		} else
			this.velocity.x = 0;
		if (KeyHandler.inputList.contains("DOWN")) {
			this.velocity.y = b ? (1) : 0;
		} else
			this.velocity.y = 0;
		if (KeyHandler.inputList.contains("RIGHT")) {
			this.velocity.x = r ? (1) : 0;
		} else
			this.velocity.x = 0;
		this.setPosition(this.position.add(this.velocity));
	}

}
