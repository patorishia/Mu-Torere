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

    /**
     * Cria o controlador do ecrã de escolha de cor.
     */
    public EscolherCorController() {
    }

    /** Título do ecrã de escolha de cor. */
    @FXML
    private Label labelTitulo;

    /** Label que indica qual jogador deve escolher a cor. */
    @FXML
    private Label labelJogadorAtual;

    /** Círculo clicável que representa a cor clara. */
    @FXML
    private Circle circuloClara;

    /** Círculo clicável que representa a cor escura. */
    @FXML
    private Circle circuloEscura;

    /** Botão usado para confirmar a cor escolhida. */
    @FXML
    private Button btnConfirmarCor;

    /** Nome do jogador escolhido pela roleta para selecionar a cor. */
    private String jogadorQueEscolhe;

    /** Cor atribuída ao primeiro jogador. */
    private String corJogador1;

    /** Cor atribuída ao segundo jogador. */
    private String corJogador2;

    /** Indica se este ecrã está à espera de uma mensagem de cores pela rede. */
    private boolean aguardarCoresRede;

    /**
     * Inicializa o ecrã e prepara o modo de escolha ou espera em rede.
     *
     * @param url localização usada para resolver caminhos relativos, fornecida pelo JavaFX
     * @param rb recursos de internacionalização, fornecidos pelo JavaFX
     */
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
            mostrarEsperaEscolhaCor();
            iniciarRececaoCoresRede();
        }
    }

    /**
     * Seleciona a cor clara.
     *
     * @param event evento de clique no círculo da cor clara
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
     *
     * @param event evento de clique no círculo da cor escura
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
     *
     * @param cor cor selecionada, "clara" ou "escura"
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
        btnConfirmarCor.setDisable(true);

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

    /**
     * Inicia uma thread que aguarda a mensagem com as cores escolhidas pelo adversário.
     */
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

    /**
     * Aplica as cores recebidas por rede e avança para o jogo.
     *
     * @param mensagem mensagem no formato "CORES_REDE|jogador|cor1|cor2"
     */
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

    /**
     * Indica se o jogador deste computador é quem deve escolher a cor.
     *
     * @return true se o jogador local puder escolher a cor
     */
    private boolean jogadorLocalEscolheCor() {
        if (!"rede".equals(DadosGlobais.modoJogo)) {
            return true;
        }

        return jogadorQueEscolhe.equals(DadosGlobais.nomeJogadorLocal);
    }

    /**
     * Cria a mensagem de rede com as cores atribuídas aos jogadores.
     *
     * @return mensagem de cores no formato usado pelo protocolo da aplicação
     */
    private String criarMensagemCoresRede() {
        return "CORES_REDE|"
                + DadosGlobais.jogadorQueEscolheCor + "|"
                + DadosGlobais.corJogador1 + "|"
                + DadosGlobais.corJogador2;
    }

    /**
     * Atualiza a cor do jogador local com base no nome guardado globalmente.
     */
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

    /**
     * Mostra a cor atribuída em modo de rede ou entra diretamente no jogo local.
     */
    private void mostrarCorEEntrarNoJogo() {
        if ("rede".equals(DadosGlobais.modoJogo)) {
            esconderControlosEscolhaCor();
            labelTitulo.setText("Cor atribuída");
            labelJogadorAtual.setText("A tua cor é: " + DadosGlobais.corJogadorLocal);

            Thread thread = new Thread(() -> {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                }

                Platform.runLater(() -> ScreenManager.show("/fxml/Jogo.fxml"));
            });

            thread.start();
        } else {
            ScreenManager.show("/fxml/Jogo.fxml");
        }
    }

    /**
     * Mostra o estado de espera quando a cor será escolhida pelo adversário.
     */
    private void mostrarEsperaEscolhaCor() {
        labelTitulo.setText("A aguardar");
        labelJogadorAtual.setText("À espera do adversário escolher a cor...");
        esconderControlosEscolhaCor();
    }

    /**
     * Esconde os controlos de escolha de cor quando o jogador local não deve interagir.
     */
    private void esconderControlosEscolhaCor() {
        circuloClara.setVisible(false);
        circuloClara.setManaged(false);
        circuloEscura.setVisible(false);
        circuloEscura.setManaged(false);
        btnConfirmarCor.setVisible(false);
        btnConfirmarCor.setManaged(false);
        btnConfirmarCor.setDisable(true);
    }

    /**
     * Obtém a última mensagem recebida pelo servidor ou cliente ativo.
     *
     * @return última mensagem de rede, ou null se não existir ligação ativa
     */
    private String obterMensagemRede() {
        if (DadosGlobais.servidorRede != null) {
            return DadosGlobais.servidorRede.getUltimaMensagem();
        }

        if (DadosGlobais.clienteRede != null) {
            return DadosGlobais.clienteRede.getUltimaMensagem();
        }

        return null;
    }

    /**
     * Obtém o número de mensagens recebidas pelo servidor ou cliente ativo.
     *
     * @return quantidade de mensagens recebidas pela ligação ativa
     */
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
