package ljal;

/**
 * Utility class to help creating coordination graphs
 */
public class CoordinationGraphs {

    public static int[][] independentLearners(int agents) {
        return new int[agents][0];
    }

    public static int[][] jointActionLearners(int agents) {
        int[][] graph = new int[agents][agents - 1];
        for (int i = 0; i < agents; ++i) {
            for (int j = 0; j < agents - 1; ++j) {
                graph[i][j] = j < i ? j : j + 1;
            }
        }
        return graph;
    }
}
