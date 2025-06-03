import java.util.List;

public class ProvaCanviValorsMutacio {
    public static void main(String[] args) {

        // MUTACIÓ PER ERROR DE CÒPIA

        FamiliaVirus fam = new FamiliaVirus("FamAuladell", 0, 20);  // tpcMax = 20%
        VirusARN virusBase = new VirusARN("VirusIria", fam, 0.5, 3, 3, 0.1, 4, 0.3, 10, 0.2);

        VirusARN mut1 = virusBase.mutacio();

        System.out.println("--- MUTACIÓ PER ERROR DE CÒPIA ---");
        System.out.println("Nom nou virus: " + mut1.nom());
        System.out.println("pMal: " + mut1.probDesenvoluparMalaltia());
        System.out.println("pMor: " + mut1.taxaMortalitat());
        System.out.println("tCon: " + mut1.tempsContagi());
        System.out.println("pCon: " + mut1.taxaContagi());


        // MUTACIÓ PER COINCIDÈNCIA

        VirusARN virusA = new VirusARN("VirusMireia", fam, 0.4, 3, 3, 0.2, 5, 0.2, 8, 0.2);
        VirusARN virusB = new VirusARN("VirusRoman", fam, 0.6, 5, 5, 0.1, 3, 0.4, 12, 0.3);

        VirusARN virusMutat = virusA.mutacio(virusB);

        System.out.println("\n--- MUTACIÓ PER COINCIDÈNCIA ---");
        System.out.println("Nom nou virus: " + virusMutat.nom());
        System.out.println("pMal: " + virusMutat.probDesenvoluparMalaltia());
        System.out.println("pMor: " + virusMutat.taxaMortalitat());
        System.out.println("tCon: " + virusMutat.tempsContagi());
        System.out.println("pCon: " + virusMutat.taxaContagi());
        System.out.println("tInc: " + virusMutat.tempsIncubacio());
        System.out.println("tLat: " + virusMutat.tempsLatencia());
        System.out.println("tImm: " + virusMutat.tempsImmunitat());
        System.out.println("pMutEC: " + virusMutat.probabilitatMutacioErrorCopia());
    }
}
