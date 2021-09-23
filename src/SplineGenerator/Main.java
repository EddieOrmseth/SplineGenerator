package SplineGenerator;

import SplineGenerator.Util.ControlPoint;

public class Main {

    public static void main(String[] args) {

//        Spline spline1 = new Spline();
//        spline1.addControlPoint(new ControlPoint(1, 1, Math.PI / 4));
////        spline.addControlPoint(new ControlPoint(10, 3));
//        spline1.addControlPoint(new ControlPoint(14, 8));
//        spline1.addControlPoint(new ControlPoint(9, 10));
////        spline1.addControlPoint(new ControlPoint(-16.9, -30));
//        spline1.addControlPoint(new ControlPoint(-5, 7, (7 * Math.PI) / 4));
//        spline1.generate();
//        System.out.println(spline1);
//        System.out.println(spline1.getDesmosEquations());
////        System.out.println(Arrays.toString(spline.getEquation(1, 1)));
//
//        Spline spline2 = new Spline();
//        spline2.addControlPoint(new ControlPoint(1, 1, Math.PI / 4));
////        spline.addControlPoint(new ControlPoint(10, 3));
//        spline2.addControlPoint(new ControlPoint(14, 8));
//        spline2.addControlPoint(new ControlPoint(9, 10));
////        spline.addControlPoint(new ControlPoint(-16.9, -30));
//        spline2.addControlPoint(new ControlPoint(-5, 7, (7 * Math.PI) / 4));
//        spline2.addControlPoint(new ControlPoint(1, 1, Math.PI / 4));
//        spline2.generate3();
//        System.out.println(spline2);
//        System.out.println(spline2.getDesmosEquations());

        Spline spline = new Spline();

        spline.addControlPoint(new ControlPoint(3, 0, Math.PI / 4));
        spline.addControlPoint(new ControlPoint(0, 3,  (3 * Math.PI) / 4));
        spline.addControlPoint(new ControlPoint(-3, 0, Math.PI));
        spline.addControlPoint(new ControlPoint(0, -3, Math.PI / 3));

        spline.closed = true;
        spline.interpolationMethod = Spline.InterpolationMethod.CatmulRom;

        spline.generate3();
        System.out.println(spline);
        System.out.println(spline.getDesmosEquations());

    }

}
