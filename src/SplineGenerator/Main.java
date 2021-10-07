package SplineGenerator;

import SplineGenerator.Applied.FollowerGradient;
import SplineGenerator.GUI.SplineDisplay;
import SplineGenerator.Splines.PolynomicSpline;
import SplineGenerator.Splines.Spline;
import SplineGenerator.Util.*;

public class Main {

    public static void main(String[] args) {

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
//        System.out.println(spline.printAsSpline(spline.derivatives.get(1)));

        DPoint myPoint = new DPoint(0, 0);
        DPoint point = spline.findClosestPointOnSegment(myPoint, 1, .001);
        System.out.println(point);

//        System.out.println(spline.getExtrema(.01));

        SplineDisplay display = new SplineDisplay(spline, 0, 1);
//        display.onSplineDisplayables.add((t) -> {
//            DVector derivative = spline.evaluateDerivative(t, 1);
//            DPoint startPoint = spline.get(t);
//
//            return new DPosVector(startPoint, derivative);
//        });

//        display.onGridDisplayables.add(gridPoint -> {
//           DPoint nearestPoint = spline.findClosestPointOnSpline(gridPoint, .01);
//
//           return new DPosVector(gridPoint, nearestPoint);
//        });

        Function<DVector, DVector> distanceModifier = variable -> {
            variable.multiplyAll(6);
            return variable;
        };

        Function<DVector, DVector> derivativeModifier = variable -> {
            variable.setMagnitude(10);
            return variable;
        };

        FollowerGradient follower = new FollowerGradient(spline, derivativeModifier, distanceModifier);

        display.onGridDisplayables.add(gridPoint -> {
            DVector followerPoint = follower.evaluateAt(gridPoint);
            return new DPosVector(gridPoint, followerPoint);
        });

//        display.onGridDisplayables.add(gridPoint -> {
//            DPoint pointOnSpline = spline.findClosestPointOnSpline(gridPoint, .01);
//            DVector position = new DVector(gridPoint.clone(), pointOnSpline);
//            position.multiplyAll(6);
//
//            DVector derivative = spline.evaluateDerivative(pointOnSpline.get(pointOnSpline.getDimensions() - 1), 1);
//            derivative.setMagnitude(10);
//            derivative.add(position);
//
//            return new DPosVector(gridPoint, derivative);
//        });

        display.display();
    }

}
