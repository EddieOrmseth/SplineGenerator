package SplineGenerator;

public class Direction {

    public double x;
    public double y;

    public Direction(double x, double y) {
        double magnitude = Math.sqrt((x * x) + (y * y));
        this.x = x / magnitude;
        this.y = y / magnitude;
    }

    public Direction(double radians) {
        x = Math.cos(radians);
        y = Math.sin(radians);
    }

    public double getSlope() {
        return y / x;
    }

    public double getDegrees() {
        return Math.toDegrees(getRadians());
    }

    public double getRadians() {
        return Math.tan(y / x);
    }

}
