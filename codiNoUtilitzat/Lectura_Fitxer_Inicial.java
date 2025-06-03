import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.*;
import java.io.*;

public class Fitxer_Inicial extends Lectura {
    public Fitxer_Inicial(String nom_Fitxer){
        super(nom_Fitxer);
    }

    public Map<String, Map<String, Integer>> carregarInicial() {
        //Pre: format de fitxer correcte
        //Post: retorna un Map<String, Map<String, Integer>> on hi ha el nom de la regió, el nom dels virus i
        // el nombre d'infectats de cadascun
        Map<String, Map<String, Integer>> Inicial = new HashMap<>();
        List<String> linies = llegirLinies();

        String regio_actual = null;
        Map<String, Integer> infeccions = null;

        for(String linia : linies) {
            linia = linia.trim();
            if(linia.isEmpty() || linia.startsWith("#")) {
                continue;
            }

            if(linia.startsWith("regio ")) {
                regio_actual = linia.substring(6).trim();
                infeccions = new HashMap<>();
                estatInicial.put(regioActual, infeccions);
            }

            else if(linia.startsWith("NOM_VIRUS ") && regioActual != null) {
                String nomVirus = linia.substring(10).trim();
                int index = linies.indexOf(linia);
                if (index + 1 < linies.size() && linies.get(index + 1).trim().startsWith("INFECTATS ")) {
                    int infectats = Integer.parseInt(linies.get(index + 1).substring(10).trim());
                    infeccions.put(nomVirus, infectats);
                }
            }

            else if (linia.equals("*")) {
                regioActual = null;
            }
        }
        return Inicial;
    }
}
