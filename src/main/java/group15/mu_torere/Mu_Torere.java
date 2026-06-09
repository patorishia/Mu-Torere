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
import javafx.stage.Stage;

/**
 * Classe principal da aplicação Mu Torere.
 * Responsável por iniciar o programa e carregar o menu inicial.
 */
public class Mu_Torere extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        // Carregar o menu inicial (primeiro ecrã da aplicação)
        Parent root = FXMLLoader.load(getClass().getResource("/gui/menu/MenuInicial.fxml"));

        Scene scene = new Scene(root);

        primaryStage.setTitle("Mu Torere");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

        // Guardar a stage no ScreenManager para permitir troca de ecrãs
        ScreenManager.setStage(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
