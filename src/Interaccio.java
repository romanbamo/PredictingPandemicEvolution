/** @file Interaccio.java
@brief Classe Interaccio
@author Roman Barrera, Mireia Ferrer i Iria Auladell
*/
import java.util.Scanner;
//@class Interaccio enllac entre aplicatiu i usuari.
public class Interaccio{
/** @brief Permet a introduir els noms dels fitxers.
@pre Fitxers existents
@post --
    @return nomFitxer
*/
public static fitxersSimulacio(){
    Scanner teclat = new Scanner(System.in);
    string nomFitxer = teclat.nextLine();
    return nomFitxer
}
/** @brief Permet a l'usuari introduir el tipus de confinament
abans de avancar la simulacio una unitat de temps (dia)
@pre confinament no buit.
@post Estat de simulacio actual modificat
*/
public static interaccio(char confinament){
    //implementar un metode per entrar el tipus de confinament
    afegirConfinament(confinament);
}
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
/** @brief Permet a l'usuari aturar la simulacio.
@pre --
@post Simulacio aturada i resetejada
*/
public static aturarSimulacio(){
        //metode presionar boto, si boto true executa
    bool botoAturar = false 
    }
    
}