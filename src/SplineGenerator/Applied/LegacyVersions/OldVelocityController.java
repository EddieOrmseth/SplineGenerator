package SplineGenerator.Applied.LegacyVersions;

import SplineGenerator.Applied.BasicVelocityController;

/**
 * An interface for objects that control velocity
 */
public interface OldVelocityController extends BasicVelocityController {

//    /**
//     * A method for getting the desired velocity
//     *
//     * @return The desired velocity
//     */
//    double getVelocity();

    /**
     * A method that can be used to tell if the velocity controller is accelerating
     *
     * @return Whether or not the controller object should be accelerating
     */
    boolean isAccelerating();

    /**
     * A method that can be called to update the velocity controller
     */
    void update();

    default void reset() {

    }

}
