import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.io.IOException;

/** 
 * @file Simulacio.java
 * @brief Classe que representa una simulació d'epidèmies a diverses regions
 * @author Roman Barrera
 */

/**
 * @class Simulacio
 * @brief Classe que gestiona la simulació de l'evolució de virus en diferents regions
 * 
 * Aquesta classe permet carregar dades de regions, virus i estats inicials,
 * gestionar confinaments, i obtenir informació sobre l'estat i evolució de les epidèmies.
 */

public class Simulacio{
    private Map<String, Regio> mapaRegions = new HashMap<>();   ///< Mapa que associa noms de regions amb objectes Regio
    private List<Virus> llistaVirus = new ArrayList<>();        ///< Llista de virus presents a la simulació
    private List<String> nomsRegions = new ArrayList<>();       ///< Llista de noms de regions
    private List<String> nomsVirus = new ArrayList<>();         ///< Llista de noms de virus
    private int diaSimulacio = 0;                  ///< Dia actual de la simulació

    /**
     * @brief Carrega les dades de regions des d'un fitxer
     * @param path Ruta del fitxer a carregar
     * @pre El fitxer ha de tenir el format correcte i existir
     * @post El mapa mapaRegions contindrà totes les regions llegides del fitxer
     */
    public void carregarRegions(String path) {
        LlegirFitxerRegionsR lector = new LlegirFitxerRegionsR();
        try {
            List<RegioLlegida> dades = lector.llegirFitxer(path);
            Map<String, Map<String, Double>> regionsVeines = new HashMap<>();

            for (RegioLlegida r : dades) {
                Regio novaRegio = new Regio(r.nom, r.nHab, r.taxaMobInt);
                mapaRegions.put(r.nom, novaRegio);
                regionsVeines.put(r.nom, r.veins);
                nomsRegions.add(r.nom);
            }

            for (Map.Entry<String, Regio> entry : mapaRegions.entrySet()) {
                String nomRegio = entry.getKey();
                Regio regio = entry.getValue();
                
                Map<String, Double> veins = regionsVeines.get(nomRegio);
                if (veins != null) {
                    for (Map.Entry<String, Double> vei : veins.entrySet()) {
                        String nomVeina = vei.getKey();
                        Double taxa = vei.getValue();
                        
                        Regio regioVeina = mapaRegions.get(nomVeina);
                        if (regioVeina != null) {
                            regio.afegirRegioVeina(regioVeina, taxa);
                        } else {
                            System.err.println("Advertència: Regió veïna no trobada - " + nomVeina);
                        }
                    }
                }
            }
            
        } catch (IOException e) {
            System.err.println("Error llegint el fitxer de regions: " + e.getMessage());
        }
    }
    /**
     * @brief Carrega les dades de virus des d'un fitxer i els afegeix a la llista de virus
     * @param path Ruta del fitxer a carregar
     * @pre El fitxer ha de tenir el format correcte i existir
     * @post La llista llistaVirus contindrà tots els virus llegits del fitxer
     */
    public void carregarVirus(String path) {
        LlegirFitxerVirusR lector = new LlegirFitxerVirusR();
        try {
            Map<String, Map<String, ?>> dades = lector.llegirFitxer(path);
            Map<String, FamiliaVirusLlegit> familiesLlegides = lector.familiesVirusLlegides();
            Map<String, VirusLlegit> virusLlegits = lector.virusLlegits();

            for (VirusLlegit v : virusLlegits.values()) {
                FamiliaVirusLlegit familiaLlegida = familiesLlegides.get(v.familia);
                if (familiaLlegida == null) {
                    System.err.println("Advertència: No s'ha trobat la família '" + v.familia + 
                                     "' per al virus '" + v.nom + "'");
                    continue;
                }

                FamiliaVirus familia = new FamiliaVirus(familiaLlegida.nom, familiaLlegida.probMutCoincidencia, familiaLlegida.tpcMaximVariacio);

                Virus nouVirus;
                if (v.tipus.equals("ARN")) {
                    nouVirus = new VirusARN(v.nom, 
                                          familia, 
                                          v.probMalaltia, 
                                          v.incubacio, 
                                          v.latencia, 
                                          v.mortalitat, 
                                          v.duradaContagi,
                                          v.taxaContagi, 
                                          v.duradaImmunitat, 
                                          v.probMutacioCopia);
                } 
                else if (v.tipus.equals("ADN")) {
                    nouVirus = new VirusADN(v.nom, 
                                          familia, 
                                          v.probMalaltia, 
                                          v.incubacio, 
                                          v.latencia, 
                                          v.mortalitat, 
                                          v.duradaContagi,
                                          v.taxaContagi, 
                                          v.duradaImmunitat);
                } 
                else {
                    System.err.println("Advertència: Tipus de virus desconegut '" + v.tipus + 
                                     "' per al virus '" + v.nom + "'");
                    continue;
                }
                llistaVirus.add(nouVirus);
            }
            
        } catch (IOException e) {
            System.err.println("Error llegint el fitxer de virus: " + e.getMessage());
        }
    }
    /**
     * @brief Carrega l'estat inicial d'infeccions des d'un fitxer
     * @param path Ruta del fitxer a carregar
     * @pre El fitxer ha de tenir el format correcte i existir
     * @post Les regions indicades al fitxer tindran els infectats inicials configurats
     */
    public void carregarEstatInicial(String path){
        LlegirFitxerInfectatsInicialsR lector = new LlegirFitxerInfectatsInicialsR();
        try {
            List<EstatInicialLlegit> dades = lector.llegirFitxer(path);

            for (EstatInicialLlegit e : dades) {
                Regio r = mapaRegions.get(e.nomRegio);
                for(Map.Entry<String, Integer> virusInicial : e.virusInicials.entrySet()){
                    Virus virusActual = buscarVirusPerNom(virusInicial.getKey());
                    r.afegirNovaAfectacio(virusActual, virusInicial.getValue());
                }
            }
            
        } catch (IOException e) {
            System.err.println("Error llegint el fitxer d'estats actuals: " + e.getMessage());
        }
    }

