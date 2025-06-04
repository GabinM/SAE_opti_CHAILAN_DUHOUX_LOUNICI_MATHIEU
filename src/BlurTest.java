import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class BlurTest {
    public static void main(String[] args) throws Exception {
        // Boucle sur plusieurs images comme avant
        for (int i = 1; i <= 5; i++) {
            BufferedImage img = ImageIO.read(new File("Ex/Planete " + i + ".jpg"));

            // Flou par moyenne 5x5
            double[][] mean5x5 = ImageConvolution.meanKernel(5);
            BufferedImage blurMean5 = ImageConvolution.applyFilter(img, mean5x5);

            // Flou gaussien 5x5
            double[][] gauss5x5 = ImageConvolution.gauss5x5();
            BufferedImage blurGauss5 = ImageConvolution.applyFilter(img, gauss5x5);

            // Répéter le flou gaussien pour accentuer l'effet
            BufferedImage blurGauss5_twice = ImageConvolution.applyFilter(blurGauss5, gauss5x5);

            ImageIO.write(blurMean5, "png", new File("Planete" + i + "_blur_mean5x5.png"));
            ImageIO.write(blurGauss5, "png", new File("Planete" + i + "_blur_gauss5x5.png"));
            ImageIO.write(blurGauss5_twice, "png", new File("Planete" + i + "_blur_gauss5x5_2pass.png"));
        }
    }
}
