package SplineGenerator.GUI;

import SplineGenerator.Util.DPoint;
import SplineGenerator.Util.DPosVector;
import SplineGenerator.Util.DVector;
import SplineGenerator.Util.Function;

import java.awt.*;

/**
 * A class for painting object on the spline
 */
public class DisplayGraphics {

    /**
     * The Graphics2D object to be painted on
     */
    private Graphics2D graphics;

    /**
     * The function to translate the points
     */
    private Function<DPoint, DPoint> translate;

    /**
     * The index to be used as the x-dimension
     */
    int xDim;

    /**
     * The index to be used as the y-dimension
     */
    int yDim;

    /**
     * The color to color the point
     */
    private Color pointColor = new Color(255, 255, 255);

    /**
     * The radius of drawn points
     */
    private double pointRadius = 6;

    /**
     * The color to color the vectors
     */
    private Color vectorColor = new Color(120, 8, 142);

    /**
     * The length to draw each vector
     */
    private double vectorLength = 20;

    /**
     * The ration from vectorLength to the desired arrow length
     */
    private double arrowLengthRatio = .2;

    /**
     * The ratio of the orthogonal vector when drawing the arrows
     */
    private double arrowOrthRatio = .5;

    /**
     * The width of the vector
     */
    private int lineWidth = 2;

    /**
     * A simple constructor requiring the object to be painted on
     *
     * @param graphics The object to be painted on
     */
    public DisplayGraphics(Graphics2D graphics, Display display) {
        this.graphics = graphics;
        xDim = display.xDim;
        yDim = display.yDim;
    }

    /**
     * A method for getting the translation of a given point
     *
     * @param point The point to be translated
     * @return The translated point
     */
    public DPoint translate(DPoint point) {
        return translate.get(point);
    }

    /**
     * A method for setting the translation
     *
     * @param translation The new translation
     */
    public void setTranslation(Function<DPoint, DPoint> translation) {
        this.translate = translation;
    }

    /**
     * A method for painting a DVector on the object
     *
     * @param posVector The vector to be drawn
     */
    public void paintVector(DPosVector posVector) {
        paintVector(posVector.getStartPoint(), posVector.toVector(), xDim, yDim);
    }

    /**
     * A method for painting a DVector on the object
     *
     * @param point  The point at which to put the vector
     * @param vector The vector to be painted
     */
    public void paintVector(DPoint point, DVector vector) {
        paintVector(point, vector, xDim, yDim);
    }

    /**
     * A method for painting a DVector on the object
     *
     * @param point  The point at which to put the vector
     * @param vector The vector to be painted
     * @param xDim   The value to use as the x-dimension
     * @param yDim   The value to use as the y-dimension
     */
    public void paintVector(DPoint point, DVector vector, int xDim, int yDim) {
        graphics.setStroke(new BasicStroke(lineWidth));
        graphics.setColor(vectorColor);

        point = translate.get(point);
        vector.multiply(yDim, -1);
        vector.setMagnitude(vectorLength);
        DPoint endPoint = new DPoint(point.get(xDim) + vector.get(xDim), point.get(yDim) + vector.get(yDim));

        graphics.drawLine((int) point.get(xDim), (int) point.get(yDim), (int) (endPoint.get(xDim)), (int) (endPoint.get(yDim)));

        vector.multiplyAll(-1);
        DVector orthVector = new DVector(-vector.get(yDim), vector.get(xDim));
        orthVector.setMagnitude(vectorLength * arrowOrthRatio);
        DVector arrow1 = vector.clone().add(orthVector);
        orthVector.multiplyAll(-1);
        DVector arrow2 = vector.clone().add(orthVector);
        arrow1.setMagnitude(vectorLength * arrowLengthRatio);
        arrow2.setMagnitude(vectorLength * arrowLengthRatio);

        graphics.drawLine((int) endPoint.get(xDim), (int) endPoint.get(yDim), (int) (endPoint.get(xDim) + arrow1.get(xDim)), (int) (endPoint.get(yDim) + arrow1.get(yDim)));
        graphics.drawLine((int) endPoint.get(xDim), (int) endPoint.get(yDim), (int) (endPoint.get(xDim) + arrow2.get(xDim)), (int) (endPoint.get(yDim) + arrow2.get(yDim)));
    }

