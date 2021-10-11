package SplineGenerator.Splines;

import SplineGenerator.Util.*;

import java.util.ArrayList;

/**
 * A class for creating polynomic splines
 */
public class PolynomicSpline extends Spline {

    /**
     * The order of each polynomial constructed
     */
    private int polynomicOrder;

    /**
     * A nice and easy default constructor for PolynomicSplines
     *
     * @param dimensions The number of dimensions the spline is in
     */
    public PolynomicSpline(int dimensions) {
        super(SplineType.Polynomic, dimensions);
        setPolynomicOrder(3);
    }

    /**
     * A nice and easy default constructor for PolynomicSplines
     *
     * @param dimensions The number of dimensions the spline is in
     * @param order      The order of each polynomial constructed
     */
    public PolynomicSpline(int dimensions, int order) {
        super(SplineType.Polynomic, dimensions);
        setPolynomicOrder(order);
    }

    /**
     * A method for setting the PolynomicType
     *
     * @param polynomicOrder The PolynomicType to be set to
     */
    public void setPolynomicOrder(int polynomicOrder) {
        this.polynomicOrder = polynomicOrder;
        termsPerPiece = polynomicOrder + 1;
    }

    /**
     * The polynomic specific implementation of get
     *
     * @param t The value to evaluate the spline at
     * @return The value of the function at t
     */
    @Override
    public DPoint get(double t) {
        DPoint point = new DPoint(matrices.length);

        double tValue = t - ((int) t);

        int s = (int) t;

        for (int n = 0; n < spline.length; n++) {
            for (int p = 0; p < spline[n][s].length; p++) {
                point.add(n, spline[n][s][p] * Math.pow(tValue, p));
            }
        }

        return point;
    }

    /**
     * A method for getting the value of the gradient at a specific t-value
     *
     * @param t          The t-value to evaluate the derivative at
     * @param derivative The derivative to evaluate
     * @return The DVector of the derivative at t
     */
    @Override
    public DVector evaluateDerivative(double t, int derivative) {
        DVector point = new DVector(matrices.length);
        double[][][] function = derivatives.get(derivative);

        double tValue = t - ((int) t);
        int s = (int) t;

        for (int n = 0; n < function.length; n++) {
            for (int p = 0; p < function[n][s].length; p++) {
                point.add(n, function[n][s][p] * Math.pow(tValue, p));
            }
        }

        return point;
    }

    /**
     * The polynomic specific implementation of initializeMatrices
     */
    @Override
    public void initializeMatrices() {
        pieces = isClosed() ? controlPoints.size() : controlPoints.size() - 1;

        numTotalTerms = termsPerPiece * pieces;
        equationLength = numTotalTerms + 1;

        for (int n = 0; n < matrices.length; n++) {
            matrices[n] = new Matrix(numTotalTerms, equationLength);
        }

        spline = new double[matrices.length][pieces][termsPerPiece];
        derivatives = new ArrayList<>();
    }

    /**
     * The polynomic specific implementation of generate
     */
    @Override
    public void generate() {
        initializeMatrices();
        int row = 0;

        row = matchPositions(row);

        for (int i = 0; i < interpolationTypes.size(); i++) {
            switch (interpolationTypes.get(i).interpolationType) {
                case Linked:
                    row = linkMiddleValues(row, i + 1);
                    break;
                case CatmulRom:
                    setMiddleCatmulRomSlopes(i + 1);
                case Hermite:
                    row = setMiddleValues(row, i + 1);
                    break;
                case None:
                default:
            }
            if (!isClosed()) {
                row = setEndingEquations(row, interpolationTypes.get(i), i + 1);
            }
        }

        System.out.println(printMatrices());

        for (int n = 0; n < matrices.length; n++) {
            matrices[n].solve();
        }

        setSpline();
        derivatives.add(spline);
    }

