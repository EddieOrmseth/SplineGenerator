package SplineGenerator.Util;

import java.util.ArrayList;

public class ControlPoint {

    public int t = -1;

    public double x = 0;
    public double y = 0;
    public Direction firstDer = null;
    public ArrayList<Direction> headings;


    public ControlPoint(double x, double y) {
        this.x = x;
        this.y = y;
        headings = new ArrayList<>();
    }

    public ControlPoint(double x, double y, double radians) {
        this(x, y);
        firstDer = new Direction(radians);
    }

}
