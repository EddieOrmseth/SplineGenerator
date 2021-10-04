package SplineGenerator.Applied;

import SplineGenerator.Splines.Spline;
import SplineGenerator.Util.*;

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
     * A DDirection[] for holding the directions at each point in the space that the spline exists in
     */
    private DDirection[] followerGradient;

    /**
     * The Extrema object for holding the bounds of the GradientFollower
     */
    public Extrema bounds;

    /**
     * A DVector for holding the lengths of the GradientFollower's dimensions
     */
    public DVector lengths;

    /**
     * A DVector for holding the lengths of each dimension in the array
     */
    public DVector arrayLengths;

    /**
     * The number used to step when creating the grid
     */
    public double followerStep;

    /**
     * The number that is used as the step value in finding points on the spline
     */
    public double splinePointStep = .005;

    /**
     * A constructor for the FollowerGradient including all the necessary parts
     *
     * @param spline The spline to be followed
     */
    public FollowerGradient(Spline spline) {
        this.spline = spline;
    }

    /**
     * A constructor for the FollowerGradient including all the necessary parts
     *
     * @param spline           The spline to be followed
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
    public DDirection evaluateAt(DPoint point) {
        DPoint pointOnSpline = spline.findClosestPointOnSpline(point, splinePointStep);

        DVector gradient = gradientModifier.get(spline.evaluateDerivative(pointOnSpline.get(point.getDimensions()) - 1, 1));
        DVector distance = distanceModifier.get(new DVector(point, pointOnSpline));

        return gradient.vectorAddition(distance).toDirection();
    }

    /**
     * A method for setting the gradientModifier
     *
     * @param gradientModifier The function to be used
     */
    public void setGradientModifier(Function<DVector, DVector> gradientModifier) {
        this.gradientModifier = gradientModifier;
    }

    /**
     * A method for setting the distanceModifier
     *
     * @param distanceModifier The function to be used
     */
    public void setDistanceModifier(Function<DVector, DVector> distanceModifier) {
        this.distanceModifier = distanceModifier;
    }

    /**
     * A method for setting the bounds of the GradientFollower
     */
    public void setBounds() {
        bounds = spline.getExtrema(splinePointStep);
        lengths = bounds.getVector();
    }

    /**
     * A method for initialing the array that holds the gradientFollower
     */
    public void initializeSpace() {
        setBounds();
        arrayLengths = lengths.clone();
        arrayLengths.multiplyAll(1 / followerStep);

        double[] aLValues = arrayLengths.getValues();
        int totalPoints = 1;
        for (int n = 0; n < spline.matrices.length; n++) {
            aLValues[n] = (int) aLValues[n];
            totalPoints *= aLValues[n];
        }

        followerGradient = new DDirection[totalPoints];
    }

//    public int pointToIndex(DPoint point) {
//
//    }
//
//    public DPoint indexToPoint(int index) {
//
//    }

}
