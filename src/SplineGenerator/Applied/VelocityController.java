package SplineGenerator.Applied;

import SplineGenerator.GUI.KeyBoardListener;
import SplineGenerator.Util.DDirection;
import SplineGenerator.Util.DPosVector;
import SplineGenerator.Util.DVector;

import java.awt.event.KeyEvent;

public class VelocityController {

    private int dimensions;
    private Navigator.Controller controller;

    private double maximumVelocity;
    private double minimumVelocity;
    private double maximumAcceleration;
    private double currentVelocity;

    private double accelerateThresh = .999999999999999999999;
    private boolean accelerating = false;
    private double newAngle;

    private DVector lastDirection;

    public VelocityController(int dimensions, Navigator.Controller controller, double maximumVelocity, double minimumVelocity, double maximumAcceleration, double currentVelocity) {
        this.dimensions = dimensions;
        this.controller = controller;
        this.maximumVelocity = maximumVelocity;
        this.minimumVelocity = minimumVelocity;
        this.maximumAcceleration = maximumAcceleration;
        this.currentVelocity = currentVelocity;
        lastDirection = new DDirection(dimensions);
    }

    public void update(DVector currentDirection) {

//        if (currentDirection.equals(lastDirection)) {
//            return;
//        }
//
//        if (KeyBoardListener.get(KeyEvent.VK_F)) {
//            int cat = 12;
//        }
//
//        double acceleration = lastDirection.dot(currentDirection) - accelerateThresh;
//        newAngle = lastDirection.getAngleBetween(currentDirection);
//        if (acceleration > 0) {
//            acceleration = acceleration / (1.0 - accelerateThresh);
//            accelerating = true;
//        } else {
//            acceleration = acceleration / accelerateThresh * 30;
//            accelerating = false;
//        }
//        acceleration *= maximumAcceleration;
//        if (acceleration > maximumAcceleration) {
//            acceleration = maximumAcceleration;
//        }
//        currentVelocity += acceleration;
//        if (currentVelocity >= maximumVelocity) {
//            currentVelocity = maximumVelocity;
//        } else if (currentVelocity < minimumVelocity) {
//            currentVelocity = minimumVelocity;
//        }
//        lastDirection.set(currentDirection);

        double diff = currentDirection.getMagnitude() - lastDirection.getMagnitude();
        currentVelocity += diff * 100;
        lastDirection.set(currentDirection);

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

    public double getAngle() {
        return newAngle;
    }

    public void setVelocity(double velocity) {
        this.currentVelocity = velocity;
    }

    public int getDimensions() {
        return dimensions;
    }

}
