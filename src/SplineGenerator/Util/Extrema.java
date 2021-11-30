package SplineGenerator.Util;

/**
 * A class to represent the Extrema of each dimension
 */
public class Extrema {

    /**
     * A DPoint for holding the smaller values
     */
    public DPoint lesserPoint;

    /**
     * A DPoint for holding the greater values
     */
    public DPoint greaterPoint;

    /**
     * A simple constructor requiring only the dimensions of the object
     *
     * @param dimensions The number of dimensions required
     */
    public Extrema(int dimensions) {
        lesserPoint = new DPoint(dimensions);
        greaterPoint = new DPoint(dimensions);
    }

    public Extrema(DPoint lesserPoint, DPoint greaterPoint) {
        this.lesserPoint = lesserPoint;
        this.greaterPoint = greaterPoint;
    }

    /**
     * A method for multiplying each point by a given scalar
     *
     * @param scalar The scalar to be multiplied by
     */
    public void multiplyAll(double scalar) {
        lesserPoint.multiplyAll(scalar);
        greaterPoint.multiplyAll(scalar);
    }

    /**
     * A method for checking to see if the given point is between the Extrema
     *
     * @param point The point to check
     * @return Whether or not the point is between the Extrema
     */
    public boolean contains(DPoint point) {
        for (int n = 0; n < lesserPoint.getDimensions() && n < point.getDimensions(); n++) {
            if (point.get(n) < lesserPoint.get(n) || point.get(n) > greaterPoint.get(n)) {
                return false;
            }
        }

        return true;
    }

    /**
     * A method for getting the dimensions of the Extrema object
     *
     * @return THe number of dimensions of the Extrema object
     */
    public int getDimensions() {
        return lesserPoint.getDimensions();
    }

    /**
     * A method for cloning the Extrema object
     *
     * @return The new identical Extrema object
     */
    @Override
    public Extrema clone() {
        return new Extrema(lesserPoint.clone(), greaterPoint.clone());
    }

    /**
     * A method for getting a vector pointing from the lowerPoint to the greaterPoint
     *
     * @return The DVector that is created
     */
    public DVector getVector() {
        return new DVector(lesserPoint, greaterPoint);
    }

    /**
     * A method for getting the String representation of the Extrema
     *
     * @return The String representation of the Extrema
     */
    @Override
    public String toString() {
        return "Greater Point: \n" + greaterPoint + "\nLesser Point: \n" + lesserPoint;
    }

}
