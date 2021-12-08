package SplineGenerator.Applied;

import SplineGenerator.Splines.Spline;

public class SimpleVelocityController implements VelocityController {

    private int dimensions;
    private Segmenter.Controller controller;

    private double maximumVelocity;
    private double minimumVelocity;
    private double currentVelocity;

    private boolean accelerating = false;

    private double lastVelocity;

    private double maxDerivMag;
    private double minDerivMag;

    private double multiplier;

    public SimpleVelocityController(int dimensions, Segmenter.Controller controller, double maximumVelocity, double minimumVelocity, double currentVelocity) {
        this.dimensions = dimensions;
        this.controller = controller;
        this.maximumVelocity = maximumVelocity;
        this.minimumVelocity = minimumVelocity;
        this.currentVelocity = currentVelocity;
        this.lastVelocity = currentVelocity;

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

        System.out.println("Max Deriv Mag: " + maxDerivMag);
        System.out.println("Min Deriv Mag: " + minDerivMag);
        System.out.println("Multiplier: " + multiplier);

    }

    public void update() {

        double deriv = controller.getSpline().evaluateDerivative(controller.getTValue(),1).getMagnitude();
        this.currentVelocity = minimumVelocity + (deriv - minDerivMag) * multiplier;

        if (this.currentVelocity >= maximumVelocity) {
            this.currentVelocity = maximumVelocity;
        } else if (this.currentVelocity < minimumVelocity) {
            this.currentVelocity = minimumVelocity;
        }

        accelerating = lastVelocity < this.currentVelocity;
        lastVelocity = this.currentVelocity;
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
