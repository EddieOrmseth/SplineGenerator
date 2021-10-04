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
     * A DVector for translating the given point into the first quadrant, only the first quadrant is used in the follower.
     * This DVector moves the lowerExtrema to the origin
     */
    public DVector translation;

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

        return gradient.add(distance).toDirection();
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
     * A method that sets the bounds object to the extrema of the spline
     */
    public void setBoundsAsExtrema() {
        bounds = spline.getExtrema(splinePointStep);
    }

    /**
     * A method for preparing necessary variables of the FollowerGradient
     */
    public void prepareBounds() {
        lengths = bounds.getVector();
        translation = bounds.lesserPoint.toVector();
        translation.multiplyAll(-1);
    }

    /**
     * A method for initializing the array that holds the FollowerGradient
     */
    public void initializeSpace() {
        prepareBounds();
        arrayLengths = lengths.clone();
        arrayLengths.multiplyAll(1 / followerStep);

        int totalPoints = 1;
        for (int n = 0; n < spline.matrices.length; n++) {
            totalPoints *= (int) arrayLengths.get(n);
        }

        followerGradient = new DDirection[totalPoints];
    }

    /**
     * A method for creating the FollowerGradient
     */
    public void computeGradient() {
        initializeSpace();
        for (int i = 0; i < followerGradient.length; i++) {
            followerGradient[i] = evaluateAt(indexToPoint(i));
        }
    }

    /**
     * A method for getting the correct index of a given point in the followerGradient
     *
     * @param point The point to find the index of
     * @return The index of the given point
     */
    public int pointToIndex(DPoint point) {
        point.add(translation);
        point.multiplyAll(1 / followerStep);
        int index = 0;
        int subDimensionVolume;
        for (int n = 0; n < spline.getDimensions(); n++) {
            subDimensionVolume = 1;
            for (int s = n + 1; s < spline.getDimensions(); s++) {
                subDimensionVolume *= (int) arrayLengths.get(n);
            }
            index += point.get(n) * subDimensionVolume;
        }

        return index;
    }

    /**
     * A method for getting the DPoint that an index represents
     *
     * @param index The index to find the DPoint for
     * @return The DPoint that the index represents
     */
    public DPoint indexToPoint(int index) {
        DPoint point = new DPoint(spline.getDimensions());

        int remaining = index;

        int subDimensionVolume;
        for (int n = 0; n < spline.getDimensions(); n++) {
            subDimensionVolume = 1;
            for (int s = n + 1; s < spline.getDimensions(); s++) {
                subDimensionVolume *= (int) arrayLengths.get(n);
            }
            point.set(n, remaining / subDimensionVolume);
            remaining = remaining % subDimensionVolume;
        }

        return point;
    }

    /**
     * A method for getting the precomputed direction at the specified point
     *
     * @param point The point at which to get the DDirection
     * @return The DDirection at the given point
     */
    public DDirection get(DPoint point) {
        return followerGradient[pointToIndex(point)];
    }
}
