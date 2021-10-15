package SplineGenerator.Applied;

import SplineGenerator.Util.DDirection;
import SplineGenerator.Util.DPoint;

/**
 * An interface for things that can provide navigation
 */
public interface Navigator {

     Controller getController();

     abstract class Controller {

         public abstract void update(DPoint point);

         public abstract DDirection getDirection();

         public double getVelocity() {
             return 0;
         }

     }

}
