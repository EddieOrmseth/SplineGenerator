package SplineGenerator;

import SplineGenerator.Applied.LegacyVersions.OldVelocityController;
import SplineGenerator.Applied.PathFinder;
import SplineGenerator.GUI.BallVelocityDirectionController;
import SplineGenerator.GUI.Display;
import SplineGenerator.Util.DPoint;
import SplineGenerator.Util.DVector;
import SplineGenerator.Util.Extrema;
import SplineGenerator.Util.PathAugments.StandardPointTarget;

public class FRCPathFinderAutoPick {

    public static void main(String... args) {

        DVector targetPosition = new DVector(-6.66, -2.58);

        PathFinder toStartingPoint = new PathFinder(2);
        toStartingPoint.setTarget(new StandardPointTarget(2, targetPosition, 1, 1));

        PathFinder.Controller positionController = toStartingPoint.getController();

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
                return 2;
            }

        };

        DPoint lesserPoint = new DPoint(5, 5);
        DPoint greaterPoint = new DPoint(-10, -10);
        Display display = new Display(2, new Extrema(lesserPoint, greaterPoint), 0, 1, 1600, 700);

        display.displayables.add((graphics) -> graphics.paintPoint(positionController.getPosition().clone()));

        DPoint initialBallPosition = new DPoint(0, 0);
        BallVelocityDirectionController ball = new BallVelocityDirectionController(positionController, initialBallPosition);
        ball.velocityController = basicVelocityController;

        ball.start();

        display.display();

        while (true) {
            display.repaint();
        }


    }

}
