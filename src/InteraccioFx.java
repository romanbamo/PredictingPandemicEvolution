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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;

/**
 * @class InteraccioFxAjuda
 * @brief Classe principal que hereta de Application i conté tota la lògica de la interfície.
 *
 * Gestiona dues finestres principals: una per a la simulació diària i una altra per als acumulats.
 * Permet seleccionar fitxers, configurar confinaments i visualitzar dades epidemiològiques.
 */
public class InteraccioFx extends Application {

    private Stage escenariPrincipalSimulacio;          ///< Finestra principal de simulació diària
    private Stage escenariPrincipalAcumulats;          ///< Finestra principal de dades acumulades
    private int codiConfinament = 0;                   ///< Codi del nivell de confinament actual
    private float taxaConfinament = 0;                 ///< Taxa de confinament aplicada (0-100)
    //private String valorRegioConfinament;              ///< Regió seleccionada per al confinament tou
    //private String valorRegioDades;                    ///< Regió seleccionada per l'estudi de les dades
    //private String valorVirusDades;                    ///< Virus seleccionat per l'estudi de les dades
    private Label etiquetaConfirmacioVirus;            ///< Etiqueta que confirma selecció de fitxer de virus
    private Label etiquetaConfirmacioRegio;            ///< Etiqueta que confirma selecció de fitxer de regió
    private Label etiquetaConfirmacioEstat;            ///< Etiqueta que confirma selecció de fitxer d'estat
    private String rutaFitxerVirus;                    ///< Ruta del fitxer de virus seleccionat
    private String rutaFitxerRegio;                    ///< Ruta del fitxer de regió seleccionat
    private String rutaFitxerEstat;                    ///< Ruta del fitxer d'estat inicial seleccionat
    private final String[] regions = {"Regio1", "Regio2", "Regio3", "Regio4", "Regio5", "Regio6", "Regio7", "Regio8", "Regio9"}; ///< Llista de regions disponibles
    private final String[] virus = {"Virus1", "Virus2", "Virus3", "Virus4", "Virus5", "Virus6", "Virus7", "Virus8", "Virus9"};   ///< Llista de virus disponibles
    private final int[] dadesHistograma = {2560, 1980, 3240, 1500, 2800}; ///< Dades de mostra per a l'histograma
    private final StringProperty valorRegioDades = new SimpleStringProperty(); ///< Regió seleccionada per l'estudi de les dades
    private final StringProperty valorVirusDades = new SimpleStringProperty(); ///< Virus seleccionat per l'estudi de les dades
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
        BarChart<String, Number> graficSimulacio = crearGraficBarres("Grafica de Malalts", dadesHistograma);
        GridPane representacioDadesSimulacio = crearRepresentacioDades(subtitolGraficSimulacio, graficSimulacio, subtitolTextSimulacio, dadesTextSimulacio);

        ComboBox<NivellConfinament> confinamentChoice = crearComboBoxConfinament();
        ComboBox<String> regioConfinamentChoice = crearComboBoxRegions();
        TextField fieldTaxaConfinament = crearTextFieldTaxa();

        HBox contenidorConfinament = crearContenidorConfinament(confinamentChoice);
        HBox contenidorConfinamentRegio = crearContenidorConfinamentRegio(regioConfinamentChoice);
        HBox contenidorConfinamentTaxa = crearContenidorConfinamentTaxa(fieldTaxaConfinament);
        
        configurarListenersConfinament(confinamentChoice, contenidorConfinamentRegio, contenidorConfinamentTaxa);
        vincularComboBoxAProperty(regioConfinamentChoice, valorRegioConfinament);
        configurarListenerTaxa(fieldTaxaConfinament);

        ComboBox<String> simulacioRegioChoice = crearComboBoxRegions();
        ComboBox<String> simulacioVirusChoice = crearComboBoxVirus();

        vincularComboBoxAProperty(simulacioRegioChoice, valorRegioDades);
        vincularComboBoxAProperty(simulacioVirusChoice, valorVirusDades);

        Button avancarDia = new Button("Avançar un dia");

        VBox contenidorBotoControl = new VBox(avancarDia, botoAcumulats);
        contenidorBotoControl.setAlignment(Pos.BOTTOM_RIGHT);

