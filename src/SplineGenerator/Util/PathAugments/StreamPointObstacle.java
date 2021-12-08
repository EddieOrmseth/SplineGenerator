package SplineGenerator.Util.PathAugments;

import SplineGenerator.GUI.DisplayGraphics;
import SplineGenerator.GUI.Displayable;
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
     * The DVector orthogonal to the vectorBetween
     */
    private DVector orthVector;

    /**
     * The coefficient to scale the distance to the obstacle by
     */
    private double awayCoefficient;

    /**
     * The power to raise the distance to the obstacle to
     */
    private double awayPower;

    /**
     * The number to multiple the dot product of the vectorBetween and velocity vectors when setting the magnitude of orth
     */
    private double streamCoefficient;

    /**
     * The number to raise the distance of the vectorBetween to when finding the force of the orthogonal vector
     */
    private double streamPower;

    /**
     * A simple constructor that takes care of everything
     *
     * @param dimensions        The number of dimensions the PathAugment exists in
     * @param obstaclePosition  The position of the obstacle
     * @param awayCoefficient   The coefficient of the effect Function
     * @param awayPower         The power of the effect Function
     * @param streamCoefficient The coefficient to use when finding the magnitude of the orthogonal vector
     * @param streamPower       The coefficient to use when finding the magnitude of the orthogonal vector
     */
    public StreamPointObstacle(int dimensions, DPoint obstaclePosition, double awayCoefficient, double awayPower, double streamCoefficient, double streamPower) {
        super(dimensions);
        orthVector = new DVector(dimensions);
        this.obstaclePosition = obstaclePosition;
        this.awayCoefficient = awayCoefficient;
        this.awayPower = awayPower;
        this.streamCoefficient = streamCoefficient;
        this.streamPower = streamPower;
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
        return vectorBetween.getMagnitude() > toTarget.getMagnitude();
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
//        effect.set(vectorBetween);
//        effect.setMagnitude(PathAugmentFunctions.Util.getSingleTermPolynomialAmplification(vectorBetween.getMagnitude(), awayCoefficient, awayPower));
//
//        DVector realToTarget = toTarget.clone();
//        realToTarget.multiplyAll(-1);
//
//        if (vectorBetween.getAngleBetween(realToTarget) >= Math.PI / 2.0) {
//            PathAugmentFunctions.Util.getOrthogonalVectorAccentuation(vectorBetween, realToTarget, orth);
//            orth.setMagnitude((Math.pow(vectorBetween.getMagnitude(), -1.5)) * (vectorBetween.dot(realToTarget) / (vectorBetween.getMagnitude() * realToTarget.getMagnitude())) * streamDotCoefficient);
//            effect.add(orth);
//        }
//
//        return effect;
        return PathAugmentFunctions.GetEffect.getEffectStandardStream(vectorBetween, toTarget, orthVector, effect, awayCoefficient, awayPower, streamCoefficient, streamPower);
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
