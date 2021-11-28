package SplineGenerator.Util.PathAugments;

import SplineGenerator.Util.DPoint;
import SplineGenerator.Util.DVector;
import SplineGenerator.Util.Function;
import SplineGenerator.Util.MultivariableFunctions.FiveVariableFunction;
import SplineGenerator.Util.MultivariableFunctions.FourVariableFunction;
import SplineGenerator.Util.MultivariableFunctions.ThreeVariableFunction;

/**
 * A class representing objects in the space that affect the path
 */
public class PathAugment {

    /**
     * The Function to be called when skipAugment is called
     */
    private ThreeVariableFunction<DVector, DPoint, DVector, Boolean> skipAugment;

    /**
     * The Function to be called when getVectorBetween is called
     */
    private Function<DPoint, DVector> getVectorBetween;

    /**
     * The Function to be called when getEffect is called
     */
    private FourVariableFunction<DVector, DVector, DPoint, DVector, DVector> getEffect;

    /**
     * The Function to be called when skipEffect is called;
     */
    private FiveVariableFunction<DVector, DVector, DVector, DPoint, DVector, Boolean> skipEffect;

    /**
     * A simple constructor that does nothing
     */
    public PathAugment() {

    }

    /**
     * A constructor that yields a ready-to-go PathAugment
     *
     * @param skipAugment      The skipAugment Function
     * @param getVectorBetween The getVectorBetween Function
     * @param getEffect        The getEffect Function
     * @param skipEffect       The skipEffect Function
     */
    public PathAugment(ThreeVariableFunction<DVector, DPoint, DVector, Boolean> skipAugment,
                       Function<DPoint, DVector> getVectorBetween,
                       FourVariableFunction<DVector, DVector, DPoint, DVector, DVector> getEffect,
                       FiveVariableFunction<DVector, DVector, DVector, DPoint, DVector, Boolean> skipEffect) {
        this.skipAugment = skipAugment;
        this.getVectorBetween = getVectorBetween;
        this.getEffect = getEffect;
        this.skipEffect = skipEffect;
    }

    /**
     * A method for setting the skipAugment Function
     *
     * @param skipAugment The skipAugment function
     */
    public void setSkipAugment(ThreeVariableFunction<DVector, DPoint, DVector, Boolean> skipAugment) {
        this.skipAugment = skipAugment;
    }

    /**
     * A method for setting the getVectorBetween Function
     *
     * @param getVectorBetween The skipAugment function
     */
    public void setGetVectorBetween(Function<DPoint, DVector> getVectorBetween) {
        this.getVectorBetween = getVectorBetween;
    }

    /**
     * A method for setting the getEffect Function
     *
     * @param getEffect The skipAugment function
     */
    public void setGetEffect(FourVariableFunction<DVector, DVector, DPoint, DVector, DVector> getEffect) {
        this.getEffect = getEffect;
    }

    /**
     * A method for setting the skipEffect Function
     *
     * @param skipEffect The skipAugment function
     */
    public void setSkipEffect(FiveVariableFunction<DVector, DVector, DVector, DPoint, DVector, Boolean> skipEffect) {
        this.skipEffect = skipEffect;
    }

    /**
     * A method for determining weather or not to use this PathAugment
     *
     * @param toTarget The vector pointing from the object to the target
     * @param position The position of the object
     * @param velocity The velocity of the object
     * @return true if this PathAugment should be used, false otherwise
     */
    public boolean skipAugment(DVector toTarget, DPoint position, DVector velocity) {
        return skipAugment.get(toTarget, position, velocity);
    }

    /**
     * A method for getting the vector between the given DPoint and PathAugment. The vector shall point form the PathAugment to the DPoint
     *
     * @param point The given point
     * @return The DVector between the PathAugment and the given point
     */
    public DVector getVectorBetween(DPoint point) {
        return getVectorBetween.get(point);
    }

    /**
     * A method for getting the effect of the PathAugment
     *
     * @param vectorBetween The vector from the PathAugment to the object
     * @param toTarget      The vector form the object to the target
     * @param position      The position of the object
     * @param velocity      The velocity of the object
     * @return The effect of the PathAugment on the object
     */
    public DVector getEffect(DVector vectorBetween, DVector toTarget, DPoint position, DVector velocity) {
        return getEffect.get(vectorBetween, toTarget, position, velocity);
    }

    /**
     * A method for determining weather or not to use the effect of this PathAugment
     *
     * @param vectorBetween The vector from the PathAugment and object
     * @param toTarget      The vector from the object to the target
     * @param effect        The effect of the PathAugment
     * @param position      The position of the object
     * @param velocity      The velocity of the object
     * @return true if the effect should be used, false otherwise
     */
    public boolean skipEffect(DVector vectorBetween, DVector toTarget, DVector effect, DPoint position, DVector velocity) {
        return skipEffect.get(vectorBetween, toTarget, effect, position, velocity);
    }

    /**
     * A method for determining if the object is moving towards this PathAugment
     *
     * @param vectorBetween The vector between the PathAugment and the object, it shall point from the PathAugment to the object
     * @param velocity      The velocity vector of the object
     * @return true if the object is moving away from the PathAugment, false otherwise
     */
    public boolean movingAwayFrom(DVector vectorBetween, DVector velocity) {
        return vectorBetween.dot(velocity) >= 0;
    }

}