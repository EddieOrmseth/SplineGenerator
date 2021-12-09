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

    public void update() {
        navigationController.update(positionSupplier.get());
        velocityController.update();
    }

    public DVector getMotion() {
        DVector motion = navigationController.getDirection();
        motion.setMagnitude(velocityController.getVelocity());
        return motion;
    }
}
