package SplineGenerator.GUI;

import SplineGenerator.Applied.Navigator;
import SplineGenerator.Applied.LegacyVersions.OldVelocityController;
import SplineGenerator.Applied.StepController;
import SplineGenerator.Util.DPoint;
import SplineGenerator.Util.DVector;

import java.awt.event.KeyEvent;

/**
 * A class representing a Follower that can be configured to move at a specific velocity
 */
public class BallVelocityDirectionController extends BallDirectionFollower {

    /**
     * The velocity controller for velocity
     */
    public OldVelocityController velocityController;

    /**
     * The previous movement of the object
     */
    private DVector lastMovement;

    /**
     * A simple constructor requiring the necessary components
     *
     * @param controller The controller that will provide directions
     * @param position   The initial position of the ball
     */
    public BallVelocityDirectionController(Navigator.Controller controller, DPoint position) {
        super(controller, position);
        lastMovement = new DVector(0, 0);
    }

    /**
     * The method to be called constantly as to move the ball
     *
     * @param graphics The object to display on
     */
    @Override
    public void display(DisplayGraphics graphics) {

        long now = System.currentTimeMillis();
        double delta = (now - lastTime) / 1000.0;

        if (!arrowPressed()) {
            if (!KeyBoardListener.get(KeyEvent.VK_SHIFT) && delta != 0) {

                controller.update(position.clone());
                DVector direction = controller.getDirection();
                lastMovement.set(direction);

                velocityController.update();
                DVector movement = direction.clone();

//                movement.setMagnitude(movementLength * delta);
                movement.setMagnitude(velocityController.getVelocity() * delta);
                position.add(movement);

            } else {
                lastTime = System.currentTimeMillis();
            }

        } else {

            if (KeyBoardListener.get(KeyEvent.VK_LEFT)) {
                position.add(graphics.xDim, -movementLength * delta);
            }
            if (KeyBoardListener.get(KeyEvent.VK_RIGHT)) {
                position.add(graphics.xDim, movementLength * delta);
            }
            if (KeyBoardListener.get(KeyEvent.VK_UP)) {
                position.add(graphics.yDim, movementLength * delta);
            }
            if (KeyBoardListener.get(KeyEvent.VK_DOWN)) {
                position.add(graphics.yDim, -movementLength * delta);
            }

        }

        lastTime = now;

        if (KeyBoardListener.get(KeyEvent.VK_SPACE)) {
            reset();
        }

        paint(graphics);
    }

    public void reset() {
        position.set(initialPosition);
        controller.reset();
    }

    /**
     * A method for painting the follower
     *
     * @param graphics The object on which to paint
     */
    public void paint(DisplayGraphics graphics) {
        graphics.paintPoint(position.clone(), 0, 1, color);
        graphics.paintVector(position.clone(), lastMovement.clone());
        graphics.getGraphics().drawString("Velocity: " + velocityController.getVelocity(), 100, 100);
        graphics.getGraphics().drawString("Accelerating: " + velocityController.isAccelerating(), 100, 150);
    }

}
