package dataEntity;

public class Point{
	
	public double x;
	public double y;
	
	public Point() {
	}
	
	public Point(double x, double y) {
		super();
		this.x = x;
		this.y = y;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "["+x+" "+y+"]";
	}
}