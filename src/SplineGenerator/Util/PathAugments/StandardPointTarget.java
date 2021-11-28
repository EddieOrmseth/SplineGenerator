package SplineGenerator.Util.PathAugments;

import SplineGenerator.GUI.Displayable;
import SplineGenerator.GUI.SplineGraphics;
import SplineGenerator.Util.DPoint;
import SplineGenerator.Util.DVector;

import java.awt.*;

/**
 * A class representing a simple point target
 */
public class StandardPointTarget extends PathAugment implements Displayable {

    /**
     * The point for the target to draw to
     */
    private DPoint targetPosition;

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
     * @param dimensions     The number of dimensions the PathAugment exists in
     * @param targetPosition The position of the target
     * @param coefficient    The strength of the pull towards the target
     * @param power          The power to raise the distance to
     */
    public StandardPointTarget(int dimensions, DPoint targetPosition, double coefficient, double power) {
        super(dimensions);
        this.targetPosition = targetPosition;
        this.coefficient = -coefficient;
        this.power = power;
    }

    /**
     * A method for setting the targetPosition;
     *
     * @param targetPosition The new targetPosition;
     */
    public void setPosition(DPoint targetPosition) {
        this.targetPosition.set(targetPosition);
    }

    @Override
    public boolean skipAugment(DVector toTarget, DPoint position, DVector velocity) {
        return false;
    }

    @Override
    public DVector getVectorBetween(DPoint point) {
        return vectorBetween.set(targetPosition, point);
    }

    @Override
    public DVector getEffect(DVector vectorBetween, DVector toTarget, DPoint position, DVector velocity) {
        effect.set(vectorBetween);
        effect.setMagnitude(PathAugmentFunctions.Util.getSingleTermPolynomialAmplifier(vectorBetween.getMagnitude(), coefficient, power));
        return effect;
    }

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
    public void display(SplineGraphics graphics) {
        graphics.paintPoint(targetPosition.clone(), 0, 1, new Color(0, 0, 255), 14);
    }

}
