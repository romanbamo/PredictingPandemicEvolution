import java.util.ArrayList;
import java.util.List;

public class TestContagiAmbConfinament {
    public static void main(String[] args) {

        // Creem una família fictícia i un virus com a l'exemple de ProvaIriaAfectacio
        FamiliaVirus f = new FamiliaVirus("TestFam", 0.2, 0);
        VirusARN virus = new VirusARN("prova", f,
                0.5,  // pMal
                3,    // tInc
                3,    // tLat
                0.1,  // pMor
                4,    // tCon
                0.3,  // pCon (no afecta)
                10,   // tImm
                0.0   // pMutEC
        );

        // Creem dues regions connectades
        Regio regA = new Regio("RegioA", 10000, 0.2);
        Regio regB = new Regio("RegioB", 10000, 0.2);
        regA.afegirRegioVeina(regB, 0.3);
        regB.afegirRegioVeina(regA, 0.3);

        // Creem l'afectació amb valors de prova i la posem manualment (igual que el de la prova 1, he reutilitzat el codi)
        AfectacioVirusRegio afr = new AfectacioVirusRegio(virus, regA, 0);

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

        afr.posar_infectats_prova(infectats_no_contagiosos);
        afr.posar_malalts_prova(malalts);
        afr.posar_morts_prova(morts);
        afr.posar_contagiosos_prova(contagiosos);
        afr.determina_valors_prova(3040, 1515, 150);

        // Afegim aquesta afectació a la regió
        regA.afegirAfectacioProva(virus, afr); // no volem sumar més infectats

        // PAS 1: Avancem un dia sense confinament
        regA.avancarDia();
        regB.avancarDia();

        System.out.println("\n--- SENSE CONFINAMENT ---");
        System.out.println("Contagiosos RegioA: " + regA.nombreContagiosos(virus));
        System.out.println("Infectats RegioB: " + regB.nombreInfectats(virus));
        System.out.println("Poblacio RegioB: " + regB.poblacio());

        // PAS 2: Apliquem confinament dur
        regA.aplicarConfinamentDur(0.1);
        System.out.println("\n[Confinament aplicat a RegioA]");

        // PAS 3: Avancem un dia més
        regA.avancarDia();
        regB.avancarDia();

        System.out.println("\n--- AMB CONFINAMENT ---");
        System.out.println("Contagiosos RegioA: " + regA.nombreContagiosos(virus));
        System.out.println("Infectats RegioB: " + regB.nombreInfectats(virus));
        System.out.println("Poblacio RegioB: " + regB.poblacio());

        // PAS 4: Aixequem el confinament
        regA.aixecarConfinament();
        System.out.println("\n[Confinament aixecat a RegioA]");

        // PAS 5: Avancem un altre dia
        regA.avancarDia();
        regB.avancarDia();

        System.out.println("\n--- DESPRÉS DE DESCONFINAR ---");
        System.out.println("Contagiosos RegioA: " + regA.nombreContagiosos(virus));
        System.out.println("Infectats RegioB: " + regB.nombreInfectats(virus));
        System.out.println("Poblacio RegioB: " + regB.poblacio());
    }
}