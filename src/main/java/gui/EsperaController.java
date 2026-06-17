/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package gui;

import group15.mu_torere.DadosGlobais;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controlador do ecrã de espera. Mostra o IP do servidor e aguarda ligação do
 * adversário.
 */
public class EsperaController implements Initializable {

    /**
     * Cria o controlador do ecrã de espera por ligação.
     */
    public EsperaController() {
    }

    /** Label onde é mostrado o IP do servidor criado ou usado. */
    @FXML
    private Label labelIPServidor;

    /** Label que mostra o estado atual da ligação em rede. */
    @FXML
    private Label labelEstadoLigacao;

    /** Indica se a thread de espera deve continuar a verificar a ligação. */
    private boolean verificarLigacao;

    /**
     * Inicializa o ecrã de espera e começa a verificar a ligação de rede.
     *
     * @param url localização usada para resolver caminhos relativos, fornecida pelo JavaFX
     * @param rb recursos de internacionalização, fornecidos pelo JavaFX
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Mostrar o IP guardado no ecrã anterior
        labelIPServidor.setText("IP: " + DadosGlobais.ipServidor);
        iniciarVerificacaoLigacao();
    }

    /**
     * Cancela a espera por ligação e volta ao menu inicial.
     */
    @FXML
    private void cancelar() {
        verificarLigacao = false;
        DadosGlobais.limparJogoAtual();
        ScreenManager.show("/fxml/MenuInicial.fxml");
    }

    /**
     * Inicia uma thread que verifica quando o servidor ou cliente fica ligado.
     */
    private void iniciarVerificacaoLigacao() {
        verificarLigacao = true;

        Thread thread = new Thread(() -> {
            while (verificarLigacao) {
                if (DadosGlobais.servidorRede != null && DadosGlobais.servidorRede.isClienteLigado()) {
                    Platform.runLater(() -> {
                        labelEstadoLigacao.setText("Estado: ligação aceite");

                        new Thread(() -> {
                            try {
                                Thread.sleep(200);
                            } catch (Exception e) {
                            }
                            Platform.runLater(() -> ScreenManager.show("/fxml/InserirJogadores.fxml"));
                        }).start();
                    });
                    verificarLigacao = false;
                } else if (DadosGlobais.clienteRede != null && DadosGlobais.clienteRede.isLigado()) {
                    Platform.runLater(() -> {
                        labelEstadoLigacao.setText("Estado: ligado ao servidor");

                        new Thread(() -> {
                            try {
                                Thread.sleep(200);
                            } catch (Exception e) {
                            }
                            Platform.runLater(() -> ScreenManager.show("/fxml/InserirJogadores.fxml"));
                        }).start();
                    });
                    verificarLigacao = false;
                }

            }
        });

        thread.start();
    }
}
