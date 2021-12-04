package SplineGenerator.Applied;

import SplineGenerator.Util.DDirection;
import SplineGenerator.Util.DPoint;
import SplineGenerator.Util.DVector;

/**
 * An interface for things that can provide navigation
 */
public interface Navigator {

    /**
     * A method for getting a Controller that can navigate on the spline
     *
     * @return The Controller object that can navigate the spline
     */
    Controller getController();

    /**
     * An abstract class defining the methods that a controller must have
     */
    abstract class Controller {

        /**
         * A method that updates the position of the controlled object
         *
         * @param point The new position
         */
        public abstract void update(DPoint point);

        /**
         * A method for getting the direction in the form of a unit vector
         *
         * @return The direction in the form a unit vector
         */
        public abstract DVector getVector();

        /**
         * The velocity at that specified point
         *
         * @return The specified velocity
         */
        public double getVelocity() {
            return 0;
        }

        /**
         * A method that can be used to tell if the controller has completed the navigation
         *
         * @return Whether or not the controlled object has reached the end of the navigation
         */
        public abstract boolean isFinished();

    }

}
