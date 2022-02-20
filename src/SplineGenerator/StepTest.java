package SplineGenerator;

import SplineGenerator.Applied.SplineVelocityController;
import SplineGenerator.Applied.StepController;
import SplineGenerator.GUI.BallVelocityDirectionController;
import SplineGenerator.GUI.DisplayGraphics;
import SplineGenerator.GUI.KeyBoardListener;
import SplineGenerator.GUI.SplineDisplay;
import SplineGenerator.Splines.PolynomicSpline;
import SplineGenerator.Splines.Spline;
import SplineGenerator.Util.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.function.Supplier;

public class StepTest {

    public static void main(String... args) {

        KeyBoardListener.initialize();

        PolynomicSpline spline = new PolynomicSpline(2);

        spline.addControlPoint(new DControlPoint(new DVector( -2.8123550200082,  -1.0795619459264), new DDirection(1, 0), new DDirection(0, 0)));
        spline.addControlPoint(new DControlPoint(new DVector(-3.79037, -2.2429), new DDirection(1, 1)));
        spline.addControlPoint(new DControlPoint(new DVector(-1.22526973306752, -2.2429), new DDirection(1, 1)));
        spline.addControlPoint(new DControlPoint(new DVector(-3.79037 , -2.7520008784104 ), new DDirection(1, 1)));
        spline.addControlPoint(new DControlPoint(new DVector(-.6587 , -3.83007), new DDirection(1, 1)));
        spline.addControlPoint(new DControlPoint(new DVector(-3.79037, -2.7520008784104), new DDirection(1, 0), new DDirection(0, 0)));

        spline.setPolynomicOrder(5);
        spline.closed = false;

        InterpolationInfo c1 = new InterpolationInfo();
        c1.interpolationType = Spline.InterpolationType.Hermite;
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

        spline.generate();
        spline.takeNextDerivative();

        Function<DVector, DVector> derivativeModifier = variable -> {
            variable.setMagnitude(10);
            return variable;
        };

        Function<DVector, DVector> distanceModifier = variable -> {
            variable.multiplyAll(35.001);
            return variable;
        };


        StepController navigator = new StepController(spline, derivativeModifier, distanceModifier, .02, .1);
        StepController.Controller navigationController = navigator.getController();

        Supplier<Double> supplier = navigationController::getTValue;

        SplineVelocityController velocityController = new SplineVelocityController(spline, supplier, 1.7, 1.0, 0.0, 0.2, 0.2);
        velocityController.addStopToEnd(2.5, .01);

        SplineDisplay display = new SplineDisplay(spline, 0, 1, 1600, 700);

        BallVelocityDirectionController ball = new BallVelocityDirectionController(navigationController, new DPoint(0, 0));
        ball.velocityController = velocityController;

        display.displayables.add(ball);

        try {
            BufferedImage backgroundImage = ImageIO.read(new File("C:\\Users\\Tators 03\\Desktop\\code\\SplineGenerator\\Capture.PNG"));
            throw new IllegalCallerException("Ibrahim Change This To A Relative Path");
        } catch (IOException e) {
            System.out.println("Unable to read background Image");
        }

//        JFrame editorFrame = new JFrame("Image Demo");
//        editorFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//
//        BufferedImage image = null;
//        try
//        {
//             image = ImageIO.read(new File("C:\\Users\\Tators 03\\Desktop\\code\\SplineGenerator\\Capture.PNG"));
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//            System.exit(1);
//        }
//        ImageIcon imageIcon = new ImageIcon(image);
//        JLabel jLabel = new JLabel();
//        jLabel.setIcon(imageIcon);
//
//        display.displayables.add((DisplayGraphics displays) -> {
//            display.getContentPane().add(jLabel,BorderLayout.CENTER);
//        });

//        editorFrame.getContentPane().add(jLabel, BorderLayout.CENTER);

//        editorFrame.pack();
//        editorFrame.setLocationRelativeTo(null);
//        editorFrame.setVisible(true);

/*        try {
            Image picture = ImageIO.read(new File("C:\\Users\\Tators 03\\Desktop\\code\\SplineGenerator\\Capture.PNG"));
//            ImageIcon imageIcon = new ImageIcon(picture);
//            JLabel jLabel = new JLabel();
            display.setIconImage(picture);
//            display.getContentPane().add(jLabel,BorderLayout.CENTER);
            System.out.println("hi");
        } catch (IOException e) {
            e.printStackTrace();
        }
        */
//        display.getContentPane().add(jLabel,BorderLayout.CENTER);
        ball.start();

        display.display();

        while (true) {
            display.repaint();
        }

    }

}
