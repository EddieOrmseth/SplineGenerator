package SplineGenerator.Applied;

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
        public abstract DVector getDirection();

        /**
         * A method for getting the position of the controller object
         *
         * @return The position of the controlled object
         */
        public abstract DPoint getPosition();

        /**
         * A method that can be used to determine if the object has reached its destination
         */
        public abstract boolean isFinished();

        public void reset() {

        }

        public void setPosition(DPoint point) {

        }

        public double getTValue() {
            return 0;
        }

    }

}
