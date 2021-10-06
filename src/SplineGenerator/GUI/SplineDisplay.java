package SplineGenerator.GUI;

import SplineGenerator.Splines.Spline;
import SplineGenerator.Util.BoundingBox;
import SplineGenerator.Util.DPoint;
import SplineGenerator.Util.DVector;
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
     * The index of the dimension of the spline to be displayed on the x-axis
     */
    private int x;

    /**
     * The index of the dimension of the spline to be displayed on the y-axis
     */
    private int y;

    /**
     * The amount to step by when creating the spline out of tons of line segments
     */
    private double pointOnSplineStep = .001;

    /**
     * The image object that is painted on to screen
     */
    private BufferedImage image;

    /**
     * A BoundingBox that the spline resides in
     */
    private BoundingBox boundingBox;

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
     * The length to draw each vector
     */
    private double vectorLength = 50;

    /**
     * The length of the arrow
     */
    private double arrowLength;

    /**
     * The color to color the vectors
     */
    private Color vectorColor = new Color(120, 8, 142);

    /**
     * The amount to step by when stepping on the spline, int t values
     */
    private double onSplineStep = .5;

    /**
     * The amount to step by when drawing on the grid, in the scale of the spline
     */
    private double onGridStep = 1;

    /**
     * The displayables that are dependant on the t-value of the spline
     */
    private ArrayList<Function<Double, Displayable>> onSplineDisplayables;

    /**
     * The displayables that are dependant on the location in the plane
     */
    private ArrayList<Function<DPoint, Displayable>> onGridDisplayables;

    /**
     * A constructor for the SplineDisplay
     *
     * @param spline The spline to be displayed
     */
    public SplineDisplay(Spline spline, int x, int y) {
        this.spline = spline;
        this.x = x;
        this.y = y;
        image = new BufferedImage(800, 500, 1);
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

        paintDerivative(0.5, 1);
        paintDerivative(1.5, 1);
        paintDerivative(2.5, 1);
        paintDerivative(3.5, 1);
        paintDerivative(4.5, 1);
        paintDerivative(5.5, 1);
        paintDerivative(6.5, 1);
        paintDerivative(7.5, 1);
        paintDerivative(8.5, 1);
        paintDerivative(9.5, 1);
        paintDerivative(10.5, 1);

//        paintDerivative(0.75, 2);
//        paintDerivative(1.75, 2);
//        paintDerivative(2.75, 2);
//        paintDerivative(3.75, 2);
//        paintDerivative(4.75, 2);

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
                graphics.drawLine((int) p1.get(x), (int) p1.get(y), (int) p2.get(x), (int) p2.get(y));
            } else {
                p1 = translate(spline.get(t));
                graphics.drawLine((int) p2.get(x), (int) p2.get(y), (int) p1.get(x), (int) p1.get(y));
            }

            setP2 = !setP2;
        }

//        System.out.println("Spline Drawn");
    }

    public void paintOnGrid() {

    }

    public void paintOnSpline() {

    }

    /**
     * A method for setting the translation values, namely xOffset, yOffset and scalar
     */
    public void setTranslationValues() {
        etBoundingBox();
        double xScalar = (image.getWidth() * (1 - 2 * percentBorder)) / (boundingBox.x2 - boundingBox.x1);
        double yScalar = (image.getHeight() * (1 - 2 * percentBorder)) / (boundingBox.y2 - boundingBox.y1);

        scalar = Math.min(xScalar, yScalar);
        boundingBox.applyScalar(scalar);

        xOffset = (int) ((image.getWidth() / 2) - ((boundingBox.x2 + boundingBox.x1) / 2));
        yOffset = (int) ((image.getHeight() / 2) - ((boundingBox.y2 + boundingBox.y1) / 2));
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

            if (point.get(x) < box.x1) {
                box.x1 = point.get(x);
            } else if (point.get(x) > box.x2) {
                box.x2 = point.get(x);
            }

            if (point.get(y) < box.y1) {
                box.y1 = point.get(y);
            } else if (point.get(y) > box.y2) {
                box.y2 = point.get(y);
            }
        }

        boundingBox = box;
        return box;
    }

    /**
     * A method for adjusting the coordinates of the spline to fit the settings of the graph
     *
     * @param point The point to be translated
     * @return The new translated point
     */
    public DPoint translate(DPoint point) {
        point.multiply(x, scalar);
        point.multiply(y, scalar);

        point.add(x, xOffset);
        point.add(y, yOffset);

        point.set(y, image.getHeight() - point.get(y));

        return point;
    }

    /**
     * A method for painting a point
     *
     * @param point The point to draw
     * @param x     The dimension to use as x
     * @param y     The dimension to use as y
     */
    public void paintPoint(DPoint point, int x, int y) {
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        graphics.setColor(Color.WHITE);
        graphics.setStroke(new BasicStroke(3));
        graphics.fillOval((int) point.get(x) - 3, (int) point.get(y) - 3, 5, 5);
    }

    /**
     * A method for painting a point
     *
     * @param point  The point to which to draw the vector
     * @param vector The vector to be draw
     * @param x      The dimension to use as x
     * @param y      The dimension to use as y
     */
    public void paintVector(DPoint point, DVector vector, int x, int y) {
        vector.multiply(y, -1);
        vector.setMagnitude(vectorLength);
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        graphics.setColor(vectorColor);
        graphics.setStroke(new BasicStroke(3));
        graphics.drawLine((int) point.get(x), (int) point.get(y), (int) (point.get(x) + vector.get(x)), (int) (point.get(y) + vector.get(y)));

        DPoint endPoint = point.clone().add(vector);
        DVector reverse = vector.clone();
        reverse.multiplyAll(-60);
        DVector orthVector = new DVector(-vector.get(y), vector.get(x));
        orthVector.setMagnitude(30);

        graphics.drawLine((int) endPoint.get(x), (int) endPoint.get(y), (int) (endPoint.get(x) + (.2) * (reverse.get(x) + orthVector.get(x))), (int) (endPoint.get(y) + (.2) * (reverse.get(y) + orthVector.get(y))));
        orthVector.multiplyAll(-1);
        graphics.drawLine((int) endPoint.get(x), (int) endPoint.get(y), (int) (endPoint.get(x) + (.2) * (reverse.get(x) + orthVector.get(x))), (int) (endPoint.get(y) + (.2) * (reverse.get(y) + orthVector.get(y))));
    }

    /**
     * A method for painting the derivative at a point
     *
     * @param t          The t value to evaluate the derivative at
     * @param derivative The derivative to use
     */
    public void paintDerivative(double t, int derivative) {
        DPoint startPoint = translate(spline.get(t));
        paintVector(startPoint, spline.evaluateDerivative(t, derivative).toDirection(), x, y);
        paintPoint(startPoint, x, y);
    }

}