package SplineGenerator;

import SplineGenerator.Applied.SplineVelocityController;
import SplineGenerator.Applied.StepController;
import SplineGenerator.GUI.BallVelocityDirectionController;
import SplineGenerator.GUI.KeyBoardListener;
import SplineGenerator.GUI.SplineDisplay;
import SplineGenerator.Splines.PolynomicSpline;
import SplineGenerator.Splines.Spline;
import SplineGenerator.Util.*;

public class RobotFigure8Replica {

    public static void main(String... args) {

        KeyBoardListener.initialize();

        PolynomicSpline spline = new PolynomicSpline(2);
        spline.setPolynomicOrder(5);
        spline.closed = false;
        double i = 1;

        // Define Path
        spline.addControlPoint(new DControlPoint(new DVector(0, 0), new DDirection(1, 1), new DDirection(0, 0)));
        spline.addControlPoint(new DControlPoint(new DVector(i, i), new DDirection(1, 0)));
        spline.addControlPoint(new DControlPoint(new DVector(2 * i, 0), new DDirection(1, 0)));
        spline.addControlPoint(new DControlPoint(new DVector(i, -i), new DDirection(1, 0)));
        spline.addControlPoint(new DControlPoint(new DVector(0, 0), new DDirection(1, 0)));
        spline.addControlPoint(new DControlPoint(new DVector(-i, i), new DDirection(1, 0)));
        spline.addControlPoint(new DControlPoint(new DVector(-2 * i, 0), new DDirection(1, 0)));
        spline.addControlPoint(new DControlPoint(new DVector(-i, -i), new DDirection(1, 0)));
        spline.addControlPoint(new DControlPoint(new DVector(0, 0), new DDirection(0, 0), new DDirection(0, 0)));

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

        // Define How We Move on the Spline
        Function<DVector, DVector> derivativeModifier = variable -> {
            variable.setMagnitude(10);
            return variable;
        };

        Function<DVector, DVector> distanceModifier = variable -> {
            variable.multiplyAll(35);
            return variable;
        };

        // Create the Position Controller
        StepController navigator = new StepController(spline, derivativeModifier, distanceModifier, .02, .1);
        StepController.Controller stepController = navigator.getController();

        // Create the Velocity Controller
//        SplineVelocityController velocityController = new SplineVelocityController(spline, stepController::getTValue, 2, 1, 0, .2, .2);
//        SplineVelocityController velocityController = new SplineVelocityController(spline, stepController::getTValue, 4.96824, 4.96824, 0, .2, .2);
        SplineVelocityController velocityController = new SplineVelocityController(spline, stepController::getTValue, 1, .5, 0, .2, .2);
        velocityController.addStopToEnd(2.5, .01);

        // Create the Motion Controller
//        MotionController motionController = new MotionController(stepController, velocityController, subsystems.getSwerveDrive()::getPoint);

        SplineDisplay display = new SplineDisplay(spline, 0, 1, 1600, 700);

        BallVelocityDirectionController ball = new BallVelocityDirectionController(stepController, new DPoint(0, 0));
        ball.velocityController = velocityController;

        display.displayables.add(ball);

        DVector theta0Vector = new DVector(1, 0);
        DVector currentVector = new DVector(2);
        display.displayables.add((input) -> {
            DVector deriv = spline.evaluateDerivative(stepController.getTValue(), 1);

            double theta = deriv.getAngleBetween(theta0Vector);
            currentVector.set(0, Math.cos(theta), Math.sin(theta));

//            input.paintVector(stepController.getPosition().clone(), deriv);
            input.paintVector(stepController.getPosition().clone(), currentVector);
        });

        ball.start();
        display.display();

        while (true) {
            display.repaint();
        }

    }

}
