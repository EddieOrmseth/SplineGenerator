package SplineGenerator.Splines;

import SplineGenerator.Util.*;
import SplineGenerator.Util.Point;

import java.util.ArrayList;

/**
 * An abstract class to hold methods and fields that may be used by one or more subclasses / subtypes of splines
 */
public abstract class Spline {

    /**
     * An ArrayList<ControlPoint> for holding the ControlPoints the spline will be generated for
     */
    public ArrayList<ControlPoint> controlPoints;

    /**
     * An ArrayList<InterpolationType> for holding the type of interpolation where the index is the (n + 1)th derivative
     */
    public ArrayList<InterpolationInfo> interpolationTypes;

    /**
     * An ArrayList<double[]> for holding extra equations that must be added to the xMatrix but are not covered by the InterpolationTypes
     */
    public ArrayList<double[]> addedRowsX;

    /**
     * An ArrayList<double[]> for holding extra equations that must be added to the yMatrix but are not covered by the InterpolationTypes
     */
    public ArrayList<double[]> addedRowsY;

    /**
     * A Matrix for holding the parametric-x equations
     */
    public Matrix xMatrix;

    /**
     * A Matrix for holding the parametric-y equations
     */
    public Matrix yMatrix;

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
    public Spline(SplineType splineType) {
        this.splineType = splineType;
        controlPoints = new ArrayList<>();
        interpolationTypes = new ArrayList<>();
        addedRowsX = new ArrayList<>();
        addedRowsY = new ArrayList<>();
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
        if (xMatrix.getHeight() != yMatrix.getHeight()) {
            System.out.println("X and Y matrices are not the same size");
        }
        return xMatrix.getHeight();
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
    public void addControlPoint(ControlPoint controlPoint) {
        controlPoint.t = controlPoints.size();
        controlPoints.add(controlPoint);
    }

    /**
     * A method for removing a ControlPoint from the spline
     *
     * @param controlPoint The ControlPoint object to be removed
     */
    public void removeControlPoint(ControlPoint controlPoint) {
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
        ArrayList<ControlPoint> newOrder = new ArrayList<>();
        ControlPoint controlPoint;

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
    public ControlPoint getFirst() {
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
    public abstract Point get(double t);

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
     */
    public void setMiddleCatmulRomSlopes(int heading) {
        for (int i = 1; i < controlPoints.size() - 1; i++) {
            controlPoints.get(i).headings.set(heading, new Direction(controlPoints.get(i + 1).x - controlPoints.get(i - 1).x, controlPoints.get(i + 1).y - controlPoints.get(i - 1).y));
        }

        if (isClosed()) {
            controlPoints.get(0).headings.set(heading, new Direction(controlPoints.get(controlPoints.size() - 1).x - controlPoints.get(1).x, controlPoints.get(controlPoints.size() - 1).y - controlPoints.get(1).y));
            controlPoints.get(controlPoints.size() - 1).headings.set(heading, new Direction(controlPoints.get(controlPoints.size() - 2).x - controlPoints.get(0).x, controlPoints.get(controlPoints.size() - 2).y - controlPoints.get(0).y));
        }
    }

    /**
     * A method for getting the string representation of the spline
     *
     * @return The string representation of the spline
     */
    @Override
    public String toString() {
        return "X Matrix:\n" + xMatrix + "\nY Matrix:\n" + yMatrix;
    }

    /**
     * A method for getting the parametric equations to put into desmos
     *
     * @return The parametric equations to put into desmos
     */
    public String getDesmosEquations() {
        StringBuilder builder = new StringBuilder();
        int lastSpot = xMatrix.getWidth() - 1;

        for (int i = 0; i < pieces; i++) {
            builder.append("For ").append(i).append("<t<").append(i + 1).append("\n");
            builder.append("(").append(xMatrix.get((i * 4) + 0, lastSpot)).append("t^3 + ").append(xMatrix.get((i * 4) + 1, lastSpot)).append("t^2 + ").append(xMatrix.get((i * 4) + 2, lastSpot)).append("t + ").append(xMatrix.get((i * 4) + 3, lastSpot));
            builder.append(", ").append(yMatrix.get((i * 4) + 0, lastSpot)).append("t^3 + ").append(yMatrix.get((i * 4) + 1, lastSpot)).append("t^2 + ").append(yMatrix.get((i * 4) + 2, lastSpot)).append("t + ").append(yMatrix.get((i * 4) + 3, lastSpot)).append(")\n");
        }

        return builder.toString();
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
