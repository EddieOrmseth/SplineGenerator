package SplineGenerator.Applied;

import SplineGenerator.Util.*;

import java.util.ArrayList;

/**
 * A class for finding a path around objects to a certain destination
 */
public class PathFinderV1 implements Navigator {

    /**
     * A list of the modifiers that determine the paths to be created
     */
    private ArrayList<Function<DPoint, DVector>> modifiers;

    /**
     * A Space object for holding the data
     */
    private Space<DDirection> space;

    public PathFinderV1(Extrema bounds, double spaceStep) {
        modifiers = new ArrayList<>();
        space = new Space<>(bounds, spaceStep);
    }

    /**
     * A method for evaluating the modifiers at the specified point
     *
     * @param point The point at which to evaluate the modifiers
     * @return The value of all the modifiers at the specified point
     */
    public DDirection evaluateModifiers(DPoint point) {
        DVector vector = new DVector(space.getDimensions());
        for (int i = 0; i < modifiers.size(); i++) {
            vector.add(modifiers.get(i).get(point));
        }

        return vector.toDirection();
    }

    /**
     * A method for getting the computed direction at a certain point
     *
     * @param point The point at which to get the computed direction
     * @return The direction at the specified point
     */
    public DDirection getDirection(DPoint point) {
        return space.get(point);
    }

    /**
     * A method for adding a modifier
     *
     * @param modifier The modifier to be added
     */
    public void addModifier(Function<DPoint, DVector> modifier) {
        modifiers.add(modifier);
    }

    /**
     * A method for removing a modifier
     *
     * @param modifier The modifier to be removed
     */
    public void removeModifier(Function<DPoint, DVector> modifier) {
        modifiers.remove(modifier);
    }

    /**
     * A method for computing the motion caused by the modifiers
     */
    public void compute() {
        for (int i = 0; i < space.size(); i++) {
            space.set(i, evaluateModifiers(space.indexToPoint(i)));
        }
    }

    /**
     * A method for getting the number of dimensions of the PathFinder
     *
     * @return The number of dimensions of the PathFinder
     */
    public int getDimensions() {
        return space.getDimensions();
    }

//
//    /**
//     * A method for getting the direction from a position and velocity;
//     *
//     * @param position The current position of the object
//     * @param velocity The current velocity of the object
//     * @return The direction created
//     */
//    public DDirection getMovement(DPoint position, DVector velocity) {
//
//        DVector finalVector = new DVector(position.getDimensions());
//        DVector vectorBetween;
//
//        for (int i = 0; i < modifiers.size(); i++) {
//            vectorBetween = new DVector()
//
//
//        }
//
//        return finalVector.toDirection();
//    }

    /**
     * A class for holding objects in the way of the PathFinder
     */
    public static class Obstacle implements Function<DPoint, DVector> {

        /**
         * A Function for getting the Vector from the object to the point
         */
        public Function<DPoint, DVector> distance;

        /**
         * A Function for getting the strength of the vector
         */
        public Function<DVector, DVector> strength;

        /**
         * A simple constructor that does nothing
         */
        public Obstacle() {

        }

        /**
         * A constructor that requires the two functions needed to determine movement
         *
         * @param distance The Function that determines the vector between the given Point and the Obstacle
         * @param strength The Function that determines the strength of the vector
         */
        public Obstacle(Function<DPoint, DVector> distance, Function<DVector, DVector> strength) {
            this.distance = distance;
            this.strength = strength;
        }

        /**
         * A method for getting the movement vector caused by the obstacle at a certain point
         *
         * @param point The point at which to get the movement vector
         * @return The movement vector at the specified point
         */
        @Override
        public DVector get(DPoint point) {
            DVector vector = distance.get(point);
            vector = strength.get(vector);

            return vector;
        }
    }

    /**
     * A method for getting a function that determines the distance from a point
     *
     * @param point The point from which to get the distance
     * @return The Function that can be used to find the distance
     */
    public static Function<DPoint, DVector> getPointDistanceFunction(DPoint point) {
        return variable -> new DVector(point, variable);
    }

    /**
     * A method for getting a function that determines the distance from a point
     *
     * @param point  The point at which to base the circle
     * @param radius The radius of the circle
     * @return The Function that can be used to find the distance
     */
    public static Function<DPoint, DVector> getCircleDistanceFunction(DPoint point, double radius) {
        return variable -> {
            DVector vector = new DVector(point, variable);
            if (point.getDistance(variable) > radius) {
                vector.setMagnitude(vector.getMagnitude() - radius);
            }

            return vector;
        };
    }