    /**
     * Puts the values of each equation into the spline double[]
     */
    public void setSpline() {
        int i = 0;
        for (int n = 0; n < matrices.length; n++) {
            for (int p = 0; p < pieces; p++) {
                for (int t = termsPerPiece - 1; t >= 0; t--) {
                    spline[n][p][t] = matrices[n].matrix[i][matrices[n].matrix[0].length - 1];
                    i++;
                }
            }
            i = 0;
        }
    }

    /**
     * A method for setting the ending equations for the given criteria
     *
     * @param row        The row to start filling
     * @param info       The object containing the necessary information
     * @param derivative The level of continuity that we are setting / modifying
     * @return The number of the next row to be used
     */
    public int setEndingEquations(int row, InterpolationInfo info, int derivative) {
        switch (info.endBehavior) {
            case Hermite:
                switch (info.endEffect) {
                    case Both:
                        setDimensionalValues(row, 0, derivative, 0, getDimensionalValues(0, derivative));
                        row++;
                        setDimensionalValues(row, pieces - 1, derivative, 1, getDimensionalValues(controlPoints.size() - 1, derivative));
                        row++;
                        break;
                    case Beginning:
                        setDimensionalValues(row, 0, derivative, 0, getDimensionalValues(0, derivative));
                        row++;
                        break;
                    case Ending:
                        setDimensionalValues(row, pieces - 1, derivative, 1, getDimensionalValues(controlPoints.size() - 1, derivative));
                        row++;
                        break;
                }
                break;
            case CatmulRom:
                switch (info.endEffect) {
                    case Both:
                        for (int n = 0; n < matrices.length; n++) {
                            setValue(row, 0, derivative, 0, controlPoints.get(0).values.get(derivative).get(n), matrices[n].matrix);
                        }
                        row++;
                        for (int n = 0; n < matrices.length; n++) {
                            setValue(row, pieces - 1, derivative, 1, controlPoints.get(0).values.get(derivative).get(n), matrices[n].matrix);
                        }
                        row++;
                        break;
                    case Beginning:
                        for (int n = 0; n < matrices.length; n++) {
                            setValue(row, 0, derivative, 0, controlPoints.get(0).values.get(derivative).get(n), matrices[n].matrix);
                        }
                        row++;
                        break;
                    case Ending:
                        for (int n = 0; n < matrices.length; n++) {
                            setValue(row, pieces - 1, derivative, 1, controlPoints.get(0).values.get(derivative).get(n), matrices[n].matrix);
                        }
                        row++;
                        break;
                }
                break;
            case None:
            default:
        }

        return row;
    }

    /**
     * A method for insuring the splines go through their control points
     *
     * @param row The number of the row to start filling
     * @return The number of the next row available for use
     */
    public int matchPositions(int row) {

        // The first ControlPoint
        setDimensionalValues(row, 0, 0, 0, getDimensionalValues(0, 0));
        row++;

        // ControlPoints with pieces on both sides
        for (int i = 1; i < controlPoints.size() - 1; i++) {
            setDimensionalValues(row, i - 1, 0, 1, getDimensionalValues(i, 0));
            row++;
            setDimensionalValues(row, i, 0, 0, getDimensionalValues(i, 0));
            row++;
        }

        // The last ControlPoint
        setDimensionalValues(row, controlPoints.size() - 2, 0, 1, getDimensionalValues(controlPoints.size() - 1, 0));
        row++;

        if (isClosed()) { // Match the positions of the piece connecting the beginning and end
            setDimensionalValues(row, pieces - 1, 0, 0, getDimensionalValues(controlPoints.size() - 1, 0));
            row++;
            setDimensionalValues(row, pieces - 1, 0, 1, getDimensionalValues(0, 0));
            row++;
        }

        return row;
    }

