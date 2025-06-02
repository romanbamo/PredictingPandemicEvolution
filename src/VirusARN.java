/**
 * @class VirusARN
 * @brief Representa un virus de tipus ARN, que pot mutar.
 * @author Iria Auladell
 */


import java.util.ArrayList;
import java.util.List;

public class VirusARN extends Virus implements Comparable<VirusARN> {

// ATRIBUTS

    private double pMutEC;  // Probabilitat de mutació per error de còpia
    private int numMutacions; // He afegit aquest contador per poder tenir constància dels cops que un virus muta,
    // ja que a la hora de crear el nom del nou virus, és necessari tenir constància dels cops que ha mutat.
    private List<VirusARN> mutacions; // Llista que guarda totes les mutacions d'aquest virus. Això ens serà
    // útil perquè quan hom assoleix immunitat d'un virus, tb la guanya de tots els virus que siguin mutació d'ell.

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
        numMutacions = 0;  // Quan el virus és nou, doncs encara no ha mutat cap cop
        mutacions = new ArrayList<>(); // Aquí el que fem és inicialitzar la llista on anirem posant
        // totes les mutacions d'aquest virus. Al principi, doncs no tindrà cap.

    }

    public double probabilitatMutacioErrorCopia() {
        // Pre: El virus ha de ser vàlid i existir
        // Post: Retorna la probabilitat de mutació per error de còpia d'aquest virus

        return pMutEC;
    }

    public List<VirusARN> obtenirMutacions() {
        // Pre: -
        // Post: Retorna la llista de mutacions que s'han creat a partir d'aquest virus.
        return new ArrayList<>(mutacions); // Retornem una còpia per no exposar la llista interna directament.
    }

    // Per quan implementem la classe de Mutació, és important assegurar-nos de que dos virus són
    // de la mateixa familia.
    public boolean pertanyenMateixaFamilia(VirusARN B) {
        // Pre: Virus A i Virus B han d’existir
        // Post: Retorna cert si A i B formen part de la mateixa familia, altrament retorna false

        return this.familia().esLaMateixaFamilia(B.familia());

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

        // Incrementem el comptador de mutacions per aquest virus
        this.numMutacions++; // modifiquem el atribut (crec que per això es recomana posar el this davant)

        // Creem el nom del nou virus mutat
        String nom_original = this.nom();  // Nom del virus original
        String nom_nou = nom_original + "_" + numMutacions;  // Nou nom del virus mutat (grip --> grip_1, grip_2...)

        // Retornem el nou virus ARN mutat
        VirusARN nou_virus = new VirusARN(nom_nou, fam_mut , pMal_mut, tInc_mut, tLat_mut, pMor_mut,
                tCon_mut, pCon_mut, tImm_mut, pMutEC_mut);

        this.afegim_mutacio(nou_virus); // Afegim el nou virus a la llista de mutacions.

        return nou_virus;

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
    // Post: Retorna una mutació per coincidència de "this" i B.
    // Aquest nou Virus C, pertany a la mateixa classe de A i de B, i tindrà característiques tant
    // de A com de B, i els seus paràmetres quedaran canviats. A més, la persona infectada passarà a tenir aquest
    // nou virus en comptes de A i de B. El nom d'aquest serà la concatenació dels noms de A i de B


        if (!(B instanceof VirusARN)){
            throw new IllegalArgumentException("Hi ha un virus que no és ARN, problema");
        }

        // ara que sabem que és un VirusARN, podem fer un casting per canviar el tipus aparent
        VirusARN B_ARN = (VirusARN) B; // càsting


        double tCon_num_aleatori = Math.random();

        // Calculem els nous valors per als paràmetres afectats amb la fórmula de la pràctica
        double pMal_mutacio = prob_afectada_per_mut_coincidència(this.probDesenvoluparMalaltia(), B_ARN.probDesenvoluparMalaltia());
        double pMor_mutacio = prob_afectada_per_mut_coincidència(this.taxaMortalitat(), B_ARN.taxaMortalitat());
        double pCon_mutacio = prob_afectada_per_mut_coincidència(this.taxaContagi(), B_ARN.taxaContagi());
        int tCon_mutacio = (int) Math.round(tCon_num_aleatori * this.tempsContagi() + (1 - tCon_num_aleatori) * B_ARN.tempsContagi());

        // La resta de paràmetres els obtinc fent la mitjana aritmètica entre els corresponents valors d’A i B.
        int tInc_mutacio = (this.tempsIncubacio() + B_ARN.tempsIncubacio()) / 2;
        int tLat_mutacio = (this.tempsLatencia() + B_ARN.tempsLatencia()) / 2;
        int tImm_mutacio = (this.tempsImmunitat() + B_ARN.tempsImmunitat()) / 2;
        double pMutEC_mutacio = (this.probabilitatMutacioErrorCopia() + B_ARN.probabilitatMutacioErrorCopia()) / 2;

        // Generem el nou nom del virus combinant els noms de "this" i B

        String nom_nou = generar_nom_mutacio(B_ARN);  // He decidit fer un mètode per assegurar-me de que
        // el nom que creem, és únic i no existeix (per mirar si hi ha hagut alguna mutació anterior amb els mateixos
        // virus i evitar tornar a fer servir el mateix nom).

        // Creem el nou virus ARN amb les noves característiques (combinació de les característiques del virus A i B)
        VirusARN virus_C_mutat = new VirusARN(nom_nou, this.familia(), pMal_mutacio, tInc_mutacio, tLat_mutacio,
                pMor_mutacio, tCon_mutacio, pCon_mutacio, tImm_mutacio, pMutEC_mutacio);

        this.afegim_mutacio(virus_C_mutat); // Afegim el nou virus a la llista de mutacions del virus A ("this).
        B_ARN.afegim_mutacio(virus_C_mutat); // Afegim el nou virus a la llista de mutacions del virus B.

        return virus_C_mutat;

    }

    /**
     * Aquí el que fem és fer ús del CompareTo, però el personalitzem per comparar dos virus segons la seva força.
     * Com ens diu el document, la força dels virus es calcula com (en ordre de prioritat):
     * 1. Taxa de contagi (pCon) - com més alta, millor.
     * 2. Probabilitat de desenvolupar la malaltia (pMal) - com més alta, millor.
     * 3. Taxa de mortalitat (pMor) - com més alta, millor.
     * 4. Nom del virus (ordre alfabètic).
     *
     * @param altreVirus El virus ARN amb el qual compararé el de la classe aquest.
     * @return Un valor negatiu si this < altreVirus, 0 si són iguals, positiu si this > altreVirus
     */
    @Override
    public int compareTo(VirusARN altreVirus) {
    // Pre: El Virus altreVirus ha d'existir i ser de tipus VirusARN
    // Post: Retorna quin dels dos virus és més fort segons uns certs criteris definits

        // Primer de tot comparem per taxa de contagi (pCon)
        int comparacio = Double.compare(this.taxaContagi(), altreVirus.taxaContagi());
        if (comparacio != 0) {
            return comparacio; //si no són iguals em retorna un int (depenent de si és positiu o negatiu,
            // llavors sabrem quin dels dos és més fort. Si és positiu, this és més fort, sino, altreVirus és més fort
        }

        // Si pCon són iguals, mirem la probabilitat de desenvolupar la malaltia (pMal)
        comparacio = Double.compare(this.probDesenvoluparMalaltia(), altreVirus.probDesenvoluparMalaltia());
        if (comparacio != 0) {
            return comparacio;
        }

        // Si pCon són iguals i pMal també, llavors passem a mirar la taxa de mortalitat (pMor)
        comparacio = Double.compare(this.taxaMortalitat(), altreVirus.taxaMortalitat());
        if (comparacio != 0) {
            return comparacio;
        }

        // Finalment, si totes les altres són iguals, mirem per ordre alfabètic (si tot l'anterior empata), ja que
        // mai hi haurà dos virus amb el mateix nom
        return this.nom().compareTo(altreVirus.nom());
        // PETIT CONCEPTE DE TEORIA:
        // Els Strings ja implementen la interfície Comparable<String> i ja tenen el seu propi mètode compareTo()
        // Aquest ja ve fet dins la classe String de Java.
        // public final class String implements Comparable<String> { ... }
    }

    public VirusARN VirusMesFort (VirusARN B){
        // Pre: Virus de la classe i Virus B han de ser ARN i han d'estar inicialitzats correctament
        // Post: Retornem el virus més fort dels dos seguint els criteris explicats en el compareTo

        if (this.compareTo(B) >= 0){
            return this;
        }
        else {
            return B;
        }
    }

