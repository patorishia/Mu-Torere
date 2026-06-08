/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package gui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controlador do ecrã Escolher Cor. O jogador escolhido pela roleta seleciona a
 * cor clara ou escura.
 */
public class EscolherCorController implements Initializable {

    @FXML
    private Label labelTitulo;
    @FXML
    private Label labelJogadorAtual;

    @FXML
    private Circle circuloClara;
    @FXML
    private Circle circuloEscura;

    @FXML
    private Button btnConfirmarCor;

    // Variáveis internas
    private String jogadorQueEscolhe;
    private String corJogador1;
    private String corJogador2;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // Quem escolhe a cor vem da roleta
        jogadorQueEscolhe = DadosGlobais.jogadorQueEscolheCor;

        // Atualizar texto
        labelJogadorAtual.setText(jogadorQueEscolhe + ", escolhe a cor:");
    }

    /**
     * Seleciona a cor clara.
     */
    @FXML
    private void selecionarClara(MouseEvent event) {

        if (jogadorQueEscolhe.equals(DadosGlobais.nomeJogador1)) {
            corJogador1 = "claro";
            corJogador2 = "escuro";
        } else {
            corJogador1 = "escuro";
            corJogador2 = "claro";
        }

        highlightEscolha("clara");
    }

    /**
     * Seleciona a cor escura.
     */
    @FXML
    private void selecionarEscura(MouseEvent event) {

        if (jogadorQueEscolhe.equals(DadosGlobais.nomeJogador1)) {
            corJogador1 = "escuro";
            corJogador2 = "claro";
        } else {
            corJogador1 = "claro";
            corJogador2 = "escuro";
        }

        highlightEscolha("escura");
    }

    /**
     * Aplica o highlight visual ao círculo selecionado. Remove o highlight
     * anterior e aplica ao círculo escolhido.
     */
    private void highlightEscolha(String cor) {

        // Remover highlight dos dois círculos
        circuloClara.getStyleClass().remove("escolhercor-highlight");
        circuloEscura.getStyleClass().remove("escolhercor-highlight");

        // Aplicar highlight ao círculo selecionado
        if (cor.equals("clara")) {
            circuloClara.getStyleClass().add("escolhercor-highlight");
        } else {
            circuloEscura.getStyleClass().add("escolhercor-highlight");
        }
    }

    /**
     * Guarda as cores escolhidas e avança para o tabuleiro.
     */
    @FXML
    private void confirmarCor() {

        DadosGlobais.corJogador1 = corJogador1;
        DadosGlobais.corJogador2 = corJogador2;

        ScreenManager.show("/gui/tabuleiro/Tabuleiro.fxml");
    }
}
