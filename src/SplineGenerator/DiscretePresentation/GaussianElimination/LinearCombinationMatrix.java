package SplineGenerator.DiscretePresentation.GaussianElimination;

import SplineGenerator.Util.Matrix;

import java.nio.file.Path;
import java.nio.file.Paths;

public class LinearCombinationMatrix {

    public static void main(String... args) {

        Path path = Paths.get("C:\\Users\\eddie\\Downloads\\Programming\\GitHubRetry\\SplineGenerator\\GEFailure.txt");
        Matrix matrix = Matrix.readMatrix(path);

        matrix.multiplyRow(3, .38);
        matrix.addRow(3, .5, 4);
        matrix.addRow(3, 2.3, 2);

        matrix.debug = true;
        matrix.solve();



    }

}
