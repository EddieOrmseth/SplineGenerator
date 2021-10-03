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

}
