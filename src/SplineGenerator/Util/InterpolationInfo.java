package SplineGenerator.Util;

import SplineGenerator.Splines.Spline.EndBehavior;
import SplineGenerator.Splines.Spline.EndBehaviorEffect;
import SplineGenerator.Splines.Spline.InterpolationType;

/**
 * A class that holds the information necessary for interpolation at the nth derivative
 */
public class InterpolationInfo {

    /**
     * The interpolation type to be used with the points that have pieces on either side
     */
    public InterpolationType interpolationType = InterpolationType.None;

    /**
     * The interpolation type to be used on the end points that have only 1 piece on a single side
     */
    public EndBehavior endBehavior = EndBehavior.None;

    /**
     * The effect of the EndBehavior, it may effect just one side, both, or neither
     */
    public EndBehaviorEffect endEffect = EndBehaviorEffect.Both;

}
