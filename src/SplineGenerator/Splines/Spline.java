package SplineGenerator.Splines;

import SplineGenerator.Util.*;

import java.util.ArrayList;

/**
 * An abstract class to hold methods and fields that may be used by one or more subclasses / subtypes of splines
 */
public abstract class Spline {

    /**
     * An ArrayList<ControlPoint> for holding the ControlPoints the spline will be generated for
     */
    public ArrayList<DControlPoint> controlPoints;

    /**
     * An ArrayList<InterpolationType> for holding the type of interpolation where the index is the (n + 1)th derivative
     */
    public ArrayList<InterpolationInfo> interpolationTypes;

    /**
     * The matrices for holding the parametric equations
     */
    public Matrix[] matrices;

    /**
     * An array for holding the spline, [dimension][piece][term]
     */
    public double[][][] spline;

    /**
     * An ArrayList<double[][][]> for holding the derivatives of the spline
     */
    public ArrayList<double[][][]> derivatives;

    /**
     * A boolean to note whether or not to close the spline, this has effects on interpolation methods;
     */
    public boolean closed = false;

    /**
     * The number of pieces the spline is composed of
     */
    public int pieces;

    /**
     * An int representing the number of terms / variables per piece of the spline
     */
    public int termsPerPiece;

    /**
     * The number of total terms, also the number of variables needed
     */
    public int numTotalTerms;

    /**
     * The number of total terms + 1, also the numTotalTerms + 1 (+ 1 for the value at the end of the equation)
     */
    public int equationLength;

    /**
     * The field containing the spline type
     */
    private SplineType splineType;

    /**
     * Default and only constructor for the spline super-class, all fields in this class will be initialized here if necessary
     *
     * @param splineType The type of spline to be generated, this will be always determined by the subclass's constructor
     */
    public Spline(SplineType splineType, int dimensions) {
        this.splineType = splineType;
        controlPoints = new ArrayList<>();
        interpolationTypes = new ArrayList<>();
        matrices = new Matrix[dimensions];
    }

    /**
     * @return The type of spline the instance represents
     */
    public SplineType getSplineType() {
        return splineType;
    }

    /**
     * A method for getting the number of equations that the matrix can hold
     *
     * @return the number of equations that the matrix can hold
     */
    public int getNumEquations() {
        return matrices[0].getHeight();
    }

    /**
     * A method to tell if the spline is registered as closed or not
     *
     * @return Whether or not the spline is closed
     */
    public boolean isClosed() {
        return closed;
    }

    /**
     * A method for getting the number of pieces the spline is composed of
     *
     * @return The number of pieces the spline is composed of
     */
    public int getNumPieces() {
        return pieces;
    }

    /**
     * A method for adding a ControlPoint to the spline, this method appends the ControlPoint to the end of the list
     *
     * @param controlPoint The ControlPoint object to be added
     */
    public void addControlPoint(DControlPoint controlPoint) {
        controlPoint.t = controlPoints.size();
        controlPoints.add(controlPoint);
    }

    /**
     * A method for removing a ControlPoint from the spline
     *
     * @param controlPoint The ControlPoint object to be removed
     */
    public void removeControlPoint(DControlPoint controlPoint) {
        int index = controlPoints.indexOf(controlPoint);
        controlPoints.remove(controlPoint);
        for (int i = index; i < controlPoints.size(); i++) {
            controlPoints.get(i).t--;
        }
    }

    /**
     * A method for setting the current order of the ControlPoints as the order in which the spline will follow
     */
    public void setCurrentOrder() {
        for (int i = 0; i < controlPoints.size(); i++) {
            controlPoints.get(i).t = i;
        }
    }

    /**
     * A method for putting the ControlPoints in the correct order with respect to the t field.
     */
    public void reorder() {
        ArrayList<DControlPoint> newOrder = new ArrayList<>();
        DControlPoint controlPoint;

        for (int i = controlPoints.size() - 1; i >= 0; i--) {
            controlPoint = getFirst();
            newOrder.add(controlPoint);
            controlPoints.remove(controlPoint);
        }

        controlPoints = newOrder;
    }

    /**
     * A method for getting the next ControlPoint in the sequence
     *
     * @return The ControlPoint that is marked as the being the first in the sequence
     */
    public DControlPoint getFirst() {
        int index = 0;
        for (int i = 1; i < controlPoints.size(); i++) {
            if (controlPoints.get(i).t < controlPoints.get(index).t) {
                index = i;
            }
        }

        return controlPoints.get(index);
    }

    /**
     * A method for getting the value at a specified t-value
     *
     * @param t The position to get the value at
     * @return The position of the function evaluated at t
     */
    public abstract DPoint get(double t);

