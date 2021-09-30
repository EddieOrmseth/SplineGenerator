package SplineGenerator.Util;

public class BoundingBox {

    public double x1 = Double.MAX_VALUE;
    public double y1 = Double.MAX_VALUE;
    public double x2 = Double.MIN_VALUE;
    public double y2 = Double.MIN_VALUE;

    public void applyScalar(double scalar) {
        x1 *= scalar;
        y1 *= scalar;
        x2 *= scalar;
        y2 *= scalar;
    }

}
