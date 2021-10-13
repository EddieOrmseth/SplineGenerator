package SplineGenerator.GUI;

import SplineGenerator.Applied.IntersectionResolver;
import SplineGenerator.Applied.TimeDirection;
import SplineGenerator.Util.DPoint;
import SplineGenerator.Util.DVector;

import java.awt.event.KeyEvent;

/**
 * A class that displays a small ball that follows a given FollowerGradient
 */
public class BallIntersectionResolver implements Displayable {

    /**
     * The position of the ball
     */
    private DPoint position;

    /**
     * The t-value of the ball
     */
    private double tValue = 0;

    /**
     * The FollowerGradient to be followed
     */
    private IntersectionResolver follower;

    /**
     * The movement of the ball
     */
    private DVector movement;

    /**
     * How are to move on each call of display
     */
    private double movementLength = .1;

    /**
     * The segment the ball is currently on
     */
    public int segment = 0;

    /**
     * A constructor requiring the FollowerGradient and an initial position
     *
     * @param follower The FollowerGradient to be followed
     * @param position The initial position of the ball
     */
    public BallIntersectionResolver(IntersectionResolver follower, DPoint position) {
        this.follower = follower;
        this.position = position.clone();
        tValue = follower.spline.findClosestPointOnSpline(position, .01).get(position.getDimensions());
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
            TimeDirection timeDirection = follower.get(tempPos.clone());
            int prevSeg = segment;
            segment = timeDirection.getMinSurroundingSegment(tValue);
            if (KeyBoardListener.get(KeyEvent.VK_ENTER) && tValue % .5 > .2 && tValue % .5 < .3 && prevSeg != segment || KeyBoardListener.get(KeyEvent.VK_S)) {
                int k = 0;
                timeDirection.getMinSurroundingSegment(tValue);
            }
            if (segment != -1) {
                DVector movement = timeDirection.get(segment).clone();
                if (movement == null) {
                    return;
                }
                tValue = timeDirection.times[segment];

                DVector adjustedDistance = follower.getDistanceModifier().get(movement);
                DVector adjustedDerivative = follower.getGradientModifier().get(follower.spline.evaluateDerivative(tValue, 1));

                adjustedDerivative.add(adjustedDistance);
                adjustedDerivative.setMagnitude(movementLength);
                tempPos.add(adjustedDerivative);
            }

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

        if (KeyBoardListener.get(KeyEvent.VK_SPACE)) {
            tValue = 0;
        }

        graphics.paintPoint(position.clone());
        graphics.getGraphics().drawString("" + tValue, 100, 100);
        graphics.getGraphics().drawString("" + segment, 100, 115);
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
