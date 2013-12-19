package ljal;

/**
 * Class representing a joint action of multiple agents
 */
public class JointAction {
	
	public JointAction(Agent[] agents, int[] actions) {
		m_agents = agents;
		m_actions = actions;
		
		indexFromActions();
	}
	
	public JointAction(Agent[] agents, int index) {
		m_agents = agents;
		m_index = index;
		
		actionsFromIndex();
	}
	
	/**
	 * Gets the separate actions
	 * 
	 * @return The composite joint action
	 */
	public int[] getActions() {
		return m_actions;
	}
	
	/**
	 * Gets the global index of this action over all possible joint actions over the agents
	 * 
	 * @return The index of the joint action
	 */
	public int getIndex() {
		return m_index;
	}
	
	/**
	 * Returns the index of the joint action with the supplied action prepended
	 * 
	 * @param agent The agent of which to prepended an action
	 * @param action The action to be prepended
	 * @return The index of the joint action with the new action prepended
	 */
	public int prependedActionIndex(Agent agent, int action) {
		return action + agent.getActions() * m_index;
	}
	
	private void indexFromActions() {
		m_index = 0;
		int offset = 1;
		for (int i = 0; i < m_agents.length; ++i) {
			m_index += offset * m_actions[i];
			offset *= m_agents[i].getActions(); 
		}
	}
	
	private void actionsFromIndex() {
		m_actions = new int[m_agents.length];
		
		int index = m_index;
		for (int i = 0; i < m_agents.length; ++i) {
			int agentActions = m_agents[i].getActions();
			m_actions[i] = index % agentActions;
			index /= agentActions;
		}
	}
	
	private Agent[] m_agents;
	private int[] m_actions;
	private int m_index;
	
}
