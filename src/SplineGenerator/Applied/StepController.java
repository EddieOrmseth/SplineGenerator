package SplineGenerator.Applied;

import SplineGenerator.Splines.Spline;
import SplineGenerator.Util.DDirection;
import SplineGenerator.Util.DPoint;
import SplineGenerator.Util.DVector;
import SplineGenerator.Util.Function;

/**
 * A class for calculating the direction without precalculating any values
 */
public class StepController implements Navigator {

    /**
     * The spline to be followed
     */
    private Spline spline;

    /**
     * A Function for modifying the Vector given by the spline
     */
    private Function<DVector, DVector> derivativeModifier;

    /**
     * A Function for modifying the DVector from the current point to the nearest point on the spline
     */
    private Function<DVector, DVector> distanceModifier;

    /**
     * The maximum amount that the t value can move in one iteration
     */
    private double maxStep;

    /**
     * The number to add
     */
    private double increment;

    /**
     * A constructor requiring all the necessary components
     *
     * @param spline             The spline to be followed
     * @param derivativeModifier The function for moving forward on the spline
     * @param distanceModifier   The function for moving back towards the spline
     * @param increment          The amount to move on the spline between each check
     * @param maxStep            The maximum amount the t value can change in one iteration
     */
    public StepController(Spline spline, Function<DVector, DVector> derivativeModifier, Function<DVector, DVector> distanceModifier, double increment, double maxStep) {
        this.spline = spline;
        this.derivativeModifier = derivativeModifier;
        this.distanceModifier = distanceModifier;
        this.increment = increment;
        this.maxStep = maxStep;
    }

    public DVector modifyDerivative(DVector derivative) {
        return derivativeModifier.get(derivative);
    }

    public DVector modifyDistance(DVector distance) {
        return distanceModifier.get(distance);
    }

    public double findClosestTValue(DPoint position, double previousT) {
        double minT = previousT - maxStep;
        if (minT < 0) {
            minT = 0;
        }
        double maxT = previousT + maxStep;
        if (maxT > spline.getNumPieces()) {
            maxT = spline.getNumPieces();
        }

        double bestT = minT;
        double bestDist = position.getDistance(spline.get(minT));;

        double dist;
        for (double t = minT + increment; t < maxT; t += increment) {
            dist = position.getDistance(spline.get(t));
            if (dist < bestDist) {
                bestT = t;
            }
        }

        return bestT;
    }

    /**
     * A method for getting the spline to be followed
     *
     * @return The spline to be followed
     */
    public Spline getSpline() {
        return spline;
    }

    /**
     * A method for getting a controller
     *
     * @return A new controller for the object
     */
    @Override
    public Controller getController() {
        return new Controller(spline.getDimensions(), this);
    }

    /**
     * The Controller that navigates the spline
     */
    public class Controller extends Navigator.Controller {

        private double tValue = 0;
        private DPoint position;

        private StepController stepController;

        private DVector direction;

        public Controller(int dimensions, StepController stepController) {
            position = new DPoint(dimensions);
            this.stepController = stepController;
            direction = new DVector(dimensions);
        }

        @Override
        public void update(DPoint point) {
            position.set(point);
            tValue = stepController.findClosestTValue(position, tValue);

            DPoint splinePos = spline.get(tValue);
            DVector distance = new DVector(position, splinePos);
            DVector deriv = spline.evaluateDerivative(tValue, 1);

            distance = stepController.modifyDistance(distance);
            deriv = stepController.modifyDerivative(deriv);

            direction.set(distance);
            direction.add(deriv);
        }

        @Override
        public DVector getDirection() {
            return direction;
        }

        @Override
        public DPoint getPosition() {
            return position;
        }

        @Override
        public boolean isFinished() {
            return false;
        }

        public double getTValue() {
            return tValue;
        }

    }

}
