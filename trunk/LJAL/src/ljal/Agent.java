package ljal;

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
        m_F = new HashMap<>();
        m_actions = actions;
        m_selection = selection;
        m_alpha = alpha;

        m_updates = 0;
    }

    /**
     * Sets the neighbors of this agent
     *
     * @param neighbors A list of neighboring agents
     */
    public void setNeighbors(Agent[] neighbors) {
        m_neighbors = neighbors;

        m_Q = new double[m_actions * jointNeighborActions()];
        for (int i = 0; i < m_Q.length; ++i) {
            m_Q[i] = 0;
        }

        for (Agent neighbor : m_neighbors) {
            double[] F_neighbor = new double[neighbor.m_actions];
            for (int i = 0; i < F_neighbor.length; ++i) {
                F_neighbor[i] = 0.0;
            }
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
        int jointAction = 0;
        int tmp = 1;
        for (Agent agent : m_neighbors) {
            jointAction += tmp * agent.m_last_action;
            tmp *= agent.m_actions;
        }

        m_Q[m_last_action + m_actions * jointAction] += m_alpha * (reward - m_Q[m_last_action + m_actions * jointAction]);

        // are you sure you did everything you wanted here?
        //you never use the values from  double[] F_neighbor
        for (Agent neighbor : m_neighbors) {
            double[] F_neighbor = m_F.get(neighbor);
            for (int i = 0; i < neighbor.m_actions; ++i) {
                F_neighbor[i] *= ((double) m_updates) / (m_updates + 1);
            }
            F_neighbor[neighbor.m_last_action] += 1.0 / (m_updates + 1);
        }

        m_updates += 1;
    }

    private int jointNeighborActions() {
        int jointActions = 1;
        for (Agent neighbor : m_neighbors) {
            jointActions *= neighbor.m_actions;
        }
        return jointActions;
    }

    private double[] estimatedValues() {
        double[] EV = new double[m_actions];
        for (int i = 0; i < EV.length; ++i) {
            EV[i] = 0.0;
        }

        int jointActions = jointNeighborActions();
        for (int i = 0; i < jointActions; ++i) {
            int index = i;
            double probability = 1.0;
            for (Agent neighbor : m_neighbors) {
                double[] F_neighbor = m_F.get(neighbor);
                probability *= F_neighbor[index % F_neighbor.length];
                index /= F_neighbor.length;
            }

            for (int j = 0; j < m_actions; j++) {
                EV[j] += m_Q[j + m_actions * i] * probability;
            }
        }

        return EV;
    }
}
