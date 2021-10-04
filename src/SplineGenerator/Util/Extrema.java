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

    @Override
    public String toString() {
        return "Greater Point: \n" + greaterPoint + "\nLesser Point: \n" + lesserPoint;
    }

}
