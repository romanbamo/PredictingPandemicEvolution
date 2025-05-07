/** @file Interaccio.java
@brief Classe Interaccio
@author Roman Barrera
*/
import java.util.Scanner;
//@class Interaccio enllac entre aplicatiu i usuari.
public class Interaccio{
    // FRANCESC --> CAL PENSAR BÉ SI CONVÉ QUE SIGUI UN MÒDUL FUNCIONAL... POTSER CONVINDRIA UNA REFERÈNCIA A LA SIMULACIÓ
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
        // FRANCESC: SI ES POGUÉS EVITAR LA DEPENDÈNCIA DIRECTA DE REGIÓ ...
        // EN QUALSEVOL CAS, NO HAURIA DE RETORNAR UNA ÚNICA REGIÓ
        // A MÉS, AQUEST MÈTODE EL VEIG MÉS AL MAIN ...
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

    public static Virus fitxerVirus(){
        /* @brief Demana a l'usuari la ruta del fitxer de virus i el guarda a un Map de Virus.
        @pre --
        @post Dades de fitxer de virus guardades en un Map de virus.
        */
        String path = llegirText("Introdueix la direcció del fitxer de Virus: ");

        LecturaFitxerVirus lector = new LecturaFitxerVirus();
        lector.llegirArxiuVirus(path);
            
        Map<String, Virus> Virus = lector.getVirus();

        return Virus;
    }

    public static AfectacioVirusRegio fitxerInicial(){
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
        // FRANCESC: CALDRIA DEMANAR PRIMER SI ES VOL FER ALGUN CONFINAMENT O DESC. , O BÉ DONAR OPCIÓ A NO FER RES
        String confinament = llegirText("Introdueix el tipus de confinament(D=dur/T=tou/X=treure confinament): ");
        afegirConfinament(confinament); // FRANCESC: ENTENC Q AQUEST MÈTODE ÉS UN PRIVAT D'AQUESTA CLASSE (?)
        String confinament = llegirText("Introdueix el tipus de confinament(D=dur/T=tou/X=treure confinament): ");//Es pot treure confinament entre regions colindants pero no es obligatori treure el confinament de totes les regions colindants. Demanar per exemple (TipusConfinament, RegioAConfinar)
        afegirConfinament(confinament);
    }

    public static void incrementarDia(){
        // FRANCESC: NO VEIG GENS CLAR AQUEST MÈTODE. EL PLANTEJAMENT HAURIA DE SER UN ALTRE, EN TOT CAS (AQUÍ NO HI HA 
        //           CAP INTERACCIÓ)
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
