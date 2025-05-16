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

// -------------------------------  ATRIBUTS (idea principal, falta canviar moltes coses segurament) --------------------------------

    // Virus i regió que han estat afectats
    private Virus virus;         // Virus que afecta la regió
    private Regio regio;         // Regió infectada

    // Estat actual (atrbibuts que requereixen un histograma, és a dir, hem de saber la distribució per dies de cada fase)
    private List<Integer> _infectats_no_contagiosos; // nombre d'infectats nous a cada dia de la infecció, que encara no han passat el temps de latència
    private List<Integer> _malalts;     // nombre de malalts a cada dia de la infecció
    private List<Integer> _immunes;     // nombre d'inmunes a cada dia de la infecció
    private List<Integer> _contagiosos; // nombre de contagiosos a cada dia de la infecció

    // SEGUR QUE FALTEN --> hem de decidir quines categories requereixen aquest
    //“repartiment” temporal del nombre d’afectats (infectats, contagiosos, immunes,...). Potser Morts ?


    // Estadístiques que hem d'anar acumulant
    private int totalMalalts;     // Total de persones que han estat malaltes al llarg de la malaltia
    private int totalMorts;       // Total de persones mortes al llarg de la malaltia
    private int totalInfectats;   // Total de persones que s'han infectat al llarg de la malaltia

    // Altres coses que he de controlar
    private List<Integer> mortsDiaries;  // Aquest vector l'he fet per saber les morts que s'espera tenir cada
    // dia

