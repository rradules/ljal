package ljal;

class LocalJointActionLearners {

    public double[] ljal(Agent[] agents, double[] rewards, int rounds) {
        double[] collected_rewards = new double[rounds];
        for (int t = 0; t < rounds; ++t) {
            int jointAction = 0;
            int tmp = 1;
            for (Agent agent : agents) {
                jointAction += tmp * agent.selectAction(t);
                tmp *= agent.getActions();
            }

            double reward = rewards[jointAction];
            collected_rewards[t] = reward;

            for (Agent agent : agents) {
                agent.update(reward);
            }
        }

        return collected_rewards;
    }

}