        VBox contenidorSelecSimul = crearContenidorSeleccio(simulacioRegioChoice, simulacioVirusChoice);
        VBox contenidorFullConfinament = new VBox(10, contenidorConfinament, contenidorConfinamentRegio, contenidorConfinamentTaxa);
        VBox contenidorDesplegablesSimulacio = new VBox(30, contenidorSelecSimul, contenidorFullConfinament);
        
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
        BarChart<String, Number> graficAcumulats = crearGraficBarres("Grafica de Malalts", dadesHistograma);
        GridPane representacioDadesAcumulats = crearRepresentacioDades(subtitolGraficAcumulats, graficAcumulats, subtitolTextAcumulats, dadesTextAcumulats);

        ComboBox<String> simulacioRegioChoice = crearComboBoxRegions();
        ComboBox<String> simulacioVirusChoice = crearComboBoxVirus();
        Button avancarDia = new Button("Avançar un dia");

        VBox contenidorBotoControl = new VBox(avancarDia, botoSimulacio);
        contenidorBotoControl.setAlignment(Pos.BOTTOM_RIGHT);

        VBox contenidorSelecSimul = crearContenidorSeleccio(simulacioRegioChoice, simulacioVirusChoice);
        HBox contenidorAcumulats = new HBox(20, contenidorSelecSimul, representacioDadesAcumulats);

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
                } else if (text.contains("Regió")) {
                    rutaFitxerRegio = f.getAbsolutePath();
                } else {
                    rutaFitxerEstat = f.getAbsolutePath();
                }
                etiquetaConfirmacio.setVisible(true);
            }
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

        String[] labels = {"Regió:", "Virus:", "Infectats:", "Contagiosos:", "Malalts:", "Nous infectats:", "Defuncions:", "Inmunes:"};
        String[] valors = {"0", "0", "0", "0", "0", "0", "0", "0"};

        for (int i = 0; i < labels.length; i++) {
            if (tipus.equals("Acumulats") && i == 5) continue;
            gridPane.add(new Label(labels[i]), 0, i);
            
            Label valor = new Label(valors[i]);
            if (i < 2) {
                valor.setStyle("-fx-font-weight: bold; -fx-padding: 6px;");
            } else {
                valor.setStyle("-fx-border-color: black; -fx-border-width: 1px; -fx-padding: 6px;");
            }
            gridPane.add(valor, 1, i);
        }
        return gridPane;
    }

    /**
     * @brief Crea un gràfic de barres amb les dades especificades.
     * @param titol Títol del gràfic
     * @param dades Array amb les dades a representar
     * @return BarChart configurat amb les dades
     * @pre titol != null && dades != null && dades.length > 0
     * @post Retorna un BarChart que representa les dades especificades
     */
    private BarChart<String, Number> crearGraficBarres(String titol, int[] dades) {
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Dia");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("N Malalts");

        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle(titol);
        barChart.setPrefSize(600, 400);

        XYChart.Series<String, Number> serie = new XYChart.Series<>();
        for (int i = 0; i < dades.length; i++) {
            serie.getData().add(new XYChart.Data<>(String.valueOf(i+1), dades[i]));
        }
        barChart.getData().add(serie);
        return barChart;
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
     * @brief Crea un ComboBox per seleccionar una regió.
     * @return ComboBox configurat amb la llista de regions
     * @pre Cert
     * @post Retorna un ComboBox que permet seleccionar una regió de la llista disponible
     */
    private ComboBox<String> crearComboBoxRegions() {
        ObservableList<String> items = FXCollections.observableArrayList(regions);
        FXCollections.sort(items);
        ComboBox<String> comboBox = new ComboBox<>(items);
        comboBox.setPromptText("Escull regió");
        return comboBox;
    }

    /**
     * @brief Crea un ComboBox per seleccionar un virus.
     * @return ComboBox configurat amb la llista de virus
     * @pre Cert
     * @post Retorna un ComboBox que permet seleccionar un virus de la llista disponible
     */
    private ComboBox<String> crearComboBoxVirus() {
        ComboBox<String> comboBox = new ComboBox<>(FXCollections.observableArrayList(virus));
        comboBox.setPromptText("Escull virus");
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
