package SplineGenerator.Applied;

import SplineGenerator.Util.DPoint;
import SplineGenerator.Util.DVector;

/**
 * A class that tracks the position of an object
 */
public class PositionTracker {

    /**
     * The current position of an object
     */
    private DPoint position;

    /**
     * The previous velocity of the object
     */
    private DVector previousVelocity;

    /**
     * A simple constructor requiring the initial position
     *
     * @param initialPoint The initial position of the object
     */
    public PositionTracker(DPoint initialPoint) {
        position = initialPoint;
    }

    /**
     * A constructor requiring the initial position and velocity
     *
     * @param initialPoint The initial position of the object
     * @param velocity The initial velocity of the object
     */
    public PositionTracker(DPoint initialPoint, DVector velocity) {
        position = initialPoint;
        previousVelocity = velocity;
    }

    /**
     * A method fore directly adding distance to the position
     *
     * @param point The velocity to add
     * @param time The duration over which to add the velocity
     */
    private void addDistance(DPoint point, double time) {
        position.addNTimes(point, time);
    }

    /**
     * A method for directly setting the position
     *
     * @param point The new position
     */
    public void setPosition(DPoint point ) {
        position = point;
    }

    /**
     * A method for adding change to the current position
     *
     * @param change The change in position to be added
     */
    public void update(DPoint change) {
        position.add(change);
    }

    /**
     * A method for adding to the position based on velocity
     *
     * @param currentVelocity The current velocity
     * @param time The time since the last update
     */
    public void update(DVector currentVelocity, double time) {
        double previousSpeed = previousVelocity.getMagnitude();
        double currentSpeed = currentVelocity.getMagnitude();
        DVector avgVel = previousVelocity.add(currentVelocity);
        avgVel.multiplyAll(.5);
        double distance = .5 * ((currentSpeed - previousSpeed) / time) * (time * time) + (previousSpeed) * time;
        previousVelocity.setMagnitude(distance);
        update(previousVelocity);
        previousVelocity.set(currentVelocity);
    }

    /**
     * A method for getting the estimated position
     *
     * @return The estimated position
     */
    public DPoint get() {
        return position;
    }

}
