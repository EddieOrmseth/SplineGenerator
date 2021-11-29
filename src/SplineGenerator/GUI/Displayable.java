package SplineGenerator.GUI;

/**
 * An interface for things that can be displayed
 */
@FunctionalInterface
public interface Displayable {

    /**
     * The method that will be called to display the object on the Graphics2D object
     *
     * @param graphics The object to display on
     */
    void display(DisplayGraphics graphics);
}
