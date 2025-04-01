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

import java.util.List;
import java.util.ArrayList;

public class AfectacioVirusRegio {

    // ATRIBUTS (idea principal, falta canviar moltes coses segurament)

    // Virus i regió que han estat afectats
    private Virus virus;         // Virus que afecta la regió
    private Regio regio;         // Regió infectada

    // Estat actual (atrbibuts que requereixen un histograma, és a dir, hem de saber la distribució per dies de cada fase)
    private List<Integer> _infectats; // nombre d'infectats a cada dia de la infecció
    private List<Integer> _malalts;     // nombre de malalts a cada dia de la infecció
    private List<Integer> _immunes;     // nombre d'inmunes a cada dia de la infecció
    private List<Integer> _contagiosos; // nombre de contagiosos a cada dia de la infecció

    // SEGUR QUE FALTEN --> hem de decidir quines categories requereixen aquest
    //“repartiment” temporal del nombre d’afectats (infectats, contagiosos, immunes,...). Potser Morts ?

    // Altres estadístiques que hem de tenir del dia actual (a nivell de grup)
    private int contagiosos;     // Persones que poden transmetre el virus (calculat a partir del temps de latència)
    /** ns si contagiosos seria una llista o no*/
    private int nousInfectats;   // Persones contagiades
    private int novesDefuncions; // Persones que han mort des del dia anterior

    // Estadístiques que hem d'anar acumulant
    private int totalMalalts;     // Total de persones que han estat malaltes
    private int totalMorts;       // Total de persones mortes
    private int totalContagis;    // Total de contagis que s'han fet
    private int totalInfectats;   // Total de persones que s'han infectat



    // MÈTODES

    /**
     * Constructor de la classe AfectacioVirusRegio.
     * @param v Virus que afecta la regió.
     * @param r Regió que és infectada per el virus.
     */
    public AfectacioVirusRegio(Virus v, Regio r, int numero_infectats) {
        // Pre: virus ha de ser un objecte vàlid de la classe Virus, i regio ha de ser una regió
        // que existeixi.
        // Creem una zona concreta que està afectada per un virus concret.

        // Inicialitzem els atributs
        virus = v;
        regio = r;

        // Inicialitzem les llistes per als histogrames
        _infectats = new ArrayList<>();
        _malalts = new ArrayList<>();
        _immunes = new ArrayList<>();
        _contagiosos = new ArrayList<>();

        // Inicialitzem les estadístiques acumulades
        totalMalalts = 0;
        totalMorts = 0;
        totalContagis = 0;
        totalInfectats = numero_infectats;

        // Inicialitzem els infectats actuals a partir del número d'infectats que es passa com a paràmetre
        _infectats.add(numero_infectats);
    }

