package SplineGenerator.Util.MultivariableFunctions;

public interface FourVariableFunction<I1, I2, I3, I4, O> {

    O get(I1 input1, I2 input2, I3 input3, I4 input4);

}
