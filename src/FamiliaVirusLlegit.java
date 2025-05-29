/**
 * @file FamiliaVirusLlegit.java
 * @brief Implementació de la classe que representa una família de virus amb les seves característiques.
 * @author Romà Barrera
 */

/**
 * @class FamiliaVirusLlegit
 * @brief Representa una família de virus amb les seves propietats de mutació i variació.
 * @details Aquesta classe serveix com a estructura de dades temporal per emmagatzemar la informació
 *          bàsica d'una família de virus llegida des d'un fitxer.
 *          
 *          És una classe de dades utilitzada per transportar informació entre les capes de l'aplicació
 *          i per relacionar virus del mateix tipus.
 */
public class FamiliaVirusLlegit {
    public String nom;                  ///< Nom identificatiu de la família de virus
    public double probMutCoincidencia; ///< Probabilitat de mutació coincident entre virus de la mateixa família [0-1]
    public int tpcMaximVariacio;       ///< Taxa percentual màxima de variació admissible dins de la família (en percentatge)

    /**
     * @brief Constructor principal de la classe FamiliaVirusLlegit
     * @param nom Nom identificatiu de la família
     * @param probMutCoincidencia Probabilitat que dos virus de la família mutin de forma coincident [0-1]
     * @param tpcMaximVariacio Percentatge màxim de variació admissible entre virus de la mateixa família
     * 
     * @pre probMutCoincidencia ha de ser un valor entre 0 i 1
     * @pre tpcMaximVariacio ha de ser un enter positiu
     * @post Crea una instància de FamiliaVirusLlegit amb totes les propietats inicialitzades
     */
    public FamiliaVirusLlegit(String nom, double probMutCoincidencia, int tpcMaximVariacio) {
        this.nom = nom;
        this.probMutCoincidencia = probMutCoincidencia;
        this.tpcMaximVariacio = tpcMaximVariacio;
    }
}