    /**
     * A method for linking all the derivatives of the spline
     *
     * @param row        The row to start filling in
     * @param derivative The number of times to differentiate
     * @return The next row that is available for use
     */
    public int linkMiddleValues(int row, int derivative) {

        // ControlPoints with pieces on both sides
        for (int i = 1; i < controlPoints.size() - 1; i++) {
            linkDimensionalValues(row, i - 1, i, derivative, 1, 0);
            row++;
        }

        if (isClosed()) {
            linkDimensionalValues(row, 0, pieces - 1, derivative, 0, 1);
            row++;
            linkDimensionalValues(row, pieces - 2, pieces - 1, derivative, 1, 0);
            row++;
        }

        return row;
    }

    /**
     * A method for setting all the values to the given value for a certain derivative
     *
     * @param row        The number of the row to start filling with equations
     * @param derivative The number of times to differentiate
     * @return The index of the next row to start filling with equations
     */
    public int setMiddleValues(int row, int derivative) {

        // ControlPoints with pieces on each side
        for (int i = 1; i < controlPoints.size() - 1; i++) {
            setDimensionalValues(row, i - 1, derivative, 1, getDimensionalValues(i, derivative));
            row++;
            setDimensionalValues(row, i, derivative, 0, getDimensionalValues(i, derivative));
            row++;
        }

        if (isClosed()) {
            // The first ControlPoint
            setDimensionalValues(row, 0, derivative, 0, getDimensionalValues(0, derivative));
            row++;

            // The last ControlPoint
            setDimensionalValues(row, controlPoints.size() - 2, derivative, 1, getDimensionalValues(controlPoints.size() - 1, derivative));
            row++;

            setDimensionalValues(row, pieces - 1, derivative, 0, getDimensionalValues(controlPoints.size() - 1, derivative));
            row++;

            setDimensionalValues(row, pieces - 1, derivative, 1, getDimensionalValues(0, derivative));
            row++;
        }

        return row;
    }

    /**
     * A method for getting an equation multiplied by -1
     *
     * @param derivative The number of times to differentiate
     * @param value      The value to evaluate the function at
     * @return An array of the coefficients for the function multiplied by -1
     */
    public double[] getNegativeEquation(int derivative, double value) {
        double[] equation = getEquation(derivative, value);

        for (int i = 0; i < equation.length; i++) {
            equation[i] *= -1;
        }

        return equation;
    }

    /**
     * A method that gets the list of coefficients for the nth derivative at a certain value
     *
     * @param derivative The number of times to differentiate
     * @param value      The value to evaluate the function at
     * @return An array of the coefficients for the function
     */
    public double[] getEquation(int derivative, double value) {
        double[] equation;


        equation = new double[termsPerPiece];

        for (int i = 0; i < equation.length; i++) {
            double derivCo = SplineMath.getDerivativeCoefficient((equation.length - 1) - i, derivative);
            double pow;
            if (derivCo != 0) {
                pow = Math.pow(value, (equation.length - 1) - (i + derivative));
            } else {
                pow = 0;
            }
            equation[i] = derivCo * pow;
        }

        return equation;
    }


    public double[] getDimensionalValues(int controlPoint, int derivative) {
        return controlPoints.get(controlPoint).values.get(derivative).getValues();
    }

    /**
     * A method for linking values / forcing values to be the same
     *
     * @param row        The row of the matrix to be used
     * @param equation1  The first equation to be linked
     * @param equation2  The second equation to be linked
     * @param derivative The number of times to differentiate
     * @param value1     The value to evaluate the first function at
     * @param value2     The value to evaluate the second function at
     * @return The number of the next row to use
     */
    public int linkDimensionalValues(int row, int equation1, int equation2, int derivative, double value1, double value2) {
        for (int n = 0; n < matrices.length; n++) {
            linkValues(row, equation1, equation2, derivative, value1, value2, matrices[n].matrix);
        }
        row++;
        return row;
    }

