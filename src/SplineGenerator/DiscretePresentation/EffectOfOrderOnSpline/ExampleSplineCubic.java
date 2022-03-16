package SplineGenerator.DiscretePresentation.EffectOfOrderOnSpline;

import SplineGenerator.GUI.SplineDisplay;
import SplineGenerator.Splines.PolynomicSpline;
import SplineGenerator.Splines.Spline;
import SplineGenerator.Util.DControlPoint;
import SplineGenerator.Util.DVector;
import SplineGenerator.Util.InterpolationInfo;

public class ExampleSplineCubic {

    public static void main(String... args) {

        PolynomicSpline spline = new PolynomicSpline(2);

        spline.addControlPoint(new DControlPoint(new DVector(0, 0), new DVector(1, 1)));
        spline.addControlPoint(new DControlPoint(new DVector(2, 1)));
        spline.addControlPoint(new DControlPoint(new DVector(4, 0)));
        spline.addControlPoint(new DControlPoint(new DVector(4, 3)));
        spline.addControlPoint(new DControlPoint(new DVector(1, 2)));
        spline.addControlPoint(new DControlPoint(new DVector(1, -1), new DVector(-1, -1)));

        spline.setPolynomicOrder(3);
        spline.closed = false;

        InterpolationInfo c1 = new InterpolationInfo();
        c1.interpolationType = Spline.InterpolationType.Linked;
        c1.endBehavior = Spline.EndBehavior.Hermite;
        spline.interpolationTypes.add(c1);

        InterpolationInfo c2 = new InterpolationInfo();
        c2.interpolationType = Spline.InterpolationType.Linked;
        c2.endBehavior = Spline.EndBehavior.None;
        spline.interpolationTypes.add(c2);

        spline.generate();

        SplineDisplay display = new SplineDisplay(spline, 0, 1, 1600, 700);

        display.displayables.add((graphics) -> {
            graphics.getGraphics().drawString("Cubic", 600, 100);
        });

        display.display();

    }

}
