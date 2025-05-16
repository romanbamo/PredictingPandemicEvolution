import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InteraccioFx extends Application {

    // Etiquetes de confirmació (✓)
    private Label etiquetaConfirmacioVirus;
    private Label etiquetaConfirmacioRegio;
    private Label etiquetaConfirmacioEstat;

    // Fitxers seleccionats
    private String rutaFitxerVirus;
    private String rutaFitxerRegio;
    private String rutaFitxerEstat;

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

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Dia");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("N Malalts");

        BarChart<String, Number> grafic = new BarChart<>(xAxis, yAxis);
        grafic.setTitle("Grafica de Malalts");
        grafic.setPrefSize(600, 400);

        XYChart.Series<String, Number> serie1 = new XYChart.Series<>();
        serie1.setName("2023");
        serie1.getData().add(new XYChart.Data<>("1", 2560));
        serie1.getData().add(new XYChart.Data<>("2", 1980));
        serie1.getData().add(new XYChart.Data<>("3", 3240));
        
        grafic.getData().add(serie1);

        GridPane representacioDades = new GridPane();

        representacioDades.setAlignment(Pos.CENTER);
        representacioDades.setHgap(20);
        representacioDades.setVgap(10);

        representacioDades.add(subtitolGrafic, 0, 0);
        representacioDades.add(grafic, 0, 1);
        representacioDades.add(subtitolText, 1, 0);
        representacioDades.add(dadesText, 1, 1);

        VBox contenidorPrincipal = new VBox(30, titolPrincipal, representacioDades);
        contenidorPrincipal.setAlignment(Pos.CENTER);
        contenidorPrincipal.setPadding(new Insets(100));

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
