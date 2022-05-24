package model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class Sprite {
	public Vector2 position;
	public Vector2 velocity;
	private Image image;
	private Rectangle collider = new Rectangle();
	private Color color;
	private double width, height;

	public Sprite(double x, double y, double width, double height, Color color) {
		position = new Vector2(x, y);
		this.width = width;
		this.height = height;
		this.color = color;
		this.collider = new Rectangle(x, y, width, height);
	}

	public Sprite(double x, double y, String fileName) {
		position = new Vector2(x, y);
		setImage(fileName);
	}

	public void setPosition(Vector2 position) {
		this.position = position;
		collider.setX(position.x);
		collider.setY(position.y);
	}

	public void setImage(String fileName) {
		if (color == null) {
			image = new Image(fileName);
			collider = new Rectangle(position.x, position.y, 16, 16);
		}
	}

	public void setColor(Color color) {
		if (image == null)
			this.color = color;
	}

	public Rectangle getBoundary() {
		return collider;
	}

	public void render(GraphicsContext context) {
		if (image != null)
			context.drawImage(image, position.x, position.y, 16, 16);
		else if (color != null) {
			Paint prev = context.getFill();
			context.setFill(color);
			context.fillRect(position.x, position.y, width, height);
			context.setFill(prev);
		}
	}

	public void moveTo(Vector2 des, double speed) {
		Vector2 dir = des.sub(this.position);
		this.setPosition(this.position.add(dir.mul(speed)));
	}

	@Override
	public String toString() {
		return "(" + collider.getWidth() + ", " + collider.getHeight() + ")";
	}

	public boolean overlap(Sprite other) {
		boolean noOverlap = this.position.x + this.collider.getWidth() < other.position.x
				|| other.position.x + other.collider.getWidth() < this.position.x
				|| this.position.y + this.collider.getHeight() < other.position.y
				|| other.position.y + other.collider.getHeight() < this.position.y;
		return !noOverlap;
	}

}
