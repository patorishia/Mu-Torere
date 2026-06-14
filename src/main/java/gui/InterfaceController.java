package gui;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */

import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.RotateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author patri
 */



public class InterfaceController implements Initializable {

    /* =========================
           VARIÁVEIS GLOBAIS
       ========================= */

    private static int jogadorQueEscolheCor = -1;

    public static void definirJogadorQueEscolheCor(int jogador) {
        jogadorQueEscolheCor = jogador;
    }

    /* =========================
               ECRÃS
       ========================= */

    @FXML
    private BorderPane menuInicial;
    @FXML
    private BorderPane inserirJogadores;
    @FXML
    private BorderPane jogoLocal;
    @FXML
    private BorderPane jogoRede;
    @FXML
    private BorderPane espera;
    @FXML
    private BorderPane fimJogo;
    @FXML
    private BorderPane parametros;
    @FXML
    private BorderPane roleta;
    @FXML
    private BorderPane escolherCor;   // <-- faltava este
    @FXML
    private Button btnContinuarRoleta;
    @FXML
    private Button btnConfirmarCor;

    /* =========================
               PARÂMETROS
       ========================= */

    @FXML
    private ComboBox<String> temaCombo;

    /* =========================
               TABULEIRO
       ========================= */

    @FXML
    private Pane tabuleiro;
    @FXML
    private Circle pecaClara1;
    @FXML
    private Circle pecaClara2;
    @FXML private Circle pecaClara3, pecaClara4;
    @FXML
    private Circle pecaEscura1;
    @FXML
    private Circle pecaEscura2;
    @FXML private Circle pecaEscura3, pecaEscura4;

    private Circle pecaSelecionada = null;

    /* =========================
               ROLETA
       ========================= */

    private Circle roletaFundo;
    @FXML
    private javafx.scene.control.Label labelResultado;

    /* =========================
           ESCOLHER COR
       ========================= */

    @FXML
    private Circle circuloClara;
    @FXML
    private Circle circuloEscura;

    private String corEscolhida = null;
    @FXML
    private Button btnJogoLocal;
    @FXML
    private Button btnJogoRede;
    @FXML
    private Button btnDefinicoes;
    @FXML
    private Button btnSair;
    @FXML
    private Button btnContinuarInserir;
    @FXML
    private Circle casaCentro;
    @FXML
    private Circle casa0;
    @FXML
    private Circle casa1;
    @FXML
    private Circle casa2;
    @FXML
    private Circle casa3;
    @FXML
    private Circle casa4;
    @FXML
    private Circle casa5;
    @FXML
    private Circle casa6;
    @FXML
    private Circle casa7;
    @FXML
    private Button btnDefinicoesJogoLocal;
    @FXML
    private Button btnVoltarMenuJogoLocal;
    @FXML
    private Button btnConectarRede;
    @FXML
    private Button btnVoltarRede;
    @FXML
    private Label labelIPServidor;
    @FXML
    private Button btnCancelarEspera;
    @FXML
    private Button btnVoltarMenuFim;
    @FXML
    private Button btnGuardarParametros;
    @FXML
    private Button btnVoltarParametros;
    @FXML
    private Label seta;

    /* =========================
               INICIALIZAÇÃO
       ========================= */

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        mostrarMenuInicial();

        if (temaCombo != null) {
            temaCombo.getItems().setAll("Claro", "Escuro");
        }

