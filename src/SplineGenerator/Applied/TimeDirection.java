package SplineGenerator.Applied;

import SplineGenerator.Util.DDirection;
import SplineGenerator.Util.DVector;

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

    public TimeDirection(double length, double segmentSize) {
        this.length = length;
        this.segmentSize = segmentSize;
        directions = new DVector[length % segmentSize > 0 ? (int) (length / segmentSize) + 1 : (int) (length / segmentSize)];
        times = new double[directions.length];
    }

    public void insert(DVector vector, double time, int segment) {
        directions[segment] = vector;
        times[segment] = time;
    }

    public DVector get(int segment) {
        return directions[segment];
    }

    public DVector get(double tValue) {
        tValue /= segmentSize;
        return directions[(int) tValue];
    }

    public DVector getMinSurrounding(double tValue) {
        int segment = tToSegment(tValue);

        DVector v0 = segment - 1 >= 0 ? get(segment - 1) : null;
        DVector v1 = get(segment);
        DVector v2 = segment + 1 < directions.length ? get(segment + 1) : null;

        double v0Mag = v0 != null ? v0.getMagnitude() : Double.MAX_VALUE;
        double v1Mag = v1 != null ? v1.getMagnitude() : -1;
        double v2Mag = v2 != null ? v2.getMagnitude() : Double.MAX_VALUE;

        if (v0 == null && v1 == null && v2 == null) {
            return new DVector(3);
        }

        if (v0Mag < v1Mag && v0Mag < v2Mag) {
//            v0.addDimensions(1);
//            v0.set(v0.getDimensions() - 1, times[segment - 1]);
            return v0;
        } else if (v1Mag < v0Mag && v1Mag < v2Mag) {
//            v1.addDimensions(1);
//            v1.set(v1.getDimensions() - 1, times[segment]);
            return v1;
        } else {
//            v2.addDimensions(1);
//            v2.set(v2.getDimensions() - 1, times[segment + 1]);
            return v2;
        }
    }

    public int getMinSurroundingSegment(double tValue) {
        int segment = tToSegment(tValue);

        DVector v0 = segment - 1 >= 0 ? get(segment - 1) : null;
        DVector v1 = get(segment);
        DVector v2 = segment + 1 < directions.length ? get(segment + 1) : null;

        double v0Mag = v0 != null ? v0.getMagnitude() : Double.MAX_VALUE;
        double v1Mag = v1 != null ? v1.getMagnitude() : -1;
        double v2Mag = v2 != null ? v2.getMagnitude() : Double.MAX_VALUE;

        if (v0 == null && v1 == null && v2 == null) {
            return -1;
        }

        if (v0Mag < v1Mag && v0Mag < v2Mag) {
            return segment - 1;
        } else if (v1Mag < v0Mag && v1Mag < v2Mag) {
            return segment;
        } else {
            return segment + 1;
        }
    }

    public int tToSegment(double tValue) {
        return (int) (tValue / segmentSize);
    }

}
