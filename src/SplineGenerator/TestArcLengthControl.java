package SplineGenerator;

import SplineGenerator.Applied.ArcLengthConverter;
import SplineGenerator.Applied.Navigator;
import SplineGenerator.Applied.SplineVelocityController;
import SplineGenerator.GUI.KeyBoardListener;
import SplineGenerator.GUI.SplineDisplay;
import SplineGenerator.Splines.PolynomicSpline;
import SplineGenerator.Splines.Spline;
import SplineGenerator.Util.*;
import SplineGenerator.Splines.Spline.*;

import java.util.concurrent.atomic.AtomicReference;

public class TestArcLengthControl {

    public static void main(String... rags) {
        KeyBoardListener.initialize();

        PolynomicSpline spline = new PolynomicSpline(2);
        spline.addControlPoint(new DControlPoint(new DVector(0.0, 0.0), new DVector(5.0, 0.0), new DDirection(0.0, 0.0)));
        spline.addControlPoint(new DControlPoint(new DVector(4.0, 2.0)));
        spline.addControlPoint(new DControlPoint(new DVector(8.0, 0.0), new DDirection(1.0, 0.0), new DDirection(0.0, 0.0)));

        spline.setPolynomicOrder(5);
        spline.closed = false;

        InterpolationInfo c1 = new InterpolationInfo();
        c1.interpolationType = InterpolationType.Linked;
        c1.endBehavior = EndBehavior.Hermite;
        spline.interpolationTypes.add(c1);
        InterpolationInfo c2 = new InterpolationInfo();
        c2.interpolationType = InterpolationType.Linked;
        c2.endBehavior = EndBehavior.Hermite;
        spline.interpolationTypes.add(c2);
        InterpolationInfo c3 = new InterpolationInfo();
        c3.interpolationType = InterpolationType.Linked;
        c3.endBehavior = EndBehavior.None;
        spline.interpolationTypes.add(c3);
        InterpolationInfo c4 = new InterpolationInfo();
        c4.interpolationType = InterpolationType.Linked;
        c4.endBehavior = EndBehavior.None;
        spline.interpolationTypes.add(c4);

        spline.generate();
        spline.takeNextDerivative();

        SplineVelocityController splineVelocityController = new SplineVelocityController(spline, 1.7D, 1.0D, 0.0D, 0.2D, 0.2D);
        ArcLengthConverter arcLengthConverter = new ArcLengthConverter(spline, 0.05D, splineVelocityController);
        arcLengthConverter.computeMovement();
        arcLengthConverter.addEndVelocity(0.0D, 0.1D);
        ArcLengthConverter.Controller controller = arcLengthConverter.getController();

        AtomicReference<Double> arcLength = new AtomicReference(0.0);

        SplineDisplay splineDisplay = new SplineDisplay(spline, 0, 1, 1600, 700);

        DPoint position = new DPoint(0.0, 0.0);
        AtomicReference<DVector> motion = new AtomicReference(new DVector(0.0, 0.0));
        AtomicReference<Long> time = new AtomicReference(0L);

        splineDisplay.displayables.add((displayGraphics) -> {
            double delta = (double)(System.currentTimeMillis() - time.get()) / 1000.0;
            time.set(System.currentTimeMillis());

            motion.get().multiplyAll(delta);
            position.add(motion.get());

            arcLength.set(arcLength.get() + motion.get().getMagnitude());

            controller.update(new DPoint(arcLength.get()));
            motion.set(controller.getDirection());

            motion.get().setMagnitude(controller.getVelocity());
            displayGraphics.paintPoint(position.clone());
        });

        splineDisplay.display();

        while(true) {
            splineDisplay.repaint();
        }
    }

}
