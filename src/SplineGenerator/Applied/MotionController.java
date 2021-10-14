package SplineGenerator.Applied;

import SplineGenerator.Util.DPoint;
import SplineGenerator.Util.DVector;

public interface MotionController {

    DVector getMotion(DPoint point);

}
