package SplineGenerator.Applied;

import SplineGenerator.Splines.Spline;
import SplineGenerator.Util.DDirection;
import SplineGenerator.Util.DVector;

public class SimpleVelocityController {

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

    public SimpleVelocityController(int dimensions, Segmenter.Controller controller, double maximumVelocity, double minimumVelocity, double maximumAcceleration, double currentVelocity) {
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
        multiplier = velDiff / derivDiff;

    }

    public void update(DVector currentDirection) {

        if (currentDirection.equals(lastDirection)) {
            return;
        }

        // Works Okay
//        double mag = controller.getSpline().evaluateDerivative(controller.getTValue(), 1).getMagnitude();
//        if (mag > prevMag) {
//            currentVelocity += maximumAcceleration * 1.8 * (Math.abs(mag - prevMag));
//            accelerating = true;
//        } else {
//            currentVelocity -= maximumAcceleration * .9 * (Math.abs(mag - prevMag));
//            accelerating = false;
//        }
//        prevMag = mag;
//
//        if (currentVelocity >= maximumVelocity) {
//            currentVelocity = maximumVelocity;
//        } else if (currentVelocity < minimumVelocity) {
//            currentVelocity = minimumVelocity;
//        }

        double deriv = controller.getSpline().evaluateDerivative(controller.getTValue(),1).getMagnitude();
        currentVelocity = minimumVelocity + deriv * multiplier;

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
