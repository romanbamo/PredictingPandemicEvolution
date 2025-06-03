import java.util.List;

public class ProvaNomMutacions {
    public static void main(String[] args) {


        FamiliaVirus fam = new FamiliaVirus("FamAuladell", 0.2, 20);

        // Creo un virus ARN inicial
        VirusARN virusOriginal = new VirusARN("VirusIria", fam,
                0.4, 3, 3, 0.1, 5, 0.3, 10, 0.5);

        System.out.println("Mutacions per error de còpia:");
        VirusARN actual = virusOriginal;
        for (int i = 1; i <= 5; i++) {
            VirusARN mutat = actual.mutacio();  // fem que el virus actual mutí
            System.out.println("- Mutacio " + i + ": " + mutat.nom());
        }

        System.out.println("\nMutacions per coincidència:");
        // Creem dos virus diferents de la mateixa família
        VirusARN virusA = new VirusARN("VirusMireia", fam,
                0.3, 2, 2, 0.05, 4, 0.2, 8, 0.4);
        VirusARN virusB = new VirusARN("VirusRoman", fam,
                0.6, 4, 3, 0.15, 6, 0.35, 12, 0.6);

        for (int i = 1; i <= 3; i++) {
            VirusARN virusMutat = virusA.mutacio(virusB);
            System.out.println("- Coincidència " + i + ": " + virusMutat.nom());
        }

        // Mostrem totes les mutacions creades per virusA
        System.out.println("\nMutacions registrades a virusA:");
        List<VirusARN> mutacions = virusA.obtenirMutacions();
        for (VirusARN v : mutacions) {
            System.out.println("- " + v.nom());
        }

        // Mostrem totes les mutacions creades per virusOriginal
        System.out.println("\nMutacions registrades a virusOriginal:");
        List<VirusARN> mutacionsErrorCopia = virusOriginal.obtenirMutacions();
        for (VirusARN v : mutacionsErrorCopia) {
            System.out.println("- " + v.nom());
        }
    }
}
