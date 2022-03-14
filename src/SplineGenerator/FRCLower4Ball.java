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

public class FRCLower4Ball {

    public static void main(String... args) {

        KeyBoardListener.initialize();

        PolynomicSpline spline = new PolynomicSpline(2);
        spline.setPolynomicOrder(5);
        spline.closed = false;

        DVector initialPosition = new DVector(0.39, -2.403);

        spline.addControlPoint(new DControlPoint(initialPosition, new DVector(0, -1), new DDirection(0, 0)));
        spline.addControlPoint(new DControlPoint(new DVector(.38, -3.42)/*, new DDirection(1.5, 1.5)*/));
        spline.addControlPoint(new DControlPoint(new DVector(-0.7, -3.58)));
        spline.addControlPoint(new DControlPoint(new DVector(-2.01, -2.9), new DVector(-1, .2), new DDirection(0, 0)));

//        DVector initialPosition = new DVector(-.673, -3.757);
//
//        spline.addControlPoint(new DControlPoint(initialPosition, new DVector(-1, 1), new DDirection(0, 0)));
//        spline.addControlPoint(new DControlPoint(new DVector(-3.53, -1.97), new DVector(-1, 1), new DDirection(0, 0)));


        // Set Interpolation Info
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

//        DPoint point = spline.get(1.98);
        display.displayables.add(stepController);

        ball.start();
        display.display();

        while (true) {
            display.repaint();
        }

    }

}
