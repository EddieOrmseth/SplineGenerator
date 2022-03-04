package SplineGenerator;

import SplineGenerator.Applied.*;
import SplineGenerator.GUI.*;
import SplineGenerator.Splines.PolynomicSpline;
import SplineGenerator.Splines.Spline;
import SplineGenerator.Util.*;

import java.awt.*;

public class FRC2022RealStartingBallPath {

    public static void main(String... args) {

        KeyBoardListener.initialize();

        PolynomicSpline firstPieceSpline = new PolynomicSpline(2);
        firstPieceSpline.setPolynomicOrder(3);
        firstPieceSpline.closed = false;

        DVector initialPositionP1 = new DVector(0.42, -2.405);

        firstPieceSpline.addControlPoint(new DControlPoint(initialPositionP1, new DVector(0, -1)));
        firstPieceSpline.addControlPoint(new DControlPoint(new DVector(.42, -2.98), new DVector(0, -1)));

        // Set Interpolation Info
        InterpolationInfo c1P1 = new InterpolationInfo();
        c1P1.interpolationType = Spline.InterpolationType.Linked;
        c1P1.endBehavior = Spline.EndBehavior.Hermite;
        firstPieceSpline.interpolationTypes.add(c1P1);

        InterpolationInfo c2P1 = new InterpolationInfo();
        c2P1.interpolationType = Spline.InterpolationType.Linked;
        c2P1.endBehavior = Spline.EndBehavior.None;
        firstPieceSpline.interpolationTypes.add(c2P1);

        // Create Necessary Equations
        firstPieceSpline.generate();
        firstPieceSpline.takeNextDerivative();

        System.out.println("Spline: \n" + firstPieceSpline.getDesmosEquations());

        // Define How We Move on the Spline
        Function<DVector, DVector> derivativeModifierP1 = variable -> {
            variable.setMagnitude(10);
            return variable;
        };

        Function<DVector, DVector> distanceModifierP1 = variable -> {
            variable.multiplyAll(40);
            return variable;
        };

        // Create the Position Controller
        StepController navigatorP1 = new StepController(firstPieceSpline, derivativeModifierP1, distanceModifierP1, .02, .05);
        StepController.Controller stepControllerP1 = navigatorP1.getController();

        // Create the Velocity Controller
//        SplineVelocityController velocityController = new SplineVelocityController(spline, stepController::getTValue, 2, 1, 0, .2, .2);
//        SplineVelocityController velocityControllerP1 = new SplineVelocityController(firstPieceSpline, stepControllerP1::getTValue, 4, 2, 0, .6, 0);
//        velocityControllerP1.addStopToEnd(2.5, .01);

        ConstantVelocityController velocityControllerP1 = new ConstantVelocityController(2);

        // Create the Motion Controller
        MotionController motionControllerP1 = new MotionController(stepControllerP1, velocityControllerP1, stepControllerP1::getPosition);







        PolynomicSpline secondPieceSpline = new PolynomicSpline(2);
        secondPieceSpline.setPolynomicOrder(5);
        secondPieceSpline.closed = false;

        DVector initialPositionP2 = new DVector(0.42, -2.98);

        secondPieceSpline.addControlPoint(new DControlPoint(initialPositionP2, new DDirection(-1, -1), new DDirection(0, 0)));
        secondPieceSpline.addControlPoint(new DControlPoint(new DVector( -0.65, -3.77)/*, new DDirection(-1, 1)*/));
        secondPieceSpline.addControlPoint(new DControlPoint(new DVector(-3.192, -2.188)/*, new DDirection(1.5, 1.5)*/));
        secondPieceSpline.addControlPoint(new DControlPoint(new DVector(-2.65, -1.58), new DDirection(1, 1), new DDirection(0, 0)));

        // Set Interpolation Info
        InterpolationInfo c1P2 = new InterpolationInfo();
        c1P2.interpolationType = Spline.InterpolationType.Linked;
        c1P2.endBehavior = Spline.EndBehavior.Hermite;
        secondPieceSpline.interpolationTypes.add(c1P2);

        InterpolationInfo c2P2 = new InterpolationInfo();
        c2P2.interpolationType = Spline.InterpolationType.Linked;
        c2P2.endBehavior = Spline.EndBehavior.Hermite;
        secondPieceSpline.interpolationTypes.add(c2P2);

        InterpolationInfo c3P2 = new InterpolationInfo();
        c3P2.interpolationType = Spline.InterpolationType.Linked;
        c3P2.endBehavior = Spline.EndBehavior.None;
        secondPieceSpline.interpolationTypes.add(c3P2);

        InterpolationInfo c4P2 = new InterpolationInfo();
        c4P2.interpolationType = Spline.InterpolationType.Linked;
        c4P2.endBehavior = Spline.EndBehavior.None;
        secondPieceSpline.interpolationTypes.add(c4P2);

        // Create Necessary Equations
        secondPieceSpline.generate();
        secondPieceSpline.takeNextDerivative();

        System.out.println("Spline: \n" + secondPieceSpline.getDesmosEquations());

        // Define How We Move on the Spline
        Function<DVector, DVector> derivativeModifierP2 = variable -> {
            variable.setMagnitude(10);
            return variable;
        };

        Function<DVector, DVector> distanceModifierP2 = variable -> {
            variable.multiplyAll(40);
            return variable;
        };

        // Create the Position Controller
        StepController navigatorP2 = new StepController(secondPieceSpline, derivativeModifierP2, distanceModifierP2, .02, .05);
        StepController.Controller stepControllerP2 = navigatorP2.getController();

        // Create the Velocity Controller
//        SplineVelocityController velocityController = new SplineVelocityController(spline, stepController::getTValue, 2, 1, 0, .2, .2);
        SplineVelocityController velocityControllerP2 = new SplineVelocityController(secondPieceSpline, stepControllerP2::getTValue, 4, 2, 0, .6, 0);
        velocityControllerP2.addStopToEnd(2.5, .01);

        // Create the Motion Controller
        MotionController motionControllerP2 = new MotionController(stepControllerP2, velocityControllerP2, stepControllerP2::getPosition);


        SequentialMCMotionController motionController = new SequentialMCMotionController(motionControllerP1, motionControllerP2);


        DPoint greaterPoint = new DPoint(1, 2);
        DPoint lesserPoint = new DPoint(-8, -8);
        Display display = new Display(2, new Extrema(lesserPoint, greaterPoint), 0, 1, 1600, 700);
        display.displayables.add((graphics) -> graphics.drawSpline(firstPieceSpline, .001, new Color(0, 255, 0), null, null));
        display.displayables.add((graphics) -> graphics.drawSpline(secondPieceSpline, .001));

        BallMotionControllerFollower ball = new BallMotionControllerFollower(motionController, new DPoint(0, 0));
        ball.setPosition(initialPositionP1);

        display.displayables.add(ball);
        ball.start();

        display.display();

        while (true) {
            display.repaint();
        }

    }

}