import java.awt.*;
import java.util.HashMap;

class MainLabels {
    public static void main(String[] args){
        Color[] palette = AlgoKMeans.getPalette("../planete_1.jpg", 10, new Norme94());
        HashMap<Color, String> labels = OutilCouleur.getLabels(palette, OutilCouleur.DEFAULT_LABELS);

        for(Color c : labels.keySet()){
            System.out.println("couleur : "+c.getRed() + "," + c.getGreen() + "," + c.getBlue() + " - "+labels.get(c));
        }
    }
}