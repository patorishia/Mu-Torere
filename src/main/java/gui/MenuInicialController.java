/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package gui;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;

/**
 * Controlador do ecrã inicial (Menu Inicial).
 *
 * Este controlador gere as ações dos botões do menu:
 *  - Jogo Local
 *  - Jogo em Rede
 *  - Definições
 *  - Sair
 *
 * Cada botão chama o ScreenManager para trocar de ecrã.
 */
public class MenuInicialController {

    /**
     * Abre o ecrã de inserir jogadores para o modo local.
     */
    @FXML
    private void abrirInserirJogadores(ActionEvent e) {
        ScreenManager.show("/fxml/InserirJogadores.fxml");
    }

    /**
     * Abre o ecrã do modo de jogo em rede.
     */
    @FXML
    private void abrirJogoRede(ActionEvent e) {
        ScreenManager.show("/fxml/InserirIP.fxml");
    }

    /**
     * Abre o ecrã de definições.
     */
    @FXML
    private void abrirDefinicoes(ActionEvent e) {
        ScreenManager.show("/fxml/Parametros.fxml");
    }

    /**
     * Encerra a aplicação.
     */
    @FXML
    private void sairJogo(ActionEvent e) {
        System.exit(0);
    }
}
