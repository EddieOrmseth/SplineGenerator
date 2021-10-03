package SplineGenerator.Util;

/**
 * A class for holding a multidimensional vector
 */
public class DVector {

    /**
     * The values contained by the Vector
     */
    protected double[] values;

    /**
     * A simple constructor for the Vector
     *
     * @param dimensions The number of dimensions the Vector has
     */
    public DVector(int dimensions) {
        values = new double[dimensions];
    }

    /**
     * A constructor for the Vector that includes values
     *
     * @param values The values to assign to each dimension
     */
    public DVector(double... values) {
        this.values = new double[values.length];
        for (int i = 0; i < values.length; i++) {
            this.values[i] = values[i];
        }
    }

    /**
     * A method for getting the number of dimensions the point has
     *
     * @return The number of dimensions the Vector has
     */
    public int getDimensions() {
        return values.length;
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
     * A method for getting the value for the specified dimension
     *
     * @param n The dimension to get the value at
     * @return The value contained
     */
    public double get(int n) {
        return values[n];
    }

    /**
     * A method for getting all the values of the Vector
     *
     * @return All the values of the Vector
     */
    public double[] getAll() {
        return values;
    }

    /**
     * A method for setting a value of the Direction
     *
     * @param n      The number that corresponds to the dimension to be changed
     * @param values The new value that is set
     */
    public void set(int n, double... values) {
        for (int i = 0; i < values.length && i < this.values.length; i++) {
            this.values[n + i] = values[i];
        }
    }

}
