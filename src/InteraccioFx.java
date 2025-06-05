/**
 * @file InteraccioFxAjuda.java
 * @brief Aplicació JavaFX per a la simulació d'una pandèmia amb diferents nivells de confinament.
 * @author Roma Barrera 
 * 
 * Aquesta aplicació permet carregar fitxers de dades, seleccionar regions i virus,
 * aplicar diferents mesures de confinament i visualitzar dades diàries i acumulades
 * en format textual i gràfic.
 */

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.*;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.Node;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;


/**
 * @class InteraccioFxAjuda
 * @brief Classe principal que hereta de Application i conté tota la lògica de la interfície.
 *
 * Gestiona dues finestres principals: una per a la simulació diària i una altra per als acumulats.
 * Permet seleccionar fitxers, configurar confinaments i visualitzar dades epidemiològiques.
 *
 * @invariant simulacio != null
 * @invariant valorRegioDades != null && valorVirusDades != null && valorRegioDadesAcum != null && valorVirusDadesAcum != null && valorRegioConfinament != null
 * @invariant graficSimulacio != null && graficAcumulats != null
 * @invariant codiConfinament ∈ {0, 1, 2}
 * @invariant codiDadaDiaria ∈ {0, 1, 2, 3}
 *
 */
public class InteraccioFx extends Application {
    private Simulacio simulacio = new Simulacio();
    private GridPane gridPaneDiari;
    private GridPane gridPaneAcumulats;
    private Stage escenariPrincipalSimulacio;          ///< Finestra principal de simulació diària
    private Stage escenariPrincipalAcumulats;          ///< Finestra principal de dades acumulades
    private int codiConfinament = 0;                   ///< Codi del nivell de confinament actual
    private int codiDadaDiaria = 0;                   ///< Codi del tipus de dada diaria a consultar
    private float taxaConfinament = 0;                 ///< Taxa de confinament aplicada (0-100)
    private Label etiquetaConfirmacioVirus;            ///< Etiqueta que confirma selecció de fitxer de virus
    private Label etiquetaConfirmacioRegio;            ///< Etiqueta que confirma selecció de fitxer de regió
    private Label etiquetaConfirmacioEstat;            ///< Etiqueta que confirma selecció de fitxer d'estat
    private String rutaFitxerVirus;                    ///< Ruta del fitxer de virus seleccionat
    private String rutaFitxerRegio;                    ///< Ruta del fitxer de regió seleccionat
    private String rutaFitxerEstat;                    ///< Ruta del fitxer d'estat inicial seleccionat
    private BarChart<String, Number> graficSimulacio;
    private BarChart<String, Number> graficAcumulats;    
    private final List<String> labelsDades = new ArrayList<>(Arrays.asList(
        "Regió:", "Virus:", "Infectats:", "Contagiosos:", 
        "Malalts:", "Nous infectats:", "Defuncions:", "Inmunes:"
    ));
    private final List<Label> valorsDades = new ArrayList<>();
    private final StringProperty valorRegioDades = new SimpleStringProperty(); ///< Regió seleccionada per l'estudi de les dades
    private final StringProperty valorVirusDades = new SimpleStringProperty(); ///< Virus seleccionat per l'estudi de les dades
    private final StringProperty valorRegioDadesAcum = new SimpleStringProperty(); ///< Regió seleccionada per l'estudi de les dades
    private final StringProperty valorVirusDadesAcum = new SimpleStringProperty(); ///< Virus seleccionat per l'estudi de les dades
    private final StringProperty valorRegioConfinament = new SimpleStringProperty();///<Regió seleccionada per al confinament tou
    /**
     * @enum NivellConfinament
     * @brief Enumeració que representa els diferents nivells de confinament.
     */
    public enum NivellConfinament {
        SENSE("Sense confinament", 0),  ///< Sense mesures de confinament
        TOU("Tou", 1),                  ///< Confinament lleu
        DUR("Dur", 2);                  ///< Confinament estricte
        
        private final String text;      ///< Text descriptiu del nivell
        private final int codi;         ///< Codi numèric del nivell
        
        /**
         * @brief Constructor de l'enumeració.
         * @param text Descripció textual del nivell de confinament
         * @param codi Valor numèric associat al nivell
         */
        NivellConfinament(String text, int codi) {
            this.text = text;
            this.codi = codi;
        }
        
        @Override
        public String toString() {
            return text;
        }
        
        /**
         * @brief Retorna el codi numèric del nivell de confinament.
         * @return Enter que representa el nivell
         */
        public int codi() {
            return codi;
        }
    }

    /**
     * @enum NivellConfinament
     * @brief Enumeració que representa els diferents nivells de confinament.
     */
    public enum DadesDiaries {
        MALALTS("Malalts", 0),          ///< Malalts
        IMMUNES("Immunes", 1),          ///< Immunes
        CONTAGIOSOS("Contagiosos", 2),  ///< Contagiosos
        DEFUNCIONS("Defuncions", 3);    ///< Morts diaris
        
        private final String text;      ///< Text descriptiu del nivell
        private final int codi;         ///< Codi numèric del nivell
        
        /**
         * @brief Constructor de l'enumeració.
         * @param text Descripció textual
         * @param codi Valor numèric associat
         */
        DadesDiaries(String text, int codi) {
            this.text = text;
            this.codi = codi;
        }
        
        @Override
        public String toString() {
            return text;
        }
        
