package SplineGenerator.Applied;

import SplineGenerator.Util.DDirection;
import SplineGenerator.Util.DPoint;
import SplineGenerator.Util.DVector;
import SplineGenerator.Util.Function;
import SplineGenerator.Util.MultivariableFunctions.FiveVariableFunction;
import SplineGenerator.Util.MultivariableFunctions.FourVariableFunction;
import SplineGenerator.Util.MultivariableFunctions.ThreeVariableFunction;

import java.util.ArrayList;

/**
 * A new and improved PathFinder for smoother motion and better navigation
 */
public class PathFinderV2 implements Navigator {

    /**
     * An ArrayList<PathObject> for holding the obstacles found in the given space
     */
    private ArrayList<PathAugment> augments;

    /**
     * The PathObject that the PathFinder will try to reach
     */
    private PathAugment target;

    /**
     * A simple constructor that requires to arguments
     */
    public PathFinderV2() {
        augments = new ArrayList<>();
    }

    /**
     * A method for setting the target of the PathFinder
     *
     * @param target The new target for the PathFinder
     */
    public void setTarget(PathAugment target) {
        this.target = target;
    }

    /**
     * A method for getting the current target
     *
     * @return The current target object
     */
    public PathAugment getTarget() {
        return target;
    }

    /**
     * A method for adding PathAugments
     *
     * @param augment The PathAugment to be added
     */
    public void addAugment(PathAugment augment) {
        augments.add(augment);
    }

    /**
     * A method for removing a PathAugment
     *
     * @param augment The PathAugment to be removed
     */
    public void removeAugment(PathAugment augment) {
        augments.remove(augment);
    }

    /**
     * A method for getting the DDirection based off the position and velocity of an object
     *
     * @param position The position of the object
     * @param velocity The velocity of the object
     * @return The DVector caused by the position, velocity, and PathAugments
     */
    public DDirection getDirection(DPoint position, DVector velocity) {
        DVector finalEffect = new DVector(position.getDimensions());
        DVector toTarget = target.getVectorBetween(position);

        PathAugment augment;
        DVector vectorBetween;
        DVector effect;

        for (int i = 0; i < augments.size(); i++) {
            augment = augments.get(i);

            if (augment.skipAugment(toTarget, position, velocity)) {
                continue;
            }

            vectorBetween = augment.getVectorBetween(position);
            effect = augment.getEffect(vectorBetween, toTarget, position, velocity);

            if (!augment.skipEffect(vectorBetween, toTarget, effect, position, velocity)) {
                finalEffect.add(effect);
            }
        }

        return finalEffect.toDirection();
    }

    /**
     * A class representing objects in the space that affect the path
     */
    public static class PathAugment {

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

    @Override
    public Controller getController() {
        return null;
    }

    public class Controller extends Navigator.Controller {


        @Override
        public void update(DPoint point) {

        }

        @Override
        public DDirection getDirection() {
            return null;
        }
    }

}

