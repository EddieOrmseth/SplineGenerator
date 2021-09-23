package SplineGenerator.Splines;

import SplineGenerator.Util.Matrix;
import SplineGenerator.Util.SplineMath;

/**
 * A class for creating polynomic splines
 */
public class PolynomicSpline extends Spline {

    /**
     * An enumeration for the different types of polynomic splines
     */
    public enum PolynomicType {
        Cubic,
        Quartic,
        Quintic
    }

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
        int equation = 0;

        equation = matchPositions(equation);

        for (int i = 0; i < interpolationTypes.size(); i++) {

        }
    }

    /**
     * A method for insuring the splines go through their control points
     * @param equation The number of the row / equation to start filling
     * @return The number of rows / equations used by this function
     */
    public int matchPositions(int equation) {
        int p;
        for (p = 0; p < controlPoints.size() - 1; p++) {
            insertEquation(equation, p, getEquation(0, 0), controlPoints.get(p).x, xMatrix.matrix);
            insertEquation(equation, p, getEquation(0, 0), controlPoints.get(p).y, yMatrix.matrix);

            equation++;

            insertEquation(equation, p, getEquation(0, 1), controlPoints.get(p + 1).x, xMatrix.matrix);
            insertEquation(equation, p, getEquation(0, 1), controlPoints.get(p + 1).y, yMatrix.matrix);

            equation++;
        }

        if (isClosed()) {
            insertEquation(equation, p, getEquation(0, 0), controlPoints.get(p).x, xMatrix.matrix);
            insertEquation(equation, p, getEquation(0, 0), controlPoints.get(p).y, yMatrix.matrix);

            equation++;

            insertEquation(equation, p, getEquation(0, 1), controlPoints.get(0).x, xMatrix.matrix);
            insertEquation(equation, p, getEquation(0, 1), controlPoints.get(0).y, yMatrix.matrix);

            equation++;
        }

        return equation;
    }

    /**
     * A method for getting an equation multiplied by -1
     * @param derivative The number of times to differentiate
     * @param value The value to evaluate the function at
     * @return An array of the coefficients for the function multiplied by -1
     */
    public double[] getNegativeEquation(int derivative, int value) {
        double[] equation = getEquation(derivative, value);

        for (int i = 0; i < equation.length; i++) {
            equation[i] *= -1;
        }

        return equation;
    }

    /**
     * A method that gets the list of coefficients for the nth derivative at a certain value
     * @param derivative The number of times to differentiate
     * @param value The value to evaluate the function at
     * @return An array of the coefficients for the function
     */
    public double[] getEquation(int derivative, int value) {
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
            equation[i] =  derivCo * pow;
        }

        return equation;
    }

}
