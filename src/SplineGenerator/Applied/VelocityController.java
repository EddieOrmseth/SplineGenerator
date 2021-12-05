package SplineGenerator.Applied;

import SplineGenerator.Util.DDirection;

public class VelocityController {

    private int dimensions;
    private Navigator.Controller controller;

    private double maximumVelocity;
    private double minimumVelocity;
    private double maximumAcceleration;
    private double currentVelocity;

    private double accelerateThresh = .3;

    private DDirection lastDirection;

    public VelocityController(int dimensions, Navigator.Controller controller, double maximumVelocity, double minimumVelocity, double maximumAcceleration, double currentVelocity) {
        this.dimensions = dimensions;
        this.controller = controller;
        this.maximumVelocity = maximumVelocity;
        this.minimumVelocity = minimumVelocity;
        this.maximumAcceleration = maximumAcceleration;
        this.currentVelocity = currentVelocity;
        lastDirection = new DDirection(dimensions);
    }

    public void update(DDirection currentDirection) {
        double acceleration = lastDirection.dot(currentDirection) - accelerateThresh;
        acceleration = acceleration > 0 ? acceleration / (1.0 - accelerateThresh) : acceleration / accelerateThresh;
        acceleration *= maximumAcceleration;
        if (acceleration > maximumAcceleration) {
            acceleration = maximumAcceleration;
        }
        currentVelocity += acceleration;
        if (currentVelocity >= maximumVelocity) {
            currentVelocity = maximumVelocity;
        } else if (currentVelocity < minimumVelocity) {
            currentVelocity = minimumVelocity;
        }
        lastDirection.set(currentDirection);
    }

    public double getVelocity() {
        return currentVelocity;
    }

    public void setVelocity(double velocity) {
        this.currentVelocity = velocity;
    }

    public int getDimensions() {
        return dimensions;
    }

}