        /**
         * @brief Retorna el codi numèric.
         * @return Enter que representa la dada
         */
        public int codi() {
            return codi;
        }
    }

    /**
     * @brief Mètode d'inici de l'aplicació JavaFX.
     * @param finestraFitxers Finestra principal d'inicialització de JavaFX
     * @pre Cert
     * @post S'ha obert la finestra de selecció de fitxers
     */
    @Override
    public void start(Stage finestraFitxers) {
        obrirFinestraFitxers();
    }

    /**
     * @brief Configura i mostra la finestra de selecció de fitxers.
     * @pre Cert
     * @post S'ha creat i mostrat la finestra de selecció de fitxers
     */
    private void obrirFinestraFitxers() {
        Stage finestraFitxers = new Stage();
        finestraFitxers.setTitle("Infektopia");

        FileChooser seleccionadorFitxer = new FileChooser();
        Label titolPrincipalFitxers = crearTitol("Benvingut/da a la simulació InfekTopia!", 28);
        Label subtitolFitxers = crearTitol("Selecciona els fitxers corresponents per inicialitzar la simulació:", 16);

        inicialitzarEtiquetesConfirmacio();
        
        Button botoVirus = crearBotoFitxer("Buscar arxiu Virus", finestraFitxers, etiquetaConfirmacioVirus);
        Button botoRegio = crearBotoFitxer("Buscar arxiu Regió", finestraFitxers, etiquetaConfirmacioRegio);
        Button botoEstat = crearBotoFitxer("Buscar arxiu Estat Inicial", finestraFitxers, etiquetaConfirmacioEstat);
        Button botoTancarFitxers = new Button("D'acord'");
        botoTancarFitxers.setOnAction(e -> {
            simulacio.carregarVirus(rutaFitxerVirus);
            simulacio.carregarRegions(rutaFitxerRegio);
            simulacio.carregarEstatInicial(rutaFitxerEstat);
            obrirFinestraSimulacio();
            finestraFitxers.close();
        });

        HBox caixaVirus = new HBox(10,botoVirus, etiquetaConfirmacioVirus);
        HBox caixaRegio = new HBox(10,botoRegio, etiquetaConfirmacioRegio);
        HBox caixaEstat = new HBox(10,botoEstat, etiquetaConfirmacioEstat);
        HBox contenidorTancar = new HBox(10,botoTancarFitxers);
        contenidorTancar.setAlignment(Pos.BOTTOM_RIGHT);

        VBox contenidorBotons = new VBox(20, caixaVirus, caixaRegio, caixaEstat);
        BorderPane contenidorGeneral = new BorderPane();
        contenidorGeneral.setPadding(new Insets(50));
        contenidorTancar.setPadding(new Insets(10));

        VBox centre = new VBox(30, subtitolFitxers, contenidorBotons);
        BorderPane.setMargin(centre, new Insets(100, 0, 0, 0));
        contenidorGeneral.setTop(titolPrincipalFitxers);
        contenidorGeneral.setCenter(centre);
        contenidorGeneral.setBottom(contenidorTancar);

        finestraFitxers.setScene(new Scene(contenidorGeneral, 500, 400));
        finestraFitxers.setMaximized(true);
        finestraFitxers.show();
    }

    /**
     * @brief Obre la finestra de simulació diària.
     * @pre S'han seleccionat els fitxers necessaris
     * @post S'han creat i configurat les finestres de simulació i acumulats
     */
    private void obrirFinestraSimulacio() {
        if (escenariPrincipalSimulacio == null) {
            escenariPrincipalSimulacio = new Stage();
            configurarFinestraSimulacio();
        }
        
        if (escenariPrincipalAcumulats == null) {
            escenariPrincipalAcumulats = new Stage();
            configurarFinestraAcumulats();
        }
        
        escenariPrincipalSimulacio.show();
        escenariPrincipalAcumulats.hide();
    }

