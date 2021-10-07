package SplineGenerator.GUI;

import SplineGenerator.Applied.FollowerGradient;
import SplineGenerator.Util.DPoint;
import SplineGenerator.Util.DVector;

import java.awt.event.KeyEvent;

/**
 * A class that displays a small ball that follows a given FollowerGradient
 */
public class BallFollower implements Displayable {

    private DPoint position;

    private FollowerGradient followerGradient;

    private DVector movement;

    private double movementLength = .1;

    public BallFollower(FollowerGradient followerGradient, DPoint position) {
        this.followerGradient = followerGradient;
        this.position = position.clone();
    }

    @Override
    public void display(SplineGraphics graphics) {
        DPoint tempPos = position.clone();
        if (!arrowPressed()) {
            movement = followerGradient.evaluateAt(tempPos).toVector();
            movement.setMagnitude(movementLength);
            tempPos.add(movement);

            System.out.println("Here: " + tempPos);

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

    public boolean arrowPressed() {
        return KeyBoardListener.get(KeyEvent.VK_LEFT) || KeyBoardListener.get(KeyEvent.VK_RIGHT) || KeyBoardListener.get(KeyEvent.VK_UP) || KeyBoardListener.get(KeyEvent.VK_DOWN);
    }

}