    /**
     * Que fa ? Si avancem la simulació un dia, llavors s'actualitza l’estat de la infecció en aquesta la regió.
     */
    public void avançarUnDia(){
        // Pre: La simulació s'ha d'estar executant
        // Post: S'actualitza l'estat, calculant de nou els nous contagis, morts, persones immunes, segons
        // les característiques de la regió i del virus que està afectant.


        // ---------------------- Calculo nous contagis --------------------
        // Primer de tot, calculem nous contagis

        // He decidit que la posició 0 de la llista _infectats representa el dia actual, és a dir, avui.
        // Sempre que avancem un dia en la simulació, col·loquem els nous infectats al principi de la llista,
        // i els elements que ja estaven es desplacen una posició cap a la dreta.
        // Posició 0: Avui (els nous contagis que acabem de calcular), Posició 1: Ahir, Posició 2: Abans d'ahir (i així successivament...).
        int nous_contagis = virus.nousContagis(_infectats.get(0));  // Utilitzem el mètode de Virus per calcular nous contagis
        _infectats.add(0, nous_contagis);  // Els nous infectats es col·loquen al principi de la llista
        totalInfectats = totalInfectats + nous_contagis;
        totalContagis = totalContagis + nous_contagis;

        // -------------------- Actualitzo els malalts --------------------
        actualitzar_malalts();  // He decidit fer-ho amb un mètode privat perquè així tingui menys codi la funció i s'entengui més.

        // -------------------- Actualitzem els immunes --------------------
        actualitzar_immunes();  // També ho he passat a un mètode privat perquè quedi tot més clar.

        // IDEA DE COSES QUE FALTEN PER FER:

        // Per ara, només hem implementat les parts que controlen infectats, malalts i immunes.
        // Encara falta:
        // - Mirar com tenir constància dels contagiosos (si m'aclaro i tinc idea de com poder-los mantenir com a llista. Tot i que segurament els acabi fent com a acomulats).
        // - Fer el càlcul de morts i de la seva distribució.

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

// ------------------------------------------------------------------------------------------------------------------------

    // MÈTODES PRIVATS:

    // Per el mètode d'avançar un dia

    /**
     * Actualitza la llista de malalts segons el temps d'incubació del virus.
     * Si ja ha passat el temps d'incubació d'un infectat, aquest passa a ser malalt.
     *
     * Com que mantenim l'historial de cada estat (infectats, malalts, contagiosos, immunes)
     * amb un esquema on la posició 0 representa el dia actual, hem de moure els infectats
     * que ja han passat la incubació a la llista de malalts.
     */
    private void actualitzar_malalts() {
    // Pre: La llista _infectats ha d'estar inicialitzada.
    // Post: Si la llista _infectats és major que el temps d'incubació del virus, vol dir que les persones del primer dia ja han
    // esdevingut malaltes i les movem de la llista _infectats a la llista malalts. Per tant, actualitzem la llista `malalts`
    // amb el nombre de nous malalts al dia actual (posició 0). A més, també sumem aquests nous malalts al total de malalts acumulats.
        if (_infectats.size() > virus.tempsIncubacio()) {
            int nous_malalts = _infectats.remove(_infectats.size() - 1); // Traiem els infectats que ja han completat la incubació
            _malalts.add(0, nous_malalts); // Els afegim a la llista de malalts al dia actual (posició 0)
            totalMalalts += nous_malalts; // Actualitzem el total de malalts
        } else {
            _malalts.add(0, 0); // Si no hi ha nous malalts avui, afegim un 0 a la llista de malalts
        }
    }

    /**
     * Aquest mètode és per actualitzar la llista d'immunes segons el temps d'immunitat del virus.
     * Quan un malalt ja ha passat el temps d'immunitat complet, aquest passa a ser considerat immune.
     *
     * Aquí, he considerat que el temps d'immunitat especificat pel virus (per exemple, 10 dies) indica
     * que la persona no serà immune fins que hagi passat completament aquest temps. Per tant, hem de
     * deixar passar aquests dies sencers i, només un cop els hagi completat, la considerarem immune.
     *
     * Per tant, jo he assumit que si per exemple tenim un temps d'immunitat de 10 dies, la persona només serà immune al dia següent,
     * és a dir, al dia 11. */
    private void actualitzar_immunes() {
    // Pre: La llista _malalts ha d'estar inicialitzada i amb valors vàlids.
    // Post: En el cas de que la llista malalts tingui tants dies registrats com el temps d'immunitat, llavors vol dir
    // que els malalts del primer dia registrat ja han passat suficient temps, i ja els podem considerar inmunes.
    // Per tant, els nous immunes els movem de la llista _malalts a la llista _immunes.

        int temps_immunitat = virus.tempsImmunitat();

        if (_malalts.size() > temps_immunitat) {
            int nous_immunes = _malalts.remove(_malalts.size() - 1); // Els malalts que han superat la immunitat es consideren immunes.
            _immunes.add(0, nous_immunes);  // Afegim els nous immunes a la posició 0 de la llista.
        } else {
            _immunes.add(0, 0); // Si no hi ha nous immunes avui, afegim un 0.
        }
    }

}

