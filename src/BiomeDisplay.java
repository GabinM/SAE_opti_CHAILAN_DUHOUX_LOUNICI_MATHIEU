import java.awt.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class BiomeDisplay {
    public static void imageToPalette(String path, String newPath, Color[] palette) throws IOException {
        System.out.println("taille de la palette d'entree : " + palette.length);
        BufferedImage img = ImageIO.read(new File(path));
        for (int i = 0; i < img.getWidth(); i++) {
            for (int j = 0; j < img.getHeight(); j++) {
                Color c = OutilCouleur.getPlusProche(new Color(img.getRGB(i, j)), palette);
                img.setRGB(i, j, c.getRGB());
            }
        }

        ImageIO.write(img, "png", new File(newPath));

    }
}