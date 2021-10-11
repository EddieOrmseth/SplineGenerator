package SplineGenerator.Util;

/**
 * A utility class holding math functions that are be necessary in creating splines
 */
public final class SplineMath {

    /**
     * A method that returns the coefficient of the term x^power times after differentiating n times
     *
     * @param power     The power of the term
     * @param diffTimes The number of times to differentiate
     * @return The coefficient of the term x^power times after differentiating diffTimes times
     */
    public static double getDerivativeCoefficient(int power, int diffTimes) {
        int coefficient = 1;

        for (int i = 0; i < diffTimes; i++) {
            coefficient *= power;
            power--;
        }

        return coefficient;
    }

}
