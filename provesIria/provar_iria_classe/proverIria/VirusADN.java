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

    // Recordem que hem fet el mètode mutar de tal manera que ens serveixi tant per saber si pot mutar com per
    // fer la mutació en el cas de ARN (si és un ADN, ha de donar error)

    // Important: Hem de complir el LSP: qualsevol subclasse hauria de poder substituir a Virus sense que el
    // comportament global del programa es trenqui. La idea que hem tingut llavors, és que si ho fem a ADN, que
    // es retorni a ell mateix, i així, segueix el post de ARN també.


    /**
     * Sobreescrivim el mètode de mutació per als virus ADN.
     * Com que aquests virus no poden mutar, he pensat que seria bona idea retornar el mateix objecte.
     * Així, com hem dit, complim el LSP, ja que no trenquem la postcondició: seguim retornant un Virus.
     *
     * Així, si es crida virus.mutacio(...), i és un ADN, es podrà seguir amb el codi sense problemes.
     */

    @Override
    public VirusADN mutacio() {
        // Pre: El virus ha d'existir (no pot ser null).
        // Post: Retorna el mateix virus "this", sense cap modificació, perquè els virus de tipus ADN no poden mutar.
        return this;
    }

    @Override
    public VirusADN mutacio(Virus B) {
        // Pre: Els virus de la classe i B han d'existir (no poden ser null) i pertànyer a la mateixa família.
        // Post: Es retorna el virus de la pròpia classe sense cap modificació.
        return this;
    }

    @Override
    public boolean muta() {
        // Pre: El virus ha d’estar inicialitzat correctament.
        // Post: Retorno fals, ja que els virus ADN no poden mutar.
        return false;
    }


}


