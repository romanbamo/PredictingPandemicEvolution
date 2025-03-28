/**
 * @class VirusARN
 * @brief Representa un virus de tipus ARN, que pot mutar.
 * @author Iria Auladell
 */

public class VirusARN extends Virus {

// ATRIBUTS

    private double pMutEC;  // Probabilitat de mutació per error de còpia

// METODES PÚBLICS

    // Tot i que ara no hem de posar atributs, si que expliquem aqui
    // per quan seguim amb el treball, que tindrem un atribut per a aquesta
    // classe de virus que muten, que és la
    // P_mutEC  probabilitat de mutació per error de còpia

    // Fem primer el constructor de la classe VirusARN, tenint en compte aquest nou atribut
    public VirusARN(String nom_virus, FamiliaVirus familia_virus, double p_mal, int t_inc, int t_lat, double p_mor,
                    int t_con, double p_con, int t_imm, double p_mutEC) {
        // Pre: Els valors han de ser positius (prob han d'anar de 0 a 1) i el nom ha de ser vàlid.
        // Post: Es crea un virus de tipus ARN que pot mutar. A part de tenir totes les
        // característiques del Virus, tindrà també una certa probabilitat de mutació per error de còpia.

        super(nom_virus, familia_virus, p_mal, t_inc, t_lat, p_mor, t_con, p_con, t_imm); // això ho fem per
        // cridar al constructor de la classe abstracta Virus

        pMutEC = p_mutEC;

    }

    public double probabilitatMutacioErrorCopia() {
        // Pre: El virus ha de ser vàlid i existir
        // Post: Retorna la probabilitat de mutació per error de còpia d'aquest virus

        return pMutEC;
    }

    // Per quan implementem la classe de Mutació, és important assegurar-nos de que dos virus són
    // de la mateixa familia.
    public boolean PertanyenMateixaFamilia(VirusARN A, VirusARN B) {
        // Pre: Virus A i Virus B han d’existir
        // Post: Retorna cert si A i B formen part de la mateixa familia, altrament retorna false

        return A.familia().esLaMateixaFamilia(B.familia());

    }

    /**
     * Fem ús d'aquest mètode quan hi ha una mutació per error de còpia a un virus ARN.
     * Tenim el propi virus ARN que muta.
     * @return El nou virus ARN amb les seves característiques modificades.
     */

    @Override
    public VirusARN mutacio() {
        // Pre: El virus ha de ser ARN (els unics que poden mutar) i ha de tenir la seva probabilitat de mutació
        // per error de còpia.
        // Post: Retorna "this" si no hi ha mutació. Si hi ha, es genera un nou virus ARN de la mateixa família que
        // l’original, amb els paràmetres modificats. Per tant, retornarà una mutació per error de còpia de "this"
        // El nom d'aquest nou virus serà el nom del virus que muta però amb un número correlatiu que indiqui el número
        // de vegades que ha mutat.

        int maximVariacio = this.familia().tpcMax();  // Primer de tot, hem de saber el tant per cent màxim (tpc_max)
        // de variació, el qual és una característica de la família del virus (cridem el seu mètode).

        // Calculo els % aleatoris entre -tpcMax i +tpcMax per als 4 paràmetres que em diu la pràctica. Cada un tindrà
        // una variació percentual diferent

        int variacioPMal = fem_tpc_de_variació(maximVariacio);
        int variacioPMor = fem_tpc_de_variació(maximVariacio);
        int variacioTCon = fem_tpc_de_variació(maximVariacio);
        int variacioPCon = fem_tpc_de_variació(maximVariacio);

        // Modifiquem els valors d'aquests 4 paràmetres
        double pMal_mut = aplicar_tcp_variació_prob(this.probDesenvoluparMalaltia(), variacioPMal);
        double pMor_mut = aplicar_tcp_variació_prob(this.taxaMortalitat(), variacioPMor);
        int tCon_mut = Math.round(aplicar_tcp_variació_temps(this.tempsContagi(), variacioTCon));
        double pCon_mut = aplicar_tcp_variació_prob(this.taxaContagi(), variacioPCon);

        // Heretem del Virus original (el de la classe), la resta de valors tal qual són, sense modificar.
        int tInc_mut = this.tempsIncubacio();
        int tLat_mut = this.tempsLatencia();
        int tImm_mut = this.tempsImmunitat();
        double pMutEC_mut = this.probabilitatMutacioErrorCopia();
        FamiliaVirus fam_mut =  this.familia();

        // Hem de crear un nou nom amb sufix (ex: grip_1).
        String nouNom = this.nom() + "no ser si fer-ho amb un contador per exemple";

        // Retornem el nou virus ARN mutat
        return new VirusARN(nouNom, fam_mut , pMal_mut, tInc_mut, tLat_mut, pMor_mut,
                tCon_mut, pCon_mut, tImm_mut, pMutEC_mut);

    }


