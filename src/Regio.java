public class Regio {
// F -> MIREIA, VAIG COMENTANT ENMIG DEL TEU CODI !



    /* La classe Regio
  * representa una àrea geogràfica on es poden estudiar els efectes d’un virus.
     * Aquesta classe serveix per emmagatzemar informació sobre la població d’una regió
     * i els contactes que tenen les persones entre elles, un factor important en la propagació d’un virus.
     */
    //ATRIBUTS
    private String nom;
    private int poblacio;
    private boolean enConfinament;
    private int casosActuals; // F-> AQUEST ATRIBUT NO ÉS NECESSARI, NO CAL AJUNTAR ELS CASOS DE TOTS ELS VIRUS
    // Nombre de casos actius a la regió
    // F-> ET FALTA LA TAXA DE INTERNA DE CONTACTES I TOT EL QUE FA REFERÈNCIA A REGIONS VEÏNES !

    public Regio(String nom_regio, int poblacio_reg, boolean enConfinament, int casosActuals){ 
    // F-> REVISAR EN FUNCIÓ DELS ATRIBUTS
        this.nom = nom_regio;
        this.poblacio = poblacio_reg;
        this.enConfinament = false;
        this.casosActuals = 0;
    }


    // MÈTODES

    //con

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

    public int casosActuals() { // F-> NO !
        return casosActuals;
    }

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

