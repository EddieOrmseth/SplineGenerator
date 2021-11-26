package SplineGenerator;

import SplineGenerator.Applied.PathFinder;
import SplineGenerator.Applied.Segmenter;
import SplineGenerator.GUI.BallDirectionFollower;
import SplineGenerator.GUI.KeyBoardListener;
import SplineGenerator.GUI.SplineDisplay;
import SplineGenerator.Splines.PolynomicSpline;
import SplineGenerator.Splines.Spline;
import SplineGenerator.Util.*;

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
        display.displayables.add(new BallDirectionFollower(follower.getController(), new DPoint(spline.getDimensions())));
        // */

         /* IntersectionResolver
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

        // /* Create PathFinder
        PathFinder pathFinder = new PathFinder(new Extrema(new DPoint(-25, -20), new DPoint(25, 20)), .25);

//        DPoint p1 = new DPoint(5, -5);
//        pathFinder.addModifier(new PathFinder.Obstacle(PathFinder.getCircleDistanceFunction(p1, .5), gridPoint -> {
////            if (gridPoint.getMagnitude() < 5) {
////                gridPoint.setMagnitude(0);
////            } else {
//                gridPoint.setMagnitude(45 * (Math.pow(1 / gridPoint.getMagnitude(), 2)));
////            }
//            return gridPoint;
//        }));

        DPoint p2f = new DPoint(5, -5);
        Function<DPoint, DVector> p2fModifier = gridPoint -> {
            DVector vector = new DVector(p2f, gridPoint);
            vector.setMagnitude(15 * (Math.pow(1 / vector.getMagnitude(), 1)));
            return vector;
        };
        pathFinder.addModifier(p2fModifier);

//        DPoint p3 = new DPoint(5, 5);
//        pathFinder.addModifier(gridPoint -> {
//            DVector vector = new DVector(p3, gridPoint);
//            vector.setMagnitude(15 * (Math.pow(1 / vector.getMagnitude(), 1)));
//            return vector;
//        });

//        DPoint ls1p1 = new DPoint(-10, 10);
//        DPoint ls1p2 = new DPoint(10, -10);
//        pathFinder.addModifier(new PathFinder.Obstacle(PathFinder.getLineSegment2DDistanceFunction(ls1p1, ls1p2), new Function<DVector, DVector>() {
//            @Override
//            public DVector get(DVector variable) {
//                variable.setMagnitude(15 * (Math.pow(1 / variable.getMagnitude(), 1)));
//                return variable;
//            }
//        }));

        DPoint destination = new DPoint(-15, -10);
        pathFinder.addModifier(gridPoint -> {
            DVector vector = new DVector(gridPoint, destination);
            vector.setMagnitude(4);
            return vector;
        });

        pathFinder.compute();

        pathFinder.removeModifier(p2fModifier);

        PathFinder.Controller ballController = pathFinder.getController();
        BallDirectionFollower ball = new BallDirectionFollower(ballController, new DPoint(15, 10));
        display.displayables.add(ball);
        // */

        DPoint p2 = new DPoint(5, -5);
        pathFinder.addModifier(gridPoint -> {
//            DVector vector = new DVector(p2, gridPoint);
//            vector.setMagnitude(15 * (Math.pow(1 / vector.getMagnitude(), 1)));
//            return vector;

            DVector vectorBetween = new DVector(p2, gridPoint);
            DVector velocity = ballController.velocity.clone();

            if (vectorBetween.dot(velocity) > 0) {
                System.out.println("Returning Here");
                return new DDirection(vectorBetween.getDimensions());
            }

            System.out.println("Here");

            double betweenMag = vectorBetween.getMagnitude();
            double projMag = velocity.projectOnto(vectorBetween.clone()).getMagnitude();

            velocity.multiplyAll(betweenMag / projMag);

            DVector orth = velocity.add(vectorBetween);
            orth.setMagnitude(15 * (Math.pow(1 / vectorBetween.getMagnitude(), 1)));
            return orth.toDirection();
        });

        // /* Display PathFinder On Grid
        display.onGridDisplayables.add(gridPoint -> {
            return new DPosVector(gridPoint, pathFinder.getDirection(gridPoint.clone()).toDirection());
        });
        // */

        display.display();

        while (true) {
            display.repaint();
        }
    }

}
