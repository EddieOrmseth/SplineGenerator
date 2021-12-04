package SplineGenerator.Util.PathAugments;

import SplineGenerator.GUI.KeyBoardListener;
import SplineGenerator.Util.DPoint;
import SplineGenerator.Util.DVector;

import java.awt.event.KeyEvent;

/**
 * A utility class for holding useful Functions
 */
public final class PathAugmentFunctions {

    /**
     * A private default constructor and no other constructor to prevent instantiation
     */
    private PathAugmentFunctions() {

    }

    /**
     * A class for holding the skipArgument Functions
     */
    public static final class SkipAugment {

        /**
         * A private default constructor and no other constructor to prevent instantiation
         */
        private SkipAugment() {

        }

    }

    /**
     * A class for holding the getVectorBetween Functions
     */
    public static final class GetVectorBetween {

        /**
         * A private default constructor and no other constructor to prevent instantiation
         */
        private GetVectorBetween() {

        }

        /**
         * A method for getting the Vector between a point and an object
         *
         * @param point  The point that lies at the beginning of the vector
         * @param object The object that lies at the end of the vector
         * @param result The DVector to store the result in
         * @return
         */
        public static DVector getVectorBetweenPointAndObject(DPoint point, DPoint object, DVector result) {
            return result.set(point, object);
        }

        /**
         * A method for getting the vector between a point and a circular object
         *
         * @param circleCenter The center of the circle
         * @param radius       The radius of the circle
         * @param object       The position of the object
         * @param result       The vector between the given object position and the circular object
         * @return The result vector
         */
        public static DVector getVectorBetweenCircularObjectAndObject(DPoint circleCenter, double radius, DPoint object, DVector result) {
            result = getVectorBetweenPointAndObject(circleCenter, object, result);
            if (result.getMagnitude() > radius) {
                result.setMagnitude(result.getMagnitude() - radius);
            } else {
                result.setMagnitude(radius - result.getMagnitude());
            }
            return result;
        }

        /**
         * A method for getting the vector between a line segment and a given object
         *
         * @param p1     The first point defining the line segment
         * @param p2     The second point defining the line segment
         * @param line   The vector from the first point to the second
         * @param object The position at which to get the vector between
         * @param result The vector between the point and the line
         * @return The result vector
         */
        public static DVector getVectorBetweenLineSegmentAndObject(DPoint p1, DPoint p2, DVector line, DPoint object, DVector result) {

            result.set(p1, object);
            double thetaP1 = line.getAngleBetween(result);
            if (thetaP1 > Math.PI / 2.0) {
                return result;
            }

            result.set(p2, object);
            double thetaP2 = line.getAngleBetween(result);
            if (thetaP2 < Math.PI / 2.0) {
                return result;
            }

            if (KeyBoardListener.get(KeyEvent.VK_F)) {
                int cat = 12;
            }

            result.projectOnto(line);
            result.set(line, result);

            line.set(p1, p2);

            return result;
        }

        /**
         * A method for getting the vector between a line segment and a given object
         *
         * @param p1     The first point defining the line segment
         * @param p2     The second point defining the line segment
         * @param line   The vector from the first point to the second
         * @param object The position at which to get the vector between
         * @param result The vector between the point and the line
         * @return The result vector
         */
        public static boolean getVectorBetweenLineSegmentAndObject(DPoint p1, DPoint p2, DVector line, DPoint object, DVector result, boolean between) {

            result.set(p1, object);
            if (line.dot(result) < 0) {
//                return result;
                return false;
            }

            result.set(p2, object);
            if (line.dot(result) > 0) {
//                return result;
                return false;
            }

            result.projectOnto(line);
            result.set(line, result);

            line.set(p1, p2);

//            return result;
            return true;
        }

    }

    /**
     * A class for holding the getEffect Functions
     */
    public static final class GetEffect {