    /**
     * A method for linking values / forcing values to be the same
     *
     * @param row        The row of the matrix to be used
     * @param equation1  The first equation to be linked
     * @param equation2  The second equation to be linked
     * @param derivative The number of times to differentiate
     * @param value1     The value to evaluate the first function at
     * @param value2     The value to evaluate the second function at
     * @param matrix     The matrix in which to put the equation
     * @return The number of the next row to use
     */
    public int linkValues(int row, int equation1, int equation2, int derivative, double value1, double value2, double[][] matrix) {

        insertEquation(row, equation1, getNegativeEquation(derivative, value1), matrix);
        insertEquation(row, equation2, getEquation(derivative, value2), 0, matrix);
        row++;

        return row;
    }

    /**
     * A method for setting the value of an equation
     *
     * @param row         The row of the matrix to be used
     * @param piece       The equation of the value to be set
     * @param derivative  The number of times to differentiate
     * @param value       The value to evaluate the function at
     * @param finalValues The value to be inserted at the end the line, the value to which the equation equals
     * @return The number of the next row to use
     */
    public int setDimensionalValues(int row, int piece, int derivative, double value, double... finalValues) {
        for (int n = 0; n < finalValues.length; n++) {
            setValue(row, piece, derivative, value, finalValues[n], matrices[n].matrix);
        }
        row++;
        return row;
    }

    /**
     * A method for setting the value of an equation
     *
     * @param row        The row of the matrix to be used
     * @param piece      The equation of the value to be set
     * @param derivative The number of times to differentiate
     * @param value      The value to evaluate the function at
     * @param finalValue The value to be inserted at the end the line, the value to which the equation equals
     * @param matrix     The matrix in which to put the equation
     * @return The number of the next row to use
     */
    public int setValue(int row, int piece, int derivative, double value, double finalValue, double[][] matrix) {

        insertEquation(row, piece, getEquation(derivative, value), finalValue, matrix);
        row++;

        return row;
    }

    /**
     * A method for getting a String representation of the spline
     *
     * @return The String representation of the spline
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        for (int n = 0; n < spline.length; n++) {
            builder.append("\nDimension: ").append(n);
            for (int s = 0; s < spline[n].length; s++) {
                builder.append("\n\tPiece: ").append(s).append("\n\t\t");
                for (int t = 0; t < spline[n][s].length; t++) {
                    builder.append(spline[n][s][t]).append("t^").append(t);
                    if (t != spline[n][s].length - 1) {
                        builder.append(" + ");
                    }
                }
            }
        }

        return builder.toString();
    }

    /**
     * A method for getting the String representation of the given ed array
     *
     * @param spline The array to be printed, must be in the format [dimension][piece][term]
     * @return The String representation of the spline
     */
    public String printAsSpline(double[][][] spline) {
        StringBuilder builder = new StringBuilder();

        for (int n = 0; n < spline.length; n++) {
            builder.append("\nDimension: ").append(n);
            for (int s = 0; s < spline[n].length; s++) {
                builder.append("\n\tPiece: ").append(s).append("\n\t\t");
                for (int t = 0; t < spline[n][s].length; t++) {
                    builder.append(spline[n][s][t]).append("t^").append(t);
                    if (t != spline[n][s].length - 1) {
                        builder.append(" + ");
                    }
                }
            }
        }

        return builder.toString();
    }

    /**
     * A method for getting the parametric equations to put into desmos
     *
     * @return The parametric equations to put into desmos
     */
    @Override
    public String getDesmosEquations() {
        StringBuilder builder = new StringBuilder();
        int lastSpot = matrices[0].getWidth() - 1;

        for (int p = 0; p < pieces; p++) {
            builder.append("For ").append(p).append("<t<").append(p + 1).append("\n");
            builder.append("(");
            for (int n = 0; n < matrices.length; n++) {
                for (int t = 0; t < termsPerPiece; t++) {
                    builder.append(matrices[n].matrix[(p * termsPerPiece) + t][lastSpot]).append("t^").append(termsPerPiece - (t + 1));
                    if (t < termsPerPiece - 1) {
                        builder.append(" + ");
                    }
                }
                if (n == matrices.length - 1) {
                    builder.append(")\n");
                } else {
                    builder.append(", ");
                }
            }
        }

        return builder.toString();
    }

}