    /**
     * Fem servir aquest mètode quan hi ha una mutació per coincidència entre dos virus de la mateixa família.
     *
     * Tenim un virus A, del qual la persona ja està infectada d'aquest virus.
     * @param B Persona infectada de A, s'infecta també d'aquest virus.
     * @return C Un nou virus resultant de la combinació de A i de B, que agafarà característiques dels dos i formarà
     * part de la mateixa familia
     */

    @Override
    public VirusARN mutacio(Virus B) {
        // Pre: A i B han de pertànyer a la mateixa família de virus, i els dos han d'estar presents en la mateixa
        // persona infectada al mateix moment.
        // Post: Retorna "this" si no muta. Altrament, si muta, retorna una mutació per coincidència de "this" i B.
        // Aquest nou Virus C, pertany a la mateixa classe de A i de B, i tindrà característiques tant
        // de A com de B, i els seus paràmetres quedaran canviats. A més, la persona infectada passarà a tenir aquest
        // nou virus en comptes de A i de B. El nom d'aquest serà la concatenació dels noms de A i de B
    }



    // MÈTODES PRIVATS

    /**
     * Genera una variació percentual aleatòria entre -tpcMax i +tpcMax. Aquest representa la variació percentual
     *  que haurem d'aplicar després a un dels 4 paràmetres afectats per la mutació per error de còpia (pMal, pMor, pCon, tCon).
     */
    private int fem_tpc_de_variació(int tpcMax) {
    // Pre: tpcMax és el tant per cent màxim de variació permesa segons la família del virus. Ha de ser un
    // enter positiu, que vagi de 0 a 100 (per exemple, 20 indica un màxim de ±20%).
    // Post: retorna un valor enter dins l’interval [-tpcMax, +tpcMax]

        int total_valors = 2 * tpcMax + 1;  // Nombre total de valors possibles (-tpcMax fins a +tpcMax)
        double num_aleatori = Math.random();  // Calculem un num aleatori entre 0 i 1
        int num_aleatori_dins_possibilitats = (int) num_aleatori * total_valors;  // Valor entre 0 i (2*tpcMax + 1)
        int tpc_final = num_aleatori_dins_possibilitats - tpcMax;  // Movem el tpc perquè ha d'anar d'entre
        // -tpcMax fins a +tpcMax, i teniem de 0 a (2*tpcMax + 1)

        return tpc_final;
    }

    /**
     * Recalculem paràmetres de probabilitat aplicant una variació percentual.
     */
    private double aplicar_tcp_variació_prob(double valorInicial, int tcpVariació) {

    //Pre: El valor inicial ha de ser una probabilitat vàlida, és a dir, un valor entre 0 i 1. La tcpVariació és
    // un enter que representa el tant per cent de variació que es vol aplicar (pot ser positiu o negatiu).

    //Post: Retorna la nova probabilitat obtinguda després d’aplicar la variació percentual, però ens assegurem que
    // segueixi sent un valor vàlid entre 0 i 1.

        double multiplicador = (1 + (tcpVariació / 100.0));// Passem de tenir el tcp de variació, ha saber per quan
        // hem de multiplicar le nostre valor original. Per exemple, si tenim inicialment una tcpVarició de +10%,
        //haurem de multiplicar llavors per 1.10. En canvi, si tenim una tcpVarició de -20%, haurem de multiplicar per 0.80
        double prob_final = valorInicial * multiplicador;

                //CUIDADO PQ AIXO NO CAL PER EL DEL TEMPS !!

        // Com que és una probabilitat, ens hem d'assegurar de que vagi de 0 a 1.
        prob_final = Math.max(0.0, Math.min(1.0, prob_final));

        return prob_final;
    }

    /**
     * Recalculem paràmetres referents a períodes de temps aplicant una variació percentual.
     */
    private int aplicar_tcp_variació_temps(int valorInicial, int tcpVariació) {

        //Pre: El valor inicial representa un temps (en dies) que ha de ser ≥ 1. La tcpVariació és
        //un enter que representa el tant per cent de variació que es vol aplicar (pot ser positiu o negatiu)
        //Post:Retorna el valor modificat del paràmetre de temps, després d’aplicar la variació percentual indicada.

        double multiplicador = (1 + (tcpVariació / 100.0));
        int temps_final = (int) Math.round (valorInicial * multiplicador);

        return Math.max(1, temps_final); // Ens assegurem que el temps mínim sigui d'1 dia, ja que no te sentit que
        // un període de temps duri 0 o dies negatius.
    }

}