    /**
     * A method for painting a point
     *
     * @param point The point to be painted
     */
    public void paintPoint(DPoint point) {
        paintPoint(point, xDim, yDim);
    }

    /**
     * A method for painting a point
     *
     * @param point The point to be painted
     * @param xDim  The index to be used as the x-dimension
     * @param yDim  The index to be used as teh y-dimension
     */
    public void paintPoint(DPoint point, int xDim, int yDim) {
        paintPoint(point, xDim, yDim, Color.WHITE);
    }

    /**
     * A method for painting a point
     *
     * @param point The point to be painted
     * @param xDim  The index to be used as the x-dimension
     * @param yDim  The index to be used as teh y-dimension
     * @param color The color to paint the point
     */
    public void paintPoint(DPoint point, int xDim, int yDim, Color color) {
        graphics.setColor(color);
        graphics.setStroke(new BasicStroke(3));

        point = translate.get(point);

        graphics.fillOval((int) (point.get(xDim) - pointRadius), (int) (point.get(yDim) - pointRadius), (int) (2 * pointRadius), (int) (2 * pointRadius));
    }

    /**
     * A method for painting a point
     *
     * @param point  The point to be painted
     * @param xDim   The index to be used as the x-dimension
     * @param yDim   The index to be used as the y-dimension
     * @param color  The color to paint the circle
     * @param radius The radius of the circle
     */
    public void paintPoint(DPoint point, int xDim, int yDim, Color color, int radius) {
        graphics.setColor(color);
        graphics.setStroke(new BasicStroke(3));

        point = translate.get(point);

        graphics.fillOval((int) (point.get(xDim) - radius), (int) (point.get(yDim) - radius), (int) (2 * radius), (int) (2 * radius));
    }

    /**
     * A method for drawing a circle on the screen
     *
     * @param center The center of the circle
     * @param radius The radius of the circle
     * @param xDim   The x dimension of the circle
     * @param yDim   The y dimension of the circle
     */
    public void paintCircle(DPoint center, double radius, int xDim, int yDim, Color color) {
        graphics.setColor(color);
        DPoint upperLeft = center.clone();
        upperLeft.add(0, -radius);
        upperLeft.add(1, radius);
        center = translate.get(center);
        upperLeft = translate.get(upperLeft);
        int scaledRadius = 2 * (int) (center.get(xDim) - upperLeft.get(xDim));

        graphics.drawOval((int) (upperLeft.get(xDim)), (int) (upperLeft.get(yDim)), scaledRadius, scaledRadius);
    }

    /**
     * A method for painting a line between two untranslated points
     *
     * @param p1    The first point
     * @param p2    The second point
     * @param width The width of the line
     * @param color The color of the line
     * @param xDim  The x dimension
     * @param yDim  The y dimension
     */
    public void paintLine(DPoint p1, DPoint p2, float width, Color color, int xDim, int yDim) {
        p1 = translate.get(p1);
        p2 = translate.get(p2);
        paintTranslatedLine(p1, p2, width, color, xDim, yDim);
    }

    /**
     * A method for painting a line between two translated points
     *
     * @param p1    The first point
     * @param p2    The second point
     * @param width The width of the line
     * @param color The color of the line
     * @param xDim  The x dimension
     * @param yDim  The y dimension
     */
    public void paintTranslatedLine(DPoint p1, DPoint p2, float width, Color color, int xDim, int yDim) {
        graphics.setStroke(new BasicStroke(width));
        graphics.setColor(color);
        graphics.drawLine((int) p1.get(xDim), (int) p1.get(yDim), (int) p2.get(xDim), (int) p2.get(yDim));
    }

    /**
     * A method for getting the Graphics2D object
     *
     * @return The Graphics2D object
     */
    public Graphics2D getGraphics() {
        return graphics;
    }


}
