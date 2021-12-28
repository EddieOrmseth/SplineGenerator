package SplineGenerator;

import SplineGenerator.Util.Matrix;

public class NewMatrixTesting {

    public static void main(String... args) {

        double[][] mLeft = {{.8333333333, -.8333333333}, {.833333333, .166666666}};
        double[][] mRight = {{.09}, {.2}};

        Matrix left = new Matrix(mLeft);
        Matrix right = new Matrix(mRight);

        Matrix result = left.multiply(right);
        System.out.println(result);

    }

}
