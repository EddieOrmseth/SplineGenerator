package SplineGenerator;

import SplineGenerator.Applied.GeneralVelocityController;
import SplineGenerator.Applied.Navigator;
import SplineGenerator.Applied.Segmenter;
import SplineGenerator.Applied.VelocityController;
import SplineGenerator.GUI.*;
import SplineGenerator.Splines.PolynomicSpline;
import SplineGenerator.Splines.Spline;
import SplineGenerator.Util.*;

public class VelocityTestMain {

    public static void main(String... args) {

        KeyBoardListener.initialize();

        Function<Double, Double> accelerationFunction = GeneralVelocityController.getLinearThreshAccelerationFunction(.2, .01);
        DVector startingVelocity = new DVector(0, 0);
        GeneralVelocityController velocityController = new GeneralVelocityController(100, 100, accelerationFunction, startingVelocity, 15, 5);

        PolynomicSpline spline = new PolynomicSpline(2);

        // /* Crazy Shit
        spline.addControlPoint(new DControlPoint(new DVector(9, 1), new DDirection(Math.cos(0), Math.sin(0)), new DDirection(0, 0), new DDirection(0, 0)));
        spline.addControlPoint(new DControlPoint(new DVector(3, 3)));
        spline.addControlPoint(new DControlPoint(new DVector(-10, 10)));
        spline.addControlPoint(new DControlPoint(new DVector(2, 4)));
        spline.addControlPoint(new DControlPoint(new DVector(0, 6)));
        spline.addControlPoint(new DControlPoint(new DVector(-2, 0)));
        spline.addControlPoint(new DControlPoint(new DVector(0, 2)));
        spline.addControlPoint(new DControlPoint(new DVector(-5, 5)));
        spline.addControlPoint(new DControlPoint(new DVector(-10, 5)));
        spline.addControlPoint(new DControlPoint(new DVector(-7, 9)));
        spline.addControlPoint(new DControlPoint(new DVector(-8, -11), new DDirection(Math.cos(Math.PI / 2), Math.sin(Math.PI / 2)), new DDirection(Math.cos(0), Math.sin(0))));
        // */

         /* Figure 8
        spline.addControlPoint(new DControlPoint(new DVector(0, 0), new DDirection(-Math.cos(Math.PI / 4), Math.sin(-Math.PI / 4)), new DDirection(0, 0)));
        spline.addControlPoint(new DControlPoint(new DVector(10, -10)));
        spline.addControlPoint(new DControlPoint(new DVector(20, 0)));
        spline.addControlPoint(new DControlPoint(new DVector(10, 10)));
        spline.addControlPoint(new DControlPoint(new DVector(0, 0)));
        spline.addControlPoint(new DControlPoint(new DVector(-10, -10)));
        spline.addControlPoint(new DControlPoint(new DVector(-20, 0)));
        spline.addControlPoint(new DControlPoint(new DVector(-10, 10), new DDirection(Math.cos(0), Math.sin(0)), new DDirection(0, 0)));
        // */

        // /* Spline Stuff
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

        System.out.println(spline.printMatrices());

        System.out.println("Time to Generate: " + (endTimeGenerate - startTimeGenerate) + " milliseconds");

        spline.takeNextDerivative();
        spline.takeNextDerivative();

        SplineDisplay display = new SplineDisplay(spline, 0, 1, 1600, 700);
        display.onGridBoundaries = new Extrema(new DPoint(-25, -20), new DPoint(25, 20));

        display.onSplineDisplayables.add(tValue -> {
            return new DPosVector(spline.get(tValue), spline.evaluateDerivative(tValue, 1));
        });

        display.onSplineDisplayables.add(tValue -> {
            return new DPosVector(spline.get(tValue), spline.evaluateDerivative(tValue, 2));
        });
        // */

        // /* Segmenter
        Function<DVector, DVector> distanceModifier = variable -> {
            variable.multiplyAll(6);
            return variable;
        };

        Function<DVector, DVector> derivativeModifier = variable -> {
            variable.setMagnitude(10);
            return variable;
        };

        Segmenter segmenter = new Segmenter(spline, derivativeModifier, distanceModifier);
        segmenter.bounds = new Extrema(new DPoint(-30, -20), new DPoint(30, 20));

        long startTimeCompute = System.currentTimeMillis();
        segmenter.computeGradient();
        long endTimeCompute = System.currentTimeMillis();

        System.out.println("Time to Compute: " + ((endTimeCompute - startTimeCompute) / 1000.0) + " seconds");

        Navigator.Controller positionController = segmenter.getController();
        NewBallVelocityDirectionController ball = new NewBallVelocityDirectionController(positionController, new DPoint(0, 0));
        ball.velocityController = velocityController;

        display.displayables.add(ball);

        ball.start();
        display.display();

        while (true) {
            display.repaint();
        }
    }

}
