import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class BlurTest {
    public static void main(String[] args) throws Exception {
        for (int i = 1; i <= 5; i++) {
            BufferedImage img = ImageIO.read(new File("Ex/Planete " + i + ".jpg"));

            // // Flou par moyenne 3x3
            // double[][] mean3x3 = ImageConvolution.meanKernel(3);
            // BufferedImage blurMean3 = ImageConvolution.applyFilter(img, mean3x3);

            // Flou par moyenne 5x5
            double[][] mean5x5 = ImageConvolution.meanKernel(5);
            BufferedImage blurMean5 = ImageConvolution.applyFilter(img, mean5x5);

            // // Flou par moyenne 7x7
            // double[][] mean7x7 = ImageConvolution.meanKernel(7);
            // BufferedImage blurMean7 = ImageConvolution.applyFilter(img, mean7x7);

            // // Flou gaussien 3x3
            // double[][] gauss3x3 = ImageConvolution.gauss3x3();
            // BufferedImage blurGauss3 = ImageConvolution.applyFilter(img, gauss3x3);

            // // Flou gaussien 5x5
            // double[][] gauss5x5 = ImageConvolution.gauss5x5();
            // BufferedImage blurGauss5 = ImageConvolution.applyFilter(img, gauss5x5);

            // // Flou gaussien 7x7
            // double[][] gauss7x7 = ImageConvolution.gauss7x7();
            // BufferedImage blurGauss7 = ImageConvolution.applyFilter(img, gauss7x7);

            // Répéter le flou gaussien 5x5 pour accentuer l'effet
            // BufferedImage blurGauss5_twice = ImageConvolution.applyFilter(blurGauss5, gauss5x5);

            // Répéter le flou gaussien 7x7 pour accentuer l'effet
            // BufferedImage blurGauss7_twice = ImageConvolution.applyFilter(blurGauss7, gauss7x7);

            // ImageIO.write(blurMean3, "png", new File("Planete" + i + "_blur_mean3x3.png"));
            ImageIO.write(blurMean5, "png", new File("Planete" + i + "_blur_mean5x5.png"));
            // ImageIO.write(blurMean7, "png", new File("Planete" + i + "_blur_mean7x7.png"));
            // ImageIO.write(blurGauss3, "png", new File("Planete" + i + "_blur_gauss3x3.png"));
            // ImageIO.write(blurGauss5, "png", new File("Planete" + i + "_blur_gauss5x5.png"));
            // ImageIO.write(blurGauss7, "png", new File("Planete" + i + "_blur_gauss7x7.png"));
            // ImageIO.write(blurGauss5_twice, "png", new File("Planete" + i + "_blur_gauss5x5_2pass.png"));
            // ImageIO.write(blurGauss7_twice, "png", new File("Planete" + i + "_blur_gauss7x7_2pass.png"));
        }
    }
}
