/**
 * @file LlegirFitxerVirusR.java
 * @brief Implementació per a la lectura i processament de fitxers de virus i les seves famílies.
 * @details Aquesta classe proporciona funcionalitats per llegir i processar fitxers que contenen 
 *          informació sobre virus i les seves famílies, emmagatzemant les dades en estructures 
 *          de dades adequades per al seu ús posterior.
 */

import java.io.*;
import java.util.*;

/**
 * @class LlegirFitxerVirus
 * @brief Classe principal per a la lectura de fitxers de virus i famílies de virus.
 * @details Aquesta classe llegeix fitxers amb un format específic, identificant les seccions 
 *          de famílies de virus i virus individuals, i emmagatzema aquesta informació en mapes 
 *          per a un accés eficient.
 *
 * @invariant families != null && virus != null
 * @invariant !families.containsKey(null) && !families.containsValue(null)
 * @invariant !virus.containsKey(null) && !virus.containsValue(null)
 *
 */
public class LlegirFitxerVirusR {
    private Map<String, FamiliaVirusLlegit> families = new HashMap<>(); ///< Mapa per emmagatzemar les famílies de virus
    private Map<String, VirusLlegit> virus = new HashMap<>();          ///< Mapa per emmagatzemar els virus individuals
    private boolean enSeccioFamilies = false;                          ///< Bandera per indicar si estem a la secció de famílies
    private boolean enSeccioVirus = false;                             ///< Bandera per indicar si estem a la secció de virus

    /**
     * @brief Mètode principal per llegir un fitxer i carregar les dades.
     * @param path Camí al fitxer que es vol llegir.
     * @return Map amb dues entrades: "families" (Map<String, FamiliaVirusLlegit>) i "virus" (Map<String, VirusLlegit>).
     * @throws IOException Si es produeix un error de lectura o el format del fitxer és incorrecte.
     * @pre El fitxer ha d'existir i tenir el format correcte.
     * @post Les dades del fitxer s'han carregat als mapes interns de la classe.
     */
    public Map<String, Map<String, ?>> llegirFitxer(String path) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(path));
        String line;

        while ((line = br.readLine()) != null) {
            line = line.trim();
            procesarLinea(line, br);
        }
        br.close();

        Map<String, Map<String, ?>> resultat = new HashMap<>();
        resultat.put("families", families);
        resultat.put("virus", virus);
        return resultat;
    }

    /**
     * @brief Processa una línia del fitxer i determina a quina secció pertany.
     * @param line Línia actual del fitxer.
     * @param br BufferedReader per llegir més línies si cal.
     * @throws IOException Si el format de la línia no és el esperat.
     * @private
     */
    private void procesarLinea(String line, BufferedReader br) throws IOException {
        if (line.isEmpty() || line.startsWith("#")) {
            return;
        }

        switch (line) {
            case "families":
                enSeccioFamilies = true;
                enSeccioVirus = false;
                break;
                
            case "virus":
                enSeccioFamilies = false;
                enSeccioVirus = true;
                break;
                
            case "*":
                break;
                
            default:
                if (enSeccioFamilies) {
                    procesarFamilia(line, br);
                } else if (enSeccioVirus) {
                    procesarVirus(line, br);
                }
                break;
        }
    }

    /**
     * @brief Processa la informació d'una família de virus.
     * @param line Línia actual que conté el nom de la família.
     * @param br BufferedReader per llegir les línies següents amb més informació de la família.
     * @throws IOException Si el format de les línies no és el correcte.
     * @private
     */
    private void procesarFamilia(String line, BufferedReader br) throws IOException {
        if (!line.startsWith("nom ")) {
            throw new IOException("Format incorrecte. S'esperava 'nom' per a família.");
        }
        String nomFamilia = line.substring(4).trim().split("#")[0].trim();

        line = br.readLine().trim();
        if (!line.startsWith("prob_mut_coincidencia ")) {
            throw new IOException("Format incorrecte. S'esperava 'prob_mut_coincidencia'.");
        }
        double probMut = Double.parseDouble(netejarLinia(line.substring(21).trim().split("#")[0].trim()));

        line = br.readLine().trim();
        if (!line.startsWith("tpc_maxim_variacio ")) {
            throw new IOException("Format incorrecte. S'esperava 'tpc_maxim_variacio'.");
        }
        int tpcVariacio = Integer.parseInt(netejarLinia(line.substring(18).trim().split("#")[0].trim()));

        br.readLine();

        families.put(nomFamilia, new FamiliaVirusLlegit(nomFamilia, probMut, tpcVariacio));
    }

    /**
     * @brief Processa la informació d'un virus individual.
     * @param line Línia actual que conté el nom del virus.
     * @param br BufferedReader per llegir les línies següents amb més informació del virus.
     * @throws IOException Si el format de les línies no és el correcte.
     * @private
     */
    private void procesarVirus(String line, BufferedReader br) throws IOException {
        if (!line.startsWith("nom ")) {
            throw new IOException("Format incorrecte. S'esperava 'nom' per a virus.");
        }
        String nomVirus = line.substring(4).trim().split("#")[0].trim();

        line = br.readLine().trim();
        if (!line.startsWith("tipus ")) {
            throw new IOException("Format incorrecte. S'esperava 'tipus'.");
        }
        String tipus = line.substring(6).trim().split("#")[0].trim();

        line = br.readLine().trim();
        if (!line.startsWith("familia ")) {
            throw new IOException("Format incorrecte. S'esperava 'familia'.");
        }
        String familia = line.substring(8).trim().split("#")[0].trim();

        Map<String, String> parametres = new HashMap<>();
        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (line.equals("*")) {
                break;
            }
            
            String[] parts = line.split(" ", 2);
            if (parts.length != 2) {
                throw new IOException("Format incorrecte en paràmetre: " + line);
            }
            parametres.put(parts[0], parts[1].trim());
        }

        VirusLlegit virusLlegit = new VirusLlegit(
            nomVirus,
            tipus,
            familia,
            Double.parseDouble(netejarLinia(parametres.get("prob_malaltia"))),
            Integer.parseInt(netejarLinia(parametres.get("incubacio"))),
            Integer.parseInt(netejarLinia(parametres.get("latencia"))),
            Integer.parseInt(netejarLinia(parametres.get("durada_contagi"))),
            Integer.parseInt(netejarLinia(parametres.get("durada_immunitat"))),
            Double.parseDouble(netejarLinia(parametres.get("mortalitat"))),
            Double.parseDouble(netejarLinia(parametres.get("taxa_contagi"))),
            parametres.containsKey("prob_mutacio_copia") ? 
                Double.parseDouble(netejarLinia(parametres.get("prob_mutacio_copia"))) : 0.0
        );

        virus.put(nomVirus, virusLlegit);
    }


    private String netejarLinia(String line) throws IOException {
        if (line == null) throw new IOException("S'esperaven més línies al fitxer");
        return line.trim().split("#")[0].trim();
    }


    /**
     * @brief Retorna el mapa de famílies de virus llegides.
     * @return Mapa amb les famílies de virus, on la clau és el nom de la família.
     */
    public Map<String, FamiliaVirusLlegit> familiesVirusLlegides() {
        return families;
    }

    /**
     * @brief Retorna el mapa de virus llegits.
     * @return Mapa amb els virus individuals, on la clau és el nom del virus.
     */
    public Map<String, VirusLlegit> virusLlegits() {
        return virus;
    }
}
