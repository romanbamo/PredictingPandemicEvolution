import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
public class lecturaFitxerVirus extends Lectura {

    public Fitxer_Virus(String nom_fitxer) {
        super(nom_fitxer);
    }

    public Map<String, Familia> carregarFamilies() {
        // Pre: El fitxer d'entrada ha de tenir el format correcte.
        // Post: Retorna un mapa amb les famílies de virus llegides del fitxer.
        // El mapa té com a clau el nom de la família i com a valor l'objecte FamiliaVirus corresponent.
    
        Map<String, Familia> families = new HashMap<>();
        List<String> linies = llegirLinies();

        for (String liniaOriginal : linies) {
            String linia = liniaOriginal.trim();
        
            if (linia.equals("virus")) {
                break;
            }
        
            if (linia.startsWith("nom")) {
                String[] parts = linia.split("\\s+"); // Separem per espais
        
                if (parts.length >= 4) {
                    String nom = parts[1];
                    double tpc = Double.parseDouble(parts[2]);
                    double prob = Double.parseDouble(parts[3]);
        
                    families.put(nom, new Familia(nom, prob, tpc));
                }
            }
        }
        return families;

        public Map<String, Virus> carregarVirus(Map<String, Familia> families) {
            /**
     * Precondició: El fitxer que es llegeix està ben format.
     * Postcondició: Retorna un mapa on la clau és el nom del virus i el valor és un objecte Virus,
     * que pot ser una instància de Virus_ADN o Virus_ARN, segons les dades del fitxer.
     * Com a resultat, es generen correctament tots els virus definits al fitxer.
     */
            Map<String, Virus> virusMap = new HashMap<>();
            List<String> linies = llegirLinies();
        
            boolean processant = false;
            String nom = null, tipus = null, nomFamilia = null;
            double p_malaltia = 0, p_mortalitat = 0, p_contagi = 0, t_contagi = 0, p_mutacio = 0;
            int t_incubacio = 0, t_latencia = 0, t_immunitat = 0;
            for(int i = 0; i < linies.size(); i++) {
                String linia = linies.get(i).trim();
                if(linia.equals("virus")) {
                    llegintVirus = true;
                    continue;
                } 
                if(llegintVirus || linia.isEmpty() || linia.startsWith("#")) continue;
                if(linia.startsWith("NOM ")) nom = linia.substring(4).trim();
                else if(linia.startsWith("TIPUS ")) tipus = linia.substring(6).trim();
                else if(linia.startsWith("FAMILIA ")) nomFamilia = linia.substring(8).trim();
                else if(linia.startsWith("MORTALITAT ")) p_mortalitat = Double.parseDouble(linia.substring(11).trim());
                else if(linia.startsWith("PROBABILITAT MALALTIA")) p_malaltia = Double.parseDouble(linia.substring(14).trim());
                else if(linia.startsWith("INCUBACIO ")) t_incubacio = Integer.parseInt(linia.substring(10).trim());
                else if(linia.startsWith("LATENCIA ")) t_latencia = Integer.parseInt(linia.substring(9).trim());
                else if(linia.startsWith("DURACIO CONTAGI ")) t_contagi = Double.parseDouble(linia.substring(15).trim());
                else if(linia.startsWith("DURACIO IMMUNITAT")) t_immunitat = Integer.parseInt(linia.substring(17).trim());
                
                else if(linia.startsWith("TAXA CONTAGI ")) p_contagi = Double.parseDouble(linia.substring(13).trim());
                else if(linia.startsWith("PROBABILITAT DE MUTACIO ")) p_mutacio = Double.parseDouble(linia.substring(19).trim());
                else if(linia.equals("*")) {
                    Familia f = families.get(nomFamilia);
                    Virus v;
    
        
        
    
        