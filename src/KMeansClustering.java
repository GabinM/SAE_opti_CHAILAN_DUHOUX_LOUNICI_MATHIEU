import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;

public class KMeansClustering {
    private Random rand = new Random();

    // Extraction des couleurs RGB de l'image
    public static double[][] extractPixels(String filename) throws Exception {
        BufferedImage img = ImageIO.read(new File(filename));
        int width = img.getWidth();
        int height = img.getHeight();
        double[][] pixels = new double[width * height][3];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = img.getRGB(x, y);
                pixels[y * width + x][0] = (rgb >> 16) & 0xFF; // R
                pixels[y * width + x][1] = (rgb >> 8) & 0xFF;  // G
                pixels[y * width + x][2] = rgb & 0xFF;         // B
            }
        }
        return pixels;
    }

    // KMeans clustering
    public int[] cluster(double[][] data, int nClusters) {
        int n = data.length;
        int dim = data[0].length;
        double[][] centroids = new double[nClusters][dim];

        // Initialisation: choisir des points aléatoires comme centroïdes
        Set<Integer> used = new HashSet<>();
        for (int i = 0; i < nClusters; i++) {
            int idx;
            do {
                idx = rand.nextInt(n);
            } while (used.contains(idx));
            used.add(idx);
            centroids[i] = Arrays.copyOf(data[idx], dim);
        }

        int[] labels = new int[n];
        boolean changed;
        do {
            changed = false;
            // Affectation à le centroïde le plus proche
            for (int i = 0; i < n; i++) {
                int best = -1;
                double bestDist = Double.MAX_VALUE;
                for (int j = 0; j < nClusters; j++) {
                    double dist = distance(data[i], centroids[j]);
                    if (dist < bestDist) {
                        bestDist = dist;
                        best = j;
                    }
                }
                if (labels[i] != best) {
                    changed = true;
                    labels[i] = best;
                }
            }
            // Mise à jour des centroïdes
            double[][] sums = new double[nClusters][dim];
            int[] counts = new int[nClusters];
            for (int i = 0; i < n; i++) {
                for (int d = 0; d < dim; d++)
                    sums[labels[i]][d] += data[i][d];
                counts[labels[i]]++;
            }
            for (int j = 0; j < nClusters; j++) {
                if (counts[j] > 0) {
                    for (int d = 0; d < dim; d++)
                        centroids[j][d] = sums[j][d] / counts[j];
                }
            }
        } while (changed);

        return labels;
    }

    private double distance(double[] a, double[] b) {
        double s = 0;
        for (int i = 0; i < a.length; i++)
            s += (a[i] - b[i]) * (a[i] - b[i]);
        return Math.sqrt(s);
    }

    public static void segmentImage(String inputFile, String outputFile, int nClusters) throws Exception {
        BufferedImage img = ImageIO.read(new File(inputFile));
        int width = img.getWidth();
        int height = img.getHeight();
        double[][] pixels = extractPixels(inputFile);

        KMeansClustering kmeans = new KMeansClustering();
        int[] labels = kmeans.cluster(pixels, nClusters);

        // Calcul de la couleur moyenne pour chaque cluster
        double[][] sum = new double[nClusters][3];
        int[] count = new int[nClusters];
        for (int i = 0; i < pixels.length; i++) {
            int cluster = labels[i];
            for (int c = 0; c < 3; c++)
                sum[cluster][c] += pixels[i][c];
            count[cluster]++;
        }

        int[][] meanColor = new int[nClusters][3];
        for (int k = 0; k < nClusters; k++)
            for (int c = 0; c < 3; c++)
                meanColor[k][c] = count[k] == 0 ? 0 : (int) Math.round(sum[k][c] / count[k]);

        // Création de l’image segmentée
        BufferedImage clustered = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int cluster = labels[y * width + x];
                int rgb = (meanColor[cluster][0] << 16) | (meanColor[cluster][1] << 8) | meanColor[cluster][2];
                clustered.setRGB(x, y, rgb);
            }
        }
        ImageIO.write(clustered, "png", new File(outputFile));
    }

    // Exemple d'utilisation
    public static void main(String[] args) throws Exception {
        segmentImage("Planete1_blur_mean5x5.png", "Planete1_biomes.png", 5);
    }
}