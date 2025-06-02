
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Collections;

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


    private Map<Regio, Double> taxesExternesContacte;
    // He tingut la idea de fer aquest atribut amb un map també. Aquest mapa ens serveix per a guardar,
    // per cada regió veïna, quina és la taxa de contacte externa
    // que tenim amb ella. És a dir, com de probable és que una persona de la nostra regió
    // es posi en contacte amb una persona d'aquella regió concreta.


    private Map<String, AfectacioVirusRegio> afectacions;
    // Crec que aquest atribut és un dels més importants, perquè és regió la que ha de coneixer les seves propies
    // afectacions. Perquè una regió pot estar infectada per varis virus, i de cada un nosaltres hem de tenir constància
    // de com està la població durant cada dia.
    // NOVA IDEA: Fer-ho amb un MAP --> per a cada virus guardem la seva afectació directament amb una clau que és
    // el seu nom (virus.nom()), en comptes d'haver de mirar en tota la llista, quina afectacio correspon a un
    // cert virus. Així evitem bucles innecessaris.

    // He decidit afegir una millora, i és que quan m'he adonat de que quan feiem confinament canviavem la taxa de
    // contagia externa i interna a 0, doncs hem de tenir una manera de poder guardar les inicials, perquè així,
    // quan desconfinem, poguem recuperar-les.
    private Double taxaContacteInternOriginal; // per guardar la taxa interna original abans de confinar
    private Map<Regio, Double> taxesExternesOriginals = new HashMap<>(); // per guardar la taxa externa original amb cada veïna abans de confinar

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
        taxesExternesContacte = new HashMap<>();

        enConfinament = false;  // Crec que quan la creem, la definim sense confinament i després ja se li
        // assigna si en algun moment se li aplica un confinament.

        // Inicialitzem les llistes buides (encara no sabem amb quines regions fa contacte ni quins virus té afectant-la)
        regionsVeines = new ArrayList<>();
        afectacions = new HashMap<>();
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

    /**
     * Aquí el que fem és tornar la taxa de contacte interna d’aquesta regió.
     * És a dir, la probabilitat que dues persones d’aquesta mateixa regió entrin en contacte. Aquesta és pròpia de la
     * regió.
     */
    public double taxaInternaContacte() {
        // Pre: la regió ha d’estar inicialitzada i tenir una taxa interna vàlida (entre 0 i 1)
        // Post: Retornem la taxa de contacte interna de la regió.

        return taxaContacteIntern;
    }


    public double taxaExternaContacte(Regio veina){
        // Pre: La regió veïna ha de ser una regió veina que estigui guardada en aquesta regió.
        // Post: Retorna la taxa de contacte externa amb aquesta regió veïna.

        if (!taxesExternesContacte.containsKey(veina)) {
            throw new IllegalArgumentException("No hi ha taxa externa guardada per aquesta regió veïna.");
        }

        return taxesExternesContacte.get(veina);

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
            throw new IllegalArgumentException("Una regió no pot ser veïna d’ella"); // tirem excepció tal i com hem vist a classe
        }

        // Això també és important per assegurar-nos de que no repetim, ja que si ja hem dit que una regió és veiïna,
        // doncs no podem tornar-la a afegir a la llista.
        if (!regionsVeines.contains(veina)) {
            regionsVeines.add(veina);
            taxesExternesContacte.put(veina, taxaExternaContacte); // afegim al map, per aquesta regió veina, la seva corresponent taxa
        }
    }

    /**
     * Ara el que necessitem és passar-li a AfectacioVirusRegió la llista de regions veïnes per a poder
     * fer les fòrmules que ens ha donat el professor per a poder calcular com evoluciona el virus
     * en la regió. Per exemple, la útilitzem per a poder calcular els contagis externs (necessitem mirar una a una
     * si aquestes regions tenen el virus i quants contagiosos tenen).
     */
    public List<Regio> regionsVeines() {
        // Pre: La regió ha de tenir inicialitzada la llista de regions veïnes.
        // Post: Retornem la llista de regions veïnes amb les quals hi ha contacte.

        return regionsVeines;
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
        afectacions.put(virus.nom(),novaAfectacio);
    }


    /**
     * Aquesta funció l'he fet perquè necessitem saber quantes persones estan sanes
     * respecte a un virus concret en aquesta regió. És a dir, hem de saber quanta
     * gent no te el virus i es pot contegiar (ni infectats, ni contagiosos,
     * ni immunitzats (ja que aquests estan sans però no els tenim en compte ja que no es poden contagiar).
     *
     * Això és super important perquè ho utilitzarem a les fórmules de contagi intern
     * i també quan calgui saber quanta gent es pot contagiar.
     *
     * Per tant, el que fem és agafar l'afectació que té aquesta regió amb el virus
     * i li restem a la població total de la regió totes les persones que ja han passat
     * per alguna fase de la infecció.
     */
    public int nombreSans(Virus virus) {
        // Pre: virus ha de ser vàlid, i pot ser que encara no hi hagi afectació d'aquest virus a la regió
        // Post: Retorna quantes persones de la regió estan sanes i es poden contegiar
        AfectacioVirusRegio avr = esta_present_virus_a_la_regio(virus);

        if (avr == null) {
            // Si encara no hi ha afectació d’aquest virus en aquesta regió, tothom està sa.
            return poblacio;
        }

        int totalContagiosos = avr.nombreContagiosos();  // Total de contagiosos
        int totalImmunes = avr.nombreImmunes(); // Total de persones que han passat la malaltia i són immunes durant um temps
        int totalInfectatsNoContagiosos = avr.nombreInfectatsNoContagiosos(); // Els que s’han infectat però encara no poden contagiar

        int persones_amb_virus = totalContagiosos + totalImmunes + totalInfectatsNoContagiosos;

        // Llavors, els sanes és tota la població menys totes les persones que han tingut contacte amb aquest virus
        int sans = poblacio - persones_amb_virus;

        // Ens assegurem que no retorni valors negatius, per si de cas
        return Math.max(sans, 0);
    }


    public boolean estaEnConfinament() {
        // Pre: L’atribut enConfinament ha d’estar inicialitzat.
        // Post: Retornem true si la regió està en confinament, i si no ho està, tornem false.

        return enConfinament;
    }


    /**
     * Bàsicament amb aquesta funció podem saber des d'una classe, si entre aquesta regió concreta
     * i una regió veïna, hi ha o no confinament. És important mirar les dos regions, perquè pot ser
     * que la propi regió no tingui confinament, però si la regió veïna si que en te, serà impossible que
     * la contagi. Per tant, només en el cas de que les dues regions NO tinguin confinament, podrem contagiar o ens
     * podran contagiar. Si una de les dos esta confinada, podem dir que no hi ha contacte.
     */
    public boolean hiHaConfinamentAmb(Regio veina) {
        // Pre: veina ha de ser una regió vàlida.
        // Post: Retornem true si alguna de les dues regions està en confinament (i per tant, no poden contagiar-se).
        return this.taxaExternaContacte(veina) == 0.0 || veina.taxaExternaContacte(this) == 0.0;
    }

    /**
     * Per poder fer les fòrmules, ja que necessitem el nombre de persones contagioses que poden transmetre un virus concret de la
     * regió.
     */
    public int nombreContagiosos(Virus virus) {
    // Pre: virus ha de ser un objecte vàlid.
    // Post: Retorna el nombre de persones contagioses per aquest virus en aquesta regió.

        AfectacioVirusRegio avr = esta_present_virus_a_la_regio(virus);

        if (avr == null) {
            // Si no està aquest virus, ningú pot estar contagiant-lo.
            return 0;
        }

        return avr.nombreContagiosos();
    }

    /**
     * @brief Retorna el nombre total de persones infectades per un virus (contagioses o no).
     * @param virus Virus del qual volem saber els infectats totals dins la regió.
     * @return Nombre total d’infectats (contagiosos + no contagiosos) dins la regió.
     * 
     * @pre El virus ha de ser un objecte vàlid.
     * @post Si el virus està present a la regió, es retorna el total d'infectats. Altrament, retorna 0.
     * 
     * @author Romà Barrera
     */
    public int nombreInfectats(Virus virus) {
        AfectacioVirusRegio avr = esta_present_virus_a_la_regio(virus);
        if (avr == null) return 0;
        return avr.nombreContagiosos() + avr.nombreInfectatsNoContagiosos();
    }

    /**
     * @brief Retorna el nombre de persones malaltes per un virus en aquesta regió.
     * @param virus Virus del qual volem saber el nombre de malalts dins la regió.
     * @return Nombre de persones malaltes per aquest virus dins la regió.
     * 
     * @pre El virus ha de ser un objecte vàlid.
     * @post Retorna el nombre de persones malaltes per aquest virus, o 0 si no hi ha afectació.
     * 
     * @author Romà Barrera
     */
    public int nombreMalalts(Virus virus) {
        AfectacioVirusRegio avr = esta_present_virus_a_la_regio(virus);
        if (avr == null) return 0;
        return avr.nombreMalalts();
    }

    /**
     * @brief Retorna el nombre de nous contagis produïts per un virus en aquesta regió.
     * @param virus Virus del qual volem saber els nous contagis dins la regió.
     * @return Nombre de nous contagis per aquest virus dins la regió.
     * 
     * @pre El virus ha de ser un objecte vàlid.
     * @post Retorna el nombre de nous contagis per aquest virus, o 0 si no hi ha afectació.
     * 
     * @author Romà Barrera
     */
    public int nousInfectats(Virus virus) {
        AfectacioVirusRegio avr = esta_present_virus_a_la_regio(virus);
        if (avr == null) return 0;
        return avr.nousContagios();
    }

    /**
     * @brief Retorna el nombre de noves defuncions causades per un virus en aquesta regió.
     * @param virus Virus del qual volem saber les noves defuncions dins la regió.
     * @return Nombre de noves defuncions per aquest virus dins la regió.
     * 
     * @pre El virus ha de ser un objecte vàlid.
     * @post Retorna el nombre de noves defuncions per aquest virus, o 0 si no hi ha afectació.
     * 
     * @author Romà Barrera
     */
    public int novesDefuncions(Virus virus) {
        AfectacioVirusRegio avr = esta_present_virus_a_la_regio(virus);
        if (avr == null) return 0;
        return avr.novesDefuncions();
    }

    /**
     * @brief Retorna el nombre de persones immunitzades contra un virus en aquesta regió.
     * @param virus Virus del qual volem saber els immunes dins la regió.
     * @return Nombre de persones immunes a aquest virus dins la regió.
     * 
     * @pre El virus ha de ser un objecte vàlid.
     * @post Retorna el nombre de persones immunitzades per aquest virus, o 0 si no hi ha afectació.
     * 
     * @author Romà Barrera
     */
    public int nombreImmunes(Virus virus) {
        AfectacioVirusRegio avr = esta_present_virus_a_la_regio(virus);
        if (avr == null) return 0;
        return avr.nombreImmunes();
    }

    /**
     * @brief Retorna l’evolució diària del nombre de malalts per un virus concret en aquesta regió.
     * @pre El virus ha de ser un objecte vàlid.
     * @post Retorna la llista sencera de malalts per dia per aquest virus.
     * @author Romà Barrera
     * @param virus El virus del qual es vol consultar l’evolució de malalts.
     * @return Llista amb el nombre de malalts per dia.
     */
    public List<Integer> evolucioMalalts(Virus virus) {
        AfectacioVirusRegio avr = esta_present_virus_a_la_regio(virus);
        if (avr == null) return new ArrayList<>();
        return avr.evolucioMalalts();
    }

    /**
     * @brief Retorna l’evolució diària del nombre d’immunes per un virus concret en aquesta regió.
     * @pre El virus ha de ser un objecte vàlid.
     * @post Retorna la llista sencera d’immunes per dia per aquest virus.
     * @author Romà Barrera
     * @param virus El virus del qual es vol consultar l’evolució d’immunes.
     * @return Llista amb el nombre d’immunes per dia.
     */
    public List<Integer> evolucioImmunes(Virus virus) {
        AfectacioVirusRegio avr = esta_present_virus_a_la_regio(virus);
        if (avr == null) return new ArrayList<>();
        return avr.evolucioImmunes();
    }

    /**
     * @brief Retorna l’evolució diària del nombre de contagiosos per un virus concret en aquesta regió.
     * @pre El virus ha de ser un objecte vàlid.
     * @post Retorna la llista sencera de contagiosos per dia per aquest virus.
     * @author Romà Barrera
     * @param virus El virus del qual es vol consultar l’evolució de contagiosos.
     * @return Llista amb el nombre de contagiosos per dia.
     */
    public List<Integer> evolucioContagiosos(Virus virus) {
        AfectacioVirusRegio avr = esta_present_virus_a_la_regio(virus);
        if (avr == null) return new ArrayList<>();
        return avr.evolucioContgiosos();
    }

    /**
     * @brief Retorna l’evolució diària del nombre de morts per un virus concret en aquesta regió.
     * @pre El virus ha de ser un objecte vàlid.
     * @post Retorna la llista sencera de morts diàries per aquest virus.
     * @author Romà Barrera
     * @param virus El virus del qual es vol consultar l’evolució de defuncions.
     * @return Llista amb el nombre de morts per dia.
     */
    public List<Integer> evolucioMorts(Virus virus) {
        AfectacioVirusRegio avr = esta_present_virus_a_la_regio(virus);
        if (avr == null) return new ArrayList<>();
        return avr.mortsDiaries();
    }

    /**
     * @brief Retorna els acumulats totals d’afectació per un virus dins d’aquesta regió.
     * @pre El virus ha d’estar present a la regió.
     * @post Retorna una llista amb: [total infectats, total malalts, total morts]
     * @author Romà Barrera
     * @param virus Virus del qual es volen les dades.
     * @return Llista d’acumulats totals: infectats, malalts i morts.
     */
    public List<Integer> acumulatsTotals(Virus virus) {
        AfectacioVirusRegio avr = esta_present_virus_a_la_regio(virus);
        List<Integer> acumulats = new ArrayList<>();

        if (avr != null) {
            acumulats.add(avr.totalInfectats());
            acumulats.add(avr.totalMalalts());
            acumulats.add(avr.totalMorts());
        } else {
            acumulats.add(0);
            acumulats.add(0);
            acumulats.add(0);
        }

        return acumulats;
    }

    /**
     * @brief Avança un dia en la simulació per totes les afectacions de virus dins d’aquesta regió.
     * @pre L’estructura de dades afectacions ha d’estar inicialitzada amb objectes AfectacioVirusRegio.
     * @post Actualitza totes les afectacions dins d’aquesta regió fent avançar un dia de simulació.
     * @author Romà Barrera
     */
    public void avancarDia() {
        for (Map.Entry<String, AfectacioVirusRegio> afectacio : afectacions.entrySet()) {
            afectacio.getValue().avançarUnDia();
        }
    }



    /**
     * L'utilitzem per a saber si un cert virus ja està contegiant una regió o si encara no i hem de crear una
     * nova afectació.
     */
    public boolean VirusJaEstaAfectant(Virus virus) {
        // Pre: virus ha de ser vàlid
        // Post: Retornem true si el virus ja està afectant la regió, altrament tornem false.

        return esta_present_virus_a_la_regio(virus) != null;
    }



    /**
     * @brief Retorna una llista ordenada amb els noms dels virus presents en aquesta regió.
     * 
     * @author Romà Barrera
     * Aquest mètode recorre totes les afectacions virals que hi ha a la regió i extreu
     * el nom del virus corresponent, afegint-lo a una llista que retorna.
     * 
     * @pre L'atribut 'afectacions' ha d'estar inicialitzat i no ser null.
     * @post Retorna una llista amb el nom de tots els virus associats a les afectacions actuals de la regió.
     * 
     * @return List<String> Una llista amb els noms dels virus que afecten la regió.
     */
    public List<String> virusPresentsARegio(){
        List<String> llistaVirus = new ArrayList<>();
        for(Map.Entry<String, AfectacioVirusRegio> afectacio : afectacions.entrySet()){
            llistaVirus.add(afectacio.getKey());
        }
        Collections.sort(llistaVirus);
        return llistaVirus;
    }

    /**
     * @brief Retorna una llista ordenada amb els noms de les regions veines d'aquesta regió.
     * 
     * @author Romà Barrera
     *
     * Tot i existir el mètode regionsVeines(), és més eficaç aquest mètode per coneixer només el nom de les regions
     * veines. D'aquesta manera evitem recorrer el vector de regions en la classe destí i generar dos estructures de dades.
     * Aquest mètode recorre totes les regions veines de la regió i extreu
     * el nom, afegint-lo a una llista que retorna.
     * 
     * @pre L'atribut 'regionsVeines' ha d'estar inicialitzat i no ser null.
     * @post Retorna una llista amb el nom de totes les regions veines la regió.
     * 
     * @return List<String> Una llista amb els noms de les regions veines de la regió.
     */
    public List<String> nomRegionsVeines(){
        List<String> llistaRegionsVeines = new ArrayList<>();
        for(Regio regioVeina : regionsVeines){
            llistaRegionsVeines.add(regioVeina.nom());
        }
        Collections.sort(llistaRegionsVeines);
        return llistaRegionsVeines;
    }

    /**
     * Aquesta funció és molt important ja que quan moren persones per culpa d'algun virus, la població
     * disminueix, per tant hem de canviar aquest número. És a dir, per cada morts que es produeixen en un dia de cada
     * un dels virus que estan afectant a la regió, hem de restar-les al nombre total de persones que té la regió
     * A més, m'he assegurat de que la població mai baixi de 0, perquè no ser si és possible, però per evitar que per alguna rao,
     * morin més persones de les que viuen en la regió.
     */
    public void persones_moren(int morts) {
        // Pre: El nombre de morts ha de ser un nombre positiu.
        // Post: Restem les morts al número de persones que te la regió.

        poblacio = poblacio - morts;

        if (poblacio < 0) {
            poblacio = 0;
        }
    }

    /**
     * He fet aquest mètode per a poder retornar totes les afectacions presents en aquesta regió.
     * He tingut la necessitat de fer aquest mètode per a poder calcular els nous contagis totals de totes les
     * afectacions, i així poder aplicar la formula de mutació per coincidència.
     */
    public List<AfectacioVirusRegio> AfectacionsDeLaRegio() {
        // Pre: afectacions ha d'estar inicialitzat
        // Post: Retornem la llista amb totes les afectacions d'aquesta regio
        return new ArrayList<>(afectacions.values());
    }


    /**
     * Afegeixo informació de que fer aquest mètode per tenir una idea molt més clara. M'he basat totalment
     * en el pseudocodi donat, que estava súper bé. Basicament el que fem és mirar si en aquesta regió es pot produir
     * una mutació per coincidència. Primer de tot agrupem tots els virus ARN per famílies. Un cop tenim agrupats els virus per família,
     * només ens interessen les famílies que tenen dos o més virus en aquesta regió, ja que només en aquests casos
     * es pot produir una mutació per coincidència.
     *
     * Un cop tenim la família, fem totes les combinacions possibles de parelles (V, V') de virus d’aquesta família.
     * Per a cada parella, calculem si es produeix mutació amb la probabilitat PMC (segons la fórmula).
     * En cas que hi hagi mutació, es crea un nou virus a partir de V i V’, i aquest substitueix un percentatge dels infectats comuns.
     */
    public void comprovarMutacionsPerCoincidencia() {
        // Pre: -
        // Post: Si es produeix una mutació per coincidència entre dos virus d'una mateixa família ARN, es crea el nou virus i s'afegeix la seva afectació.

        // PRIMER: el que fem és agrupar tots els virus ARN de la regió per famílies

        Map<FamiliaVirus, List<VirusARN>> virus_per_cada_familia = agrupem_virus_segons_familia();

        // SEGON: Per cada família que tingui més d’un virus, fem totes les parelles

        /**
         * Res teoria per entendre-ho
         * Map.Entry<K, V> --> Cada element del mapa és una parella clau-valor
         * entrySet() retorna totes les parelles clau-valor del mapa (com si fos una llista de tuples).
         * I llavors per veure totes i analitzar-les, doncs ho fem amb un for.
         */

        for (Map.Entry<FamiliaVirus, List<VirusARN>> entry : virus_per_cada_familia.entrySet()) {
            List<VirusARN> virusFam = entry.getValue();
            if (virusFam.size() > 1) { // Mirem només les families que tenen dos o mes virus presents en aquesta regió

                // Si te més de dos, hem de fer parelles dos a dos
                for (int i = 0; i < virusFam.size(); i++) {
                    for (int j = i + 1; j < virusFam.size(); j++) { // fem que j comenci a i+1 i així evitem repetir parelles, i amb si mateix
                        VirusARN Virus_A = virusFam.get(i);
                        VirusARN Virus_B = virusFam.get(j);

                        AfectacioVirusRegio afectacio_A = afectacions.get(Virus_A.nom()); // (V) en el pseudocodi donat
                        AfectacioVirusRegio afectacio_B = afectacions.get(Virus_B.nom()); // (V') en el pseudocodi donat

                        int Contagiosos_A = afectacio_A.nousContagios(); // (NCD(V,R,D)) en el pseudocodi donat
                        int Contagiosos_B = afectacio_B.nousContagios(); // (NCD(V',R,D)) en el pseudocodi donat

                        double poblacio_actual_regio = poblacio; // R.poblacio(D) en el pseudocodi donat
                        if (poblacio_actual_regio == 0) continue;

                        // Calculem el nombre d’infectats comuns
                        // Inf_comuns(V,V’,R,D) = (NCD(V,R,D)/R.poblacio(D)) * (NCD(V,R,D)/R.poblacio(D))
                        double Inf_comuns = ((double) Contagiosos_A / poblacio_actual_regio) * ((double) Contagiosos_B / poblacio_actual_regio);

                        // Calculem la probabilitat de mutació per coincidència
                        // PMC(V,V’,R,D) = prob(mutCoinc(V,V’,R,D)) = Inf_comuns(V,V’,R,D) * F.probMutCoinc()
                        double PMC = Inf_comuns * entry.getKey().probMutacioCoincidencia();

                        // Generem un valor aleatori a l’interval [0,1)
                        double aleatori = Math.random();

                        VirusARN V_mes_feble;

                        VirusARN V_mes_fort = Virus_A.VirusMesFort(Virus_B); // (W) en el pseudocodi donat
                        if (V_mes_fort == Virus_A){
                            V_mes_feble = Virus_B;
                        }
                        else {
                            V_mes_feble = Virus_A;
                        }

                        AfectacioVirusRegio afectacio_feble = afectacions.get(V_mes_feble.nom());

                        if (aleatori < PMC) { // Es produeix mutació, i per tant, es crea nou virus i nova afectació
                            VirusARN V_mut = Virus_A.mutacio(Virus_B);

                            // V_nou substituirà V,V’ en un PMC*100 % dels infectats comuns de V i V’, és a dir
                            // Nous_contagis(V_mut,R,D) = Inf_comuns(V,V’,R,D) * PMC(V,V’,R,D)
                            int Nous_contagis_V_mut = (int) Math.round(Inf_comuns * PMC);

                            // Reduïm infectats del més feble
                            afectacio_feble.restarNousInfectatsAvui(Nous_contagis_V_mut);

                            // Afegim nova afectació o sumem infectats si ja existeix
                            if (afectacions.containsKey(V_mut.nom())) {
                                AfectacioVirusRegio afectacio_v_mut = afectacions.get(V_mut.nom());
                                afectacio_v_mut.afegir_infectats(Nous_contagis_V_mut);
                            } else {
                                this.afegirNovaAfectacio(V_mut, Nous_contagis_V_mut);
                            }


                        } else { // Si no hi ha mutació, tots els infectats comuns van al virus més fort
                            // Nous_contagis(W,R,D) = Inf_comuns(V,V’,R,D)
                            int Nous_contagis_V_mes_fort = (int) Math.round(Inf_comuns * poblacio_actual_regio);
                            if (Nous_contagis_V_mes_fort > 0) {
                                AfectacioVirusRegio afectacio_V_mes_fort = afectacions.get(V_mes_fort.nom());
                                afectacio_V_mes_fort.afegir_infectats(Nous_contagis_V_mes_fort);
                                afectacio_feble.restarNousInfectatsAvui(Nous_contagis_V_mes_fort);
                            }
                        }
                    }
                }
            }
        }
    }


