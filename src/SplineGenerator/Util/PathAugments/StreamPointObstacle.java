package SplineGenerator.Util.PathAugments;

import SplineGenerator.GUI.Displayable;
import SplineGenerator.GUI.SplineGraphics;
import SplineGenerator.Util.DPoint;
import SplineGenerator.Util.DVector;

import java.awt.*;

/**
 * A class representing a more smoothly navigated point obstacle
 */
public class StreamPointObstacle extends PathAugment implements Displayable {

    /**
     * The position of the obstacle
     */
    private DPoint obstaclePosition;

    /**
     * A simple constructor that takes care of everything
     *
     * @param obstaclePosition The position of the obstacle
     * @param coefficient The coefficient of the effect Function
     * @param power The power of the effect Function
     */
    public StreamPointObstacle(DPoint obstaclePosition, double coefficient, double power) {
        this.obstaclePosition = obstaclePosition;
        setSkipAugment((toTarget, point, velocity) -> false);
        setGetVectorBetween(objectPoint -> new DVector(obstaclePosition, objectPoint));
        setGetEffect(PathAugmentFunctions.GetEffect.getDirectSingleTermPolynomialDistanceFunction(coefficient, power, obstaclePosition.getDimensions()));
        setSkipEffect((vectorBetween, toTarget, effect, position, velocity) -> false);
    }

    /**
     * A method for displaying the target on the screen
     *
     * @param graphics The object to display on
     */
    @Override
    public void display(SplineGraphics graphics) {
        graphics.paintPoint(obstaclePosition.clone(), 0, 1, new Color(255, 0, 0), 10);
    }
}
