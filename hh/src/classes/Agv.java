package classes;

import static config.Config.TILE_HEIGHT;
import static config.Config.TILE_WIDTH;

import java.util.ArrayList;
import java.util.List;

import application.MainScene;
import classes.game.Physics;
import classes.tilemaps.Tile;
import classes.tilemaps.Tile.Direction;
import classes.tilemaps.TilemapLayer;
import classes.window.Window;

public class Agv extends Actor {
	private XText text;
	public boolean isImmortal = false;
	private boolean isDisable = false;
	public double desX;
	public double desY;
	private XText desText;
	private TilemapLayer pathLayer;
	private double speed = 0.5;

	public Agv(MainScene scene, int x, int y, int desX, int desY, TilemapLayer pathLayer) {
		super(scene, x, y, "agv");
		this.desX = desX;
		this.desY = desY;
		this.text = new XText(this.scene, this.getTranslateX(), this.getTranslateY(), "AGV", "");
		this.text.getStyleClass().add("agv-text");

		this.desText = new XText(this.scene, this.desX * TILE_WIDTH, this.desY * TILE_HEIGHT - 5, "DES", "");
		this.desText.getStyleClass().add("agv-des");

		this.pathLayer = pathLayer;
		this.estimateArrivalTime(x * TILE_WIDTH, y * TILE_HEIGHT, desX * TILE_WIDTH, desY * TILE_HEIGHT);
	}

	public void ToastInvalidToast() {
		Toast invalidMove = new Toast(this.scene, "Di chuyển không hợp lệ", 3000);
		invalidMove.play();
	}

	public void ToastOverLay() {
		Toast overlay = new Toast(this.scene, "AGV va chạm với Agent", 2000);
		overlay.play();
	}

	public void update() {
		this.velocity = Vector2.zero;
		if (this.isDisable)
			return;
		boolean t = true, l = true, b = true, r = true;
		List<Tile> tiles = this.getTilesWithin();
		for (Tile tile : tiles) {
			if (tile.direction == Direction.TOP) {
				b = false;
				if (this.scene.keyHandler.down())
					this.ToastInvalidToast();
			}
			if (tile.direction == Direction.LEFT) {
				r = false;
				if (this.scene.keyHandler.right())
					this.ToastInvalidToast();
			}
			if (tile.direction == Direction.BOTTOM) {
				t = false;
				if (this.scene.keyHandler.up())
					this.ToastInvalidToast();
			}
			if (tile.direction == Direction.RIGHT) {
				l = false;
				if (this.scene.keyHandler.left())
					this.ToastInvalidToast();
			}
		}
		
		for (AutoAgv a : this.scene.autoAgvs) {
			if (Physics.isOverlap(this.getTranslateX(), this.getTranslateY(), a.getTranslateX(), a.getTranslateY())) {
				if (this.getTranslateX() ==  a.getTranslateX() + TILE_WIDTH || this.getTranslateX() + TILE_WIDTH ==  a.getTranslateX())
					continue;
				else if (this.getTranslateY() <= a.getTranslateY() + TILE_HEIGHT)
					t = false;
				else if (this.getTranslateY() + TILE_HEIGHT >=  a.getTranslateY())
					b = false;
				
				if (this.getTranslateY() == a.getTranslateY() + TILE_HEIGHT || this.getTranslateY()  + TILE_HEIGHT == a.getTranslateY())
					continue;
				else if (this.getTranslateX() <= a.getTranslateX() + TILE_WIDTH )
					l = false;	
				else if (this.getTranslateX() + TILE_WIDTH >= a.getTranslateX())
					r = false;
			}
			
			if (Physics.isOverlap(this.getTranslateX(), this.getTranslateY(), a.getTranslateX(), a.getTranslateY())) {
				if (this.getTranslateX() ==  a.getTranslateX() + TILE_WIDTH || this.getTranslateX() + TILE_WIDTH ==  a.getTranslateX())
					continue;
				else if (this.getTranslateY() <= a.getTranslateY() + TILE_HEIGHT)
					l = false;
			}
			if (this.getTranslateX() + 20 >= a.getTranslateX() 
					&& a.getTranslateY()-20 <= this.getTranslateY() 
					&& this.getTranslateY() <= a.getTranslateY()+20 
					) {
				r = false;
			}
			if (this.getTranslateX() - 20 <= a.getTranslateX() 
					&& a.getTranslateY()-20 <= this.getTranslateY() 
					&& this.getTranslateY() <= a.getTranslateY()+20 
					) {
				l = false;
			}
			if (this.getTranslateY() - 20 >= a.getTranslateY() 
					&& a.getTranslateX() <= this.getTranslateX()-20 
					&& this.getTranslateX()+20 <= a.getTranslateX()
					) {
				t = false;
			}
			if (this.getTranslateY() + 20 <= a.getTranslateY() 
					&& a.getTranslateX()-20 <= this.getTranslateX()
					&& this.getTranslateX()+20 <= a.getTranslateX()
					) {
				b = false;
			}
		}

		if (this.scene.keyHandler.up()) {
			if (t) {
				if (this.scene.physics.topCollision(this)) {
					this.setTranslateY((int) Math.floor(this.getTranslateY() / TILE_HEIGHT) * TILE_HEIGHT);
				} else
					this.setVelocity(Vector2.down.mul(this.speed));
			}
		}
		if (this.scene.keyHandler.down()) {
			if (b) {
				if (this.scene.physics.bottomCollision(this)) {
					this.setTranslateY((int) Math.floor(this.getTranslateY() / TILE_HEIGHT) * TILE_HEIGHT);
				} else
					this.setVelocity(Vector2.up.mul(this.speed));

			}

		}
		this.moveY();
		if (this.scene.keyHandler.left()) {
			if (l) {
				if (this.scene.physics.leftCollision(this)) {
					this.setTranslateX((int) Math.floor(this.getTranslateX() / TILE_WIDTH) * TILE_WIDTH);
				} else
					this.setVelocity(Vector2.left.mul(this.speed));
			}
		}
		if (this.scene.keyHandler.right()) {
			if (r) {
				if (this.scene.physics.rightCollision(this)) {
					this.setTranslateX((int) Math.floor(this.getTranslateX() / TILE_WIDTH) * TILE_WIDTH);
				} else
					this.setVelocity(Vector2.right.mul(this.speed));
			}

		}
		this.moveX();
		this.text.setPosition(this.getTranslateX(), this.getTranslateY() - TILE_HEIGHT/2);
		this.text.move();
		this.handleOverlap();
	}

	private ArrayList<Tile> getTilesWithin() {
		return this.pathLayer.getTilesWithinXY(this.getTranslateX(), this.getTranslateY(), 31, 31);
	}

	public void handleOverlap() {
		this.scene.agents.forEach(p -> {
			if (this.scene.physics.isOverlap(p, this)) {
				this.ToastOverLay();
				if (!this.isImmortal) {
					this.isDisable = true;
					Window.setTimeout(new LambdaExpression() {
						@Override
						public void expression() {
							isImmortal = true;
							isDisable = false;
							Window.setTimeout(new LambdaExpression() {
								@Override
								public void expression() {
									isImmortal = false;

								}
							}, 2000);
						}
					}, 1000);
				}
			}
		});
	}
}
