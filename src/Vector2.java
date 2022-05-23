
public class Vector2 {
	public static double distance(Vector2 a, Vector2 b) {
		return Math.sqrt(Math.pow(a.x-b.x, 2)+Math.pow(a.y-b.y, 2));
	}
	public static double magnitude(Vector2 a) {
		return Math.sqrt(Math.pow(a.x, 2)+Math.pow(a.y, 2));
	}
	public static Vector2 zero = new Vector2(0, 0);
	
	public static Vector2 up = new Vector2(0, 1);
	public static Vector2 down = new Vector2(0, -1);
	public static Vector2 left = new Vector2(-1, 0);
	public static Vector2 right = new Vector2(1, 0);
	
	public double x, y;
	public Vector2(double x, double y) {
		this.x = x;
		this.y = y;
	}
	public Vector2 add(Vector2 b) {
		return new Vector2(x+b.x, y+b.y);
	}
	public Vector2 sub(Vector2 b) {
		return new Vector2(x-b.x, y-b.y);
	}
	public Vector2 mul(double b) {
		return new Vector2(x*b, y*b);
	}
	public Vector2 div(double b) {
		return new Vector2(x/b, y/b);
	}
	public Vector2 normalize() {
		return this.div(Vector2.magnitude(this));
	}
	@Override
	public boolean equals(Object other) {
		if(other == this) return true;
		if(!(other instanceof Vector2)) return true;
		Vector2 _other = (Vector2)other;
		return this.x==_other.x && this.y==_other.y;
	}
	@Override
	public String toString() {
		return "(x, y) = (" + this.x + ", " +this.y +")";
	}
}