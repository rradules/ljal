package ljal;

/**
 * Interface for algorithms selecting the next action for an agent
 */
public interface SelectionAlgorithm {

    /**
     * Chooses the action for an agent to select, given the estimated values of
     * its actions
     *
     * @param EV The estimated values of the different actions
     * @param t The time step at which to choose the action
     * @return The action chosen by the algorithm (index of EV)
     */
    public int selectAction(double[] EV, int t);
}
