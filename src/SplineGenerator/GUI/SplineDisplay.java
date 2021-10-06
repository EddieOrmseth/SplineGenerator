package SplineGenerator.GUI;

import SplineGenerator.Splines.Spline;
import SplineGenerator.Util.BoundingBox;
import SplineGenerator.Util.DPoint;
import SplineGenerator.Util.Function;

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
    private BoundingBox boundingBox;

    /**
     * A BoundingBox that the scaled spline resides in
     */
    private BoundingBox scaledBoundingBox;

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
    private double onSplineStep = 1;

    /**
     * The amount to step by when drawing on the grid, in the scale of the spline
     */
    private double onGridStep = 1;

    /**
     * The displayables that are dependant on the t-value of the spline
     */
    public ArrayList<Function<Double, Displayable>> onSplineDisplayables;

    /**
     * The displayables that are dependant on the location in the plane
     */
    public ArrayList<Function<DPoint, Displayable>> onGridDisplayables;

    /**
     * A constructor for the SplineDisplay
     *
     * @param spline The spline to be displayed
     */
    public SplineDisplay(Spline spline, int xDim, int yDim) {
        this.spline = spline;
        this.xDim = xDim;
        this.yDim = yDim;
        image = new BufferedImage(800, 500, 1);
        graphics = new SplineGraphics((Graphics2D) image.getGraphics(), this);
        graphics.setTranslation(this::translate);
        onSplineDisplayables = new ArrayList<>();
        onGridDisplayables = new ArrayList<>();
        setTitle("Spline Display");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    /**
     * A method for creating the window and displaying the spline
     */
    public void display() {
        setBounds(100, 100, 800 + 16, 500 + 39);
        setLayout(null);
        spline.takeNextDerivative();
        spline.takeNextDerivative();
        setTranslationValues();
        setVisible(true);
    }

    @Override
    public void paint(Graphics graphics) {
        drawAxis();
        drawSpline();

        paintOnGrid();
        paintOnSpline();

        graphics.drawImage(image, 8, 39, this);
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

//        System.out.println("Spline Drawn");
    }

    /**
     * A method for displaying the onGridDisplayables
     */
    public void paintOnGrid() {
        DPoint point = new DPoint(spline.getDimensions());
        for (double y = boundingBox.y1; y < boundingBox.y2; y += onGridStep) {
            for (double x = boundingBox.x1; x < boundingBox.x2; x += onGridStep) {
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
     * A method for setting the translation values, namely xOffset, yOffset and scalar
     */
    public void setTranslationValues() {
        boundingBox = etBoundingBox();
        double xScalar = (image.getWidth() * (1 - 2 * percentBorder)) / (boundingBox.x2 - boundingBox.x1);
        double yScalar = (image.getHeight() * (1 - 2 * percentBorder)) / (boundingBox.y2 - boundingBox.y1);

        scalar = Math.min(xScalar, yScalar);
        scaledBoundingBox = boundingBox.clone();
        scaledBoundingBox.applyScalar(scalar);

        xOffset = (int) ((image.getWidth() / 2) - ((scaledBoundingBox.x2 + scaledBoundingBox.x1) / 2));
        yOffset = (int) ((image.getHeight() / 2) - ((scaledBoundingBox.y2 + scaledBoundingBox.y1) / 2));
    }

    /**
     * A method for getting a setting the boundingBox object
     *
     * @return The BoundingBox object that the spline resides inside
     */
    public BoundingBox etBoundingBox() {
        BoundingBox box = new BoundingBox();
        DPoint point;

        for (double t = 0; t < spline.pieces; t += pointOnSplineStep) {
            point = spline.get(t);

            if (point.get(xDim) < box.x1) {
                box.x1 = point.get(xDim);
            } else if (point.get(xDim) > box.x2) {
                box.x2 = point.get(xDim);
            }

            if (point.get(yDim) < box.y1) {
                box.y1 = point.get(yDim);
            } else if (point.get(yDim) > box.y2) {
                box.y2 = point.get(yDim);
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

}