public class ProvaConfinament {
    public static void main(String[] args) {

        // --------------------------------------------------------------------------
        // ----------------------- PROVA DE CONFINAMENTS ----------------------------
        // --------------------------------------------------------------------------


        // PAS 1: Crear dues regions amb població que m'he inventat i una taxa de contacte interna que també m'he inventat
        Regio barcelona = new Regio("Barcelona", 1000000, 0.5);
        Regio girona = new Regio("Girona", 150000, 0.4);

        // PAS 2: Les he fet veïnes i he posat per les dos una axa de contacte. El valor dona igual, ara només vull veure
        // que quan confino passa a 0.0 i que quan desconfino passa a 0.3 un altre cop
        barcelona.afegirRegioVeina(girona, 0.3);
        girona.afegirRegioVeina(barcelona, 0.3);

        // PAS 3: Miro que s'han desat bé les taxas que he posat
        System.out.println("\n--- ABANS DE CONFINAR ---");
        System.out.println("Taxa interna BCN: " + barcelona.taxaInternaContacte());
        System.out.println("Taxa externa BCN->GIR: " + barcelona.taxaExternaContacte(girona));
        System.out.println("Taxa externa GIR->BCN: " + girona.taxaExternaContacte(barcelona));

        // PAS 4: Aplico confinament DUR a Barcelona
        barcelona.aplicarConfinamentDur(0.1);

        // PAS 5: Miro que realment les taxes han canviat (hauria de ser 0.1 la interna i 0.0 la externa)
        System.out.println("\n--- DESPRÉS DE CONFINAMENT DUR ---");
        System.out.println("Taxa interna BCN: " + barcelona.taxaInternaContacte());
        System.out.println("Taxa externa BCN->GIR: " + barcelona.taxaExternaContacte(girona));
        System.out.println("Taxa externa GIR->BCN: " + girona.taxaExternaContacte(barcelona));
        System.out.println("Barcelona confinada? " + barcelona.estaEnConfinament());

        // PAS 6: Ara el que faig és desconfinar i tornar a mirar les taxes si han tornat que ser iguals que al princi
        barcelona.aixecarConfinament();

        System.out.println("\n--- DESPRÉS D'AIXECAR CONFINAMENT ---");
        System.out.println("Taxa interna BCN: " + barcelona.taxaInternaContacte());
        System.out.println("Taxa externa BCN->GIR: " + barcelona.taxaExternaContacte(girona));
        System.out.println("Taxa externa GIR->BCN: " + girona.taxaExternaContacte(barcelona));
        System.out.println("Barcelona confinada? " + barcelona.estaEnConfinament());

        // PAS 7: Ara en contes de fer el dur, faig el TOU només entre Girona i Barcelona
        girona.aplicarConfinamentTouAmb(barcelona);

        System.out.println("\n--- DESPRÉS DE CONFINAMENT TOU GIR->BCN ---");
        System.out.println("Taxa externa BCN->GIR: " + barcelona.taxaExternaContacte(girona));
        System.out.println("Taxa externa GIR->BCN: " + girona.taxaExternaContacte(barcelona));
        System.out.println("Girona confinada? " + girona.estaEnConfinament());

        // PAS 8: Miro que realemnt les dos tenen 0.0 i per tant, que el mètode que em diu que estan aillades funciona
        System.out.println("\n--- COMPROVACIÓ CONFINAMENT ENTRE REGIONS ---");
        System.out.println("Hi ha confinament entre BCN i GIR? " + barcelona.hiHaConfinamentAmb(girona));
    }
}
