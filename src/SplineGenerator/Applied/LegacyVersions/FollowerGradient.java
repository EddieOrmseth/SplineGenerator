package SplineGenerator.Applied.LegacyVersions;

import SplineGenerator.Applied.Navigator;
import SplineGenerator.Splines.Spline;
import SplineGenerator.Util.*;

/**
 * A class representing a gradient such that each value points in the direction of the correct movement
 */
public class FollowerGradient implements Navigator {

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
    public double followerStep = .25;

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
        DPoint pointOnSpline = spline.findClosestPointOnSpline(point.clone(), splinePointStep);
        DVector position = new DVector(point.clone(), pointOnSpline);
        position = distanceModifier.get(position);

        DVector derivative = spline.evaluateDerivative(pointOnSpline.get(pointOnSpline.getDimensions() - 1), 1);
        derivative = gradientModifier.get(derivative);

        return derivative.add(position).toDirection();
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
        lengths.addAll(1);
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
        for (int n = 0; n < arrayLengths.getDimensions(); n++) {
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
                subDimensionVolume *= (int) arrayLengths.get(s);
            }
            index += ((int) point.get(n)) * subDimensionVolume;
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
                subDimensionVolume *= (int) arrayLengths.get(s);
            }
            point.set(n, remaining / subDimensionVolume);
            remaining = remaining % subDimensionVolume;
        }

        point.multiplyAll(followerStep);
        point.subtract(translation);
        return point;
    }

    /**
     * A method for getting the precomputed direction at the specified point
     *
     * @param point The point at which to get the DDirection
     * @return The DDirection at the given point
     */
    public DDirection getDirection(DPoint point) {
        if (isOutOfBounds(point)) {
            return new DDirection(spline.getDimensions());
        }
        return followerGradient[pointToIndex(point)];
    }

    /**
     * A method for checking to see if a given point is within the bounds of the FollowerGradient
     *
     * @param point The point to check
     * @return false if it is within the bounds, true if it is out of bounds
     */
    public boolean isOutOfBounds(DPoint point) {
        return !bounds.contains(point);
    }

    /**
     * A method for getting the Controller for this Navigator
     *
     * @return The Controller object
     */
    @Override
    public Controller getController() {
        return new Controller(this);
    }

    /**
     * A class that acts as a controller for this class
     */
    public class Controller extends Navigator.Controller {

        /**
         * The FollowerGradient to be followed
         */
        private FollowerGradient followerGradient;

        /**
         * The position of the controlled object
         */
        private DPoint point;

        /**
         * A simple constructor requiring the object to be followed / provide directions
         *
         * @param followerGradient The object that provides data about the desired path
         */
        private Controller(FollowerGradient followerGradient) {
            this.followerGradient = followerGradient;
        }

        /**
         * A method for updating the current position of the controlled object
         *
         * @param point The new position
         */
        @Override
        public void update(DPoint point) {
            this.point = point;
        }

        /**
         * A method for getting the desired direction at the current point
         *
         * @return The DDirection at the specified point
         */
        @Override
        public DVector getDirection() {
            return followerGradient.getDirection(point.clone());
        }

        /**
         * A method for getting the position of the controller object
         *
         * @return The position of the controlled object
         */
        public DPoint getPosition() {
            return point;
        }

        /**
         * A method that can be used to determine if the object has reached its destination
         */
        @Override
        public boolean isFinished() {
            return false;
        }

    }

}
