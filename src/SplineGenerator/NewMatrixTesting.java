package SplineGenerator;

import SplineGenerator.Applied.ArcLengthConverter;
import SplineGenerator.Applied.SplineVelocityController;
import SplineGenerator.GUI.KeyBoardListener;
import SplineGenerator.GUI.SplineDisplay;
import SplineGenerator.Splines.PolynomicSpline;
import SplineGenerator.Splines.Spline;
import SplineGenerator.Util.*;

import java.util.concurrent.atomic.AtomicReference;

public class NewMatrixTesting {

    public static void main(String... args) {

        double[][] mLeft = {{.8333333333, -.8333333333}, {.833333333, .166666666}};
        double[][] mRight = {{.09}, {.2}};

        Matrix left = new Matrix(mLeft);
        Matrix right = new Matrix(mRight);

        Matrix result = left.multiply(right);
        System.out.println(result);

    }

    public static class TestArcLengthControl {

        public static void main(String... args) {

            KeyBoardListener.initialize();

            PolynomicSpline spline = new PolynomicSpline(2);

            spline.addControlPoint(new DControlPoint(new DVector(0, 0), new DVector(5, 0), new DDirection(0, 0)));
            spline.addControlPoint(new DControlPoint(new DVector(4, 2)));
            spline.addControlPoint(new DControlPoint(new DVector(8, 0), new DDirection(1, 0), new DDirection(0, 0)));

            spline.setPolynomicOrder(5);
            spline.closed = false;

            InterpolationInfo c1 = new InterpolationInfo();
            c1.interpolationType = Spline.InterpolationType.Linked;
            c1.endBehavior = Spline.EndBehavior.Hermite;
            spline.interpolationTypes.add(c1);

            InterpolationInfo c2 = new InterpolationInfo();
            c2.interpolationType = Spline.InterpolationType.Linked;
            c2.endBehavior = Spline.EndBehavior.Hermite;
            spline.interpolationTypes.add(c2);

            InterpolationInfo c3 = new InterpolationInfo();
            c3.interpolationType = Spline.InterpolationType.Linked;
            c3.endBehavior = Spline.EndBehavior.None;
            spline.interpolationTypes.add(c3);

            InterpolationInfo c4 = new InterpolationInfo();
            c4.interpolationType = Spline.InterpolationType.Linked;
            c4.endBehavior = Spline.EndBehavior.None;
            spline.interpolationTypes.add(c4);

            spline.generate();
            spline.takeNextDerivative();

            SplineVelocityController splineVelocityController = new SplineVelocityController(spline, 1.7, 1, 0, .2, .2);
            ArcLengthConverter arcLengthConverter = new ArcLengthConverter(spline, .05, splineVelocityController);

            arcLengthConverter.computeMovement();
            arcLengthConverter.addEndVelocity(0,.1);

            ArcLengthConverter.Controller controller = arcLengthConverter.getController();

            SplineDisplay splineDisplay = new SplineDisplay(spline, 0, 1, 1600, 700);

            AtomicReference<Double> arcLength = new AtomicReference<>(0.0);
            DPoint position = new DPoint(0, 0);
            AtomicReference<DVector> motion = new AtomicReference<>(new DVector(0, 0));
            AtomicReference<Long> time = new AtomicReference<>(0L);

            splineDisplay.displayables.add((displayGraphics) -> {
                double delta = (System.currentTimeMillis() - time.get()) / 1000.0;
                time.set(System.currentTimeMillis());

                motion.get().multiplyAll(delta);
                position.add(motion.get());
                arcLength.set(arcLength.get() + motion.get().getMagnitude());
                System.out.println(motion.get().getMagnitude());
                controller.update(new DPoint(arcLength.get()));
                System.out.println(controller.getDirection());
                motion.set(controller.getDirection());
                motion.get().setMagnitude(controller.getVelocity());

    //            System.out.println
    //            arcLength.get());

                displayGraphics.paintPoint(position.clone());
            });

            splineDisplay.display();

            while (true) {
                splineDisplay.repaint();
            }

        }

    }
}
