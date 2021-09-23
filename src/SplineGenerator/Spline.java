package SplineGenerator;

import SplineGenerator.Util.ControlPoint;
import SplineGenerator.Util.Direction;
import SplineGenerator.Util.Matrix;

import java.util.ArrayList;

public class Spline {

    public enum SplineType {
        Cubic,
        Quartic,
        Quintic,
        Trigonometric,
        Hyperbolic
    }

    public enum InterpolationMethod {
        Natural,
        Hermite,
        CatmulRom,
        Cardinal
    }

    private ArrayList<ControlPoint> controlPoints;
    private Matrix xMatrix;
    private Matrix yMatrix;

    public SplineType splineType = SplineType.Cubic;
    public InterpolationMethod interpolationMethod = InterpolationMethod.Natural;
    public boolean closed = false;

    private int splineLength;
    private int equationLength;
    private int numEquations;
    private int numTotalTerms;

    public Spline() {
        controlPoints = new ArrayList<>();
    }

    public void addControlPoint(ControlPoint controlPoint) {
        controlPoint.t = controlPoints.size();
        controlPoints.add(controlPoint);
    }

    public void removeControlPoint(ControlPoint controlPoint) {
        int index = controlPoints.indexOf(controlPoint);
        controlPoints.remove(controlPoint);
        for (int i = index; i < controlPoints.size(); i++) {
            controlPoints.get(i).t--;
        }
    }

    public void setCurrentOrder() {
        for (int i = 0; i < controlPoints.size(); i++) {
            controlPoints.get(i).t = i;
        }
    }

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

    public ControlPoint getFirst() {
        int index = 0;
        for (int i = 1; i < controlPoints.size(); i++) {
            if (controlPoints.get(i).t < controlPoints.get(index).t) {
                index = i;
            }
        }

        return controlPoints.get(index);
    }

    public void initializeMatrices() {
        numEquations = closed ? controlPoints.size() : controlPoints.size() - 1;
        if (splineType == SplineType.Cubic) {
            splineLength = 4;
        } else {
            splineLength = 6;
        }
        numTotalTerms = splineLength * numEquations;
        equationLength = numTotalTerms + 1;

        xMatrix = new Matrix(numTotalTerms, equationLength);
        yMatrix = new Matrix(numTotalTerms, equationLength);
    }

    public void generate3() {
        int equation = 0;
        initializeMatrices();

        equation = matchPositions(equation);

        switch (interpolationMethod) {
            case Natural:
                if (closed) {
                    equation = matchAllNatural(equation);
                } else {
                    equation = matchGivenInitialSlopes(equation);
                    equation = matchMiddlingNatural(equation);
                }
                break;
            case CatmulRom:
                setCatmulRomSlopes();
            case Hermite:
                equation = matchGivenAllSlopes(equation);
                break;
            case Cardinal:
            default:
                throw new IllegalArgumentException("That Interpolation Method Is Not Currently Supported");
        }

        System.out.println(xMatrix);
        System.out.println(yMatrix);

        xMatrix.solve();
        yMatrix.solve();
    }

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

        if (closed) {
            insertEquation(equation, p, getEquation(0, 0), controlPoints.get(p).x, xMatrix.matrix);
            insertEquation(equation, p, getEquation(0, 0), controlPoints.get(p).y, yMatrix.matrix);

            equation++;

            insertEquation(equation, p, getEquation(0, 1), controlPoints.get(0).x, xMatrix.matrix);
            insertEquation(equation, p, getEquation(0, 1), controlPoints.get(0).y, yMatrix.matrix);

            equation++;
        }

