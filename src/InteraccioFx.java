import javafx.application.Application;
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

//Dies de la gràfica depenen del dies de latencia i dels dies de comtagi' que son la durada de la infeccio de cada viruws
//Acumulats amb un flow segons virus i regio Malalts infectats i morts
//Confinament tou s'ha d'escollir una regio'
//confinament duur s'ha d'introduir taxa de contacte'
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InteraccioFx extends Application {

    //Variable de confinament
    private int codiConfinament = 0;
    private float taxaConfinament = 0;
    private String valorRegioConfinament;

    // Etiquetes de confirmació (✓)
    private Label etiquetaConfirmacioVirus;
    private Label etiquetaConfirmacioRegio;
    private Label etiquetaConfirmacioEstat;

    // Fitxers seleccionats
    private String rutaFitxerVirus;
    private String rutaFitxerRegio;
    private String rutaFitxerEstat;

    public enum NivellConfinament {
        SENSE("Sense confinament", 0),
        TOU("Tou", 1),
        DUR("Dur", 2);
        
        private final String text;
        private final int codi;
        
        NivellConfinament(String text, int codi) {
            this.text = text;
            this.codi = codi;
        }
        
        @Override
        public String toString() {
            return text;
        }
        
        public int codi() {
            return codi;
        }
    }

    @Override
    public void start(Stage finestraFitxers) {

        finestraFitxers.setTitle("Infektopia");

        FileChooser seleccionadorFitxer = new FileChooser();

        // Etiquetes de títol i confirmació
        Label titolPrincipalFitxers = new Label("Benvingut/da a la simulació InfekTopia!");
        titolPrincipalFitxers.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;");
        HBox contenidorTitolPrincipalFitxers = new HBox(titolPrincipalFitxers);
        contenidorTitolPrincipalFitxers.setAlignment(Pos.CENTER);

        Label subtitolFitxers = new Label("Selecciona els fitxers corresponents per inicialitzar la simulació:");
        subtitolFitxers.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        HBox contenidorSubtitolFitxers = new HBox(subtitolFitxers);
        contenidorSubtitolFitxers.setAlignment(Pos.CENTER);

        etiquetaConfirmacioVirus = new Label("✓");
        etiquetaConfirmacioRegio = new Label("✓");
        etiquetaConfirmacioEstat = new Label("✓");

        estilitzarEtiquetaConfirmacio(etiquetaConfirmacioVirus);
        estilitzarEtiquetaConfirmacio(etiquetaConfirmacioRegio);
        estilitzarEtiquetaConfirmacio(etiquetaConfirmacioEstat);

        etiquetaConfirmacioVirus.setVisible(false);
        etiquetaConfirmacioRegio.setVisible(false);
        etiquetaConfirmacioEstat.setVisible(false);

        // Botons per seleccionar fitxers
        Button botoVirus = new Button("Buscar arxiu Virus");
        Button botoRegio = new Button("Buscar arxiu Regió");
        Button botoEstat = new Button("Buscar arxiu Estat Inicial");
        Button botoTancarFitxers = new Button("D'acord'");

        // Accions dels botons
        botoVirus.setOnAction(e -> {
            File f = seleccionadorFitxer.showOpenDialog(finestraFitxers);
            if (f != null) {
                rutaFitxerVirus = f.getAbsolutePath();
                etiquetaConfirmacioVirus.setVisible(true);
            }
        });

        botoRegio.setOnAction(e -> {
            File f = seleccionadorFitxer.showOpenDialog(finestraFitxers);
            if (f != null) {
                rutaFitxerRegio = f.getAbsolutePath();
                etiquetaConfirmacioRegio.setVisible(true);
            }
        });

        botoEstat.setOnAction(e -> {
            File f = seleccionadorFitxer.showOpenDialog(finestraFitxers);
            if (f != null) {
                rutaFitxerEstat = f.getAbsolutePath();
                etiquetaConfirmacioEstat.setVisible(true);
            }
        });
        
        botoTancarFitxers.setOnAction(e -> {
            obrirFinestraSimulacio();
            finestraFitxers.close();
            
        });

        // Disseny dels elements
        HBox caixaVirus = new HBox(10, botoVirus, etiquetaConfirmacioVirus);
        HBox caixaRegio = new HBox(10, botoRegio, etiquetaConfirmacioRegio);
        HBox caixaEstat = new HBox(10, botoEstat, etiquetaConfirmacioEstat);
        HBox contenidorTancar = new HBox(botoTancarFitxers);

        caixaVirus.setAlignment(Pos.CENTER);
        caixaRegio.setAlignment(Pos.CENTER);
        caixaEstat.setAlignment(Pos.CENTER);
        contenidorTancar.setAlignment(Pos.BOTTOM_RIGHT);

        VBox contenidorBotons = new VBox(20, caixaVirus, caixaRegio, caixaEstat);
        BorderPane contenidorGeneral = new BorderPane();
        contenidorGeneral.setPadding(new Insets(50));
        contenidorTancar.setPadding(new Insets(10));



        VBox centre = new VBox(30, contenidorSubtitolFitxers, contenidorBotons);
        BorderPane.setMargin(centre, new Insets(100, 0, 0, 0));
        contenidorGeneral.setTop(contenidorTitolPrincipalFitxers);
        contenidorGeneral.setCenter(centre);
        contenidorGeneral.setBottom(contenidorTancar);

        Scene escenaFitxers = new Scene(contenidorGeneral, 500, 400);
        finestraFitxers.setScene(escenaFitxers);
        finestraFitxers.setMaximized(true);
        finestraFitxers.show();

    }

    private void obrirFinestraSimulacio() {
        Stage escenariPrincipal = new Stage();

        escenariPrincipal.setTitle("InfekTopia");

        Label titolPrincipal = new Label("InfekTopia");
        titolPrincipal.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;");
        HBox contenidorTitolPrincipal = new HBox(titolPrincipal);
        contenidorTitolPrincipal.setAlignment(Pos.CENTER);

        Label subtitolText = new Label("Dades diàries");
        subtitolText.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        HBox contenidorSubtitolText = new HBox(subtitolText);
        contenidorSubtitolText.setAlignment(Pos.CENTER);

        Label subtitolGrafic = new Label("Representació gràfica");
        subtitolGrafic.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        HBox contenidorSubtitolGrafic = new HBox(subtitolGrafic);
        contenidorSubtitolGrafic.setAlignment(Pos.CENTER);

        Label enRegio = new Label("Regió:");
        Label enVirus = new Label("Virus:");
        Label enInfect = new Label("Infectats:");
        Label enContag = new Label("Contagiosos:");
        Label enMalalts = new Label("Malalts:");
        Label enNousInfect = new Label("Nous infectats:");
        Label enNovesDefun = new Label("Defuncions:");
        Label enInmunes = new Label("Inmunes:");

        Label resRegio = new Label("0");
        Label resVirus = new Label("0");
        Label resInfect = new Label("0");
        Label resContag = new Label("0");
        Label resMalalts = new Label("0");
        Label resNousInfect = new Label("0");
        Label resNovesDefun = new Label("0");
        Label resInmunes = new Label("0");

        resRegio.setStyle("-fx-font-weight: bold; -fx-padding: 6px;");
        resVirus.setStyle("-fx-font-weight: bold; -fx-padding: 6px;");
        resInfect.setStyle("-fx-border-color: black; -fx-border-width: 1px; -fx-padding: 6px;");
        resContag.setStyle("-fx-border-color: black; -fx-border-width: 1px; -fx-padding: 6px;");
        resMalalts.setStyle("-fx-border-color: black; -fx-border-width: 1px; -fx-padding: 6px;");
        resNousInfect.setStyle("-fx-border-color: black; -fx-border-width: 1px; -fx-padding: 6px;");
        resNovesDefun.setStyle("-fx-border-color: black; -fx-border-width: 1px; -fx-padding: 6px;");
        resInmunes.setStyle("-fx-border-color: black; -fx-border-width: 1px; -fx-padding: 6px;");

        GridPane dadesText = new GridPane();
        dadesText.setPadding(new Insets(10));

        dadesText.add(enRegio, 0, 0);
        dadesText.add(enVirus, 0, 1);
        dadesText.add(enInfect, 0, 2);
        dadesText.add(enContag, 0, 3);
        dadesText.add(enMalalts, 0, 4);
        dadesText.add(enNousInfect, 0, 5);
        dadesText.add(enNovesDefun, 0, 6);
        dadesText.add(enInmunes, 0, 7);

        dadesText.add(resRegio, 1, 0);
        dadesText.add(resVirus, 1, 1);
        dadesText.add(resInfect, 1, 2);
        dadesText.add(resContag, 1, 3);
        dadesText.add(resMalalts, 1, 4);
        dadesText.add(resNousInfect, 1, 5);
        dadesText.add(resNovesDefun, 1, 6);
        dadesText.add(resInmunes, 1, 7);

        int[] dadesHistograma = {2560, 1980, 3240, 1500, 2800};

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Dia");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("N Malalts");

        BarChart<String, Number> grafic = new BarChart<>(xAxis, yAxis);
        grafic.setTitle("Grafica de Malalts");
        grafic.setPrefSize(600, 400);

        XYChart.Series<String, Number> serie1 = new XYChart.Series<>();
        for (int i = 0; i < dadesHistograma.length; i++) {
            serie1.getData().add(new XYChart.Data<>(String.valueOf(i+1), dadesHistograma[i]));
        }
        
        grafic.getData().add(serie1);

        GridPane representacioDades = new GridPane();

        representacioDades.setAlignment(Pos.CENTER);
        representacioDades.setHgap(20);
        representacioDades.setVgap(10);

        representacioDades.add(subtitolGrafic, 0, 0);
        representacioDades.add(grafic, 0, 1);
        representacioDades.add(subtitolText, 1, 0);
        representacioDades.add(dadesText, 1, 1);

        Label textConfinament = new Label("Escollir confinament");
        Label textConfinamentRegio = new Label("Escollir regió per confinament");
        Label textConfinamentTaxa = new Label("Taxa de confinament a aplicar");
        Label escollirVirusText = new Label("Consultar dades del virus:");
        Label escollirRegioText = new Label("Consultar dades de la regió:");
        Label tantPerCent = new Label("%");

        String[] regions = {"Regio1", "Regio2", "Regio3", "Regio4", "Regio5", "Regio6", "Regio7", "Regio8", "Regio9"};
        String[] virus = {"Virus1", "Virus2", "Virus3", "Virus4", "Virus5", "Virus6", "Virus7", "Virus8", "Virus9"};

        ObservableList<String> items = FXCollections.observableArrayList(regions);
        FXCollections.sort(items);

        ComboBox<NivellConfinament> confinamentChoice =
	    new ComboBox<>(FXCollections.observableArrayList(NivellConfinament.values()));
        confinamentChoice.setPromptText("Escull tipus confinament");

        ComboBox<String> regioConfinament = new ComboBox<>(FXCollections.observableArrayList(items));
        confinamentChoice.setPromptText("Escull regió veina");

        TextField fieldTaxaConfinament = new TextField();
        fieldTaxaConfinament.setPromptText("Ingressa taxa");

        HBox contenidorConfinament = new HBox(10, textConfinament, confinamentChoice);
        HBox contenidorConfinamentRegio = new HBox(10, textConfinamentRegio, regioConfinament);
        HBox contenidorConfinamentTaxa = new HBox(10, textConfinamentTaxa, fieldTaxaConfinament, tantPerCent);
    
        contenidorConfinamentRegio.setVisible(false);
        contenidorConfinamentTaxa.setVisible(false);

        confinamentChoice.setValue(NivellConfinament.SENSE);
        confinamentChoice.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldVal, newVal) -> { codiConfinament = newVal.codi; 
                                        contenidorConfinamentRegio.setVisible(codiConfinament == NivellConfinament.TOU.codi());
                                        contenidorConfinamentTaxa.setVisible(codiConfinament == NivellConfinament.DUR.codi());
        });

        regioConfinament.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldVal, newVal) -> { valorRegioConfinament = newVal;
                                        System.out.println("Regió seleccionada: " + valorRegioConfinament);
        });

    fieldTaxaConfinament.textProperty().addListener((obs, oldVal, newVal) -> {
        try {
            if (!newVal.isEmpty()) {
                taxaConfinament = Float.parseFloat(newVal);
                System.out.println("Taxa actualitzada: " + taxaConfinament);
            } else {
                taxaConfinament = 0;
            }
        } catch (NumberFormatException e) {
            System.err.println("Error: Entrada no válida. Introdueix un número.");
            fieldTaxaConfinament.setText(oldVal);
        }
    });

        ComboBox<String> simulacioRegioChoice = new ComboBox<>(FXCollections.observableArrayList(regions));
        simulacioRegioChoice.setPromptText("Escull regió per simulació");

        ComboBox<String> simulacioVirusChoice = new ComboBox<>(FXCollections.observableArrayList(virus));
        simulacioVirusChoice.setPromptText("Escull virus per simulació");

        Button avancarDia = new Button("Avançar un dia");

        HBox contenidorEscollirRegio = new HBox(10, escollirRegioText, simulacioRegioChoice);
        HBox contenidorEscollirVirus = new HBox(10, escollirVirusText, simulacioVirusChoice);

        VBox contenidorSelecSimul = new VBox(10, contenidorEscollirRegio, contenidorEscollirVirus);
        
        VBox contenidorFullConfinament = new VBox(10, contenidorConfinament, contenidorConfinamentRegio, contenidorConfinamentTaxa);

        VBox contenidorDesplegables = new VBox(30, contenidorSelecSimul, contenidorFullConfinament);
        
        HBox contenidorSimulacio = new HBox(20, contenidorDesplegables, representacioDades);

        VBox contenidorPrincipal = new VBox(30, titolPrincipal, contenidorSimulacio, avancarDia);
        contenidorPrincipal.setAlignment(Pos.CENTER);
        contenidorPrincipal.setPadding(new Insets(10));

        Scene escenaPrincipal = new Scene(contenidorPrincipal, 500, 400);
        escenariPrincipal.setScene(escenaPrincipal);
        escenariPrincipal.setMaximized(true);
        escenariPrincipal.show();

    }

    private void estilitzarEtiquetaConfirmacio(Label etiqueta) {
        etiqueta.setStyle("-fx-text-fill: #4CAF50; -fx-font-size: 16px;");
    }


    public static void main(String[] args) {
        launch(args);
    }
}

