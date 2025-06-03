import java.util.ArrayList;
import java.util.List;

public class ProvaIriaAfectacio {
    public static void main(String[] args) {

        // Creem una família fictícia
        FamiliaVirus f = new FamiliaVirus("TestFam", 0.2, 0);

        // Creem un virus ARN amb les característiques de l'exemple
        VirusARN v = new VirusARN("prova", f,
                0.5, // pMal (prob de malaltia)
                3,   // tInc (temps incubació)
                3,   // tLat (temps latència)
                0.1, // pMor (prob de mort)
                4,   // tCon (temps contagi)
                0.3, // pCon (no afecta a l'exemple)
                10,  // tImm (temps immunitat)
                0.0  // pMutEC (sense mutació)
        );

        // Creo una regió amb una població que m'he inventat
        Regio r = new Regio("TestLand", 10000, 0.2);

        // Creo l'afectació amb 0 infectats inicials (els afegeixo manualment segons el exemple que vull recrear)
        AfectacioVirusRegio afr = new AfectacioVirusRegio(v, r, 0);

        List<Integer> infectats_no_contagiosos = new ArrayList<>();
        infectats_no_contagiosos.add(200);
        infectats_no_contagiosos.add(100);
        infectats_no_contagiosos.add(90);
        infectats_no_contagiosos.add(80);

        List<Integer> contagiosos = new ArrayList<>();
        contagiosos.add(90);
        contagiosos.add(100);
        contagiosos.add(111);
        contagiosos.add(110);

        List<Integer> malalts = new ArrayList<>();
        malalts.add(42);
        malalts.add(45);
        malalts.add(55);
        malalts.add(50);

        List<Integer> morts = new ArrayList<>();
        morts.add(0);
        morts.add(0);
        morts.add(0);
        morts.add(0);


        // Forcem estat del dia D-1 segons exemple
        afr.posar_infectats_prova(infectats_no_contagiosos);
        afr.posar_malalts_prova(malalts);
        afr.posar_morts_prova(morts); // representem a, b, c com 0 per simplificar
        afr.posar_contagiosos_prova(contagiosos); // per evitar errors interns
        afr.determina_valors_prova(3040, 1515, 150);

        // Simulem 200 nous contagis (tal com diu l'exemple)

        // Imprimim resultats tal com diu l'exemple
        System.out.println("== INFECTATS NO CONTAGIOSOS INICIALS ==");
        System.out.println(afr.dona_infectats_no_contagiosos());

        System.out.println("== INFECTATS INICIALS ==");
        System.out.println(afr.dona_contagiosos());

        System.out.println("== MALALTS INICIALS ==");
        System.out.println(afr.dona_malalts());

        System.out.println("== TOTALS INICIALS ==");
        System.out.println("Total malalts: " + afr.dona_totalMalalts());
        System.out.println("Total morts: " + afr.dona_totalMorts());
        System.out.println("Total infectats: " + afr.dona_totalInfectats());

        // Ara avancem un dia
        afr.avançarUnDia();

        System.out.println("   ");
        System.out.println("AVANÇEM UN DIA :)");
        System.out.println("   ");

        // Imprimim resultats tal com diu l'exemple
        System.out.println("== INFECTATS NO CONTAGIOSOS ==");
        System.out.println(afr.dona_infectats_no_contagiosos());

        System.out.println("== INFECTATS ==");
        System.out.println(afr.dona_contagiosos());

        System.out.println("== MALALTS ==");
        System.out.println(afr.dona_malalts());

        System.out.println("== MORTS PER DIA ==");
        System.out.println(afr.dona_mortsDiaries());

        System.out.println("== TOTALS ==");
        System.out.println("Total malalts: " + afr.dona_totalMalalts());
        System.out.println("Total morts: " + afr.dona_totalMorts());
        System.out.println("Total infectats: " + afr.dona_totalInfectats());

    }
}