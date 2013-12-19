package ljal.experiments;

import java.util.Arrays;

import ljal.CoordinationGraphs;
import ljal.LocalJointActionLearners;
import ljal.SelectionAlgorithm;
import ljal.Softmax;

public class Experiment1 {

    public static void main(String[] args) {
        int agents = 5;
        int actions = 4;
        SelectionAlgorithm selection = new Softmax(ExperimentUtils.adaptiveTau);
        double alpha = 0.1;
        double[] rewards = ExperimentUtils.randomRewards(agents, (int) Math.pow(actions, agents));
        int rounds = 200;

        double[] sortedRewards = Arrays.copyOf(rewards, rewards.length);
        Arrays.sort(sortedRewards);

        System.out.print("Top rewards - ");
        for (int i = 0; i < 20; ++i) {
            if (i > 0) {
                System.out.print(", ");
            }
            System.out.print(sortedRewards[sortedRewards.length - i - 1]);
        }
        System.out.println();

        LocalJointActionLearners il = new LocalJointActionLearners(CoordinationGraphs.independentLearners(agents), actions, selection, alpha);
        double[] ilRewards = il.learn(rewards, rounds);
        System.out.print("Independent learners - ");
        System.out.println(ilRewards[ilRewards.length - 1]);

        LocalJointActionLearners jal = new LocalJointActionLearners(CoordinationGraphs.jointActionLearners(agents), actions, selection, alpha);
        double[] jalRewards = jal.learn(rewards, rounds);
        System.out.print("Joint action learners - ");
        System.out.println(jalRewards[jalRewards.length - 1]);
    }
}
