import java.util.ArrayList;
import java.util.List;

public class ProvaMutacioErrorCopia {
    public static void main(String[] args) {

        // 1. Creem una família amb 100% de variació (tpcMax) i activem mutació per error de còpia
        FamiliaVirus f = new FamiliaVirus("Mutadora", 0.0, 20); // 0.0 per coincidència, 20% de variació

        // 2. Creem un virus ARN amb pMutEC = 1.0 per forçar mutació
        VirusARN v = new VirusARN("mutant", f,
                0.5, 2, 1, 0.1, 3, 0.3, 5, 1.0); // tInc = 2, tLat = 1, tCon = 3, tImm = 5

        // 3. Creem una regió de prova
        Regio r = new Regio("Mutoland", 10000, 0.2);

        // 4. Inicialitzem una afectació amb 0 infectats
        AfectacioVirusRegio afr = new AfectacioVirusRegio(v, r, 0);

        // Registrem manualment l'afectació
        r.afegirNovaAfectacio(v, 0); // Perquè aparegui al mapa intern

        // 5. Simulem 100 nous infectats avui
        List<Integer> infectatsAvui = new ArrayList<>();
        infectatsAvui.add(100); // posició 0 = avui
        afr.posar_infectats_prova(infectatsAvui);
        afr.posar_malalts_prova(new ArrayList<>());
        afr.posar_morts_prova(new ArrayList<>());
        afr.posar_contagiosos_prova(new ArrayList<>());
        afr.determina_valors_prova(100, 0, 0);



        // AQUI NOMES VULL PROVAR QUE EL NOM ES CREI CORRECTAMENT, CRIDAR LA FUNCIO DE MUTACIO PER ERROR DE COPIA PERO DESDE VIRUS

    }
}
