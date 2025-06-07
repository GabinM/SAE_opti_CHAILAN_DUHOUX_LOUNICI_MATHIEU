import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.*;

import javax.management.RuntimeErrorException;

//là faut faire en sorte que la zone recherchée soit plus petite
//au lieu de rechercher la distance entre tous les points de l'image, juste rechercher ceux
//qui sont dans une certaine zone (dans le for, mettre la position de la couleur + delta)

public class DBSCAN2D {

    // eps : rayon de détection pour chaque point
    public static int[][] cluster(BufferedImage img, Color c, double eps, int minPts) {
        int width = img.getWidth();
        int height = img.getHeight();
        int clusterId = 1;
        int[][] visited = new int[width][height];
        // 0 : pas visité, -1 : visité mais est un bruit

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (visited[i][j] != 0 || img.getRGB(i, j) != c.getRGB())
                    continue;
                visited[i][j] = clusterId; // 1 normalement

                List<int[]> neighbors = regionQuery(img, i, j, eps, c);

                if (neighbors.size() < minPts) {
                    visited[i][j] = -1; // bruit
                } else {
                    clusterId++;
                    expandCluster(img, visited, i, j, neighbors, clusterId, eps, minPts, c);
                }
            }

        }
        System.out.println("nombre de clusters : " + clusterId);
        return visited;
    }

    private static void expandCluster(BufferedImage img, int[][] visited,
            int x, int y, List<int[]> neighbors, int clusterId, double eps, int minPts, Color c) {
        visited[x][y] = clusterId;
        Queue<int[]> queue = new LinkedList<>(neighbors);
        while (!queue.isEmpty()) {
            int[] loc = queue.poll();
            int val = visited[loc[0]][loc[1]];
            if (val == 0) {
                visited[loc[0]][loc[1]] = clusterId;
                List<int[]> neigh2 = regionQuery(img, loc[0], loc[1], eps, c);
                if (neigh2.size() >= minPts) {
                    queue.addAll(neigh2);
                }
            } else if (val == -1) {
                visited[loc[0]][loc[1]] = clusterId;
            }
        }
    }

    private static List<int[]> regionQuery(BufferedImage img, int x, int y, double eps, Color c) {
        List<int[]> neighbors = new ArrayList<>();
        int intRadius = (int) Math.round(eps);
        int minX = Math.max(0, x - intRadius);
        int minY = Math.max(0, y - intRadius);
        int maxX = Math.min(img.getWidth(), x + intRadius);
        int maxY = Math.min(img.getHeight(), y + intRadius);
        for (int i = minX; i < maxX; i++) {
            for (int j = minY; j < maxY; j++) {
                /*
                 * if (distance(coords[idx], coords[i]) <= eps) {
                 * neighbors.add(i);
                 */
                if (img.getRGB(i, j) == c.getRGB()) {
                    neighbors.add(new int[] { i, j });
                }

            }

        }
        return neighbors;

    }

    private static double distance(int[] a, int[] b) {
        int dx = a[0] - b[0], dy = a[1] - b[1];
        return Math.sqrt(dx * dx + dy * dy);
    }
}
