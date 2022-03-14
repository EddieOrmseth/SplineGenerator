package SplineGenerator.Applied;

import SplineGenerator.Applied.LegacyVersions.OldVelocityController;

public class ConstantVelocityController implements OldVelocityController {

    public double velocity;

    public ConstantVelocityController(double velocity) {
        this.velocity = velocity;
    }

    @Override
    public double getVelocity() {
        return velocity;
    }

    @Override
    public boolean isAccelerating() {
        return false;
    }

    @Override
    public void update() {

    }
}
