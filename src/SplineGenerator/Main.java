package SplineGenerator;

import SplineGenerator.Splines.PolynomicSpline;
import SplineGenerator.Splines.Spline;
import SplineGenerator.Util.ControlPoint;
import SplineGenerator.Util.InterpolationInfo;

public class Main {

    public static void main(String[] args) {

        PolynomicSpline spline = new PolynomicSpline();
        spline.addControlPoint(new ControlPoint(1, 1, Math.PI / 4));
//        spline.addControlPoint(new ControlPoint(2, 7, Math.PI / 2));
//        spline.addControlPoint(new ControlPoint(-3, 1, 5 * Math.PI / 4));
        spline.addControlPoint(new ControlPoint(-5, -3, 0 * Math.PI / 4));
//        spline.addControlPoint(new ControlPoint(-1, 6, 5 * Math.PI / 4));
//        spline.addControlPoint(new ControlPoint(4, -6, 5 * Math.PI / 4));
        spline.addControlPoint(new ControlPoint(1, -2, 1 * Math.PI / 4));

        spline.setPolynomicType(PolynomicSpline.PolynomicType.Quartic);
        spline.closed = true;

        InterpolationInfo c1 = new InterpolationInfo();
        c1.interpolationType = Spline.InterpolationType.Hermite;
        c1.endBehavior = Spline.EndBehavior.Hermite;
        spline.interpolationTypes.add(c1);

        InterpolationInfo c2 = new InterpolationInfo();
        c2.interpolationType = Spline.InterpolationType.Linked;
        spline.interpolationTypes.add(c2);

        spline.generate();
        System.out.println(spline);
        System.out.println(spline.getDesmosEquations());
    }

}
