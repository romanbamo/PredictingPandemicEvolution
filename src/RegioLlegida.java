import java.util.Map;
import java.util.HashMap;
/**
 * @class RegioLlegida
 * @brief Representa una regió amb les seves dades bàsiques i connexions amb altres regions. És una estructura temporal.
 * @author Romà Barrera
 * 
 * Aquesta classe actua com una estructura de dades per emmagatzemar:
 * - Nom de la regió
 * - Nombre d'habitants
 * - Taxa de mobilitat interna
 * - Connexions amb regions veïnes
 */
public class RegioLlegida {
    public String nom;             ///< Nom de la regió
    public int nHab;               ///< Nombre d'habitants de la regió
    public double taxaMobInt;      ///< Taxa de mobilitat interna de la regió
    public Map<String, Double> veins; ///< Mapa de regions veïnes amb les seves taxes de mobilitat

    /**
     * @brief Constructor de la classe RegioLlegida
     * @param n Nom de la regió
     * @param nhab Nombre d'habitants
     * @param taxa Taxa de mobilitat interna
     */
    RegioLlegida(String n, int nhab, double taxa) {
        this.nom = n; 
        this.nHab = nhab; 
        this.taxaMobInt = taxa; 
        this.veins = new HashMap<>();
    }

    /**
     * @brief Afegeix una regió veïna amb la seva taxa de mobilitat
     * @param nom Nom de la regió veïna
     * @param taxa Taxa de mobilitat cap a aquesta regió
     */
    public void afegirVeina(String nom, double taxa) {
        veins.put(nom, taxa);
    }       
}