    /**
     * A method for getting a function that determines the distance from a point
     *
     * @param p1 The first point of the line segment (must be in the format (x, y))
     * @param p2 The second point of the line segment (must be in the format (x, y))
     * @return The Function that can be used to find the distance
     */
    public static Function<DPoint, DVector> getLineSegment2DDistanceFunction(DPoint p1, DPoint p2) {

        double x1 = p1.get(0), y1 = p1.get(1);
        double x2 = p2.get(0), y2 = p2.get(1);

        if (x1 == x2 && y1 == y2) {
//                return Math.sqrt(((x - x1) * (x - x2)) + ((y - y1) * (y - y2)));
            return getPointDistanceFunction(p1);
        }

        return variable -> {

            double x = variable.get(0), y = variable.get(1);

            if (x1 == x2) {
                double tempY1 = Math.min(y1, y2), tempY2 = Math.max(y1, y2);

                if (y < tempY1) {
//                    return Math.sqrt(((x1 - x) * (x1 - x)) + ((tempY1 - y) * (tempY1 - y)));
                    return new DVector(x1 - x, tempY1 - y);
                } else if (y > tempY2) {
//                    return Math.sqrt(((x2 - x) * (x2 - x)) + ((tempY2 - y) * (tempY2 - y)));
                    return new DVector(x2 - x, tempY2 - y);
                } else {
//                    return Math.abs(x - x1);
                    return new DVector(x1 - x, 0);
                }
            } else if (y1 == y2) {
                double tempX1 = Math.min(x1, x2), tempX2 = Math.max(x1, x2);

                if (x < tempX1) {
//                    return Math.sqrt(((tempX1 - x) * (tempX1 - x)) + ((y1 - y) * (y1 - y)));
                    return new DVector(tempX1 - x, y1 - y);
                } else if (x > tempX2) {
//                    return Math.sqrt(((tempX2 - x) * (tempX2 - x)) + ((y2 - y) * (y2 - y)));
                    return new DVector(tempX2 - x, y2 - y);
                } else {
//                    return Math.abs(y - y1);
                    return new DVector(0, y1 - y);
                }
            }

            double slope = (y2 - y1) / (x2 - x1);
            double yInt = y1 - (slope * x1);

            double perpSlope = -1 * (1 / slope);

            double yInt1 = y1 - (perpSlope * x1);
            double yInt2 = y2 - (perpSlope * x2);

            double tempV1 = yInt1 + (x * perpSlope);
            double tempV2 = yInt2 + (x * perpSlope);

            double v1 = Math.min(tempV1, tempV2);
            double v2 = Math.max(tempV1, tempV2);

            if (y > v1 && y < v2) { // Between Lines

                double perpYIntercept = y - (perpSlope * x);

                double intersectX = (perpYIntercept - yInt) / (slope - perpSlope);
                double intersectY = intersectX * slope + yInt;

//                return Math.sqrt(((intersectX - x) * (intersectX - x)) + ((intersectY - y) * (intersectY - y)));
                return new DVector(x - intersectX, y - intersectY);
            } else { // Not Between Lines
                double d1 = Math.sqrt(((x - x1) * (x - x1)) + ((y - y1) * (y - y1)));
                double d2 = Math.sqrt(((x - x2) * (x - x2)) + ((y - y2) * (y - y2)));
//                return Math.min(d1, d2);
                if (d1 < d2) {
                    return new DVector(x - x1, y - y1);
                } else {
                    return new DVector(x - x2, y - y2);
                }
            }

        };

    }

    /**
     * A method for getting a controller associated with this object
     *
     * @return The controller for the PathFinder
     */
    @Override
    public Controller getController() {
        return new Controller(this);
    }

    /**
     * A Controller that provides directions for an object
     */
    public class Controller extends Navigator.Controller {

        /**
         * The PathFinder object to be followed
         */
        private PathFinderV1 pathFinder;

        /**
         * The position of the controlled object
         */
        private DPoint position;

        /**
         * The previous position of the controlled object
         */
        private DPoint previousPosition;

        /**
         * The velocity of the controlled object
         */
        public DVector velocity;

        /**
         * A simple constructor for a controller that follows the pathfinder
         *
         * @param pathFinder
         */
        public Controller(PathFinderV1 pathFinder) {
            this.pathFinder = pathFinder;
            position = new DPoint(pathFinder.getDimensions());
            previousPosition = new DPoint(pathFinder.getDimensions());
            velocity = new DVector(pathFinder.getDimensions());
        }

        /**
         * A method for setting the position of the controller
         *
         * @param point The new position
         */
        @Override
        public void update(DPoint point) {
            previousPosition = position.clone();
            position = point;
            velocity = new DVector(previousPosition, position);
        }

        /**
         * A method for getting the DDirection computed by the PathFinder
         *
         * @return The DDirection computed by the PathFinder
         */
        @Override
        public DDirection getDirection() {
//            return pathFinder.getDirection(position);

            return pathFinder.evaluateModifiers(position);
        }
    }

}