// ----------------------------------------------- MÈTODES PÚBLICS ------------------------------------------------------------------

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
        _infectats_no_contagiosos = new ArrayList<>();
        _malalts = new ArrayList<>();
        _immunes = new ArrayList<>();
        _contagiosos = new ArrayList<>();
        mortsDiaries = new ArrayList<>();

        // Inicialitzem les estadístiques acumulades
        totalMalalts = 0;
        totalMorts = 0;
        totalInfectats = numero_infectats;

        // Inicialitzem els infectats actuals a partir del número d'infectats que es passa com a paràmetre
        _infectats_no_contagiosos.add(numero_infectats);
    }

    /**
     * Que fa ? Si avancem la simulació un dia, llavors s'actualitza l’estat de la infecció en aquesta la regió.
     */
    public void avançarUnDia(){
        // Pre: La simulació s'ha d'estar executant
        // Post: S'actualitza l'estat, calculant de nou els nous contagis, morts, persones immunes, segons
        // les característiques de la regió i del virus que està afectant.

        // -------------------- Actualitzem els immunes ---------------------------------------------------------------
        actualitzar_immunes();  // Ho he passat a un mètode privat perquè quedi tot més clar.

        // ---------------------- Calculo nous contagiosos (si ja ha passat el temps de latencia) --------------------

        // He decidit que la posició 0 de la llista representa el dia actual, és a dir, avui.
        // Sempre que avancem un dia en la simulació, col·loquem els nous infectats al principi de la llista,
        // i els elements que ja estaven es desplacen una posició cap a la dreta.
        // Posició 0: Avui (els nous contagiosos que acabem de calcular), Posició 1: Ahir, Posició 2: Abans d'ahir (i així successivament...).

        int temps_no_pot_contagiar = virus.tempsLatencia();
        int nous_contagiosos = 0;
        // Perquè un infectat pugui començar a contagiar, ha hagut de passar el temps de latència
        if (_infectats_no_contagiosos.size() == temps_no_pot_contagiar) {
            nous_contagiosos = _infectats_no_contagiosos.remove(_infectats_no_contagiosos.size() - 1);
        }
            _contagiosos.add(0, nous_contagiosos);

        // -------------------- Actualitzo els malalts --------------------
        actualitzar_malalts();  // He decidit fer-ho també amb un mètode privat perquè així tingui menys codi la funció i s'entengui més.

        // -------------------- Calculo les morts d'avui --------------------
        calcula_quants_moren(_malalts.get(0));  // Calculem els morts d'avui a partir dels malalts d'avui

        // -------------------- Aplicar les morts calculades --------------------
        actualitzar_morts();

        // -------------- Calculem nous infectats a partir dels contagiosos -------------------------------------------
        actualitzar_infectats_no_contagiosos();

    }

    // MÈTODES PER FER TOT LO DE LES MUTACIONS

    // mutacio per error de còpia

    /**
     * Aquesta funció el que fa és mirar si en el dia d'avui, es produeix una mutació del virus que afecta la regió.
     * Ho fem a partir de la fórmula que ens han donat a l'annex. En cas que sí que hi hagi mutació, es calcula el
     * nou virus mutat, i s'actualitza l'afectació de la regió per aquest nou virus.
     */
    public void comprovarMutacioErrorCopia() {
        // Pre: virus ha de ser un objecte vàlid. El sistema ha d'estar en el moment d'avançar un dia (estat actual).
        // Post: Si hi ha mutació, es crea un nou virus i es passa a afectar la regió amb aquest nou virus.
        // Si no hi ha mutació, no es fa res i el virus continua sent el mateix.

        // Els virus que no són ARN no poden mutar per error de còpia
        if (!(virus instanceof VirusARN)) return;

        VirusARN virusARN = (VirusARN) virus;

        // --------------------------- Primer calculem el nombre de nous contagis d’avui ----------------------------

        // Ens basem en els nous infectats afegits avui a la posició 0 del vector d’infectats

        int nousContagis  = 0;

        if (!_infectats_no_contagiosos.isEmpty()){
            nousContagis = _infectats_no_contagiosos.get(0);
        }

        // Si no hi ha hagut contagis, no pot haver-hi mutació per error de còpia
        if (nousContagis == 0) {
            return;
        }

        // --------------------------- Calculem la probabilitat de mutació  --------------------

        double tm = virusARN.probabilitatMutacioErrorCopia();
        double pm = tm * nousContagis;

        // Si és major a 1, doncs llavors le fixem a 1 (ja que els nums aleatoris que generarem van de 0 a 1)
        if (pm > 1.0) {
            pm = 1.0;
        }

        // --------------------------- Generarem un nombre aleatori entre 0 i 1. ---------------------------

        double aleatori = Math.random();  // num aleatori entre 0 i 1
        if (aleatori > pm) {
            return;
        }

        // Si es produeix la mutació, llavors ara creem el nou virus. Anomenem Vnou a aquest nou virus.
        VirusARN Vnou = virusARN.mutacio();

        // --------------------------- Calculem quantes infeccions passen al nou virus -----------------------

        // Aquest nou virus Vnou substituirà a V en un pm(V,D)*100 % dels nous contagis de V

        int nousContagisMutats = (int) Math.round(pm * nousContagis); // nombre de contagis que muten
        int nousContagisRestants = nousContagis - nousContagisMutats; // perquè clar, els nous contagis
        // del nou virus substitueixen els del virus que hi havia, per tant, hem de restar-los al que hi havien

        // --------------------------- Canviem ara les dades de la regió -------------------------------------

        // Reduim els contagis del virus original que hi havia a la regió.
        _infectats_no_contagiosos.set(0, nousContagisRestants);  // reduïm els que corresponen al virus original

        // Ara, ve el pas en que hem de crear una nova afectació per aquest nou virus.
        regio.afegirNovaAfectacio(Vnou, nousContagisMutats);
    }

    // mutacio per coincidència

