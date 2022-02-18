package SplineGenerator;

import SplineGenerator.Util.Matrix;

public class MatrixThing {

    public static void main(String... args) {

        Matrix mat = Matrix.readMatrix();
        mat.solve();

        System.out.println(mat);

    }

}