    /**
     * @brief Retorna una llista ordenada de totes les regions carregades
     * @return Llista de noms de regions ordenats alfabèticament
     */
    public List<String> mostrarRegionsActuals(){
        List<String> regions = new ArrayList<>();
        for(Map.Entry<String, Regio> r : mapaRegions.entrySet()){
            regions.add(r.getKey());
        }
        Collections.sort(regions);
        return regions;
    }

    /**
     * @brief Retorna una llista de regions veïnes d'una regió donada
     * @param nomRegio Nom de la regió a consultar
     * @return Llista de noms de regions veïnes
     */
    public List<String> mostrarRegionsVeines(String nomRegio){
        Regio r = mapaRegions.get(nomRegio);
        return r.nomRegionsVeines();
    }

    /**
     * @brief Retorna una llista de virus presents en una regió
     * @param nomRegio Nom de la regió a consultar
     * @return Llista de noms de virus presents a la regió
     */
    public List<String> mostrarVirusRegio(String nomRegio){
        Regio r = mapaRegions.get(nomRegio);
        return r.virusPresentsARegio();
    }

    /**
     * @brief Aplica un confinament dur a una regió
     * @param nomRegio Nom de la regió a confinar
     * @param taxa Taxa de mobilitat durant el confinament
     */
    public void afegirConfinament(String nomRegio, Float taxa){
        Regio r = mapaRegions.get(nomRegio);
        if (r != null) {
            r.aplicarConfinamentDur(taxa);  //confinament dur
        }
    }

    /**
     * @brief Aplica un confinament tou entre dues regions veïnes
     * @param nomRegio Nom de la primera regió
     * @param nomRegioVeina Nom de la regió veïna
     */
    public void afegirConfinament(String nomRegio, String nomRegioVeina){
        Regio r = mapaRegions.get(nomRegio);
        Regio v = mapaRegions.get(nomRegioVeina);
        if (r != null) {
            r.aplicarConfinamentTouAmb(v);
            v.aplicarConfinamentTouAmb(r); // molt important, el confinament ha de ser simètric. Això és necessari
            // sobretot per poder guardar les taxes externes originals en abdues regions, sino, només podriem
            // tornar a l'original a una d'elles
        }
    }

    /**
     * @brief Aixeca el confinament d'una regió
     * @param nomRegio Nom de la regió a desconfinar
     */
    public void desconfinar(String nomRegio){
        Regio r = mapaRegions.get(nomRegio);
        r.aixecarConfinament();
    }

