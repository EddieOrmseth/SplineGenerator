package SplineGenerator.Applied;

import SplineGenerator.GUI.KeyBoardListener;
import SplineGenerator.Util.DVector;
import SplineGenerator.Util.Function;

import java.awt.event.KeyEvent;

public class GeneralVelocityController implements VelocityController {

    private DVector previousMotion;

    private int currentIndex = 0;
    private long startTime = System.currentTimeMillis();
    private long previousTime = startTime;
    private double registerTimeSize;
    private double millisecondsPerIndex;
    private double[] pastDirectionChange;

    private double summedDirectionChange = 0;

    private Function<Double, Double> accelerationFunction;

    private double previousDesiredVelocity;
    private double desiredVelocity;

    private boolean isSpeedingUp = false;

    private double minVelocity;
    private double maxVelocity;

    public GeneralVelocityController(double registerTimeSize, int maxEntriesPerRegister, Function<Double, Double> accelerationFunction, DVector velocity, double maxVelocity, double minVelocity) {
        this.registerTimeSize = registerTimeSize;
        millisecondsPerIndex = registerTimeSize / maxEntriesPerRegister;
        pastDirectionChange = new double[maxEntriesPerRegister];

        previousMotion = new DVector(velocity.getDimensions());
        previousMotion.set(velocity);
        desiredVelocity = velocity.getMagnitude();
        previousDesiredVelocity = desiredVelocity;

        this.accelerationFunction = accelerationFunction;
        this.maxVelocity = maxVelocity;
        this.minVelocity = minVelocity;
    }

    //    @Override
    public void update(DVector motion) {
        // There is a minimum time that must be elapsed before updating
        long now = System.currentTimeMillis();
        if (now - previousTime < millisecondsPerIndex) {
            return;
        }

        double angleDiff = previousMotion.getAngleBetween(motion);
        if (Double.isNaN(angleDiff)) {
            angleDiff = 0;
        }

        if (KeyBoardListener.get(KeyEvent.VK_F)) {
            angleDiff = 0;
        }

        updateSummedDirection(now, angleDiff);

        desiredVelocity += accelerationFunction.get(summedDirectionChange);

        if (desiredVelocity > maxVelocity) {
            desiredVelocity = maxVelocity;
        } else if (desiredVelocity < minVelocity) {
            desiredVelocity = maxVelocity;
        }

        previousMotion.set(motion);
        isSpeedingUp = previousDesiredVelocity == desiredVelocity ? isSpeedingUp : desiredVelocity > previousDesiredVelocity;
        previousDesiredVelocity = desiredVelocity;
    }

    @Override
    public double getVelocity() {
        return desiredVelocity;
    }

    public boolean isSpeedingUp() {
        return isSpeedingUp;
    }

    public void updateSummedDirection(long now, double currentChange) {
        int previousIndex = currentIndex;
        long deltaTime = now - previousTime;
        previousTime = now;
        currentIndex += (int) (deltaTime / millisecondsPerIndex);

        currentIndex %= pastDirectionChange.length;

        if (currentIndex > previousIndex) {
            for (int i = previousIndex + 1; i <= currentIndex; i++) {
                summedDirectionChange -= pastDirectionChange[i];
                pastDirectionChange[i] = 0;
            }
        } else {
            for (int i = previousIndex + 1; i < pastDirectionChange.length; i++) {
                summedDirectionChange -= pastDirectionChange[i];
                pastDirectionChange[i] = 0;
            }
            for (int i = 0; i <= currentIndex; i++) {
                summedDirectionChange -= pastDirectionChange[i];
                pastDirectionChange[i] = 0;
            }
        }

        summedDirectionChange += currentChange;
        pastDirectionChange[currentIndex] = currentChange;
    }

    public double getSummedDirectionChange() {
        return summedDirectionChange;
    }

    public static Function<Double, Double> getLinearThreshAccelerationFunction(double threshold, double scale) {
        return change -> {
            double delta = threshold - change;
            return delta * scale;
        };
    }

    public static Function<Double, Double> getPolyThreshAccelerationFunction(double threshold, double scale, double power) {
        return change -> {
            double delta = threshold - change;
            return scale * Math.abs(Math.abs(Math.pow(delta, power))) * Math.signum(delta);
        };
    }

}
