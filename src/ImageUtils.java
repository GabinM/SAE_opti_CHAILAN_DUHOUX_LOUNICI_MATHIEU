import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageUtils {

    /**
     * Méthode permettant de charger une image
     * @param path le fichier d'entrée
     * @return l'objet image
     */
    public static BufferedImage loadImage(String path){
        try {
            return ImageIO.read(new File(path));
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de l'image : " + path);
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Méthode permettant de save une image dans un fichier
     * @param img
     * @param path
     */
    public static void saveImage(BufferedImage img, String path) {
        try {
            ImageIO.write(img, "png", new File(path));
        } catch (IOException e) {
            System.out.println("Erreur d'écriture de l'image : " + e.getMessage());
        }
    }


    /**
     * Retourne une version clair de l'image en appliquant un filtre de luminosité.
     *
     * @param img         L'image d'origine
     * @param pourcentage Pourcentage d'éclaircissement (ex : 0.75 pour 75%)
     * @return Nouvelle image éclaircie
     */
    public static BufferedImage rendreFondClair(BufferedImage img, double pourcentage) {
        int width = img.getWidth();
        int height = img.getHeight();
        BufferedImage res = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Color c = new Color(img.getRGB(i, j));
                int r = (int) Math.round(c.getRed() + pourcentage * (255 - c.getRed()));
                int g = (int) Math.round(c.getGreen() + pourcentage * (255 - c.getGreen()));
                int b = (int) Math.round(c.getBlue() + pourcentage * (255 - c.getBlue()));

                Color bright = new Color(Math.min(r, 255), Math.min(g, 255), Math.min(b, 255));

                res.setRGB(i, j, bright.getRGB());
            }
        }
        return res;
    }


}
