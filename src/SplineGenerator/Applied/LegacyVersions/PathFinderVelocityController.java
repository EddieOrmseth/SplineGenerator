package SplineGenerator.Applied.LegacyVersions;

import SplineGenerator.Applied.Navigator;
import SplineGenerator.Util.DPoint;
import SplineGenerator.Util.DVector;

public class PathFinderVelocityController implements OldVelocityController {

    private double maxVelocity;
    private double minVelocity;

    private double maxAcceleration;

    private double maxAccelerationAngle;

    private boolean accelerating;

    private Navigator.Controller controller;

    private DPoint currentPosition;
    private DVector currentPathFinderOutput;
    private double currentVelocity;

    private DPoint previousPosition;
    private DVector previousPathFinderOutput;
    private double previousVelocity;

    public PathFinderVelocityController(Navigator.Controller controller, double maxVelocity, double minVelocity, double maxAcceleration, double maxAccelerationAngle) {
        this.controller = controller;
        this.maxVelocity = maxVelocity;
        this.minVelocity = minVelocity;
        this.maxAcceleration = maxAcceleration;
        this.maxAccelerationAngle = maxAccelerationAngle;
        currentPosition = controller.getPosition().clone();
        currentPathFinderOutput = controller.getDirection().clone();
        currentVelocity = minVelocity;
        previousVelocity = minVelocity;
        previousPosition = currentPosition.clone();
        previousPathFinderOutput = currentPathFinderOutput.clone();
    }

    /**
     * A method for getting the desired velocity
     *
     * @return The desired velocity
     */
    @Override
    public double getVelocity() {
        return currentVelocity;
    }

    /**
     * A method that can be used to tell if the velocity controller is accelerating
     *
     * @return Whether or not the controller object should be accelerating
     */
    @Override
    public boolean isAccelerating() {
        return accelerating;
    }

    /**
     * A method that can be called to update the velocity controller
     */
    @Override
    public void update() {

        currentPosition = controller.getPosition();
        currentPathFinderOutput = controller.getDirection();
        if (currentPosition.equals(previousPosition)) {
            return;
        }





        if (currentVelocity > maxVelocity) {
            currentVelocity = minVelocity;
        } else if (currentVelocity < minVelocity) {
            currentVelocity = minVelocity;
        }

        accelerating = previousVelocity == currentVelocity ? accelerating : currentVelocity > previousVelocity;

        previousPosition.set(currentPosition);
        previousVelocity = currentVelocity;
        previousPathFinderOutput.set(currentPathFinderOutput);
    }
}
