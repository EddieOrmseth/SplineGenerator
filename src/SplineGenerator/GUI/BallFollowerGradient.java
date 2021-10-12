package SplineGenerator.GUI;

import SplineGenerator.Applied.DirectionController;
import SplineGenerator.Applied.FollowerGradient;
import SplineGenerator.Util.DPoint;
import SplineGenerator.Util.DVector;

import java.awt.event.KeyEvent;

/**
 * A class that displays a small ball that follows a given FollowerGradient
 */
public class BallFollowerGradient implements Displayable {

    /**
     * The position of the ball
     */
    private DPoint position;

    /**
     * The FollowerGradient to be followed
     */
    private DirectionController follower;

    /**
     * The movement of the ball
     */
    private DVector movement;

    /**
     * How are to move on each call of display
     */
    private double movementLength = .1;

    /**
     * A constructor requiring the FollowerGradient and an initial position
     *
     * @param follower The FollowerGradient to be followed
     * @param position The initial position of the ball
     */
    public BallFollowerGradient(DirectionController follower, DPoint position) {
        this.follower = follower;
        this.position = position.clone();
    }

    /**
     * The method that moves and displays the ball
     *
     * @param graphics The object to display on
     */
    @Override
    public void display(SplineGraphics graphics) {
        DPoint tempPos = position.clone();
        if (!arrowPressed()) {
            movement = follower.getDirection(tempPos.clone()).toVector();
            movement.setMagnitude(movementLength);
            tempPos.add(movement);

            position = tempPos;
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
