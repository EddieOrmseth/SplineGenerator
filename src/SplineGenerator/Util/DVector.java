package SplineGenerator.Util;

/**
 * A class for holding a multidimensional vector
 */
public class DVector extends DPoint {

    /**
     * A simple constructor for the Vector
     *
     * @param dimensions The number of dimensions the Vector has
     */
    public DVector(int dimensions) {
        super(dimensions);
    }

    /**
     * A constructor for the Vector that includes values
     *
     * @param values The values to assign to each dimension
     */
    public DVector(double... values) {
        super(values);
    }

    /**
     * A constructor for a vector between the two given points.
     *
     * @param p1 The first point
     * @param p2 The second point
     */
    public DVector(DPoint p1, DPoint p2) {
        values = new double[p1.getDimensions()];
        for (int n = 0; n < values.length; n++) {
            values[n] = p2.get(n) - p1.get(n);
        }
    }

    /**
     * A method for getting the magnitude of the vector
     *
     * @return The magnitude of the vector
     */
    public double getMagnitude() {
        double value = 0;

        for (int i = 0; i < values.length; i++) {
            value += values[i] * values[i];
        }

        return Math.sqrt(value);
    }

    /**
     * A method for adding two DVectors
     *
     * @param vector The vector to add to this object
     * @return The resulting DVector
     */
    public DVector vectorAddition(DVector vector) {
        for (int n = 0; n < getDimensions() && n < vector.getDimensions(); n++) {
            add(n, vector.get(n));
        }
        return this;
    }

}
