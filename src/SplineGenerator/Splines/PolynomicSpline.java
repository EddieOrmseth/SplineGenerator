package SplineGenerator.Splines;

import SplineGenerator.Util.InterpolationInfo;
import SplineGenerator.Util.Matrix;
import SplineGenerator.Util.SplineMath;

/**
 * A class for creating polynomic splines
 */
public class PolynomicSpline extends Spline {

    /**
     * The field that stores the subtype of PolynomicSpline to be created
     */
    private PolynomicType polynomicType;

    /**
     * A nice and easy default constructor for PolynomicSplines
     */
    public PolynomicSpline() {
        super(SplineType.Polynomic);
        setPolynomicType(PolynomicType.Cubic);
    }

    /**
     * A method for setting the PolynomicType
     *
     * @param polynomicType The PolynomicType to be set to
     */
    public void setPolynomicType(PolynomicType polynomicType) {
        this.polynomicType = polynomicType;
        switch (polynomicType) {
            case Cubic:
                termsPerPiece = 4;
                break;
            case Quartic:
                termsPerPiece = 5;
                break;
            case Quintic:
                termsPerPiece = 6;
                break;
        }
    }

    /**
     * The polynomic specific implementation of initializeMatrices
     */
    @Override
    public void initializeMatrices() {
        pieces = isClosed() ? controlPoints.size() : controlPoints.size() - 1;

        numTotalTerms = termsPerPiece * pieces;
        equationLength = numTotalTerms + 1;

        xMatrix = new Matrix(numTotalTerms, equationLength);
        yMatrix = new Matrix(numTotalTerms, equationLength);
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
                    setMiddleCatmulRomSlopes(i);
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

        for (int i = 0; i < addedRowsX.size(); i++) {
            insertEquation(row, 0, addedRowsX.get(i), xMatrix.matrix);
            insertEquation(row, 0, addedRowsY.get(i), yMatrix.matrix);
            row++;
        }

        System.out.println(this);

        xMatrix.solve();
        yMatrix.solve();
    }

