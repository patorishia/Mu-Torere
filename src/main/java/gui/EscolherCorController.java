/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package gui;

import group15.mu_torere.DadosGlobais;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
    private boolean aguardarCoresRede;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // Quem escolhe a cor vem da roleta
        jogadorQueEscolhe = DadosGlobais.jogadorQueEscolheCor;

        if (jogadorQueEscolhe == null) {
            labelJogadorAtual.setText("Erro: jogador não definido");
            return;
        }

        // Atualizar texto
        labelJogadorAtual.setText(jogadorQueEscolhe + ", escolhe a cor:");

        if ("rede".equals(DadosGlobais.modoJogo) && !jogadorLocalEscolheCor()) {
            labelJogadorAtual.setText("A aguardar escolha de cor de " + jogadorQueEscolhe);
            circuloClara.setDisable(true);
            circuloEscura.setDisable(true);
            btnConfirmarCor.setDisable(true);
            iniciarRececaoCoresRede();
        }
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

        // ativar botao continuar
        btnConfirmarCor.setDisable(false);

    }

    /**
     * Guarda as cores escolhidas e avança para o tabuleiro.
     */
    @FXML
    private void confirmarCor() {

        DadosGlobais.corJogador1 = corJogador1;
        DadosGlobais.corJogador2 = corJogador2;

        if ("rede".equals(DadosGlobais.modoJogo)
                && DadosGlobais.servidorRede != null
                && DadosGlobais.servidorRede.isClienteLigado()) {
            DadosGlobais.servidorRede.enviarMensagem(criarMensagemCoresRede());
        }

        if ("rede".equals(DadosGlobais.modoJogo)
                && DadosGlobais.clienteRede != null
                && DadosGlobais.clienteRede.isLigado()) {
            DadosGlobais.clienteRede.enviarMensagem(criarMensagemCoresRede());
        }

        atualizarCorJogadorLocal();
        mostrarCorEEntrarNoJogo();
    }

    private void iniciarRececaoCoresRede() {
        aguardarCoresRede = true;

        Thread thread = new Thread(() -> {
            int numeroMensagens = 0;

            while (aguardarCoresRede) {
                int numeroAtual = obterNumeroMensagensRede();

                if (numeroAtual > numeroMensagens) {
                    numeroMensagens = numeroAtual;
                    String mensagem = obterMensagemRede();

                    if (mensagem != null && mensagem.startsWith("CORES_REDE|")) {
                        Platform.runLater(() -> aplicarCoresRede(mensagem));
                        aguardarCoresRede = false;
                    }
                }

                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    aguardarCoresRede = false;
                }
            }
        });

        thread.start();
    }

    private void aplicarCoresRede(String mensagem) {
        String[] partes = mensagem.split("\\|");

        if (partes.length != 4) {
            return;
        }

        DadosGlobais.jogadorQueEscolheCor = partes[1];
        DadosGlobais.corJogador1 = partes[2];
        DadosGlobais.corJogador2 = partes[3];
        atualizarCorJogadorLocal();
        mostrarCorEEntrarNoJogo();
    }

    private boolean jogadorLocalEscolheCor() {
        if (!"rede".equals(DadosGlobais.modoJogo)) {
            return true;
        }

        return jogadorQueEscolhe.equals(DadosGlobais.nomeJogadorLocal);
    }

    private String criarMensagemCoresRede() {
        return "CORES_REDE|"
                + DadosGlobais.jogadorQueEscolheCor + "|"
                + DadosGlobais.corJogador1 + "|"
                + DadosGlobais.corJogador2;
    }

    private void atualizarCorJogadorLocal() {
        if (DadosGlobais.nomeJogadorLocal == null) {
            return;
        }

        if (DadosGlobais.nomeJogadorLocal.equals(DadosGlobais.nomeJogador1)) {
            DadosGlobais.corJogadorLocal = DadosGlobais.corJogador1;
        } else {
            DadosGlobais.corJogadorLocal = DadosGlobais.corJogador2;
        }
    }

    private void mostrarCorEEntrarNoJogo() {
        if ("rede".equals(DadosGlobais.modoJogo)) {
            labelJogadorAtual.setText("A tua cor é: " + DadosGlobais.corJogadorLocal);

            Thread thread = new Thread(() -> {
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                }

                Platform.runLater(() -> ScreenManager.show("/fxml/Jogo.fxml"));
            });

            thread.start();
        } else {
            ScreenManager.show("/fxml/Jogo.fxml");
        }
    }

    private String obterMensagemRede() {
        if (DadosGlobais.servidorRede != null) {
            return DadosGlobais.servidorRede.getUltimaMensagem();
        }

        if (DadosGlobais.clienteRede != null) {
            return DadosGlobais.clienteRede.getUltimaMensagem();
        }

        return null;
    }

    private int obterNumeroMensagensRede() {
        if (DadosGlobais.servidorRede != null) {
            return DadosGlobais.servidorRede.getNumeroMensagensRecebidas();
        }

        if (DadosGlobais.clienteRede != null) {
            return DadosGlobais.clienteRede.getNumeroMensagensRecebidas();
        }

        return 0;
    }
}