// De tots els virus que es troben en una mateixa regió, primer cal fer parelles dos a dos. Ara bé, perquè
// es pugui donar aquest tipus de mutació, és molt important veure que realment formen part de la mateixa família.

    /**
     * Aquest mètode l'he fet basicament per poder, després d’actualitzar l’estat del virus en aquesta regió, comprovar
     * si es pot produir una mutació per coincidència. Ara bé, aquest càlcul no depèn només d’aquest virus,
     * sinó de totes les afectacions d’aquesta regió. Per això, cridem una funció implementada en
     * la classe Regio.
     */

    public void HihaMutacioCoincidencia() {
        // Pre:
        // Post: es comprova si hi ha mutacions per coincidència dins de la regió
        regio.comprovarMutacionsPerCoincidencia();
    }


    // ALTRES MÈTODES QUE NECESSITEM (Que ens els crida la classe regió)


    /**
     * Aquesta funció l'he fet per poder saber el nombre total de persones que hi ha a la regió i que són contagioses
     * per aquest virus concret. És a dir, com que tenim un histograma de contagiosos (una llista que ens diu quants contagiosos
     * nous hi ha a cada dia de la infecció), el que faig és sumar tots aquests per saber el total de contagiosos actuals.
     *
     * Aquesta funció es cridarà des de la classe Regio, i l'he necessitat per poder fer servir
     * les fórmules de contagis interns i externs que hi ha en el formulari. Si és d'aquest virus, no cal cridar-la,
     * però si per exemple volem els contagiosos d'un altre virus de la regió, doncs clar, només la classe regió
     * sap totes les afectacions que hi ha en ella, per tant hem de mirar primer quina afectacio és la que correspon
     * a aquest virus, i després ja determinar a partir d'aquesta afectació, el nombre de contagiosos.
     */
    public int nombreContagiosos(){
    // PRE: La llista _contagiosos ha d'estar inicialitzada correctament.
    // POST: Retorna la suma total de contagiosos que hi ha en un dia concret (és la suma dels valors de la llista _contagiosos).
        return calcular_total(_contagiosos);
    }


    public int nombreImmunes(){
        // PRE: La llista _immunes ha d'estar inicialitzada correctament.
        // POST: Retorna la suma total de immunes que hi ha en un dia concret (és la suma dels valors de la llista _immunes).
        return calcular_total(_immunes);
    }

    public int nombreInfectatsNoContagiosos(){
        // PRE: La llista _infectats_no_contagiosos ha d'estar inicialitzada correctament.
        // POST: Retorna la suma total de infectats però que encara no poden contegiar que hi ha en un dia concret (és la suma dels valors de la llista _infectats_no_contagiosos).
        return calcular_total(_infectats_no_contagiosos);
    }


    /**
     * Aquesta funció simplement l'hem fet perquè, en una regió, sabem totes les afectacions que té, però
     * en molts casos necessitem saber quin virus està associat a aquesta afectació (la regió ja la sabrem perquè
     * seràn afectacions propies d'aquesta). Per tant, necessitem doncs un mètode que ens retorni el Virus associat,
     * i així doncs podrem veure quins virus estan presents, quins no, podem fer les fòrmules de la mutació per
     * coincidència...
     */
    public Virus quinVirusHiHa(){
    // PRE: L’atribut virus ha d’haver estat inicialitzat en el constructor (no pot ser que sigui null).
    // POST: Retornem el virus propi d'aquesta afectació.
        return virus;
    }

    public void afegir_infectats(int n) {
        _infectats_no_contagiosos.set(0, _infectats_no_contagiosos.get(0) + n);
    }

    // PREGUNTA MEVA: CAL RECALCULAR MORTS O QUE ??
    // PERQUÈ LO DE MUTACIÓ, QUAN S'HA DE CRIDAR, A ON ??
    // PERQUÈ CLAR, AIXÒ AFECTA ALS CALCULS




