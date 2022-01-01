package SplineGenerator.Applied;

import SplineGenerator.Applied.LegacyVersions.OldVelocityController;
import SplineGenerator.Splines.Spline;
import SplineGenerator.Util.DPoint;
import SplineGenerator.Util.DVector;

import java.util.Arrays;
import java.util.function.Supplier;

public class ArcLengthConverter implements Navigator {

    /**
     * The spline to be followed
     */
    private Spline spline;

    /**
     * The step on the spline when finding the movements
     */
    private double onSplineStep;

    /**
     * The velocity controller
     */
    private SplineVelocityController velocityController;

    /**
     * The arcLengths for each data point
     */
    public double[] arcLengths;

    /**
     * The movement data
     */
    public DVector[] data;

    /**
     * A constructor for the FollowerGradient including all the necessary parts
     */
    public ArcLengthConverter(Spline spline, double onSplineStep, SplineVelocityController velocityController) {
        this.spline = spline;
        this.onSplineStep = onSplineStep;
        this.velocityController = velocityController;
        spline.takeNextDerivative();
    }

    public void initializeArrays() {
        int size = (int) (spline.getNumPieces() / onSplineStep);
        arcLengths = new double[size];
        data = new DVector[size];
    }

    public void computeMovement() {
        initializeArrays();
        double t = 0;
        DPoint previousPoint = new DPoint(spline.getDimensions());
        DPoint point;
        double arc = 0;

        for (int i = 0; i < data.length; i++) {

            data[i] = spline.evaluateDerivative(t, 1);
            velocityController.update(data[i].getMagnitude());
            data[i].setMagnitude(velocityController.getVelocity());

            point = spline.get(t);
            arc += point.getDistance(previousPoint);
            previousPoint.set(point);
            arcLengths[i] = arc;

//            System.out.println(data[i]);

            t += onSplineStep;
        }

    }

    public void addEndVelocity(double finalVelocity, double acceleration){
        int bobertito = 0;
        double maxVelocity = 0;
        for(int i = data.length - 1; i >= 0 ; i--){
            maxVelocity = finalVelocity + acceleration * bobertito;
            bobertito ++;
            if(data[i].getMagnitude() > maxVelocity){
                data[i].setMagnitude(maxVelocity);
            } else {
                break;
            }
        }
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Data: \n");
        for (int i = 0; i < data.length; i++) {
            builder.append(i + ": " + data[i] + "\n");
        }
        return builder.toString();
    }

    @Override
    public Controller getController() {
        return new Controller(this);
    }

    public class Controller extends Navigator.Controller implements OldVelocityController {

        private ArcLengthConverter arcLengthConverter;

        private int index = 0;

        private DPoint zeroPoint = new DPoint(0, 0);

        private Controller(ArcLengthConverter arcLengthConverter) {
            this.arcLengthConverter = arcLengthConverter;
        }

        @Override
        public void update(DPoint point) {
//            double arcLength = pointGetter.get();
            double arcLength = point.get(0);
            System.out.println("ArcLength: " + arcLength);

            int i = index;
            for (; i < arcLengthConverter.arcLengths.length; i++) {
                if (arcLengthConverter.arcLengths[i] > arcLength) {
                    i--;
                    break;
                }
            }

            index = i >= arcLengthConverter.arcLengths.length ? arcLengthConverter.arcLengths.length - 1 : i;
        }

        @Override
        public DVector getDirection() {
            return arcLengthConverter.data[index].clone();
        }

        @Override
        public DPoint getPosition() {
            return zeroPoint;
        }

        @Override
        public boolean isFinished() {
            return false;
        }

        @Override
        public double getVelocity() {
            return arcLengthConverter.data[index].getMagnitude();
        }

//        @Override
//        public void update(DVector motion) {
//
//        }

        @Override
        public boolean isAccelerating() {
            return false;
        }

        @Override
        public void update() {

        }
    }


}
