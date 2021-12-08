package SplineGenerator.Applied;

import SplineGenerator.GUI.KeyBoardListener;
import SplineGenerator.Splines.Spline;
import SplineGenerator.Util.*;

import java.awt.event.KeyEvent;

/**
 * A class representing a gradient such that each value points in the direction of the correct movement
 */
public class Segmenter implements Navigator {

    /**
     * The spline to be followed
     */
    public Spline spline;

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
    private TimeDirection[] followerGradient;

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
     * The number to step by when finding the smallest values on the spline
     */
    private double onSplineSegmentSize = .25;

    /**
     * The radius that each value must be within
     */
    private double onPathRadius = 4;

    /**
     * A constructor for the FollowerGradient including all the necessary parts
     *
     * @param spline The spline to be followed
     */
    public Segmenter(Spline spline) {
        this.spline = spline;
    }

    /**
     * A constructor for the FollowerGradient including all the necessary parts
     *
     * @param spline           The spline to be followed
     * @param gradientModifier The modifier for the gradient DVector
     * @param distanceModifier The modifier for the distance DVector
     */
    public Segmenter(Spline spline, Function<DVector, DVector> gradientModifier, Function<DVector, DVector> distanceModifier) {
        this.spline = spline;
        this.gradientModifier = gradientModifier;
        this.distanceModifier = distanceModifier;
    }

    /**
     * A method for getting the follower DVector at the given point
     *
     * @param point The point to get the TimeDirection for
     * @return The follower object TimeDirection
     */
    public TimeDirection evaluateAt(DPoint point) {
        TimeDirection direction = new TimeDirection(spline.pieces, onSplineSegmentSize);
        DPoint nearestPoint = null;

        double t = 0;
        int s = 0;
        boolean lastSegment = false;
        while (!lastSegment) {

            if (t + onSplineSegmentSize >= spline.pieces) {
                lastSegment = true;
                nearestPoint = spline.findClosestPointOnInterval(point.clone(), t, spline.pieces, splinePointStep);
            }

            if (!lastSegment) {
                nearestPoint = spline.findClosestPointOnInterval(point.clone(), t, t + onSplineSegmentSize, splinePointStep);
            }

            DVector distance = new DVector(point, nearestPoint);
            if (distance.getMagnitude() < onPathRadius) {
                direction.set(distance, nearestPoint.get(point.getDimensions()), s);
            }

            t += onSplineSegmentSize;
            s++;
        }

        return direction;
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
     * A method for getting the gradientModifier
     *
     * @return The function for gradient modification
     */
    public Function<DVector, DVector> getGradientModifier() {
        return gradientModifier;
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
     * A method for setting the distanceModifier
     *
     * @return The function for distance modification
     */
    public Function<DVector, DVector> getDistanceModifier() {
        return distanceModifier;
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

        followerGradient = new TimeDirection[totalPoints];
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
    public TimeDirection get(DPoint point) {
        if (isOutOfBounds(point)) {
            return new TimeDirection(spline.pieces, onSplineSegmentSize);
        }
        return followerGradient[pointToIndex(point)];
    }

    /**
     * A method for getting the direction at the specified point
     *
     * @param point The point in space INCLUDING the time as the last dimension
     * @return The point in space, the final dimension will be the time
     */
    public DVector getDirection(DPoint point) {
        DPoint position = point.clone();
        position.removeDimension(position.getDimensions() - 1);

        TimeDirection timeDirection = get(position.clone()).clone();
        int segment = timeDirection.getMinSurroundingSegment(point.get(point.getDimensions() - 1));
        if (segment == -1) {
            DDirection returnDirection = new DDirection(spline.getDimensions() + 1);
            returnDirection.forceSet(returnDirection.getDimensions() - 1, point.get(point.getDimensions() - 1));
            return returnDirection;
        }

        DVector distance = timeDirection.get(segment);
        if (distance == null) {
            DDirection returnDirection = new DDirection(spline.getDimensions() + 1);
            returnDirection.forceSet(returnDirection.getDimensions() - 1, point.get(point.getDimensions() - 1));
            return returnDirection;
        }

        distance = distanceModifier.get(distance);
        DVector derivative = gradientModifier.get(spline.evaluateDerivative(timeDirection.times[segment], 1));
        distance.add(derivative);

        DVector direction = distance.clone();
        direction.addDimensions(1);
        direction.set(direction.getDimensions() - 1, timeDirection.times[segment]);

        return direction;
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
     * A method for getting a random TimeDirection
     *
     * @return A random TimeDirection
     */
    public TimeDirection getTimeDirection() {
        return followerGradient[0];
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
         * The Navigator to follow
         */
        private Segmenter segmenter;

        /**
         * The position of the controlled object
         */
        private DPoint point;

        /**
         * The t value of the controlled object
         */
        private double tValue = 0;

        /**
         * A TimeDirection for converting t values to segment numbers
         */
        private TimeDirection segmentGetter;

        /**
         * The current segment of the controlled object
         */
        public int segment;

        /**
         * A constructor, all the Controller needs is the Segmenter to follow
         *
         * @param segmenter The Segmenter to follow
         */
        private Controller(Segmenter segmenter) {
            this.segmenter = segmenter;
            segmentGetter = segmenter.getTimeDirection();
        }

        /**
         * A method for updating the position of the controlled object
         *
         * @param point The new position
         */
        @Override
        public void update(DPoint point) {
            this.point = point;
            if (KeyBoardListener.get(KeyEvent.VK_SPACE)) {
                tValue = 0;
            }
        }

        /**
         * A method for getting the desired direction at the current point
         *
         * @return The DDirection at the specified point
         */
        @Override
        public DVector getDirection() {
            DPoint timePoint = point.clone();
            timePoint.addDimensions(1);
            timePoint.set(timePoint.getDimensions() - 1, tValue);

            DVector direction = segmenter.getDirection(timePoint);

            tValue = direction.get(direction.getDimensions() - 1);
            segment = segmentGetter.tToSegment(tValue);

            direction.removeDimension(direction.getDimensions() - 1);

            return direction;
        }

        /**
         * A method for getting the tValue of the controller
         *
         * @return The tValue of the controller
         */
        public double getTValue() {
            return tValue;
        }

        /**
         * A method for getting the spline that is followed by the controller
         *
         * @return The spline the controller follows
         */
        public Spline getSpline() {
            return segmenter.spline;
        }

    }

}
