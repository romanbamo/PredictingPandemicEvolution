import java.util.List;

public class MainSimulacio {
    public static void main(String[] args) {
        Simulacio simulacio = new Simulacio();
        System.out.println("=== CARREGANT DADES ===");
        System.out.println("Carregant regions...");
        simulacio.carregarRegions("regions.txt");
        List<String> regions = simulacio.mostrarRegionsActuals();
        System.out.println("Regions carregades: " + regions.size());
        System.out.println("\nCarregant virus...");
        simulacio.carregarVirus("virus.txt");
        System.out.println("Virus carregats: " + simulacio.obtenirNomsVirus());
        System.out.println("\nCarregant estat inicial...");
        try {
            simulacio.carregarEstatInicial("estatInicial.txt");
            System.out.println("Estat inicial carregat correctament");
        } catch (Exception e) {
            System.err.println("ERROR carregant estat inicial: " + e.getMessage());
            e.printStackTrace();
            return;
        }
        System.out.println("\n=== REGIONS DISPONIBLES ===");
        for (String regio : regions) {
            System.out.println("- " + regio);
            List<String> virusRegio = simulacio.mostrarVirusRegio(regio);
            if (!virusRegio.isEmpty()) {
                System.out.println("  Virus presents: " + String.join(", ", virusRegio));
            }
            List<String> veins = simulacio.mostrarRegionsVeines(regio);
            System.out.println("  Regions veïnes: " + String.join(", ", veins));
        }
        System.out.println("\n=== APLICANT CONFINAMENTS ===");
        simulacio.afegirConfinament("metropolitana_nord", 0.2f); // Confinament dur a Barcelona
        simulacio.afegirConfinament("girona", "catalunya_central"); // Confinament tou entre Girona i Barcelona
        System.out.println("\n=== SIMULANT 5 DIES ===");
        for (int i = 0; i < 5; i++) {
            simulacio.avancarDia();
            System.out.println("Dia " + (i+1) + " completat");
        }
        System.out.println("\n=== ESTADÍSTIQUES FINALS ===");
        for (String regio : regions) {
            List<String> virusRegio = simulacio.mostrarVirusRegio(regio);
            if (!virusRegio.isEmpty()) {
                System.out.println("\nRegió: " + regio);
                
                for (String virus : virusRegio) {
                    System.out.println("  Virus: " + virus);
                    System.out.println("    Infectats totals: " + simulacio.nombreInfectats(regio, virus));
                    System.out.println("    Malalts actuals: " + simulacio.nombreMalalts(regio, virus));
                    System.out.println("    Immunes actuals: " + simulacio.nombreImmunes(regio, virus));
                    System.out.println("    Contagiosos actuals: " + simulacio.nombreContagiosos(regio, virus));
                    List<Integer> evolMalalts = simulacio.evolucioMalalts(regio, virus);
                    System.out.println("    Evolució malalts (últims 5 dies): " + 
                                      evolMalalts.subList(Math.max(0, evolMalalts.size()-5), evolMalalts.size()));
                }
            }
        }
        System.out.println("\n=== AIXECANT CONFINAMENTS ===");
        simulacio.desconfinar("metropolitana_nord");
        System.out.println("Confinament dur aixecat");
        simulacio.desconfinar("girona");
        System.out.println("Confinament tou aixecat");
        
        System.out.println("Simulació completada amb èxit!");
    }
}
