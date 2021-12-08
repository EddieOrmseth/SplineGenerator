package SplineGenerator.Applied;

import SplineGenerator.Splines.Spline;

/**
 * A simplified controller for velocity
 */
public class SegmenterSimpleVelocityController implements VelocityController {

    /**
     * The controller that provides the tValue
     */
    private Segmenter.Controller controller;

    /**
     * The maximum velocity of the controller
     */
    private double maximumVelocity;

    /**
     * The minimum velocity of the controller
     */
    private double minimumVelocity;

    /**
     * The current velocity of the controller
     */
    private double currentVelocity;

    /**
     * Whether or not the controller is accelerating
     */
    private boolean accelerating = false;

    /**
     * The previous velocity of the controller
     */
    private double lastVelocity;

    /**
     * The maximum derivative magnitude
     */
    private double maxDerivMag;

    /**
     * The minimum derivative magnitude
     */
    private double minDerivMag;

    /**
     * The scalar to be used to go from to derivative interval to the velocity interval
     */
    private double multiplier;

    /**
     * A constructor requiring the basic components of the controller
     *
     * @param controller      The Segmenter.Controller for getting the spline tValue
     * @param maximumVelocity The maximum velocity
     * @param minimumVelocity The minimum velocity
     * @param currentVelocity The initial velocity
     */
    public SegmenterSimpleVelocityController(Segmenter.Controller controller, double maximumVelocity, double minimumVelocity, double currentVelocity) {
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

    /**
     * A method that can be called to update the current velocity
     */
    public void update() {

        double deriv = controller.getSpline().evaluateDerivative(controller.getTValue(), 1).getMagnitude();
        this.currentVelocity = minimumVelocity + (deriv - minDerivMag) * multiplier;

        if (this.currentVelocity >= maximumVelocity) {
            this.currentVelocity = maximumVelocity;
        } else if (this.currentVelocity < minimumVelocity) {
            this.currentVelocity = minimumVelocity;
        }

        accelerating = lastVelocity < this.currentVelocity;
        lastVelocity = this.currentVelocity;
    }

    /**
     * A method for getting the current velocity
     *
     * @return The current velocity
     */
    public double getVelocity() {
        return currentVelocity;
    }

    /**
     * A method that can be used to tell if the controller is accelerating
     *
     * @return Whether controller is accelerating or not
     */
    public boolean isAccelerating() {
        return accelerating;
    }

    /**
     * A method for setting the current velocity
     *
     * @param velocity The new velocity
     */
    public void setVelocity(double velocity) {
        this.currentVelocity = velocity;
    }

}
