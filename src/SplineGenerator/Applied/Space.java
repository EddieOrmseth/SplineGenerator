package SplineGenerator.Applied;

import SplineGenerator.Util.DPoint;
import SplineGenerator.Util.DVector;
import SplineGenerator.Util.Extrema;

/**
 * A Data Structure for holding and representing n-dimensional space
 */
public class Space<T> {

    /**
     * The Extrema object for holding the bounds of the PathFinder
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
    public double spaceStep = .25;

    /**
     * An array representing the space
     */
    private Object[] space;

    /**
     * A simple constructor that contains arguments needed to find the total amount of data
     */
    public Space(Extrema bounds, double spaceStep) {
        this.bounds = bounds;
        this.spaceStep = spaceStep;

        initializeSpace();
    }

    /**
     * A method for preparing necessary variables of the Space
     */
    public void prepareBounds() {
        lengths = bounds.getVector();
        lengths.addAll(1);
        translation = bounds.lesserPoint.toVector();
        translation.multiplyAll(-1);
    }

    /**
     * A method for initializing the array that holds the Space
     */
    public void initializeSpace() {
        prepareBounds();
        arrayLengths = lengths.clone();
        arrayLengths.multiplyAll(1 / spaceStep);

        int totalPoints = 1;
        for (int n = 0; n < arrayLengths.getDimensions(); n++) {
            totalPoints *= (int) arrayLengths.get(n);
        }

        space = new Object[totalPoints];
    }

    /**
     * A method for getting the correct index of a given point in the followerGradient
     *
     * @param point The point to find the index of
     * @return The index of the given point
     */
    public int pointToIndex(DPoint point) {
        point.add(translation);
        point.multiplyAll(1 / spaceStep);
        int index = 0;
        int subDimensionVolume;
        for (int n = 0; n < bounds.getDimensions(); n++) {
            subDimensionVolume = 1;
            for (int s = n + 1; s < bounds.getDimensions(); s++) {
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
        return indexToPoint(index, new DPoint(getDimensions()));
    }

    /**
     * A method for getting the DPoint that an index represents
     *
     * @param index The index to find the DPoint for
     * @return The DPoint that the index represents
     */
    public DPoint indexToPoint(int index, DPoint point) {
        int remaining = index;

        int subDimensionVolume;
        for (int n = 0; n < bounds.getDimensions(); n++) {
            subDimensionVolume = 1;
            for (int s = n + 1; s < bounds.getDimensions(); s++) {
                subDimensionVolume *= (int) arrayLengths.get(s);
            }
            point.set(n, remaining / subDimensionVolume);
            remaining = remaining % subDimensionVolume;
        }

        point.multiplyAll(spaceStep);
        point.subtract(translation);
        return point;
    }

    /**
     * A method for getting the T value at a certain point
     *
     * @param point The point at which to get the T value
     * @return The T value at the specified point
     */
    public T get(DPoint point) {
        return (T) space[pointToIndex(point)];
    }

    /**
     * A method for setting the T value at a certain point
     *
     * @param point The point at which to set the T value
     * @param value  The T value to be set at the specified point
     */
    public void set(DPoint point, T value) {
        space[pointToIndex(point)] = value;
    }

    /**
     * A method for setting the T value at a certain index
     *
     * @param index The index at which to set the T value
     * @return The T value to be set at the specified index
     */
    public void set(int index, T value) {
        space[index] = value;
    }

    /**
     * A method for getting the total amount of T objects held by the space
     *
     * @return The total amount of T object held by the space
     */
    public int size() {
        return space.length;
    }

    /**
     * A method for getting the bounds of the space
     *
     * @return The bounds of the space
     */
    public int getDimensions() {
        return bounds.getDimensions();
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

}