//    // Operacions
//    public void afegirCasos(Virus virus, int nousInfectats) { // F-> AIXÒ MILLOR GESTIONAR-HO A NIVELL D'AFECTACIÓ (VIRUS-REGIÓ)
//        // Afegeix nous casos d'un virus a la regió
//        // Verifiquem que el nombre de nous infectats sigui vàlid
//        if (nousInfectats < 0) {
//            System.out.println("Error: Nombre de nous infectats no vàlid.");
//        }
//
//        // Evitem que el total d'infectats superi la població total
//        int nousCasosReals = Math.min(nousInfectats, poblacio - casosActuals);
//        casosActuals += nousCasosReals;
//
//    }


// ---------------------------------------------------------------------------------------------------------------------
// ---------------------------------------------------- CONFINAMENT ----------------------------------------------------
// ---------------------------------------------------------------------------------------------------------------------

    /**
     * Aquesta funció l'he fet per aplicar un confinament a la regió.
     * Com be està explicat a la pràctica, hi ha dos tipus, un que és el confinament dur, i l'altre que és el confinament suau.
     * Si és dur, es redueix la taxa de contacte interna i evitem qualsevol connexio amb les veïnes.
     * Si és suau, només evitem les connexions externes, però la taxa interna la deixem igual.
     * Ara bé, és molt important que, abans de fer canvis, guardem les taxes originals per poder-les recuperar després quan desconfinem.
     */
    public void aplicarConfinamentDur(double nova_taxa_interna) {
        // Pre: Si dur és true, la nova_taxa_interna ha de ser entre 0 i 1.
        // Post: Modifiquem les taxes i indiquem que la regió està confinada.

        // Primer de tot, molt important que guardem les taxes originals per quan desconfinem. (ho he fet amb un
        // mètode privat de la classe)
        guardem_les_taxes_originals();

        taxaContacteIntern = nova_taxa_interna;

        // Ara, evitem les connexions externes (amb totes les veïnes)
        for (Regio veina : regionsVeines) {
                // Tallem les connexions de les regions veines amb aquesta regio
                taxesExternesContacte.put(veina, 0.0);
                // Tallem les connexions de les veines amb aquesta regio
                veina.taxesExternesContacte.put(this, 0.0);
        }

        // Ara, canviem el nostre boolea i indiquem que aquesta regió ESTÀ en confinament
        enConfinament = true; // NO SER si cal fer aquest només quan és confinament dur o en general
    }

    /**
     * Aquesta funció serveix per fer un confinament tou (només amb una veïna concreta).
     * Tallem la connexió amb aquesta regió veïna, però no modifiquem la taxa interna ni la resta de connexions.
     */
    public void aplicarConfinamentTouAmb(Regio veina) {
        // Guardem la taxa original si no l’hem guardat encara
        if (!taxesExternesOriginals.containsKey(veina)) {
            taxesExternesOriginals.put(veina, taxesExternesContacte.get(veina));
        }
        taxesExternesContacte.put(veina, 0.0);
        veina.taxesExternesContacte.put(this, 0.0);
    }


    /**
     * He fet aquesta funció perquè, quan volguem acabar amb el confinament, doncs fem aixecarConfinament, és a dir,
     * tornem a posar les taxes que hi havia abans (que com ja he dit les hem guardat).
     * Per tant aquí, doncs bàsicament hem de tornar a com estava la regió abans del confinament.
     */
    public void aixecarConfinament() {
        // Pre: La regió estava confinada (boolea enConfinament ha de ser true)
        // Post: Recuperem totes les taxes que hi havia abans del confinament.

        // Si no tenim la taxa original guardada, no podem desconfinar
        // bueno, el null només ho he fet per assegurar, però en teoria,
        // mai hauria de ser null ja que ens assegurem de que abans de fer confinament, guardem l¡original.
        // Ara be, sempre pot haver falles i així, tal i com hem vist a teoria, amb les excepcions podem saber que
        // l'error ha estat aquí.
        if (taxaContacteInternOriginal == null) {
            throw new IllegalStateException("No es pot aixecar el confinament perquè no s’han guardat les taxes originals.");
        }


        taxaContacteIntern = taxaContacteInternOriginal;


        // Recuperem totes les taxes externes
        for (Regio veina : regionsVeines) {
            if (taxesExternesOriginals.containsKey(veina)) {
                double taxaOriginal = taxesExternesOriginals.get(veina);
                taxesExternesContacte.put(veina, taxaOriginal);
                veina.taxesExternesContacte.put(this, taxaOriginal);
            }
        }

        // Ja no estem en confinament, per tant, canviem el boolea
        enConfinament = false;
    }




