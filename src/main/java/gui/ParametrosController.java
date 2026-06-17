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
 * Controlador do ecrã de Definições.
 * Permite ativar/desativar som e escolher o tema da aplicação.
 */
public class ParametrosController implements Initializable {

    /**
     * Cria o controlador do ecrã de definições.
     */
    public ParametrosController() {
    }

    /** CheckBox que indica se o som da aplicação está ativo. */
    @FXML private CheckBox checkSom;

    /** ComboBox usada para escolher o tema visual da aplicação. */
    @FXML private ComboBox<String> temaCombo;

    /**
     * Inicializa o ecrã com as definições guardadas nos dados globais.
     *
     * @param url localização usada para resolver caminhos relativos, fornecida pelo JavaFX
     * @param rb recursos de internacionalização, fornecidos pelo JavaFX
     */
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
        ScreenManager.show("/fxml/MenuInicial.fxml");
    }

    /**
     * Voltar ao menu sem guardar alterações.
     */
    @FXML
    private void voltarAtras() {
        ScreenManager.show("/fxml/MenuInicial.fxml");
    }
}