// ----------------------------------------------- MÈTODES PRIVATS ---------------------------------------------------------


    // PER EL MÈTODE D'AVANÇAR UN DIA:

    /**
     * Actualitza la llista de malalts segons la probabilitat que una persona infectada amb el virus desenvolupi la malaltia.
     * Segons la fòrmula: Nous_malalts(R,V,D) = R.inf(V,D-1)[V.I()] * V.probMalalt()
     * Això vol dir que mirem el grup d'infectats que es troben al final del període d’incubació,
     * i calculem quants d’ells es posen malalts segons la probabilitat de desenvolupar la malaltia.
     */
    private void actualitzar_malalts() {
    // Pre: La llista _contagiosos ha d'estar inicialitzada i tenir almenys un element.
    // Post: El nombre de nous malalts calculats per avui s'afegeix a la llista _malalts en la posició 0.
    // El total de malalts acumulats s'actualitza amb la suma d'aquests nous malalts

        int dies_per_emmalaltir = virus.tempsContagiSenseSintomes();  // T_inc - T_lat
        int nous_malalts = 0;

        if (_contagiosos.size() >= dies_per_emmalaltir) {
            // Calculem els nous malalts, i necessitem demanar a virus la prob. d'emmalaltir.
            double prob_emmalaltir = virus.probDesenvoluparMalaltia();
            int contagiosos_final_t_incubacio = _contagiosos.get(dies_per_emmalaltir);
            double nous_malalts_sense_arrodonir = contagiosos_final_t_incubacio * prob_emmalaltir;
            int malalts_avui = (int) Math.round(nous_malalts_sense_arrodonir);
            _malalts.add(0, malalts_avui);
            totalMalalts = totalMalalts + malalts_avui;
        } else {
            _malalts.add(0, 0);
        }
    }

    /**
     * Aquest mètode és per actualitzar la llista d'immunes segons el temps d'immunitat del virus.
     * Quan un contagiat ja ha passat el temps de contagi complet, aquest passa a ser considerat immune.
     *
     * Aquí, he considerat que el temps de contagi especificat pel virus (per exemple, 10 dies) indica
     * que la persona no serà immune fins que hagi passat completament aquest temps. Per tant, hem de
     * deixar passar aquests dies sencers i, només un cop els hagi completat, la considerarem immune.
     *
     * Per tant, jo he assumit que si per exemple tenim un temps de contagi de 10 dies, la persona només serà immune al dia següent,
     * és a dir, al dia 11.
     *
     * Molt important: ARA JA TINC EN COMPTE QUE NO PASSEN TOTS ELS CONTAGIATS A IMMUNES. És a dir, quan un grup de contagiosos ha passat
     * el temps de contagi, no podem simplement passar-los tots a immunes perquè durant aquest temps hi ha hagut morts.
     * Llavors, abans de passar-los a immunes, calculo el total de morts que hi ha hagut durant tot aquest període i ho resto
     * del nombre de contagiosos d'aquell grup. Així, només els que han sobreviscut passen a ser immunes.
     *
     * El que faig ara és que cada cop que arriba el dia en què els contagiosos es poden considerar immunes, faig servir la funció
     * `morts_totals()` per calcular quants han mort durant aquest període. Després, només afegeixo a la llista d'immunes
     * els que han sobreviscut. Això em permet tenir un control molt més exacte sobre qui passa a ser immune i qui no.
     *
     * A més, aquells inmunes que ja han passat el temps de inmunitat, els treiem de la llista
     */
    private void actualitzar_immunes() {
        // Pre: La llista _contagiosos i _immunes ha d'estar inicialitzada i amb valors vàlids.
        // Post: En el cas de que la llista contagiosos tingui més dies registrats que el temps de contagi, llavors vol dir
        // que els contagiosos del primer dia registrat ja han passat suficient temps, i ja els podem considerar inmunes.
        // Per tant, els nous immunes els movem de la llista _contagiosos a la llista _immunes, però restant els que han mort.
        // Si la llista de _immunes te més dies registrats que el temps de immunitat, llavors vol dir
        // que els immunes del primer dia registrat ja han passat suficient temps, i ja deixen de ser inmunes. Per tant
        // s'eliminen de la llista _immunes.

        int temps_contagi = virus.tempsContagi();
        int temps_malaltia = virus.tempsMalaltia();

        if (_contagiosos.size() == temps_contagi & _malalts.size() == temps_malaltia & mortsDiaries.size() == temps_malaltia) {
            int nous_immunes = _contagiosos.get(_contagiosos.size() - 1); // Aquests són els contagiosos que haurien de passar a immunes.

            _malalts.remove(_malalts.size() - 1); // Eliminem aquest grup de la llista de malalts perquè ja han passat a ser immunes (els que no han mort).
            mortsDiaries.remove(mortsDiaries.size() - 1); // Eliminem d'aquest grup, les morts del grup que ja és inmune i que ja no mor més gent.
            _contagiosos.remove(_contagiosos.size() - 1); // Eliminem aquest grup de la llista d'infectats perquè ja han passat a ser immunes.
            _immunes.add(0, nous_immunes);  // Afegim els nous immunes a la posició 0 de la llista.

        } else {
            _immunes.add(0, 0); // Si no hi ha nous immunes avui, afegim un 0.
        }

        // Eliminem els immunes que ja han passat el temps d'immunitat.
        int temps_immune = virus.tempsImmunitat();

        if (_immunes.size() > temps_immune) {
            _immunes.remove(_immunes.size() - 1);
        }

    }

    /**
     * Hem fet aquest mètode per a poder calcular el nombre de nous contagis (infectats no contagiosos) seguint les
     * fórmules que ens han donat a la pràctica. Segons aquestes, per a poder calcular-los, hem de
     * considerar els contagis interns i els contagis externs (de regions veïnes).
     * Després agafem els valors reals (no poden ser més que el nombre de sans) i els guarda a la posició 0 (indicant que són d'aquest dia).
     */
    private void actualitzar_infectats_no_contagiosos() {
    // Pre: les llistes internes i la regió han d'estar inicialitzades i correctes.
    // Post: La llista _infectats_no_contagiosos té un nou element a la posició 0 que representa els nous infectats del dia.
    // El total d'infectats acumulat (totalInfectats) s'actualitza amb aquests nous infectats.
        int contagisInterns = calcular_contagis_interns();
        int contagisExterns = calcular_contagis_externs();

        int nous_contagis = contagisInterns + contagisExterns;

        // Com que no pot haver-hi més contagis que persones sanes:
        int sans = regio.nombreSans(virus);  // Cridem aquest mètode de la classe regió.
        int nous_contagis_reals = Math.min(nous_contagis, sans); // si nous_contagis és superior, ens quedem amb el
        // mínim que serà per tant el nombre de sans. Si no, ens quedem amb el nombre de contagis que ja teniem.

        _infectats_no_contagiosos.add(0, nous_contagis_reals);
        totalInfectats += nous_contagis_reals;
    }


    /**
     * He fet aquest mètode per a saber quantes morts haurien d'afegir-se cada dia al vector mortsDiaries.
     * La idea és que cada cop que tenim nous malalts, calculem quants d'aquests moriran durant el període de contagi,
     * i aquests els dividim de manera igual durant tots els dies de la malaltia.
     *
     * A diferència del que havia fet inicialment, he decidit al final que aquesta funció només faci el càlcul i
     * afegeixi aquestes morts al vector, sense restar-les de la llista de malalts, ni res. Això ja ho farà un altre
     * mètode.
     *
     * Com funciona:
     * Cada dia que hi hagi nous malalts, s'afegiran noves morts al vector mortsDiaries (i aquí ho calculem).
     * Per exemple, si avui tenim 100 nous malalts, i la probabilitat de mortalitat és 0.2 (20%),
     * esperem que morin 20 persones. Si el temps de contagi és de 5 dies, llavors esperem que hi hagi 4 morts per dia (d'aquest grup).
     *
     * Pero clar, no només moriran 4, sinó que hi ha altres grups de malalts. Per tant, cada dia s'hauran d'anar afegint noves morts
     * al vector mortsDiaries.
     *
     * ex: el dia k, el vector seria (considerant només els 100 emmalaltits aquest dia) [4,0,0,0,0], i el dia k+1 seria
     * [0,4,0,0,0]. Ara bé, si incorporem els 125 del dia següent, llavors el dia k+1 tindrem [5, 4, 0, 0 , 0 ], i
     * el k+2 [0, 5, 4, 0, 0 ] (suposant que el dia k+2 no ha emmalaltit ningú o tan pocs que no dona ni per a 1 mort)
     * En resum, tot es va desplaçant a dreta, per l'esquerra entra el nombre de morts/dia corresponent als que acaben
     * d'emmalaltir, i a la casella j tindrem el nombre de morts entre els malalts que es troben al dia j+1 de malaltia.
     */

    private void calcula_quants_moren(int nous_malalts) {
        // Pre: El nombre de nous malalts ha de ser positiu, i la taxa de mortalitat del virus ha d'estar entre 0 i 1.
        // Post: Es calcula el nombre de morts esperades per aquests nous malalts i s'afegeixen al vector `mortsDiaries`.

        if (nous_malalts == 0) {
            mortsDiaries.add(0, 0);
            return;
        }

        int temps_malaltia = virus.tempsMalaltia();

        // Calculem el nombre total de morts esperades
        int mort_en_tot_periode = morts_totals(nous_malalts);

        // Calculem quantes morts haurien d'afegir-se cada dia durant el període de la malaltia
        int morts_cada_dia = mort_en_tot_periode / temps_malaltia;
        // int falten_repartir = mort_en_tot_periode % temps_malaltia; (no ser com implementar-ho)

        // Omplim el vector mortsDiaries amb els nous morts d'aquest grup
        mortsDiaries.add(0, morts_cada_dia);
    }


