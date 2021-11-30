package SplineGenerator.Util.MultivariableFunctions;

public interface TwoVariableFunction<I1, I2, O> {

    O get(I1 input1, I2 input2);

}
