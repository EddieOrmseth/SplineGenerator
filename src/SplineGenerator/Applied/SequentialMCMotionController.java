package SplineGenerator.Applied;

import SplineGenerator.Applied.LegacyVersions.OldVelocityController;
import SplineGenerator.Util.DPoint;
import SplineGenerator.Util.DVector;

public class SequentialMCMotionController extends MotionController {

    private final MotionController[] motionControllers;
    int index = 0;

    public SequentialMCMotionController(MotionController... motionControllers) {
        super(null, null, motionControllers[0].positionSupplier);
        this.motionControllers = motionControllers;
    }

    @Override
    public void update() {
        update(positionSupplier.get());
    }

    @Override
    public void update(DPoint point) {
        motionControllers[index].update(point);
        if (motionControllers[index].isFinished()) {
            index++;
            System.out.println("Changing Motion Controller");
        }
    }

    /**
     * A method for getting the motion output of the controllers
     *
     * @return The desired motion
     */
    public DVector getMotion() {
        DVector motion = motionControllers[index].getPositionController().getDirection();
        motion.setMagnitude(motionControllers[index].getVelocityController().getVelocity());
        return motion;
    }

    public boolean isFinished() {
        return motionControllers[motionControllers.length - 1].isFinished();
    }

    public Navigator.Controller getPositionController() {
        return motionControllers[index].getPositionController();
    }

    public OldVelocityController getVelocityController() {
        return motionControllers[index].getVelocityController();
    }

    public void reset() {
        index = 0;
    }

}
