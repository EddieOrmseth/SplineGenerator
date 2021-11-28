package SplineGenerator.Util.PathAugments;

import SplineGenerator.Util.DPoint;
import SplineGenerator.Util.DVector;
import SplineGenerator.Util.MultivariableFunctions.FourVariableFunction;

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

    }

    /**
     * A class for holding teh getEffect Functions
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
        public static FourVariableFunction<DVector, DVector, DPoint, DVector, DVector> getDirectSingleTermPolynomialDistanceFunction(double coefficient, double power, int dimensions) {
            DVector effect = new DVector(dimensions);
            return (vectorBetween, toTarget, position, velocity) -> {
                effect.set(vectorBetween);
                effect.setMagnitude(coefficient * Math.pow(vectorBetween.getMagnitude(), power));
                return effect;
            };
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
        public static double getSingleTermPolynomialAmplifier(double value, double coefficient, double power) {
            return coefficient * Math.pow(value, power);
        }

        /**
         * A method for accentuating the orthogonal vector that already exists between two vectors
         *
         * @param v1 The vector to find the orthogonal vector of
         * @param v2 The vector to accentuate during the process
         * @return The orthogonal vector
         */
        public static DVector getOrthogonalVectorAccentuation(DVector v1, DVector v2) {
//            DVector v1Orig = v1.clone();
            double v1Mag = v1.getMagnitude();
            double projMag = v2.projectOnto(v1.clone()).getMagnitude();

            v2.multiplyAll(v1Mag / projMag);
            v1.add(v2);

//            if (Math.abs(v1Orig.dot(v1)) > .1) {
//                System.out.println("ERROR HERE");
//            }

            return v1;
        }

    }

}