        if (tabuleiro != null) {
            setupPeca(pecaClara1);
            setupPeca(pecaClara2);
            setupPeca(pecaClara3);
            setupPeca(pecaClara4);

            setupPeca(pecaEscura1);
            setupPeca(pecaEscura2);
            setupPeca(pecaEscura3);
            setupPeca(pecaEscura4);
        }
    }

    /* =========================
           SELEÇÃO DE COR
       ========================= */

    @FXML
    private void selecionarClara() {
        corEscolhida = "clara";
        highlightEscolha();
    }

    @FXML
    private void selecionarEscura() {
        corEscolhida = "escura";
        highlightEscolha();
    }

    private void highlightEscolha() {

        // limpar contornos
        circuloClara.setStroke(Color.BLACK);
        circuloClara.setStrokeWidth(2);

        circuloEscura.setStroke(Color.BLACK);
        circuloEscura.setStrokeWidth(2);

        // aplicar highlight
        if ("clara".equals(corEscolhida)) {
            circuloClara.setStroke(Color.DODGERBLUE);
            circuloClara.setStrokeWidth(5);
        } else if ("escura".equals(corEscolhida)) {
            circuloEscura.setStroke(Color.DODGERBLUE);
            circuloEscura.setStrokeWidth(5);
        }

        btnConfirmarCor.setDisable(false);
    }

    private void confirmarCor() {
        System.out.println("Cor escolhida: " + corEscolhida);

        // Aqui avanças para o jogo
        abrirJogoLocal();
    }

    /* =========================
           NAVEGAÇÃO ENTRE ECRÃS
       ========================= */

    private void esconderTodos() {
        menuInicial.setVisible(false);
        inserirJogadores.setVisible(false);
        jogoLocal.setVisible(false);
        jogoRede.setVisible(false);
        espera.setVisible(false);
        fimJogo.setVisible(false);
        parametros.setVisible(false);
        roleta.setVisible(false);
        if (escolherCor != null) escolherCor.setVisible(false);
    }

    @FXML
    private void mostrarMenuInicial() {
        esconderTodos();
        menuInicial.setVisible(true);
    }

    @FXML
    private void abrirInserirJogadores() {
        esconderTodos();
        inserirJogadores.setVisible(true);
    }

    @FXML
    private void abrirRoleta() {
        esconderTodos();
        roleta.setVisible(true);
        iniciarRoleta();
    }

    @FXML
    private void abrirJogoLocal() {
        esconderTodos();
        jogoLocal.setVisible(true);
    }

    @FXML
    private void abrirJogoRede() {
        esconderTodos();
        jogoRede.setVisible(true);
    }

    @FXML
    private void abrirDefinicoes() {
        esconderTodos();
        parametros.setVisible(true);
    }

    @FXML
    private void sairJogo() {
        System.exit(0);
    }

    @FXML
    private void abrirEscolherCor() {
        esconderTodos();
        if (escolherCor != null) {
            escolherCor.setVisible(true);
        }
    }

    /* =========================
               ROLETA
       ========================= */

    private void iniciarRoleta() {

        btnContinuarRoleta.setDisable(true);
        labelResultado.setText("A roleta vai decidir...");

        RotateTransition rt = new RotateTransition(Duration.seconds(1.2), roletaFundo);
        rt.setByAngle(360 * 3);
        rt.setCycleCount(1);

        rt.setOnFinished(e -> {
            boolean jogador1Escolhe = Math.random() < 0.5;
            jogadorQueEscolheCor = jogador1Escolhe ? 1 : 2;

            labelResultado.setText("Jogador " + jogadorQueEscolheCor + " escolhe a cor!");
            btnContinuarRoleta.setDisable(false);
        });

        rt.play();
    }

    private void continuarDepoisDaRoleta() {
        // Em vez de ir direto para o jogo, vai para o ecrã de escolher cor
        abrirEscolherCor();
    }

    /* =========================
           LÓGICA DO TABULEIRO
       ========================= */

    private void setupPeca(Circle peca) {
        if (peca == null) return;

        peca.setOnMouseClicked(e -> {

            limparStrokes();

            pecaSelecionada = peca;

            // Peça selecionada fica azul
            peca.setStroke(Color.BLUE);
            peca.setStrokeWidth(3);

            // Todas as peças claras ficam highlight verde
            highlightClaras();
        });
    }

    private void limparStrokes() {
        Circle[] todas = {
                pecaClara1, pecaClara2, pecaClara3, pecaClara4,
                pecaEscura1, pecaEscura2, pecaEscura3, pecaEscura4
        };

        for (Circle c : todas) {
            if (c != null) c.setStroke(null);
        }
    }

    private void highlightClaras() {
        Circle[] claras = { pecaClara1, pecaClara2, pecaClara3, pecaClara4 };

        for (Circle c : claras) {
            if (c != pecaSelecionada) {
                c.setStroke(Color.LIGHTGREEN);
                c.setStrokeWidth(3);
            }
        }
    }
}
