package SplineGenerator.GUI;

import SplineGenerator.Applied.FollowerGradient;
import SplineGenerator.Splines.Spline;
import SplineGenerator.Util.*;

import java.awt.*;
import java.util.ArrayList;

/**
 * A simple class for displaying a spline
 */
public class SplineDisplay extends Display {

    /**
     * The spline to be displayed
     */
    private Spline spline;

    /**
     * The amount to step by when creating the spline out of tons of line segments
     */
    private double pointOnSplineStep = .001;

    /**
     * The color to color the splines when (int) t % 3 == 0
     */
    private Color c1 = new Color(255, 0, 0);

    /**
     * The color to color the splines when (int) t % 3 == 1
     */
    private Color c2 = new Color(0, 0, 255);

    /**
     * The color to color the splines when (int) t % 3 == 2
     */
    private Color c3 = new Color(0, 255, 0);

    /**
     * The amount to step by when stepping on the spline, int t values
     */
    private double onSplineStep = .1;

    /**
     * The displayables that are dependant on the t-value of the spline
     */
    public ArrayList<Function<Double, Displayable>> onSplineDisplayables;

    /**
     * A constructor for the SplineDisplay
     *
     * @param spline The spline to be displayed
     */
    public SplineDisplay(Spline spline, int xDim, int yDim, int width, int height) {
        super(spline.getDimensions(), null, xDim, yDim, width, height);
        this.spline = spline;
        onSplineDisplayables = new ArrayList<>();
        setTitle("Spline Display");
    }

    /**
     * A method for painting the display
     *
     * @param graphics The object upon which to paint
     */
    @Override
    public void paint(Graphics graphics) {
        image.getGraphics().clearRect(0, 0, image.getWidth(), image.getHeight());
        drawAxis();
        drawSpline();

        paintOnGrid();
        paintOnSpline();
        paintDisplayables();

        graphics.drawImage(image, windowWidthOffset, windowHeightOffset, this);
    }

    /**
     * A method for drawing the spline on the image object
     */
    public void drawSpline() {
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        graphics.setStroke(new BasicStroke(2));
        boolean setP2 = true;
        DPoint p1 = translate(spline.get(0)), p2 = spline.get(0);
        int tVal;

        for (double t = pointOnSplineStep; t < spline.pieces; t += pointOnSplineStep) {
            tVal = (int) t % 3;
            if (tVal == 0) {
                graphics.setColor(c1);
            } else if (tVal == 1) {
                graphics.setColor(c2);
            } else {
                graphics.setColor(c3);
            }

            if (setP2) {
                p2 = translate(spline.get(t));
                graphics.drawLine((int) p1.get(xDim), (int) p1.get(yDim), (int) p2.get(xDim), (int) p2.get(yDim));
            } else {
                p1 = translate(spline.get(t));
                graphics.drawLine((int) p2.get(xDim), (int) p2.get(yDim), (int) p1.get(xDim), (int) p1.get(yDim));
            }

            setP2 = !setP2;
        }
    }

    /**
     * A method for displaying the onSplineDisplayables
     */
    public void paintOnSpline() {
        for (double t = onSplineStep / 2.0; t < spline.pieces; t += onSplineStep) {
            for (int d = 0; d < onSplineDisplayables.size(); d++) {
                onSplineDisplayables.get(d).get(t).display(graphics);
            }
        }
    }

    /**
     * A method for setting the translation values, namely xOffset, yOffset and scalar
     */
    @Override
    public void setTranslationValues() {
        boundingBox = etBoundingBox();
        double xScalar = (image.getWidth() * (1 - 2 * percentBorder)) / (boundingBox.greaterPoint.get(xDim) - boundingBox.lesserPoint.get(xDim));
        double yScalar = (image.getHeight() * (1 - 2 * percentBorder)) / (boundingBox.greaterPoint.get(yDim) - boundingBox.lesserPoint.get(yDim));

        scalar = Math.min(xScalar, yScalar);
        scaledBoundingBox = boundingBox.clone();
        scaledBoundingBox.multiplyAll(scalar);

        xOffset = (int) ((image.getWidth() / 2) - ((scaledBoundingBox.greaterPoint.get(xDim) + scaledBoundingBox.lesserPoint.get(xDim)) / 2));
        yOffset = (int) ((image.getHeight() / 2) - ((scaledBoundingBox.greaterPoint.get(yDim) + scaledBoundingBox.lesserPoint.get(yDim)) / 2));
    }

    /**
     * A method for getting and setting the boundingBox object
     *
     * @return The BoundingBox object that the spline resides inside
     */
    public Extrema etBoundingBox() {
        Extrema box = new Extrema(spline.getDimensions());
        DPoint point;

        for (double t = 0; t < spline.pieces; t += pointOnSplineStep) {
            point = spline.get(t);

            if (point.get(xDim) < box.lesserPoint.get(xDim)) {
                box.lesserPoint.set(xDim, point.get(xDim));
            } else if (point.get(xDim) > box.greaterPoint.get(xDim)) {
                box.greaterPoint.set(xDim, point.get(xDim));
            }

            if (point.get(yDim) < box.lesserPoint.get(yDim)) {
                box.lesserPoint.set(yDim, point.get(yDim));
            } else if (point.get(yDim) > box.greaterPoint.get(yDim)) {
                box.greaterPoint.set(yDim, point.get(yDim));
            }
        }

        return box;
    }

    /**
     * A method for painting the derivative at a point
     *
     * @param t          The t value to evaluate the derivative at
     * @param derivative The derivative to use
     */
    public void paintDerivative(double t, int derivative) {
        DPoint startPoint = translate(spline.get(t));
        graphics.paintVector(startPoint, spline.evaluateDerivative(t, derivative).toDirection(), xDim, yDim);
        graphics.paintPoint(startPoint, xDim, yDim);
    }

    /**
     * A method for displaying a FollowerGradient
     *
     * @param follower The FollowerGradient to display
     */
    public void displayGradient(FollowerGradient follower) {
        onGridDisplayables.add(gridPoint -> {
            DVector followerPoint = follower.getDirection(gridPoint.clone());
            return new DPosVector(gridPoint, followerPoint);
        });
    }

}