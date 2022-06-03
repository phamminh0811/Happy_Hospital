package model;

import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import static config.Config.*;

import java.util.ArrayList;
import java.util.List;

import application.Main;

public class Agv extends Actor {
	private TextClass text;
	private KeyCode keyW;
	private KeyCode keyA;
	private KeyCode keyS;
	private KeyCode keyD;
	public boolean isImmortal = false;
	private boolean isDisable = false;
	private double desX;
	private double desY;

	private TextClass desText;

	public Agv() {
		super(0, 0, "file:sprites/agv-1.png");
	}

	public Agv(double x, double y, double desX, double desY) {
		super(x, y, "agv");
		this.desX = desX;
		this.desY = desY;
		this.text = new TextClass(this.position.x, this.position.x - this.getBoundary().getHeight() * 0.5, "AGV", 16);
		this.desText = new TextClass(this.desX, this.desY, "DES", 16);
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

	public void ToastInvalidMove() {
		int fontSize = 30;
		Main.context.setFill(Color.BLACK);
		Main.context.setFont(new Font("Comic sans MS", fontSize));
		String text = "Invalid move";
		Main.context.fillText(text, WINDOW_WIDTH / 2 - fontSize * text.length() / 4, WINDOW_HEIGHT - 50);
	}

	public void update() {
		this.velocity = Vector2.zero;
		if (this.isDisable)
			return;
		boolean t = true, l = true, b = true, r = true;
		List<Tile> tiles = this.getTilesWithin();
		for (Tile tile : tiles) {

			if (tile.direction == "top") {
				b = false;
				if (KeyHandler.inputList.contains("DOWN")) {
					ToastOverLay();
				}
			}
			if (tile.direction == "left") {
				r = false;
				if (KeyHandler.inputList.contains("RIGHT"))
					ToastOverLay();

			}
			if (tile.direction == "bottom") {
				t = false;
				if (KeyHandler.inputList.contains("UP"))
					ToastOverLay();

			}
			if (tile.direction == "right") {
				l = false;
				if (KeyHandler.inputList.contains("LEFT"))
					ToastOverLay();

			}
		}
		if (KeyHandler.inputList.contains("UP")) {
			if (t) {
				this.position.y -= 1;
			}
		}
		if (KeyHandler.inputList.contains("LEFT")) {
			if (l) {
				// this.velocity.x = -1;
				this.position.x -= 1;
			}
		}
		if (KeyHandler.inputList.contains("DOWN")) {
			if (b) {
				// this.velocity.y = 1;
				this.position.y += 1;
			}
		}
		if (KeyHandler.inputList.contains("RIGHT")) {
			if (r) {
				// this.velocity.x = 1;
				this.position.x += 1;
			}
		}
	}
}
