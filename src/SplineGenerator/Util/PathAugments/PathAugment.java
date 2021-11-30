package SplineGenerator.Util.PathAugments;

import SplineGenerator.Util.DPoint;
import SplineGenerator.Util.DVector;

/**
 * A class representing objects in the space that affect the path
 */
public abstract class PathAugment {

    /**
     * The DVector representing the vector from the object to the PathAugment
     */
    protected DVector vectorBetween;

    /**
     * The DVector representing the effect of the PathAugment
     */
    protected DVector effect;

    /**
     * The number of dimensions the PathAugment exists in
     */
    protected int dimensions;

    /**
     * A simple constructor that initializes the necessary objects
     *
     * @param dimensions The number of dimensions the PathAugment exists in
     */
    protected PathAugment(int dimensions) {
        this.dimensions = dimensions;
        vectorBetween = new DVector(dimensions);
        effect = new DVector(dimensions);
    }

    /**
     * A method for determining weather or not to use this PathAugment
     *
     * @param toTarget The vector pointing from the object to the target
     * @param position The position of the object
     * @param velocity The velocity of the object
     * @return true if this PathAugment should be used, false otherwise
     */
    public abstract boolean skipAugment(DVector vectorBetween, DVector toTarget, DPoint position, DVector velocity);

    /**
     * A method for getting the vector between the given DPoint and PathAugment. The vector shall point form the PathAugment to the DPoint
     *
     * @param point The given point
     * @return The DVector between the PathAugment and the given point
     */
    public abstract DVector getVectorBetween(DPoint point);

    /**
     * A method for getting the effect of the PathAugment
     *
     * @param vectorBetween The vector from the PathAugment to the object
     * @param toTarget      The vector form the object to the target
     * @param position      The position of the object
     * @param velocity      The velocity of the object
     * @return The effect of the PathAugment on the object
     */
    public abstract DVector getEffect(DVector vectorBetween, DVector toTarget, DPoint position, DVector velocity);

    /**
     * A method for determining weather or not to use the effect of this PathAugment
     *
     * @param vectorBetween The vector from the PathAugment to object
     * @param toTarget      The vector from the object to the target
     * @param effect        The effect of the PathAugment
     * @param position      The position of the object
     * @param velocity      The velocity of the object
     * @return true if the effect should be used, false otherwise
     */
    public abstract boolean skipEffect(DVector vectorBetween, DVector toTarget, DVector effect, DPoint position, DVector velocity);

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

    /**
     * A method getting the number of dimensions the PathAugment exists in
     *
     * @return The number of dimensions the PathAugment exists in
     */
    public int getDimensions() {
        return dimensions;
    }

}