// ---------------------------------------------------------------------------------------------------------------------
// -------------------------------------- METODES PRIVATS --------------------------------------------------------------
// ---------------------------------------------------------------------------------------------------------------------

    /**
     * Aquesta funció l'he fet perquè sabent un virus que afecta a la regió, poguem obtenir el seu objecte
     * corresponent VirusAfectacióRegió.
     * És a dir, bàsicament el que fem és buscar dins la llista d’afectacions quin objecte relaciona aquest virus amb la regió,
     * i si el trobem, el retornem. Ara bé, si no està, retornem null i així sabrem que aquest virus encara
     * no està en la regió.
     */
    private AfectacioVirusRegio esta_present_virus_a_la_regio (Virus virus_a_buscar) {
        // Pre: virus_a_buscar ha de ser vàlid.
        // Post: Retornem l’afectació d’aquest virus dins d'aquesta regió si existeix. Si no existeix, tornem null.

        return afectacions.get(virus_a_buscar.nom());
    }


    /**
     * Agrupem tots els virus ARN presents en aquesta regió segons la seva família. He decidit fer-ho amb un mapa on
     * la clau és l’objecte FamiliaVirus i el valor és la llista de tots els virus ARN d'aquella família que estan a la regió.
     */
    private Map<FamiliaVirus, List<VirusARN>> agrupem_virus_segons_familia() {
            // Pre: Les afectacions han d’estar inicialitzades i poden contenir diversos virus ARN.
            // Post: Retornem un mapa on agrupem per família els virus ARN que hi ha en aquesta regió.

            Map<FamiliaVirus, List<VirusARN>> virusPerFamilia = new HashMap<>();

            for (AfectacioVirusRegio a : afectacions.values()) {
                Virus v = a.quinVirusHiHa();
                if (v instanceof VirusARN) {
                    VirusARN virus_ARN = (VirusARN) v;
                    FamiliaVirus fam = virus_ARN.familia();

                    // hem de mirar si existeix o no aquesta familia, i si no existeix l'afegim en el map posant com a
                    // clau aquesta nova familia i com a valor de moment una clau buida que anirem afegint amb els virus
                    // que tinguem d'aquesta familia.

                    virusPerFamilia.putIfAbsent(fam, new ArrayList<>());
                    virusPerFamilia.get(fam).add(virus_ARN);

                }
            }

            return virusPerFamilia;
    }


    /**
     * Aquesta funció privada l'he fet perquè quan confinem, abans de canviar res,
     * guardem les taxes originals internes i externes. Així després les podem restaurar si cal.
     * He decidit separar-ho per claredat i reutilització.
     */
    private void guardem_les_taxes_originals() {
        // Guardem la taxa interna només si encara no l’hem guardat (és a dir, només el primer cop que fem el confinament,
        // després ja està guardada i no cal anar guardant-la més. A més això provocaria problemes perquè faria que
        // guardessim el valor 0 per exemple i això NO pot ser
        if (taxaContacteInternOriginal == null) {
            taxaContacteInternOriginal = taxaContacteIntern;
        }

        // Guardem cada una de les taxes externes amb les veïnes (ho hem de fer amb el for perquè sino, estariem copiant
        // la referència i modificar-la, faria modificar la original cosa que ho hem d'evitar)
        for (Regio veina : regionsVeines) {
            if (!taxesExternesOriginals.containsKey(veina)) {
                Double taxaActual = taxesExternesContacte.get(veina);
                taxesExternesOriginals.put(veina, taxaActual);
            }
        }
    }


}



