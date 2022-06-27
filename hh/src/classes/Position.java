package classes;

public class Position {
	public double x, y;
	public Position(double x, double y) {
		this.x = x;
		this.y = y;
	}
	static double between(Position x, Position y) {
		return Math.sqrt((x.x - y.x) * (x.x - y.x) + (x.y - y.y) * (x.y - y.y));
	}
	@Override
	public String toString() {
		return "Position [x=" + x + ", y=" + y + "]";
	}
	
}
