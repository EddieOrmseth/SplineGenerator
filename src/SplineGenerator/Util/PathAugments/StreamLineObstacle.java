package SplineGenerator.Util.PathAugments;

import SplineGenerator.GUI.DisplayGraphics;
import SplineGenerator.GUI.Displayable;
import SplineGenerator.GUI.KeyBoardListener;
import SplineGenerator.Util.DPoint;
import SplineGenerator.Util.DVector;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * A class for representing a line that can be smoothly moved around
 */
public class StreamLineObstacle extends PathAugment implements Displayable {

    /**
     * The point where the line begins
     */
    private DPoint p1;

    /**
     * The point where the line ends
     */
    private DPoint p2;

    /**
     * The DVector between the two given points p1 and p2
     */
    private DVector lineSegment;

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
    public StreamLineObstacle(DPoint p1, DPoint p2, double awayCoefficient, double awayPower, double streamCoefficient, double streamPower) {
        super(p1.getDimensions());
        this.p1 = p1;
        this.p2 = p2;
        lineSegment = new DVector(p1, p2);
        this.awayCoefficient = awayCoefficient;
        this.awayPower = awayPower;
        this.streamCoefficient = streamCoefficient;
        this.streamPower = streamPower;
        orthVector = new DVector(p1.getDimensions());
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
        useDot = PathAugmentFunctions.GetVectorBetween.getVectorBetweenLineSegmentAndObject(p1, p2, lineSegment, point, vectorBetween, useDot);
//        System.out.println(point + "\n" + useDot);
        return vectorBetween;

//        vectorBetween.set(p1, point);
//        double thetaP1 = lineSegment.getAngleBetween(vectorBetween);
//        if (thetaP1 > Math.PI / 2.0) {
//            useDot = true;
//            return vectorBetween;
//        }
//
//        vectorBetween.set(p2, point);
//        double thetaP2 = lineSegment.getAngleBetween(vectorBetween);
//        if (thetaP2 < Math.PI / 2.0) {
//            useDot = true;
//            return vectorBetween;
//        }
//
//        useDot = false;
//
//        if (KeyBoardListener.get(KeyEvent.VK_F)) {
//            int cat = 12;
//        }
//
//        vectorBetween.projectOnto(lineSegment);
//        vectorBetween.set(lineSegment, vectorBetween);
//
//        lineSegment.set(p1, p2);
//
//        return vectorBetween;
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
        graphics.paintLine(p1.clone(), p2.clone(), 3, color, 0, 1);
        if (useDot) {
            graphics.getGraphics().drawString("Using Dot Product: true", 100, 100);
        } else {
            graphics.getGraphics().drawString("Using Dot Product: false", 100, 100);
        }
    }
}
