import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
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
    public void start(Stage escenariPrincipal) {
        escenariPrincipal.setTitle("InfekTopia");

        Label titolPrincipal = new Label("InfekTopia");
        titolPrincipal.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        Button botoSeleccionaFitxers = new Button("Selecciona fitxers");
        botoSeleccionaFitxers.setStyle("-fx-background-color: #1976D2; -fx-text-fill: white;");
        botoSeleccionaFitxers.setOnAction(e -> obrirFinestraSeleccioFitxers());

        VBox contenidorPrincipal = new VBox(30, titolPrincipal, botoSeleccionaFitxers);
        contenidorPrincipal.setAlignment(Pos.CENTER);
        contenidorPrincipal.setPadding(new Insets(100));

        Scene escenaPrincipal = new Scene(contenidorPrincipal, 500, 400);
        escenariPrincipal.setScene(escenaPrincipal);
        escenariPrincipal.show();
    }

    private void obrirFinestraSeleccioFitxers() {
        Stage finestra = new Stage();
        finestra.setTitle("Arxius per a la simulació");

        FileChooser seleccionadorFitxer = new FileChooser();

        // Etiquetes de títol i confirmació
        Label titolFitxers = new Label("Escull els arxius corresponents per a la simulació");
        titolFitxers.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

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
            File f = seleccionadorFitxer.showOpenDialog(finestra);
            if (f != null) {
                rutaFitxerVirus = f.getAbsolutePath();
                etiquetaConfirmacioVirus.setVisible(true);
            }
        });

        botoRegio.setOnAction(e -> {
            File f = seleccionadorFitxer.showOpenDialog(finestra);
            if (f != null) {
                rutaFitxerRegio = f.getAbsolutePath();
                etiquetaConfirmacioRegio.setVisible(true);
            }
        });

        botoEstat.setOnAction(e -> {
            File f = seleccionadorFitxer.showOpenDialog(finestra);
            if (f != null) {
                rutaFitxerEstat = f.getAbsolutePath();
                etiquetaConfirmacioEstat.setVisible(true);
            }
        });
        
        botoTancarFitxers.setOnAction(e -> finestra.close());

        // Disseny dels elements
        HBox caixaVirus = new HBox(10, botoVirus, etiquetaConfirmacioVirus);
        HBox caixaRegio = new HBox(10, botoRegio, etiquetaConfirmacioRegio);
        HBox caixaEstat = new HBox(10, botoEstat, etiquetaConfirmacioEstat);
        HBox contenidorTancar = new HBox(botoTancarFitxers);

        caixaVirus.setAlignment(Pos.CENTER_LEFT);
        caixaRegio.setAlignment(Pos.CENTER_LEFT);
        caixaEstat.setAlignment(Pos.CENTER_LEFT);
        contenidorTancar.setAlignment(Pos.BOTTOM_RIGHT);

        VBox contenidorBotons = new VBox(20, caixaVirus, caixaRegio, caixaEstat);
        BorderPane contenidorGeneral = new BorderPane();
        contenidorGeneral.setPadding(new Insets(50));
        contenidorGeneral.setPadding(new Insets(50));
        contenidorTancar.setPadding(new Insets(10));

        VBox centre = new VBox(30, titolFitxers, contenidorBotons);
        contenidorGeneral.setCenter(centre);
        contenidorGeneral.setBottom(contenidorTancar);

        Scene escenaFitxers = new Scene(contenidorGeneral, 500, 400);
        finestra.setScene(escenaFitxers);
        finestra.show();
    }

    private void estilitzarEtiquetaConfirmacio(Label etiqueta) {
        etiqueta.setStyle("-fx-text-fill: #4CAF50; -fx-font-size: 16px;");
    }


    public static void main(String[] args) {
        launch(args);
    }
}

