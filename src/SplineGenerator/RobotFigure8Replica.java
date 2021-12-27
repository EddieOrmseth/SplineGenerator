package SplineGenerator;

import SplineGenerator.Applied.LegacyVersions.OldVelocityController;
import SplineGenerator.Applied.LegacyVersions.SegmenterComplexVelocityController;
import SplineGenerator.Applied.Segmenter;
import SplineGenerator.GUI.BallVelocityDirectionController;
import SplineGenerator.GUI.Display;
import SplineGenerator.GUI.KeyBoardListener;
import SplineGenerator.GUI.SplineDisplay;
import SplineGenerator.Splines.PolynomicSpline;
import SplineGenerator.Splines.Spline;
import SplineGenerator.Util.*;

public class RobotFigure8Replica {

    public static void main(String... args) {

        KeyBoardListener.initialize();

        PolynomicSpline spline = new PolynomicSpline(2);

        spline.addControlPoint(new DControlPoint(new DVector(0, 0), new DDirection(1, 0)));
        spline.addControlPoint(new DControlPoint(new DVector(1, 1), new DDirection(1, 0)));
        spline.addControlPoint(new DControlPoint(new DVector(2, 0), new DDirection(1, 0)));
        spline.addControlPoint(new DControlPoint(new DVector(1, -1), new DDirection(1, 0)));
        spline.addControlPoint(new DControlPoint(new DVector(0, 0), new DDirection(1, 0)));
        spline.addControlPoint(new DControlPoint(new DVector(-1, 1), new DDirection(1, 0)));
        spline.addControlPoint(new DControlPoint(new DVector(-2, 0), new DDirection(1, 0)));
        spline.addControlPoint(new DControlPoint(new DVector(-1, -1), new DDirection(1, 0)));

        spline.setPolynomicOrder(5);
        spline.closed = true;

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

        Function<DVector, DVector> derivativeModifier = variable -> {
            variable.setMagnitude(10);
            return variable;
        };

        Function<DVector, DVector> distanceModifier = variable -> {
            variable.multiplyAll(35.001);
            return variable;
        };

        Segmenter segmenter = new Segmenter(spline, derivativeModifier, distanceModifier);
        segmenter.bounds = new Extrema(new DPoint(-5, -5), new DPoint(5, 5));
        segmenter.followerStep = .05;

        segmenter.computeGradient();

        Segmenter.Controller navigatorController = segmenter.getController();
        OldVelocityController velocityController = new SegmenterComplexVelocityController(navigatorController, 1.7, 1, 0, .2, .2);

        BallVelocityDirectionController ball = new BallVelocityDirectionController(navigatorController, new DPoint(0, 0));
        ball.velocityController = velocityController;

        SplineDisplay display = new SplineDisplay(spline, 0, 1, 1600, 700);
        display.displayables.add(ball);

        ball.start();

        display.display();

        while (true) {
            display.repaint();
        }
    }

}
