/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package gui;

import group15.mu_torere.DadosGlobais;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controlador do ecrã de espera.
 * Mostra o IP do servidor e aguarda ligação do adversário.
 */
public class EsperaController implements Initializable {

    @FXML private Label labelIPServidor;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Mostrar o IP guardado no ecrã anterior
        labelIPServidor.setText("IP: " + DadosGlobais.ipServidor);
    }

    @FXML
    private void cancelar() {
        ScreenManager.show("/fxml/MenuInicial.fxml");
    }
}
