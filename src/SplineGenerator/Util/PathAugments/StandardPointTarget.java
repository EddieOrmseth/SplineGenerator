package SplineGenerator.Util.PathAugments;

import SplineGenerator.Applied.PathAugment;
import SplineGenerator.GUI.Displayable;
import SplineGenerator.GUI.SplineGraphics;
import SplineGenerator.Util.DPoint;
import SplineGenerator.Util.DVector;

import java.awt.*;

/**
 * A class representing a simple point target
 */
public class StandardPointTarget extends PathAugment implements Displayable {

    /**
     * The point for the target to draw to
     */
    private DPoint targetPosition;

    /**
     * A simple constructor that requires the position of the target
     *
     * @param targetPosition The position of the target
     * @param strength The strength of the pull towards the target
     */
    public StandardPointTarget(DPoint targetPosition, double strength) {
        this.targetPosition = targetPosition;
        setSkipAugment((toTarget, point, velocity) -> false);
        setGetVectorBetween(objectPoint -> new DVector(targetPosition, objectPoint));
        setGetEffect((vectorBetween, toTarget, position, velocity) -> {
            DVector effect = vectorBetween.clone();
            effect.setMagnitude(-strength);
            return effect;
        });
        setSkipEffect((vectorBetween, toTarget, effect, position, velocity) -> false);
    }

    /**
     * A method for setting the targetPosition;
     *
     * @param targetPosition The new targetPosition;
     */
    public void setPosition(DPoint targetPosition) {
        this.targetPosition.copy(targetPosition);
    }

    /**
     * A method for displaying the target on the screen
     *
     * @param graphics The object to display on
     */
    @Override
    public void display(SplineGraphics graphics) {
        graphics.paintPoint(targetPosition.clone(), 0, 1, new Color(0, 0, 255), 14);
    }
}
