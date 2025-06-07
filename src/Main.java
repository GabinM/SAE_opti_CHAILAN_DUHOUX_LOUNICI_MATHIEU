import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.*;
import java.util.HashMap;
import java.util.Random;
import java.util.random.*;

import javax.imageio.ImageIO;

public class Main {

    // Soit c'est dans kmeans qui détecte mal les couleurs
    // (parce que c'est en lab ?)
    // soit c'est dbscan ? ca serait étonnant

    public static void main(String[] args) throws IOException {

        BufferedImage img = ImageIO.read(new File(args[0]));

        // Redimensionner l'image à 25% de sa taille originale
        int newWidth = img.getWidth() / 4;
        int newHeight = img.getHeight() / 4;
        BufferedImage resizedImg = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = resizedImg.createGraphics();
        g2d.drawImage(img.getScaledInstance(newWidth, newHeight, java.awt.Image.SCALE_SMOOTH), 0, 0, null);
        g2d.dispose();

        double[][] mean5x5 = ImageConvolution.meanKernel(5);
        BufferedImage blurImg = ImageConvolution.applyFilter(resizedImg, mean5x5);

        PaletteData palette = AlgoKMeans.getPalette(blurImg, 6);
        OutilCouleur.getLabels(palette, OutilCouleur.DEFAULT_LABELS);

        BufferedImage noiseReducedImg = BiomeDisplay.imageToPalette(blurImg, palette);
        ImageIO.write(noiseReducedImg, "png", new File("test_rendu.png"));
        BufferedImage clearImage = ImageUtils.rendreFondClair(noiseReducedImg, 0.75);

        for (PaletteElement p : palette.getPalette()) {
            ImageIO.write(highlightEcosystems(noiseReducedImg, p, clearImage), "png",
                    new File("rendu_" + p.getName() + ".png"));
        }

    }

    public static BufferedImage highlightEcosystems(BufferedImage img, PaletteElement pal, BufferedImage placeHolder) {
        int[][] ecosystems = DBSCAN2D.cluster(img,
                pal.getColor(), 2, 4);

        BufferedImage newImg = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);

        HashMap<Integer, Color> colors = new HashMap<>();
        Random rand = new Random();
        System.out.println("analyse de : " + pal.getName());
        for (int i = 0; i < ecosystems.length; i++) {
            for (int j = 0; j < ecosystems[0].length; j++) {
                int idCluster = ecosystems[i][j];
                if (idCluster > 0) {
                    if (!colors.containsKey(idCluster)) {
                        colors.put(idCluster, new Color(rand.nextInt(256), rand.nextInt(256),
                                rand.nextInt(256)));
                    }
                    newImg.setRGB(i, j, colors.get(idCluster).getRGB());
                    // clearImage.setRGB(i, j, Color.WHITE.getRGB());
                } else {
                    newImg.setRGB(i, j, placeHolder.getRGB(i, j));
                } /*
                   * else {
                   * clearImage.setRGB(i, j, Color.BLACK.getRGB());
                   * }
                   */

            }
        }

        return newImg;
        // ImageIO.write(clearImage, "png", new File("test.png"));
    }

}
