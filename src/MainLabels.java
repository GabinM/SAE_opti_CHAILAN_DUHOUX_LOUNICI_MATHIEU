import java.awt.*;
import java.io.IOException;
import java.util.HashMap;

class MainLabels {
    public static void main(String[] args) throws IOException {
        Color[] palette = AlgoKMeans.getPalette("../planete_1.png", 10, new Norme94());
        HashMap<Color, String> labels = OutilCouleur.getLabels(palette, OutilCouleur.DEFAULT_LABELS);

        for (Color c : labels.keySet()) {
            System.out.println(
                    "couleur : " + c.getRed() + "," + c.getGreen() + "," + c.getBlue() + " - " + labels.get(c));
        }

        BiomeDisplay.imageToPalette("../planete_1.png", "../planete_1_traitee.png", palette);
        AlgoKMeans.getPalette("../planete_1_traitee.png", 10, new Norme94());
    }
}