    /**
     * @brief Retorna el nombre total d'infectats per un virus en una regió.
     * @param nomRegio Nom de la regió a consultar.
     * @param nomVirus Nom del virus a consultar.
     * @return Nombre d'infectats (contagiosos i no contagiosos) per aquest virus.
     */
    public int nombreInfectats(String nomRegio, String nomVirus) {
        Regio r = mapaRegions.get(nomRegio);
        Virus v = buscarVirusPerNom(nomVirus);
        return r.nombreInfectats(v);
    }

    /**
     * @brief Retorna el nombre de persones malaltes per un virus en una regió.
     * @param nomRegio Nom de la regió a consultar.
     * @param nomVirus Nom del virus a consultar.
     * @return Nombre de malalts per aquest virus a la regió.
     */
    public int nombreMalalts(String nomRegio, String nomVirus) {
        Regio r = mapaRegions.get(nomRegio);
        Virus v = buscarVirusPerNom(nomVirus);
        return r.nombreMalalts(v);
    }

    /**
     * @brief Retorna el nombre de nous contagis per un virus en una regió.
     * @param nomRegio Nom de la regió a consultar.
     * @param nomVirus Nom del virus a consultar.
     * @return Nombre de nous contagis per aquest virus a la regió.
     */
    public int nousInfectats(String nomRegio, String nomVirus) {
        Regio r = mapaRegions.get(nomRegio);
        Virus v = buscarVirusPerNom(nomVirus);
        return r.nousInfectats(v);
    }

    /**
     * @brief Retorna el nombre de noves defuncions per un virus en una regió.
     * @param nomRegio Nom de la regió a consultar.
     * @param nomVirus Nom del virus a consultar.
     * @return Nombre de noves defuncions per aquest virus a la regió.
     */
    public int novesDefuncions(String nomRegio, String nomVirus) {
        Regio r = mapaRegions.get(nomRegio);
        Virus v = buscarVirusPerNom(nomVirus);
        return r.novesDefuncions(v);
    }

    /**
     * @brief Retorna el nombre de persones immunes a un virus en una regió.
     * @param nomRegio Nom de la regió a consultar.
     * @param nomVirus Nom del virus a consultar.
     * @return Nombre de persones immunes a aquest virus a la regió.
     */
    public int nombreImmunes(String nomRegio, String nomVirus){
        Regio r = mapaRegions.get(nomRegio);
        Virus v = buscarVirusPerNom(nomVirus);
        return r.nombreImmunes(v);   
    }

    /**
     * @brief Retorna el nombre de persones contagioses per un virus en una regió.
     * @param nomRegio Nom de la regió a consultar.
     * @param nomVirus Nom del virus a consultar.
     * @return Nombre de persones que poden contagiar el virus en aquesta regió.
     */
    public int nombreContagiosos(String nomRegio, String nomVirus) {
        Regio r = mapaRegions.get(nomRegio);
        Virus v = buscarVirusPerNom(nomVirus);
        return r.nombreContagiosos(v);
    }

    /**
     * @brief Retorna l’evolució de malalts per un virus concret dins d’una regió.
     * @pre El nom de la regió i del virus han de ser vàlids.
     * @post Retorna una llista amb el nombre de malalts per dia.
     * @param nomRegio Nom de la regió.
     * @param nomVirus Nom del virus.
     * @return Llista amb el nombre de malalts per dia.
     */
    public List<Integer> evolucioMalalts(String nomRegio, String nomVirus) {
        Regio r = mapaRegions.get(nomRegio);
        Virus v = buscarVirusPerNom(nomVirus);
        return r.evolucioMalalts(v);
    }

    /**
     * @brief Retorna l’evolució d’immunes per un virus concret dins d’una regió.
     * @pre El nom de la regió i del virus han de ser vàlids.
     * @post Retorna una llista amb el nombre d’immunes per dia.
     * @param nomRegio Nom de la regió.
     * @param nomVirus Nom del virus.
     * @return Llista amb el nombre d’immunes per dia.
     */
    public List<Integer> evolucioImmunes(String nomRegio, String nomVirus) {
        Regio r = mapaRegions.get(nomRegio);
        Virus v = buscarVirusPerNom(nomVirus);
        return r.evolucioImmunes(v);
    }

