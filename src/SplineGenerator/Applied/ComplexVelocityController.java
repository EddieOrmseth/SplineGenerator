package SplineGenerator.Applied;

import SplineGenerator.Splines.Spline;
import SplineGenerator.Util.DDirection;
import SplineGenerator.Util.DVector;

public class ComplexVelocityController implements VelocityController {

    private int dimensions;
    private Segmenter.Controller controller;

    private double maximumVelocity;
    private double minimumVelocity;
    private double maximumAcceleration;
    private double currentVelocity;

    private boolean accelerating = false;

    private DVector lastDirection;

    private double maxDerivMag;
    private double minDerivMag;

    private double multiplier;

    private double maxPercentBound = 0.4;
    private double minPercentBound = 0.1;

    private double percentMaxDerivMag;
    private double percentMinDerivMag;

    public ComplexVelocityController(int dimensions, Segmenter.Controller controller, double maximumVelocity, double minimumVelocity, double maximumAcceleration, double currentVelocity) {
        this.dimensions = dimensions;
        this.controller = controller;
        this.maximumVelocity = maximumVelocity;
        this.minimumVelocity = minimumVelocity;
        this.maximumAcceleration = maximumAcceleration;
        this.currentVelocity = currentVelocity;
        lastDirection = new DDirection(dimensions);

        Spline spline = controller.getSpline();

        maxDerivMag = Double.MIN_VALUE;
        minDerivMag = Double.MAX_VALUE;
        double value;

        for (double i = 0; i < spline.getNumPieces(); i += .01) {
            value = spline.evaluateDerivative(i, 1).getMagnitude();
            if (value > maxDerivMag) {
                maxDerivMag = value;
            } else if (value < minDerivMag) {
                minDerivMag = value;
            }
        }

        double velDiff = maximumVelocity - minimumVelocity;

        double derivDiff = maxDerivMag - minDerivMag;
        percentMaxDerivMag = maxDerivMag - derivDiff * maxPercentBound;
        percentMinDerivMag = minDerivMag + derivDiff * minPercentBound;

//        double derivDiff = maxDerivMag - minDerivMag;
        double percentDerivDiff = percentMaxDerivMag - percentMinDerivMag;

        multiplier = velDiff / percentDerivDiff;

        System.out.println("Max Deriv Mag: " + maxDerivMag);
        System.out.println("Min Deriv Mag: " + minDerivMag);
        System.out.println("Multiplier: " + multiplier);

    }

    public void update(DVector currentDirection) {

        if (currentDirection.equals(lastDirection)) {
            return;
        }

        double deriv = controller.getSpline().evaluateDerivative(controller.getTValue(),1).getMagnitude();
        currentVelocity = minimumVelocity + (deriv - percentMinDerivMag) * multiplier;

        if (currentVelocity >= maximumVelocity) {
            currentVelocity = maximumVelocity;
        } else if (currentVelocity < minimumVelocity) {
            currentVelocity = minimumVelocity;
        }
    }

    public double getVelocity() {
        return currentVelocity;
    }

    public boolean isAccelerating() {
        return accelerating;
    }

    public void setVelocity(double velocity) {
        this.currentVelocity = velocity;
    }

    public int getDimensions() {
        return dimensions;
    }

}
