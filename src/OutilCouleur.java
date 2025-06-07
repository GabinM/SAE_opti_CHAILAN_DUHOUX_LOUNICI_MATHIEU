import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import static java.util.Map.entry;
import java.awt.*;

public class OutilCouleur {

    public final static Map<Color, String> DEFAULT_LABELS = Map.ofEntries(
            entry(new Color(81, 77, 39), "tundra"),
            entry(new Color(33, 44, 12), "taïga"),
            entry(new Color(83, 97, 38), "forêt tempérée"),
            entry(new Color(39, 60, 1), "forêt tropicale"),
            entry(new Color(248, 241, 171), "savane"),
            entry(new Color(68, 89, 32), "prairie"),
            entry(new Color(152, 140, 120), "désert"),
            entry(new Color(255, 255, 255), "glacier"),
            entry(new Color(18, 85, 91), "eau peu profonde"),
            entry(new Color(0, 0, 52), "eau profonde"));

    public static int[] getTabColor(int rgb) {
        int[] color = new int[3];
        color[0] = (rgb & 0xff0000) >> 16;
        color[1] = (rgb & 0xff00) >> 8;
        color[2] = (rgb & 0xff);

        return color;
    }

    public static void getLabels(PaletteData paletteD, Map<Color, String> labels) {

        ArrayList<Color> remainingColors = new ArrayList<>(labels.keySet());

        for (PaletteElement pal : paletteD.getPalette()) {
            Color[] cols = new Color[remainingColors.size()];
            int i = 0;
            for (Color color : remainingColors) {
                cols[i] = color;
                i++;
            }
            Color bestColor = getPlusProche(pal.getColor(), cols);
            pal.setName(labels.get(bestColor));
            remainingColors.remove(bestColor);

        }
    }

    public static Color getPlusProche(Color c, Color[] palette) {
        Norme94 norme = new Norme94();
        double[] lab = Colors.convertRGB_to_Lab(OutilCouleur.getTabColor(c.getRGB()));
        Color bestColor = null;
        double bestDistance = Double.MAX_VALUE;
        for (Color c2 : palette) {
            double[] lab2 = Colors.convertRGB_to_Lab(OutilCouleur.getTabColor(c2.getRGB()));
            double dist = norme.distanceCouleur(lab, lab2);
            if (dist < bestDistance) {
                bestColor = c2;
                bestDistance = dist;
            }
        }
        return bestColor;
    }

}