// -------------------------------------------------------------------------------------------------------------------------------

// MÈTODES PRIVATS

// mètodes privats per al mètode de mutació per error de copia

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
        int num_aleatori_dins_possibilitats = (int) (num_aleatori * total_valors);  // Valor entre 0 i (2*tpcMax + 1)
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

    private void afegim_mutacio(VirusARN nou_virus) {
    // Pre: El paràmetre nou_virus ha de ser un objecte vàlid de la classe VirusARN (no pot ser null). A més,
    // aquest virus ha de ser una mutació generada a partir d'aquest virus original (és a dir, de la mateixa família).
    // Post: La nova mutació s'ha afegit a la llista de mutacions d'aquest virus. Aquesta llista manté un historial de totes
    // les mutacions que s'han creat a partir d'aquest virus, tant per error de còpia com per coincidència.
        mutacions.add(nou_virus); // Afegim la nova mutació a la llista de mutacions.
    }







// mètodes privats per al mètode de mutació per coincidència

    /**
     * Apliquem la fórmula de mutació per coincidència a les probabilitats dels paràmetres.
     *
     * @param valor_paràmetre_virus_A Prob d'un paràmetre del virus A (que és el de la classe --> "this").
     * @param valor_paràmetre_virus_B Prob d'un paràmetre del virus B.
     * @return El valor que ens dona després d'aplicar la fórmula de mutació per coincidència.
     *
     * Pre: valorA i valorB han de ser valors entre 0 i 1, ja que representen probabilitats.
     *      Es genera un valor aleatori `p` entre 0 i 1 que determina la influència de cada virus en la mutació.
     * Post: Retorna un valor entre 0 i 1 resultant de la fórmula de mutació per coincidència.
     */
    private double prob_afectada_per_mut_coincidència(double valor_paràmetre_virus_A, double valor_paràmetre_virus_B) {
    // Pre: probA i probB han de ser valors entre 0 i 1, ja que son probabilitats.
    // Post: Retorna un valor entre 0 i 1 que ens surt d'aplicar la fórmula de mutació per coincidència.

        // Generem un valor aleatori p entre 0 i 1 per aplicar la fórmula
        double p = Math.random();

        // Apliquem la fórmula
        double valor_paràmetre_virus_mutat = p * valor_paràmetre_virus_A + (1 - p) * valor_paràmetre_virus_B;

        // Ens assegurem que el resultat estigui entre 0 i 1 (ja que és una prob, no pot sortir d'aquests marges)
        return Math.max(0.0, Math.min(1.0, valor_paràmetre_virus_mutat));
    }


    /**
     * He decidit fer aquesta funció per a generar un nom únic per la mutació entre dos virus. A més, com se'ns
     * ha dit a la pràctica, és molt important assegurar que sempre es segueixi el mateix criteri per a unir el nom
     * dels dos virus (A i B). És el mateix que la mutació es faci sent per exemple grip (Virus A) i grip_2 (Virus B),
     * que que sigui grip (Virus B) i grip_2 (Virus A). I no podria ser que tinguessin noms diferents.
     *
     * Si el nom que fem ja existeix a la llista de mutacions, llavors he cregut que era bona idea que s'afegeixi un
     * índex incremental (_1, _2, _3, etc.) fins que sigui vàlid. (al igual que hem fet per les mutacions per error de còpia)
     *
     * @param B L'altre virus ARN que està en la mutació per coincidència.
     * @return Un nom únic per la mutació.
     *
     * Pre: El virus actual ("this") i virusB han de ser objectes vàlids de VirusARN i pertànyer a la mateixa família.
     * Post: Retorna un nom que combina els noms dels dos virus seguint un criteri coherent i únic, sense col·lisions.
     */
    private String generar_nom_mutacio(VirusARN B) {
    // Pre: El virus actual ("this") i virusB han de ser objectes vàlids de VirusARN i pertànyer a la mateixa família.
    // Post: Retorna un nom únic que combina els noms dels dos virus. Si ja existeix el nom, s'afegeix un índex incremental
    // segons el cops que ja s'ha fet aquesta mutació.

        // He cregut que era bona idea ordenar els noms perquè així, si son els mateixos dos virus, independentment
        // quin passem com a A i quin com a B, el nom que es creei sigui sempre el mateix.
        String nom_A = this.nom();
        String nom_B = B.nom();
        String nom_inicial;

        if (nom_A.compareTo(nom_B) <= 0) {  // Si nom_A és menor o igual a nom_B alfabèticament
            nom_inicial = nom_A + "_" + nom_B;
        } else {
            nom_inicial = nom_B + "_" + nom_A;
        }

        // Ara comprovem si el nom que hem fet existeix en la llista de mutacions.
        String nom_nou = nom_inicial; // Inicialment, el nom nou és igual al nom inicial (ajuntar els dos noms)
        int vegades_que_existeix = 1; // Comptador que s'anirà incrementant cada cop que trobem un nom duplicat

        // Aquí, anem afegint un al comptador fins que trobem un nom vàlid, que no estigui a la llista de mutacions
        while (!exiteix_nom_virus(nom_nou)) {
            // Si el nom ja existeix, afegim un guió baix i un número que indica les vegades q ja s'ha produit
            // una mutació per convivència d'aquests dos virus (grip_grip_2_1, grip_grip_2_2...)
            nom_nou = nom_inicial + "_" + vegades_que_existeix;

            vegades_que_existeix++;
        }

        // Retornem el nom nou que és vàlid
        return nom_nou;
    }

    /**
     * Ens assegurem de que el nom del virus mutat, ja existeix a la llista de mutacions d'aquest virus.
     * Així evitem que es creïn duplicats quan es generen nous virus per mutació per coincidència.
     *
     * @param nom_mutacio El nom de la mutació que volem veure si existeix o no.
     * @return Cert si el nom de la mutació és vàlid (és a dir, no existeix a la llista de mutacions). Fals altrament.*/
    private boolean exiteix_nom_virus(String nom_mutacio) {
    // Pre: nom_mutacio ha de ser un String vàlid i no nul.
    // Post: Retorna true si la llista de mutacions no té cap virus mutat amb aquest nom, altrament retorna false si ja existeix.
        for (VirusARN mutacio : mutacions) {
            if (mutacio.nom().equals(nom_mutacio)) {
                return false;  // El nom ja existeix a la llista de mutacions (ja ha passat aquesta mutació per convivència)
            }
        }
        return true;  // No existeix aquest nom, i per tant és el primer cop que passa aquesta mutació
    }

    @Override
    public boolean muta() {
    // Pre: El virus ha d’estar inicialitzat correctament.
    // Post: Retorno cert, ja que els virus ARN poden mutar.
        return true;
    }

}