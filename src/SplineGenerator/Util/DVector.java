package SplineGenerator.Util;

import java.util.Arrays;

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
     * A method for setting the magnitude of the vector
     *
     * @param magnitude The new magnitude
     */
    public void setMagnitude(double magnitude) {
        double currentMagnitude = getMagnitude();

        for (int n = 0; n < values.length; n++) {
            values[n] *= (magnitude / currentMagnitude);
        }
    }

    /**
     * A method for getting a DDirection from the current DVector
     *
     * @return The DDirection created from the current DVector
     */
    public DDirection toDirection() {
        return new DDirection(values);
    }

    /**
     * A method for adding two DVectors
     *
     * @param vector The vector to add to this object
     * @return The resulting DVector
     */
    public DVector add(DVector vector) {
        for (int n = 0; n < getDimensions() && n < vector.getDimensions(); n++) {
            values[n] += vector.get(n);
        }

        return this;
    }

    /**
     * A method for subtracting two DVectors
     *
     * @param vector The vector to be subtracted from this one
     * @return The resulting DVector
     */
    public DVector subtract(DVector vector) {
        for (int n = 0; n < getDimensions() && n < vector.getDimensions(); n++) {
            values[n] -= vector.get(n);
        }

        return this;
    }

    /**
     * A method for cloning the DPoint
     *
     * @return The cloned object
     */
    @Override
    public DVector clone() {
        return new DVector(Arrays.copyOf(values, values.length));
    }

}
