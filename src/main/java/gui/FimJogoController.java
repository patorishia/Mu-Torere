/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package gui;

import group15.mu_torere.DadosGlobais;
import group15.mu_torere.GestorSons;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controlador do ecrã Fim de Jogo.
 * Mostra o vencedor e permite voltar ao menu inicial.
 */
public class FimJogoController implements Initializable {

    /**
     * Cria o controlador do ecrã de fim de jogo.
     */
    public FimJogoController() {
    }

    /** Label onde é apresentado o vencedor da partida. */
    @FXML private Label labelVencedor;

    /**
     * Inicializa o ecrã, apresenta o vencedor e toca o som de vitória.
     *
     * @param url localização usada para resolver caminhos relativos, fornecida pelo JavaFX
     * @param rb recursos de internacionalização, fornecidos pelo JavaFX
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // Mostrar o vencedor guardado globalmente
        if (DadosGlobais.vencedor != null && DadosGlobais.vencedor.startsWith("Parabéns!")) {
            labelVencedor.setText(DadosGlobais.vencedor);
        } else {
            labelVencedor.setText("Vencedor: " + DadosGlobais.vencedor);
        }

        GestorSons.tocarVitoria();
    }

    /**
     * Voltar ao menu inicial.
     */
    @FXML
    private void mostrarMenuInicial() {
        DadosGlobais.limparJogoAtualAssincrono();
        ScreenManager.show("/fxml/MenuInicial.fxml");
    }
}
