package classes.game;

import application.MainScene;
import classes.LambdaExpression;
import classes.Sprite;
import classes.Vector2;
import classes.tilemaps.Tile;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import static config.Config.*;

public class Physics {
	public MainScene scene;

	public Physics(MainScene scene) {
		this.scene = scene;
	}

	public void moveTo(Sprite sprite, Vector2 des, double speed) {
		Vector2 dir = des.sub(sprite.getPosition());
		double d = Vector2.magnitude(dir);
		if (d > 1e-4) {
			dir = dir.normalize();
			sprite.velocity = dir.mul(speed);
		} else
			sprite.velocity = Vector2.zero;
		sprite.move();
	}

	public static boolean isOverlap(double x1, double y1, double x2, double y2) {
		boolean noOverlap = x1 + TILE_WIDTH < x2 || x2 + TILE_WIDTH < x1 || y1 + TILE_HEIGHT < y2 || y2 + TILE_HEIGHT < y1;
		return !noOverlap;
	}

	public static boolean isOverlap(ImageView m1, ImageView m2) {
		boolean noOverlap = m1.getTranslateX() + TILE_WIDTH < m2.getTranslateX() || m2.getTranslateX() + TILE_WIDTH < m1.getTranslateX()
				|| m1.getTranslateY() + TILE_HEIGHT < m2.getTranslateY() || m2.getTranslateY() + TILE_HEIGHT < m1.getTranslateY();

		return !noOverlap;
	}

	public boolean topCollision(Sprite sprite) {
		double x = sprite.getTranslateX();
		double y = sprite.getTranslateY();
		int x_th = (int) x / TILE_WIDTH;
		int y_th = (int) y / TILE_HEIGHT;
		for (int i = -1; i <= 1; i++) {
			Tile tile = this.scene.noPathLayer.tiles2D[x_th + i][y_th - 1];
			if (tile != null) {
				if (isOverlap(x, y, tile.x, tile.y)) {
					if (x == tile.x + TILE_WIDTH || x + TILE_WIDTH == tile.x)
						continue;
					else if (y <= tile.y + TILE_HEIGHT)
						return true;
				}
			}
		}
		return false;
	}

	public boolean bottomCollision(Sprite sprite) {
		double x = sprite.getTranslateX();
		double y = sprite.getTranslateY();
		int x_th = (int) x / TILE_WIDTH;
		int y_th = (int) y / TILE_HEIGHT;
		for (int i = -1; i <= 1; i++) {
			Tile tile = this.scene.noPathLayer.tiles2D[x_th + i][y_th + 1];
			if (tile != null) {
				if (isOverlap(x, y, tile.x, tile.y)) {
					if (x == tile.x + TILE_WIDTH || x + TILE_WIDTH == tile.x)
						continue;
					else if (y + TILE_HEIGHT >= tile.y)
						return true;
				}
			}
		}
		return false;
	}

	public boolean leftCollision(Sprite sprite) {
		double x = sprite.getTranslateX();
		double y = sprite.getTranslateY();
		int x_th = (int) x / TILE_WIDTH;
		int y_th = (int) y / TILE_HEIGHT;
		for (int i = -1; i <= 1; i++) {
			Tile tile = this.scene.noPathLayer.tiles2D[x_th - 1][y_th + i];
			if (tile != null) {
				if (isOverlap(x, y, tile.x, tile.y)) {
					if (y == tile.y + TILE_HEIGHT || y + TILE_HEIGHT == tile.y)
						continue;
					else if (x <= tile.x + TILE_WIDTH)
						return true;
				}
			}
		}
		return false;
	}

	public boolean rightCollision(Sprite sprite) {
		double x = sprite.getTranslateX();
		double y = sprite.getTranslateY();
		int x_th = (int) x / TILE_WIDTH;
		int y_th = (int) y / TILE_HEIGHT;
		for (int i = -1; i <= 1; i++) {
			Tile tile = this.scene.noPathLayer.tiles2D[x_th + 1][y_th + i];
			if (tile != null) {
				if (isOverlap(x, y, tile.x, tile.y)) {
					if (y == tile.y + TILE_HEIGHT || y + TILE_HEIGHT == tile.y)
						continue;
					else if (x + TILE_WIDTH >= tile.x)
						return true;
				}
			}
		}
		return false;
	}

	public void addOverlap(ImageView o1, ImageView o2, LambdaExpression e) {
		Timeline update = new Timeline(new KeyFrame(Duration.millis(1000.0/60), p->{
			if (isOverlap(o1, o2)) {
				e.expression();
			}
		}));
		update.setCycleCount(Animation.INDEFINITE);
		update.play();
	}

	public void update() {
		this.scene.agents.forEach(e -> {
			e.preUpdate();
		});
		this.scene.autoAgvs.forEach(e -> {
			e.preUpdate(1000, 1000);
		});
	}
}
