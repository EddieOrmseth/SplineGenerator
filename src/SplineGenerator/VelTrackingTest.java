package SplineGenerator;

import SplineGenerator.Util.Matrix;

public class VelTrackingTest {

    public static void main(String... args) {

        double[][] xData = {{0, 0, 1, 0}, {0, 1, 0, 1}, {2, 1, 0, -1}};
        double[][] yData = {{0, 0, 1, 0}, {0, 1, 0, 1}, {2, 1, 0, 1}};

        Matrix xMatrix = new Matrix(xData);
        Matrix yMatrix = new Matrix(yData);

        xMatrix.solve();
        yMatrix.solve();

        System.out.println(xMatrix);
        System.out.println(yMatrix);

    }

}
