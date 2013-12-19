package ljal;

import java.util.Arrays;
import java.util.HashMap;

/**
 * Class representing an agent learning with knowledge about its neighbors in
 * the coordination graph
 */
public class Agent {

    private int m_actions;
    private SelectionAlgorithm m_selection;
    private double m_alpha;
    private Agent[] m_neighbors;
    private JointActions m_jointNeighborActions;
    private double[] m_Q;
    private HashMap<Agent, double[]> m_F;
    private int m_last_action;
    private int m_updates;

    /**
     * Constructor
     *
     * @param actions The number of actions the agent has
     * @param selection The algorithm to select the next action of the agent
     * @param alpha The learning constant
     */
    public Agent(int actions, SelectionAlgorithm selection, double alpha) {
        m_actions = actions;
        m_selection = selection;
        m_alpha = alpha;

        m_updates = 0;
    }

    /**
     * Sets the neighbors of this agent Must be called before other the
     * selection and update methods are used
     *
     * @param neighbors A list of neighboring agents
     */
    public void setNeighbors(Agent[] neighbors) {
        m_neighbors = neighbors;
        m_jointNeighborActions = new JointActions(neighbors);

        m_Q = new double[m_actions * m_jointNeighborActions.getCount()];
        Arrays.fill(m_Q, 0);

        m_F = new HashMap<Agent, double[]>();
        for (Agent neighbor : m_neighbors) {
            double[] F_neighbor = new double[neighbor.m_actions];
            Arrays.fill(F_neighbor, 0.0);
            m_F.put(neighbor, F_neighbor);
        }
    }

    /**
     * Returns the number of actions this agent has
     *
     * @return The number of actions
     */
    public int getActions() {
        return m_actions;
    }

    /**
     * Selects an action for this agent
     *
     * @param t The time step
     * @return The chosen action
     */
    public int selectAction(int t) {
        m_last_action = m_selection.selectAction(estimatedValues(), t);
        return m_last_action;
    }

    /**
     * Updates the values of the agent based on the received reward
     *
     * @param reward
     */
    public void update(double reward) {
        int[] lastNeighborActions = new int[m_neighbors.length];
        for (int i = 0; i < m_neighbors.length; ++i) {
            lastNeighborActions[i] = m_neighbors[i].m_last_action;
        }
        int jointActionIndex = new JointAction(m_neighbors, lastNeighborActions).prependedActionIndex(this, m_last_action);

        m_Q[jointActionIndex] += m_alpha * (reward - m_Q[jointActionIndex]);

        for (Agent neighbor : m_neighbors) {
            double[] F_neighbor = m_F.get(neighbor);
            for (int i = 0; i < neighbor.m_actions; ++i) {
                F_neighbor[i] *= ((double) m_updates) / (m_updates + 1);
            }
            F_neighbor[neighbor.m_last_action] += 1.0 / (m_updates + 1);
        }

        m_updates += 1;
    }

    private double[] estimatedValues() {
        double[] EV = new double[m_actions];
        Arrays.fill(EV, 0.0);

        for (JointAction jointAction : m_jointNeighborActions) {
            double probability = 1.0;
            for (int i = 0; i < m_neighbors.length; ++i) {
                double[] F_neighbor = m_F.get(m_neighbors[i]);
                probability *= F_neighbor[jointAction.getActions()[i]];
            }

            for (int i = 0; i < m_actions; i++) {
                EV[i] += m_Q[jointAction.prependedActionIndex(this, i)] * probability;
            }
        }

        return EV;
    }
}
