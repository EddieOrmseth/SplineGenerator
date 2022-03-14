package SplineGenerator;

import SplineGenerator.Applied.SegmenterComplexVelocityController;
import SplineGenerator.Applied.Segmenter;
import SplineGenerator.GUI.BallVelocityDirectionController;
import SplineGenerator.GUI.KeyBoardListener;
import SplineGenerator.GUI.SplineDisplay;
import SplineGenerator.Splines.PolynomicSpline;
import SplineGenerator.Splines.Spline;
import SplineGenerator.Util.*;

import java.awt.*;

public class Main {

    public static void main(String[] args) {


        KeyBoardListener.initialize();

        DVector translation = new DVector(3, -2);
        System.out.println("Initial Translation: " + translation);

        DVector TLRotation = new DVector(-1, -1);
        DVector TRRotation = new DVector(-1, 1);
        DVector BRRotation = new DVector(1, 1);
        DVector BLRotation = new DVector(1, -1);

        TLRotation.add(translation);
        TRRotation.add(translation);
        BRRotation.add(translation);
        BLRotation.add(translation);

        DVector result = new DVector(0, 0);
        result.add(TLRotation).add(TRRotation).add(BRRotation).add(BLRotation).multiplyAll(.25);
        System.out.println("Result: " + result);

        int cat = 120;

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

        Segmenter resolver = new Segmenter(spline, derivativeModifier, distanceModifier);
        resolver.bounds = new Extrema(new DPoint(-30, -20), new DPoint(30, 20));

        long startTimeCompute = System.currentTimeMillis();
        resolver.computeGradient();
        long endTimeCompute = System.currentTimeMillis();

        System.out.println("Time to Compute: " + ((endTimeCompute - startTimeCompute) / 1000.0) + " seconds");

//        Segmenter.Controller ballController0 = resolver.getController();
//        SimpleVelocityController velocityController0 = new SimpleVelocityController(ballController0, .015,.005, .01);
//        BallVelocityDirectionController ballFollower0 = new BallVelocityDirectionController(ballController0, new DPoint(0, 0));
//        ballFollower0.color = new Color(193, 98, 98);
//        ballFollower0.velocityController = velocityController0;
//        display.displayables.add(ballFollower0);

        Segmenter.Controller ballController1 = resolver.getController();
        SegmenterComplexVelocityController velocityController1 = new SegmenterComplexVelocityController(ballController1, 01500, 00500, 01000, .4, .1);
        BallVelocityDirectionController ballFollower1 = new BallVelocityDirectionController(ballController1, new DPoint(0, 0));
        ballFollower1.color = new Color(105, 105, 239);
        ballFollower1.velocityController = velocityController1;
        display.displayables.add(ballFollower1);

//        display.onGridDisplayables.add(gridPoint -> {
//            int segment = ballController0.segment;
//            if (segment != -1) {
//                DVector vector = resolver.get(gridPoint.clone()).get(segment);
//                if (vector != null) {
//                    return new DPosVector(gridPoint.clone(), vector);
//                } else {
//                    return new DVector(gridPoint.getDimensions());
//                }
//            }
//            return new DVector(gridPoint.getDimensions());
//        });
//
//        display.onGridDisplayables.add(gridPoint -> {
//            int segment = ballController1.segment;
//            if (segment != -1) {
//                DVector vector = resolver.get(gridPoint.clone()).get(segment);
//                if (vector != null) {
//                    return new DPosVector(gridPoint.clone(), vector);
//                } else {
//                    return new DVector(gridPoint.getDimensions());
//                }
//            }
//            return new DVector(gridPoint.getDimensions());
//        });

        // */

         /* Display the derivative on the spline
        display.onSplineDisplayables.add(tValue -> {
            DPoint point = spline.get(tValue);
            DVector derivative = spline.evaluateDerivative(tValue, 1);
            return new DPosVector(point, derivative);
        });
        // */

         /* PathFinderV2
        Extrema extrema = new Extrema(new DPoint(-12, -12), new DPoint(12, 12));
        Display display = new Display(2, extrema, 0, 1, 1600, 700);
        PathFinderV2 pathFinder = new PathFinderV2(2);

        StandardPointTarget target = new StandardPointTarget(2, new DPoint(-15, -10), 4, 1);
        pathFinder.setTarget(target);
        display.displayables.add(target);

//        StreamPointObstacle obstacle0 = new StreamPointObstacle(2, new DPoint(0, 0), 200, -3, -200);
//        pathFinder.addAugment(obstacle0);
//        display.displayables.add(obstacle0);
//
        StreamPointObstacle obstacle1 = new StreamPointObstacle(2, new DPoint(2, 4), 200, -3, -200);
        pathFinder.addAugment(obstacle1);
        display.displayables.add(obstacle1);

        StreamPointObstacle obstacle2 = new StreamPointObstacle(2, new DPoint(0, -1), 200, -3, -200);
        pathFinder.addAugment(obstacle2);
        display.displayables.add(obstacle2);

        StreamPointObstacle obstacle3 = new StreamPointObstacle(2, new DPoint(0, 3), 200, -3, -200);
        pathFinder.addAugment(obstacle3);
        display.displayables.add(obstacle3);

//        StreamCircleObstacle circle0 = new StreamCircleObstacle(2, new DPoint(0, 0), 3,200, -3, -250);
//        pathFinder.addAugment(circle0);
//        display.displayables.add(circle0);

//        BallDirectionFollower ballDirectionFollower = new BallDirectionFollower(pathFinder.getController(), new DPoint(15, 10));
//        display.displayables.add(ballDirectionFollower);

        int numLines = 50;
        Function<Integer, Color> colorFunction = i -> {
            double kB = i / (double) numLines;
            double kR = 1 - kB;
            return new Color((int) (kR * 255), 0, (int) (kB * 255));
//            return new Color(255, 255, 255);
        };
//        LineOfLineDirectionFollowers lines = new LineOfLineDirectionFollowers(pathFinder, new DPoint(13, 12), new DPoint(17, 8), numLines, colorFunction, 3, .1, 0, 1);
//        display.displayables.add(lines);

        LineOfLineDirectionFollowers linesLots = new LineOfLineDirectionFollowers(pathFinder, new DPoint(4, 15), new DPoint(16, 3), numLines, colorFunction, 3, .1, 0, 1);
        display.displayables.add(linesLots);

         /*
        display.onGridDisplayables.add(gridPoint -> {

        });
        // */

//        ballDirectionFollower.start();
//        lines.start();
//        linesLots.start();

         /* Big Path PathFinder
        Extrema extrema = new Extrema(new DPoint(-40, -20), new DPoint(35, 20));
        Display display = new Display(2, extrema, 0, 1, 1600, 700);
        display.onGridBoundaries = new Extrema(new DPoint(-55, -20), new DPoint(35, 20));

        PathFinder pathFinder = new PathFinder(2);

        StandardPointTarget target = new StandardPointTarget(2, new DPoint(-45, 0), 4, 1);
        pathFinder.setTarget(target);
        display.displayables.add(target);
        display.addMouseListener(target);
        // */

        // X: (-55, 35) Y: (-18, 18)

//        StreamPointObstacle obstacle0 = new StreamPointObstacle(2, new DPoint(0, 0), 200, -3, -400, -1.5);
//        pathFinder.addAugment(obstacle0);
//        display.displayables.add(obstacle0);

        double pointStreamCoefficient = -800;
        double pointStreamPower = -1.5;

        double circleStreamCoefficient = -500;
        double circleStreamPower = -1.5;

        double lineStreamCoefficient = -800;
        double lineStreamPower = -1.5;

         /* Big Mess 1
        StreamPointObstacle obstacle1 = new StreamPointObstacle(2, new DPoint(10, 4), 200, -3, pointStreamCoefficient, pointStreamPower);
        pathFinder.addAugment(obstacle1);
        display.displayables.add(obstacle1);
//
        StreamPointObstacle obstacle2 = new StreamPointObstacle(2, new DPoint(6, 12), 200, -3, pointStreamCoefficient, pointStreamPower);
        pathFinder.addAugment(obstacle2);
        display.displayables.add(obstacle2);
//
        StreamPointObstacle obstacle3 = new StreamPointObstacle(2, new DPoint(15, 17), 200, -3, pointStreamCoefficient, pointStreamPower);
        pathFinder.addAugment(obstacle3);
        display.displayables.add(obstacle3);
//
        StreamPointObstacle obstacle4 = new StreamPointObstacle(2, new DPoint(5, -14), 200, -3, pointStreamCoefficient, pointStreamPower);
        pathFinder.addAugment(obstacle4);
        display.displayables.add(obstacle4);
//
        StreamPointObstacle obstacle5 = new StreamPointObstacle(2, new DPoint(10, -13), 200, -3, pointStreamCoefficient, pointStreamPower);
        pathFinder.addAugment(obstacle5);
        display.displayables.add(obstacle5);
//
        StreamPointObstacle obstacle6 = new StreamPointObstacle(2, new DPoint(13, -4), 200, -3, pointStreamCoefficient, pointStreamPower);
        pathFinder.addAugment(obstacle6);
        display.displayables.add(obstacle6);
//
        StreamPointObstacle obstacle7 = new StreamPointObstacle(2, new DPoint(2, 5), 200, -3, pointStreamCoefficient, pointStreamPower);
        pathFinder.addAugment(obstacle7);
        display.displayables.add(obstacle7);
//
        StreamPointObstacle obstacle8 = new StreamPointObstacle(2, new DPoint(2, 15), 200, -3, pointStreamCoefficient, pointStreamPower);
        pathFinder.addAugment(obstacle8);
        display.displayables.add(obstacle8);
//
        StreamPointObstacle obstacle9 = new StreamPointObstacle(2, new DPoint(0, 10), 200, -3, pointStreamCoefficient, pointStreamPower);
        pathFinder.addAugment(obstacle9);
        display.displayables.add(obstacle9);
//
        StreamPointObstacle obstacle10 = new StreamPointObstacle(2, new DPoint(2, -5), 200, -3, pointStreamCoefficient, pointStreamPower);
        pathFinder.addAugment(obstacle10);
        display.displayables.add(obstacle10);
//
        StreamPointObstacle obstacle11 = new StreamPointObstacle(2, new DPoint(0, -15), 200, -3, pointStreamCoefficient, pointStreamPower);
        pathFinder.addAugment(obstacle11);
        display.displayables.add(obstacle11);
//
        StreamPointObstacle obstacle12 = new StreamPointObstacle(2, new DPoint(-2, 3), 200, -3, pointStreamCoefficient, pointStreamPower);
        pathFinder.addAugment(obstacle12);
        display.displayables.add(obstacle12);
//
        StreamPointObstacle obstacle13 = new StreamPointObstacle(2, new DPoint(-4, 0), 200, -3, pointStreamCoefficient, pointStreamPower);
        pathFinder.addAugment(obstacle13);
        display.displayables.add(obstacle13);
//
        StreamPointObstacle obstacle14 = new StreamPointObstacle(2, new DPoint(-6, -8), 200, -3, pointStreamCoefficient, pointStreamPower);
        pathFinder.addAugment(obstacle14);
        display.displayables.add(obstacle14);
//
        StreamPointObstacle obstacle15 = new StreamPointObstacle(2, new DPoint(-3, -9), 200, -3, pointStreamCoefficient, pointStreamPower);
        pathFinder.addAugment(obstacle15);
        display.displayables.add(obstacle15);
//
        StreamPointObstacle obstacle16 = new StreamPointObstacle(2, new DPoint(-20, 8), 200, -3, pointStreamCoefficient, pointStreamPower);
        pathFinder.addAugment(obstacle16);
        display.displayables.add(obstacle16);
//
        StreamPointObstacle obstacle17 = new StreamPointObstacle(2, new DPoint(-20, 2), 200, -3, pointStreamCoefficient, pointStreamPower);
        pathFinder.addAugment(obstacle17);
        display.displayables.add(obstacle17);
//
        StreamPointObstacle obstacle18 = new StreamPointObstacle(2, new DPoint(-20, 0), 200, -3, pointStreamCoefficient, pointStreamPower);
        pathFinder.addAugment(obstacle18);
        display.displayables.add(obstacle18);
//
        StreamPointObstacle obstacle19 = new StreamPointObstacle(2, new DPoint(-20, -6), 200, -3, pointStreamCoefficient, pointStreamPower);
        pathFinder.addAugment(obstacle19);
        display.displayables.add(obstacle19);
//
        StreamPointObstacle obstacle20 = new StreamPointObstacle(2, new DPoint(-42, 3), 200, -3, pointStreamCoefficient, pointStreamPower);
        pathFinder.addAugment(obstacle20);
        display.displayables.add(obstacle20);
//
        StreamPointObstacle obstacle21 = new StreamPointObstacle(2, new DPoint(-40, 0), 200, -3, pointStreamCoefficient, pointStreamPower);
        pathFinder.addAugment(obstacle21);
        display.displayables.add(obstacle21);
//
        StreamCircleObstacle circle1 = new StreamCircleObstacle(2, new DPoint(-10, 5), 2.5,200, -3, circleStreamCoefficient, circleStreamPower);
        pathFinder.addAugment(circle1);
        display.displayables.add(circle1);
//
        StreamCircleObstacle circle2 = new StreamCircleObstacle(2, new DPoint(-48, -5), 4.5,200, -3, circleStreamCoefficient, circleStreamPower);
        pathFinder.addAugment(circle2);
        display.displayables.add(circle2);
        // */ // End Big Mess 1

//        StreamLineObstacle line1 = new StreamLineObstacle(new DPoint(0, 5), new DPoint(10, 0), 200, -3, lineStreamCoefficient, lineStreamPower);
//        pathFinder.addAugment(line1);
//        display.displayables.add(line1);

         /* Field 1
//        Extrema extrema = new Extrema(new DPoint(-55, -20), new DPoint(35, 20)); Size of place
        DPoint cornerTL = new DPoint(-50, 18);
        DPoint cornerTR = new DPoint(28, 18);
        DPoint cornerBR = new DPoint(28, -18);
        DPoint cornerBL = new DPoint(-50, -18);

        // Box
        StreamPolygonObstacle box = new StreamPolygonObstacle(2, 150, -3, 0, 0, cornerTL, cornerTR, cornerBR, cornerBL, cornerTL);
        pathFinder.addAugment(box);
        display.displayables.add(box);

        // Center Thing
        StreamCircleObstacle c1 = new StreamCircleObstacle(2, new DPoint(-19, -8), .75,200, -3, circleStreamCoefficient, circleStreamPower);
        pathFinder.addAugment(c1);
        display.displayables.add(c1);

        StreamCircleObstacle c2 = new StreamCircleObstacle(2, new DPoint(-22, 3), .75,200, -3, circleStreamCoefficient, circleStreamPower);
        pathFinder.addAugment(c2);
        display.displayables.add(c2);

        StreamCircleObstacle c4 = new StreamCircleObstacle(2, new DPoint(0, -3), .75,200, -3, circleStreamCoefficient, circleStreamPower);
        pathFinder.addAugment(c4);
        display.displayables.add(c4);

        // Triangle Thing
        StreamPolygonObstacle triangleThing = new StreamPolygonObstacle(2, 200, -3, lineStreamCoefficient, lineStreamPower, new DPoint(8, 18), new DPoint(-3, 8), new DPoint(-14, 18));
        pathFinder.addAugment(triangleThing);
        display.displayables.add(triangleThing);
        // */ // End Field 1

         /* LineOfLineDirectionFollowers
        int numLines = 50;
        Function<Integer, Color> colorFunction = i -> {
            double kB = i / (double) numLines;
            double kR = 1 - kB;
            return new Color((int) (kR * 255), 0, (int) (kB * 255));
        };
        LineOfLineDirectionFollowers lines = new LineOfLineDirectionFollowers(pathFinder, new DPoint(35, 20), new DPoint(35, -20), numLines, colorFunction, 3, .1, 0, 1);
        display.displayables.add(lines);
        // */ // End LineOfLineDirectionFollowers

         /* Line Motion Testing
        StreamLineObstacle basicLine = new StreamLineObstacle(new DPoint(-20, 8), new DPoint(-2, -8),200, -3, lineStreamCoefficient, lineStreamPower);
        pathFinder.addAugment(basicLine);
        display.displayables.add(basicLine);
        // */ //

         /* // BallDirectionFollower
        VelocityController velocityController = new VelocityController(2, null, .3,.05, .01, 0);
        BallDirectionFollower ballFollower = new BallDirectionFollower(pathFinder.getController(), new DPoint(20, 1));
        ballFollower.velocityController = velocityController;
        display.displayables.add(ballFollower);
        // */ // End BallDirectionFollower

        // /* Display Of PathFinder
        // X: (-55, 35) Y: (-18, 18)
//        Extrema preCompExtrema = new Extrema(new DPoint(-55, -20), new DPoint(35, 20));
//        Space<DVector> space = pathFinder.getPrecomputedField(preCompExtrema, .1);

//        DVector velocity = new DVector(2);
//        display.onGridDisplayables.add(gridPoint -> {
//           if (!space.isOutOfBounds(gridPoint)) {
//               return new DPosVector(gridPoint.clone(), space.get(gridPoint.clone()).clone());
//           } else {
//               return new DPosVector(gridPoint.clone(), new DVector(space.getDimensions()));
//           }
//            return new DPosVector(gridPoint.clone(), pathFinder.getDirection(gridPoint.clone(), velocity));
//        });
        // */ // End Display Of PathFinder

//        lines.start();
//        ballFollower0.start();
        ballFollower1.start();

        display.display();

        while (true) {
            display.repaint();
            try {
                Thread.sleep(5);
            } catch (Exception e) {

            }
        }
    }

}