        return equation;
    }

    public int matchAllNatural(int equation) {
        equation = matchInitialNatural(equation);
        equation = matchMiddlingNatural(equation);
        return equation;
    }

    public int matchInitialNatural(int equation) {

        for (int d = 1; d < splineLength - 1; d++) {
            insertEquation(equation, 0, getNegativeEquation(d, 0), xMatrix.matrix);
            insertEquation(equation, 0, getNegativeEquation(d, 0), yMatrix.matrix);

            insertEquation(equation, numEquations - 1, getEquation(d, 1), 0, xMatrix.matrix);
            insertEquation(equation, numEquations - 1, getEquation(d, 1), 0, yMatrix.matrix);

            equation++;
        }

        return equation;
    }

    public int matchMiddlingNatural(int equation) {

        for (int e = 0; e < numEquations - 1; e++) {
            for (int d = 1; d < splineLength - 1; d++) {
                insertEquation(equation, e, getNegativeEquation(d, 1), xMatrix.matrix);
                insertEquation(equation, e, getNegativeEquation(d, 1), yMatrix.matrix);

                insertEquation(equation, e + 1, getEquation(d, 0), 0, xMatrix.matrix);
                insertEquation(equation, e + 1, getEquation(d, 0), 0, yMatrix.matrix);

                equation++;
            }
        }

        return equation;
    }

    public int matchGivenAllSlopes(int equation) {
        equation = matchGivenInitialSlopes(equation);
        equation = matchGivenMiddlingSlopes(equation);
        return equation;
    }

    public int matchGivenInitialSlopes(int equation) {

        insertEquation(equation, 0, getEquation(1, 0), controlPoints.get(0).firstDer.x, xMatrix.matrix);
        insertEquation(equation, 0, getEquation(1, 0), controlPoints.get(0).firstDer.y, yMatrix.matrix);

        equation++;

        insertEquation(equation, numEquations - 1, getEquation(1, 1), controlPoints.get(controlPoints.size() - 1).firstDer.x, xMatrix.matrix);
        insertEquation(equation, numEquations - 1, getEquation(1, 1), controlPoints.get(controlPoints.size() - 1).firstDer.y, yMatrix.matrix);

        equation++;

        return equation;
    }

    public int matchGivenMiddlingSlopes(int equation) {
        int i;
        for (i = 1; i < controlPoints.size() - 1; i++) {

            insertEquation(equation, i - 1, getEquation(1, 1), controlPoints.get(i).firstDer.x, xMatrix.matrix);
            insertEquation(equation, i - 1, getEquation(1, 1), controlPoints.get(i).firstDer.y, yMatrix.matrix);

            equation++;

            insertEquation(equation, i, getEquation(1, 0), controlPoints.get(i).firstDer.x, xMatrix.matrix);
            insertEquation(equation, i, getEquation(1, 0), controlPoints.get(i).firstDer.y, yMatrix.matrix);

            equation++;

        }

        if (closed) {

            insertEquation(equation, i - 1, getEquation(1, 1), controlPoints.get(i).firstDer.x, xMatrix.matrix);
            insertEquation(equation, i - 1, getEquation(1, 1), controlPoints.get(i).firstDer.y, yMatrix.matrix);

            equation++;

            insertEquation(equation, i, getEquation(1, 0), controlPoints.get(i).firstDer.x, xMatrix.matrix);
            insertEquation(equation, i, getEquation(1, 0), controlPoints.get(i).firstDer.y, yMatrix.matrix);

            equation++;

        }

        return equation;
    }

    public void setCatmulRomSlopes() {
        double xDiff;
        double yDiff;
        for (int i = 1; i < controlPoints.size() - 1; i++) {
            xDiff = controlPoints.get(i + 1).x - controlPoints.get(i - 1).x;
            yDiff = controlPoints.get(i + 1).y - controlPoints.get(i - 1).y;

            controlPoints.get(i).firstDer = new Direction(xDiff, yDiff);
        }

        if (closed) {
            xDiff = controlPoints.get(1).x - controlPoints.get(controlPoints.size() - 1).x;
            yDiff = controlPoints.get(1).y - controlPoints.get(controlPoints.size() - 1).y;

            controlPoints.get(0).firstDer = new Direction(xDiff, yDiff);

            xDiff = controlPoints.get(0).x - controlPoints.get(controlPoints.size() - 2).x;
            yDiff = controlPoints.get(0).y - controlPoints.get(controlPoints.size() - 2).y;

            controlPoints.get(controlPoints.size() - 1).firstDer = new Direction(xDiff, yDiff);
        }
    }

    public void insertEquation(int row, int equationNum, double[] equation, double finalValue, double[][] matrix) {
        for (int i = 0; i < equation.length; i++) {
            matrix[row][equationNum * equation.length + i] = equation[i];
        }
        matrix[row][matrix[0].length - 1] = finalValue;
    }

    public void insertEquation(int row, int equationNum, double[] equation, double[][] matrix) {
        for (int i = 0; i < equation.length; i++) {
            matrix[row][equationNum * equation.length + i] = equation[i];
        }
    }

    public double[] getNegativeEquation(int derivative, int value) {
        double[] equation = getEquation(derivative, value);

        for (int i = 0; i < equation.length; i++) {
            equation[i] *= -1;
        }

        return equation;
    }

    public double[] getEquation(int derivative, int value) {
        double[] equation;

        if (splineType == SplineType.Cubic) {
            equation = new double[4];
        } else {
            equation = new double[6];
        }

        for (int i = 0; i < equation.length; i++) {
            double derivCo = getDerivativeCoefficient((equation.length - 1) - i, derivative);
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

    public double getDerivativeCoefficient(int power, int n) {
        int coefficient = 1;

        for (int i = 0; i < n; i++) {
            coefficient *= power;
            power--;
        }

        return coefficient;
    }

    @Override
    public String toString() {
        return "X Matrix:\n" + xMatrix + "\nY Matrix:\n" + yMatrix;
    }

    public String getDesmosEquations() {
        StringBuilder builder = new StringBuilder();
        int lastSpot = xMatrix.getWidth() - 1;

        for (int i = 0; i < numEquations; i++) {
            builder.append("For ").append(i).append("<t<").append(i + 1).append("\n");
            builder.append("(").append(xMatrix.get((i * 4) + 0, lastSpot)).append("t^3 + ").append(xMatrix.get((i * 4) + 1, lastSpot)).append("t^2 + ").append(xMatrix.get((i * 4) + 2, lastSpot)).append("t + ").append(xMatrix.get((i * 4) + 3, lastSpot));
            builder.append(", ").append(yMatrix.get((i * 4) + 0, lastSpot)).append("t^3 + ").append(yMatrix.get((i * 4) + 1, lastSpot)).append("t^2 + ").append(yMatrix.get((i * 4) + 2, lastSpot)).append("t + ").append(yMatrix.get((i * 4) + 3, lastSpot)).append(")\n");
        }

        return builder.toString();
    }

}
