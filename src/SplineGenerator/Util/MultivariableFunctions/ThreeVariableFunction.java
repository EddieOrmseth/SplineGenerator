package SplineGenerator.Util.MultivariableFunctions;

public interface ThreeVariableFunction<I1, I2, I3, O> {

    O get(I1 input1, I2 input2, I3 input3);

}
