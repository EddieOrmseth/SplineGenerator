package SplineGenerator.Applied;

import SplineGenerator.Util.DDirection;
import SplineGenerator.Util.DPoint;
import SplineGenerator.Util.DVector;
import SplineGenerator.Util.PathAugments.PathAugment;

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
     * The number of dimensions of the PathFinder
     */
    private int dimensions;

    /**
     * A simple constructor that requires to arguments
     */
    public PathFinderV2(int dimensions) {
        this.dimensions = dimensions;
        augments = new ArrayList<>();
    }

    /**
     * A method for setting the target of the PathFinder
     *
     * @param target The new target for the PathFinder
     */
    public void setTarget(PathAugment target) {
        augments.remove(this.target);
        augments.add(target);
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
        toTarget.multiplyAll(-1);

        PathAugment augment;
        DVector vectorBetween;
        DVector effect;

        for (int i = 0; i < augments.size(); i++) {
            augment = augments.get(i);
            vectorBetween = augment.getVectorBetween(position);

            if (augment.skipAugment(vectorBetween, toTarget, position, velocity)) {
                continue;
            }

            effect = augment.getEffect(vectorBetween, toTarget, position, velocity);

            if (!augment.skipEffect(vectorBetween, toTarget, effect, position, velocity)) {
                finalEffect.add(effect);
            }
        }

        return finalEffect.toDirection();
    }

    /**
     * A method for getting the dimensions of the PathFinder
     *
     * @return The dimensions of the PathFinder
     */
    public int getDimensions() {
        return dimensions;
    }

    /**
     * A method for getting a controller associated with this object
     *
     * @return The controller for the PathFinder
     */
    @Override
    public Controller getController() {
        return new Controller(this);
    }

    public class Controller extends Navigator.Controller {

        /**
         * The PathFinder object to be followed
         */
        private PathFinderV2 pathFinder;

        /**
         * The position of the controlled object
         */
        private DPoint position;

        /**
         * The previous position of the controlled object
         */
        private DPoint previousPosition;

        /**
         * The velocity of the controlled object
         */
        public DVector velocity;

        /**
         * A simple constructor for a controller that follows the pathfinder
         *
         * @param pathFinder
         */
        public Controller(PathFinderV2 pathFinder) {
            this.pathFinder = pathFinder;
            position = new DPoint(pathFinder.getDimensions());
            previousPosition = new DPoint(pathFinder.getDimensions());
            velocity = new DVector(pathFinder.getDimensions());
        }

        /**
         * A method for setting the position of the controller
         *
         * @param point The new position
         */
        @Override
        public void update(DPoint point) {
            previousPosition = position.clone();
            position = point;
            velocity = new DVector(previousPosition, position);
        }

        /**
         * A method for getting the direction signified by the PathFinder
         *
         * @return The direction to be followed
         */
        @Override
        public DDirection getDirection() {
            return pathFinder.getDirection(position, velocity);
        }
    }

}

