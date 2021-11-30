package SplineGenerator.Util.PathAugments;

import SplineGenerator.GUI.DisplayGraphics;
import SplineGenerator.GUI.Displayable;
import SplineGenerator.Util.DPoint;
import SplineGenerator.Util.DVector;

import java.awt.*;

public class StreamPolygonObstacle extends PathAugment implements Displayable {

    private DPoint[] points;
    private DVector[] lineSegments;
    private DVector[] vectorsBetween;

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
     * The color of the line
     */
    private Color color = new Color(0, 255, 0);

    private boolean useDot;

    /**
     * A simple constructor that initializes the necessary objects
     */
    public StreamPolygonObstacle(int dimensions, double awayCoefficient, double awayPower, double streamCoefficient, double streamPower, DPoint... points) {
        super(dimensions);
        this.awayCoefficient = awayCoefficient;
        this.awayPower = awayPower;
        this.streamCoefficient = streamCoefficient;
        this.streamPower = streamPower;
        orthVector = new DVector(dimensions);
        this.points = points;
        lineSegments = new DVector[points.length - 1];
        for (int i = 1; i < points.length; i++) {
            lineSegments[i - 1] = new DVector(points[i - 1], points[i]);
        }
        vectorsBetween = new DVector[points.length - 1];
        for (int i = 0; i < vectorsBetween.length; i++) {
            vectorsBetween[i] = new DVector(dimensions);
        }
    }

    @Override
    public boolean skipAugment(DVector vectorBetween, DVector toTarget, DPoint position, DVector velocity) {
        return vectorBetween.getMagnitude() > toTarget.getMagnitude();
    }

    @Override
    public DVector getVectorBetween(DPoint point) {
//        return PathAugmentFunctions.GetVectorBetween.getVectorBetweenLineSegmentAndObject(p1, p2, lineSegment, point, vectorBetween);
//        return vectorBetween;
        for (int i = 1; i < points.length; i++) {
            PathAugmentFunctions.GetVectorBetween.getVectorBetweenLineSegmentAndObject(points[i - 1], points[i], lineSegments[i - 1], point, vectorsBetween[i - 1]);
        }

        int minIndex = 0;
        double minMagnitude = vectorsBetween[minIndex].getMagnitude();
        for (int i = 0; i < vectorsBetween.length; i++) {
            if (vectorsBetween[i].getMagnitude() < minMagnitude) {
                minIndex = i;
                minMagnitude = vectorsBetween[i].getMagnitude();
            }
        }

        return vectorsBetween[minIndex];
    }

    @Override
    public DVector getEffect(DVector vectorBetween, DVector toTarget, DPoint position, DVector velocity) {
//        return PathAugmentFunctions.GetEffect.getEffectStandardStream(vectorBetween, toTarget, orthVector, effect, awayCoefficient, awayPower, streamCoefficient, streamPower);

        effect.set(vectorBetween);
        effect.setMagnitude(PathAugmentFunctions.Util.getSingleTermPolynomialAmplification(vectorBetween.getMagnitude(), awayCoefficient, awayPower));

        toTarget.multiplyAll(-1);

        if (vectorBetween.getAngleBetween(toTarget) >= Math.PI / 2.0) {
            PathAugmentFunctions.Util.getOrthogonalVectorAccentuation(vectorBetween, toTarget, orthVector);
            if (useDot) {
                orthVector.setMagnitude((Math.pow(vectorBetween.getMagnitude(), streamPower)) * (vectorBetween.dot(toTarget) / (vectorBetween.getMagnitude() * toTarget.getMagnitude())) * streamCoefficient);
            } else {
                orthVector.setMagnitude((Math.pow(vectorBetween.getMagnitude(), streamPower)) * -streamCoefficient);
            }
            effect.add(orthVector);
        }

        toTarget.multiplyAll(-1);

        return effect;
    }

    @Override
    public boolean skipEffect(DVector vectorBetween, DVector toTarget, DVector effect, DPoint position, DVector velocity) {
        return false;
    }

    @Override
    public void display(DisplayGraphics graphics) {
//        graphics.paintLine(p1.clone(), p2.clone(), 3, color, 0, 1);
        for (int i = 1; i < points.length; i++) {
            graphics.paintLine(points[i - 1].clone(), points[i].clone(), 3, color, 0, 1);
        }
    }
}
