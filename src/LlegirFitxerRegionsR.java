/**
 * @file LlegirFitxerRegionsR.java
 * @brief Implementació per a la lectura i processament de fitxers de regions i territoris limítrofs
 */

import java.io.*;
import java.util.*;

/**
 * @class LlegirFitxerRegionsR
 * @brief Classe principal per a la lectura de fitxers de configuració de regions
 * 
 * Aquesta classe llegeix i processa fitxers que contenen:
 * - Dades bàsiques de regions
 * - Connexions i taxes de mobilitat entre regions
 */
public class LlegirFitxerRegionsR {
    private Map<String, RegioLlegida> regions = new HashMap<>(); ///< Mapa de regions carregades
    private boolean enSeccioLimits = false; ///< Indica si s'està processant la secció de límits

    /**
     * @brief Llegeix un fitxer i carrega les dades de les regions
     * @param path Camí al fitxer a llegir
     * @return Llista de regions llegides
     * @throws IOException En cas d'error de lectura o format incorrecte
     */
    public List<RegioLlegida> llegirFitxer(String path) throws IOException {
        List<RegioLlegida> resultat = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(path));
        String line;

        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;            
            RegioLlegida regio = procesarLinea(line, br);            
            if (regio != null) {
                resultat.add(regio);
            }
        }
        br.close();
        return resultat;
    }

    /**
     * @brief Processa una línia del fitxer
     * @param line Línia a processar
     * @param br BufferedReader per llegir més línies si cal
     * @return RegioLlegida si s'ha processat una regió, null altrament
     * @throws IOException En cas d'error de format
     */
    private RegioLlegida procesarLinea(String line, BufferedReader br) throws IOException {
        if (line.isEmpty() || line.startsWith("#")) return null;
        
        switch (line) {
            case "*":
                enSeccioLimits = false;
                return null;
                
            case "limits_i_mobilitat":
                enSeccioLimits = true;
                return null;
                
            default:
                if (enSeccioLimits) {
                    processarLimitsMobilitat(line, br);
                    return null;
                } else {
                    return processarDadesRegio(line, br);
                }
        }
    }

    /**
     * @brief Processa les dades bàsiques d'una regió
     * @param line Línia amb el nom de la regió
     * @param br BufferedReader per llegir les línies següents
     * @return Nova instància de RegioLlegida
     * @throws IOException En cas d'error de format
     */
    private RegioLlegida processarDadesRegio(String line, BufferedReader br) throws IOException {
        line = netejarLinia(line);
        if (!line.startsWith("nom ")) {
            return null;
        }
        String nom = line.substring(4).trim();

        line = netejarLinia(br.readLine());
        if (!line.startsWith("habitants ")) {
            throw new IOException("Format d'arxiu incorrecte. S'esperava 'habitants'.");
        }
        int habitants = Integer.parseInt(extreuValorNumeric(line.substring(10)));

        line = netejarLinia(br.readLine());
        if (!line.startsWith("mob_interna ")) {
            throw new IOException("Format d'arxiu incorrecte. S'esperava 'mob_interna'.");
        }
        double mobInterna = Double.parseDouble(extreuValorNumeric(line.substring(12)));

        line = netejarLinia(br.readLine());
        if (!line.equals("*")) {
            throw new IOException("Format d'arxiu incorrecte. S'esperava '*' després de les dades de regió.");
        }

        RegioLlegida regio = new RegioLlegida(nom, habitants, mobInterna);
        regions.put(nom, regio);
        return regio;
    }

    /**
     * @brief Neteja una línia eliminant comentaris i espais innecessaris
     * @param line Línia a netejar
     * @return Línia netejada
     * @throws IOException Si la línia és null
     */
    private String netejarLinia(String line) throws IOException {
        if (line == null) {
            return "";
        }
        return line.trim().split("#")[0].trim();
    }

    /**
     * @brief Extreu el valor numèric d'una cadena, eliminant possibles unitats o comentaris
     * @param s Cadena d'on extreure el valor
     * @return Valor numèric en format String
     */
    private String extreuValorNumeric(String s) {
        return s.replaceAll("[^0-9.]", "");
    }

    /**
     * @brief Processa les connexions entre regions
     * @param line Línia amb el nom de la regió origen
     * @param br BufferedReader per llegir les connexions
     * @throws IOException En cas d'error de format o regió no trobada
     */
    private void processarLimitsMobilitat(String line, BufferedReader br) throws IOException {
        String regioOrigen = line.trim();
        
        if (!regions.containsKey(regioOrigen)) {
            throw new IOException("Regió d'origen no trobada: " + regioOrigen);
        }
        
        RegioLlegida regio = regions.get(regioOrigen);
        String linea;
        
        while ((linea = br.readLine()) != null) {
            linea = netejarLinia(linea);
            if (linea.equals("*")) {
                break;
            }
            
            String[] parts = linea.split("\\s+");
            if (parts.length != 2) {
                throw new IOException("Format incorrecte en linea de secció de limits: " + linea);
            }
            
            String regioDesti = parts[0];
            double taxaMobilitat = Double.parseDouble(parts[1]);
            
            if (!regions.containsKey(regioDesti)) {
                throw new IOException("Regió destí no trobada: " + regioDesti);
            }
            
            regio.afegirVeina(regioDesti, taxaMobilitat);
        }
    }

    /**
     * @brief Retorna el mapa de regions carregades
     * @return Mapa amb totes les regions processades
     */
    public Map<String, RegioLlegida> vectorRegionsLlegides() {
        return regions;
    }
}