    /**
     * @brief Retorna l’evolució de contagiosos per un virus concret dins d’una regió.
     * @pre El nom de la regió i del virus han de ser vàlids.
     * @post Retorna una llista amb el nombre de contagiosos per dia.
     * @param nomRegio Nom de la regió.
     * @param nomVirus Nom del virus.
     * @return Llista amb el nombre de contagiosos per dia.
     */
    public List<Integer> evolucioContagiosos(String nomRegio, String nomVirus) {
        Regio r = mapaRegions.get(nomRegio);
        Virus v = buscarVirusPerNom(nomVirus);
        return r.evolucioContagiosos(v);
    }

    /**
     * @brief Retorna l’evolució de defuncions per un virus concret dins d’una regió.
     * @pre El nom de la regió i del virus han de ser vàlids.
     * @post Retorna una llista amb el nombre de morts per dia.
     * @param nomRegio Nom de la regió.
     * @param nomVirus Nom del virus.
     * @return Llista amb el nombre de morts per dia.
     */
    public List<Integer> evolucioMorts(String nomRegio, String nomVirus) {
        Regio r = mapaRegions.get(nomRegio);
        Virus v = buscarVirusPerNom(nomVirus);
        return r.evolucioMorts(v);
    }

    /**
     * @brief Retorna les estadístiques totals d’afectació d’un virus dins d’una regió.
     * @pre Els noms de la regió i del virus han de ser vàlids.
     * @post Retorna una llista amb: [total infectats, total malalts, total morts]
     * @param nomRegio Nom de la regió.
     * @param nomVirus Nom del virus.
     * @return Llista amb les estadístiques totals per a aquesta combinació.
     */
    public List<Integer> obtenirAcumulatsTotals(String nomRegio, String nomVirus) {
        Regio regio = mapaRegions.get(nomRegio);
        Virus virus = buscarVirusPerNom(nomVirus);
        return regio.acumulatsTotals(virus);
    }

    /**
     * @brief Avança un dia en la simulació per a totes les regions del territori.
     * @pre El mapa de regions ha d’estar inicialitzat i contenir objectes Regio vàlids.
     * @post S’ha avançat un dia de simulació per a cada regió, actualitzant les afectacions de cada virus.
     */
    public void avancarDia() {
        for (Regio r : mapaRegions.values()) {
            r.avancarDia();
            r.comprovarMutacionsPerCoincidencia(); // Mutació per coincidència es fa dins la regió
        }
        controlar_mutacions_per_error_de_copia(); // Mutació per error de còpia l'he fet global com un mètode privat de simulacio (he hagut de tenir en compte totes les regions)
        diaSimulacio++;
    }



    

    //-----------------------PRIVATS--------------------//

    /**
     * @brief Busca un virus pel seu nom
     * @param nomVirus Nom del virus a buscar
     * @return L'objecte Virus corresponent o null si no es troba
     * @private
     */
    private Virus buscarVirusPerNom(String nomVirus) {
        for (Virus v : llistaVirus) {
            if (v.nom().equals(nomVirus)) {
                return v;
            }
        }
        return null;
    }


