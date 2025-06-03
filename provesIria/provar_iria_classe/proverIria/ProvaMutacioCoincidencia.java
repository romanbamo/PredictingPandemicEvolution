import java.util.ArrayList;
import java.util.List;

public class ProvaMutacioCoincidencia {
    public static void main(String[] args) {

        FamiliaVirus f = new FamiliaVirus("FamiliaAuladell", 0.15, 10);

        VirusARN A = new VirusARN("A", f, 0.5, 2, 1, 0.1, 3, 0.3, 5, 0.0);
        VirusARN B = new VirusARN("B", f, 0.5, 2, 1, 0.1, 3, 0.3, 5, 0.0);

        Regio r = new Regio("IriaLand", 10000, 0.2);

        r.afegirNovaAfectacio(A, 3000);  // 3000 infectats inicials d'A
        r.afegirNovaAfectacio(B, 4000);  // 4000 infectats inicials de B

        AfectacioVirusRegio afA = r.esta_present_virus_a_la_regio(A);
        AfectacioVirusRegio afB = r.esta_present_virus_a_la_regio(B);

        // INFECTATS A
        List<Integer> infectatsA = new ArrayList<>();
        infectatsA.add(3000);
        afA.posar_infectats_prova(infectatsA);

        // MALALETS, MORTS i CONTAGIOSOS A
        afA.posar_malalts_prova(new ArrayList<Integer>());
        afA.posar_morts_prova(new ArrayList<Integer>());
        afA.posar_contagiosos_prova(new ArrayList<Integer>());
        afA.determina_valors_prova(3000, 0, 0);

        // INFECTATS B
        List<Integer> infectatsB = new ArrayList<>();
        infectatsB.add(4000);
        afB.posar_infectats_prova(infectatsB);

        // MALALETS, MORTS i CONTAGIOSOS B
        afB.posar_malalts_prova(new ArrayList<Integer>());
        afB.posar_morts_prova(new ArrayList<Integer>());
        afB.posar_contagiosos_prova(new ArrayList<Integer>());
        afB.determina_valors_prova(4000, 0, 0);

        // CONTAGIOSOS A
        List<Integer> contA = new ArrayList<>();
        contA.add(3000);
        afA.posar_contagiosos_prova(contA);

        // CONTAGIOSOS B
        List<Integer> contB = new ArrayList<>();
        contB.add(4000);
        afB.posar_contagiosos_prova(contB);

        r.comprovarMutacionsPerCoincidencia();

        System.out.println("=== RESULTATS DESPRÉS DE MUTACIÓ PER COINCIDÈNCIA ===");
        for (Virus v : r.afectacionsPresentes()) {
            System.out.println("- Virus: " + v.nom());
            AfectacioVirusRegio af = r.esta_present_virus_a_la_regio(v);
            System.out.println("  Infectats: " + af.dona_infectats_no_contagiosos());
            System.out.println("  Total infectats: " + af.dona_totalInfectats());
            System.out.println("  Total infectats: " + af.nousContagios());
        }
    }
}
