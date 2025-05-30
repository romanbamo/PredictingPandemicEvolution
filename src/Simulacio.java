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
    private int diaSimulacio = new Integer();

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

    public List<String> mostrarVirusRegio(String nomRegio){
        Regio r = mapaRegions.get(nomRegio);
        return r.virusPresentARegio();
    }

    public afegirConfinamentDur(String nomRegio, Float taxa){
        Regio r = mapaRegions.get(nomRegio);
        r.afegirConfinamentDur(taxa);
    }

    public afegirConfinamentTou(String nomRegio, String nomRegioVeina){
        Regio r = mapaRegions.get(nomRegio);
        Regio v = mapaRegions.get(nomRegioVeina);
        r.afegirConfinamentTou(v);
    }
 
    
    private Virus buscarVirusPerNom(String nomVirus) {
        for (Virus v : llistaVirus) {
            if (v.nom.equals(nomVirus)) {
                return v;
            }
        }
        return null;
    }



}