/**
 * He fet aquest mètode privat per poder calcular el nombre total de morts d'un grup de malalts
 * durant el període que dura la seva malaltia fins que poden passar a ser immunes.
 * Com que anem sumant nous malalts cada dia, aquest càlcul l'he fet per cada grup per separat.
 *
 * He utilitzat la fòrmula que posa a l'enunciat de la pràctica: Multiplico el nombre inicial de malalts pel
 * percentatge de mortalitat del virus.
 *
 * @param nous_malalts Nombre de malalts d'un grup concret.
 * @return Quanta gent esperem que mori d'aquest grup.
 */
private int morts_totals(int nous_malalts) {
    // Pre: El nombre de malalts ha de ser un enter positiu, i la taxa de mortalitat del virus ha d'estar entre 0 i 1.
    // Post: Retorna quantes morts esperem que hi hagi d'aquest grup de malalts

    double prob_mor = virus.taxaMortalitat(); // Probabilitat de mortalitat del virus

    int total_morts = (int) Math.round(nous_malalts * prob_mor); // Total de morts que esperem

    return total_morts;
}

/**
* He fet aquesta funció que el que fa és actualitzar les persones que moren en el dia d'avui. És a dir,
* mirem quantes moren de cada grup i això ho afegim al total de morts i també cal restar-ho al
* total de malalts i contagiosos, pq ja no és gent malalta i contagiosa.
*/

