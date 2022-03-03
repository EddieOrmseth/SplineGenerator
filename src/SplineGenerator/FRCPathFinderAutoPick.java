package SplineGenerator;

import SplineGenerator.Applied.LegacyVersions.OldVelocityController;
import SplineGenerator.Applied.PathFinder;
import SplineGenerator.GUI.BallVelocityDirectionController;
import SplineGenerator.GUI.Display;
import SplineGenerator.GUI.KeyBoardListener;
import SplineGenerator.Util.DPoint;
import SplineGenerator.Util.DVector;
import SplineGenerator.Util.Extrema;
import SplineGenerator.Util.PathAugments.StandardCircleObstacle;
import SplineGenerator.Util.PathAugments.StandardPointTarget;
import SplineGenerator.Util.PathAugments.StreamCircleObstacle;

import java.awt.*;

public class FRCPathFinderAutoPick {

    public static void main(String... args) {

        KeyBoardListener.initialize();

        DPoint lesserPoint = new DPoint(-10, -10);
        DPoint greaterPoint = new DPoint(5, 5);
        Display display = new Display(2, new Extrema(lesserPoint, greaterPoint), 0, 1, 1600, 700);

        DVector targetPosition = new DVector(-6.66, -2.58);
        display.displayables.add((graphics) -> graphics.paintPoint(targetPosition.clone(), 0, 1, new Color(0, 0, 255)));

        PathFinder toStartingPoint = new PathFinder(2);
        toStartingPoint.setTarget(new StandardPointTarget(2, targetPosition, 1, 1));

//        double circleStreamCoefficient = 0;
//        double circleStreamPower = 0;
        DPoint hubPosition = new DPoint(0, 0);
////        StreamCircleObstacle hub = new StreamCircleObstacle(2, hubPosition, 3.5, 200, -3, circleStreamCoefficient, circleStreamPower);
//        StreamCircleObstacle hub = new StreamCircleObstacle(2, hubPosition, 3.5, 10, 0, 0, 0);
//        toStartingPoint.addAugment(hub);
//        display.displayables.add(hub);

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

        DPoint initialBallPosition = new DPoint(0, 0);
        BallVelocityDirectionController ball = new BallVelocityDirectionController(positionController, initialBallPosition);
        ball.velocityController = basicVelocityController;

        display.displayables.add(ball);

        ball.start();

        display.display();

        while (true) {
            display.repaint();
            if (positionController.isFinished()) {
                System.out.println("We are Finished! Congratulations!");
            }
        }


    }

}
