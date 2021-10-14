package SplineGenerator.GUI;

import SplineGenerator.Applied.Segmenter;
import SplineGenerator.Applied.TimeDirection;
import SplineGenerator.Util.DDirection;
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
    private double tValue;

    /**
     * The FollowerGradient to be followed
     */
    private Segmenter follower;

    /**
     * The movement of the ball
     */
    private DVector movement;

    /**
     * How are to move on each call of display
     */
    private double movementLength = .2;

    /**
     * The segment the ball is currently on
     */
    public int segment = 0;

    /**
     * An object for getting the segment corresponding to the time
     */
    private TimeDirection segmentGetter;

    /**
     * A constructor requiring the FollowerGradient and an initial position
     *
     * @param follower The FollowerGradient to be followed
     * @param position The initial position of the ball
     */
    public BallIntersectionResolver(Segmenter follower, DPoint position) {
        this.follower = follower;
        this.position = position.clone();
        tValue = follower.spline.findClosestPointOnSpline(position, .01).get(position.getDimensions());
        segmentGetter = follower.get(new DPoint(position.getDimensions() + 1));
    }

    /**
     * The method that moves and displays the ball
     *
     * @param graphics The object to display on
     */
    @Override
    public void display(SplineGraphics graphics) {
//        DPoint tempPos = position.clone();
        if (KeyBoardListener.get(KeyEvent.VK_ENTER)) {
            int k = 1;
        }
        if (!arrowPressed()) {
//            TimeDirection timeDirection = follower.get(tempPos.clone());
//            int prevSeg = segment;
//            segment = timeDirection.getMinSurroundingSegment(tValue);
//            if (KeyBoardListener.get(KeyEvent.VK_ENTER) && tValue % .5 > .2 && tValue % .5 < .3 && prevSeg != segment || KeyBoardListener.get(KeyEvent.VK_S)) {
//                int k = 0;
//                timeDirection.getMinSurroundingSegment(tValue);
//            }
//            if (segment != -1) {
//                DVector movement = timeDirection.get(segment);
//                if (movement == null) {
//                    return;
//                }
//                movement = movement.clone();
//                tValue = timeDirection.times[segment];
//
//                DVector adjustedDistance = follower.getDistanceModifier().get(movement);
//                DVector adjustedDerivative = follower.getGradientModifier().get(follower.spline.evaluateDerivative(tValue, 1));
//
//                adjustedDerivative.add(adjustedDistance);
//                adjustedDerivative.setMagnitude(movementLength);
//                tempPos.add(adjustedDerivative);
//            }
//
//            position = tempPos;

            DPoint withTime = position.clone();
            withTime.addDimensions(1);
            withTime.set(withTime.getDimensions() - 1, tValue);

            DDirection direction = follower.getDirection(withTime);
            tValue = direction.get(direction.getDimensions() - 1);
            segment = segmentGetter.tToSegment(tValue);
            direction.removeDimension(direction.getDimensions() - 1);
            direction.setMagnitude(movementLength);
            position.add(direction);

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
