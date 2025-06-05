import java.util.Map;
import java.util.HashMap;
import static java.util.Map.entry;
import java.awt.*;

public class OutilCouleur {

    public final static Map<Color, String> DEFAULT_LABELS = Map.ofEntries(
        entry(new Color(100, 255, 92) , "tundra"),
        entry(new Color(43, 50, 35) , "taïga"),
        entry(new Color(59, 66, 43) , "forêt tempérée"),
        entry(new Color(46, 64, 34) , "forêt tropicale"),
        entry(new Color(84, 106, 70) , "savane"),
        entry(new Color(104, 95, 82) , "prairie"),
        entry(new Color(152, 140, 120) , "désert"),
        entry(new Color(200,200,200) , "glacier"),
        entry(new Color(49, 83, 100) , "eau peu profonde"),
        entry(new Color(12, 31, 47) , "eau profonde")
    );
        

    public static int[] getTabColor(int rgb){
        int[] color = new int[3];
        color[0] =  ( rgb & 0xff0000) >> 16;
        color[1] =  ( rgb & 0xff00) >> 8;
        color[2] =  ( rgb & 0xff);

        return color;
    }

    public static HashMap<Color,String> getLabels(Color[] colors, Map<Color, String> labels){
        Norme94 norme = new Norme94();
        HashMap<Color, String> res = new HashMap<>();
        for(Color c : colors){
            double[] lab = Colors.convertRGB_to_Lab(OutilCouleur.getTabColor(c.getRGB()));
            Color bestColor = null;
            double bestDistance = 100000000.0;
            for(Color c2 : labels.keySet()){
                double[] lab2 = Colors.convertRGB_to_Lab(OutilCouleur.getTabColor(c2.getRGB()));
                double dist = norme.distanceCouleur(lab, lab2);
                if(dist < bestDistance){
                    bestColor = c2;
                    bestDistance = dist;
                }
            }
            res.put(c, labels.get(bestColor));
        }

        return res;
    }

}
