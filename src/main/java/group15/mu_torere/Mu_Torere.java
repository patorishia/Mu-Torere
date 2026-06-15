/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package group15.mu_torere;

import gui.ScreenManager;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

/**
 * Classe principal da aplicação Mu Torere.
 * Responsável por iniciar o programa e carregar o menu inicial.
 */
public class Mu_Torere extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        // Carregar o menu inicial (primeiro ecrã da aplicação)
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/MenuInicial.fxml"));

        Scene scene = new Scene(root);
        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.F11) {
                primaryStage.setFullScreen(!primaryStage.isFullScreen());
            }
        });

        primaryStage.setTitle("Mu Torere");
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        primaryStage.setMinWidth(700);
        primaryStage.setMinHeight(500);
        primaryStage.show();

        // Guardar a stage no ScreenManager para permitir troca de ecrãs
        ScreenManager.setStage(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
