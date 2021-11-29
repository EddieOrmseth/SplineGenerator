package SplineGenerator.Util.PathAugments;

import SplineGenerator.GUI.Displayable;
import SplineGenerator.GUI.DisplayGraphics;
import SplineGenerator.Util.DPoint;
import SplineGenerator.Util.DVector;

import java.awt.*;

public class StreamCircleObstacle extends PathAugment implements Displayable {

    /**
     * The DVector that points orthogonally away from the Obstacle
     */
    private DVector orthVector;

    /**
     * The center of the circle obstacle
     */
    private DPoint obstacleCenter;

    /**
     * The radius of the circle
     */
    private double radius;

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
     * A simple constructor that initializes the necessary objects
     *
     * @param dimensions The number of dimensions the PathAugment exists in
     */
    public StreamCircleObstacle(int dimensions, DPoint position, double radius, double awayCoefficient, double awayPower, double streamDotCoefficient) {
        super(dimensions);
        this.obstacleCenter = position;
        this.radius = radius;
        this.awayCoefficient = awayCoefficient;
        this.awayPower = awayPower;
        this.streamDotCoefficient = streamDotCoefficient;
        orthVector = new DVector(dimensions);
    }

    @Override
    public boolean skipAugment(DVector vectorBetween, DVector toTarget, DPoint position, DVector velocity) {
        return false;
    }

    @Override
    public DVector getVectorBetween(DPoint point) {
        return PathAugmentFunctions.GetVectorBetween.getVectorBetweenCircularObjectAndObject(obstacleCenter, radius, point, vectorBetween);
    }

    @Override
    public DVector getEffect(DVector vectorBetween, DVector toTarget, DPoint position, DVector velocity) {
        effect.set(vectorBetween);
        effect.setMagnitude(PathAugmentFunctions.Util.getSingleTermPolynomialAmplification(vectorBetween.getMagnitude(), awayCoefficient, awayPower));

        DVector realToTarget = toTarget.clone();
        realToTarget.multiplyAll(-1);

        if (vectorBetween.getAngleBetween(realToTarget) >= Math.PI / 2) {
            PathAugmentFunctions.Util.getOrthogonalVectorAccentuation(vectorBetween, realToTarget, orthVector);
            orthVector.setMagnitude((Math.pow(vectorBetween.getMagnitude(), -1.5)) * (vectorBetween.dot(realToTarget) / (vectorBetween.getMagnitude() * realToTarget.getMagnitude())) * streamDotCoefficient);
            effect.add(orthVector);
        }

        return effect;
    }

    @Override
    public boolean skipEffect(DVector vectorBetween, DVector toTarget, DVector effect, DPoint position, DVector velocity) {
        return false;
    }

    @Override
    public void display(DisplayGraphics graphics) {
//        graphics.paintPoint(position.clone(), 0, 1, new Color(0, 255, 0), 3);
        graphics.paintCircle(obstacleCenter.clone(), radius, 0, 1, new Color(0, 255, 0));
    }
}
