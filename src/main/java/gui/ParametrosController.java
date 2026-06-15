/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package gui;

import group15.mu_torere.DadosGlobais;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controlador do ecrã de Definições. Permite ativar/desativar som e escolher o
 * tema da aplicação.
 */
public class ParametrosController implements Initializable {

    @FXML
    private CheckBox checkSom;
    @FXML
    private ComboBox<String> temaCombo;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // Carregar valores guardados anteriormente
        checkSom.setSelected(DadosGlobais.somAtivo);
        temaCombo.setValue(DadosGlobais.temaAtual);
    }

    /**
     * Guarda as definições escolhidas pelo utilizador.
     */
    @FXML
    private void guardarParametros() {

        // Guardar valores globais
        DadosGlobais.somAtivo = checkSom.isSelected();
        DadosGlobais.temaAtual = temaCombo.getValue();

        // Voltar ao menu
        ScreenManager.show(DadosGlobais.ecrãAnterior);
    }

    /**
     * Voltar ao menu sem guardar alterações.
     */
    @FXML
    private void voltarAtras() {
        if (DadosGlobais.ecrãAnterior != null) {
            ScreenManager.show(DadosGlobais.ecrãAnterior);
        } else {
            // fallback de segurança
            ScreenManager.show("/fxml/MenuInicial.fxml");
        }
    }

}