    /**
     * @brief Configura la finestra de simulació diària.
     * @pre Cert
     * @post La finestra de simulació diària està configurada amb tots els seus components
     */
    private void configurarFinestraSimulacio() {
        escenariPrincipalSimulacio.setTitle("InfekTopia - Simulació Diària");

        Button botoAcumulats = new Button("Mostrar Acumulats");
        botoAcumulats.setOnAction(e -> {
            escenariPrincipalSimulacio.hide();
            escenariPrincipalAcumulats.show();
        });

        Label titolPrincipalSimulacio = crearTitol("InfekTopia", 28);
        Label subtitolTextSimulacio = crearTitol("Dades diàries", 16);
        Label subtitolGraficSimulacio = crearTitol("Representació gràfica", 16);

        GridPane dadesTextSimulacio = crearGridPaneDades("Diaris");
        graficSimulacio = crearGraficBuit();
        GridPane representacioDadesSimulacio = crearRepresentacioDades(subtitolGraficSimulacio, graficSimulacio, subtitolTextSimulacio, dadesTextSimulacio);

        ComboBox<NivellConfinament> confinamentChoice = crearComboBoxConfinament();
        ComboBox<String> regioConfinamentChoice = crearComboBoxRegions();
        TextField fieldTaxaConfinament = crearTextFieldTaxa();

        ComboBox<DadesDiaries> dadesDiariesChoice = crearComboBoxDadesDiaries();

        HBox contenidorConfinament = crearContenidorConfinament(confinamentChoice);
        HBox contenidorConfinamentRegio = crearContenidorConfinamentRegio(regioConfinamentChoice);
        HBox contenidorConfinamentTaxa = crearContenidorConfinamentTaxa(fieldTaxaConfinament);
        HBox contenidorDadesDiaries = crearContenidorDadesDiaries(dadesDiariesChoice);

        configurarListenersConfinament(confinamentChoice, contenidorConfinamentRegio, contenidorConfinamentTaxa);
        vincularComboBoxAProperty(regioConfinamentChoice, valorRegioConfinament);
        configurarListenerTaxa(fieldTaxaConfinament);

        configurarListenerDadesDiaries(dadesDiariesChoice);
        

        ComboBox<String> simulacioRegioChoice = crearComboBoxRegions();
        ComboBox<String> simulacioVirusChoice = crearComboBoxVirus();

        vincularComboBoxAProperty(simulacioRegioChoice, valorRegioDades);
        vincularComboBoxAProperty(simulacioVirusChoice, valorVirusDades);

        VBox contenidorBotoControl = new VBox(10, botoAcumulats, crearBotoAvancarDia(), crearBotoSortir());
        contenidorBotoControl.setAlignment(Pos.BOTTOM_RIGHT);

        VBox contenidorSelecSimul = crearContenidorSeleccio(simulacioRegioChoice, simulacioVirusChoice);
        VBox contenidorFullConfinament = new VBox(10, contenidorConfinament, contenidorConfinamentRegio, contenidorConfinamentTaxa);
        VBox contenidorDesplegablesSimulacio = new VBox(30, contenidorSelecSimul, contenidorFullConfinament, crearBotoAplicarConfinament(), contenidorDadesDiaries, crearBotoConsultarDades("Diaris"));
        
        HBox contenidorSimulacio = new HBox(20, contenidorDesplegablesSimulacio, representacioDadesSimulacio);

        VBox contenidorPrincipalSimulacio = new VBox(30, titolPrincipalSimulacio, contenidorSimulacio, contenidorBotoControl);
        contenidorPrincipalSimulacio.setAlignment(Pos.CENTER);
        contenidorPrincipalSimulacio.setPadding(new Insets(10));

        escenariPrincipalSimulacio.setScene(new Scene(contenidorPrincipalSimulacio, 800, 600));
        escenariPrincipalSimulacio.setMaximized(true);
    }

    /**
     * @brief Configura la finestra de dades acumulades.
     * @pre Cert
     * @post La finestra de dades acumulades està configurada amb tots els seus components
     */
    private void configurarFinestraAcumulats() {
        escenariPrincipalAcumulats.setTitle("InfekTopia - Dades Acumulades");

        Button botoSimulacio = new Button("Mostrar Simulació Diària");
        botoSimulacio.setOnAction(e -> {
            escenariPrincipalAcumulats.hide();
            escenariPrincipalSimulacio.show();
        });

        Label titolPrincipalAcumulats = crearTitol("InfekTopia", 28);
        Label subtitolTextAcumulats = crearTitol("Dades Acumulades", 16);
        Label subtitolGraficAcumulats = crearTitol("Representació gràfica Acumulats", 16);

        GridPane dadesTextAcumulats = crearGridPaneDades("Acumulats");
        graficAcumulats = crearGraficBuit();
        GridPane representacioDadesAcumulats = crearRepresentacioDades(subtitolGraficAcumulats, graficAcumulats, subtitolTextAcumulats, dadesTextAcumulats);

        ComboBox<String> simulacioRegioChoiceAcum = crearComboBoxRegions();
        ComboBox<String> simulacioVirusChoiceAcum = crearComboBoxVirus();
        vincularComboBoxAProperty(simulacioRegioChoiceAcum, valorRegioDadesAcum);
        vincularComboBoxAProperty(simulacioVirusChoiceAcum, valorVirusDadesAcum);

        VBox contenidorBotoControl = new VBox(10, botoSimulacio, crearBotoAvancarDia(), crearBotoSortir());
        contenidorBotoControl.setAlignment(Pos.BOTTOM_RIGHT);

        VBox contenidorSelecSimul = crearContenidorSeleccio(simulacioRegioChoiceAcum, simulacioVirusChoiceAcum);
        VBox contenidorDesplegablesSimulacio = new VBox(30, contenidorSelecSimul, crearBotoConsultarDades("Acumulats"));
        HBox contenidorAcumulats = new HBox(20, contenidorDesplegablesSimulacio, representacioDadesAcumulats);

        VBox contenidorPrincipalAcumulats = new VBox(30, titolPrincipalAcumulats, contenidorAcumulats, contenidorBotoControl);
        contenidorPrincipalAcumulats.setAlignment(Pos.CENTER);
        contenidorPrincipalAcumulats.setPadding(new Insets(10));

        escenariPrincipalAcumulats.setScene(new Scene(contenidorPrincipalAcumulats, 800, 600));
        escenariPrincipalAcumulats.setMaximized(true);
    }

    /**
     * @brief Crea una etiqueta de títol amb l'estil especificat.
     * @param text Text a mostrar
     * @param midaFont Mida de la font en píxels
     * @return Label configurat amb l'estil sol·licitat
     * @pre text != null && midaFont > 0
     * @post Retorna un Label amb el text i mida de font especificats
     */
    private Label crearTitol(String text, int midaFont) {
        Label label = new Label(text);
        label.setStyle("-fx-font-size: " + midaFont + "px; -fx-font-weight: bold;");
        return label;
    }

