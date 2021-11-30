package SplineGenerator.GUI;

import SplineGenerator.Applied.Navigator;
import SplineGenerator.Util.DPoint;
import SplineGenerator.Util.DVector;
import SplineGenerator.Util.Function;

import java.awt.*;
import java.util.ArrayList;

/**
 * A class for creating a line of LineDirectionFollowers along a given line
 */
public class LineOfLineDirectionFollowers implements Displayable {

    /**
     * The list of followers
     */
    private ArrayList<LineDirectionFollower> followers;

    /**
     * A simple constructor requiring the necessary components
     *
     * @param controllerSupplier The object for getting the controller that will provide directions
     * @param p1                 The starting point of the line
     * @param p2                 The ending point of the line
     * @param lines              The number of lines to be used
     * @param color              The color to color the line
     * @param width              The width of the line
     * @param intervalLength     The distance that shall be gone before creating another point -> resolution of line
     * @param xDim               The index to be used as the x dimension
     * @param yDim               The index to be used as the y dimension
     */
    public LineOfLineDirectionFollowers(Navigator controllerSupplier, DPoint p1, DPoint p2, int lines, Color color, float width, double intervalLength, int xDim, int yDim) {
        followers = new ArrayList<>();
        DVector vectorBetween = new DVector(p1, p2);
        double lineLength = vectorBetween.getMagnitude();
        double separationLength = lineLength / (lines - 1);

        DPoint initPos;

        followers.add(new LineDirectionFollower(controllerSupplier.getController(), p1.clone(), color, width, intervalLength, xDim, yDim));

        for (int i = 1; i < lines; i++) {
            vectorBetween.setMagnitude(separationLength * i);
            initPos = p1.clone();
            initPos.add(vectorBetween);

            followers.add(new LineDirectionFollower(controllerSupplier.getController(), initPos, color, width, intervalLength, xDim, yDim));
        }

    }

    /**
     * A simple constructor requiring the necessary components
     *
     * @param controllerSupplier The object for getting the controller that will provide directions
     * @param p1                 The starting point of the line
     * @param p2                 The ending point of the line
     * @param lines              The number of lines to be used
     * @param colorFunction        The Function that can be used to get the color
     * @param width              The width of the line
     * @param intervalLength     The distance that shall be gone before creating another point -> resolution of line
     * @param xDim               The index to be used as the x dimension
     * @param yDim               The index to be used as the y dimension
     */
    public LineOfLineDirectionFollowers(Navigator controllerSupplier, DPoint p1, DPoint p2, int lines, Function<Integer, Color> colorFunction, float width, double intervalLength, int xDim, int yDim) {
        followers = new ArrayList<>();
        DVector vectorBetween = new DVector(p1, p2);
        double lineLength = vectorBetween.getMagnitude();
        double separationLength = lineLength / (lines - 1);

        DPoint initPos;

        followers.add(new LineDirectionFollower(controllerSupplier.getController(), p1.clone(), colorFunction.get(0), width, intervalLength, xDim, yDim));

        for (int i = 1; i < lines; i++) {
            vectorBetween.setMagnitude(separationLength * i);
            initPos = p1.clone();
            initPos.add(vectorBetween);

            followers.add(new LineDirectionFollower(controllerSupplier.getController(), initPos, colorFunction.get(i), width, intervalLength, xDim, yDim));
        }

    }

    /**
     * A method for starting the followers
     */
    public void start() {
        for (int i = 0; i < followers.size(); i++) {
            followers.get(i).start();
        }
    }

    /**
     * A method for painting the follower
     *
     * @param graphics The object on which to paint
     */
    @Override
    public void display(DisplayGraphics graphics) {
        for (int i = 0; i < followers.size(); i++) {
            followers.get(i).display(graphics);
        }
    }
}
