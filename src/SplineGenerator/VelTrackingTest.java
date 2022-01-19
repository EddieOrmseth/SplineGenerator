package SplineGenerator;

import SplineGenerator.Applied.PositionTracker;
import SplineGenerator.Applied.UltimatePositionTracker;
import SplineGenerator.Util.DPoint;
import SplineGenerator.Util.DVector;

public class VelTrackingTest {

    public static void main(String... args) {

        UltimatePositionTracker tracker1 = new UltimatePositionTracker(new DPoint(0, 0), new DVector(0, 0));
        PositionTracker tracker2 = new PositionTracker(new DPoint(0, 0), new DVector(0, 0));
        tracker1.update(new DVector(1, 1), 1);
        tracker2.update(new DVector(1, 1), 1);
        System.out.println(tracker1.get());
        System.out.println(tracker2.get());
        tracker1.update(new DVector(-1, 0), 1);
        tracker2.update(new DVector(-1, 0), 1);
        System.out.println(tracker1.get());
        System.out.println(tracker2.get());
        tracker1.update(new DVector(-1, -1), 1);
        tracker2.update(new DVector(-1, -1), 1);
        System.out.println(tracker1.get());
        System.out.println(tracker2.get());
        tracker1.update(new DVector(1, -1), 1);
        tracker2.update(new DVector(1, -1), 1);
        System.out.println(tracker1.get());
        System.out.println(tracker2.get());


    }

}
