import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
//import javafx.aplication.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos; // Import añadido
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public final class SeleccionadorArxius extends Application {

    //private Desktop desktop = Desktop.getDesktop();
    private Label tickLabelVirus; // Mover a variables de clase
    private Label tickLabelRegio;
    private Label tickLabelEstat;

    @Override
    public void start(final Stage stage) {
        stage.setTitle("Arxius per la simulació");

        final FileChooser fileChooser = new FileChooser();

        Label windowsTitle = new Label("Escull els arxius corresponents per la simulació");
        
        // Crear ticks separados para cada botón
        tickLabelVirus = new Label("✓");
        tickLabelRegio = new Label("✓");
        tickLabelEstat = new Label("✓");

        final Button virusButton = new Button("Buscar arxiu Virus");
        final Button regioButton = new Button("Buscar arxiu Regió");
        final Button estatInicialButton = new Button("Buscar arxiu E.Inicial");

        // Estilos
        windowsTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        
        tickLabelVirus.setStyle("-fx-text-fill: #4CAF50; -fx-font-size: 16px;");
        tickLabelRegio.setStyle("-fx-text-fill: #4CAF50; -fx-font-size: 16px;");
        tickLabelEstat.setStyle("-fx-text-fill: #4CAF50; -fx-font-size: 16px;");
        
        tickLabelVirus.setVisible(false);
        tickLabelRegio.setVisible(false);
        tickLabelEstat.setVisible(false);
        
        virusButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");

        // Crear HBox separados para cada botón + tick
        HBox virusBox = new HBox(5, virusButton, tickLabelVirus);
        HBox regioBox = new HBox(5, regioButton, tickLabelRegio);
        HBox estatBox = new HBox(5, estatInicialButton, tickLabelEstat);
        
        virusBox.setAlignment(Pos.CENTER_LEFT);
        regioBox.setAlignment(Pos.CENTER_LEFT);
        estatBox.setAlignment(Pos.CENTER_LEFT);

        virusButton.setOnAction(e -> {
            File file = fileChooser.showOpenDialog(stage);
            if (file != null) {
                tickLabelVirus.setVisible(true);
                transfereixRuta(file);
            }
        });

        regioButton.setOnAction(e -> {
            File file = fileChooser.showOpenDialog(stage);
            if (file != null) {
                tickLabelRegio.setVisible(true);
                transfereixRuta(file);
            }
        });

        estatInicialButton.setOnAction(e -> {
            File file = fileChooser.showOpenDialog(stage);
            if (file != null) {
                tickLabelEstat.setVisible(true);
                transfereixRuta(file);
            }
        });

        final GridPane inputGridPane = new GridPane();
        
        // Usar los HBox en lugar de los botones directamente
        GridPane.setConstraints(virusBox, 0, 0);
        GridPane.setConstraints(regioBox, 0, 1);
        GridPane.setConstraints(estatBox, 0, 2);
        
        inputGridPane.setHgap(6);
        inputGridPane.setVgap(15);
        inputGridPane.getChildren().addAll(virusBox, regioBox, estatBox);

        final Pane rootGroup = new VBox(75);
        rootGroup.getChildren().addAll(windowsTitle, inputGridPane);
        rootGroup.setPadding(new Insets(150, 150, 150, 150));

        stage.setScene(new Scene(rootGroup));
        stage.show();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }

    private void transfereixRuta(File file) {
        try {
            Process p = new ProcessBuilder("xdg-open", file.getAbsolutePath()).start();

            // Captura errores del proceso
            new Thread(() -> {
                try (var reader = new java.io.BufferedReader(
                        new java.io.InputStreamReader(p.getErrorStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.err.println("xdg-open error: " + line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

        } catch (IOException ex) {
            Logger.getLogger(SeleccionadorArxius.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