    /**
     * Aquesta funció el que fa és mirar si en el dia d'avui, es produeix una mutació per error de copia d'algun virus.
     * Ho fem a partir de la fórmula que ens han donat a l'annex. En cas que sí que hi hagi mutació, es calcula el
     * nou virus mutat, i s'actualitza l'afectació de la regió per aquest nou virus.
     *
     * @ autor = IRIA AULADELL
     */
    private void controlar_mutacions_per_error_de_copia() {
    // Pre: Cal haver avançat el dia a totes les regions i tenir actualitzats els nous contagis del dia.
    // Post: Alguns virus poden haver mutat, apareixent una nova afectació en aquestes regions.
    // Els nous contagis del virus original es redueixen.

        Map<VirusARN, Integer> contagis_totals = new HashMap<>();

        // Segons el anex de fòrmules, primer de tot, el que hem de fer és calcular els nous contagis totals per
        // cada virus que pot mutar --> pm(V,D) = tm(V)*nc(V,D) si aquest producte < =1; altrament 1
        // tm(V) = V.probMutErrorCopia()
        // nc(V,D) = dAct(D).nousContagis(V)

        // primer de tot, llavors, hem de considerar la suma de nous contagis de cada virus sobre totes les regions
        // Potser es pot fer de manera més eficient, però la idea que he tingut ha estat recorrer totes les regions,
        // veure cada una de les afectacions de cada una i després doncs separa per virus. És a dir, mirem la primera
        // regio amb les seves afectacions i per cada virus doncs posem al mapa els nous contagis. Després, mirem
        // la següent regoó amb les seves afectacions. Si els virus que hi han no els tenim en el mapa, les afegim amb
        // els seus nous contagis. Si en canvi ja estan en el mapa (ja afecten una regió que hem mirat), sumem els nous
        // contagis de la regió que estem mirant, amb els nous contagis de les regions diferents. Al final acabarem
        // tenint un MAP on per cada virus, tindrem la suma de tots els nous contagis tenint en compte totes les
        // regions a les quals està afectant

        for (Regio regio : mapaRegions.values()) {
            for (AfectacioVirusRegio afectacio : regio.AfectacionsDeLaRegio()) {
                Virus virus = afectacio.quinVirusHiHa();
                if (virus.muta()) { // aquí fem us del nostre mètode sobreescrit que ens diu si és o no ARN
                // si torna false, no es ARN ja que no muta, en canvi, si és verdader, vol dir que és ARN ja que
                // és l'únic que pot mutar

                    // Com ja hem comprovat que virus.muta() == true, sabem que és un VirusARN.
                    VirusARN virusARN = (VirusARN) virus;

                    int nous = afectacio.nousContagios();
                    contagis_totals.put(virus, contagis_totals.getOrDefault(virus, 0) + nous);
                    // amb aquesta linia, mirem si el virus ja està en el mapa.
                }
            }
        }

        // Ara ens cal saber si hi ha o no mutació. Per determinar si es produeix mutació, generarem un nombre
        // aleatori entre 0 i 1. Si resulta menor que pm(V,D), hi haurà mutació, i no n’hi haurà en cas contrari.

        for (Map.Entry<VirusARN, Integer> entry : contagis_totals.entrySet()) {
            VirusARN virus_que_pot_mutar = entry.getKey();
            int nc = entry.getValue();

            double pm = virus_que_pot_mutar.probabilitatMutacioErrorCopia() * nc;
            if (pm > 1.0) pm = 1.0; // no hauria de passar, pero bueno, ens assegurem que com a max tinguem 1

            double aleatori = Math.random();
            if (aleatori < pm) {
                Virus virus_mutat = virus_que_pot_mutar.mutacio(); // aqui cridem el metode que tinc a VirusARN que
                // gestiona el nou virus mutat per error de copia
                llistaVirus.add(virus_mutat); // afegim el nou virus a la simulació

                // Aquest nou virus Vnou substituirà a V en un pm(V,D)*100 % dels nous contagis de V, és a dir, Vnou infectarà
                // La fòrmula que segueixo és la de l'annex --> nc(Vnou,D) = nc(V,D) * pm(V,D)
                int nous_contagis_mutat = (int) Math.round(pm * nc);


                // Caldrà distribuir aquests nous contagiats de Vnou entre les regions afectades per nous contagis de V,
                // de forma proporcional al nombre de nous contagis de V. Es crearan, doncs, noves afectacions en aquestes regions.
                for (Regio regio : mapaRegions.values()) {
                    for (AfectacioVirusRegio afectacio : regio.AfectacionsDeLaRegio()) {
                        if (afectacio.quinVirusHiHa().equals(virus_que_pot_mutar)) {
                            int contagis_en_la_regio = afectacio.nousContagios();
                            if (contagis_en_la_regio > 0) {
                                double proporcio = (double) contagis_en_la_regio / nc;
                                int mutats = (int) Math.round(nous_contagis_mutat * proporcio);

                                // Ara, nc(V,D) = nc(V,D) - nc(Vnou,D) (3)
                                if (mutats > 0) {
                                    regio.afegirNovaAfectacio(virus_mutat, mutats);
                                    afectacio.restarNousInfectatsAvui(mutats);
                                }
                            }
                        }
                    }
                }
            }
        }
    }



}
