package SplineGenerator.DiscretePresentation;

import SplineGenerator.Util.DVector;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class DiscreteUtil {

    private static final String SplineInputPath = "C:\\Users\\eddie\\Downloads\\Programming\\GitHubRetry\\SplineGenerator\\src\\SplineGenerator\\DiscretePresentation\\SplineInput.txt";

    public ArrayList<DVector[]> DVectorsFromSplineInput() {
        return DVectorsFromTextFile(SplineInputPath);
    }

    public ArrayList<DVector[]> DVectorsFromTextFile(String path) {
        File file = new File(path);
        ArrayList<DVector[]> arrays = new ArrayList<>();

        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader reader = new BufferedReader(fileReader);

            String input = reader.readLine();

            while (input != null) {

//                arrays.add(DVector.fromText(input));

                input = reader.readLine();
            }

        } catch (IOException e) {
            System.out.println("Failed To Read File \"" + path +  "\" in DiscreteUtil.DVectorsFromTextFile");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        return arrays;
    }

}
