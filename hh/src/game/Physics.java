package game;

import application.Main;
import classes.Sprite;
import classes.Tile;
import classes.Vector2;
import javafx.scene.image.ImageView;

public class Physics {
	public Main scene;

	public Physics(Main scene) {
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

	public boolean isOverlap(double x1, double y1, double x2, double y2) {
		boolean noOverlap = x1 + 20 < x2 || x2 + 20 < x1 || y1 + 20 < y2 || y2 + 20 < y1;
		return !noOverlap;
	}

	public boolean isOverlap(ImageView m1, ImageView m2) {
		boolean noOverlap = m1.getTranslateX() + 20 < m2.getTranslateX() || m2.getTranslateX() + 20 < m1.getTranslateX()
				|| m1.getTranslateY() + 20 < m2.getTranslateY() || m2.getTranslateY() + 20 < m1.getTranslateY();

		return !noOverlap;
	}

	public boolean topCollision(Sprite sprite) {
		double x = sprite.getTranslateX();
		double y = sprite.getTranslateY();
		int x_th = (int) x / 20;
		int y_th = (int) y / 20;
		for (int i = -1; i <= 1; i++) {
			Tile tile = this.scene.noPathLayer.tiles2D[x_th + i][y_th - 1];
			if (tile != null) {
				if (isOverlap(x, y, tile.x, tile.y)) {
					if (x == tile.x + 20 || x == tile.x - 20)
						continue;
					else if (y <= tile.y + 20)
						return true;
				}
			}
		}
		return false;
	}

	public boolean bottomCollision(Sprite sprite) {
		double x = sprite.getTranslateX();
		double y = sprite.getTranslateY();
		int x_th = (int) x / 20;
		int y_th = (int) y / 20;
		for (int i = -1; i <= 1; i++) {
			Tile tile = this.scene.noPathLayer.tiles2D[x_th + i][y_th + 1];
			if (tile != null) {
				if (isOverlap(x, y, tile.x, tile.y)) {
					if (x == tile.x + 20 || x == tile.x - 20)
						continue;
					else if (y + 20 >= tile.y)
						return true;
				}
			}
		}
		return false;
	}

	public boolean leftCollision(Sprite sprite) {
		double x = sprite.getTranslateX();
		double y = sprite.getTranslateY();
		int x_th = (int) x / 20;
		int y_th = (int) y / 20;
		for (int i = -1; i <= 1; i++) {
			Tile tile = this.scene.noPathLayer.tiles2D[x_th - 1][y_th + i];
			if (tile != null) {
				if (isOverlap(x, y, tile.x, tile.y)) {
					if (y == tile.y + 20 || y == tile.y - 20)
						continue;
					else if (x <= tile.x + 20)
						return true;
				}
			}
		}
		return false;
	}

	public boolean rightCollision(Sprite sprite) {
		double x = sprite.getTranslateX();
		double y = sprite.getTranslateY();
		int x_th = (int) x / 20;
		int y_th = (int) y / 20;
		for (int i = -1; i <= 1; i++) {
			Tile tile = this.scene.noPathLayer.tiles2D[x_th + 1][y_th + i];
			if (tile != null) {
				if (isOverlap(x, y, tile.x, tile.y)) {
					if (y == tile.y + 20 || y == tile.y - 20)
						continue;
					else if (x + 20 >= tile.x)
						return true;
				}
			}
		}
		return false;
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