private void actualitzar_morts() {
   // Pre: Les llistes _malalts, _contagiosos i mortsDiaries han d'estar inicialitzades i tenir la mateixa mida
   // Aquestes llistes no poden estar buides.
   // Post: Les morts d'avui es resten del nombre de malalts i contagiosos corresponents.
   // El total de morts acumulat (totalMorts) s'actualitza sumant les morts d'avui.

    int mida = mortsDiaries.size();
    int total_morts_avui = 0;

    for (int i = 0; i < mida; i++) {

        // Calculem quantes morts hi ha avui (és a dir, el primer element del vector mortsDiaries)
        int morts = mortsDiaries.get(i);

        // Restem aquestes morts dels malalts i dels contagiosos del mateix dia (posició 0)
        int malalts = _malalts.get(i);
        int temps_contagi_sense_sintomes = virus.tempsContagiSenseSintomes();
        int posicio_contagiosos = i + temps_contagi_sense_sintomes;
        int contagiosos = _contagiosos.get(posicio_contagiosos);

        // Apliquem les morts als malalts i contagiosos.
        _malalts.set(i, malalts - morts);
        _contagiosos.set(posicio_contagiosos, contagiosos - morts);

        // Actualitzem el total de morts acumulat
        total_morts_avui = total_morts_avui + morts;

        // Actualitzem les persones que queden vives en la regio
        regio.persones_moren(morts);

    }

    totalMorts = totalMorts + total_morts_avui;

}

