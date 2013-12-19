package ljal.experiments;

import java.util.Random;

import ljal.AdaptiveParameter;

/**
 * Utilities to set up the experiments
 */
public class ExperimentUtils {

    static public AdaptiveParameter adaptiveTau = new AdaptiveParameter() {
        @Override
        public double at(int t) {
            return 1000 * Math.pow(0.94, t);
        }
    };

    static public double[] randomRewards(int agents, int jointActions) {
        Random random = new Random();

        double[] rewards = new double[jointActions];
        for (int i = 0; i < jointActions; ++i) {
            rewards[i] = random.nextGaussian() * 10 * agents;
        }
        return rewards;
    }
}
