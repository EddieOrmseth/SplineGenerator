package SplineGenerator;

import SplineGenerator.GUI.SplineDisplay;
import SplineGenerator.Splines.PolynomicSpline;
import SplineGenerator.Splines.Spline;
import SplineGenerator.Util.ControlPoint;
import SplineGenerator.Util.InterpolationInfo;

public class Main {

    public static void main(String[] args) {

//        PolynomicSpline spline = new PolynomicSpline();
//        spline.addControlPoint(new ControlPoint(1, 1, 0 * Math.PI / 4));
//        spline.addControlPoint(new ControlPoint(5, 3, Math.PI));
//        spline.addControlPoint(new ControlPoint(3, 3, 1 * Math.PI / 4));
//
//        spline.setPolynomicType(PolynomicSpline.PolynomicType.Cubic);
//
//        InterpolationInfo c1 = new InterpolationInfo();
//        c1.interpolationType = Spline.InterpolationType.Linked;
//        c1.endBehavior = Spline.EndBehavior.Hermite;
//        spline.interpolationTypes.add(c1);
//
//        InterpolationInfo c2 = new InterpolationInfo();
//        c2.interpolationType = Spline.InterpolationType.Linked;
//        spline.interpolationTypes.add(c2);
//
//        spline.generate();
//        System.out.println(spline);
//        System.out.println(spline.getDesmosEquations());

        PolynomicSpline spline = new PolynomicSpline();
        spline.addControlPoint(new ControlPoint(1, 1, 0 * Math.PI / 4, 0, 0));
        spline.addControlPoint(new ControlPoint(5, 3, 0));
        spline.addControlPoint(new ControlPoint(3, 3, 0 * Math.PI / 4));
        spline.addControlPoint(new ControlPoint(2, 4, 0));
        spline.addControlPoint(new ControlPoint(0, 6, 0));
        spline.addControlPoint(new ControlPoint(-2, 0, 0));
        spline.addControlPoint(new ControlPoint(0, 2, 0 * Math.PI / 2));
        spline.addControlPoint(new ControlPoint(-5, 5, Math.PI / 2, 0));

        spline.setPolynomicType(PolynomicSpline.PolynomicType.Cubic);
//        spline.closed = true;

        InterpolationInfo c1 = new InterpolationInfo();
        c1.interpolationType = Spline.InterpolationType.Linked;
        c1.endBehavior = Spline.EndBehavior.None;
        spline.interpolationTypes.add(c1);

        InterpolationInfo c2 = new InterpolationInfo();
        c2.interpolationType = Spline.InterpolationType.Linked;
        c2.endBehavior = Spline.EndBehavior.Hermite;
//        c2.endEffect = Spline.EndBehaviorEffect.Beginning;
        spline.interpolationTypes.add(c2);

//        InterpolationInfo c3 = new InterpolationInfo();
//        c3.interpolationType = Spline.InterpolationType.Linked;
//        c3.endBehavior = Spline.EndBehavior.None;
//        spline.interpolationTypes.add(c3);

        spline.generate();
        System.out.println(spline);
        System.out.println(spline.getDesmosEquations());

        SplineDisplay display = new SplineDisplay(spline);
        display.create();
    }

}
