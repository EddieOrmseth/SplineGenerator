package SplineGenerator;

import SplineGenerator.Util.DPoint;
import SplineGenerator.Util.Matrix;

public class MatrixTesting {

    public static void main(String... args) {

        DPoint[] points = new DPoint[]{new DPoint(-20, 30), new DPoint(-30, 25), new DPoint(-25, 15), new DPoint(10, 25)};
        double xTotal = 0, yTotal = 0;
        for (int i = 0; i < points.length; i++) {
            DPoint point = points[i];
            xTotal += point.get(0);
            yTotal += point.get(1);
            System.out.println(point);
        }

        xTotal /= points.length;
        yTotal /= points.length;

        MatrixInfo matrixInfo = getEquations(points);

        Matrix equations = new Matrix(matrixInfo.equations);
        Matrix constants = new Matrix(matrixInfo.constants);

        Matrix viewEq = new Matrix(equations);
        Matrix view = viewEq.addMatrixOntoRightSide(constants);

        for (int i = 0; i < view.getHeight(); i++) {
            System.out.print(view.get(i, 0) + "x");
            System.out.print(" + ");
            System.out.print(view.get(i, 1) + "y");
            System.out.print(" = ");
            System.out.print(view.get(i, 2));
            System.out.print("\n");
        }

        Matrix results = Matrix.solveWithLeastSquares(equations, constants);
        System.out.println(new DPoint(xTotal, yTotal));
        System.out.println(new DPoint(results.get(0, 2), results.get(1, 2)));
    }

    public static MatrixInfo getEquations(DPoint... points) {

        int numEquations = 0;
        for (int i = 0; i < points.length; i++) {
            numEquations += (points.length - 1) - i;
        }

        MatrixInfo matrixInfo = new MatrixInfo();
        matrixInfo.equations = new double[numEquations][points[0].getDimensions()];
        matrixInfo.constants = new double[numEquations][1];

        int row = 0;
        for (int l = 0; l < points.length; l++) {
            for (int r = l + 1; r < points.length; r++) {

                double[] equation = getEquation(points[l], points[r]);
                for (int i = 0; i < equation.length - 1; i++) {
                    matrixInfo.equations[row][i] = equation[i];
                }
                matrixInfo.constants[row][0] = equation[equation.length - 1];

                row++;
            }
        }

        return matrixInfo;
    }

    public static double[] getEquation(DPoint p1, DPoint p2) {

        if (p1.get(0) == p2.get(0)) {
            return new double[]{1, 0, p1.get(0)};
        } else if (p1.get(1) == p2.get(1)) {
            return new double[]{0, 1, p1.get(1)};
        }

        double numSlope = p1.get(1) - p2.get(1);
        double denSlope = p1.get(0) - p2.get(0);
        double slope = numSlope / denSlope;

        double yInt = p1.get(1) - (slope * p1.get(0));

        return new double[]{-slope, 1, yInt};
    }

    public static class MatrixInfo {
        public double[][] equations;
        public double[][] constants;
    }

}
