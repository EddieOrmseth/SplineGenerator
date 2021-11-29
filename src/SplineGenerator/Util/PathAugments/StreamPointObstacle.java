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
     * The DVector orthogonal to the vectorBetween
     */
    private DVector orth;

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
    private double streamDotCoefficient;

    /**
     * A simple constructor that takes care of everything
     *
     * @param dimensions The number of dimensions the PathAugment exists in
     * @param obstaclePosition The position of the obstacle
     * @param awayCoefficient      The coefficient of the effect Function
     * @param awayPower            The power of the effect Function
     */
    public StreamPointObstacle(int dimensions, DPoint obstaclePosition, double awayCoefficient, double awayPower, double streamDotCoefficient) {
        super(dimensions);
        orth = new DVector(dimensions);
        this.obstaclePosition = obstaclePosition;
        this.awayCoefficient = awayCoefficient;
        this.awayPower = awayPower;
        this.streamDotCoefficient = streamDotCoefficient;
    }

    @Override
    public boolean skipAugment(DVector vectorBetween, DVector toTarget, DPoint position, DVector velocity) {
        return false;
    }

    @Override
    public DVector getVectorBetween(DPoint point) {
        return PathAugmentFunctions.GetVectorBetween.getVectorBetweenPointAndObject(obstaclePosition, point, vectorBetween);
    }

    @Override
    public DVector getEffect(DVector vectorBetween, DVector toTarget, DPoint position, DVector velocity) {
        effect.set(vectorBetween);
        effect.setMagnitude(PathAugmentFunctions.Util.getSingleTermPolynomialAmplification(vectorBetween.getMagnitude(), awayCoefficient, awayPower));

        DVector realToTarget = toTarget.clone();
        realToTarget.multiplyAll(-1);

        if (vectorBetween.getAngleBetween(realToTarget) >= Math.PI / 2.0) {
            PathAugmentFunctions.Util.getOrthogonalVectorAccentuation(vectorBetween, realToTarget, orth);
            orth.setMagnitude((Math.pow(vectorBetween.getMagnitude(), -1)) * (vectorBetween.dot(realToTarget) / (vectorBetween.getMagnitude() * realToTarget.getMagnitude())) * streamDotCoefficient);
//            orth.setMagnitude((Math.pow(vectorBetween.getMagnitude(), -1)) * -streamDotCoefficient);
            effect.add(orth);
        }

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
        graphics.paintPoint(obstaclePosition.clone(), 0, 1, new Color(255, 0, 0), 10);
    }
}
