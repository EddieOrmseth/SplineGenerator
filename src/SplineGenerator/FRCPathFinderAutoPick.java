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

        DPoint lesserPoint = new DPoint(-10, -10);
        DPoint greaterPoint = new DPoint(5, 5);
        Display display = new Display(2, new Extrema(lesserPoint, greaterPoint), 0, 1, 1600, 700);

        DVector targetPosition = new DVector(-6.66, -2.58);
        DVector pickPosition = new DVector(-7.106, -2.874);
        display.displayables.add((graphics) -> graphics.paintPoint(targetPosition.clone(), 0, 1, new Color(0, 0, 255)));

        PathFinder toStartingPoint = new PathFinder(2);
        toStartingPoint.setTarget(new StandardPointTarget(2, targetPosition, 1, 1));

        DPoint hubPosition = new DPoint(0, 0);
        StandardCircleObstacle hub = new StandardCircleObstacle(2, hubPosition, 3.5, 1, -.5);
        toStartingPoint.addAugment(hub);
        display.displayables.add(hub);

        PathFinder.Controller positionController = toStartingPoint.getController();
        positionController.setFinishedThreshold(.25);

        OldVelocityController basicVelocityController = new OldVelocityController() {

            private double distanceToTarget = 0;
            private double minVel = 0;
            private double maxVel = 0;

            @Override
            public boolean isAccelerating() {
                return false;
            }

            @Override
            public void update() {

            }

            @Override
            public double getVelocity() {
                return 4;
            }

        };

        PolynomicSpline pickSpline = new PolynomicSpline(2);

        pickSpline.setPolynomicOrder(3);
        pickSpline.closed = false;

        pickSpline.addControlPoint(new DControlPoint(targetPosition, new DVector(0, 0)));
        pickSpline.addControlPoint(new DControlPoint(pickPosition, new DVector(0, 0)));

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
            variable.multiplyAll(35.001);
            return variable;
        };

        StepController navigator = new StepController(pickSpline, derivativeModifier, distanceModifier, .02, .1);
        StepController.Controller pickSplineController = navigator.getController();

        SplineVelocityController pickController = new SplineVelocityController(pickSpline, pickSplineController::getTValue, 1, 1, 0, 0, 0);
        pickController.addStopToEnd(10, .01);

        SequentialMCMotionController superController = new SequentialMCMotionController(new MotionController(positionController, basicVelocityController, positionController::getPosition), new MotionController(pickSplineController, pickController, pickSplineController::getPosition));

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