    /**
     * A method for getting the value of the derivative at a certain point
     *
     * @param t          The t-value to evaluate the derivative at
     * @param derivative The derivative to use
     * @return The vector at the specified point on the derivative
     */
    public abstract DVector evaluateDerivative(double t, int derivative);

    /**
     * A method for initializing the matrices to the correct size
     */
    public abstract void initializeMatrices();

    /**
     * A method for generating a spline given the current parameters
     */
    public abstract void generate();

    /**
     * A method to insert an equation into the matrix
     *
     * @param row        The row to insert the equation into
     * @param piece      The piece that the equation is a part of
     * @param equation   The equation to be inserted
     * @param finalValue The value to be placed on the final line of the matrix
     * @param matrix     The matrix in which to insert the equation
     */
    public void insertEquation(int row, int piece, double[] equation, double finalValue, double[][] matrix) {
        for (int i = 0; i < equation.length; i++) {
            matrix[row][piece * equation.length + i] = equation[i];
        }
        matrix[row][matrix[0].length - 1] = finalValue;
    }

    /**
     * A method to insert an equation into the matrix
     *
     * @param row      The row to insert the equation into
     * @param piece    The piece that the equation is a part of
     * @param equation The equation to be inserted
     * @param matrix   The matrix in which to insert the equation
     */
    public void insertEquation(int row, int piece, double[] equation, double[][] matrix) {
        for (int i = 0; i < equation.length; i++) {
            matrix[row][piece * equation.length + i] = equation[i];
        }
    }

    /**
     * A method for setting the given slopes as the Catmul-Rom type / the slope between the surrounding points
     *
     * @param derivative The derivative to set
     */
    public void setMiddleCatmulRomSlopes(int derivative) {
        for (int i = 1; i < controlPoints.size() - 1; i++) {
//            controlPoints.get(i).values.set(heading, new Direction(controlPoints.get(i + 1).x - controlPoints.get(i - 1).x, controlPoints.get(i + 1).y - controlPoints.get(i - 1).y));
            for (int n = 0; n < matrices.length; n++) {
                controlPoints.get(i).values.get(derivative).set(n, controlPoints.get(i + 1).values.get(derivative - 1).get(n) - controlPoints.get(i - 1).values.get(derivative - 1).get(n));
            }
        }

        if (isClosed()) {
//            controlPoints.get(0).values.set(heading, new Direction(controlPoints.get(controlPoints.size() - 1).x - controlPoints.get(1).x, controlPoints.get(controlPoints.size() - 1).y - controlPoints.get(1).y));
//            controlPoints.get(controlPoints.size() - 1).values.set(heading, new Direction(controlPoints.get(controlPoints.size() - 2).x - controlPoints.get(0).x, controlPoints.get(controlPoints.size() - 2).y - controlPoints.get(0).y));
            for (int n = 0; n < matrices.length; n++) {
                controlPoints.get(0).values.get(derivative).set(n, controlPoints.get(controlPoints.size() - 1).values.get(derivative - 1).get(n) - controlPoints.get(1).values.get(derivative - 1).get(n));
                controlPoints.get(controlPoints.size() - 1).values.get(derivative).set(n, controlPoints.get(controlPoints.size() - 2).values.get(derivative - 1).get(n) - controlPoints.get(0).values.get(derivative - 1).get(n));
            }
        }
    }

    /**
     * A method for adding the next derivative
     */
    public void takeNextDerivative() {
        double[][][] function = derivatives.size() == 0 ? spline : derivatives.get(derivatives.size() - 1);

        double[][][] derivative = new double[function.length][function[0].length][function[0][0].length - 1];

        for (int n = 0; n < derivative.length; n++) {
            for (int p = 0; p < derivative[n].length; p++) {
                for (int t = 0; t < derivative[n][p].length; t++) {
                    derivative[n][p][t] = (t + 1) * function[n][p][t + 1];
                }
            }
        }

        derivatives.add(derivative);
    }

    /**
     * A method for finding the closest t-value / point on the spline to the given point
     *
     * @param point The Point o find the nearest point to
     * @param tStart The t-value to start at
     * @param tEnd The t-value to end at
     * @param step  How much to step by when searching for the nearest point
     * @return The point on the spline nearest to the given point, the dimension of the DPoint is 1 more than that of the point parameter, in this lies the t-value
     */
    public DPoint findClosestPointOnInterval(DPoint point, double tStart, double tEnd, double step) {
        DPoint nearestPoint = new DPoint(point.getDimensions() + 1);
        double distance = Double.MAX_VALUE;

        DPoint newPoint;
        double newDistance;

        for (double t = tStart; t < tEnd; t += step) {
            newPoint = get(t);
            newDistance = point.getDistance(newPoint);
            if (newDistance < distance) {
                distance = newDistance;
                nearestPoint.set(0, newPoint.getValues());
                nearestPoint.set(nearestPoint.getDimensions() - 1, t);
            }
        }

        return nearestPoint;
    }

