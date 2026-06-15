/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package gui;

import group15.mu_torere.DadosGlobais;
import group15.mu_torere.GestorFicheiros;
import group15.mu_torere.Jogo;
import java.io.File;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.stage.FileChooser;

/**
 * Controlador do ecrã inicial (Menu Inicial).
 *
 * Este controlador gere as ações dos botões do menu: - Jogo Local - Jogo em
 * Rede - Definições - Sair
 *
 * Cada botão chama o ScreenManager para trocar de ecrã.
 */
public class MenuInicialController {

    /**
     * Abre o ecrã de inserir jogadores para o modo local.
     */
    @FXML
    private void abrirInserirJogadores(ActionEvent e) {
        DadosGlobais.limparJogoAtual();
        DadosGlobais.modoJogo = "local";
        ScreenManager.show("/fxml/InserirJogadores.fxml");
    }

    /**
     * Abre o ecrã do modo de jogo em rede.
     */
    @FXML
    private void abrirJogoRede(ActionEvent e) {
        DadosGlobais.limparJogoAtual();
        DadosGlobais.modoJogo = "rede";
        ScreenManager.show("/fxml/InserirIP.fxml");
    }

    /**
     * Carrega um jogo guardado num ficheiro.
     */
    @FXML
    private void carregarJogo(ActionEvent e) {
        FileChooser fc = new FileChooser();
        fc.setTitle("Carregar Jogo");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Ficheiros Mu Torere", "*.mt"));

        File ficheiro = fc.showOpenDialog(null);

        if (ficheiro != null) {
            DadosGlobais.limparJogoAtual();
            Jogo jogo = GestorFicheiros.carregarJogo(ficheiro);

            if (jogo != null) {
                DadosGlobais.jogoCarregado = jogo;
                ScreenManager.show("/fxml/Jogo.fxml");
            }
        }
    }

    /**
     * Abre o ecrã de definições.
     */
    @FXML
    private void abrirDefinicoes() {
        DadosGlobais.ecrãAnterior = "/fxml/MenuInicial.fxml";
        ScreenManager.show("/fxml/Parametros.fxml");
    }

    /**
     * Encerra a aplicação.
     */
    @FXML
    private void sairJogo(ActionEvent e) {
        DadosGlobais.limparJogoAtual();
        System.exit(0);
    }
}
