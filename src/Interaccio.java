/** @file Interaccio.java
@brief Classe Interaccio
@author Roman Barrera, Mireia Ferrer i Iria Auladell
*/
import java.util.Scanner;
//@class Interaccio enllac entre aplicatiu i usuari.
public class Interaccio{
    public static String llegirText(string indicacio){
        System.out.println(indicacio);
        Scanner cin = new Scanner(System.in);
        return cin.nextLine();
    }
    /** @brief Permet a introduir els noms dels fitxers.
    @pre Fitxers existents
    @post --
        @return nomFitxer
    */
    /** @brief Permet a l'usuari introduir el tipus de confinament
    abans de avancar la simulacio una unitat de temps (dia)
    @pre confinament no buit.
    @post Estat de simulacio actual modificat
    */
    /** @brief Permet a l'usuari avancar la simulacio una unitat de
    temps (dia)
    @pre --
    @post Estat de simulacio incrementat en unitat de temps
    */
    public static incrementarDia(){
        //metode presionar boto, si boto true executa
        bool botoPasarDia = true
        if(botoPasarDia) avançarUnDia()
    }

    
}
