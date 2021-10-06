package SplineGenerator.Util;

/**
 * A class that represents a simple function that takes in a parameter and returns a value
 *
 * @param <T> The type to be taken in
 * @param <K> The type to be returned
 */
@FunctionalInterface
public interface Function<T, K> {

    /**
     * The method that gets the K object for the given T value
     *
     * @param variable The input parameter for the function
     * @return The K value returned by the function
     */
    K get(T variable);

}
