package SplineGenerator.Util;

/**
 * A class for holding multiple DDirections with different times attached to each
 */
public class TimeDirection {

    /**
     * The DVectors for each segment
     */
    private DVector[] directions;

    /**
     * The time value attached to each direction
     */
    public double[] times;

    /**
     * The size of the t-values to tbe held
     */
    private double length;

    /**
     * The size of each segment in t-values
     */
    private double segmentSize;

    /**
     * A constructor that requires the variables to initialize the array to the correct length
     *
     * @param length The length of the spline to be followed
     * @param segmentSize The size of each segment
     */
    public TimeDirection(double length, double segmentSize) {
        this.length = length;
        this.segmentSize = segmentSize;
        directions = new DVector[length % segmentSize > 0 ? (int) (length / segmentSize) + 1 : (int) (length / segmentSize)];
        times = new double[directions.length];
    }

    /**
     * A method for setting the DVector and time value at a specified point
     *
     * @param vector The new DVector to be set
     * @param time The time to set to
     * @param segment The segment to set the new information on
     */
    public void set(DVector vector, double time, int segment) {
        directions[segment] = vector;
        times[segment] = time;
    }

    /**
     * A method for getting the value at the specified segment
     *
     * @param segment The segment to get the DVector at
     * @return The DVector at the specified segment
     */
    public DVector get(int segment) {
        return directions[segment];
    }

    /**
     * A method for getting the time at a certain t value;
     *
     * @param tValue The specified t value
     * @return The DVector at the specified t value
     */
    public DVector get(double tValue) {
        tValue /= segmentSize;
        return directions[(int) tValue];
    }

    /**
     * A method for getting the segment that contains the closest point to the position of the TimeDirection
     *
     * @param tValue The given t value
     * @return The segment containing the closest point on the spline
     */
    public int getMinSurroundingSegment(double tValue) {
        int segment = tToSegment(tValue);

        DVector v0 = segment - 1 >= 0 ? get(segment - 1) : null;
        DVector v1 = segment < directions.length ? get(segment) : null;
        DVector v2 = segment + 1 < directions.length ? get(segment + 1) : null;

        double v0Mag = v0 != null ? v0.getMagnitude() : Double.MAX_VALUE;
        double v1Mag = v1 != null ? v1.getMagnitude() : -1;
        double v2Mag = v2 != null ? v2.getMagnitude() : Double.MAX_VALUE;

        if (v0 == null && v1 == null && v2 == null) {
            return -1;
        }

        if (v0Mag < v1Mag && v0Mag < v2Mag) {
            return segment - 1;
        } else if (v2Mag < v0Mag && v2Mag < v1Mag) {
            return segment + 1;
        } else {
            return segment;
        }
    }

    /**
     * A method for converting t values to segment numbers
     *
     * @param tValue The t value to find the segment at
     * @return The segment of t
     */
    public int tToSegment(double tValue) {
        return (int) (tValue / segmentSize);
    }

    /**
     * A method for cloning the TimeDirection object
     *
     * @return The new cloned TimeDirection
     */
    @Override
    public TimeDirection clone() {
        TimeDirection timeDirection = new TimeDirection(length, segmentSize);
        DVector direction;

        for (int s = 0; s < times.length; s++) {
            direction = directions[s] != null ? directions[s].clone() : null;
            timeDirection.set(direction, times[s], s);
        }

        return timeDirection;
    }

}
