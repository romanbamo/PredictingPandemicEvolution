import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @class MainProvesLlegirRegions
 * @brief Programa principal per provar la lectura de fitxers de regions
 */
public class MainProvesLlegirRegions {
    public static void main(String[] args) {
        LlegirFitxerRegionsR lector = new LlegirFitxerRegionsR();
        String pathFitxer = "regions.txt";
       
        try {
            System.out.println("Llegint fitxer: " + pathFitxer);
            lector.llegirFitxer(pathFitxer);

            System.out.println("\nRegions llegides correctament:");
            Map<String, RegioLlegida> mapaRegions = lector.vectorRegionsLlegides();
            for (RegioLlegida regio : mapaRegions.values()) {
                System.out.println("\nRegió: " + regio.nom);
                System.out.println("Habitants: " + regio.nHab);
                System.out.println("Taxa mobilitat interna: " + regio.taxaMobInt);
                
                System.out.println("Regions veïnes:");
                for (Map.Entry<String, Double> veina : regio.veins.entrySet()) {
                    System.out.println("  - " + veina.getKey() + " (taxa: " + veina.getValue() + ")");
                }
            }

            
        } catch (IOException e) {
            System.err.println("Error llegint el fitxer: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
