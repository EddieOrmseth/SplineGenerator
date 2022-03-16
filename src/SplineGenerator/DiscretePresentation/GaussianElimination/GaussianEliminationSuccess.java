package SplineGenerator.DiscretePresentation.GaussianElimination;

import SplineGenerator.Util.Matrix;

import java.nio.file.Path;
import java.nio.file.Paths;

public class GaussianEliminationSuccess {

    public static void main(String... args) {

        Path path = Paths.get("C:\\Users\\eddie\\Downloads\\Programming\\GitHubRetry\\SplineGenerator\\GESuccess.txt");
        Matrix matrix = Matrix.readMatrix(path);
        matrix.debug = true;
        matrix.solve();

    }

}
