package SplineGenerator.Applied;

import SplineGenerator.Util.DVector;

public interface VelocityController {

    double getVelocity();

    void update(DVector currentDirection);

}
