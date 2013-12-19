package ljal;

public class LocalJointActionLearners {
	
	public LocalJointActionLearners(int[][] coordinationGraph, int actions, SelectionAlgorithm selection, double alpha) {
		m_coordinationGraph = coordinationGraph;
		m_actions = actions;
		m_selection = selection;
		m_alpha = alpha;
	}
	
	public double[] learn(double[] rewards, int rounds) {
		Agent[] agents = createAgents();
		
		double[] collected_rewards = new double[rounds];
		for (int t = 0; t < rounds; ++t) {
			int[] actions = new int[agents.length];
			for (int i = 0; i < agents.length; ++i) {
				actions[i] = agents[i].selectAction(t);
			}
			
			double reward = rewards[new JointAction(agents, actions).getIndex()];
			collected_rewards[t] = reward;
			
			for (Agent agent : agents) {
				agent.update(reward);
			}
		}
		
		return collected_rewards;
	}
	
	private Agent[] createAgents() {
		Agent[] agents = new Agent[m_coordinationGraph.length];
		for (int i = 0; i < agents.length; ++i) {
			agents[i] = new Agent(m_actions, m_selection, m_alpha);
		}
		
		for (int i = 0; i < agents.length; ++i) {
			Agent[] neighbors = new Agent[m_coordinationGraph[i].length];
			for (int j = 0; j < neighbors.length; ++j) {
				neighbors[j] = agents[m_coordinationGraph[i][j]];
			}
			
			agents[i].setNeighbors(neighbors);
		}
		
		return agents;
	}
	
	private int[][] m_coordinationGraph;
	private int m_actions;
	private SelectionAlgorithm m_selection;
	private double m_alpha;
	
}
