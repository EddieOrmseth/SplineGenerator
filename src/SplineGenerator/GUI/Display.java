package SplineGenerator.GUI;

import SplineGenerator.Util.DPoint;
import SplineGenerator.Util.Extrema;
import SplineGenerator.Util.Function;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Display extends JFrame {

    /**
     * The SplineGraphics object to be used for painting things
     */
    protected DisplayGraphics graphics;

    /**
     * The index of the dimension of the spline to be displayed on the x-axis
     */
    public int xDim;

    /**
     * The index of the dimension of the spline to be displayed on the y-axis
     */
    public int yDim;

    /**
     * The image object that is painted on to screen
     */
    protected BufferedImage image;

    /**
     * A BoundingBox that the unscaled objects will be painted in
     */
    protected Extrema boundingBox;

    /**
     * A BoundingBox that the scaled objects will be painted in
     */
    protected Extrema scaledBoundingBox;

    /**
     * The amount to offset the image in the window with respect to the width
     */
    protected int windowWidthOffset = 8;

    /**
     * The amount to offset the image in the window with respect to the height
     */
    protected int windowHeightOffset = 31;

    /**
     * The percent of the image to leave as border
     */
    protected double percentBorder = .1;

    /**
     * The amount to step by when drawing on the grid, in the scale of the spline
     */
    protected double onGridStep = 1.2;

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
     * The x offset to position the spline and related objects
     */
    protected int xOffset;

    /**
     * The y offset to position the spline and related objects
     */
    protected int yOffset;

    /**
     * The scalar that is used to scale the spline to the preferred size
     */
    protected double scalar;

    /**
     * The number of dimensions that the point given to the onGridDisplayables will be have
     */
    protected int dimensions;

    public Display(int dimensions, Extrema bounds, int xDim, int yDim, int width, int height) {
        this.dimensions = dimensions;
        this.boundingBox = bounds;
        this.xDim = xDim;
        this.yDim = yDim;
        image = new BufferedImage(width, height, 1);
        graphics = new DisplayGraphics((Graphics2D) image.getGraphics(), this);
        onGridDisplayables = new ArrayList<>();
        displayables = new ArrayList<>();
        onGridBoundaries = new Extrema(dimensions);
        graphics.setTranslation(this::translate);
        setTitle("Display");
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

    /**
     * A method for painting the display
     *
     * @param graphics The object upon which to paint
     */
    @Override
    public void paint(Graphics graphics) {
        image.getGraphics().clearRect(0, 0, image.getWidth(), image.getHeight());
        drawAxis();

        paintOnGrid();
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
     * A method for displaying the onGridDisplayables
     */
    public void paintOnGrid() {
        DPoint point = new DPoint(dimensions);
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
        double xScalar = (image.getWidth() * (1 - 2 * percentBorder)) / (boundingBox.greaterPoint.get(xDim) - boundingBox.lesserPoint.get(xDim));
        double yScalar = (image.getHeight() * (1 - 2 * percentBorder)) / (boundingBox.greaterPoint.get(yDim) - boundingBox.lesserPoint.get(yDim));

        scalar = Math.min(xScalar, yScalar);
        scaledBoundingBox = boundingBox.clone();
        scaledBoundingBox.multiplyAll(scalar);

        xOffset = (int) ((image.getWidth() / 2) - ((scaledBoundingBox.greaterPoint.get(xDim) + scaledBoundingBox.lesserPoint.get(xDim)) / 2));
        yOffset = (int) ((image.getHeight() / 2) - ((scaledBoundingBox.greaterPoint.get(yDim) + scaledBoundingBox.lesserPoint.get(yDim)) / 2));
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
     * A method for getting The number of dimensions that the point given to the onGridDisplayables will be have
     *
     * @return The number of dimensions that the point given to the onGridDisplayables will be have
     */
    public int getDimensions() {
        return dimensions;
    }

}
