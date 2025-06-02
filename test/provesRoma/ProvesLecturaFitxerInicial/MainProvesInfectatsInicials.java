import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @class MainProvesInfectatsInicials
 * @brief Programa principal per provar la lectura de fitxers d'infectats inicials
 */
public class MainProvesInfectatsInicials {
    public static void main(String[] args) {
        LlegirFitxerInfectatsInicialsR lector = new LlegirFitxerInfectatsInicialsR();
        String pathFitxer = "estatInicial.txt";
        
        try {
            System.out.println("Llegint fitxer: " + pathFitxer);
            List<EstatInicialLlegit> resultat = lector.llegirFitxer(pathFitxer);
            System.out.println("\nRegions amb infectats inicials:");
            for (EstatInicialLlegit estat : resultat) {
                System.out.println("\nRegió: " + estat.nomRegio);
                System.out.println("Virus i infectats inicials:");                
                for (Map.Entry<String, Integer> entry : estat.virusInicials.entrySet()) {
                    System.out.println("  - " + entry.getKey() + ": " + entry.getValue() + " infectats");
                }
            }
            System.out.println("\nMapa complet de regions:");
            Map<String, EstatInicialLlegit> mapaRegions = lector.obtenirMapaRegions();
            for (String nomRegio : mapaRegions.keySet()) {
                System.out.println("- " + nomRegio);
            }            
        } catch (IOException e) {
            System.err.println("Error llegint el fitxer: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
