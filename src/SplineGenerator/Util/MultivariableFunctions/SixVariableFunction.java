package SplineGenerator.Util.MultivariableFunctions;

public interface SixVariableFunction<I1, I2, I3, I4, I5, I6, O> {

    O get(I1 input1, I2 input2, I3 input3, I4 input4, I5 input5, I6 input6);

}
