/**
 * @class VirusADN
 * @brief Representa un virus de tipus ADN, és a dir,
 * virus que muten amb molt poca freqüència (podem considerar que NO muten).
 * @author Iria Auladell
 */
public class VirusADN extends Virus {

    // Fem primer el constructor de la classe VirusADN.
    public VirusADN(String nom_virus, FamiliaVirus familia_virus, double p_mal, int t_inc, int t_lat, double p_mor,
                    int t_con, double p_con, int t_imm) {
        // Pre: Els valors han de ser positius i el nom ha de ser vàlid.
        // Post: Es crea un virus de tipus ADN que NO pot mutar.

        super(nom_virus, familia_virus, p_mal, t_inc, t_lat, p_mor, t_con, p_con, t_imm); // això ho fem per
        // cridar al constructor de la classe abstracta Virus
    }
}
