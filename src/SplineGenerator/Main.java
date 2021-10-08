package SplineGenerator;

import SplineGenerator.Applied.FollowerGradient;
import SplineGenerator.GUI.BallFollower;
import SplineGenerator.GUI.KeyBoardListener;
import SplineGenerator.GUI.SplineDisplay;
import SplineGenerator.Splines.PolynomicSpline;
import SplineGenerator.Splines.Spline;
import SplineGenerator.Util.*;

public class Main {

    public static void main(String[] args) {

        KeyBoardListener.initialize();

        PolynomicSpline spline = new PolynomicSpline(2);
        spline.addControlPoint(new DControlPoint(new DVector(9, 1), new DDirection(Math.cos(0), Math.sin(0)), new DDirection(0, 0), new DDirection(0, 0)));
        spline.addControlPoint(new DControlPoint(new DVector(3, 3)));
        spline.addControlPoint(new DControlPoint(new DVector(-10, 10)));
//        spline.addControlPoint(new DControlPoint(new DVector(2, 4)));
//        spline.addControlPoint(new DControlPoint(new DVector(0, 6)));
//        spline.addControlPoint(new DControlPoint(new DVector(-2, 0)));
//        spline.addControlPoint(new DControlPoint(new DVector(0, 2)));
//        spline.addControlPoint(new DControlPoint(new DVector(-5, 5)));
//        spline.addControlPoint(new DControlPoint(new DVector(-10, 5)));
//        spline.addControlPoint(new DControlPoint(new DVector(-7, 9)));
        spline.addControlPoint(new DControlPoint(new DVector(-8, -11), new DDirection(Math.cos(Math.PI / 2), Math.sin(Math.PI / 2)), new DDirection(Math.cos(0), Math.sin(0))));

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

        long startTime = System.currentTimeMillis();
        spline.generate();
        long endTime = System.currentTimeMillis();

        System.out.println("Time to Generate: " + (endTime - startTime) + " milliseconds");

        System.out.println(spline.printMatrices());
        System.out.println(spline.getDesmosEquations());
        System.out.println(spline);

        spline.takeNextDerivative();
        spline.takeNextDerivative();

        DPoint myPoint = new DPoint(0, 0);
        DPoint point = spline.findClosestPointOnSegment(myPoint, 1, .001);
        System.out.println(point);

        SplineDisplay display = new SplineDisplay(spline, 0, 1, 1600, 700);
        display.onGridBoundaries = new Extrema(new DPoint(-15, -13), new DPoint(14, 15));

        Function<DVector, DVector> distanceModifier = variable -> {
            variable.multiplyAll(6);
            return variable;
        };

        Function<DVector, DVector> derivativeModifier = variable -> {
            variable.setMagnitude(10);
            return variable;
        };

        FollowerGradient follower = new FollowerGradient(spline, derivativeModifier, distanceModifier);
        follower.bounds = new Extrema(new DPoint(-20, -20), new DPoint(20, 20));
        follower.computeGradient();

        display.displayGradient(follower);
        display.displayables.add(new BallFollower(follower, new DPoint(spline.getDimensions())));
//        display.onGridDisplayables.add(gridPoint -> {
//            DPoint pt = spline.findClosestPointOnSpline(gridPoint, .01);
//            DVector deriv = spline.evaluateDerivative(pt.get(pt.getDimensions() - 1), 1);
//
//            return new DPosVector(gridPoint, deriv);
//        });

//        display.onSplineDisplayables.add(t -> {
//            DVector vector = spline.evaluateDerivative(t, 1);
//            return new DPosVector(spline.get(t), vector);
//        });
//        display.onSplineDisplayables.add(t -> {
//            DVector vector = spline.evaluateDerivative(t, 2);
//            return new DPosVector(spline.get(t), vector);
//        });

        display.display();

        while (true) {
            display.repaint();
        }
    }

}
