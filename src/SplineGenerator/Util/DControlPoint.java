package SplineGenerator.Util;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A class holding all the information necessary for a control point in a spline
 */
public class DControlPoint {

    /**
     * The index of the ControlPoint, where does it fall on the spline
     */
    public int t = -1;

    /**
     * An ArrayList<DVector> for holding the values of all the derivatives at the control point
     */
    public ArrayList<DVector> values;

    /**
     * A constructor that sets all the necessary values of the ControlPoint
     *
     * @param values An array of the nth derivatives, in radians
     */
    public DControlPoint(DVector... values) {
        this.values = new ArrayList<>();
        this.values.addAll(Arrays.asList(values));
    }

}
