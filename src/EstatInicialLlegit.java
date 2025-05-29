import java.util.Map;
import java.util.HashMap;
/**
 * @class EstatInicialLlegit
 * @brief Representa l'estat inicial d'infecció d'una regió per a diferents virus. És una classe de dada temporal.
 * @author Romà Barrera
 * 
 * Aquesta classe emmagatzema:
 * - El nom de la regió
 * - Un mapa amb els noms dels virus i el nombre d'infectats inicials
 */
public class EstatInicialLlegit {
    String nomRegio;                ///< Nom de la regió
    Map<String, Integer> virusInicials; ///< Mapa de virus amb el nombre d'infectats inicials

    /**
     * @brief Constructor de la classe EstatInicial
     * @param nom Nom de la regió
     */
    EstatInicialLlegit(String nom) {
        nomRegio = nom;
        virusInicials = new HashMap<>();
    }

    /**
     * @brief Afegeix un virus amb els seus infectats inicials a la regió
     * @param nomVirus Nom del virus
     * @param infectats Nombre d'infectats inicials
     */
    public void afegirVirus(String nomVirus, int infectats) {
        virusInicials.put(nomVirus, infectats);
    }
}
