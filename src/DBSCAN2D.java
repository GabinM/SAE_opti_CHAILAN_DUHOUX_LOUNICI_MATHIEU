import java.util.*;

public class DBSCAN2D {

    public static int[] cluster(int[][] coords, double eps, int minPts) {
        int n = coords.length;
        int[] labels = new int[n];
        Arrays.fill(labels, -1); // -1 = bruit

        int clusterId = 0;
        boolean[] visited = new boolean[n];

        for (int i = 0; i < n; i++) {
            if (visited[i]) continue;
            visited[i] = true;

            List<Integer> neighbors = regionQuery(coords, i, eps);

            if (neighbors.size() < minPts) {
                labels[i] = -1; // bruit
            } else {
                clusterId++;
                expandCluster(coords, labels, visited, i, neighbors, clusterId, eps, minPts);
            }
        }
        return labels;
    }

    private static void expandCluster(int[][] coords, int[] labels, boolean[] visited,
                                      int idx, List<Integer> neighbors, int clusterId, double eps, int minPts) {
        labels[idx] = clusterId;
        Queue<Integer> queue = new LinkedList<>(neighbors);
        while (!queue.isEmpty()) {
            int j = queue.poll();
            if (!visited[j]) {
                visited[j] = true;
                List<Integer> neigh2 = regionQuery(coords, j, eps);
                if (neigh2.size() >= minPts) {
                    queue.addAll(neigh2);
                }
            }
            if (labels[j] == -1) {
                labels[j] = clusterId;
            }
        }
    }

    private static List<Integer> regionQuery(int[][] coords, int idx, double eps) {
        List<Integer> neighbors = new ArrayList<>();
        for (int i = 0; i < coords.length; i++) {
            if (distance(coords[idx], coords[i]) <= eps) {
                neighbors.add(i);
            }
        }
        return neighbors;
    }

    private static double distance(int[] a, int[] b) {
        int dx = a[0] - b[0], dy = a[1] - b[1];
        return Math.sqrt(dx * dx + dy * dy);
    }
}
