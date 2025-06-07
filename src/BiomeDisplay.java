import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class BiomeDisplay {
    public static BufferedImage imageToPalette(BufferedImage img, PaletteData palette) throws IOException {
        Color[] colors = new Color[palette.getPalette().size()];
        for (int i = 0; i < colors.length; i++) {
            colors[i] = palette.getPalette().get(i).getColor();
        }
        for (int i = 0; i < img.getWidth(); i++) {
            for (int j = 0; j < img.getHeight(); j++) {
                Color c = OutilCouleur.getPlusProche(new Color(img.getRGB(i, j)), colors);
                img.setRGB(i, j, c.getRGB());
            }
        }

        return img;

    }
}