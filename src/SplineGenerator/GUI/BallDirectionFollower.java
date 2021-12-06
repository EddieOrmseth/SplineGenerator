package SplineGenerator.GUI;

import SplineGenerator.Applied.Navigator;
import SplineGenerator.Applied.VelocityController;
import SplineGenerator.Util.DDirection;
import SplineGenerator.Util.DPoint;
import SplineGenerator.Util.DVector;

import java.awt.event.KeyEvent;

/**
 * A class that follows the path given to it by a Controller at a constant speed
 */
public class BallDirectionFollower implements Displayable {

    /**
     * The controller that provides the direction
     */
    protected Navigator.Controller controller;

    /**
     * The position of the ball
     */
    protected DPoint position;

    /**
     * The initial point of the follower
     */
    protected DPoint initialPosition;

    /**
     * The distance to move the ball per millisecond
     */
    protected double movementLength = .02;

    /**
     * The timestamp, in milliseconds, of the last update
     */
    protected long lastTime = -1;

    public VelocityController velocityController;
    private DVector lastMovement;

    /**
     * A simple constructor requiring the necessary components
     *
     * @param controller The controller that will provide directions
     * @param position   The initial position of the ball
     */
    public BallDirectionFollower(Navigator.Controller controller, DPoint position) {
        this.controller = controller;
        this.position = position;
        initialPosition = new DPoint(position.getDimensions());
        initialPosition.set(position);
        lastMovement = new DVector(position.getDimensions());
    }

    /**
     * A method for notifying the ball follower that it has started
     */
    public void start() {
        lastTime = System.currentTimeMillis();
    }

    /**
     * The method to be called constantly as to move the ball
     *
     * @param graphics The object to display on
     */
    @Override
    public void display(DisplayGraphics graphics) {

        long now = System.currentTimeMillis();
        long delta = now - lastTime;

        if (!arrowPressed()) {
            if (!KeyBoardListener.get(KeyEvent.VK_SHIFT) && delta != 0) {

                controller.update(position.clone());
                DVector direction = controller.getDirection();
                lastMovement.set(direction);

                System.out.println("Magnitude: " + direction.getMagnitude());

                velocityController.update(direction);
                DVector movement = direction.clone();
//                movement.setMagnitude(movementLength * delta);
                movement.setMagnitude(velocityController.getVelocity() * .5);
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
            position.set(initialPosition);
        }

        paint(graphics);
    }

    /**
     * A method for painting the follower
     *
     * @param graphics The object on which to paint
     */
    public void paint(DisplayGraphics graphics) {
        graphics.paintPoint(position.clone());
        graphics.paintVector(position.clone(), lastMovement);
        graphics.getGraphics().drawString("Velocity: " + velocityController.getVelocity(), 100, 100);
        graphics.getGraphics().drawString("Accelerating: " + velocityController.isAccelerating(), 100, 150);
        graphics.getGraphics().drawString("Angle: " + velocityController.getAngle(), 100, 200);
    }

    /**
     * A method for getting if an arrow key is currently pressed
     *
     * @return Whether or not an arrow key is pressed
     */
    public boolean arrowPressed() {
        return KeyBoardListener.get(KeyEvent.VK_LEFT) || KeyBoardListener.get(KeyEvent.VK_RIGHT) || KeyBoardListener.get(KeyEvent.VK_UP) || KeyBoardListener.get(KeyEvent.VK_DOWN);
    }

}
