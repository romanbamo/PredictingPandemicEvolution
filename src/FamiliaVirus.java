/**
 * @class FamiliaVirus
 * @brief Representa una família de virus, agrupant diferents virus que tenen característiques comunes.
 * @details Cada virus pertany a una família. Diferents espècies de virus poden pertànyer a una mateixa família,
 * però o tots els que hi ha son NO MUTABLES, o tots son MUTABLES, però no hi ha barreja dels dos grups en les families.
 *
 * @invariant nom != null && !nom.isEmpty()
 * @invariant 0.0 <= pMut2E && pMut2E <= 1.0
 * @invariant tpcMax >= 0
 *
 * @author Iria Auladell
 * @version 2025.2.18
 */
public class FamiliaVirus {

// ATRIBUTS

    private String nom;
    private double pMut2E;  // Probabilitat de mutació per coincidència (0-1)
    private int tpcMax;  // Tant per cent màxim de variació en mutacions per error de còpia

// MÈTODES PÚBLICS

    /**
     * Constructor de la classe FamiliaVirus.
     *
     * @param nom_fam Nom de la família de virus.
     * @param P_mut2E és la probabilitat de mutació per coincidència (si és una família de virus ADN,
     *                valdrà 0)
     * @param tpc_max Tant per cent màxim de variació en mutacions per error de còpia.
     */
    public FamiliaVirus(String nom_fam, double P_mut2E, int tpc_max) {
        // Pre: El nom no pot ser buit i la taxa de mutació ha d'estar entre 0 i 1.
        // Post:  Es crea una nova família de virus amb el nom, la taxa de mutació i el tpcMax que ens han passat.

        nom = nom_fam;
        this.pMut2E = P_mut2E;
        this.tpcMax = tpc_max;

    }

    public String nom() {
        // Pre: L'objecte FamiliaVirus ha d'estar inicialitzat correctament.
        // Post: Retorna el nom de la família.

        return nom;
    }

    public double probMutacioCoincidencia() {
        // Pre: L'objecte FamiliaVirus ha d'estar inicialitzat correctament.
        // Post: Retorna un valor entre 0 i 1 que és la taxa de mutació. si és una família de virus ADN,
        // retornarà 0

        return pMut2E;
    }

    public int tpcMax() {
        // Pre: L'objecte FamiliaVirus ha d'estar inicialitzat correctament.
        // Post: Retorna tant per cent màxim de variació (en mutacions per error de còpia)

        return tpcMax;
    }

    public boolean esLaMateixaFamilia(FamiliaVirus famvirusB) {
        //Pre: Passem dos virus que han de pertanyer a una família existent
        // Post: Retorna true si el virus de la classe, i el virusB, son de la matèixa familia. Altrament,
        // retorna false

        if (famvirusB == null) return false;  // Evitem errors amb famílies nul·les
        return this.nom.equals(famvirusB.nom);  // Comparem els noms de les dues famílies
    }

}
