/**
 * @class Virus
 * @brief Implementem una classe abstracta per definir el comportament general dels virus.
 * @details Aquesta classe serveix com a base per a diferents tipus de virus (ADN i ARN).
 * En aquesta hi ha una sèrie de mètodes sense implementar-les directament.
 * Una classe abstracta no es pot instanciar directament i només proporciona
 * una estructura que altres classes han d’implementar.
 *
 * @author Iria Auladell
 * @version 2025.3.13
 */

// Com que tots els virus comparteixen atributs comuns, hem decidit fer una classe abstracte de manera que
// d'ella heretin dos tipus de Virus, els que muten (ARN) i els que no muten (ADN)

// El fet que una classe sigui abstracta només implica que no es pot instanciar directament i que pot contenir
// mètodes abstractes (és a dir, mètodes que no tenen implementació i que han de ser sobreescrits per les subclasses)

public abstract class Virus {

//ATRIBUTS

    // Per poder identificar el virus:
    private String nom;
    private FamiliaVirus familia;

    // Per saber l'efecte que tenen sobre els humans:
    private double pMal; // Probabilitat que una persona infectada amb el virus desenvolupi la malaltia (0-1)
    private int tInc; // Temps d’incubació (dies)
    private int tLat; // Temps de latència (dies)
    private double pMor; // Taxa de mortalitat (0-1)
    private int tCon; // Durada mitjana del període de contagi (dies)
    private double pCon; // Taxa de contagi (0-1)
    private int tImm; // Durada del període d’immunitat (dies)


// METODES PÚBLICS

    /**
     * Primer és important fer el constructor de la classe Virus.
     *
     * @param nom_virus     Nom del virus.
     * @param familia_virus Família a la qual pertany el virus (de tipus FamiliaVirus).
     * @param p_mal         Probabilitat que una persona infectada desenvolupi la malaltia.
     * @param t_inc         Temps d’incubació
     * @param t_lat         Temps de latència
     * @param p_mor         Taxa de mortalitat
     * @param t_con         Durada mitjana del període de contagi
     * @param p_con         Taxa de contagi
     * @param t_imm         Durada del període d’immunitat
     */

    // Hem posat probabilitats com a double i les que són de temps, com a int perquè no seran 2,5 dies
    public Virus(String nom_virus, FamiliaVirus familia_virus, double p_mal, int t_inc, int t_lat, double p_mor,
                 int t_con, double p_con, int t_imm) {
        // Pre: nom i familia no poden estar buits, i els valors numèrics han de ser positius (no tindrem
        // períodes de temps o probabilitats negatives). A més, les probabilitats han de ser valors entre 0 i 1
        // Post: Creem un nou virus amb les característiques que ens han passat.

        /** falta validar que estiguin ben posats **/

        nom = nom_virus;
        familia = familia_virus;
        pMal = p_mal;
        tInc = t_inc;
        tLat = t_lat;
        pMor = p_mor;
        tCon = t_con;
        pCon = p_con;
        tImm = t_imm;

    }

// Primer de tot hem fet els mètodes que ens serviràn per poder retornar algun atribut
// de la classe Virus, i així podem utilitzar-los en altres classes

// Per exemple, si volem fer calculs de la gent que morirà... doncs hem de tenir
// conciència de quina probabilitat de Mortalitat te.


    public String nom(){ // En comptes de getNom()
        // Pre: El virus ha de tenir un nom vàlid.
        // Post: Retorna un string que és el nom del virus.

        return nom;
    }

    public FamiliaVirus familia() {
        // Pre: El virus ha de tenir una familia vàlida.
        // Post: Retorna a quina familia pertany.

        return familia;
    }

    public double probDesenvoluparMalaltia() {
        // Pre: El virus ha de ser vàlid i existir
        // Post: Retorna un valor entre 0 i 1 que representa
        // la probabilitat de desenvolupar la malaltia.

        return pMal;
    }

    public int tempsIncubacio() {
        // Pre: El virus ha de ser vàlid i existir, i ha de tenir un temps d'incubació
        // Post: Retorna el temps d'incubació. És a dir, període de temps mig
        // transcorregut entre que una persona contreu el virus
        // i comencen els símptomes de la malaltia

        return tInc;
    }

    public int tempsLatencia() {
        // Pre: El virus ha de ser vàlid i existir, i ha de tenir un temps de lactància
        // Post: Retorna el temps de lactància. És a dir, període de temps mig transcorregut
        // entre que una persona contreu el virus i pot començar a contagiar-lo.

        return tLat;
    }

    public double taxaMortalitat() {
        // Pre: El virus ha de ser vàlid i existir
        // Post: Retorna un valor entre 0 i 1 que representa
        // que una persona malalta per efecte del virus mori a causa de
        // la malaltia en qüestió.

        return pMor;

    }

    public int tempsContagi() {
        // Pre: El virus ha de ser vàlid i existir, i ha de tenir un temps de contagi
        // Post: Retorna el temps de contagi. És a dir, període de temps mig durant el qual la persona
        //infectada pot contagiar el virus a d’altres persones

        return tCon;

    }

    public double taxaContagi() {
        // Pre: El virus ha de ser vàlid i existir
        // Post: Retorna un valor entre 0 i 1 que representa la probabilitat que una
        // persona infectada, en període de contagi, li passi el virus
        // a una altra persona no infectada amb qui ha mantingut un contacte directe

        return pCon;

    }

    public int tempsImmunitat() {
        // Pre: El virus ha de ser vàlid i existir, i ha de tenir un temps d'immunitat
        // Post: Retorna el temps d'immunitat. És a dir, temps mig que una persona és immune
        // al virus (i a les seves possibles mutacions) un cop curada

        return tImm;
    }

