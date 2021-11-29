package SplineGenerator;

import SplineGenerator.Applied.PathFinderV2;
import SplineGenerator.GUI.BallDirectionFollower;
import SplineGenerator.GUI.KeyBoardListener;
import SplineGenerator.GUI.LineOfLineDirectionFollowers;
import SplineGenerator.GUI.SplineDisplay;
import SplineGenerator.Splines.PolynomicSpline;
import SplineGenerator.Splines.Spline;
import SplineGenerator.Util.*;
import SplineGenerator.Util.PathAugments.StandardPointTarget;
import SplineGenerator.Util.PathAugments.StreamPointObstacle;

import java.awt.*;

public class Main {

    public static void main(String[] args) {

        KeyBoardListener.initialize();

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
        //*/

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

        spline.takeNextDerivative();
        spline.takeNextDerivative();

        SplineDisplay display = new SplineDisplay(spline, 0, 1, 1600, 700);
        display.onGridBoundaries = new Extrema(new DPoint(-25, -20), new DPoint(25, 20));

         /* Segmenter
        Function<DVector, DVector> distanceModifier = variable -> {
            variable.multiplyAll(6);
            return variable;
        };

        Function<DVector, DVector> derivativeModifier = variable -> {
            variable.setMagnitude(10);
            return variable;
        };

        Segmenter resolver = new Segmenter(spline, derivativeModifier, distanceModifier);
        resolver.bounds = new Extrema(new DPoint(-30, -20), new DPoint(30, 20));

        long startTimeCompute = System.currentTimeMillis();
        resolver.computeGradient();
        long endTimeCompute = System.currentTimeMillis();

        System.out.println("Time to Compute: " + ((endTimeCompute - startTimeCompute) / 1000.0) + " seconds");

        Segmenter.Controller ballController = resolver.getController();
        BallDirectionFollower ball = new BallDirectionFollower(ballController, new DPoint(0, 0));
        display.displayables.add(ball);

        display.onGridDisplayables.add(gridPoint -> {
            int segment = ballController.segment;
            if (segment != -1) {
                DVector vector = resolver.get(gridPoint.clone()).get(segment);
                if (vector != null) {
                    return new DPosVector(gridPoint.clone(), vector);
                } else {
                    return new DVector(gridPoint.getDimensions());
                }
            }
            return new DVector(gridPoint.getDimensions());
        });
        // */

         /* Display the derivative on the spline
        display.onSplineDisplayables.add(tValue -> {
            DPoint point = spline.get(tValue);
            DVector derivative = spline.evaluateDerivative(tValue, 1);
            return new DPosVector(point, derivative);
        });
        // */

        // /* PathFinderV2
        PathFinderV2 pathFinder = new PathFinderV2(2);

        StandardPointTarget target = new StandardPointTarget(2, new DPoint(-15, -10), 4, 1);
        pathFinder.setTarget(target);
        display.displayables.add(target);

//        StreamPointObstacle obstacle0 = new StreamPointObstacle(2, new DPoint(0, 0), 200, -3, -200);
//        pathFinder.addAugment(obstacle0);
//        display.displayables.add(obstacle0);

        StreamPointObstacle obstacle1 = new StreamPointObstacle(2, new DPoint(2, 4), 200, -3, -200);
        pathFinder.addAugment(obstacle1);
        display.displayables.add(obstacle1);

        StreamPointObstacle obstacle2 = new StreamPointObstacle(2, new DPoint(0, -1), 200, -3, -200);
        pathFinder.addAugment(obstacle2);
        display.displayables.add(obstacle2);

        StreamPointObstacle obstacle3 = new StreamPointObstacle(2, new DPoint(0, 3), 200, -3, -200);
        pathFinder.addAugment(obstacle3);
        display.displayables.add(obstacle3);
//
//        StreamCircleObstacle circle0 = new StreamCircleObstacle(2, new DPoint(0, 0), 3,200, -3, -250);
//        pathFinder.addAugment(circle0);
//        display.displayables.add(circle0);

        BallDirectionFollower ballDirectionFollower = new BallDirectionFollower(pathFinder.getController(), new DPoint(15, 10));
        display.displayables.add(ballDirectionFollower);

        int numLines = 10;
        Function<Integer, Color> colorFunction = i -> {
            double kB = i / 10.0;
            double kR = 1 - kB;
            return new Color((int) (kR * 255), 0, (int) (kB * 255));
//            return new Color(255, 255, 255);
        };
        LineOfLineDirectionFollowers lines = new LineOfLineDirectionFollowers(pathFinder, new DPoint(13, 12), new DPoint(17, 8), numLines, colorFunction, 3, .1, 0, 1);
//        display.displayables.add(lines);


         /*
        display.onGridDisplayables.add(gridPoint -> {

        });
        // */

        ballDirectionFollower.start();
//        lines.start();

        display.display();

        while (true) {
            display.repaint();
        }
    }

}
