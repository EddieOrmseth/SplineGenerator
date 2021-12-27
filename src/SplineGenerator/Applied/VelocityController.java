package SplineGenerator.Applied;

import SplineGenerator.Util.DVector;

public interface VelocityController extends BasicVelocityController {

    void update(DVector motion);

}
