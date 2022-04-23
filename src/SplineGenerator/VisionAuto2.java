package SplineGenerator;

import SplineGenerator.Applied.MotionController;
import SplineGenerator.Applied.SplineVelocityController;
import SplineGenerator.Applied.StepController;
import SplineGenerator.GUI.BallMotionControllerFollower;
import SplineGenerator.GUI.KeyBoardListener;
import SplineGenerator.GUI.SplineDisplay;
import SplineGenerator.Splines.PolynomicSpline;
import SplineGenerator.Splines.Spline;
import SplineGenerator.Util.*;

public class VisionAuto2 {

    public static void main(String... args) {

        KeyBoardListener.initialize();

        PolynomicSpline spline = new PolynomicSpline(2);
        spline.setPolynomicOrder(3);
        spline.closed = false;

        DVector initialPosition = new DVector(-.833, -3.695);

        spline.addControlPoint(new DControlPoint(initialPosition, new DVector(-1, 1)));
        spline.addControlPoint(new DControlPoint(new DVector(-3.19, -2.18)));
        spline.addControlPoint(new DControlPoint(new DVector(-7.15, -2.9), new DDirection(-2, -1)));

        // Set Interpolation Info
        // 5th Degree
//        InterpolationInfo c1 = new InterpolationInfo();
//        c1.interpolationType = Spline.InterpolationType.Linked;
//        c1.endBehavior = Spline.EndBehavior.Hermite;
//        spline.interpolationTypes.add(c1);
//
//        InterpolationInfo c2 = new InterpolationInfo();
//        c2.interpolationType = Spline.InterpolationType.Linked;
//        c2.endBehavior = Spline.EndBehavior.Hermite;
//        spline.interpolationTypes.add(c2);
//
//        InterpolationInfo c3 = new InterpolationInfo();
//        c3.interpolationType = Spline.InterpolationType.Linked;
//        c3.endBehavior = Spline.EndBehavior.None;
//        spline.interpolationTypes.add(c3);
//
//        InterpolationInfo c4 = new InterpolationInfo();
//        c4.interpolationType = Spline.InterpolationType.Linked;
//        c4.endBehavior = Spline.EndBehavior.None;
//        spline.interpolationTypes.add(c4);

        // 3rd Degree
        InterpolationInfo c1 = new InterpolationInfo();
        c1.interpolationType = Spline.InterpolationType.Linked;
        c1.endBehavior = Spline.EndBehavior.Hermite;
        spline.interpolationTypes.add(c1);

        InterpolationInfo c2 = new InterpolationInfo();
        c2.interpolationType = Spline.InterpolationType.Linked;
        c2.endBehavior = Spline.EndBehavior.None;
        spline.interpolationTypes.add(c2);


        // Create Necessary Equations
        spline.generate();
        spline.takeNextDerivative();

        System.out.println("Spline: \n" + spline.getDesmosEquations());

        // Define How We Move on the Spline
        Function<DVector, DVector> derivativeModifier = variable -> {
            variable.setMagnitude(10);
            return variable;
        };

        Function<DVector, DVector> distanceModifier = variable -> {
            variable.multiplyAll(40);
            return variable;
        };

        // Create the Position Controller
        StepController navigator = new StepController(spline, derivativeModifier, distanceModifier, .02, .05);
        StepController.Controller stepController = navigator.getController();

        // Create the Velocity Controller
//        SplineVelocityController velocityController = new SplineVelocityController(spline, stepController::getTValue, 2, 1, 0, .2, .2);
        SplineVelocityController velocityController = new SplineVelocityController(spline, stepController::getTValue, 3, 3, 0, .6, 0);
        velocityController.addStopToEnd(5, .01);

        // Create the Motion Controller
        MotionController motionController = new MotionController(stepController, velocityController, stepController::getPosition);

        SplineDisplay display = new SplineDisplay(spline, 0, 1, 1600, 700);

        BallMotionControllerFollower ball = new BallMotionControllerFollower(motionController, initialPosition);
        ball.velocityController = velocityController;
        ball.setPosition(initialPosition);

        display.displayables.add(ball);

        DPoint point = spline.get(1.95);
        System.out.println(point);
        display.displayables.add(point.clone());
        display.displayables.add(stepController);

        ball.start();
        display.display();

        while (true) {
            display.repaint();
        }

    }

}
