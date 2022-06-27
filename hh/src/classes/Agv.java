package classes;

import static config.Config.TILE_HEIGHT;
import static config.Config.TILE_WIDTH;

import java.util.ArrayList;
import java.util.List;

import application.Main;

public class Agv extends Actor {
	private Text text;
	public boolean isImmortal = false;
	private boolean isDisable = false;
	private double desX;
	private double desY;
	private Text invalidToast;
	private Text desText;
	private TilemapLayer pathLayer;
	private double speed = 0.5;

	public Agv(Main scene, int x, int y, int desX, int desY, TilemapLayer pathLayer) {
		super(scene, x, y, "agv");
		this.desX = desX;
		this.desY = desY;
		
		this.text = new Text(this.scene, this.getTranslateX(), this.getTranslateY(), "AGV", "");
		this.text.getStyleClass().add("agv-text");
		
		this.desText = new Text(this.scene, this.desX * 20, this.desY * 20, "DES", "");
		this.desText.getStyleClass().add("agv-des");
		
		this.pathLayer = pathLayer;
		this.estimateArrivalTime(x * TILE_WIDTH, y * TILE_HEIGHT, desX * TILE_WIDTH, desY * TILE_HEIGHT);
	}

	public void ToastinvalidToast() {
		Toast invalidMove = new Toast(this.scene, "Di chuyển không hợp lệ", 3000);
		invalidMove.play();
	}

	public void update() {
		this.velocity = Vector2.zero;
		if (this.isDisable)
			return;
		boolean t = true, l = true, b = true, r = true;
		List<Tile> tiles = this.getTilesWithin();
		for (Tile tile : tiles) {
			if (tile.direction == "up") {
				b = false;
				if (this.scene.keyHandler.down())
					this.ToastinvalidToast();
			}
			if (tile.direction == "left") {
				r = false;
				if (this.scene.keyHandler.right())
					this.ToastinvalidToast();
			}
			if (tile.direction == "down") {
				t = false;
				if (this.scene.keyHandler.up())
					this.ToastinvalidToast();
			}
			if (tile.direction == "right") {
				l = false;
				if (this.scene.keyHandler.left())
					this.ToastinvalidToast();
			}
		}
		
		if (this.scene.keyHandler.up()) {
			if (t) {
				if (this.scene.physics.topCollision(this)) {
					this.setTranslateY((int)Math.floor(this.getTranslateY()/20) * 20);
				} else
					this.setVelocity(Vector2.down.mul(this.speed));
			}
		}
		if (this.scene.keyHandler.down()) {
			if (b) {
				if (this.scene.physics.bottomCollision(this)) {
					this.setTranslateY((int)Math.floor(this.getTranslateY()/20) * 20);
				} else
					this.setVelocity(Vector2.up.mul(this.speed));

			}

		}
		this.moveY();
		if (this.scene.keyHandler.left()) {
			if (l) {
				if (this.scene.physics.leftCollision(this)) {
					this.setTranslateX((int)Math.floor(this.getTranslateX()/20) * 20);
				} else
					this.setVelocity(Vector2.left.mul(this.speed));
			}
		}
		if (this.scene.keyHandler.right()) {
			if (r) {
				if (this.scene.physics.rightCollision(this)) {
					this.setTranslateX((int)Math.floor(this.getTranslateX()/20) * 20);
				} else
					this.setVelocity(Vector2.right.mul(this.speed));
			}

		}
		this.moveX();
		this.text.setPosition(this.getTranslateX(), this.getTranslateY() - 10);
		this.text.move();
	}

	private ArrayList<Tile> getTilesWithin() {
		return this.pathLayer.getTilesWithinXY(this.getTranslateX(), this.getTranslateY(), 19, 19);
	}
}
