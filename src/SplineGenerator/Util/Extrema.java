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
     *
     * @param dimensions
     */
    public Extrema(int dimensions) {
        lesserPoint = new DPoint(dimensions);
        greaterPoint = new DPoint(dimensions);
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