// MÈTODES PER A PODER CALCULAR ELS NOUS INFECTATS DEL DIA, SEGUINT ELS CONTAGIS EXTERNS I INTERNS

    /**
     * He fet aquest mètode per a calcular el nombre de contagis interns segons la fórmula que ens ha donat el profe:
     * Contagis_int(R,V,D) = R.sans(V,D-1) * R.taxaInt() * (R.cont(V,D-1) / R.poblacio(D-1)) * V.probContagi()
     */
    private int calcular_contagis_interns() {
    // PRE:
    // POST:
        int sans = regio.nombreSans(virus);  // Ha de tornar el nombre de persones sanes
        double taxaInt = regio.taxaInternaContacte();
        int contagiosos = calcular_total(_contagiosos);
        int poblacio = regio.poblacio();
        double probContagi = virus.taxaContagi();

        if (poblacio == 0){ // bàsicament he fet això per evitar errors, perquè no dividim per 0
            return 0;
        }
        else {
            double resultat_contagis_interns = sans * taxaInt * ((double) contagiosos / poblacio) * probContagi;
            return (int) Math.round(resultat_contagis_interns);
        }
    }

    /**
     * Aquest mètode l'he fet per calcular la suma dels contagis que venen de totes les regions veïnes, les quals
     * també poden contagiar.
     *
     * Quan una regió veïna ja té el virus: Sí, sumem nous contagis externs (a la teva regió). Aquests sí que compten al contagis_ext.
     * Quan una regió veïna encara NO té el virus: NO sumem res al teu contagis_ext. El que fem és crear una nova afectació a aquella regió. Però no afecta el nombre de nous infectats de la teva regió avui.
     */
    private int calcular_contagis_externs() {
        // PRE:
        // POST:

        int contagis_ext = 0;
        int sans = regio.nombreSans(virus);  // Aquí tindrem el nombre de sans d'aquesta regió.
        double probContagi = virus.taxaContagi(); // Aquí tenim la probabilitat de contagi del virus

        List<Regio> veines = regio.regionsVeines(); // Així rebem una llista de totes les regions veines que
        // podrem contagiar amb el virus present d'aquesta regio. Ara bé, hem de mirar primer que no tinguen
        // confinament en aquetes regions, ja que sinó, no podrem contagiar-la.

        for (Regio veina : veines) { // per cada una de les regions veïnes, calculo els contagiosos que tenen
            if (!regio.hiHaConfinamentAmb(veina)) {
                double taxaExt = regio.taxaExternaContacte(veina);
                int poblacio_veina = veina.poblacio();

                if (poblacio_veina == 0) continue;

                if (veina.VirusJaEstaAfectant(virus)) {
                    // Si la regió veïna ja té el virus, sumem normal
                    int contagiosos_veina = veina.nombreContagiosos(virus);

                    double contagis_ext_per_regio = sans * taxaExt * ((double) contagiosos_veina / poblacio_veina) * probContagi;
                    contagis_ext = contagis_ext + (int)Math.round(contagis_ext_per_regio);

                } else {
                    // Si la regió veïna NO té aquest virus, segons la fòrmula podem seguir contagiant i llavors
                    // és necessari crear una nova afectació
                    int contagiosos_en_aquesta = calcular_total(_contagiosos);
                    double nous_contagis_externs = veina.poblacio() * taxaExt * ((double) contagiosos_en_aquesta / regio.poblacio()) * probContagi;
                    int nous_contagis_redondejat = (int) Math.round(nous_contagis_externs);

                    if (nous_contagis_redondejat > 0) {
                        veina.afegirNovaAfectacio(virus, nous_contagis_redondejat);
                    }

                }
            }
        }

        return contagis_ext;
    }





// PER EL MÈTODE DE LES MUTACIONS

    /**
     * Aquest és molt important ja que serà el mètode que farem servir per a actualitzar l'estat de la regió
     * després de que es produis una mutació (ja que el nou virus tindrà uns paràmetres diferents).
     *
     * IMPORTANT LLEGIR EL Q DIU LA PRÀCTICA, DE Q SEGONS EL T QUE PORTI, CONSIDEREM UNES COSES O UNES ALTRES
     *
     * @param virus_mutat El nou virus mutat que ha de passar a afectar la regió.*/

    private void actualitzar_estat_despres_de_mutacio(Virus virus_mutat) {
        // PRE: virus_mutat ha de ser un objecte vàlid de la classe Virus.
        // POST: Les estadístiques i els vectors d'estat de la regió s'actualitzen segons els nous paràmetres del virus.

        // A implementar:

    }



// ALTRES MÈTODES PRIVATS:

    /**
     * Aquesta funció fa la suma total dels valors d'una llista. L'he fet per a calcular el total de les llistes
     * que tenim. Per exemple, com que necessitem el total de contagiosos de les regions veines per calcular el total
     * de nous infectats, doncs l'he fet publica. A més, com que tenim altres llistes de enters, potser en altres
     * ocasions també voldrem calcular el total d'aquestes.
     */
    private int calcular_total(List<Integer> llista) {
        // PRE:
        // POST:
        int suma = 0;
        for (int valor : llista) {
            suma += valor;
        }
        return suma;
    }
}

