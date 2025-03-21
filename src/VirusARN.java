/**
 * @class VirusARN
 * @brief Representa un virus de tipus ARN, que pot mutar.
 * @author Iria Auladell
 */

public class VirusARN extends Virus {

// ATRIBUTS

    private double pMutEC;  // Probabilitat de mutació per error de còpia

// METODES PÚBLICS

    // Tot i que ara no hem de posar atributs, si que expliquem aqui
    // per quan seguim amb el treball, que tindrem un atribut per a aquesta
    // classe de virus que muten, que és la
    // P_mutEC  probabilitat de mutació per error de còpia

    // Fem primer el constructor de la classe VirusARN, tenint en compte aquest nou atribut
    public VirusARN(String nom_virus, FamiliaVirus familia_virus, double p_mal, int t_inc, int t_lat, double p_mor,
                    int t_con, double p_con, int t_imm, double p_mutEC) {
        // Pre: Els valors han de ser positius (prob han d'anar de 0 a 1) i el nom ha de ser vàlid.
        // Post: Es crea un virus de tipus ARN que pot mutar. A part de tenir totes les
        // característiques del Virus, tindrà també una certa probabilitat de mutació per error de còpia.

        super(nom_virus, familia_virus, p_mal, t_inc, t_lat, p_mor, t_con, p_con, t_imm, "ARN"); // això ho fem per
        // cridar al constructor de la classe abstracta Virus

        pMutEC = p_mutEC;

    }

    public double probabilitatMutacioErrorCopia() {
        // Pre: El virus ha de ser vàlid i existir
        // Post: Retorna la probabilitat de mutació per error de còpia d'aquest virus

        return pMutEC;
    }

    // Per quan implementem la classe de Mutació, és important assegurar-nos de que dos virus són
    // de la mateixa familia.
    public boolean PertanyenMateixaFamilia (VirusARN A, VirusARN B) {
        // Pre: Virus A i Virus B han d’existir
        // Post: Retorna cert si A i B formen part de la mateixa familia, altrament retorna false

        return A.familia().esLaMateixaFamilia(B.familia());

    }