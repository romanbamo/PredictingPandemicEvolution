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



    // PER SABER QUIN TIPUS DE VIRUS ÉS, podem fer --> guardar a nivell de virus, un array que fos de ADN i de ARN.

    // BONA MANERAA --> OVERRIDE (pero el POST ha de ser MES FORTT !! el que garantia el primer POST, ha de
    // cumplir-se 100%)

    // Pots fer metode mutar com a abstracte aqui, llavors fas un OVERRIDE i depen del que et retorni, pots
    // determinar si es tracta d'un virus ARN o ADN.

/** Explicació: He afegit dos metodes abstractes a la classe Virus que son de mutar.
 Aquests, hem fet una sobrecarrega, de manera que si passem per paràmetre un atribut,
 llavors es fa una, i si en passem dos, es fa un altre. L'hem escrit aquí, ja que com se'ns ha dit
 a classe, aquest mètode el podrem fer servir també per saber si el virus és ARN o ADN. Per fer-ho,
 haurem de fer un OVERRIDE posterior dins de cada subclasse, on s'implementaran. En el cas dels
 VirusARN, no passarà res perquè aquest virus SI que muten, mentres que en el cas dels ADN,
 haurem de fer saltar un error, de manera que també ens farà saber que es tracta d'un VirusADN.*/



    /**
     * Fem ús d'aquest mètode quan hi ha una mutació per error de còpia a un virus .
     * @param virus El virus ARN que muta.
     * @return El nou virus ARN amb les seves característiques modificades.
     */
    public abstract Virus mutacio (Virus virus);
    // Pre: El virus ha d'existir)
    // Post: Es genera un nou virus  de la mateixa família que l’original, amb els paràmetres modificats.
    // El nom d'aques virus serà el nom del virus que muta però amb un número correlatiu que indiqui el número
    // de vegades que ha mutat.

    /**
     * Fem servir aquest mètode quan hi ha una mutació per coincidència entre dos virus de la mateixa família.
     * @param A Persona ja està infectada d'aquest virus.
     * @param B Persona infectada de A, s'infecta també d'aquest virus.
     * @return C Un nou virus resultant de la combinació de A i de B, que agafarà característiques dels dos i formarà
     * part de la mateixa familia
     */
    public abstract Virus mutacio (Virus A,Virus B);
    // Pre: A i B han de pertànyer a la mateixa família de virus, i els dos han d'estar presents en la mateixa
    // persona infectada al mateix moment.
    // Post: Retorna un Virus C, que pertany a la mateixa classe de A i de B. Aquest, tindrà característiques tant
    // de A com de B, i els seus paràmetres quedaran canviats. A més, la persona infectada passarà a tenir aquest
    // nou virus en comptes de A i de B. El nom d'aquest serà la concatenació dels noms de A i de B


// METODES PRIVATS:


}
