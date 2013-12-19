package ljal;

/**
 * Interface for a (possibly) adaptive parameter, that changes according to the
 * round
 */
public interface AdaptiveParameter {

    /**
     * Get the value at round t
     *
     * @param t The round number
     * @return The actual parameter value
     */
    public double at(int t);
}