    public boolean haMutat (Virus x){
        // Pre: El Virus x ha de ser vàlis i ha d'existir
        // Post: Retorna cert si el virus de la classe "this" és una mutació del virus x. Altrament, retorna false.
        // Si no ha mutat, el nom del virus serà igual.
        return !nom.equals(x.nom());
    }


    /** mètode que calcula el nombre esperat de nous contagis, basant-se en el nombre actual d'infectats i la taxa
     * d'infecció. */
    public int nousContagis (int infectats_actuals){
        // Pre: El nombre d'infectats ha de ser un enter positiu, i la taxa de contagi ha d'estar entre 0 i 1.
        // Post: Retorna un enter que representa el nombre esperat de nous contagis,
        // arrodonit al valor enter més proper.

        // Calculo el nombre esperat de nous contagis, segons s'ha dit a classe: nombre actual d'infectats * la taxa
        // d'infecció

        double nous_contagis_sense_arrodonir = infectats_actuals * pCon;

        // Crec que és necessàri passar-ho a enter, ja que no pot haver-hi dos persones i quart per exemple.
        // He decidit arrodonir cap al nombre més proper.
        int nous_contagis = (int) Math.round(nous_contagis_sense_arrodonir);

        return nous_contagis;
    }

    /** mètode que calcula el nombre esperat de nous malalts, basant-se en el nombre actual de contaiosos i la prob
     * de desenvolupar la malaltia. */
    public int nousMalalts (int contagiosos_actuals){
        // Pre: El nombre de contagiosos ha de ser un enter positiu, i la prob de desenvolupar la malaltia ha d'estar entre 0 i 1.
        // Post: Retorna un enter que representa el nombre esperat de nous malalts,
        // arrodonit al valor enter més proper.

        double nous_malalts_sense_arrodonir = contagiosos_actuals * pMal;
        int nous_malalts = (int) Math.round(nous_malalts_sense_arrodonir);

        return nous_malalts;
    }




    /**
     * He fet aquest mètode per a calcular el temps de la malaltia com a:
     * T_malaltia = T_lat + T_contagi - T_incubacio
     * Això representa el temps entre que la persona es posa malalta fins que deixa d'estar malalta (es cura o mor).
     * Perquè jo m'entengui, basicament és el temps en que està malalta la persona, és a dir, el contagi dura X temps
     * però clar, d'aquest temps hi pot haver uns dies on la persona si que contagia però NO te sintomes, ja que ens
     * trobem en una situació on el temps d'incubació (temps fins q aparèixen sintomes) és major al temps de latència (temps que passa fins que comença a contagiar).
     *
     */
    public int tempsMalaltia() {
        // Pre: Els temps de latència, contagi i incubació han d'estar ben definits (≥ 0).
        // Post: Retornem la durada del període de malaltia.
        int durada = tLat + tCon - tInc;a:
        return durada;
    }

    /**
     * He fet aquest mètode per a calcular el temps en que és contagios però encara no te sintomas:
     * T_contagi_sense_sintomes = T_incubacio - T_lat
     */
    public int tempsContagiSenseSintomes() {
        // Pre: Els temps de latència i incubació han d'estar ben definits (≥ 0).
        // Post: Retornem el temps que és contagios però encara no té sintomes.
        int durada = tInc - tLat;
        return durada;
    }



/** Explicació: He afegit dos metodes abstractes a la classe Virus que son de mutar.
 Aquests, hem fet una sobrecarrega, de manera que si passem per paràmetre un atribut,
 llavors es fa una, i si en passem dos, es fa un altre. L'hem escrit aquí, ja que com se'ns ha dit
 a classe, aquest mètode el podrem fer servir també per saber si el virus és ARN o ADN. Per fer-ho,
 haurem de fer un OVERRIDE posterior dins de cada subclasse, on s'implementaran. En el cas dels
 VirusARN, no passarà res perquè aquest virus SI que muten, mentres que en el cas dels ADN,
 haurem de fer saltar un error, o alguna cosa per saber que es tracta d'un VirusADN.*/


    /**
     * Fem ús d'aquest mètode quan hi ha una mutació per error de còpia a un virus .
     * @return un virus de la mateixa família que l'original
     */
    public abstract Virus mutacio ();
    // Pre: El virus ha d'existir (no pot ser null).
    // Post: Retorna un virus de la mateixa família que l'original. Pot ser un nou virus mutat o el mateix virus "this"
    // si no hi ha mutació. En el cas que sigui un nou virus mutat, aquest tindrà els paràmetres modificats, i el
    // seu nom serà el nom de "virus" però amb un número correlatiu que indiqui el número de vegades que ha mutat.

    /**
     * Fem servir aquest mètode quan hi ha una mutació per coincidència entre dos virus de la mateixa família.
     * El virus de la classe és un virus A, i la persona ja està infectada d'aquest virus.
     * @param B Persona infectada de A, s'infecta també d'aquest virus.
     * @return C virus de la mateixa família que A i B. Pot ser un nou virus que combini característiques d'ambdós,
     * o un dels dos virus si no es produeix mutació.
     */
    public abstract Virus mutacio (Virus B);
    // Pre: A i B han d'existir, pertànyer a la mateixa família de virus, i els dos han d'estar presents en la mateixa
    // persona infectada al mateix moment.
    // Post: Retorna un virus de la mateixa família que A i B. Pot ser un nou virus que combini característiques d'ambdós,
    // o "this" si no es produeix mutació.

    /**
     * Indica si el virus pot mutar per error de còpia.
     * @return Cert si és un virus de tipus ARN (mutable). Fals en cas contrari.
     */
    public abstract boolean muta();
    // Pre: El virus ha d’estar inicialitzat correctament.
    // Post: Retorna cert si el virus és de tipus ARN, fals si és de tipus ADN.

    // METODES PRIVATS:


}
