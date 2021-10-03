package SplineGenerator;

import SplineGenerator.GUI.SplineDisplay;
import SplineGenerator.Splines.PolynomicSpline;
import SplineGenerator.Splines.Spline;
import SplineGenerator.Util.*;

public class Main {

    public static void main(String[] args) {

        PolynomicSpline spline = new PolynomicSpline(2);
        spline.addControlPoint(new DControlPoint(new DVector(1, 1), new DDirection(Math.cos(0), Math.sin(0)), new DDirection(0, 0), new DDirection(0, 0)));
//        spline.addControlPoint(new DControlPoint(new DVector(5, 3)));
//        spline.addControlPoint(new DControlPoint(new DVector(3, 3)));
        spline.addControlPoint(new DControlPoint(new DVector(2, 4)));
//        spline.addControlPoint(new DControlPoint(new DVector(0, 6)));
//        spline.addControlPoint(new DControlPoint(new DVector(-2, 0)));
//        spline.addControlPoint(new DControlPoint(new DVector(0, 2)));
        spline.addControlPoint(new DControlPoint(new DVector(-5, 5)));
//        spline.addControlPoint(new DControlPoint(new DVector(-10, 5)));
        spline.addControlPoint(new DControlPoint(new DVector(-7, 9)));
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

//        spline.takeNextDerivative();
//        spline.takeNextDerivative();
//        System.out.println(spline.printAsSpline(spline.derivatives.get(1)));
//        System.out.println(spline.printAsSpline(spline.derivatives.get(2)));

        DPoint myPoint = new DPoint(0, 0);
        DPoint point = spline.findClosestPointOnSegment(myPoint, 1, .001);
        System.out.println(point);

        SplineDisplay display = new SplineDisplay(spline, 0, 1);
        display.display();
    }

}
