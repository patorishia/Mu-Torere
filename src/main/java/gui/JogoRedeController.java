/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package gui;

import group15.mu_torere.DadosGlobais;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

/**
 * Controlador do ecrã Jogo em Rede.
 * Responsável por:
 *  - ler o IP introduzido pelo jogador
 *  - iniciar ligação ao servidor (futuro)
 *  - avançar para o ecrã de espera
 */
public class JogoRedeController {

    @FXML private TextField campoIP;

    /**
     * Tenta conectar ao servidor usando o IP introduzido.
     * (Lógica de rede será implementada mais tarde)
     */
    @FXML
    private void conectar() {

        String ip = campoIP.getText().trim();

        if (ip.isEmpty()) {
            campoIP.setPromptText("Introduza um IP válido");
            return;
        }

        // Guardar IP globalmente para o ecrã de espera
        DadosGlobais.ipServidor = ip;

        // Avançar para o ecrã de espera
        ScreenManager.show("/fxml/Espera.fxml");
    }

    @FXML
    private void mostrarMenuInicial() {
        ScreenManager.show("/fxml/MenuInicial.fxml");
    }
}
