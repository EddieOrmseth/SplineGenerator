package SplineGenerator.Util;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * A class for holding and manipulating matrices
 */
public class Matrix {

    /**
     * A double[] for holding the actual values for the matrix
     */
    public double[][] matrix; // Format: [rows][columns]

    /**
     * The threshold for setting number that are almost 1 or 0 to 1 or 0
     */
    private final static double floatThresh = 1e-10;

    /**
     * A constructor that uses the given double[] as the matrix, note this does not copy the data
     *
     * @param data The double[] to be used as the matrix
     */
    public Matrix(double[][] data) {
        matrix = data;
    }

    /**
     * A constructor for creating an empty Matrix with the specified amount of rows and columns
     *
     * @param rows    The number of rows for the Matrix
     * @param columns The number of columns for the Matrix
     */
    public Matrix(int rows, int columns) {
        matrix = new double[rows][columns];
    }

    /**
     * A copy constructor for the Matrix class
     *
     * @param matrix The matrix to be copied
     */
    public Matrix(Matrix matrix) {
        this.matrix = matrix.copyData();
    }

    /**
     * The method for solving the Matrix
     */
    public void solve() {
        solve(0, 0);
    }

    /**
     * A recursive method that solves the matrix line by line
     *
     * @param row    The row to perform the next step on
     * @param column The column to perform the next step on
     */
    private void solve(int row, int column) {
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
//                 System.out.println(this);
                solve(row, column);
            } else {
                System.out.println("Failed At This Point In Solving Matrix: \n" + this);
                throw new IllegalArgumentException("Failed to Solve Matrix");
//                solve(row + 1, column + 1);
            }
        }
    }

    /**
     * A method to be used if the value at matrix[row][column] is 0, this finds a different line that is acceptable
     *
     * @param row    The row that is unacceptable, also the row to move the new row to;
     * @param column The column of the row that cannot be 0
     * @return Whether or not a satisfactory row was found, if false the matrix may be unsolvable
     */
    public boolean gaussianArrange(int row, int column) {
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
     * A method for correcting the floating-point values that within floatThresh of either 0 or 1
     */
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

    /**
     * A method that solves the matrix using the least squares method
     *
     * @param equations The matrix with the linear equations and values in it
     * @param constants The matrix with the constants in it
     * @return The resulting matrix
     */
    public static Matrix solveWithLeastSquares(Matrix equations, Matrix constants) {
        Matrix transpose = equations.getTransposition();

        Matrix eTe = transpose.multiply(equations);
        Matrix cTe = transpose.multiply(constants);

        Matrix finalMatrix = eTe.addMatrixOntoRightSide(cTe);
        finalMatrix.solve();

        return finalMatrix;
    }

    /**
     * A method for finding out if the matrix has been solved or not
     *
     * @return Whether or not the matrix is solved
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

    /**
     * A method for inverting the matrix
     */
    public void invert() {

    }

    /**
     * A method for multiplying by another Matrix
     *
     * @param matrix The matrix to be multiplied by
     */
    public Matrix multiply(Matrix matrix) {

        if (getWidth() != matrix.getHeight()) {
            throw new IllegalArgumentException("The two matrices cannot be multiplied");
        }

        double[][] newMatrix = new double[getHeight()][matrix.getWidth()];

        for (int row = 0; row < newMatrix.length; row++) {
            for (int col = 0; col < newMatrix[row].length; col++) {
                newMatrix[row][col] = multiplyRowAndColumn(this, matrix, row, col);
            }
        }

        return new Matrix(newMatrix);
    }

    /**
     * A helper method for multiplying the row of one matrix and the column of another
     *
     * @param leftMatrix The matrix from which to multiply the row
     * @param rightMatrix The matrix from which to multiply the column
     * @param row The row to be multiplied
     * @param column The column to be multiplied
     * @return The resulting value
     */
    public double multiplyRowAndColumn(Matrix leftMatrix, Matrix rightMatrix, int row, int column) {
        double sum = 0;
        for (int i = 0; i < leftMatrix.getWidth(); i++) {
            sum += leftMatrix.get(row, i) * rightMatrix.get(i, column);
        }

        return sum;
    }

    /**
     * A method for getting the transposition of the matrix
     *
     * @return The transposed matrix
     */
    public Matrix getTransposition() {
        double[][] transposedMatrix = new double[getWidth()][getHeight()];

        for (int row = 0; row < transposedMatrix.length; row++) {
            for (int col = 0; col < transposedMatrix[row].length; col++) {
                transposedMatrix[row][col] = get(col, row);
            }
        }

        return new Matrix(transposedMatrix);
    }

    /**
     * A method for switching the position of two rows
     *
     * @param r1 The first row to be moved
     * @param r2 The second row to be moved
     */
    public void switchRow(int r1, int r2) {
        double[] tempRow = matrix[r1];
        matrix[r1] = matrix[r2];
        matrix[r2] = tempRow;
    }

    /**
     * A method for adding a multiple of one row to another
     *
     * @param addTo  The row to add to
     * @param scalar The scalar to multiple toAdd by
     * @param toAdd  The row to be added
     */
    public void addRow(int addTo, double scalar, int toAdd) {
        for (int column = 0; column < matrix[0].length; column++) {
            matrix[addTo][column] += scalar * matrix[toAdd][column];
        }
    }

    /**
     * A method for multiplying a row by a scalar
     *
     * @param row    The row to be multiplied
     * @param scalar The scalar to multiply the row by
     */
    public void multiplyRow(int row, double scalar) {
        for (int column = 0; column < matrix[0].length; column++) {
            matrix[row][column] *= scalar;
        }
    }

    /**
     * A method for adding a matrix onto the right side of another one
     *
     * @param matrix The matrix to be added
     */
    public Matrix addMatrixOntoRightSide(Matrix matrix) {
        double[][] newMatrix = new double[getHeight()][getWidth() + matrix.getWidth()];

        for (int row = 0; row < newMatrix.length; row++) {
            for (int col = 0; col < newMatrix[row].length; col++) {
                newMatrix[row][col] = col < getWidth() ? get(row, col) : matrix.get(row, col - getWidth());
            }
        }

        return new Matrix(newMatrix);
    }

    /**
     * A method that gets the width of Matrix
     *
     * @return The width of Matrix
     */
    public int getWidth() {
        return matrix[0].length;
    }

    /**
     * A method that gets the height of the Matrix
     *
     * @return The height of the Matrix
     */
    public int getHeight() {
        return matrix.length;
    }

    /**
     * A method for getting the value at a specified index
     *
     * @param row    The row of the value to return
     * @param column The column of the value to return
     * @return The value at the specified index
     */
    public double get(int row, int column) {
        return matrix[row][column];
    }

    /**
     * A method for getting a copy of the current data
     *
     * @return A copy of the current data
     */
    public double[][] copyData() {
        return copyData(matrix);
    }

    /**
     * A method for getting a copy of the given data
     *
     * @param data The data to copy
     * @return A copy of the given data
     */
    public double[][] copyData(double[][] data) {
        double[][] newData = new double[data.length][data[0].length];

        for (int rows = 0; rows < data.length; rows++) {
            for (int columns = 0; columns < data[0].length; columns++) {
                newData[rows][columns] = data[rows][columns];
            }
        }

        return newData;
    }

    /**
     * A method for reading a Matrix from the input.txt file
     *
     * @return The Matrix that was parsed from the file
     */
    public static Matrix readMatrix() {
        Path path = Paths.get("C:\\Users\\eddie\\Downloads\\Programming\\GitHubRetry\\SplineGenerator\\input.txt");

        List<String> rows = null;
        try {
            rows = Files.readAllLines(path);
        } catch (Exception e) {
            System.out.println("Failed to read input");
        }

        double[] row0 = parseRow(rows.get(0));

        double[][] matrix = new double[rows.size()][row0.length];
        matrix[0] = row0;

        for (int row = 1; row < rows.size(); row++) {
            matrix[row] = parseRow(rows.get(row));
        }

        return new Matrix(matrix);
    }

    /**
     * A method for parsing a single row of a string
     *
     * @param row The String to be parsed into a double[]
     * @return A double[] created from the String
     */
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

    /**
     * A method for getting a String representation of the Matrix
     *
     * @return A String representation of the Matrix
     */
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