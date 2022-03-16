package SplineGenerator;

import SplineGenerator.Applied.OutlierEliminator;
import SplineGenerator.Util.DPoint;

public class OutlierTester {

    public static void main(String... args) {

        DPoint p1 = new DPoint(2, 1);
        DPoint p2 = new DPoint(1.1, 1.7);
        DPoint p3 = new DPoint(.9, 1);
        DPoint p4 = new DPoint(1.1, .9);

//        DPoint newPoint = OutlierEliminator.getAdjustedAverageByIterations(10, p1, p2, p3, p4);
        DPoint newPoint = OutlierEliminator.getAdjustedAverageByMinimumChange(.001, p1, p2, p3, p4);

    }

}
