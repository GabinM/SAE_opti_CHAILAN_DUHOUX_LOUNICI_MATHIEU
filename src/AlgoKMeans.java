import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.Random;

//En gros, faudrait passer le rgb en lab pour comparer les valeurs minimales et maximales ( si ca marche)

public class AlgoKMeans {

    public final static int MAX_LOOPS = 100;

    public static PaletteData getPalette(String path, int nbCouleurs) {
        BufferedImage img;
        try {
            img = ImageIO.read(new File(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return getPalette(img, nbCouleurs);
    }

    public static PaletteData getPalette(BufferedImage img, int nbCouleurs) {
        PaletteData paletteD = new PaletteData();
        Random rand = new Random();
        Norme94 norme = new Norme94();
        double[][] paletteLab = new double[nbCouleurs][3];
        HashSet<double[]>[] centroids = new HashSet[1];
        // il faut une HashMap, sinon le HashSet va utiliser la ref des listes et
        // ca va mettre autant de couleurs que de pixels ( ce qui est faux)
        HashMap<Integer, double[]> allColorsWithLab = new HashMap<>();

        // ici faut utiliser des HashSet (car un seul même élément peut être présent,
        // donc pas besoin de re-vérifier)
        for (int i = 0; i < img.getWidth(); i++) {
            for (int j = 0; j < img.getHeight(); j++) {
                int c = img.getRGB(i, j);
                int[] temp = OutilCouleur.getTabColor(c);
                // int[] lab = RgbToLab.rgb2lab(temp[0], temp[1], temp[2]);
                double[] lab = Colors.convertRGB_to_Lab(temp);
                allColorsWithLab.put(c, lab);
                // allColorsWithLab.compute(c, (_, temp) -> temp);
            }
        }
        System.out.println("nombre total de couleurs : " + allColorsWithLab.keySet().size());

        for (int i = 0; i < nbCouleurs; i++) {
            // int index = rand.nextInt(allColors.size());
            // techniquement, c'est mieux de prendre une couleur de l'image au hasard que de
            // prendre une couleur vraiment au hasard
            // (il faudrait plus de tours pour arriver à un état stable, alors qu'en prenant
            // une couleur existante on a des chances
            // de déjà être proche d'un cluster)
            Color temp = new Color(img.getRGB(rand.nextInt(img.getWidth()), rand.nextInt(img.getHeight())));
            paletteLab[i] = Colors.convertRGB_to_Lab(OutilCouleur.getTabColor(temp.getRGB()));
        }

        boolean isFinished = false;
        int loop = 0;
        while (!isFinished && loop < MAX_LOOPS) {

            isFinished = true;

            centroids = new HashSet[nbCouleurs];
            for (int i = 0; i < nbCouleurs; i++) {
                centroids[i] = new HashSet<>();
            }

            for (int c : allColorsWithLab.keySet()) {
                int index = getPlusProche(paletteLab, allColorsWithLab.get(c), norme);
                // Integer[] temp = OutilCouleur.getTabColor(c);
                double[] temp = allColorsWithLab.get(c);
                centroids[index].add(new double[] { temp[0], temp[1], temp[2] });
            }

            for (int i = 0; i < nbCouleurs; i++) {

                if (centroids[i].size() > 1) {

                    double[] finalColor = getColor(centroids[i]);
                    DecimalFormat df = new DecimalFormat("###.##");
                    // System.out.println("l : "+finalColor[0] + " - "+ paletteLab[i][0]);
                    // System.out.println("a : "+finalColor[1] + " - "+ paletteLab[i][1]);
                    // System.out.println("b : "+finalColor[2] + " - "+ paletteLab[i][2]);
                    if (!(Objects.equals(df.format(finalColor[0]), df.format(paletteLab[i][0])))
                            && !(Objects.equals(df.format(finalColor[1]), df.format(paletteLab[i][1])))
                            && !(Objects.equals(df.format(finalColor[2]), df.format(paletteLab[i][2])))) {

                        isFinished = false;
                        paletteLab[i] = finalColor;
                    }
                }

            }

            loop++;
        }
        System.out.println("placement des centroïdes effectué en " + (loop + 1) + " tours");
        for (int i = 0; i < nbCouleurs; i++) {
            double[] d = { paletteLab[i][0], paletteLab[i][1], paletteLab[i][2] };
            int[] rgb = Colors.convertLab_to_RGB(d);
            // System.out.print("lab : "+paletteLab[i][0]+ ", "+paletteLab[i][1]+",
            // "+paletteLab[i][0] );
            // System.out.println(" ; rgb : "+rgb[0]+", "+rgb[1]+", "+rgb[2]);
            PaletteElement pal = new PaletteElement();
            pal.setLAB(d);
            pal.setColor(new Color(rgb[0], rgb[1], rgb[2]));
            ArrayList<int[]> associatedColors = new ArrayList<>();
            for (double[] lab : centroids[i]) {
                associatedColors.add(Colors.convertLab_to_RGB(lab));
            }
            paletteD.getPalette().add(pal);
        }
        return paletteD;
    }

    private static double[] getColor(HashSet<double[]> centroids) {
        int nbColors = 0; // ou 1

        double l = 0;
        double a = 0;
        double b = 0;

        for (double[] arr : centroids) {
            l += arr[0];
            a += arr[1];
            b += arr[2];
            nbColors++;
        }
        return new double[] { (l / nbColors), (a / nbColors), (b / nbColors) };
    }

    public static int getPlusProche(double[][] p, double[] c, Norme94 n) {
        int nearest = 0;
        double minDist = n.distanceCouleur(p[0], c);
        for (int k = 1; k < p.length; k++) {
            double newDist = n.distanceCouleur(p[k], c);
            if (newDist < minDist) {
                nearest = k;
                minDist = newDist;
            }
        }
        return nearest;

    }

}