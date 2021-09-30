package SplineGenerator.GUI;

import SplineGenerator.Splines.Spline;
import SplineGenerator.Util.BetterPoint;
import SplineGenerator.Util.BoundingBox;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * A simple class for displaying a spline
 */
public class SplineDisplay extends JFrame {

    /**
     * The spline to be displayed
     */
    private Spline spline;

    /**
     * The amount to step by when creating the spline out of tons of line segments
     */
    private double step = .001;

    /**
     * The image object that is painted on to screen
     */
    BufferedImage image;

    /**
     * A BoundingBox that the spline resides in
     */
    BoundingBox boundingBox;

    /**
     * The x offset to position the spline and related objects
     */
    int xOffset;

    /**
     * The y offset to position the spline and related objects
     */
    int yOffset;

    /**
     * The scalar that is used to scale the spline to the preferred size
     */
    double scalar;

    /**
     * The percent of the image to leave as border
     */
    double percentBorder = .1;

    /**
     * A constructor for the SplineDisplay
     *
     * @param spline The spline to be displayed
     */
    public SplineDisplay(Spline spline) {
        this.spline = spline;
        image = new BufferedImage(800, 500, 1);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    /**
     * A method for creating the window and displaying the spline
     */
    public void create() {
        setBounds(100, 100, 800 + 16, 500 + 39);
        setLayout(null);
        setVisible(true);
    }

    @Override
    public void paint(Graphics graphics) {
        setTranslationValues();
        drawSpline();
        graphics.drawImage(image, 8, 39, this);
    }

    /**
     * A method for drawing the spline on the image object
     */
    public void drawSpline() {
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        graphics.setStroke(new BasicStroke(2));
        graphics.setColor(new Color(255, 255, 255));
        boolean setP2 = true;
        BetterPoint p1 = translate(spline.get(0)), p2 = spline.get(0);

        for (double t = step; t < spline.pieces; t += step) {
            if (setP2) {
                p2 = translate(spline.get(t));
                graphics.drawLine((int) p1.x, (int) p1.y, (int) p2.x, (int) p2.y);
            } else {
                p1 = translate(spline.get(t));
                graphics.drawLine((int) p2.x, (int) p2.y, (int) p1.x, (int) p1.y);
            }

            setP2 = !setP2;
        }

        System.out.println("Spline Drawn");
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
        BetterPoint point;

        for (double t = step; t < spline.pieces; t += step) {
            point = spline.get(t);
            point.y = image.getHeight() - point.y;

            if (point.x < box.x1) {
                box.x1 = (int) point.x;
            } else if (point.x > box.x2) {
                box.x2 = (int) point.x;
            }

            if (point.y < box.y1) {
                box.y1 = (int) point.y;
            } else if (point.y > box.y2) {
                box.y2 = (int) point.y;
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
    public BetterPoint translate(BetterPoint point) {
        point.y = image.getHeight() - point.y;

        point.x *= scalar;
        point.y *= scalar;

        point.x += xOffset;
        point.y += yOffset;

        return point;
    }

}