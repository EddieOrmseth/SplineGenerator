package SplineGenerator.GUI;

import SplineGenerator.Applied.FollowerGradient;
import SplineGenerator.Splines.Spline;
import SplineGenerator.Util.*;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * A simple class for displaying a spline
 */
public class SplineDisplay extends JFrame {

    /**
     * The spline to be displayed
     */
    private Spline spline;

    /**
     * The SplineGraphics object to be used for painting things
     */
    private SplineGraphics graphics;

    /**
     * The index of the dimension of the spline to be displayed on the x-axis
     */
    public int xDim;

    /**
     * The index of the dimension of the spline to be displayed on the y-axis
     */
    public int yDim;

    /**
     * The amount to step by when creating the spline out of tons of line segments
     */
    private double pointOnSplineStep = .001;

    /**
     * The image object that is painted on to screen
     */
    private BufferedImage image;

    /**
     * A BoundingBox that the unscaled spline resides in
     */
    private Extrema boundingBox;

    /**
     * A BoundingBox that the scaled spline resides in
     */
    private Extrema scaledBoundingBox;

    /**
     * The x offset to position the spline and related objects
     */
    private int xOffset;

    /**
     * The y offset to position the spline and related objects
     */
    private int yOffset;

    /**
     * The scalar that is used to scale the spline to the preferred size
     */
    private double scalar;

    /**
     * The percent of the image to leave as border
     */
    private double percentBorder = .1;

    /**
     * The amount to offset the image in the window with respect to the width
     */
    private int windowWidthOffset = 8;

    /**
     * The amount to offset the image in the window with respect to the height
     */
    private int windowHeightOffset = 31;

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
     * The amount to step by when drawing on the grid, in the scale of the spline
     */
    private double onGridStep = .748;

    /**
     * The displayables that are dependant on the t-value of the spline
     */
    public ArrayList<Function<Double, Displayable>> onSplineDisplayables;

    /**
     * The displayables that are dependant on the location in the plane
     */
    public ArrayList<Function<DPoint, Displayable>> onGridDisplayables;

    /**
     * the displayables that are not dependent on any sort of input
     */
    public ArrayList<Displayable> displayables;

    /**
     * An Extrema object for holding the bounds of the grid drawings
     */
    public Extrema onGridBoundaries;

    /**
     * A constructor for the SplineDisplay
     *
     * @param spline The spline to be displayed
     */
    public SplineDisplay(Spline spline, int xDim, int yDim, int width, int height) {
        this.spline = spline;
        this.xDim = xDim;
        this.yDim = yDim;
        image = new BufferedImage(width, height, 1);
        graphics = new SplineGraphics((Graphics2D) image.getGraphics(), this);
        graphics.setTranslation(this::translate);
        onSplineDisplayables = new ArrayList<>();
        onGridDisplayables = new ArrayList<>();
        displayables = new ArrayList<>();
        onGridBoundaries = new Extrema(spline.getDimensions());
        setTitle("Spline Display");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    /**
     * A method for creating the window and displaying the spline
     */
    public void display() {
        setBounds(0, 0, image.getWidth() + windowWidthOffset, image.getHeight() + windowHeightOffset);
        setLayout(null);
        setTranslationValues();
        setVisible(true);
    }

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
     * A method for drawing the x and y axis on the graph
     */
    public void drawAxis() {
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        graphics.setColor(new Color(255, 255, 255));
        graphics.setStroke(new BasicStroke(1));
        graphics.drawLine(xOffset, 0, xOffset, image.getHeight());
        graphics.drawLine(0, image.getHeight() - yOffset, image.getWidth(), image.getHeight() - yOffset);
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
     * A method for displaying the onGridDisplayables
     */
    public void paintOnGrid() {
        DPoint point = new DPoint(spline.getDimensions());
        for (double y = onGridBoundaries.lesserPoint.get(yDim); y < onGridBoundaries.greaterPoint.get(yDim); y += onGridStep) {
            for (double x = onGridBoundaries.lesserPoint.get(xDim); x < onGridBoundaries.greaterPoint.get(xDim); x += onGridStep) {
                for (int d = 0; d < onGridDisplayables.size(); d++) {
                    point.set(yDim, y);
                    point.set(xDim, x);
                    onGridDisplayables.get(d).get(point).display(graphics);
                }
            }
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
     * A method for painting the displayables that are not dependent on anything
     */
    public void paintDisplayables() {
        for (int i = 0; i < displayables.size(); i++) {
            displayables.get(i).display(graphics);
        }
    }

    /**
     * A method for setting the translation values, namely xOffset, yOffset and scalar
     */
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
     * A method for getting a setting the boundingBox object
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
     * A method for adjusting the coordinates of the spline to fit the settings of the graph
     *
     * @param point The point to be translated
     * @return The new translated point
     */
    public DPoint translate(DPoint point) {
        point.multiply(xDim, scalar);
        point.multiply(yDim, scalar);

        point.add(xDim, xOffset);
        point.add(yDim, yOffset);

        point.set(yDim, image.getHeight() - point.get(yDim));

        return point;
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
            DVector followerPoint = follower.get(gridPoint.clone());
            return new DPosVector(gridPoint, followerPoint);
        });
    }

}