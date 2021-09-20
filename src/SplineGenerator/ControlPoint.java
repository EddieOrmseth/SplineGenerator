package SplineGenerator;

public class ControlPoint {

    public int t = -1;

    public double x = 0;
    public double y = 0;
    public Direction heading = null;

    public ControlPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public ControlPoint(double x, double y, double radians) {
        this(x, y);
        heading = new Direction(radians);
    }

}
