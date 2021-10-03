package SplineGenerator.Util;

/**
 * A class for holding a multidimensional point
 */
public class DPoint {

    /**
     * The values the point represents
     */
    private double[] values;

    /**
     * A simple constructor for the Point
     *
     * @param dimensions The number of dimensions the point has
     */
    public DPoint(int dimensions) {
        values = new double[dimensions];
    }

    /**
     * A constructor for the Point that includes values
     *
     * @param dimensions The number of dimensions the Point has
     * @param values     The values to assign to each dimension
     */
    public DPoint(int dimensions, double... values) {
        this.values = new double[dimensions];
        for (int i = 0; i < dimensions && i < values.length; i++) {
            this.values[i] = values[i];
        }
    }

    /**
     * A method for getting the number of dimensions the point has
     *
     * @return The number of dimensions the Point has
     */
    public int getDimensions() {
        return values.length;
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

    /**
     * A method for adding value to the specified index
     *
     * @param n     The dimension to modify
     * @param value The value to be added
     */
    public void add(int n, double value) {
        values[n] += value;
    }

    /**
     * A method for multiplying the specified index by a value
     *
     * @param n      The dimension to modify
     * @param scalar The value to be multiplied by
     */
    public void multiply(int n, double scalar) {
        values[n] *= scalar;
    }

}
