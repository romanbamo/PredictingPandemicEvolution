
import java.util.List;
import java.util.ArrayList;

public class Regio {
// F -> MIREIA, VAIG COMENTANT ENMIG DEL TEU CODI !



    /* La classe Regio
  * representa una àrea geogràfica on es poden estudiar els efectes d’un virus.
     * Aquesta classe serveix per emmagatzemar informació sobre la població d’una regió
     * i els contactes que tenen les persones entre elles, un factor important en la propagació d’un virus.
     */



    // --------------------------- ATRIBUTS DE LA CLASSE REGIO -------------------------------

    private String nom; // Nom de la regió
    private int poblacio; // Població total d’aquesta regió
    private boolean enConfinament; // Ens serveix per a saber si aquesta regió es troba actualment en confinament

    private double taxaContacteIntern;  // Probabilitat de contacte entre persones dins de la mateixa regió
    private List<Regio> regionsVeines;  // Llista de regions amb les quals aquesta regió té contacte

    private List<AfectacioVirusRegio> afectacions;
    // Crec que aquest atribut és un dels més importants, perquè és regió la que ha de coneixer les seves propies
    // afectacions. Perquè una regió pot estar infectada per varis virus, i de cada un nosaltres hem de tenir constància
    // de com està la població durant cada dia.
    // Per tant, per cada virus que està present a la regió, tindrem un objecte AfectacioVirusRegio que controla l’evolució.

    /**
     * Creem el constructor de la classe Regio.
     *
     * @param nom_regio Nom de la regió.
     * @param poblacio_reg Població total que té la regió.
     * @param taxa_contacte_interna Taxa de contacte entre persones dins de la mateixa regió.
     */
    public Regio(String nom_regio, int poblacio_reg, double taxa_contacte_interna) {
        // Pre: nom_regio ha de ser un string vàlid, la població ha de ser positiva i la taxa entre 0 i 1 (ja que és una probabilitat).
        // Post: Creem una nova regió amb la seva població, taxa de contacte interna, sense confinament inicial
        // i amb cap afectació ni regió veïna assignada de moment.

        nom = nom_regio;
        poblacio = poblacio_reg;
        taxaContacteIntern = taxa_contacte_interna;

        enConfinament = false;  // Crec que quan la creem, la definim sense confinament i després ja se li
        // assigna si en algun moment se li aplica un confinament.

        // Inicialitzem les llistes buides (encara no sabem amb quines regions fa contacte ni quins virus té afectant-la)
        regionsVeines = new ArrayList<>();
        afectacions = new ArrayList<>();
    }


    // ----------------------------------------- MÈTODES PÚBLICS -------------------------------------------------------

    public String nom() {
        // Pre: La regió ha de tenir un nom vàlid.
        // Post: Retorna un string que és el nom de la regió.
        return nom;
    }

    public int poblacio() {
        // Pre: La regió ha de tenir un nombre d'habitants vàlid.
        // Post: Retorna un enter que és el nombre d'habitants.
        return poblacio;
    }
    public taxaInternaContacte(){
        // Pre: La regió ha de tenir una taxa de contacte interna vàlida.
        // Post: Retorna un double que és la taxa de contacte interna.
        return taxaContacteIntern;

    } 
    public taxaExternaContacte(veina){
        // Pre: La regió ha de tenir una taxa de contacte externa vàlida.
        // Post: Retorna un double que és la taxa de contacte externa.
        return veina.taxaContacteIntern;

    }

    /**
     * Aquest mètode l'he fet per poder indicar amb quines regions veïnes té contacte aquesta regió.
     * És molt important perquè ho necessitarem després per calcular els contagis externs.
     * A més, la taxa de contacte externa amb aquesta regió pot ser diferent per a cada veïna.
     *
     * @param veina Regió veïna amb la qual es manté contacte.
     * @param taxaExternaContacte Taxa de contacte entre aquesta regió i la regió veïna (valor entre 0 i 1).
     */
    public void afegirRegioVeina(Regio veina, double taxaExternaContacte) {
        // Pre: veina ha de ser una regió vàlida i diferent de this. La taxa ha de ser un valor entre 0 i 1.
        // Post: Aquesta regió passa a tenir com a veïna la regió veina, amb la seva taxa de contacte.

        // He fet això per assegurar-me de que no poguem dir-li a una regió que és veïna d'ella mateixa.
        if (veina == this) {
            throw new IllegalArgumentException("Una regió no pot ser veïna d’ella");
        }

        // Això també és important per assegurar-nos de que no repetim, ja que si ja hem dit que una regió és veiïna,
        // doncs no podem tornar-la a afegir a la llista.
        if (!regionsVeines.contains(veina)) {
            regionsVeines.add(veina)
        }
    }


