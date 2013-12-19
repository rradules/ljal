package ljal;

import java.util.Iterator;

/**
 * Utility class to abstract the internals of mapping a joint action to an index
 */
public class JointActions implements Iterable<JointAction> {
	
	/**
	 * Constructor
	 * 
	 * @param agents The agents of which to combine the actions
	 */
	public JointActions(Agent[] agents) {
		m_agents = agents;
		m_count = 1;
		for (Agent agent : agents) {
			m_count *= agent.getActions();
		}
	}
	
	/**
	 * Returns the number of joint actions for the agents 
	 * 
	 * @return The number of action
	 */
	public int getCount() {
		return m_count;
	}
	
	/**
	 * Returns a new iterator over all the joint actions
	 * 
	 * @return A new read-only iterator, that doesn't support removal
	 */
	@Override
	public Iterator<JointAction> iterator() {
		return new JointActionsIterator();
	}
	
	private class JointActionsIterator implements Iterator<JointAction> {
		
		public JointActionsIterator() {
			m_current = 0;
		}

		@Override
		public boolean hasNext() {
			return m_current < m_count;
		}

		@Override
		public JointAction next() {
			return new JointAction(m_agents, m_current++);
		}
		
		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
		
		private int m_current;
	}
	
	private Agent[] m_agents;
	private int m_count;
}
