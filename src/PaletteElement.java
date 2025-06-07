import java.awt.*;
import java.util.HashSet;

//un élément de palette persistant pour qu'il puisse être utilisé par plusieurs algos

public class PaletteElement {

    private String name;
    private Color color;
    private double[] lab;
    private HashSet<int[]> associatedColors;

    public PaletteElement() {
        this.name = "";
        this.color = new Color(0, 0, 0);
        this.lab = new double[] { 0, 0, 0 };
        this.associatedColors = new HashSet<>();
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return this.color;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setLAB(double[] lab) {
        this.lab = lab;
    }

    public void setLAB(double l, double a, double b) {
        this.lab = new double[] { l, a, b };
    }

    public double[] getLAB() {
        return this.lab;
    }

    public HashSet<int[]> getAssociatedColors() {
        return this.associatedColors;
    }

}
