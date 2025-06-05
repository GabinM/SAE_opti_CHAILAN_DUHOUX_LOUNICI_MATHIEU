import java.awt.*;

public class Norme94 {

    public double distanceCouleur(Color color1, Color color2) {
        double[] col1 = Colors.convertRGB_to_Lab(OutilCouleur.getTabColor(color1.getRGB()));
        double[] col2 = Colors.convertRGB_to_Lab(OutilCouleur.getTabColor(color2.getRGB()));

        return distanceCouleur(col1, col2);

        /*
         * double c1 = Math.pow(col1[1], 2)+Math.pow(col1[2], 2);
         * double c2 = Math.pow(col2[1], 2)+Math.pow(col2[2], 2);
         * 
         * double sc = 1+ Math.pow(0.045,2)*c1;
         * double sh = 1+Math.pow(0.015,2)*c1;
         * double dl = col1[0]-col2[0];
         * double dc = c1 - c2;
         * double dh = (Math.pow(col1[1]-col2[1],2)+Math.pow(col1[2]-col2[2],2)) - dc;
         * 
         * double comp1 = dl;
         * double comp2 = dc/Math.pow(sc,2);
         * double comp3 = dh/Math.pow(sh,2);
         */

    }

    public double distanceCouleur(double[] col1, double[] col2) {

        double c1 = Math.sqrt(Math.pow(col1[1], 2) + Math.pow(col1[2], 2));
        double c2 = Math.sqrt(Math.pow(col2[1], 2) + Math.pow(col2[2], 2));

        double sc = 1 + (0.045 * c1);
        double sh = 1 + (0.015 * c1);
        double dl = col1[0] - col2[0];
        double dc = c1 - c2;
        double dh = Math.sqrt(Math.pow(col1[1] - col2[1], 2) + Math.pow(col1[2] - col2[2], 2) - dc);
        double comp1 = Math.pow(dl, 2);
        double comp2 = Math.pow(dc / sc, 2);
        double comp3 = Math.pow(dh / sh, 2);

        return comp1 + comp2 + comp3; // englobé par un sqrt à la base

    }
}
