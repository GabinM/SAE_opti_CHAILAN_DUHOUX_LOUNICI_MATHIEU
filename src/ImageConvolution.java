import java.awt.image.BufferedImage;

public class ImageConvolution {

    public static BufferedImage applyFilter(BufferedImage src, double[][] kernel) {
        int width = src.getWidth();
        int height = src.getHeight();
        int kSize = kernel.length;
        int kOffset = kSize / 2;
        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double[] sum = {0, 0, 0};

                for (int ky = 0; ky < kSize; ky++) {
                    for (int kx = 0; kx < kSize; kx++) {
                        int px = Math.min(Math.max(x + kx - kOffset, 0), width - 1);
                        int py = Math.min(Math.max(y + ky - kOffset, 0), height - 1);
                        int rgb = src.getRGB(px, py);

                        sum[0] += ((rgb >> 16) & 0xFF) * kernel[ky][kx]; // R
                        sum[1] += ((rgb >> 8) & 0xFF) * kernel[ky][kx];  // G
                        sum[2] += (rgb & 0xFF) * kernel[ky][kx];         // B
                    }
                }
                int r = Math.min(255, Math.max(0, (int)Math.round(sum[0])));
                int g = Math.min(255, Math.max(0, (int)Math.round(sum[1])));
                int b = Math.min(255, Math.max(0, (int)Math.round(sum[2])));
                result.setRGB(x, y, (r << 16) | (g << 8) | b);
            }
        }
        return result;
    }

    // Flou par moyenne 3x3
    public static double[][] mean3x3() {
        double[][] kernel = new double[3][3];
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                kernel[i][j] = 1.0 / 9.0;
        return kernel;
    }

    // Filtre gaussien 3x3 (σ ≈ 1.0)
    public static double[][] gauss3x3() {
        return new double[][] {
            {1/16.0, 2/16.0, 1/16.0},
            {2/16.0, 4/16.0, 2/16.0},
            {1/16.0, 2/16.0, 1/16.0}
        };
    }

    // Flou par moyenne de taille variable
    public static double[][] meanKernel(int size) {
        double[][] kernel = new double[size][size];
        double val = 1.0 / (size * size);
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                kernel[i][j] = val;
        return kernel;
    }

    // Filtre gaussien 5x5 (σ ≈ 1), à normaliser avant usage
    public static double[][] gauss5x5() {
        double[][] kernel = {
            {1,  4,  6,  4, 1},
            {4, 16, 24, 16, 4},
            {6, 24, 36, 24, 6},
            {4, 16, 24, 16, 4},
            {1,  4,  6,  4, 1}
        };
        return normalizeKernel(kernel);
    }

    // Normalisation d'un noyau
    public static double[][] normalizeKernel(double[][] kernel) {
        double sum = 0;
        for (double[] row : kernel)
            for (double v : row) sum += v;
        double[][] out = new double[kernel.length][kernel[0].length];
        for (int i = 0; i < kernel.length; i++)
            for (int j = 0; j < kernel[0].length; j++)
                out[i][j] = kernel[i][j] / sum;
        return out;
    }
}