    /**
     * A method for finding the closest t-value / point on the spline to the given point
     *
     * @param point The Point o find the nearest point to
     * @param step  How much to step by when searching for the nearest point
     * @return The point on the spline nearest to the given point, the dimension of the DPoint is 1 more than that of the point parameter, in this lies the t-value
     */
    public DPoint findClosestPointOnSpline(DPoint point, double step) {
        return findClosestPointOnInterval(point, 0, pieces, step);
    }

    /**
     * A method for finding the closest t-value / point on the spline to the given point
     *
     * @param point   The Point to find the nearest point tocx
     * @param segment The segment of the spline to check
     * @param step    How much to step by when searching for the nearest point
     * @return The point on the spline nearest to the given point, the dimension of the DPoint is 1 more than that of the point parameter, in this lies the t-value
     */
    public DPoint findClosestPointOnSegment(DPoint point, int segment, double step) {
        return findClosestPointOnInterval(point, segment, segment + 1, step);
    }

    /**
     * A method for getting the Extrema of the function
     *
     * @param step The amount to step by when finding the extrema
     * @return The Extrema object
     */
    public Extrema getExtrema(double step) {
        Extrema extrema = new Extrema(matrices.length);
        extrema.lesserPoint = get(0);
        extrema.greaterPoint = get(0);
        DPoint tempPoint;

        for (double t = step; t <= pieces; t += step) {
            tempPoint = get(t);
            for (int n = 0; n < matrices.length; n++) {
                if (tempPoint.get(n) > extrema.greaterPoint.get(n)) {
                    extrema.greaterPoint.set(n, tempPoint.get(n));
                }
                if (tempPoint.get(n) < extrema.lesserPoint.get(n)) {
                    extrema.lesserPoint.set(n, tempPoint.get(n));
                }
            }
        }

        return extrema;
    }

    /**
     * A method for getting the number of dimensions the spline is constructed in
     *
     * @return The number of dimensions in the spline is constructed in
     */
    public int getDimensions() {
        return matrices.length;
    }

    /**
     * A method for getting the string representation of the matrices
     *
     * @return The string representation of the spline
     */
    public String printMatrices() {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < matrices.length; i++) {
            builder.append("Matrix[").append(i).append("]:\n").append(matrices[i]);
        }

        return builder.toString();
    }

    /**
     * A method for getting the parametric equations to put into desmos
     *
     * @return The parametric equations to put into desmos
     */
    public String getDesmosEquations() {
        return "";
    }

    /**
     * An enumeration for the type of spline to be created
     */
    public enum SplineType {
        /**
         * A spline composed of polynomials
         */
        Polynomic,
        /**
         * A spline composed of trigonometric functions
         */
        Trigonometric,
        /**
         * A spline composed of hyperbolic functions
         */
        Hyperbolic
    }

    /**
     * An enumeration for the type of interpolation that decides the nth derivative at each ControlPoint
     */
    public enum InterpolationType {
        /**
         * The only requirement is that the nth derivatives be the same for each segment at that point
         */
        Linked,
        /**
         * The nth derivative at each point must be equal to the user-given value
         */
        Hermite,
        /**
         * The nth derivative between the two points on either side decides the slope at the point
         */
        CatmulRom,
        /**
         * The nth derivative is decided by a parameter k, used to generalize CatmulRom splines
         */
        Cardinal,
        /**
         * There is no specified interpolation method
         */
        None
    }

    /**
     * An enumeration for specifying what to do at the end of the spline with regard to InterpolationType
     */
    public enum EndBehavior {
        /**
         * Get the value from the user
         */
        Hermite,
        /**
         * Use the vector between the nth derivative at the only adjacent point
         */
        CatmulRom,
        /**
         * There is no specified end behavior method
         */
        None
    }

    /**
     * An enumeration detailing where the constraints of the EndBehavior should be put into effect, primarily used for making sure the number of equations is correct
     */
    public enum EndBehaviorEffect {
        /**
         * The EndBehavior should be applied at both ends of the spline
         */
        Both,
        /**
         * The EndBehavior should be applied only at the beginning of the spline
         */
        Beginning,
        /**
         * The EndBehavior should be applied only at the end of the spline
         */
        Ending
    }

}
