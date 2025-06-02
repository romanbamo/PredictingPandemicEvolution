import java.io.IOException;
import java.util.Map;

/**
 * @class MainProvesLlegirVirus
 * @brief Programa principal per provar la lectura de fitxers de virus
 */
public class MainProvesLlegirVirus {
    public static void main(String[] args) {
        // 1. Crear una instància del lector
        LlegirFitxerVirusR lector = new LlegirFitxerVirusR();
        
        // 2. Especificar el camí al fitxer de prova
        String pathFitxer = "virus.txt"; // Canvia pel teu fitxer de prova
        
        try {
            // 3. Llegir el fitxer
            System.out.println("Llegint fitxer: " + pathFitxer);
            Map<String, Map<String, ?>> dades = lector.llegirFitxer(pathFitxer);
            
            // 4. Mostrar resultats de famílies
            System.out.println("\nFamílies de virus llegides:");
            @SuppressWarnings("unchecked")
            Map<String, FamiliaVirusLlegit> families = (Map<String, FamiliaVirusLlegit>) dades.get("families");
            for (Map.Entry<String, FamiliaVirusLlegit> entry : families.entrySet()) {
                FamiliaVirusLlegit familia = entry.getValue();
                System.out.println("\nFamília: " + familia.nom);
                System.out.println("Probabilitat mutació coincidència: " + familia.probMutCoincidencia);
                System.out.println("TPC màxim variació: " + familia.tpcMaximVariacio);
            }
            
            // 5. Mostrar resultats de virus
            System.out.println("\nVirus llegits:");
            @SuppressWarnings("unchecked")
            Map<String, VirusLlegit> virus = (Map<String, VirusLlegit>) dades.get("virus");
            for (Map.Entry<String, VirusLlegit> entry : virus.entrySet()) {
                VirusLlegit v = entry.getValue();
                System.out.println("\nVirus: " + v.nom);
                System.out.println("Tipus: " + v.tipus);
                System.out.println("Família: " + v.familia);
                System.out.println("Probabilitat malaltia: " + v.probMalaltia);
                System.out.println("Període incubació: " + v.incubacio + " dies");
                System.out.println("Període latència: " + v.latencia + " dies");
                System.out.println("Durada contagi: " + v.duradaContagi + " dies");
                System.out.println("Durada immunitat: " + v.duradaImmunitat + " dies");
                System.out.println("Mortalitat: " + v.mortalitat);
                System.out.println("Taxa contagi: " + v.taxaContagi);
                if (v.tipus.equals("ARN")) {
                    System.out.println("Probabilitat mutació còpia: " + v.probMutacioCopia);
                }
            }
            
        } catch (IOException e) {
            System.err.println("Error llegint el fitxer: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
