package SplineGenerator.Applied;

import SplineGenerator.Util.DPoint;
import SplineGenerator.Util.DVector;
import SplineGenerator.Util.Matrix;

public class UltimatePositionTracker {

    private DPoint position;
    private DVector previousVel;

    private double[][] xData;
    private double[][] yData;

    private Matrix xMatrix;
    private Matrix yMatrix;

    public UltimatePositionTracker(DPoint initialPosition, DVector initialVelocity) {
        position = new DPoint(initialPosition.getDimensions());
        position.set(initialPosition);
        previousVel = new DVector(initialVelocity.getDimensions());
        previousVel.set(initialVelocity);
        xData = new double[3][4];
        yData = new double[3][4];
        xMatrix = new Matrix(xData);
        yMatrix = new Matrix(yData);
    }

    public void update(DVector velocity, double time) {
        if (time == 0) {
            return;
        }

        // Row 1
        xData[0][0] = 0;
        yData[0][0] = 0;

        xData[0][1] = 0;
        yData[0][1] = 0;

        xData[0][2] = 1;
        yData[0][2] = 1;

        xData[0][3] = position.get(0);
        yData[0][3] = position.get(1);

        // Row 2
        xData[1][0] = 0;
        yData[1][0] = 0;

        xData[1][1] = 1;
        yData[1][1] = 1;

        xData[1][2] = 0;
        yData[1][2] = 0;

        xData[1][3] = previousVel.get(0);
        yData[1][3] = previousVel.get(1);

        // Row 3
        xData[2][0] = 2 * time;
        yData[2][0] = 2 * time;

        xData[2][1] = 1;
        yData[2][1] = 1;

        xData[2][2] = 0;
        yData[2][2] = 0;

        xData[2][3] = velocity.get(0);
        yData[2][3] = velocity.get(1);

        xMatrix.solve();
        yMatrix.solve();

        System.out.println("(" + xData[0][3] + "t^2 + " + xData[1][3] + "t + " + xData[2][3] + ", " + yData[0][3] + "t^2 + " + yData[1][3] + "t + " + yData[2][3] + ")");

        position.set(0, xData[0][3] * Math.pow(time, 2) + xData[1][3] * time + xData[2][3]);
        position.set(1, yData[0][3] * Math.pow(time, 2) + yData[1][3] * time + yData[2][3]);

        previousVel.set(velocity);
    }

    public DPoint get() {
        return position.clone();
    }

    public void setPosition(DPoint point) {
        position.set(point);
    }

}
