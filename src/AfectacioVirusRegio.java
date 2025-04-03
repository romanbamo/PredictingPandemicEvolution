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

    // Altres estadístiques que hem de tenir del dia actual (a nivell de grup)
    private int contagiosos;     // Persones que poden transmetre el virus (calculat a partir del temps de latència)
    /** ns si contagiosos seria una llista o no*/
    private int nousInfectats;   // Persones contagiades


    // Estadístiques que hem d'anar acumulant
    private int totalMalalts;     // Total de persones que han estat malaltes al llarg de la malaltia
    private int totalMorts;       // Total de persones mortes al llarg de la malaltia
    private int totalContagis;    // Total de contagis que s'han fet al llarg de la malaltia
    private int totalInfectats;   // Total de persones que s'han infectat al llarg de la malaltia

    // Altres coses que he de controlar
    private List<Integer> mortsDiaries;  // Aquest vector l'he fet per saber les morts que s'espera tenir cada
    // dia

// ---------------------------------------------- MÈTODES PÚBLICS ------------------------------------------------------------------

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
        totalContagis = 0;
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


        // ---------------------- Calculo nous contagiosos (si ja ha passat el temps de latencia) --------------------

        // He decidit que la posició 0 de la llista representa el dia actual, és a dir, avui.
        // Sempre que avancem un dia en la simulació, col·loquem els nous infectats al principi de la llista,
        // i els elements que ja estaven es desplacen una posició cap a la dreta.
        // Posició 0: Avui (els nous contagiosos que acabem de calcular), Posició 1: Ahir, Posició 2: Abans d'ahir (i així successivament...).

        int temps_no_pot_cotagiar = virus.tempsLatencia();
        int nous_contagiosos = 0;
        // Perquè un infectat pugui començar a contagiar, ha hagut de passar el temps de latència
        if (_infectats_no_contagiosos.size() == temps_no_pot_cotagiar) {
            nous_contagiosos = _infectats_no_contagiosos.remove(_infectats_no_contagiosos.size() - 1);
        }
            _contagiosos.add(0, nous_contagiosos);
            totalContagis = totalContagis + nous_contagiosos;

        // -------------------- Actualitzem els immunes ---------------------------------------------------------------
        actualitzar_immunes();  // Ho he passat a un mètode privat perquè quedi tot més clar.

        // -------------- Calculem nous infectats a partir dels contagiosos -------------------------------------------
        actualitzar_infectats_no_contagiosos();

        // -------------------- Actualitzo els malalts --------------------
        actualitzar_malalts();  // He decidit fer-ho també amb un mètode privat perquè així tingui menys codi la funció i s'entengui més.

        // -------------------- Calculo les morts d'avui --------------------
        if (!_malalts.isEmpty() && _malalts.get(0) > 0) { // en el cas que tinguem persones malaltes aquest dia
            calcula_quants_moren(_malalts.get(0));  // Calculem els morts d'avui a partir dels malalts d'avui
        }

        // -------------------- Aplicar les morts calculades --------------------
        actualitzar_morts();



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

