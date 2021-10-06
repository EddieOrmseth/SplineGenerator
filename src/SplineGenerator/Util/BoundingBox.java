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
     * A simple default constructor
     */
    public BoundingBox () {

    }

    /**
     * A constructor that also sets the values
     *
     * @param x1 The x1 value
     * @param y1 The y1 value
     * @param x2 The x2 value
     * @param y2 The y2 value
     */
    public BoundingBox(double x1, double y1, double x2, double y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    /**
     * A method for multiplying the entire box by a scalar
     *
     * @param scalar The value to multiply the BoundingBox coordinates by
     */
    public void applyScalar(double scalar) {
        x1 *= scalar;
        y1 *= scalar;
        x2 *= scalar;
        y2 *= scalar;
    }

    @Override
    public BoundingBox clone() {
        return new BoundingBox(x1, y1, x2, y2);
    }

}