    public int setEndingEquations(int row, InterpolationInfo info, int derivative) {
        switch (info.endBehavior) {
            case Hermite:
                switch (info.endEffect) {
                    case Both:
                        setValue(row, 0, derivative, 0, controlPoints.get(0).headings.get(derivative - 1).x, xMatrix.matrix);
                        setValue(row, 0, derivative, 0, controlPoints.get(0).headings.get(derivative - 1).y, yMatrix.matrix);
                        row++;
                        setValue(row, pieces - 1, derivative, 1, controlPoints.get(controlPoints.size() - 1).headings.get(derivative - 1).y, yMatrix.matrix);
                        setValue(row, pieces - 1, derivative, 1, controlPoints.get(controlPoints.size() - 1).headings.get(derivative - 1).x, xMatrix.matrix);
                        row++;
                        break;
                    case Beginning:
                        setValue(row, 0, derivative, 0, controlPoints.get(0).headings.get(derivative - 1).x, xMatrix.matrix);
                        setValue(row, 0, derivative, 0, controlPoints.get(0).headings.get(derivative - 1).y, yMatrix.matrix);
                        row++;
                        break;
                    case Ending:
                        setValue(row, pieces - 1, derivative, 1, controlPoints.get(controlPoints.size() - 1).headings.get(derivative - 1).x, xMatrix.matrix);
                        setValue(row, pieces - 1, derivative, 1, controlPoints.get(controlPoints.size() - 1).headings.get(derivative - 1).y, yMatrix.matrix);
                        row++;
                        break;
                }
                break;
            case CatmulRom:
                if (derivative == 1) {
                    setValue(row, 0, derivative, 0, controlPoints.get(1).x - controlPoints.get(0).x, xMatrix.matrix);
                    setValue(row, 0, derivative, 0, controlPoints.get(1).y - controlPoints.get(0).y, yMatrix.matrix);
                    row++;
                    setValue(row, pieces - 1, derivative, 1, controlPoints.get(controlPoints.size() - 1).x - controlPoints.get(controlPoints.size() - 2).x, xMatrix.matrix);
                    setValue(row, pieces - 1, derivative, 1, controlPoints.get(controlPoints.size() - 1).y - controlPoints.get(controlPoints.size() - 2).y, yMatrix.matrix);
                    row++;
                    break;
                }
                switch (info.endEffect) {
                    case Both:
                        setValue(row, 0, derivative, 0, controlPoints.get(1).headings.get(derivative - 2).x - controlPoints.get(0).headings.get(derivative - 2).x, xMatrix.matrix);
                        setValue(row, 0, derivative, 0, controlPoints.get(1).headings.get(derivative - 2).y - controlPoints.get(0).headings.get(derivative - 2).y, yMatrix.matrix);
                        row++;
                        setValue(row, pieces - 1, derivative, 1, controlPoints.get(controlPoints.size() - 1).headings.get(derivative - 2).x - controlPoints.get(controlPoints.size() - 2).headings.get(derivative - 2).x, xMatrix.matrix);
                        setValue(row, pieces - 1, derivative, 1, controlPoints.get(controlPoints.size() - 1).headings.get(derivative - 2).y - controlPoints.get(controlPoints.size() - 2).headings.get(derivative - 2).y, yMatrix.matrix);
                        row++;
                        break;
                    case Beginning:
                        setValue(row, 0, derivative, 0, controlPoints.get(1).headings.get(derivative - 2).x - controlPoints.get(0).headings.get(derivative - 2).x, xMatrix.matrix);
                        setValue(row, 0, derivative, 0, controlPoints.get(1).headings.get(derivative - 2).y - controlPoints.get(0).headings.get(derivative - 2).y, yMatrix.matrix);
                        row++;
                        break;
                    case Ending:
                        setValue(row, pieces - 1, derivative, 1, controlPoints.get(controlPoints.size() - 1).headings.get(derivative - 2).x - controlPoints.get(controlPoints.size() - 2).headings.get(derivative - 2).x, xMatrix.matrix);
                        setValue(row, pieces - 1, derivative, 1, controlPoints.get(controlPoints.size() - 1).headings.get(derivative - 2).y - controlPoints.get(controlPoints.size() - 2).headings.get(derivative - 2).y, yMatrix.matrix);
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
        setValue(row, 0, 0, 0, controlPoints.get(0).x, xMatrix.matrix);
        setValue(row, 0, 0, 0, controlPoints.get(0).y, yMatrix.matrix);
        row++;

        // ControlPoints with pieces on both sides
        for (int i = 1; i < controlPoints.size() - 1; i++) {
            setValue(row, i - 1, 0, 1, controlPoints.get(i).x, xMatrix.matrix);
            setValue(row, i - 1, 0, 1, controlPoints.get(i).y, yMatrix.matrix);
            row++;
            setValue(row, i, 0, 0, controlPoints.get(i).x, xMatrix.matrix);
            setValue(row, i, 0, 0, controlPoints.get(i).y, yMatrix.matrix);
            row++;
        }

        // The last ControlPoint
        setValue(row, controlPoints.size() - 2, 0, 1, controlPoints.get(controlPoints.size() - 1).x, xMatrix.matrix);
        setValue(row, controlPoints.size() - 2, 0, 1, controlPoints.get(controlPoints.size() - 1).y, yMatrix.matrix);
        row++;

        if (isClosed()) { // Match the positions of the piece connecting the beginning and end
            setValue(row, pieces - 1, 0, 0, controlPoints.get(controlPoints.size() - 1).x, xMatrix.matrix);
            setValue(row, pieces - 1, 0, 0, controlPoints.get(controlPoints.size() - 1).y, yMatrix.matrix);
            row++;
            setValue(row, pieces - 1, 0, 1, controlPoints.get(0).x, xMatrix.matrix);
            setValue(row, pieces - 1, 0, 1, controlPoints.get(0).y, yMatrix.matrix);
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
            linkValues(row, i - 1, i, derivative, 1, 0, xMatrix.matrix);
            linkValues(row, i - 1, i, derivative, 1, 0, yMatrix.matrix);
            row++;
        }

        if (isClosed()) {
            linkValues(row, 0, pieces - 1, derivative, 0, 1, xMatrix.matrix);
            linkValues(row, 0, pieces - 1, derivative, 0, 1, yMatrix.matrix);
            row++;
            linkValues(row, pieces - 2, pieces - 1, derivative, 1, 0, xMatrix.matrix);
            linkValues(row, pieces - 2, pieces - 1, derivative, 1, 0, yMatrix.matrix);
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
            setValue(row, i - 1, derivative, 1, controlPoints.get(i).headings.get(derivative - 1).x, xMatrix.matrix);
            setValue(row, i - 1, derivative, 1, controlPoints.get(i).headings.get(derivative - 1).y, yMatrix.matrix);
            row++;
            setValue(row, i, derivative, 0, controlPoints.get(i).headings.get(derivative - 1).x, xMatrix.matrix);
            setValue(row, i, derivative, 0, controlPoints.get(i).headings.get(derivative - 1).y, yMatrix.matrix);
            row++;
        }

        if (isClosed()) {
            // The first ControlPoint
            setValue(row, 0, derivative, 0, controlPoints.get(0).headings.get(derivative - 1).x, xMatrix.matrix);
            setValue(row, 0, derivative, 0, controlPoints.get(0).headings.get(derivative - 1).y, yMatrix.matrix);
            row++;

            // The last ControlPoint
            setValue(row, controlPoints.size() - 2, derivative, 1, controlPoints.get(controlPoints.size() - 1).headings.get(derivative - 1).x, xMatrix.matrix);
            setValue(row, controlPoints.size() - 2, derivative, 1, controlPoints.get(controlPoints.size() - 1).headings.get(derivative - 1).y, yMatrix.matrix);
            row++;

            setValue(row, pieces - 1, derivative, 0, controlPoints.get(controlPoints.size() - 1).headings.get(derivative - 1).x, xMatrix.matrix);
            setValue(row, pieces - 1, derivative, 0, controlPoints.get(controlPoints.size() - 1).headings.get(derivative - 1).y, yMatrix.matrix);
            row++;
            setValue(row, pieces - 1, derivative, 1, controlPoints.get(0).headings.get(derivative - 1).x, xMatrix.matrix);
            setValue(row, pieces - 1, derivative, 1, controlPoints.get(0).headings.get(derivative - 1).y, yMatrix.matrix);
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
     * A method for getting the parametric equations to put into desmos
     *
     * @return The parametric equations to put into desmos
     */
    @Override
    public String getDesmosEquations() {
        StringBuilder builder = new StringBuilder();
        int lastSpot = xMatrix.getWidth() - 1;

        for (int p = 0; p < pieces; p++) {
            builder.append("For ").append(p).append("<t<").append(p + 1).append("\n");
            builder.append("(");
            for (int t = 0; t < termsPerPiece; t++) {
//                builder.append("(").append(xMatrix.get((i * termsPerPiece) + 0, lastSpot)).append("t^3 + ").append(xMatrix.get((i * termsPerPiece) + 1, lastSpot)).append("t^2 + ").append(xMatrix.get((i * termsPerPiece) + 2, lastSpot)).append("t + ").append(xMatrix.get((i * termsPerPiece) + 3, lastSpot));
//                builder.append(", ").append(yMatrix.get((i * termsPerPiece) + 0, lastSpot)).append("t^3 + ").append(yMatrix.get((i * termsPerPiece) + 1, lastSpot)).append("t^2 + ").append(yMatrix.get((i * termsPerPiece) + 2, lastSpot)).append("t + ").append(yMatrix.get((i * termsPerPiece) + 3, lastSpot)).append(")\n");
                builder.append(xMatrix.matrix[(p * termsPerPiece) + t][lastSpot]).append("t^").append(termsPerPiece - (t + 1));
                if (t < termsPerPiece - 1) {
                    builder.append(" + ");
                }
            }
            builder.append(", ");
            for (int t = 0; t < termsPerPiece; t++) {
//                builder.append("(").append(xMatrix.get((i * termsPerPiece) + 0, lastSpot)).append("t^3 + ").append(xMatrix.get((i * termsPerPiece) + 1, lastSpot)).append("t^2 + ").append(xMatrix.get((i * termsPerPiece) + 2, lastSpot)).append("t + ").append(xMatrix.get((i * termsPerPiece) + 3, lastSpot));
//                builder.append(", ").append(yMatrix.get((i * termsPerPiece) + 0, lastSpot)).append("t^3 + ").append(yMatrix.get((i * termsPerPiece) + 1, lastSpot)).append("t^2 + ").append(yMatrix.get((i * termsPerPiece) + 2, lastSpot)).append("t + ").append(yMatrix.get((i * termsPerPiece) + 3, lastSpot)).append(")\n");
                builder.append(yMatrix.matrix[(p * termsPerPiece) + t][lastSpot]).append("t^").append(termsPerPiece - (t + 1));
                if (t < termsPerPiece - 1) {
                    builder.append(" + ");
                }
            }
            builder.append(")\n");
        }

        return builder.toString();
    }

    /**
     * An enumeration for the different types of polynomic splines
     */
    public enum PolynomicType {
        /**
         * The spline is of the 3rd order (4 terms)
         */
        Cubic,
        /**
         * The spline is of the 4th order (5 terms)
         */
        Quartic,
        /**
         * The spline is of the 5th order (6 terms)
         */
        Quintic
    }

}