    /**
     * @brief Inicialitza les etiquetes de confirmació de selecció de fitxers.
     * @pre Cert
     * @post Les etiquetes de confirmació estan creades i configurades com a no visibles
     */
    private void inicialitzarEtiquetesConfirmacio() {
        etiquetaConfirmacioVirus = new Label("✓");
        etiquetaConfirmacioRegio = new Label("✓");
        etiquetaConfirmacioEstat = new Label("✓");

        etiquetaConfirmacioVirus.setStyle("-fx-text-fill: #4CAF50; -fx-font-size: 16px;");
        etiquetaConfirmacioRegio.setStyle("-fx-text-fill: #4CAF50; -fx-font-size: 16px;");
        etiquetaConfirmacioEstat.setStyle("-fx-text-fill: #4CAF50; -fx-font-size: 16px;");

        etiquetaConfirmacioVirus.setVisible(false);
        etiquetaConfirmacioRegio.setVisible(false);
        etiquetaConfirmacioEstat.setVisible(false);
    }

    /**
     * @brief Crea un botó per seleccionar fitxers.
     * @param text Text del botó
     * @param stage Finestra pare on es mostrarà el diàleg de selecció
     * @param etiquetaConfirmacio Etiqueta que s'actualitzarà quan es seleccioni un fitxer
     * @return Botó configurat per obrir el diàleg de selecció de fitxers
     * @pre text != null && stage != null && etiquetaConfirmacio != null
     * @post Retorna un botó que en ser clicat obre un diàleg de selecció de fitxers
     */
    private Button crearBotoFitxer(String text, Stage stage, Label etiquetaConfirmacio) {
        Button button = new Button(text);
        FileChooser seleccionadorFitxer = new FileChooser();
        button.setOnAction(e -> {
            File f = seleccionadorFitxer.showOpenDialog(stage);
            if (f != null) {
                if (text.contains("Virus")) {
                    rutaFitxerVirus = f.getAbsolutePath();
                    System.out.println(rutaFitxerVirus);
                } else if (text.contains("Regió")) {
                    rutaFitxerRegio = f.getAbsolutePath();
                    System.out.println(rutaFitxerRegio);
                } else {
                    rutaFitxerEstat = f.getAbsolutePath();
                    System.out.println(rutaFitxerEstat);
                }
                etiquetaConfirmacio.setVisible(true);
            }
        });
        return button;
    }

    /**
     * @brief Crea un botó per aplicar confinament segons el tipus seleccionat.
     * @param simulacio Instància activa de la simulació
     * @return Botó configurat per aplicar el confinament
     * @pre Els valors de valorRegioDades, valorRegioConfinament i taxaConfinament han d'estar correctament configurats
     * @post En fer clic, s'aplica el confinament (dur o tou) a les regions corresponents
     */
    private Button crearBotoAplicarConfinament() {//Simulacio simulacio
        Button botoAplicar = new Button("Aplicar confinament");

        botoAplicar.setOnAction(e -> {
            String regioPrincipal = valorRegioDades.get();

            if(regioPrincipal != null){
                if (codiConfinament == NivellConfinament.DUR.codi()) {
                    simulacio.afegirConfinament(regioPrincipal, taxaConfinament/100);
                    System.out.println("Confinament DUR aplicat a " + regioPrincipal + " amb taxa " + taxaConfinament + "%");

                } else if (codiConfinament == NivellConfinament.TOU.codi()) {
                    String regioVeina = valorRegioConfinament.get();
                    if (regioVeina != null && !regioVeina.isEmpty()) {
                        simulacio.afegirConfinament(regioPrincipal, regioVeina);
                        System.out.println("Confinament TOU entre " + regioPrincipal + " i " + regioVeina);
                    } else {
                        System.out.println("Has de seleccionar una regió veïna per al confinament tou.");
                    }
                } else if (codiConfinament == NivellConfinament.TOU.codi()){
                    simulacio.desconfinar(regioPrincipal);
                    System.out.println("Desconfinar");
                }
            } else {System.out.println("Has de seleccionar una regió principal");}

        });

        return botoAplicar;
    }

    /**
     * @brief Crea un botó per consultar dades.
     * @return Botó configurat per mostrar les dades actualitzades.
     * @pre valorRegioDadesAcum != null i valorVirusDadesAcum != null
     * @post Dades en pantalla actualitzades
     */

    private Button crearBotoConsultarDades(String tipus) {
        Button botoDades = new Button("Consultar dades");

        botoDades.setOnAction(e -> {
            String regioPrincipal = new String();
            String virusPrincipal = new String();
            if (tipus.equals("Acumulats")){
                regioPrincipal = valorRegioDadesAcum.get();
                virusPrincipal = valorVirusDadesAcum.get();
            }else {
                regioPrincipal = valorRegioDades.get();
                virusPrincipal = valorVirusDades.get();                
            }

            if(regioPrincipal != null && virusPrincipal != null){
                List<String> dadesActualitzades = obtenirDadesActualitzades(tipus);
                
                if (tipus.equals("Acumulats")) {
                    actualitzarValorsGridPane(gridPaneAcumulats, dadesActualitzades);
                    actualitzarGraficAcumulats(regioPrincipal, virusPrincipal);
                } else {
                    actualitzarValorsGridPane(gridPaneDiari, dadesActualitzades);
                    actualitzarGraficSimulacio(regioPrincipal, virusPrincipal, codiDadaDiaria);
                }
            } else {
                System.out.println("Has de seleccionar una regió i un virus");
            }
        });
        
        return botoDades;
    }

