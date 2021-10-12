package SplineGenerator;

import SplineGenerator.Applied.FollowerGradient;
import SplineGenerator.Applied.IntersectionResolver;
import SplineGenerator.GUI.BallFollowerGradient;
import SplineGenerator.GUI.BallIntersectionResolver;
import SplineGenerator.GUI.KeyBoardListener;
import SplineGenerator.GUI.SplineDisplay;
import SplineGenerator.Splines.PolynomicSpline;
import SplineGenerator.Splines.Spline;
import SplineGenerator.Util.*;

public class Main {

    public static void main(String[] args) {

        KeyBoardListener.initialize();

        PolynomicSpline spline = new PolynomicSpline(2);
//        spline.addControlPoint(new DControlPoint(new DVector(9, 1), new DDirection(Math.cos(0), Math.sin(0)), new DDirection(0, 0), new DDirection(0, 0)));
//        spline.addControlPoint(new DControlPoint(new DVector(3, 3)));
//        spline.addControlPoint(new DControlPoint(new DVector(-10, 10)));
//        spline.addControlPoint(new DControlPoint(new DVector(2, 4)));
//        spline.addControlPoint(new DControlPoint(new DVector(0, 6)));
//        spline.addControlPoint(new DControlPoint(new DVector(-2, 0)));
//        spline.addControlPoint(new DControlPoint(new DVector(0, 2)));
//        spline.addControlPoint(new DControlPoint(new DVector(-5, 5)));
//        spline.addControlPoint(new DControlPoint(new DVector(-10, 5)));
//        spline.addControlPoint(new DControlPoint(new DVector(-7, 9)));
//        spline.addControlPoint(new DControlPoint(new DVector(-8, -11), new DDirection(Math.cos(Math.PI / 2), Math.sin(Math.PI / 2)), new DDirection(Math.cos(0), Math.sin(0))));

        // Figure 8
        spline.addControlPoint(new DControlPoint(new DVector(0, 0), new DDirection(-Math.cos(Math.PI / 4), Math.sin(-Math.PI / 4)), new DDirection(0, 0)));
        spline.addControlPoint(new DControlPoint(new DVector(10, -10)));
        spline.addControlPoint(new DControlPoint(new DVector(20, 0)));
        spline.addControlPoint(new DControlPoint(new DVector(10, 10)));
        spline.addControlPoint(new DControlPoint(new DVector(0, 0)));
        spline.addControlPoint(new DControlPoint(new DVector(-10, -10)));
        spline.addControlPoint(new DControlPoint(new DVector(-20, 0)));
        spline.addControlPoint(new DControlPoint(new DVector(-10, 10), new DDirection(Math.cos(0), Math.sin(0)), new DDirection(0, 0)));

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

        long startTimeGenerate = System.currentTimeMillis();
        spline.generate();
        long endTimeGenerate = System.currentTimeMillis();

        System.out.println("Time to Generate: " + (endTimeGenerate - startTimeGenerate) + " milliseconds");

//        System.out.println(spline.printMatrices());
//        System.out.println(spline.getDesmosEquations());
//        System.out.println(spline);

        spline.takeNextDerivative();
        spline.takeNextDerivative();

        DPoint myPoint = new DPoint(0, 0);
        DPoint point = spline.findClosestPointOnSegment(myPoint, 1, .001);
        System.out.println(point);

        SplineDisplay display = new SplineDisplay(spline, 0, 1, 1600, 700);
        display.onGridBoundaries = new Extrema(new DPoint(-25, -20), new DPoint(25, 20));

        Function<DVector, DVector> distanceModifier = variable -> {
            variable.multiplyAll(6);
            return variable;
        };

        Function<DVector, DVector> derivativeModifier = variable -> {
            variable.setMagnitude(10);
            return variable;
        };

         /* Follower Gradient
        FollowerGradient follower = new FollowerGradient(spline, derivativeModifier, distanceModifier);
        follower.bounds = new Extrema(new DPoint(-30, -20), new DPoint(30, 20));

        long startTimeCompute = System.currentTimeMillis();
        follower.computeGradient();
        long endTimeCompute = System.currentTimeMillis();

        System.out.println("Time to Compute: " + (endTimeCompute - startTimeCompute) + " milliseconds");

        display.displayGradient(follower);
        display.displayables.add(new BallFollowerGradient(follower, new DPoint(spline.getDimensions())));
        // */

        // /* IntersectionResolver
        IntersectionResolver resolver = new IntersectionResolver(spline, derivativeModifier, distanceModifier);
        resolver.bounds = new Extrema(new DPoint(-30, -20), new DPoint(30, 20));

        long startTimeCompute = System.currentTimeMillis();
        resolver.computeGradient();
        long endTimeCompute = System.currentTimeMillis();

        System.out.println("Time to Compute: " + (endTimeCompute - startTimeCompute) + " milliseconds");

        display.displayables.add(new BallIntersectionResolver(resolver, new DPoint(10, 13, 1)));
        // */

        display.display();

        while (true) {
            display.repaint();
        }
    }

}