// ----------------------------------------------- MÈTODES PRIVATS ---------------------------------------------------------


    // PER EL MÈTODE D'AVANÇAR UN DIA:

    /**
     * Actualitza la llista de malalts segons la probabilitat que una persona infectada amb el virus desenvolupi la malaltia.
     * Dels nous contagiosos, una certa part, passarà a estar malalta.
     */
    private void actualitzar_malalts() {
    // Pre: La llista _contagiosos ha d'estar inicialitzada i tenir almenys un element.
    // Post: El nombre de nous malalts calculats per avui s'afegeix a la llista _malalts en la posició 0.
    // El total de malalts acumulats s'actualitza amb la suma d'aquests nous malalts

        if (!_contagiosos.isEmpty()) {
            int malalts_avui = virus.nousMalalts(_contagiosos.get(0));
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

        if (_contagiosos.size() > temps_contagi) {
            int nous_inmunes = _contagiosos.get(_contagiosos.size() - 1); // Aquests són els contagiosos que haurien de passar a immunes.

            // Calculem el nombre de morts que hi ha hagut d'aquest grup durant el període d'immunitat
            int nous_malalts = _malalts.remove(_malalts.size() - 1);
            int morts_del_grup = morts_totals(nous_malalts);

            // Restem els morts del grup inicial per saber quants han sobreviscut i, per tant, passen a ser immunes.
            int nous_immunes = nous_inmunes - morts_del_grup;

            if (nous_immunes < 0) nous_immunes = 0; // Per si de cas, assegurem que no hi ha valors negatius.

            _malalts.remove(_malalts.size() - 1); // // Eliminem aquest grup de la llista d'de malalts perquè ja han passat a ser immunes (els que no han mort).
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
     * Aquest mètode el que fa és actualitzar la llista d'infectats no contagiosos.
     * Cada dia, s'ha d'actualitzar els infectats no contagiosos a partir de la llista de contagiosos.
     * Pel que he entés, el que he fet és que calculo els nous infectats a partir dels contagiosos segons la probabilitat de contagi.
     * Per cada element de la llista _contagiosos, calculo els nous infectats d'aquest grup i els sumo tots per calcular
     * els contagis totals d'aquell dia, i els afegeixo a __infectats_no_contagiosos.
     */
    private void actualitzar_infectats_no_contagiosos() {
    // Pre: la llista _contagiosos ha d'estas inicialitzada i amb valors vàlids.
    // Post: La llista _infectats_no_contagiosos té un nou element a la posició 0 que representa els nous infectats del dia.
    // El total d'infectats acumulat (totalInfectats) s'actualitza amb aquests nous infectats.
        int total_infectats_avui = 0;
        for (int i = 0; i < _contagiosos.size(); i++) {
            total_infectats_avui = total_infectats_avui + virus.nousContagis(_contagiosos.get(i));
        }
        _infectats_no_contagiosos.add(0, total_infectats_avui);
        totalInfectats = totalInfectats + total_infectats_avui;
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
     * He fet aquest mètode per a saber quantes morts haurien de sumar-se cada dia al vector mortsDiaries.
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
     * Pero clar, no només moriran 4, sinó que hi ha altres grups de malalts. Per tant, cada dia s'hauran d'anar sumant noves morts
     * al vector mortsDiaries.
     *
     * Per tant, ho he fet de manera que cada vegada que s’afegeixen morts, aquestes s’acumulen al vector mortsDiaries.
     * Suposem que tenim un vector de T_con-1 dies (pq hem eliminat les morts del dia anterior).
     * Quan afegim nous malalts, el vector es completa fins arribar a T_con.
     * Així, les primeres T_con-1 caselles contindran el valor de les morts dels dies anteriors més les d’aquests nous malalts,
     * mentre que l’última casella només contindrà les morts d’aquests nous malalts.
     */
    private void calcula_quants_moren(int nous_malalts) {
        // Pre: El nombre de nous malalts ha de ser positiu, i la taxa de mortalitat del virus ha d'estar entre 0 i 1.
        // Post: Es calcula el nombre de morts esperades per aquests nous malalts i s'afegeixen al vector `mortsDiaries`.

        int temps_contagi = virus.tempsContagi();

        // Calculem el nombre total de morts esperades
        int mort_en_tot_periode = morts_totals(nous_malalts);

        // Calculem quantes morts haurien d'afegir-se cada dia durant el període de contagi
        int morts_cada_dia = mort_en_tot_periode / temps_contagi;
        int falten_repartir = mort_en_tot_periode % temps_contagi;

        // Omplim el vector mortsDiaries amb els nous morts d'aquest grup

        int morts_aquest_dia = 0;

        for (int i = 0; i < temps_contagi; i++) {
            if (i < falten_repartir) {
                morts_aquest_dia = morts_cada_dia + 1;  // Repartim una mort extra a aquest dia
            }
            else {
                morts_aquest_dia = morts_cada_dia;
            }
            if (i < mortsDiaries.size()) {
                // Si el vector ja té valors, els sumem amb els nous morts calculats
                mortsDiaries.set(i, mortsDiaries.get(i) + morts_aquest_dia);
            } else {
                // Si no existeixen, els afegim com a úniques morts
                mortsDiaries.add(morts_aquest_dia);
            }
        }
    }

    /**
     * He fet aquesta funció que el que fa és actualitzar les persones que moren en el dia d'avui. És a dir,
     * mirem quantes moren i això ho afegim al total de morts i NO ESTIC SEGURA, però potser també cal restar-ho al
     * total de malalts, pq ja no és gent malalta, sinó que és gent inmune.
     */
    private void actualitzar_morts() {
    // Pre:
    // Post:
        if (!mortsDiaries.isEmpty()) {
            int mortsAvui = mortsDiaries.remove(0); // Agafem les morts previstes per avui
            // El que fa remove és eliminar el primer element de la llista i moure tots els altres elements cap a
            // l'esquerra automàticament.

            // Sumem aquestes morts al total de morts que anem sumant al llarg dels dies
            totalMorts = totalMorts + mortsAvui;
        }
    }

}


// FALTA FER: Quan tenim mutacio, com varien aquestes probabilitats i tot. I com canvien el temps de lactancia...


