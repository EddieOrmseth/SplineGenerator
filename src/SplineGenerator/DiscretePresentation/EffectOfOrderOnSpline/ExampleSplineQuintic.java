package SplineGenerator.DiscretePresentation.EffectOfOrderOnSpline;

import SplineGenerator.GUI.KeyBoardListener;
import SplineGenerator.GUI.SplineDisplay;
import SplineGenerator.Splines.PolynomicSpline;
import SplineGenerator.Splines.Spline;
import SplineGenerator.Util.DControlPoint;
import SplineGenerator.Util.DVector;
import SplineGenerator.Util.InterpolationInfo;

public class ExampleSplineQuintic {

    public static void main(String... args) {

        KeyBoardListener.initialize();

        PolynomicSpline spline = new PolynomicSpline(2);

        spline.addControlPoint(new DControlPoint(new DVector(0, 0), new DVector(1, 1), new DVector(0, 0)));
        spline.addControlPoint(new DControlPoint(new DVector(2, 1)));
        spline.addControlPoint(new DControlPoint(new DVector(4, 0)));
        spline.addControlPoint(new DControlPoint(new DVector(4, 3)));
        spline.addControlPoint(new DControlPoint(new DVector(1, 2)));
        spline.addControlPoint(new DControlPoint(new DVector(1, -1), new DVector(-1, -1), new DVector(0, 0)));
        spline.setPolynomicOrder(5);
        spline.closed = false;

        InterpolationInfo c1 = new InterpolationInfo();
        c1.interpolationType = Spline.InterpolationType.Linked;
        c1.endBehavior = Spline.EndBehavior.Hermite;
        spline.interpolationTypes.add(c1);

        InterpolationInfo c2 = new InterpolationInfo();
        c2.interpolationType = Spline.InterpolationType.Linked;
        c2.endBehavior = Spline.EndBehavior.Hermite;
        spline.interpolationTypes.add(c2);

        InterpolationInfo c3 = new InterpolationInfo();
        c3.interpolationType = Spline.InterpolationType.Linked;
        c3.endBehavior = Spline.EndBehavior.None;
        spline.interpolationTypes.add(c3);

        InterpolationInfo c4 = new InterpolationInfo();
        c4.interpolationType = Spline.InterpolationType.Linked;
        c4.endBehavior = Spline.EndBehavior.None;
        spline.interpolationTypes.add(c4);

        spline.generate();

        SplineDisplay display = new SplineDisplay(spline, 0, 1, 1600, 700);

        display.displayables.add((graphics) -> {
            graphics.getGraphics().drawString("Quintic", 600, 100);
        });

        display.display();

    }

}