    /**
     * @brief Crea un botó per avançar un dia en la simulació.
     * @param simulacio Objecte Simulacio sobre el qual es cridarà el mètode avancarDia()
     * @return Botó que, en ser clicat, avança un dia la simulació.
     * @pre simulacio ha d'estar inicialitzat i en un estat vàlid.
     * @post El dia actual de la simulació s'incrementa i s'actualitza l'estat de totes les regions.
     */
    private Button crearBotoAvancarDia() {//Simulacio simulacio
        Button button = new Button("Avançar dia");
        button.setOnAction(e -> {
            simulacio.avancarDia();
            System.out.println("Dia avançat.");
        });
        return button;
    }

    /**
     * @brief Crea un botó per tancar l'aplicació.
     * @param stage Escenari principal (no s'utilitza aquí, però es podria passar si cal confirmació futura).
     * @return Botó que, en ser clicat, finalitza l'aplicació.
     * @pre L'aplicació ha d'estar en execució.
     * @post L'aplicació es tanca de forma segura.
     */
    private Button crearBotoSortir() {
        Button button = new Button("Sortir");
        button.setOnAction(e -> {
            Platform.exit();
        });
        return button;
    }


    /**
     * @brief Crea un GridPane amb les dades de la simulació.
     * @param tipus Tipus de dades a mostrar ("Diaris" o "Acumulats")
     * @return GridPane configurat amb les etiquetes i valors de les dades
     * @pre tipus != null
     * @post Retorna un GridPane amb les dades organitzades per al tipus especificat
     */
    private GridPane crearGridPaneDades(String tipus) {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        List<Label> labelsValors = new ArrayList<>();

        List<String> labelsText = List.of(
            "Regió:", "Virus:", "Infectats:", 
            "Malalts:", "Defuncions:", "Contagiosos:", "Nous infectats:", "Inmunes:"
        );
        
        List<String> valorsInicials = List.of(
            "Regio", 
            "Virus", 
            "0", "0", "0", "0", "0", "0"
        );

        for (int i = 0; i < labelsText.size(); i++) {
            if (tipus.equals("Acumulats") && i == 5) continue;
            
            gridPane.add(new Label(labelsText.get(i)), 0, i);
            
            Label valor = new Label(valorsInicials.get(i));
            labelsValors.add(valor);
            
            if (i < 2) {
                valor.setStyle("-fx-font-weight: bold; -fx-padding: 6px;");
            } else {
                valor.setStyle("-fx-border-color: black; -fx-border-width: 1px; -fx-padding: 6px;");
            }
            gridPane.add(valor, 1, i);
        }
        
        if (tipus.equals("Diaris")) {
            gridPaneDiari = gridPane;
        } else {
            gridPaneAcumulats = gridPane;
        }
        return gridPane;
    }

    /**
     * @brief Actualitza els valors d'un GridPane amb nous valors.
     * @param gridPane El GridPane que es vol actualitzar
     * @param nousValors Llista de nous valors a mostrar al GridPane
     * @pre gridPane != null
     * @pre nousValors != null
     * @post Els Labels a la columna 1 del GridPane han estat actualitzats amb els nous valors
     * @post Si nousValors té menys elements que files al GridPane, només s'actualitzen les files corresponents
     */
    private void actualitzarValorsGridPane(GridPane gridPane, List<String> nousValors) {
        ObservableList<Node> nodes = gridPane.getChildren();
        
        for (int i = 0; i < nodes.size(); i++) {
            Node node = nodes.get(i);
            
            if (GridPane.getColumnIndex(node) == 1 && node instanceof Label) {
                Label label = (Label) node;
                int fila = GridPane.getRowIndex(node);
                
                if (fila < nousValors.size()) {
                    label.setText(nousValors.get(fila));
                }
            }
        }
    }

    /**
     * @brief Obté les dades actualitzades per mostrar a la interfície.
     * @param tipus Tipus de dades a obtenir ("Diari" o "Acumulats")
     * @return Llista de strings amb les dades actualitzades
     * @pre tipus != null i ha de ser "Diari" o "Acumulats"
     * @post Retorna una llista de strings amb les dades sol·licitades
     * @post La llista retornada segueix l'ordre: regió, virus, infectats, contagiosos, malalts, nous infectats, defuncions, inmunes
     */
    private List<String> obtenirDadesActualitzades(String tipus) {
        String regioPrincipal;
        String virusPrincipal;
        
        if (tipus.equals("Acumulats")) {
            regioPrincipal = valorRegioDadesAcum.get();
            virusPrincipal = valorVirusDadesAcum.get();
            
            if (regioPrincipal == null || virusPrincipal == null) {
                return Collections.emptyList();
            }
            
            List<Integer> dadesNumeriques = simulacio.obtenirAcumulatsTotals(regioPrincipal, virusPrincipal);
            return List.of(
                regioPrincipal,
                virusPrincipal,
                String.valueOf(dadesNumeriques.get(0)), // Infectats
                String.valueOf(dadesNumeriques.get(1)), // Malalts
                String.valueOf(dadesNumeriques.get(2))  // Defuncions
            );
        } else {
            regioPrincipal = valorRegioDades.get();
            virusPrincipal = valorVirusDades.get();
            
            if (regioPrincipal == null || virusPrincipal == null) {
                return Collections.emptyList();
            }
            
            return List.of(
                regioPrincipal,
                virusPrincipal,
                String.valueOf(simulacio.nombreInfectats(regioPrincipal, virusPrincipal)),
                String.valueOf(simulacio.nombreMalalts(regioPrincipal, virusPrincipal)),
                String.valueOf(simulacio.novesDefuncions(regioPrincipal, virusPrincipal)),
                String.valueOf(simulacio.nombreContagiosos(regioPrincipal, virusPrincipal)),
                String.valueOf(simulacio.nousInfectats(regioPrincipal, virusPrincipal)),
                String.valueOf(simulacio.nombreImmunes(regioPrincipal, virusPrincipal))
            );
        }
    }


