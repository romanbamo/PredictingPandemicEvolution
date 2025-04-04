public class Regio {



    /* La classe Regio
  * representa una àrea geogràfica on es poden estudiar els efectes d’un virus.
     * Aquesta classe serveix per emmagatzemar informació sobre la població d’una regió
     * i els contactes que tenen les persones entre elles, un factor important en la propagació d’un virus.
     */
    //ATRIBUTS
    private String nom;
    private int poblacio;
    private boolean enConfinament;
    private int casosActuals;
    // Nombre de casos actius a la regió

    public Regio(String nom_regio, int poblacio_reg, boolean enConfinament, int casosActuals){
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

    public int casosActuals() {
        return casosActuals;
    }

    public void mostrarConfinament() {
        //Pre:-----
        //Post:Mostra en pantalla les regions en confinament
        if (enConfinament) {
            System.out.println("La regió " + nom + " està en confinament.");
        } else {
            System.out.println("La regió " + nom + " no està en confinament.");
        }
    }



    // Operacions
    public void afegirCasos(Virus virus, int nousInfectats) {
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
        //Pre:
        //Post:Aplica un confinament a la regió
        // Marquem la regió com a confinada
        enConfinament = true;

        // Mostrem un missatge informant del confinament aplicat
        System.out.println("S'ha afegit confinament: " + confinament + " a la regió " + nom + ".");
    }


    public void aixecarConfinament() {
        //Pre:
        //Post:Elimina el confinament a una regió
        // Eliminem el confinament
        enConfinament = false;

        // Mostrem un missatge informatiu
        System.out.println("S'ha aixecat el confinament de la regió " + nom );

    }
}

