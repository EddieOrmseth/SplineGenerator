package SplineGenerator;

import SplineGenerator.Applied.*;
import SplineGenerator.Applied.LegacyVersions.OldVelocityController;
import SplineGenerator.GUI.BallMotionControllerFollower;
import SplineGenerator.GUI.Display;
import SplineGenerator.GUI.KeyBoardListener;
import SplineGenerator.Splines.PolynomicSpline;
import SplineGenerator.Splines.Spline;
import SplineGenerator.Util.*;
import SplineGenerator.Util.PathAugments.StandardCircleObstacle;
import SplineGenerator.Util.PathAugments.StandardPointTarget;

import java.awt.*;
import java.awt.event.KeyEvent;

public class FRCPathFinderAutoPick {

    public static void main(String... args) {

        KeyBoardListener.initialize();

        DPoint lesserPoint = new DPoint(-8, -8);
        DPoint greaterPoint = new DPoint(1, 1);
        Display display = new Display(2, new Extrema(lesserPoint, greaterPoint), 0, 1, 1600, 700);

//        DVector targetPosition = new DVector(-6.634, -2.606); // Actual
        DVector targetPosition = new DVector(-5.96, -2.2); // Test
        DVector pickPosition = new DVector(-7.061, -2.8);
        display.displayables.add((graphics) -> graphics.paintPoint(targetPosition.clone(), 0, 1, new Color(0, 0, 255)));

        PathFinder toStartingPoint = new PathFinder(2);
        toStartingPoint.setTarget(new StandardPointTarget(2, targetPosition, 1, 1));

        DPoint hubPosition = new DPoint(0, 0);
        StandardCircleObstacle hub = new StandardCircleObstacle(2, hubPosition, 1.87082869339, 1, -.5);
        toStartingPoint.addAugment(hub);
        display.displayables.add(hub);

        PathFinder.Controller positionController = toStartingPoint.getController();
        positionController.setFinishedThreshold(.25);

        ConstantVelocityController basicVelocityController = new ConstantVelocityController(3);

        MotionController firstPart = new MotionController(positionController, basicVelocityController, positionController::getPosition);




        PolynomicSpline pickSpline = new PolynomicSpline(2);

        pickSpline.setPolynomicOrder(3);
        pickSpline.closed = false;

        pickSpline.addControlPoint(new DControlPoint(targetPosition, new DVector(-1, -.4)));
        pickSpline.addControlPoint(new DControlPoint(pickPosition, new DVector(-1, -.4)));

        InterpolationInfo c1 = new InterpolationInfo();
        c1.interpolationType = Spline.InterpolationType.Hermite;
        c1.endBehavior = Spline.EndBehavior.Hermite;
        pickSpline.interpolationTypes.add(c1);

        InterpolationInfo c2 = new InterpolationInfo();
        c2.interpolationType = Spline.InterpolationType.Linked;
        c2.endBehavior = Spline.EndBehavior.None;
        pickSpline.interpolationTypes.add(c2);

        pickSpline.generate();
        pickSpline.takeNextDerivative();

        display.displayables.add((graphics) -> graphics.drawSpline(pickSpline, .001));

        Function<DVector, DVector> derivativeModifier = variable -> {
            variable.setMagnitude(10);
            return variable;
        };

        Function<DVector, DVector> distanceModifier = variable -> {
            variable.multiplyAll(35);
            return variable;
        };

        StepController navigator = new StepController(pickSpline, derivativeModifier, distanceModifier, .02, .1);
        StepController.Controller pickSplineController = navigator.getController();

        SplineVelocityController pickController = new SplineVelocityController(pickSpline, pickSplineController::getTValue, 1, 1, 0, 0, 0);
        pickController.addStopToEnd(10, .01);

        MotionController secondPart = new MotionController(pickSplineController, pickController, pickSplineController::getPosition);



        PolynomicSpline returnSpline = new PolynomicSpline(2);

        returnSpline.setPolynomicOrder(3);
        returnSpline.closed = false;

        returnSpline.addControlPoint(new DControlPoint(pickPosition, new DVector(1, 1)));
        returnSpline.addControlPoint(new DControlPoint(new DVector(-2.65, -1.58), new DVector(1, 1)));

        InterpolationInfo c1r = new InterpolationInfo();
        c1r.interpolationType = Spline.InterpolationType.Hermite;
        c1r.endBehavior = Spline.EndBehavior.Hermite;
        returnSpline.interpolationTypes.add(c1r);

        InterpolationInfo c2r = new InterpolationInfo();
        c2r.interpolationType = Spline.InterpolationType.Linked;
        c2r.endBehavior = Spline.EndBehavior.None;
        returnSpline.interpolationTypes.add(c2r);

        returnSpline.generate();
        returnSpline.takeNextDerivative();

        display.displayables.add((graphics) -> graphics.drawSpline(returnSpline, .001));

        StepController returnNavigator = new StepController(returnSpline, derivativeModifier, distanceModifier, .02, .1);
        StepController.Controller returnSplineController = returnNavigator.getController();

        SplineVelocityController returnVelocityController = new SplineVelocityController(returnSpline, returnSplineController::getTValue, 3, 1.5, 0, .2, .2);
        returnVelocityController.addStopToEnd(10, .01);

        MotionController thirdPart = new MotionController(returnSplineController, returnVelocityController, returnSplineController::getPosition);



        SequentialMCMotionController superController = new SequentialMCMotionController(firstPart, secondPart, thirdPart);

//        DPoint initialBallPosition = new DPoint(0, 0);
//        BallVelocityDirectionController ball = new BallVelocityDirectionController(positionController, initialBallPosition);
//        ball.velocityController = basicVelocityController;
        BallMotionControllerFollower ball = new BallMotionControllerFollower(superController, new DPoint(0, 0));

        display.displayables.add(ball);

        ball.start();

        display.display();

//        boolean firstSection = true;
//        boolean secondSection = false;
//        boolean thirdSection = false;

        while (true) {
            display.repaint();
            if (KeyBoardListener.get(KeyEvent.VK_SPACE)) {
                superController.reset();
            }
        }


    }

}
