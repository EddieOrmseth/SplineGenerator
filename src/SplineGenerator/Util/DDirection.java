package SplineGenerator.Util;

/**
 * A class representing a multidimensional direction in the form of a unit vector
 */
public class DDirection extends DVector {

    /**
     * A method for holding the initial magnitude of the vector
     */
    private double initialMagnitude;

    /**
     * A simple constructor for the Direction
     *
     * @param dimensions The number of dimensions the Direction has
     */
    public DDirection(int dimensions) {
        super(dimensions);
    }

    /**
     * A constructor for the Point that includes values
     *
     * @param values The values to assign to each dimension
     */
    public DDirection(double... values) {
        super(values);

        initialMagnitude = super.getMagnitude();
        if (initialMagnitude == 0) {
            return;
        }
        for (int i = 0; i < this.values.length; i++) {
            this.values[i] /= initialMagnitude;
        }
    }

    /**
     * A method for getting the magnitude of the vector, for a unit vector it is always 1
     *
     * @return 1, the magnitude of a unit vector is always 1
     */
    @Override
    public double getMagnitude() {
        return 1;
    }

    /**
     * A method for setting a value of the Direction
     *
     * @param n      The number that corresponds to the dimension to be changed
     * @param values The new value that is set
     */
    @Override
    public void set(int n, double... values) {
        for (int i = 0; i < this.values.length; i++) {
            this.values[i] *= initialMagnitude;
        }

        super.set(n, values);

        initialMagnitude = super.getMagnitude();
        for (int i = 0; i < this.values.length; i++) {
            this.values[i] /= initialMagnitude;
        }
    }

    /**
     * A method for setting a value without adjusting the magnitude
     */
    public void forceSet(int n, double... values) {
        super.set(n, values);
    }

    /**
     * A method for cloning the DPoint
     *
     * @return The cloned object
     */
    @Override
    public DDirection clone() {
        DDirection direction = new DDirection(values.length);
        for (int n = 0; n < values.length; n++) {
            direction.values[n] = values[n];
        }

        return direction;
    }

}
