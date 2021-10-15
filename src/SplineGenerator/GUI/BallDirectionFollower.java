package SplineGenerator.GUI;

import SplineGenerator.Applied.Navigator;
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
    private Navigator.Controller controller;

    /**
     * The position of the ball
     */
    private DPoint position;

    /**
     * The distance to move the ball each time display is called
     */
    private double movementLength = .15;

    /**
     * A simple constructor requiring the necessary components
     *
     * @param controller The controller that will provide directions
     * @param position The initial position of the ball
     */
    public BallDirectionFollower(Navigator.Controller controller, DPoint position) {
        this.controller = controller;
        this.position = position;
    }

    /**
     * The method to be called constantly as to move the ball
     *
     * @param graphics The object to display on
     */
    @Override
    public void display(SplineGraphics graphics) {

        if (!arrowPressed()) {

            controller.update(position.clone());
            DDirection direction = controller.getDirection();
            DVector movement = direction.toVector();
            movement.setMagnitude(movementLength);
            position.add(movement);

        } else {
            if (KeyBoardListener.get(KeyEvent.VK_LEFT)) {
                position.add(graphics.xDim, -movementLength);
            }
            if (KeyBoardListener.get(KeyEvent.VK_RIGHT)) {
                position.add(graphics.xDim, movementLength);
            }
            if (KeyBoardListener.get(KeyEvent.VK_UP)) {
                position.add(graphics.yDim, movementLength);
            }
            if (KeyBoardListener.get(KeyEvent.VK_DOWN)) {
                position.add(graphics.yDim, -movementLength);
            }
        }

        graphics.paintPoint(position.clone());
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
