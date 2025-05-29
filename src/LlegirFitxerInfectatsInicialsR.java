import java.io.*;
import java.util.*;

/**
 * @file LlegirFitxerInfectatsInicials.java
 * @brief Implementació per a la lectura de fitxers d'infectats inicials per regió i virus
 */

/**
 * @class LlegirFitxerInfectatsInicials
 * @brief Classe principal per a la lectura de fitxers de configuració d'infectats inicials
 * 
 * Aquesta classe llegeix i processa fitxers que contenen:
 * - L'estat inicial d'infecció de cada regió
 * - Els diferents virus presents a cada regió
 * - El nombre d'infectats inicials per cada virus
 */
public class LlegirFitxerInfectatsInicialsR {
    private Map<String, EstatInicialLlegit> regions = new HashMap<>(); ///< Mapa de regions amb els seus infectats inicials

    /**
     * @brief Llegeix un fitxer i carrega les dades dels infectats inicials
     * @param path Camí al fitxer a llegir
     * @return Llista d'estats inicials llegits
     * @throws IOException En cas d'error de lectura o format incorrecte
     */
    public List<EstatInicialLlegit> llegirFitxer(String path) throws IOException {
        List<EstatInicialLlegit> resultat = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(path));
        String linia;

        while ((linia = br.readLine()) != null) {
            linia = linia.trim();
            EstatInicialLlegit estat = procesarLinia(linia, br);
            if (estat != null) {
                resultat.add(estat);
                regions.put(estat.nomRegio, estat);
            }
        }
        br.close();
        return resultat;
    }

    /**
     * @brief Processa una línia del fitxer
     * @param linia Línia a processar
     * @param br BufferedReader per llegir més línies si cal
     * @return EstatInicial si s'ha processat una regió, null altrament
     * @throws IOException En cas d'error de format
     */
    private EstatInicialLlegit procesarLinia(String linia, BufferedReader br) throws IOException {
        if (linia.isEmpty() || linia.startsWith("#")) return null;
        
        if (linia.startsWith("regio ")) {
            String nomRegio = linia.substring(6).trim();
            EstatInicialLlegit estat = new EstatInicialLlegit(nomRegio);
            
            // Processar els virus i infectats d'aquesta regió
            procesarVirusRegio(estat, br);
            
            return estat;
        }
        
        return null;
    }

    /**
     * @brief Processa els virus i infectats d'una regió
     * @param estat Estat inicial al qual afegir els virus
     * @param br BufferedReader per llegir les línies següents
     * @throws IOException En cas d'error de format
     */
    private void procesarVirusRegio(EstatInicialLlegit estat, BufferedReader br) throws IOException {
        String linia;
        
        while ((linia = br.readLine()) != null) {
            linia = linia.trim();
            
            if (linia.equals("*")) {
                break;  // Fi de la regió
            }
            
            if (linia.startsWith("nom_virus ")) {
                String nomVirus = linia.substring(10).trim();
                
                // Llegir següent línia que han de ser els infectats
                linia = br.readLine().trim();
                if (!linia.startsWith("infectats ")) {
                    throw new IOException("Format de fitxer incorrecte. S'esperava 'infectats' després de 'nom_virus'.");
                }
                
                int infectats = Integer.parseInt(linia.substring(9).trim());
                estat.afegirVirus(nomVirus, infectats);
            }
        }
    }

    /**
     * @brief Retorna el mapa de regions carregades
     * @return Mapa amb totes les regions processades
     */
    public Map<String, EstatInicialLlegit> obtenirMapaRegions() {
        return regions;
    }
}
