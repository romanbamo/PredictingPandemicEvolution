/** @file Interaccio.java
@brief Classe Interaccio
@author Roman Barrera
*/
import java.util.Scanner;
//@class Interaccio enllac entre aplicatiu i usuari.
public class Interaccio{
    private static String llegirText(string indicacio){
        /* @brief Permet a introduir un text.
            @pre --
            @post Mostra per pantalla les indicacions i retorna el text introduit per usuari.
            @return String
        */
        System.out.println(indicacio);
        Scanner cin = new Scanner(System.in);
        return cin.nextLine();
    }

    public static Regio fitxerRegio(){
        /* @brief Demana a l'usuari la ruta del fitxer de regions i el guarda a un Map de regions.
        @pre --
        @post Dades de fitxer de regions guardades en un Map de regions.
        */
        String path = llegirText("Introdueix la direcció del fitxer de Regions: ");

        LecturaFitxerRegio lector = new LecturaFitxerRegio();
        lector.llegirArxiuRegio(path);
            
        Map<String, Regio> Regions = lector.getRegions();

        return Regions;
    }


    public static void confinar(){
        /* @brief Permet a l'usuari introduir el tipus de confinament
        abans de avancar la simulacio una unitat de temps (dia)
        @pre confinament no buit.
        @post Estat de simulacio actual modificat
        */
        //metode presionar boto
        String confinament = llegirText("Introdueix el tipus de confinament(D=dur/T=tou/X=treure confinament): ");
        afegirConfinament(confinament);
    }

    public static void incrementarDia(){
        /* @brief Permet a l'usuari avancar la simulacio una unitat de
        temps (dia)
        @pre --
        @post Estat de simulacio incrementat en unitat de temps
        */
        //metode presionar boto, si boto true executa
        bool botoPasarDia = true
        if(botoPasarDia) avançarUnDia()
    }

    
}
