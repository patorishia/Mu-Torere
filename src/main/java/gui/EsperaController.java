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

    @FXML
    private Label labelIPServidor;
    @FXML
    private Label labelEstadoLigacao;
    private boolean verificarLigacao;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Mostrar o IP guardado no ecrã anterior
        labelIPServidor.setText("IP: " + DadosGlobais.ipServidor);
        iniciarVerificacaoLigacao();
    }

    @FXML
    private void cancelar() {
        verificarLigacao = false;

        if (DadosGlobais.servidorRede != null) {
            DadosGlobais.servidorRede.fechar();
            DadosGlobais.servidorRede = null;
        }

        if (DadosGlobais.clienteRede != null) {
            DadosGlobais.clienteRede.fechar();
            DadosGlobais.clienteRede = null;
        }

        ScreenManager.show("/fxml/MenuInicial.fxml");
    }

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
