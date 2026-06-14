/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package gui;

import group15.mu_torere.DadosGlobais;
import javafx.animation.RotateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controlador do ecrã da Roleta. Mostra os nomes dos jogadores, executa a
 * animação da seta e determina quem escolhe a cor.
 */
public class RoletaController implements Initializable {

    @FXML
    private Label labelJogador1Roleta;   // Nome do Jogador 1
    @FXML
    private Label labelJogador2Roleta;   // Nome do Jogador 2
    @FXML
    private Label labelResultado;        // Texto do resultado
    @FXML
    private Label seta;                  // Seta que roda
    @FXML
    private Button btnContinuarRoleta;   // Botão para avançar

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // Mostrar os nomes vindos do ecrã anterior
        labelJogador1Roleta.setText(DadosGlobais.nomeJogador1);
        labelJogador2Roleta.setText(DadosGlobais.nomeJogador2);

        // Iniciar a animação da roleta
        iniciarAnimacaoRoleta();
    }

    /**
     * Anima a seta e escolhe aleatoriamente o jogador vencedor.
     */
    private void iniciarAnimacaoRoleta() {

        btnContinuarRoleta.setDisable(true);
        labelResultado.setText("A roleta vai decidir...");

        // 1) ROTAÇÃO PRINCIPAL — 3 voltas completas
        RotateTransition rt = new RotateTransition(Duration.seconds(1.5), seta);
        rt.setByAngle(360 * 3);
        rt.setCycleCount(1);

        rt.setOnFinished(e -> {

            boolean jogador1Escolhe = Math.random() < 0.5;

            // 2) ROTAÇÃO FINAL — inclinar para o lado correto
            double anguloFinal = jogador1Escolhe ? -30 : 30;

            RotateTransition rt2 = new RotateTransition(Duration.seconds(0.4), seta);
            rt2.setToAngle(anguloFinal);
            rt2.setCycleCount(1);
            rt2.play();

            // Guardar vencedor globalmente
            DadosGlobais.jogadorQueEscolheCor = jogador1Escolhe
                    ? DadosGlobais.nomeJogador1
                    : DadosGlobais.nomeJogador2;

            labelResultado.setText(DadosGlobais.jogadorQueEscolheCor + " escolhe a cor!");
            btnContinuarRoleta.setDisable(false);
        });

        rt.play();
    }

    /**
     * Avança para o ecrã de escolher cor.
     */
    @FXML
    private void abrirEscolherCor() {
        ScreenManager.show("/fxml/EscolherCor.fxml");
    }
}
