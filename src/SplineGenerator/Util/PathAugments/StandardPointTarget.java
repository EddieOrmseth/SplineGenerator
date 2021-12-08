package SplineGenerator.Util.PathAugments;

import SplineGenerator.GUI.Displayable;
import SplineGenerator.GUI.DisplayGraphics;
import SplineGenerator.Util.DPoint;
import SplineGenerator.Util.DVector;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * A class representing a simple point target
 */
public class StandardPointTarget extends PathAugment implements Displayable, MouseListener {

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

    private double xScale = 0;
    private double yScale = 0;
    private DPoint mousePoint;
    private boolean newMousePoint = false;

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
        mousePoint = new DPoint(dimensions);
    }

    /**
     * A method for setting the targetPosition;
     *
     * @param targetPosition The new targetPosition;
     */
    public void setPosition(DPoint targetPosition) {
        this.targetPosition.set(targetPosition);
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
        return PathAugmentFunctions.GetVectorBetween.getVectorBetweenPointAndObject(targetPosition, point, vectorBetween);
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
        effect.setMagnitude(PathAugmentFunctions.Util.getSingleTermPolynomialAmplification(vectorBetween.getMagnitude(), coefficient, power) + PathAugmentFunctions.Util.getSingleTermPolynomialAmplification(vectorBetween.getMagnitude(), -20, -1));
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

        if (xScale == 0 && yScale == 0) {
            DPoint p1 = new DPoint(0, 0);
            DPoint p2 = new DPoint(1, 1);

            graphics.translate(p1);
            graphics.translate(p2);

            xScale = p2.get(0) - p1.get(0);
            yScale = p2.get(1) - p1.get(1);
        }

        if (newMousePoint) {
            newMousePoint = false;
            DPoint currentPoint = graphics.translate(targetPosition.clone());
            DVector between = new DVector(currentPoint, mousePoint);
            between.multiply(0, 1.0 / xScale);
            between.multiply(1, 1.0 / yScale);
            targetPosition.add(between);
        }

        graphics.paintPoint(targetPosition.clone(), 0, 1, new Color(0, 0, 255), 14);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        newMousePoint = true;
        mousePoint.set(0, e.getX());
        mousePoint.set(1, e.getY());
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