        /**
         * A private default constructor and no other constructor to prevent instantiation
         */
        private GetEffect() {

        }

        /**
         * A method for getting a function that pushes directly away from the object at a computed strength
         *
         * @param coefficient The coefficient to scale the distance (after the power has been applied) by
         * @param power       The power to raise the distance to
         * @param dimensions  The number of dimensions the obstacle is in
         * @return The resulting effect of the Function
         */
        public static DVector getEffectDirectSingleTermPolynomialDistanceFunction(DVector vectorBetween, DVector effect, double coefficient, double power, int dimensions) {
            effect.set(vectorBetween);
            effect.setMagnitude(coefficient * Math.pow(vectorBetween.getMagnitude(), power));
            return effect;
        }

        /**
         * A method for getting a the effect of a standard stream implementation from the given information
         *
         * @param vectorBetween        The vector between the objects
         * @param toTarget             The vector pointing to the target
         * @param orth                 The vector that will be used to hold the orthogonal vector
         * @param effect               The final effect of the algorithm
         * @param awayCoefficient      The coefficient used to find the directly away force
         * @param awayPower            The power used to find the directly away force
         * @param streamDotCoefficient The coefficient used to find the orthogonal force
         * @param streamDistPower      The power used to find the orthogonal force
         * @return The final effect of this stream implementation
         */
        public static DVector getEffectStandardStream(DVector vectorBetween, DVector toTarget, DVector orth, DVector effect, double awayCoefficient, double awayPower, double streamDotCoefficient, double streamDistPower) {
            effect.set(vectorBetween);
            effect.setMagnitude(PathAugmentFunctions.Util.getSingleTermPolynomialAmplification(vectorBetween.getMagnitude(), awayCoefficient, awayPower));

            toTarget.multiplyAll(-1);

            if (vectorBetween.getAngleBetween(toTarget) >= Math.PI / 2.0) {
                PathAugmentFunctions.Util.getOrthogonalVectorAccentuation(vectorBetween, toTarget, orth);
                orth.setMagnitude((Math.pow(vectorBetween.getMagnitude(), streamDistPower)) * (vectorBetween.dot(toTarget) / (vectorBetween.getMagnitude() * toTarget.getMagnitude())) * streamDotCoefficient);
                effect.add(orth);
            }

            toTarget.multiplyAll(-1);

            return effect;
        }

    }

    /**
     * A class for holding the skipEffect Functions
     */
    public static final class SkipEffect {

        /**
         * A private default constructor and no other constructor to prevent instantiation
         */
        private SkipEffect() {

        }

    }

    /**
     * A class for holding utility functions
     */
    public static final class Util {

        /**
         * A private default constructor and no other constructor to prevent instantiation
         */
        private Util() {

        }

        /**
         * A method for getting a function which amplifies a vector based on a given constant
         *
         * @param value       The value to be modified
         * @param coefficient The coefficient to scale by
         * @param power       The power to raise the given value to
         * @return The resulting DVector
         */
        public static double getSingleTermPolynomialAmplification(double value, double coefficient, double power) {
            return coefficient * Math.pow(value, power);
        }

        /**
         * A method for accentuating the orthogonal vector that already exists between two vectors
         *
         * @param v1 The vector to find the orthogonal vector of
         * @param v2 The vector to accentuate during the process
         * @return The orthogonal vector
         */
        public static DVector getOrthogonalVectorAccentuation(DVector v1, DVector v2, DVector result) {
            DVector v1Orig = v1.clone();

            DVector usedV2 = v2.clone();

            double v1Mag = v1.getMagnitude();
            double projMag = v2.projectOnto(v1.clone()).getMagnitude();

            usedV2.multiplyAll(v1Mag / projMag);

            result.set(v1);
            result.add(usedV2);

            if (Math.abs(v1Orig.dot(result)) > .1) {
                System.out.println("ERROR HERE");
            }

            return result;
        }

    }

}
