package SplineGenerator.GUI;

import java.awt.*;

/**
 * An interface for things that can be displayed
 */
public interface Displayable {

    /**
     * The method that will be called to display the object on the Graphics2D object
     *
     * @param graphics The object to display on
     */
    void display(Graphics2D graphics);
}
