/**
 * @class AfectacioVirusRegio
 * @brief Controla l’evolució d’un virus en una regió concreta dins la simulació.
 * @details Gestiona la infecció en una regió seguint l’evolució temporal del virus. Es tenen en compte:
 *   - Infectats: persones que han contret el virus.
 *   - Contagiosos: infectats que poden transmetre’l.
 *   - Malalts: infectats que desenvolupen símptomes.
 *   - Nous infectats: persones contagiades en l’últim dia.
 *   - Immunitzats: persones recuperades.
 *   - Morts: persones que han mort per culpa de la malaltia.
 *
 *  Cada dia s’actualitza l’estat de la regió:
 *   - Es calculen nous contagis segons la taxa de transmissió (P_con) i la mobilitat.
 *   - Es distribueixen les morts proporcionalment al llarg dels dies de malaltia.
 *   - Es mantenen estadístiques acumulades des de l’inici de la simulació.
 *
 *  La simulació treballa en mode text i permet consultar l’estat actual i l’evolució històrica.
 *
 * @author Iria Auladell
 * @version 2025.3.13
 */

public class AfectacioVirusRegio {

    // ATRIBUTS (idea principal, falta canviar moltes coses segurament)

    // Virus i regió que han estat afectats
    private Virus virus;         // Virus que afecta la regió
    private Regio regio;         // Regió infectada

    // Estadístiques que hem de tenir del dia actual (a nivell de grup)
    private int infectats;       // Total de persones infectades
    private int contagiosos;     // Persones que poden transmetre el virus
    private int malalts;         // Persones que han desenvolupat símptomes
    private int nousInfectats;   // Persones contagiades
    private int novesDefuncions; // Persones que han mort des del dia anterior
    private int immunes;     // Persones que ja s'han curat i són immunes

    // Estadístiques que hem d'anar acumulant
    private int totalMalalts;     // Total de persones que han estat malaltes
    private int totalMorts;       // Total de persones mortes
    private int totalContagis;    // Total de contagis que s'han fet
    private int totalInfectats;   // Total de persones que s'han infectat

    // SEGUR QUE FALTEN --> hem de decidir quines categories requereixen aquest
    //“repartiment” temporal del nombre d’afectats (infectats, contagiosos, immunes,...). Potser Morts ?

    // MÈTODES

    /**
     * Constructor de la classe AfectacioVirusRegio.
     * @param virus Virus que afecta la regió.
     * @param regio Regió que és infectada per el virus.
     */
    public AfectacioVirusRegio(Virus virus, Regio regio, int numero_infectats) {
        // Pre: virus ha de ser un objecte vàlid de la classe Virus, i regio ha de ser una regió
        // que existeixi.
        // Creem una zona concreta que està afectada per un virus concret.
    }

    /**
     * Que fa ? Si avancem la simulació un dia, llavors s'actualitza l’estat de la infecció en aquesta la regió.
     */
    public void avançarUnDia(){
        // Pre: La simulació s'ha d'estar executant
        // Post: S'actualitza l'estat, calculant de nou els nous contagis, morts, persones immunes, segons
        // les característiques de la regió i del virus que està afectant.
    }

    // Hem fet aquest mètode per poder mostrar l'estat en que es troba la regió infactada
    public void mostrarEstatActual(){
        // Pre: -
        // Post: Mostrem per pantalla, com ens diu la pràctica, en mode text, el nombre d’infectats, contagiosos,
        // malalts, nous infectats, immunitzats i morts.
    }


    public void mostrarEvolucio(){
        // Pre: -
        // Post: Mostrem per pantalla el total de malalts, morts i contagis acumulats.
    }

    // afegircasosvirus a la regio (metode d'afegir-lo)
    // dos metodes dif (confinament dur i tou) i per desconfinament igual
}

