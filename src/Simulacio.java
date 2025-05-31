/** @file Simulacio.java
    @brief Classe Simulacio
    @author Roman Barrera
*/

//@class Simulacio mostra per pantalla les dades de l evolucio.

public class Simulacio1{
    private Map<String, Regio> mapaRegions = new HashMap<>();
    private List<Virus> llistaVirus = new ArrayList<>();
    private List<String> nomsRegions = new ArrayList<>();
    private List<String> nomsVirus = new ArrayList<>();
    private int diaSimulacio = new Integer(0);

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
                FamiliaVirusLlegit familia = familiesLlegides.get(v.familia);
                if (familia == null) {
                    System.err.println("Advertència: No s'ha trobat la família '" + v.familia + 
                                     "' per al virus '" + v.nom + "'");
                    continue;
                }

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

    public void carregarEstatInicial(String path){
        LlegirFitxerInfectatsInicialsR lector = new LlegirFitxerInfectatsInicialsR();
        try {
            List<EstatInicialLlegit> dades = lector.llegirFitxer(path);

            for (EstatInicialLlegit e : dades) {
                Regio r = mapaRegions.get(e.nomRegio);
                for(Map.Entry<String, Integer> virusInicial : e.virusInicials.entrySet()){
                    Virus actualActual = buscarVirusPerNom(virusInicial.getKey());
                    r.afegirNovaAfectacio(virusActual, virusInicial.getValue());
                }
            }
            
        } catch (IOException e) {
            System.err.println("Error llegint el fitxer d'estats actuals: " + e.getMessage());
        }
    }

    public List<String> mostrarRegionsActuals(){
        List<String> regions = new ArrayList<>();
        for(Map.Entry<String, Regio> r : mapaRegions.entrySet()){
            regions.add(r.getKey());
        }
        Collections.sort(regions);
        return regions;
    }

    public List<String> mostrarRegionsVeines(String nomRegio){
        Regio r = mapaRegions.get(nomRegio);
        return r.nomRegionsVeines();
    }

    public List<String> mostrarVirusRegio(String nomRegio){
        Regio r = mapaRegions.get(nomRegio);
        return r.virusPresentARegio();
    }

    public afegirConfinamentDur(String nomRegio, Float taxa){
        Regio r = mapaRegions.get(nomRegio);
        if (r != null) {
            r.aplicarConfinamentDur(taxa);  //confinament dur
        }
    }

    public afegirConfinamentTou(String nomRegio, String nomRegioVeina){
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
        }
        diaSimulacio++;
    }



    

    //-----------------------PRIVATS--------------------//
    private Virus buscarVirusPerNom(String nomVirus) {
        for (Virus v : llistaVirus) {
            if (v.nom.equals(nomVirus)) {
                return v;
            }
        }
        return null;
    }



}
