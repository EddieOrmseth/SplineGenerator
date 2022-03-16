package SplineGenerator.Applied;

import SplineGenerator.Util.DPoint;

public class OutlierEliminator {

    public static DPoint getAdjustedAverageByIterations(int iterations, DPoint... data) {
        DPoint adjustedAverage = getAverage(data);
        System.out.println(adjustedAverage);

        for (int i = 0; i < iterations; i++) {
            adjustedAverage.set(getNewAdjustedAverage(adjustedAverage, data));
//            System.out.println("Iteration 0 : " + adjustedAverage);
            System.out.println(adjustedAverage);
        }

        return adjustedAverage;
    }

    public static DPoint getAdjustedAverageByMinimumChange(double minChange, DPoint... data) {
        DPoint adjustedAverage = getAverage(data);
        DPoint previousAverage = new DPoint(adjustedAverage.getDimensions());
        System.out.println(adjustedAverage);

        do {
            previousAverage.set(adjustedAverage);
            adjustedAverage.set(getNewAdjustedAverage(adjustedAverage, data));
            System.out.println(adjustedAverage);
        } while (previousAverage.getDistance(adjustedAverage) > minChange);

        return adjustedAverage;
    }

    private static DPoint getNewAdjustedAverage(DPoint average, DPoint... data) {
        double[] distances = new double[data.length];
        for (int i = 0; i < data.length; i++) {
            distances[i] = average.getDistance(data[i]);
        }
        double sum = sum(distances);

        for (int i = 0; i < distances.length; i++) {
//            distances[i] = Math.pow(.5, sum - distances[i]);
            distances[i] = sum - distances[i];
        }
        sum = sum(distances);

        DPoint newAverage = new DPoint(average.getDimensions());
        DPoint newPoint = new DPoint(average.getDimensions());
        for (int i = 0; i < data.length; i++) {
            newPoint.set(data[i]);
            newPoint.multiplyAll(distances[i] / sum);
            newAverage.add(newPoint);
        }

        return newAverage;
    }

    private static DPoint getAverage(DPoint... data) {
        DPoint average = new DPoint(data[0].getDimensions());
        for (int i = 0; i < data.length; i++) {
            for (int d = 0; d < data[0].getDimensions(); d++) {
                average.set(d, average.get(d) + data[i].get(d));
            }
        }

        average.multiplyAll(1.0 / data.length);
        return average;
    }

    public static double sum(double[] data) {
        double total = 0;
        for (int i = 0; i < data.length; i++) {
            total += data[i];
        }
        return total;
    }

}
