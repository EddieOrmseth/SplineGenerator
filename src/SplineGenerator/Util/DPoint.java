package SplineGenerator.Util;

import SplineGenerator.GUI.Displayable;
import SplineGenerator.GUI.DisplayGraphics;

/**
 * A class for holding a multidimensional point
 */
public class DPoint implements Displayable {

    /**
     * The values the point represents
     */
    protected double[] values;

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
     * @param values The values to assign to each dimension
     */
    public DPoint(double... values) {
        this.values = new double[values.length];
        for (int i = 0; i < values.length; i++) {
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
     * A method for getting all the values of the point
     *
     * @return The values of the point
     */
    public double[] getValues() {
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

    /**
     * A method for copying all the values of the DPoint
     *
     * @param point The new value that is set
     */
    public void set(DPoint point) {
        for (int i = 0; i < point.values.length && i < values.length; i++) {
            values[i] = point.values[i];
        }
    }

    /**
     * A method for adding a value to the specified index
     *
     * @param n     The dimension to modify
     * @param value The value to be added
     */
    public void add(int n, double value) {
        values[n] += value;
    }

    /**
     * A method for adding two DPoints
     *
     * @param point The point to be added
     */
    public DPoint add(DPoint point) {
        for (int n = 0; n < point.getDimensions() && n < values.length; n++) {
            values[n] += point.get(n);
        }

        return this;
    }

    /**
     * A method for adding two DPoints
     *
     * @param point The point to be added
     * @param times The number of times to add the point
     */
    public DPoint addNTimes(DPoint point, double times) {
        for (int n = 0; n < point.getDimensions() && n < values.length; n++) {
            values[n] += point.get(n) * times;
        }

        return this;
    }

    /**
     * A method for subtracting two DPoints
     *
     * @param point The point to be subtracted
     */
    public DPoint subtract(DPoint point) {
        for (int n = 0; n < point.getDimensions() && n < values.length; n++) {
            values[n] -= point.get(n);
        }

        return this;
    }

    /**
     * A method for adding a value to every index
     *
     * @param value The value to be added
     */
    public void addAll(double value) {
        for (int n = 0; n < values.length; n++) {
            values[n] += value;
        }
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

    /**
     * A method for multiplying each index by a scalar
     *
     * @param scalar The value to be multiplied by
     */
    public void multiplyAll(double scalar) {
        for (int n = 0; n < values.length; n++) {
            values[n] *= scalar;
        }
    }

    /**
     * A method for getting the distance between the two points
     *
     * @param point The point to find to distance to
     * @return The distance between the points
     */
    public double getDistance(DPoint point) {
        double total = 0;
        for (int n = 0; n < values.length && n < point.getDimensions(); n++) {
            total += (values[n] - point.get(n)) * (values[n] - point.get(n));
        }

        return Math.sqrt(total);
    }

    /**
     * A method for adding a number of dimensions on the end of the point
     *
     * @param numDimensions The number of dimensions to be added
     */
    public void addDimensions(int numDimensions) {
        double[] newValues = new double[values.length + numDimensions];
        for (int n = 0; n < values.length; n++) {
            newValues[n] = values[n];
        }
        values = newValues;
    }

    /**
     * A method for removing dimensions
     *
     * @param dimension The dimension to remove
     * @return The value previously held by that dimension
     */
    public double removeDimension(int dimension) {
        double value = values[dimension];
        int realIndex = 0;
        double[] newValues = new double[values.length - 1];
        for (int i = 0; i < getDimensions(); i++) {
            if (realIndex == dimension) {
                continue;
            }
            newValues[realIndex] = values[i];
            realIndex++;
        }
        values = newValues;
        return value;
    }

    @Override
    public void display(DisplayGraphics graphics) {
        graphics.paintPoint(this);
    }

    /**
     * A method for converting the current DPoint to a DVector
     *
     * @return The DVector that is created
     */
    public DVector toVector() {
        return new DVector(values);
    }

    /**
     * A method for cloning the DPoint
     *
     * @return The cloned object
     */
    @Override
    public DPoint clone() {
        DPoint point = new DPoint(values.length);
        for (int n = 0; n < values.length; n++) {
            point.set(n, values[n]);
        }

        return point;
    }

    /**
     * A method for getting the String representation of the point
     *
     * @return A String representation of the point
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append("(");
        for (int n = 0; n < values.length; n++) {
            builder.append(values[n]);
            if (n != values.length - 1) {
                builder.append(", ");
            }
        }
        builder.append(")");

        return builder.toString();
    }

}
