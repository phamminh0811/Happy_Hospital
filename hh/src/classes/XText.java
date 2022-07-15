package classes;

import application.MainScene;
import javafx.scene.text.Text;

public class XText extends Text implements IDestroyable{
    public int i;
    public int j;
    private Vector2 position;
    public MainScene scene;

    public XText(MainScene scene, double x, double y, String text, String style) {
        super(text);
        this.setStyle(style);
        this.scene = scene;
        this.position = new Vector2(x, y);
        this.scene.add(this);
    }
    public XText(MainScene scene, int x, int y, String text, String style) {
        super(text);
        this.setStyle(style);
        i = x;
        j = y;
        this.scene = scene;
        this.position = new Vector2(x, y);
        this.scene.add(this);
    }
    public void setPosition(double x, double y) {
    	this.position = new Vector2(x, y);
    	this.setTranslateX(x);
    	this.setTranslateY(y);
    }
    public void move() {
        this.setTranslateX(this.position.x);
        this.setTranslateY(this.position.y);
    }
    public void destroy() {
    	this.scene.destroy(this);
    }
}