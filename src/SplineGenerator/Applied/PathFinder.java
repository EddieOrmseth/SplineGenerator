package SplineGenerator.Applied;

import SplineGenerator.Util.DDirection;
import SplineGenerator.Util.DPoint;
import SplineGenerator.Util.DVector;
import SplineGenerator.Util.Function;

import java.nio.file.Path;
import java.util.ArrayList;

/**
 * A class for finding a path around objects to a certain destination
 */
public class PathFinder implements Navigator {

    /**
     * A list of the modifiers that determine the paths to be created
     */
    private ArrayList<Function<DPoint, DVector>> modifiers;

    /**
     * A Space object for holding the data
     */
    private Space<DDirection> space;

    /**
     * A method for evaluating the modifiers at the specified point
     *
     * @param point The point at which to evaluate the modifiers
     * @return The value of all the modifiers at the specified point
     */
    public DDirection evaluateModifiers(DPoint point) {
        DVector vector = new DVector(space.getDimensions());
        for (int i = 0; i < modifiers.size(); i++) {
            vector.add(modifiers.get(i).get(point));
        }

        return vector.toDirection();
    }

    public DDirection getDirection(DPoint point) {
        return space.get(point);
    }

    /**
     * A method for adding a modifier
     *
     * @param modifier The modifier to be added
     */
    public void addModifier(Function<DPoint, DVector> modifier) {
        modifiers.add(modifier);
    }

    /**
     * A method for removing a modifier
     *
     * @param modifier The modifier to be removed
     */
    public void removeModifier(Function<DPoint, DVector> modifier) {
        modifiers.remove(modifier);
    }

    /**
     * A method for computing the motion caused by the modifiers
     */
    public void compute() {
        for (int i = 0; i < space.size(); i++) {
            space.set(i, evaluateModifiers(space.indexToPoint(i)));
        }
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
        private PathFinder pathFinder;

        /**
         * The position of the controlled object
         */
        private DPoint position;

        /**
         * A simple constructor for a controller that follows the pathfinder
         *
         * @param pathFinder
         */
        public Controller(PathFinder pathFinder) {
            this.pathFinder = pathFinder;
        }

        /**
         * A method for setting the position of the controller
         *
         * @param point The new position
         */
        @Override
        public void update(DPoint point) {
            position = point;
        }

        /**
         * A method for getting the DDirection computed by the PathFinder
         *
         * @return The DDirection computed by the PathFinder
         */
        @Override
        public DDirection getDirection() {
            return pathFinder.getDirection(position);
        }
    }

}
