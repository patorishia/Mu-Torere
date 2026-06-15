/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package gui;

import group15.mu_torere.DadosGlobais;
import javafx.application.Platform;
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
    private boolean aguardarRoletaRede;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // Mostrar os nomes vindos do ecrã anterior
        labelJogador1Roleta.setText(DadosGlobais.nomeJogador1);
        labelJogador2Roleta.setText(DadosGlobais.nomeJogador2);

        if ("rede".equals(DadosGlobais.modoJogo) && DadosGlobais.clienteRede != null) {
            aguardarResultadoRoletaRede();
        } else {
            iniciarAnimacaoRoleta();
        }
    }

    /**
     * Anima a seta da roleta e determina qual jogador escolhe a cor. A animação
     * tem duas fases: 1) Rotação rápida (efeito visual) 2) Rotação final para
     * apontar para o jogador escolhido
     */
    private void iniciarAnimacaoRoleta() {

        // Desativar o botão enquanto a roleta está a girar
        btnContinuarRoleta.setDisable(true);
        labelResultado.setText("A roleta vai decidir...");

        // -------------------------------
        // 1) ROTAÇÃO PRINCIPAL (efeito)
        // -------------------------------
        // A seta roda 3 voltas completas apenas para criar animação visual.
        RotateTransition rt = new RotateTransition(Duration.seconds(1.5), seta);
        rt.setByAngle(360 * 3);   // 3 voltas completas
        rt.setCycleCount(1);

        rt.setOnFinished(e -> {

            // Escolha aleatória do jogador vencedor
            boolean jogador1Escolhe = Math.random() < 0.5;

            // Jogador 1 → esquerda  (180°)
            // Jogador 2 → direita   (0°)
            double anguloFinal = jogador1Escolhe ? 135 : 45;

            RotateTransition rt2 = new RotateTransition(Duration.seconds(0.4), seta);
            rt2.setToAngle(anguloFinal);
            rt2.setCycleCount(1);
            rt2.play();

            // Guardar o nome do jogador que escolhe a cor
            DadosGlobais.jogadorQueEscolheCor = jogador1Escolhe
                    ? DadosGlobais.nomeJogador1
                    : DadosGlobais.nomeJogador2;

            // Enviar resultado ao cliente (modo rede)
            if ("rede".equals(DadosGlobais.modoJogo)
                    && DadosGlobais.servidorRede != null
                    && DadosGlobais.servidorRede.isClienteLigado()) {
                DadosGlobais.servidorRede.enviarMensagem("ROLETA_REDE|" + DadosGlobais.jogadorQueEscolheCor);
            }

            // Atualizar texto do resultado
            labelResultado.setText(DadosGlobais.jogadorQueEscolheCor + " escolhe a cor!");

            // Reativar botão
            btnContinuarRoleta.setDisable(false);

            // Se o jogador local não for o escolhido, avança automaticamente
            continuarAutomaticamenteSeNaoEscolheCor();
        });

        // Iniciar animação principal
        rt.play();
    }

    /**
     * Avança para o ecrã de escolher cor.
     */
    @FXML
    private void abrirEscolherCor() {
        aguardarRoletaRede = false;
        ScreenManager.show("/fxml/EscolherCor.fxml");
    }

    private void aguardarResultadoRoletaRede() {
        btnContinuarRoleta.setDisable(true);
        labelResultado.setText("A aguardar resultado da roleta...");
        aguardarRoletaRede = true;

        Thread thread = new Thread(() -> {
            int numeroMensagens = 0;

            while (aguardarRoletaRede) {
                if (DadosGlobais.clienteRede.getNumeroMensagensRecebidas() > numeroMensagens) {
                    numeroMensagens = DadosGlobais.clienteRede.getNumeroMensagensRecebidas();
                    String mensagem = DadosGlobais.clienteRede.getUltimaMensagem();

                    if (mensagem != null && mensagem.startsWith("ROLETA_REDE|")) {
                        Platform.runLater(() -> aplicarResultadoRoletaRede(mensagem));
                        aguardarRoletaRede = false;
                    }
                }

                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    aguardarRoletaRede = false;
                }
            }
        });

        thread.start();
    }

    private void aplicarResultadoRoletaRede(String mensagem) {
        String[] partes = mensagem.split("\\|");

        if (partes.length != 2) {
            return;
        }

        DadosGlobais.jogadorQueEscolheCor = partes[1];
        boolean jogador1Escolhe = DadosGlobais.jogadorQueEscolheCor.equals(DadosGlobais.nomeJogador1);
        seta.setRotate(jogador1Escolhe ? 135 : 45);
        labelResultado.setText(DadosGlobais.jogadorQueEscolheCor + " escolhe a cor!");
        btnContinuarRoleta.setDisable(false);
        continuarAutomaticamenteSeNaoEscolheCor();
    }

    private void continuarAutomaticamenteSeNaoEscolheCor() {
        if (!"rede".equals(DadosGlobais.modoJogo)) {
            return;
        }

        if (DadosGlobais.jogadorQueEscolheCor.equals(DadosGlobais.nomeJogadorLocal)) {
            return;
        }

        btnContinuarRoleta.setDisable(true);

        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(1800);
            } catch (InterruptedException e) {
            }

            Platform.runLater(() -> ScreenManager.show("/fxml/EscolherCor.fxml"));
        });

        thread.start();
    }
}
