package SplineGenerator.Util;

import SplineGenerator.GUI.DisplayGraphics;

import java.util.Arrays;

/**
 * A class for holding a vector that begins and ends at a certain position
 */
public class DPosVector extends DVector {

    /**
     * The point at the tail of the vector
     */
    private DPoint startPoint;

    /**
     * A simple constructor for the Vector
     *
     * @param dimensions The number of dimensions the Vector has
     */
    public DPosVector(int dimensions) {
        super(dimensions);
        startPoint = new DPoint(dimensions);
    }

    /**
     * A constructor for the Vector that includes values
     *
     * @param values The values to assign to each dimension
     */
    public DPosVector(double... values) {
        super(values);
        startPoint = new DPoint(values.length);
    }

    /**
     * A constructor for a vector between the two given points.
     *
     * @param p1 The first point
     * @param p2 The second point
     */
    public DPosVector(DPoint p1, DPoint p2) {
        p1 = p1.clone();
        p2 = p2.clone();
        values = new double[p1.getDimensions()];
        for (int n = 0; n < values.length; n++) {
            values[n] = p2.get(n) - p1.get(n);
        }

        startPoint = p1;
    }

    /**
     * A constructor for a vector between the two given points.
     *
     * @param startPoint The first point
     * @param vector The vector to the second point
     */
    public DPosVector(DPoint startPoint, DVector vector) {
        this(Arrays.copyOf(vector.getValues(), vector.getValues().length));
        this.startPoint = startPoint.clone();
    }

    /**
     * A method for getting the final position of the DPosVector
     *
     * @param dimension The dimension to get
     * @return The final value of the DPosVector at in that dimension
     */
    @Override
    public double get(int dimension) {
        return values[dimension] + startPoint.get(dimension);
    }

    /**
     * A method for setting the starting point
     *
     * @param startPoint The new starting point
     */
    public void setStartPoint(DPoint startPoint) {
        this.startPoint = startPoint;
    }

    /**
     * A method for getting the starting point
     *
     * @return The starting point
     */
    public DPoint getStartPoint() {
        return startPoint;
    }

    /**
     * A method for getting just the vector
     *
     * @return The vector
     */
    public DVector toVector() {
        return new DVector(values);
    }

    /**
     * A method for painting the DPosVector at a specified point
     *
     * @param graphics The object to display on
     */
    @Override
    public void display(DisplayGraphics graphics) {
        graphics.paintVector(this);
    }

    /**
     * A method for cloning the DPosVector
     *
     * @return A clone of this object
     */
    @Override
    public DPosVector clone() {
        DPosVector clone = new DPosVector(values.length);
        for (int n = 0; n < values.length; n++) {
            clone.set(n, values[n]);
        }

        clone.setStartPoint(startPoint.clone());
        return clone;
    }

}
