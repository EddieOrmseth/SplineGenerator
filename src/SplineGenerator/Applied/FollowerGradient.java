package SplineGenerator.Applied;

import SplineGenerator.Splines.Spline;
import SplineGenerator.Util.DPoint;
import SplineGenerator.Util.DVector;
import SplineGenerator.Util.Function;

/**
 * A class representing a gradient such that each value points in the direction of the correct movement
 */
public class FollowerGradient {

    /**
     * The spline to be followed
     */
    private Spline spline;

    /**
     * A Function for modifying the Vector given by the spline
     */
    private Function<DVector, DVector> gradientModifier;

    /**
     * A Function for modifying the DVector from the current point to the nearest point on the spline
     */
    private Function<DVector, DVector> distanceModifier;

    /**
     * A constructor for the FollowerGradient including all the necessary parts
     *
     * @param spline The spline to be followed
     * @param gradientModifier The modifier for the gradient DVector
     * @param distanceModifier The modifier for the distance DVector
     */
    public FollowerGradient(Spline spline, Function<DVector, DVector> gradientModifier, Function<DVector, DVector> distanceModifier) {
        this.spline = spline;
        this.gradientModifier = gradientModifier;
        this.distanceModifier = distanceModifier;
    }

    /**
     * A method for getting the follower DVector at the given point
     *
     * @param point The point to get the DVector for
     * @return The follower DVector
     */
    public DVector evaluateAt(DPoint point) {
        DPoint pointOnSpline = spline.findClosestPointOnSpline(point, .01);

        DVector gradient = gradientModifier.get(spline.evaluateDerivative(pointOnSpline.get(point.getDimensions()) - 1, 1));
        DVector distance = distanceModifier.get(new DVector(point, pointOnSpline));

        return gradient.vectorAddition(distance);
    }

}