    /**
     * Aquesta funció serveix per afegir una nova afectació a la regió.
     * És a dir, quan arriba un nou virus, hem de crear una nova afectació perquè clar, el que volem és
     * tenir control sobre com evoluciona aquest virus en la regió.
     *
     * El que he fet és basicament crear un objecte de la classre AfectacióVirusRegió (cridant el constructor)
     * i llavors, doncs relacionar el virus amb la regió. I com ja hem comentat en la pròpia classe de
     * AfectacioVirusRegió, doncs és necessari passar-li també el nombre d'infectats inicials.
     * Després, per així tenir constància de l'evolució de cada virus que te la regió, doncs el que he fet és
     * afegir-la a la llista d’afectacions.
     */
    public void afegirNovaAfectacio(Virus virus, int infectats_inicials) {
        // Pre: El virus ha de ser vàlid i no ha d'existir ja una afectació seva en aquesta regió.
        // infectats_inicials ha de ser major o igual a 0.
        // Post: S’afegeix una nova afectació a la llista d’afectacions d’aquesta regió per controlar aquest virus.

        AfectacioVirusRegio novaAfectacio = new AfectacioVirusRegio(virus, this, infectats_inicials);
        afectacions.add(novaAfectacio);
    }
// PREGUNTA MEVA: QUAN UN VIRUS ACABA, TIPO JA DEIXA D'INFECTAR (Q AIXO NS COM ES TE CONSTÀNCIA), L'ELIMINEM DE LA LLISTA ?











    public void mostrarConfinament() {  
    // F-> IMPORTANT: CENTREU TOTES LES ENTRADES I SORTIDES (println) A LA CLASSE D'INTERACCIÓ. AQUEST MÈTODE NO TOCA AQUÍ 
        //Pre:-----
        //Post:Mostra en pantalla les regions en confinament
        if (enConfinament) {
            System.out.println("La regió " + nom + " està en confinament.");
        } else {
            System.out.println("La regió " + nom + " no està en confinament.");
        }
    }



    // Operacions
    public void afegirCasos(Virus virus, int nousInfectats) { // F-> AIXÒ MILLOR GESTIONAR-HO A NIVELL D'AFECTACIÓ (VIRUS-REGIÓ)
        // Afegeix nous casos d'un virus a la regió
        // Verifiquem que el nombre de nous infectats sigui vàlid
        if (nousInfectats < 0) {
            System.out.println("Error: Nombre de nous infectats no vàlid.");
        }

        // Evitem que el total d'infectats superi la població total
        int nousCasosReals = Math.min(nousInfectats, poblacio - casosActuals);
        casosActuals += nousCasosReals;

        System.out.println("S'han afegit " + nousCasosReals + " nous casos de " + virus.nom() + " a la regió " + nom + ".");
    }


    public void afegirConfinament(String confinament) { 
    // F-> NO VEIG CLAR AQUEST PARÀMETRE. JO FARIA UN MÈTODE public void confinamentDur SENSE PARÀMETRE O COM A MOLT AMB UN 
    //     BOOLEAN QUE INDIQUI SI ES VOL CONFINAR O DESCONFINAR 
        //Pre:
        //Post:Aplica un confinament a la regió
        // Marquem la regió com a confinada
        enConfinament = true;

        // Mostrem un missatge informant del confinament aplicat
        System.out.println("S'ha afegit confinament: " + confinament + " a la regió " + nom + ".");
    }


    public void aixecarConfinament() { // F-> AQUEST JA EL VEIG MILLOR (SENSE EL PRINTLN I SUPOSANT Q ÉS CONF. DUR)
        //Pre:
        //Post:Elimina el confinament a una regió
        // Eliminem el confinament
        enConfinament = false;

        // Mostrem un missatge informatiu
        System.out.println("S'ha aixecat el confinament de la regió " + nom );

    }
}

