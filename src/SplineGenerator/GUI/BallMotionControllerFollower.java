package SplineGenerator.GUI;

import SplineGenerator.Applied.MotionController;
import SplineGenerator.Util.DPoint;

public class BallMotionControllerFollower extends BallVelocityDirectionController {

    private MotionController motionController;

    /**
     * A simple constructor requiring the necessary components
     *
     * @param motionController The controller that will provide directions
     * @param position         The initial position of the ball
     */
    public BallMotionControllerFollower(MotionController motionController, DPoint position) {
        super(motionController.getPositionController(), position);
        this.motionController = motionController;
    }

    @Override
    public void display(DisplayGraphics graphics) {
        motionController.update();
        controller = motionController.getPositionController();
        velocityController = motionController.getVelocityController();
        super.display(graphics);
    }

    @Override
    public void reset() {
        motionController.reset();
        super.reset();
    }

}
