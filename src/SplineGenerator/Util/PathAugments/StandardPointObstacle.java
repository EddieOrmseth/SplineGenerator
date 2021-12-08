package SplineGenerator.Util.PathAugments;

import SplineGenerator.GUI.Displayable;
import SplineGenerator.GUI.DisplayGraphics;
import SplineGenerator.Util.DPoint;
import SplineGenerator.Util.DVector;

import java.awt.*;

/**
 * A class representing a very basic point obstacle
 */
public class StandardPointObstacle extends PathAugment implements Displayable {

    /**
     * The position of the obstacle
     */
    private DPoint obstaclePosition;

    /**
     * The number to scale the distance by
     */
    private double coefficient;

    /**
     * The number to raise the distance to
     */
    private double power;

    /**
     * A simple constructor that requires the position of the target
     *
     * @param dimensions       The number of dimensions the PathAugment exists in
     * @param obstaclePosition The position of the target
     * @param coefficient      The strength of the pull towards the target
     * @param power            The power to raise the distance to
     */
    public StandardPointObstacle(int dimensions, DPoint obstaclePosition, double coefficient, double power) {
        super(dimensions);
        this.obstaclePosition = obstaclePosition;
        this.coefficient = coefficient;
        this.power = power;
    }

    /**
     * A method for determining weather or not to use this PathAugment
     *
     * @param toTarget The vector pointing from the object to the target
     * @param position The position of the object
     * @param velocity The velocity of the object
     * @return true if this PathAugment should be used, false otherwise
     */
    @Override
    public boolean skipAugment(DVector vectorBetween, DVector toTarget, DPoint position, DVector velocity) {
        return false;
    }

    /**
     * A method for getting the vector between the given DPoint and PathAugment. The vector shall point form the PathAugment to the DPoint
     *
     * @param point The given point
     * @return The DVector between the PathAugment and the given point
     */
    @Override
    public DVector getVectorBetween(DPoint point) {
        return PathAugmentFunctions.GetVectorBetween.getVectorBetweenPointAndObject(obstaclePosition, point, vectorBetween);
    }

    /**
     * A method for getting the effect of the PathAugment
     *
     * @param vectorBetween The vector from the PathAugment to the object
     * @param toTarget      The vector form the object to the target
     * @param position      The position of the object
     * @param velocity      The velocity of the object
     * @return The effect of the PathAugment on the object
     */
    @Override
    public DVector getEffect(DVector vectorBetween, DVector toTarget, DPoint position, DVector velocity) {
        effect.set(vectorBetween);
        effect.setMagnitude(PathAugmentFunctions.Util.getSingleTermPolynomialAmplification(vectorBetween.getMagnitude(), coefficient, power));
        return effect;
    }

    /**
     * A method for determining weather or not to use the effect of this PathAugment
     *
     * @param vectorBetween The vector from the PathAugment to object
     * @param toTarget      The vector from the object to the target
     * @param effect        The effect of the PathAugment
     * @param position      The position of the object
     * @param velocity      The velocity of the object
     * @return true if the effect should be used, false otherwise
     */
    @Override
    public boolean skipEffect(DVector vectorBetween, DVector toTarget, DVector effect, DPoint position, DVector velocity) {
        return false;
    }

    /**
     * A method for displaying the target on the screen
     *
     * @param graphics The object to display on
     */
    @Override
    public void display(DisplayGraphics graphics) {
        graphics.paintPoint(obstaclePosition.clone(), 0, 1, new Color(255, 0, 0), 10);
    }
}
