import java.awt.*;

public enum Biome {
    TUNDRA(new Color(71, 70, 61), "Tundra"),
    TAIGA(new Color(43, 50, 35), "Taiga"),
    FORET_TEMPEREE(new Color(59, 66, 43), "Forêt tempérée"),
    FORET_TROPICALE(new Color(46, 64, 34), "Forêt tropicale"),
    SAVANE(new Color(84, 106, 70), "Savane"),
    PRAIRIE(new Color(104, 95, 82), "Prairie"),
    DESERT(new Color(152, 140, 120), "Désert"),
    GLACIER(new Color(200, 200, 200), "Glacier"),
    EAU_PEU_PROFONDE(new Color(49, 83, 100), "Eau peu profonde"),
    EAU_PROFONDE(new Color(12, 31, 47), "Eau profonde");


    private final Color color;
    private final String name;

    Biome(Color color, String name) {
        this.color = color;
        this.name = name;
    }

    public Color getColor() {
        return color;
    }

    public String getName() {
        return name;
    }
}