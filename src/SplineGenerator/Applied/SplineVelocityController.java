package SplineGenerator.Applied;

import SplineGenerator.Applied.LegacyVersions.OldVelocityController;
import SplineGenerator.Splines.Spline;

import java.util.function.Supplier;

/**
 * A slightly more complex velocity controller
 */
public class SplineVelocityController implements OldVelocityController {

    /**
     * The controller that provides the tValue
     */
    private Spline spline;

    /**
     * Teh supplier that causes
     */
    private Supplier<Double> derivSupplier;

    /**
     * Teh supplier that causes
     */
    private Supplier<Double> tSupplier;

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
     * The percentage of the derivative interval to be spent at maximum velocity
     */
    private double maxPercentBound;

    /**
     * The percentage of the derivative interval to be spent at minimum velocity
     */
    private double minPercentBound;

    /**
     * The maximum bound for the derivative accounting for the percent bound
     */
    private double percentMaxDerivMag;

    /**
     * The minimum bound for the derivative accounting for the percent bound
     */
    private double percentMinDerivMag;

    double tStartSlow;
    double maxAccelPerT;

    /**
     * A constructor requiring the basic components of the controller
     *
     * @param spline          The spline to be followed
     * @param maximumVelocity The maximum velocity
     * @param minimumVelocity The minimum velocity
     * @param currentVelocity The initial velocity
     */
    public SplineVelocityController(Spline spline, double maximumVelocity, double minimumVelocity, double currentVelocity, double maxPercentBound, double minPercentBound) {
        this.spline = spline;
        this.maximumVelocity = maximumVelocity;
        this.minimumVelocity = minimumVelocity;
        this.currentVelocity = currentVelocity;
        this.maxPercentBound = maxPercentBound;
        this.minPercentBound = minPercentBound;

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

        tStartSlow = spline.getNumPieces();

        System.out.println("Max Deriv Mag: " + maxDerivMag);
        System.out.println("Min Deriv Mag: " + minDerivMag);
        System.out.println("Multiplier: " + multiplier);

    }

    /**
     * A constructor requiring the basic components of the controller
     *
     * @param spline          The spline to be followed
     * @param derivSupplier   the Supplier for the update of the derivative
     * @param maximumVelocity The maximum velocity
     * @param minimumVelocity The minimum velocity
     * @param currentVelocity The initial velocity
     */
    public SplineVelocityController(Spline spline, Supplier<Double> derivSupplier, double maximumVelocity, double minimumVelocity, double currentVelocity, double maxPercentBound, double minPercentBound) {
        this(spline, maximumVelocity, minimumVelocity, currentVelocity, maxPercentBound, minPercentBound);
        this.tSupplier = derivSupplier;
    }

    /**
     * A method for adding a stop at the end of the path
     *
     * @param maxAccelPerT The maximum acceleration at the end of the path
     * @param increment The amount to jump by when searching for the tStartSlow barrier
     */
    public void addStopToEnd(double maxAccelPerT, double increment) {
        this.maxAccelPerT = maxAccelPerT;

        for (double t = spline.getNumPieces() - increment; t >= 0; t -= increment) {
            double tDiff = spline.getNumPieces() - t;
            double minVelocity = tDiff * maxAccelPerT;

            updateWithT(t);
            if (getVelocity() < minVelocity) {
                tStartSlow = t;
                System.out.println("T Start Slow: " + tStartSlow);
                return;
            }
        }

    }

    /**
     * A method that can be called to update the current velocity
     */
    public void update(double deriv) {

        currentVelocity = minimumVelocity + (deriv - percentMinDerivMag) * multiplier;

        if (currentVelocity >= maximumVelocity) {
            currentVelocity = maximumVelocity;
        } else if (currentVelocity < minimumVelocity) {
            currentVelocity = minimumVelocity;
        }

        accelerating = lastVelocity != currentVelocity ? lastVelocity <= currentVelocity : accelerating;
        lastVelocity = currentVelocity;
    }

    /**
     * A method for updating with a t value instead of the derivative
     *
     * @param t The t value of the controlled object
     */
    public void updateWithT(double t) {
        if (t < tStartSlow) {
            update(spline.evaluateDerivative(t, 1).getMagnitude());
        } else {
            accelerating = false;
            currentVelocity = (spline.getNumPieces() - t) * maxAccelPerT;
//            System.out.println("Current T: " + (t));
        }
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

    @Override
    public void update() {
//        update(derivSupplier.get());
        updateWithT(tSupplier.get());
    }

    /**
     * A method for setting the current velocity
     *
     * @param velocity The new velocity
     */
    public void setVelocity(double velocity) {
        this.currentVelocity = velocity;
    }

    /**
     * A method for resetting the controller
     */
    @Override
    public void reset() {

    }

}
