/**
 * @file VirusLlegit.java
 * @brief Implementació de la classe que representa un virus amb totes les seves característiques.
 * @author Romà Barrera
 */

/**
 * @class VirusLlegit
 * @brief Representa un virus amb totes les seves propietats i característiques epidemiològiques.
 * @details Aquesta classe serveix com a estructura de dades temporal per emmagatzemar la informació
 *          d'un virus llegit des d'un fitxer.
 *          
 *          És una classe de dades utilitzada per transportar informació entre les capes de l'aplicació.
 *
 * @invariant nom != null && !nom.isEmpty()
 * @invariant tipus != null && (tipus.equals("ARN") || tipus.equals("ADN"))
 * @invariant familia != null && !familia.isEmpty()
 * @invariant 0.0 <= probMalaltia && probMalaltia <= 1.0
 * @invariant incubacio >= 0
 * @invariant latencia >= 0
 * @invariant duradaContagi >= 0
 * @invariant duradaImmunitat >= 0
 * @invariant 0.0 <= mortalitat && mortalitat <= 1.0
 * @invariant 0.0 <= taxaContagi && taxaContagi <= 1.0
 * @invariant 0.0 <= probMutacioCopia && probMutacioCopia <= 1.0
 *
 */
public class VirusLlegit {
    public String nom;                  ///< Nom identificatiu del virus
    public String tipus;                ///< Tipus o classificació del virus
    public String familia;              ///< Família de virus a la qual pertany
    public double probMalaltia;         ///< Probabilitat de desenvolupar la malaltia [0-1]
    public int incubacio;               ///< Temps d'incubació en dies
    public int latencia;                ///< Temps de latència en dies
    public int duradaContagi;           ///< Durada del període de contagi en dies
    public int duradaImmunitat;         ///< Durada de la immunitat post-contagi en dies
    public double mortalitat;           ///< Taxa de mortalitat [0-1]
    public double taxaContagi;          ///< Taxa bàsica de reproducció (R0)
    public double probMutacioCopia;     ///< Probabilitat de mutació per còpia [0-1]

    /**
     * @brief Constructor principal de la classe VirusLlegit
     * @param nom Nom identificatiu del virus
     * @param tipus Tipus o classificació del virus
     * @param familia Família de virus a la qual pertany
     * @param probMalaltia Probabilitat de desenvolupar la malaltia [0-1]
     * @param incubacio Temps d'incubació en dies
     * @param latencia Temps de latència en dies
     * @param duradaContagi Durada del període de contagi en dies
     * @param duradaImmunitat Durada de la immunitat post-contagi en dies
     * @param mortalitat Taxa de mortalitat [0-1]
     * @param taxaContagi Taxa bàsica de reproducció (R0)
     * @param probMutacioCopia Probabilitat de mutació per còpia [0-1]
     * 
     * @pre Tots els paràmetres numèrics han de ser valors vàlids dins dels seus rangs
     * @post Crea una instància de VirusLlegit amb totes les propietats inicialitzades
     */
    public VirusLlegit(String nom, String tipus, String familia, double probMalaltia, 
                      int incubacio, int latencia, int duradaContagi, int duradaImmunitat,
                      double mortalitat, double taxaContagi, double probMutacioCopia) {
        this.nom = nom;
        this.tipus = tipus;
        this.familia = familia;
        this.probMalaltia = probMalaltia;
        this.incubacio = incubacio;
        this.latencia = latencia;
        this.duradaContagi = duradaContagi;
        this.duradaImmunitat = duradaImmunitat;
        this.mortalitat = mortalitat;
        this.taxaContagi = taxaContagi;
        this.probMutacioCopia = probMutacioCopia;
    }
}