    /**
     * @brief Crea un gràfic de barres buit inicial
     * @return BarChart buit
     */
    private BarChart<String, Number> crearGraficBuit() {
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Habitants");

        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle(" ");
        barChart.setPrefSize(600, 400);
        
        return barChart;
    }

    /**
     * @brief Actualitza el gràfic de barres de simulacio segons el codiDadaDiaria i el virus i regió escollida
     * @param codi Codi que determina quines dades mostrar
     * @pre graficSimulacio != null
     * @post El gràfic mostra les dades corresponents al codi en format List<Integer>
     */
    private void actualitzarGraficSimulacio(String nomRegio, String nomVirus, int codi) {
        List<Integer> dades = obtenirDadesPerGrafic(nomRegio, nomVirus, codi);
        String titol = obtenirTitolPerGrafic(codi);

        graficSimulacio.getData().clear();
        graficSimulacio.setTitle(titol);
        
        XYChart.Series<String, Number> serie = new XYChart.Series<>();
        serie.setName("Dia");
        for (int i = 0; i < dades.size(); i++) {
            serie.getData().add(new XYChart.Data<>(String.valueOf(i+1), dades.get(i)));
        }
        graficSimulacio.getData().add(serie);
    }

    /**
     * @brief Actualitza el gràfic de barres d'acumulats amb noms descriptius per a cada barra
     * @param nomRegio Nom de la regió a visualitzar
     * @param nomVirus Nom del virus a visualitzar
     * @pre nomRegio != null && !nomRegio.isEmpty()
     * @pre nomVirus != null && !nomVirus.isEmpty()
     * @post El gràfic mostra les dades acumulades amb barres etiquetades descriptivament
     */
    private void actualitzarGraficAcumulats(String nomRegio, String nomVirus) {
        List<Integer> dades = simulacio.obtenirAcumulatsTotals(nomRegio, nomVirus);
        String titol = "Dades Acumulades - " + nomRegio + " (" + nomVirus + ")";
        
        // Llista de noms per a les barres
        List<String> nomsBarres = Arrays.asList("Infectats", "Malalts", "Defuncions");
        
        graficAcumulats.getData().clear();
        graficAcumulats.setTitle(titol);        

        XYChart.Series<String, Number> serie = new XYChart.Series<>();
        serie.setName("Estadístiques");
        
        // Assegurar que tenim suficients noms per a les dades
        int numBarres = Math.min(dades.size(), nomsBarres.size());
        
        for (int i = 0; i < numBarres; i++) {
            serie.getData().add(new XYChart.Data<>(nomsBarres.get(i), dades.get(i)));
        }
        
        graficAcumulats.getData().add(serie);
    }

    /**
     * @brief Obté les dades per mostrar al gràfic segons el tipus de dada sol·licitat
     * @param nomRegio Nom de la regió de la qual es volen obtenir les dades
     * @param nomVirus Nom del virus del qual es volen obtenir les dades
     * @param codi Tipus de dada a obtenir (0: Malalts, 1: Immunes, 2: Contagiosos, 3: Defuncions)
     * @return Llista d'enters amb les dades sol·licitades
     * @pre nomRegio != null && !nomRegio.isEmpty()
     * @pre nomVirus != null && !nomVirus.isEmpty()
     * @pre codi >= 0 && codi <= 3
     * @post Retorna una llista amb les dades corresponents al tipus sol·licitat
     * @post Si el codi no és vàlid, retorna una llista amb valors zero
     */
    private List<Integer> obtenirDadesPerGrafic(String nomRegio, String nomVirus, int codi) {
        switch(codi) {
            case 0: return simulacio.evolucioMalalts(nomRegio, nomVirus);
            case 1: return simulacio.evolucioImmunes(nomRegio, nomVirus);
            case 2: return simulacio.evolucioContagiosos(nomRegio, nomVirus);
            case 3: return simulacio.evolucioMorts(nomRegio, nomVirus);
            default: return Arrays.asList(0, 0, 0, 0);
        }
    }

    /**
     * @brief Converteix un array de primitius int a una llista d'Integers
     * @param array Array de primitius int a convertir
     * @return Llista d'Integers amb els mateixos valors que l'array original
     * @pre array != null
     * @post La llista retornada conté els mateixos elements que l'array en el mateix ordre
     * @post La mida de la llista retornada és igual a la longitud de l'array
     */
    private List<Integer> convertArrayToList(int[] array) {
        List<Integer> list = new ArrayList<>();
        for (int value : array) {
            list.add(value);
        }
        return list;
    }

