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

    /**
     * A constructor that sets all the necessary values of the ControlPoint
     * @param x The x position of the point
     * @param y The y position of the point
     * @param headings Direction of the nth derivative, in radians
     */
    public ControlPoint(double x, double y, double... headings) {
        this(x, y);
//        firstDer = new Direction(radians);
//        headings.add(firstDer);
        for (int i = 0; i < headings.length; i++) {
            this.headings.add(new Direction(headings[i]));
        }
    }

}
