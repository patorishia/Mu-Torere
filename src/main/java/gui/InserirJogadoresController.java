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
 * Controlador do ecrã Inserir Jogadores. Lê os nomes, valida e avança para o
 * ecrã da roleta.
 */
public class InserirJogadoresController implements Initializable {

    /**
     * Cria o controlador do ecrã de inserção de jogadores.
     */
    public InserirJogadoresController() {
    }

    /** Campo de texto para o nome do primeiro jogador. */
    @FXML
    private TextField txtJogador1;

    /** Campo de texto para o nome do segundo jogador. */
    @FXML
    private TextField txtJogador2;

    /** Indica se o controller está à espera de mensagens de nomes pela rede. */
    private boolean aguardarNomesRede;

    /**
     * Inicializa o ecrã e adapta os campos quando o jogo está em modo de rede.
     *
     * @param url localização usada para resolver caminhos relativos, fornecida pelo JavaFX
     * @param rb recursos de internacionalização, fornecidos pelo JavaFX
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (!"rede".equals(DadosGlobais.modoJogo)) {
            return;
        }

        if (DadosGlobais.servidorRede != null) {
            txtJogador1.setPromptText("Nome do servidor");
            txtJogador2.setPromptText("A aguardar nome do cliente");
            txtJogador2.setDisable(true);
            iniciarRececaoNomesRede();
        } else if (DadosGlobais.clienteRede != null) {
            txtJogador1.setPromptText("Nome do servidor");
            txtJogador2.setPromptText("O teu nome");
            txtJogador1.setDisable(true);
        }
    }

    /**
     * Ação do botão "Continuar". Valida os nomes, guarda-os e muda para o ecrã
     * da roleta.
     *
     * @param event evento gerado pelo botão
     */
    @FXML
    private void abrirRoleta(ActionEvent event) {

        if ("rede".equals(DadosGlobais.modoJogo)) {
            abrirRoletaRede();
            return;
        }

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

        // Avançar para a roleta
        ScreenManager.show("/fxml/Roleta.fxml");
    }

    /**
     * Ação do botão "Voltar". Regressa ao menu inicial.
     *
     * @param event evento gerado pelo botão
     */
    @FXML
    private void voltar(ActionEvent event) {
        aguardarNomesRede = false;
        DadosGlobais.limparJogoAtual();
        ScreenManager.show("/fxml/MenuInicial.fxml");
    }

    /**
     * Inicia uma thread que aguarda mensagens de nomes vindas da rede.
     */
    private void iniciarRececaoNomesRede() {
        aguardarNomesRede = true;

        Thread thread = new Thread(() -> {
            int numeroMensagens = 0;

            while (aguardarNomesRede) {
                int numeroAtual = obterNumeroMensagensRede();

                if (numeroAtual > numeroMensagens) {
                    numeroMensagens = numeroAtual;
                    String mensagem = obterMensagemRede();

                    if (mensagem != null && mensagem.startsWith("NOMES_REDE|")) {
                        Platform.runLater(() -> aplicarNomesRede(mensagem));
                        aguardarNomesRede = false;
                    } else if (mensagem != null && mensagem.startsWith("NOME_CLIENTE|")) {
                        Platform.runLater(() -> aplicarNomeClienteRede(mensagem));
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

    /**
     * Encaminha o fluxo da roleta consoante este computador seja servidor ou cliente.
     */
    private void abrirRoletaRede() {
        if (DadosGlobais.servidorRede != null) {
            abrirRoletaServidor();
        } else if (DadosGlobais.clienteRede != null) {
            enviarNomeCliente();
        }
    }

    /**
     * Valida os nomes no servidor, envia-os ao cliente e avança para a roleta.
     */
    private void abrirRoletaServidor() {
        String nome1 = txtJogador1.getText().trim();
        String nome2 = txtJogador2.getText().trim();

        if (nome1.isBlank() || nome2.isBlank()) {
            mostrarAlerta("Nomes inválidos", "Preenche o teu nome e aguarda pelo nome do cliente.");
            return;
        }

        DadosGlobais.nomeJogador1 = nome1;
        DadosGlobais.nomeJogador2 = nome2;
        DadosGlobais.nomeJogadorLocal = nome1;
        DadosGlobais.servidorRede.enviarMensagem("NOMES_REDE|" + nome1 + "|" + nome2);
        aguardarNomesRede = false;
        ScreenManager.show("/fxml/Roleta.fxml");
    }

    /**
     * Envia ao servidor o nome introduzido pelo jogador cliente.
     */
    private void enviarNomeCliente() {
        String nome2 = txtJogador2.getText().trim();

        if (nome2.isBlank()) {
            mostrarAlerta("Nome inválido", "Preenche o teu nome.");
            return;
        }

        DadosGlobais.nomeJogador2 = nome2;
        DadosGlobais.nomeJogadorLocal = nome2;
        DadosGlobais.clienteRede.enviarMensagem("NOME_CLIENTE|" + nome2);
        txtJogador2.setDisable(true);
        txtJogador2.setPromptText("A aguardar o adversário...");

        iniciarRececaoNomesRede();
    }

    /**
     * Aplica os nomes recebidos numa mensagem de rede e avança para a roleta.
     *
     * @param mensagem mensagem no formato "NOMES_REDE|nome1|nome2"
     */
    private void aplicarNomesRede(String mensagem) {
        String[] partes = mensagem.split("\\|");

        if (partes.length != 3) {
            return;
        }

        DadosGlobais.nomeJogador1 = partes[1];
        DadosGlobais.nomeJogador2 = partes[2];

        if (DadosGlobais.clienteRede != null) {
            DadosGlobais.nomeJogadorLocal = DadosGlobais.nomeJogador2;
        }

        ScreenManager.show("/fxml/Roleta.fxml");
    }

    /**
     * Atualiza o campo do segundo jogador com o nome enviado pelo cliente.
     *
     * @param mensagem mensagem no formato "NOME_CLIENTE|nome"
     */
    private void aplicarNomeClienteRede(String mensagem) {
        String[] partes = mensagem.split("\\|");

        if (partes.length != 2) {
            return;
        }

        txtJogador2.setText(partes[1]);
        DadosGlobais.nomeJogador2 = partes[1];
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
     * @return quantidade de mensagens de rede recebidas
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

    /**
     * Mostra um alerta de validação ao utilizador.
     *
     * @param titulo título da janela de alerta
     * @param texto mensagem apresentada no alerta
     */
    private void mostrarAlerta(String titulo, String texto) {
        Alert alerta = new Alert(Alert.AlertType.WARNING);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(texto);
        alerta.showAndWait();
    }
}
