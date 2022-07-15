package classes;

import application.MainScene;
import javafx.scene.CacheHint;
import javafx.scene.image.ImageView;

public class Sprite extends ImageView implements IDestroyable{
	public Vector2 velocity;
	public int x;
	public int y;	
	public MainScene scene;
	public boolean active;
	public Sprite(MainScene scene, int x, int y, String type) {
		super("file:sprites/" + type + ".png");
		this.setCache(true);
		this.setCacheHint(CacheHint.SPEED);
		this.x = x;
		this.y = y;
		this.scene = scene;
	}
	public void setVelocity(Vector2 velocity) {
		this.velocity = velocity;
	}
	public void setPosition(double x, double y) {
		this.setTranslateX(x);
		this.setTranslateY(y);
	}
	public Vector2 getPosition() {
		return new Vector2(this.getTranslateX(), this.getTranslateY());
	}
	public void move() {
		moveX();
		moveY();
	}
	public void moveX() {
		this.setTranslateX(this.getTranslateX() + this.velocity.x);		
	}
	public void moveY() {
		this.setTranslateY(this.getTranslateY() + this.velocity.y);
	}
	
	public void destroy() {
		this.scene.destroy(this);
	}
	@Override
	public String toString() {
		return "Sprite [position=" + this.getTranslateX() + " " + this.getTranslateY() + ", velocity=" + velocity + ", scene=" + scene + "]";
	}
}
