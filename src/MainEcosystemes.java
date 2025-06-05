import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.io.File;
import java.util.*;

public class MainEcosystemes {

    public static void main(String[] args) throws Exception {
        // 1. Paramètres
        String inputImage = "Planete_1.jpg";
        int nBiomes = 6; // À ajuster selon l'image
        double eps = 2.0; // Distance DBSCAN (à tester)
        int minPts = 4;   // DBSCAN (à tester)

        // 2. Flouter l'image (5x5 moyenne)
        BufferedImage img = ImageIO.read(new File(inputImage));

        // Redimensionner l'image à 500x500
        int targetWidth = 500, targetHeight = 500;
        BufferedImage resizedImg = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        resizedImg.getGraphics().drawImage(
            img.getScaledInstance(targetWidth, targetHeight, java.awt.Image.SCALE_SMOOTH),
            0, 0, null
        );

        double[][] mean5x5 = ImageConvolution.meanKernel(5);
        BufferedImage blurImg = ImageConvolution.applyFilter(resizedImg, mean5x5);

        // 3. Appliquer KMeans sur les couleurs (espace RGB)
        int width = blurImg.getWidth(), height = blurImg.getHeight();
        double[][] pixels = new double[width * height][3];
        for (int y = 0; y < height; y++)
            for (int x = 0; x < width; x++) {
                int rgb = blurImg.getRGB(x, y);
                pixels[y * width + x][0] = (rgb >> 16) & 0xFF;
                pixels[y * width + x][1] = (rgb >> 8) & 0xFF;
                pixels[y * width + x][2] = rgb & 0xFF;
            }

        KMeansClustering kmeans = new KMeansClustering();
        int[] biomeLabels = kmeans.cluster(pixels, nBiomes);

        // 4. Créer la matrice de labels par pixel
        int[][] labels = new int[height][width];
        for (int y = 0; y < height; y++)
            for (int x = 0; x < width; x++)
                labels[y][x] = biomeLabels[y * width + x];

        // 5. Pour chaque biome, appliquer DBSCAN spatial et générer l’image des écosystèmes
        for (int biomeId = 0; biomeId < nBiomes; biomeId++) {
            // a. Extraire les pixels du biome
            List<int[]> coordsList = new ArrayList<>();
            for (int y = 0; y < height; y++)
                for (int x = 0; x < width; x++)
                    if (labels[y][x] == biomeId)
                        coordsList.add(new int[]{x, y});

            if (coordsList.isEmpty()) continue;

            int[][] coords = coordsList.toArray(new int[coordsList.size()][]);

            // b. Appliquer DBSCAN sur les positions (x, y)
            int[] ecoLabels = DBSCAN2D.cluster(coords, eps, minPts);

            // c. Générer une palette de couleurs (différentes par écosystème)
            int maxEco = Arrays.stream(ecoLabels).max().orElse(0);
            Color[] ecoColors = new Color[maxEco + 2];
            Random rand = new Random(biomeId + 42); // pour reproductibilité
            for (int i = 0; i < ecoColors.length; i++)
                ecoColors[i] = new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));

            // d. Créer l'image à afficher
            // On part d'une version blanchie de l'image floutée pour le fond
            BufferedImage ecoImg = ImageUtils.rendreFondClair(blurImg, 0.75);

            // e. Colorier chaque écosystème d'une couleur différente
            for (int i = 0; i < coords.length; i++) {
                int x = coords[i][0], y = coords[i][1];
                int eco = ecoLabels[i];
                if (eco > 0)
                    ecoImg.setRGB(x, y, ecoColors[eco].getRGB());
            }

            // f. Sauvegarder l'image pour ce biome
            String outName = String.format("Ecosystemes_biome%d.png", biomeId);
            ImageIO.write(ecoImg, "PNG", new File(outName));
            System.out.println("Image sauvegardée : " + outName);
        }
    }
}