package SplineGenerator;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Matrix {

    public final double[][] matrix; // Format: [rows][columns]
    private final static double floatThresh = 1e-10;

    public Matrix(double[][] data) {
        matrix = data;
    }

    public Matrix (int rows, int columns) {
        matrix = new double[rows][columns];
    }

    public Matrix(Matrix matrix) {
        this.matrix = matrix.copyData();
    }

    public void solve() {
        solve(0, 0);
    }

    private void solve (int row, int column) {
        if (row >= matrix.length || column >= matrix[0].length) {
            correctFloats();
            return;
        }
        if (matrix[row][column] != 0) {
            multiplyRow(row, 1 / matrix[row][column]);
            for (int i = 0; i < matrix.length; i++) {
                if (i == row) {
                    continue;
                }
                addRow(i, -matrix[i][column], row);
            }
            solve(row + 1, column + 1);
        } else {
             if (gaussianArrange(row, column)) {
                solve(row, column);
            } else {
                throw new IllegalArgumentException("Failed to Solve Matrix");
//                solve(row + 1, column + 1);
            }
        }
    }

    public void correctFloats() {
        for (int r = 0; r < matrix.length; r++) {
            for (int c = 0; c < matrix[0].length; c++) {
                if (Math.abs(1 - matrix[r][c]) < floatThresh) {
                    matrix[r][c] = 1;
                } else if (Math.abs(matrix[r][c]) < floatThresh) {
                    matrix[r][c] = 0;
                }
            }
        }
    }

    public boolean gaussianArrange(int row, int column) {
        System.out.println("Rearrange");
        if (matrix[row][column] == 0) {
            for (int r = row; r < matrix.length; r++) {
                if (matrix[r][column] != 0) {
                    switchRow(row, r);
                    return true;
                }
            }
        } else {
            return true;
        }

        return false;
    }

    /**
     *
     * @return
     */
    public boolean isSolved() {
        for (int r = 0; r < matrix.length; r++) {
            for (int c = 0; c < matrix[0].length - 1; c++) {
                if (c == r) {
                    if (matrix[r][c] != 1) {
                        return false;
                    }
                } else {
                    if (matrix[r][c] != 0) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    public void invert() {

    }

    public void multiplyMatrix(Matrix matrix) {

    }

    public void switchRow(int r1, int r2) {
        double[] tempRow = matrix[r1];
        matrix[r1] = matrix[r2];
        matrix[r2] = tempRow;
    }

    public void addRow(int addTo, double scalar, int toAdd) {
        for (int column = 0; column < matrix[0].length; column++) {
            matrix[addTo][column] += scalar * matrix[toAdd][column];
        }
    }

    public void multiplyRow(int row, double scalar) {
        for (int column = 0; column < matrix[0].length; column++) {
            matrix[row][column] *= scalar;
        }
    }

    public int getWidth() {
        return matrix[0].length;
    }

    public int getHeight() {
        return matrix.length;
    }

    public double get(int row, int column) {
        return matrix[row][column];
    }

    public double[][] copyData() {
        return copyData(matrix);
    }

    public double[][] copyData(double[][] data) {
        double[][] newData = new double[data.length][data[0].length];

        for (int rows = 0; rows < data.length; rows++) {
            for (int columns = 0; columns < data[0].length; columns++) {
                newData[rows][columns] = data[rows][columns];
            }
        }

        return newData;
    }

    public static Matrix readMatrix() {
        Path path = Paths.get("C:\\Users\\eddie\\Downloads\\Programming\\GitHubRetry\\SplineGenerator\\input.txt");

        List<String> rows = null;
        try {
            rows = Files.readAllLines(path);
        } catch (Exception e) {
            System.out.println("Failed to parse input");
        }

        double[] row0 = parseRow(rows.get(0));

        double[][] matrix = new double[rows.size()][row0.length];
        matrix[0] = row0;

        for (int row = 1; row < rows.size(); row++) {
            matrix[row] = parseRow(rows.get(row));
        }

        return new Matrix(matrix);
    }

    public static double[] parseRow(String row) {

        row = row.trim();
        ArrayList<Double> values = new ArrayList<>();

        int loc;
        while (true) {

            loc = row.indexOf(" ");

            if (loc == -1) {
                values.add(Double.parseDouble(row));
                break;
            }

            values.add(Double.parseDouble(row.substring(0, loc)));

            row = row.substring(loc + 1);

        }

        double[] rowData = new double[values.size()];
        for (int i = 0; i < values.size(); i++) {
            rowData[i] = values.get(i);
        }

        return rowData;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        for (int row = 0; row < matrix.length; row++) {
            for (int column = 0; column < matrix[0].length; column++) {
                builder.append(matrix[row][column]).append(" ");
            }

            builder.append("\n");
        }

        return builder.toString();
    }

}