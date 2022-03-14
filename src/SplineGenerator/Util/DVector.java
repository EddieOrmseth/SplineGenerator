package SplineGenerator.Util;

import SplineGenerator.GUI.DisplayGraphics;

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
        for (int n = 0; n < p1.getDimensions() && n < p2.getDimensions(); n++) {
            values[n] = p2.get(n) - p1.get(n);
        }
    }

    /**
     * A method for a setting the vector so that it can lie between the two given points.
     *
     * @param p1 The first point
     * @param p2 The second point
     */
    public DVector set(DPoint p1, DPoint p2) {
//        values = new double[p1.getDimensions()];
        for (int n = 0; n < p1.getDimensions() && n < p2.getDimensions(); n++) {
            values[n] = p2.get(n) - p1.get(n);
        }

        return this;
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

        if (currentMagnitude == 0) {
            return;
        }

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
     * A method for getting the dot product of two vectors
     *
     * @param vector The other vector to be used in the dot product
     * @return The result of the dot product between the two vectors
     */
    public double dot(DVector vector) {
        double total = 0;
        for (int i = 0; i < values.length && i < vector.getDimensions(); i++) {
            total += values[i] * vector.get(i);
        }

        return total;
    }

    /**
     * A method for getting the angle between this vector and the given vector
     *
     * @param vector The given vector
     * @return The angle, in radians, between the vectors
     */
    public double getAngleBetween(DVector vector) {
        if (getMagnitude() == 0 || vector.getMagnitude() == 0) {
            return 0;
        }
        return Math.acos((dot(vector)) / (getMagnitude() * vector.getMagnitude()));
    }

    /**
     * A method for projecting this vector onto a given vector
     *
     * @param vector The vector to project this onto
     * @return The resulting vector
     */
    public DVector projectOnto(DVector vector) {
        double scalar = (dot(vector)) / (vector.getMagnitude() * vector.getMagnitude());
        vector.multiplyAll(scalar);
        return vector;
    }

    /**
     * A method for displaying the vector
     *
     * @param graphics What to display the vector on
     */
    @Override
    public void display(DisplayGraphics graphics) {
        graphics.paintVector(new DPosVector(values.length), this);
    }

    /**
     * A method for cloning the DPoint
     *
     * @return The cloned object
     */
    @Override
    public DVector clone() {
        DVector vector = new DVector(values.length);
        for (int n = 0; n < values.length; n++) {
            vector.set(n, values[n]);
        }

        return vector;
    }

}
