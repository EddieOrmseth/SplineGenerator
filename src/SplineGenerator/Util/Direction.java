package SplineGenerator.Util;

/**
 * A class for holding a direction / unit vector
 */
public class Direction {

    /**
     * The x-value of the vector
     */
    public double x;

    /**
     * The y-value of the vector
     */
    public double y;

    /**
     * A constructor that takes in a vector and converts to a unit-vector
     *
     * @param x The x-value of the vector
     * @param y The y-value of the vector
     */
    public Direction(double x, double y) {
        double magnitude = Math.sqrt((x * x) + (y * y));
        this.x = x / magnitude;
        this.y = y / magnitude;
    }

    /**
     * A constructor that uses an amount of radians to create the vector
     *
     * @param radians The angle of the direction in radians
     */
    public Direction(double radians) {
        x = Math.cos(radians);
        y = Math.sin(radians);
    }

    /**
     * A method for getting the slope of the vector
     *
     * @return The slope of the vector
     */
    public double getSlope() {
        return y / x;
    }

    /**
     * A method for getting the degrees between the vector and the x-axis
     *
     * @return The degrees between the vector and the x-axis
     */
    public double getDegrees() {
        return Math.toDegrees(getRadians());
    }

    /**
     * A method for getting the number of radians between the vector and the x-axis
     *
     * @return The number of radians between the vector and the x-axis
     */
    public double getRadians() {
        return Math.tan(y / x);
    }

}
