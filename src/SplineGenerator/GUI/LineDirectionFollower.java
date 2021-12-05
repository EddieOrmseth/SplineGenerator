package SplineGenerator.GUI;

import SplineGenerator.Applied.Navigator;
import SplineGenerator.Util.DDirection;
import SplineGenerator.Util.DPoint;
import SplineGenerator.Util.DVector;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

/**
 * A class for showing the path of non pre-computable motions
 */
public class LineDirectionFollower extends BallDirectionFollower {

    /**
     * The color to draw the line
     */
    private Color color;

    /**
     * The width of the line
     */
    private float width;

    /**
     * The length that should be gone before setting another point
     */
    private double intervalLength;

    /**
     * The index to be used as the x dimension
     */
    private int xDim;

    /**
     * The index to be used as the y dimension
     */
    private int yDim;

    /**
     * An ArrayList<DPoint> for holding the past points, these are in the coordinates of the screen
     */
    private ArrayList<DPoint> pointList;

    /**
     * A boolean used to record if the follower was controlled by a user on the last iteration
     */
    private boolean controlled;

    /**
     * A simple constructor requiring the necessary components
     *
     * @param controller     The controller that will provide directions
     * @param position       The initial position of the ball
     * @param color          The color to color the line
     * @param width          The width of the line
     * @param intervalLength The distance that shall be gone before creating another point -> resolution of line
     * @param xDim The index to be used as the x dimension
     * @param yDim The index to be used as the y dimension
     */
    public LineDirectionFollower(Navigator.Controller controller, DPoint position, Color color, float width, double intervalLength, int xDim, int yDim) {
        super(controller, position);
        this.color = color;
        this.width = width;
        this.intervalLength = intervalLength;
        pointList = new ArrayList<>();
        pointList.add(position.clone());
        this.xDim = xDim;
        this.yDim = yDim;
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
                DVector movement = direction.toVector();
                movement.setMagnitude(movementLength * delta);
                position.add(movement);

                if (controlled) {
                    pointList.clear();
                    pointList.add(position.clone());
                }

                controlled = false;

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

            controlled = true;
        }

        lastTime = now;

        if (KeyBoardListener.get(KeyEvent.VK_SPACE)) {
            position.set(initialPosition);
            pointList.clear();
            pointList.add(position.clone());
        }

        paint(graphics);
    }

    /**
     * A method for painting the follower
     *
     * @param graphics The object on which to paint
     */
    @Override
    public void paint(DisplayGraphics graphics) {

        if (position.getDistance(pointList.get(pointList.size() - 1)) > intervalLength) {
            pointList.add(position.clone());
        }

        paintPointList(graphics, pointList);

        super.paint(graphics);
    }

    /**
     * A method for painting a line from a list of points
     *
     * @param graphics  The object on which to paint
     * @param pointList The list of points
     */
    public void paintPointList(DisplayGraphics graphics, ArrayList<DPoint> pointList) {
        DPoint prev;
        DPoint next;

        for (int i = 1; i < pointList.size(); i++) {
            prev = pointList.get(i - 1);
            next = pointList.get(i);
            graphics.paintLine(prev.clone(), next.clone(), width, color, xDim, yDim);
        }
    }

}
