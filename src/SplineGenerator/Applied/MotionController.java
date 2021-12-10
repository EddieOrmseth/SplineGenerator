package SplineGenerator.Applied;

import SplineGenerator.Util.DPoint;
import SplineGenerator.Util.DVector;

import java.util.function.Supplier;

public class MotionController {

    private Navigator.Controller navigationController;
    private VelocityController velocityController;

    private Supplier<DPoint> positionSupplier;

    public MotionController(Navigator.Controller navigationController, VelocityController velocityController, Supplier<DPoint> positionSupplier) {
        this.navigationController = navigationController;
        this.velocityController = velocityController;
        this.positionSupplier = positionSupplier;
    }

    public MotionController(Navigator.Controller navigationController, VelocityController velocityController) {
        this.navigationController = navigationController;
        this.velocityController = velocityController;
    }

    public void update() {
        navigationController.update(positionSupplier.get());
        velocityController.update();
    }

    public void update(DPoint position) {
        navigationController.update(position);
        velocityController.update();
    }

    public DVector getMotion() {
        DVector motion = navigationController.getDirection();
        motion.setMagnitude(velocityController.getVelocity());
        return motion;
    }

    public boolean isFinished() {
        return navigationController.isFinished();
    }
}
