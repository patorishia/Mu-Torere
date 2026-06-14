/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package gui;

import group15.mu_torere.DadosGlobais;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import java.net.URL;
import java.util.ResourceBundle;


/**
 * Controlador do ecrã Inserir Jogadores.
 * Lê os nomes, valida e avança para o ecrã da roleta.
 */
public class InserirJogadoresController implements Initializable {

    @FXML private TextField txtJogador1; // Campo do Jogador 1
    @FXML private TextField txtJogador2; // Campo do Jogador 2
    private boolean aguardarNomesRede;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if ("rede".equals(DadosGlobais.modoJogo) && DadosGlobais.clienteRede != null) {
            txtJogador1.setPromptText("A aguardar nomes do servidor");
            txtJogador2.setPromptText("A aguardar nomes do servidor");
            txtJogador1.setDisable(true);
            txtJogador2.setDisable(true);
            iniciarRececaoNomesRede();
        }
    }

    /**
     * Ação do botão "Continuar".
     * Valida os nomes, guarda-os e muda para o ecrã da roleta.
     */
    @FXML
    private void abrirRoleta(ActionEvent event) {

        String nome1 = txtJogador1.getText().trim();
        String nome2 = txtJogador2.getText().trim();

        // Validação
        if (nome1.isBlank() || nome2.isBlank()) {
            Alert alerta = new Alert(Alert.AlertType.WARNING);
            alerta.setTitle("Nomes inválidos");
            alerta.setHeaderText(null);
            alerta.setContentText("Por favor, preenche os nomes dos dois jogadores.");
            alerta.showAndWait();
            return;
        }

        // Guardar nomes para o próximo ecrã
        DadosGlobais.nomeJogador1 = nome1;
        DadosGlobais.nomeJogador2 = nome2;

        if ("rede".equals(DadosGlobais.modoJogo)
                && DadosGlobais.servidorRede != null
                && DadosGlobais.servidorRede.isClienteLigado()) {
            DadosGlobais.servidorRede.enviarMensagem("NOMES_REDE|" + nome1 + "|" + nome2);
        }

        // Avançar para a roleta
        ScreenManager.show("/fxml/Roleta.fxml");
    }
    
    /**
     * Ação do botão "Voltar".
     * Regressa ao menu inicial.
     */
    @FXML
    private void voltar(ActionEvent event) {
        aguardarNomesRede = false;
        ScreenManager.show("/fxml/MenuInicial.fxml");
    }

    private void iniciarRececaoNomesRede() {
        aguardarNomesRede = true;

        Thread thread = new Thread(() -> {
            int numeroMensagens = 0;

            while (aguardarNomesRede) {
                if (DadosGlobais.clienteRede.getNumeroMensagensRecebidas() > numeroMensagens) {
                    numeroMensagens = DadosGlobais.clienteRede.getNumeroMensagensRecebidas();
                    String mensagem = DadosGlobais.clienteRede.getUltimaMensagem();

                    if (mensagem != null && mensagem.startsWith("NOMES_REDE|")) {
                        Platform.runLater(() -> aplicarNomesRede(mensagem));
                        aguardarNomesRede = false;
                    }
                }

                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    aguardarNomesRede = false;
                }
            }
        });

        thread.start();
    }

    private void aplicarNomesRede(String mensagem) {
        String[] partes = mensagem.split("\\|");

        if (partes.length != 3) {
            return;
        }

        DadosGlobais.nomeJogador1 = partes[1];
        DadosGlobais.nomeJogador2 = partes[2];
        ScreenManager.show("/fxml/Roleta.fxml");
    }
}