    /**
     * @brief Obté el títol corresponent per al gràfic segons el tipus de dada
     * @param codi Tipus de dada (0: Malalts, 1: Immunes, 2: Contagiosos, 3: Defuncions)
     * @return String amb el títol descriptiu del tipus de dada
     * @pre codi >= 0
     * @post Retorna un títol descriptiu per al tipus de dada indicat
     * @post Si el codi no és reconegut, retorna "Dades Desconegudes"
     */
    private String obtenirTitolPerGrafic(int codi) {
        switch(codi) {
            case 0: return "Dades Diàries - Malalts";
            case 1: return "Dades Diàries - Immunes";
            case 2: return "Dades Diàries - Contagiosos";
            case 3: return "Dades Diàries - Defuncions";
            default: return "Dades Desconegudes";
        }
    }

    /**
     * @brief Crea una representació de dades que combina text i gràfic.
     * @param subtitolGrafic Subtítol per al gràfic
     * @param grafic Gràfic a incloure
     * @param subtitolText Subtítol per al text
     * @param dadesText Dades en format text
     * @return GridPane que conté tots els elements organitzats
     * @pre Tots els paràmetres != null
     * @post Retorna un GridPane que combina la representació gràfica i textual de les dades
     */
    private GridPane crearRepresentacioDades(Label subtitolGrafic, BarChart<String, Number> grafic, Label subtitolText, GridPane dadesText) {
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(20);
        gridPane.setVgap(10);

        gridPane.add(subtitolGrafic, 0, 0);
        gridPane.add(grafic, 0, 1);
        gridPane.add(subtitolText, 1, 0);
        gridPane.add(dadesText, 1, 1);
        return gridPane;
    }

    /**
     * @brief Crea un ComboBox per seleccionar el nivell de confinament.
     * @return ComboBox configurat amb els nivells de confinament
     * @pre Cert
     * @post Retorna un ComboBox que permet seleccionar entre els diferents nivells de confinament
     */
    private ComboBox<NivellConfinament> crearComboBoxConfinament() {
        ComboBox<NivellConfinament> comboBox = new ComboBox<>(FXCollections.observableArrayList(NivellConfinament.values()));
        comboBox.setPromptText("Escull tipus confinament");
        comboBox.setValue(NivellConfinament.SENSE);
        return comboBox;
    }

    /**
     * @brief Crea un ComboBox per seleccionar el tipus de dada per l'histograma.
     * @return ComboBox configurat amb els tipus de dades
     * @pre Cert
     * @post Retorna un ComboBox que permet seleccionar entre els diferents tipus de dades disponibles
     */
    private ComboBox<DadesDiaries> crearComboBoxDadesDiaries() {
        ComboBox<DadesDiaries> comboBox = new ComboBox<>(FXCollections.observableArrayList(DadesDiaries.values()));
        comboBox.setPromptText("Escull tipus de dada");
        comboBox.setValue(DadesDiaries.MALALTS);
        return comboBox;
    }

    /**
     * @brief Crea un ComboBox per seleccionar una regió.
     * @return ComboBox configurat amb la llista de regions
     * @pre Cert
     * @post Retorna un ComboBox que permet seleccionar una regió de la llista disponible
     */
    private ComboBox<String> crearComboBoxRegions() {
        ObservableList<String> items = FXCollections.observableArrayList(simulacio.mostrarRegionsActuals());//simulacio.mostrarRegionsActuals()
        FXCollections.sort(items);
        ComboBox<String> comboBox = new ComboBox<>(items);
        comboBox.setPromptText("Escull regió");
        return comboBox;
    }

    /**
     * @brief Crea un ComboBox per seleccionar un virus que s'actualitza automàticament segons la regió seleccionada.
     * @return ComboBox configurat amb la llista de virus de la regió actual
     * @pre Cert
     * @post Retorna un ComboBox que mostra els virus de la regió seleccionada i s'actualitza quan canvia la regió
     */
    private ComboBox<String> crearComboBoxVirus() {
        // Creem el ComboBox inicialment buit
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.setPromptText("Escull virus");
        
        // Configurarem un listener per actualitzar els virus quan canviï la regió
        valorRegioDades.addListener((observable, oldValue, newValue) -> {
            // Netegem els items actuals
            comboBox.getItems().clear();
            
            // Si s'ha seleccionat una regió vàlida
            if (newValue != null && !newValue.isEmpty()) {
                // Obtenim els virus per a la nova regió
                List<String> virusRegio = simulacio.mostrarVirusRegio(newValue);
                
                // Afegim els nous virus al ComboBox
                comboBox.getItems().addAll(FXCollections.observableArrayList(virusRegio));
                
                // Seleccionem el primer virus per defecte si n'hi ha
                if (!virusRegio.isEmpty()) {
                    comboBox.getSelectionModel().selectFirst();
                }
            }
        });
        
        return comboBox;
    }

    /**
     * @brief Crea un camp de text per introduir la taxa de confinament.
     * @return TextField configurat per rebre la taxa
     * @pre Cert
     * @post Retorna un TextField preparat per rebre un valor numèric de taxa
     */
    private TextField crearTextFieldTaxa() {
        TextField textField = new TextField();
        textField.setPromptText("Ingressa taxa");
        return textField;
    }

    /**
     * @brief Crea un contenidor per al selector de confinament.
     * @param comboBox ComboBox del nivell de confinament
     * @return HBox que conté l'etiqueta i el ComboBox
     * @pre comboBox != null
     * @post Retorna un HBox que conté els elements per seleccionar el nivell de confinament
     */
    private HBox crearContenidorConfinament(ComboBox<NivellConfinament> comboBox) {
        Label label = new Label("Escollir confinament");
        HBox hbox = new HBox(10, label, comboBox);
        return hbox;
    }

