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

    public static final class SkipAugment {

        /**
         * A private default constructor and no other constructor to prevent instantiation
         */
        private SkipAugment() {

        }

    }

    public static final class GetVectorBetween {

        /**
         * A private default constructor and no other constructor to prevent instantiation
         */
        private GetVectorBetween() {

        }

    }

    public static final class GetEffect {

        /**
         * A private default constructor and no other constructor to prevent instantiation
         */
        private GetEffect() {

        }

        public static FourVariableFunction<DVector, DVector, DPoint, DVector, DVector> getDirectSingleTermPolynomialDistanceFunction(double coefficient, double power, int dimensions) {
            DVector effect = new DVector(dimensions);
            return (vectorBetween, toTarget, position, velocity) -> {
                effect.copy(vectorBetween);
                effect.setMagnitude(coefficient * Math.pow(vectorBetween.getMagnitude(), power));
                return effect;
            };
        }

    }

    public static final class SkipEffect {

        /**
         * A private default constructor and no other constructor to prevent instantiation
         */
        private SkipEffect() {

        }

    }

}
