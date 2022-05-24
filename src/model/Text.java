package model;

public class Text {
	private double x, y;
	public String text;
	private int fontSize;

	public Text(double x, double y, String text, int fontSize) {
		this.x = x;
		this.y = y;
		this.text = text;
		this.fontSize = fontSize;
	}
	public void setText(String text) {
		this.text = text;
	}
	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}
	public void setX(double x) {
		this.x = x;
	}
	public void setY(double y) {
		this.y = y;
	}
}
