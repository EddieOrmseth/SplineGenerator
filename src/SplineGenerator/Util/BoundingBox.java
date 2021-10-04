package SplineGenerator.Util;

/**
 * A class for holding a simple 2D bounding box
 */
public class BoundingBox {

    /**
     * The lesser x-value
     */
    public double x1 = Double.MAX_VALUE;

    /**
     * The lesser y-value
     */
    public double y1 = Double.MAX_VALUE;

    /**
     * The greater x-value
     */
    public double x2 = Double.MIN_VALUE;

    /**
     * The greater y-value
     */
    public double y2 = Double.MIN_VALUE;

    /**
     * A method for multiplying the entire box by a scalar
     * @param scalar
     */
    public void applyScalar(double scalar) {
        x1 *= scalar;
        y1 *= scalar;
        x2 *= scalar;
        y2 *= scalar;
    }

}