    /**
     * @brief Crea un contenidor per al selector de tipus de dada.
     * @param comboBox ComboBox del tipus de dada
     * @return HBox que conté l'etiqueta i el ComboBox
     * @pre comboBox != null
     * @post Retorna un HBox que conté els elements per seleccionar el tipus de dada
     */
    private HBox crearContenidorDadesDiaries(ComboBox<DadesDiaries> comboBox) {
        Label label = new Label("Escollir tipus de dada");
        HBox hbox = new HBox(10, label, comboBox);
        return hbox;
    }

    /**
     * @brief Crea un contenidor per al selector de regió per confinament.
     * @param comboBox ComboBox de regions
     * @return HBox que conté l'etiqueta i el ComboBox (inicialment invisible)
     * @pre comboBox != null
     * @post Retorna un HBox que conté els elements per seleccionar una regió per confinament
     */
    private HBox crearContenidorConfinamentRegio(ComboBox<String> comboBox) {
        Label label = new Label("Escollir regió per confinament");
        HBox hbox = new HBox(10, label, comboBox);
        hbox.setVisible(false);
        return hbox;
    }

    /**
     * @brief Crea un contenidor per a la taxa de confinament.
     * @param textField Camp de text per a la taxa
     * @return HBox que conté l'etiqueta, el camp de text i el símbol % (inicialment invisible)
     * @pre textField != null
     * @post Retorna un HBox que conté els elements per introduir la taxa de confinament
     */
    private HBox crearContenidorConfinamentTaxa(TextField textField) {
        Label label = new Label("Taxa de confinament a aplicar");
        Label percent = new Label("%");
        HBox hbox = new HBox(10, label, textField, percent);
        hbox.setVisible(false);
        return hbox;
    }

    /**
     * @brief Crea un contenidor per a la selecció de regió i virus.
     * @param comboBoxRegio ComboBox de regions
     * @param comboBoxVirus ComboBox de virus
     * @return VBox que conté els selectors de regió i virus
     * @pre comboBoxRegio != null && comboBoxVirus != null
     * @post Retorna un VBox que conté els selectors de regió i virus organitzats verticalment
     */
    private VBox crearContenidorSeleccio(ComboBox<String> comboBoxRegio, ComboBox<String> comboBoxVirus) {
        Label labelRegio = new Label("Consultar dades de la regió:");
        Label labelVirus = new Label("Consultar dades del virus:");
        HBox hboxRegio = new HBox(10, labelRegio, comboBoxRegio);
        HBox hboxVirus = new HBox(10, labelVirus, comboBoxVirus);
        return new VBox(10, hboxRegio, hboxVirus);
    }

    /**
     * @brief Configura els listeners per al ComboBox de confinament.
     * @param comboBox ComboBox de nivell de confinament
     * @param contenidorRegio Contenidor del selector de regió
     * @param contenidorTaxa Contenidor del camp de taxa
     * @pre Tots els paràmetres != null
     * @post S'han configurat els listeners que mostren/amaguen els contenidors segons la selecció
     */
    private void configurarListenersConfinament(ComboBox<NivellConfinament> comboBox, HBox contenidorRegio, HBox contenidorTaxa) {
        comboBox.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldVal, newVal) -> { 
                codiConfinament = newVal.codi; 
                contenidorRegio.setVisible(codiConfinament == NivellConfinament.TOU.codi());
                contenidorTaxa.setVisible(codiConfinament == NivellConfinament.DUR.codi());
            });
    }

    /**
     * @brief Configura els listeners per al ComboBox de dadesDiaries.
     * @param comboBox ComboBox de nivell de dades diaries
     * @pre Tots els paràmetres != null
     * @post S'ha configurat el listener que modifica el valor de codiDadaDiaria
     */
    private void configurarListenerDadesDiaries(ComboBox<DadesDiaries> comboBox) {
        comboBox.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldVal, newVal) -> { 
                codiDadaDiaria = newVal.codi; 
            });
    }

    /**
     * @brief Vincula un ComboBox amb una Property per capturar el valor seleccionat
     * @param comboBox ComboBox a monitoritzar
     * @param property Property on s'emmagatzemarà el valor seleccionat
     * @pre comboBox != null && property != null
     * @post La property conté l'últim valor seleccionat al ComboBox
     */
    private void vincularComboBoxAProperty(ComboBox<String> comboBox, StringProperty property) {
        property.set(comboBox.getValue());
        comboBox.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldVal, newVal) -> property.set(newVal));
    }

    /**
     * @brief Configura el listener per al camp de taxa de confinament.
     * @param textField Camp de text per a la taxa
     * @pre textField != null
     * @post S'ha configurat el listener que valida i actualitza taxaConfinament quan canvia el text
     */
    private void configurarListenerTaxa(TextField textField) {
        textField.textProperty().addListener((obs, oldVal, newVal) -> {
            try {
                if (!newVal.isEmpty()) {
                    taxaConfinament = Float.parseFloat(newVal);
                    System.out.println("Taxa actualitzada: " + taxaConfinament);
                } else {
                    taxaConfinament = 0;
                }
            } catch (NumberFormatException e) {
                System.err.println("Error: Entrada no válida. Introdueix un número.");
                textField.setText(oldVal);
            }
        });
    }

    /**
     * @brief Mètode principal que inicia l'aplicació.
     * @param args Arguments de la línia de comandes
     * @pre Cert
     * @post S'ha iniciat l'aplicació JavaFX
     */
    public static void main(String[] args) {
        launch(args);
    }
}
