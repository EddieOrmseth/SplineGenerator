package SplineGenerator.Applied;

import SplineGenerator.Applied.LegacyVersions.OldVelocityController;
import SplineGenerator.Util.DPoint;
import SplineGenerator.Util.DVector;

import java.util.function.Supplier;

/**
 * A controller for complete motion, includes direction and velocity;
 */
public class MotionController {

    /**
     * The direction controller
     */
    private Navigator.Controller navigationController;

    /**
     * The velocity controller
     */
    private OldVelocityController velocityController;

    /**
     * Optional: the object that supplies the current position;
     */
    protected Supplier<DPoint> positionSupplier;

    /**
     * A constructor that requires a position supplier
     *
     * @param navigationController The direction controller to be followed
     * @param velocityController The velocity controller to be followed
     * @param positionSupplier The position supplier
     */
    public MotionController(Navigator.Controller navigationController, OldVelocityController velocityController, Supplier<DPoint> positionSupplier) {
        this.navigationController = navigationController;
        this.velocityController = velocityController;
        this.positionSupplier = positionSupplier;
    }

    /**
     * A constructor that does not require a position supplier
     *
     * @param navigationController The direction controller to be followed
     * @param velocityController The velocity controller to be followed
     */
    public MotionController(Navigator.Controller navigationController, OldVelocityController velocityController) {
        this.navigationController = navigationController;
        this.velocityController = velocityController;
    }

    /**
     * A method for updating the controllers, the position is retrieved from the position supplier
     */
    public void update() {
        navigationController.update(positionSupplier.get());
        velocityController.update();
//        update(positionSupplier.get()); // DO NOT DO THIS
    }

    /**
     * A method for updating the controllers
     *
     * @param position The position that is fed to the navigation controller
     */
    public void update(DPoint position) {
        navigationController.update(position);
        velocityController.update();
    }

    /**
     * A method for getting the motion output of the controllers
     *
     * @return The desired motion
     */
    public DVector getMotion() {
        DVector motion = navigationController.getDirection();
        motion.setMagnitude(velocityController.getVelocity());
        return motion;
    }

    public void reset() {
        navigationController.reset();
        velocityController.reset();
    }

    /**
     * A method to determine of the navigator has finished its path
     *
     * @return Whether or not the navigator has finished its path
     */
    public boolean isFinished() {
        return navigationController.isFinished();
    }

    public Navigator.Controller getPositionController() {
        return navigationController;
    }

    public OldVelocityController getVelocityController() {
        return velocityController;
    }

    public Supplier<DPoint> getPositionSupplier() {
        return positionSupplier;
    }

}
