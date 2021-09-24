package SplineGenerator.Util;

import java.util.ArrayList;

/**
 * A class holding all the information necessary for a control point in a spline
 */
public class ControlPoint {

    /**
     * The index of the ControlPoint, where does it fall on the spline
     */
    public int t = -1;

    /**
     * The x-value of the ControlPoint
     */
    public double x;

    /**
     * The y-value of the ControlPoint
     */
    public double y;

    /**
     * An ArrayList<Direction> for holding the values of all the derivatives at the control point
     */
    public ArrayList<Direction> headings;

    /**
     * A constructor that sets all the necessary values of the ControlPoint
     *
     * @param x        The x position of the point
     * @param y        The y position of the point
     * @param headings An array of the nth derivatives, in radians
     */
    public ControlPoint(double x, double y, double... headings) {
        this.x = x;
        this.y = y;
        this.headings = new ArrayList<>();
        for (int i = 0; i < headings.length; i++) {
            this.headings.add(new Direction(headings[i]));
        }
    }

}
