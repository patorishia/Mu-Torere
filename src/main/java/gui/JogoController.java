/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package gui;

import group15.mu_torere.DadosGlobais;
import group15.mu_torere.*;
import java.io.File;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.stage.FileChooser;

public class JogoController implements Initializable {

    @FXML
    private Label labelJogadorAtual;

    @FXML
    private Circle casaCentro, casa0, casa1, casa2, casa3, casa4, casa5, casa6, casa7;

    @FXML
    private Circle pecaClara1, pecaClara2, pecaClara3, pecaClara4;
    @FXML
    private Circle pecaEscura1, pecaEscura2, pecaEscura3, pecaEscura4;

    private Circle pecaSelecionada;

    private Jogo jogo;

    private final Map<Circle, Peca> mapaGuiParaModelo = new HashMap<>();
    private final Map<Peca, Circle> mapaModeloParaGui = new HashMap<>();
    private final Map<Circle, Posicao> mapaCasaGuiParaModelo = new HashMap<>();

    private Circle[] casas;
    private Circle[] pecas;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // Se existe jogo carregado → usa-o
        if (DadosGlobais.jogoCarregado != null) {
            jogo = DadosGlobais.jogoCarregado;
            DadosGlobais.jogoCarregado = null; // limpar para não reutilizar
        } else {
            // Criar jogo novo
            jogo = new Jogo(
                    DadosGlobais.nomeJogador1,
                    DadosGlobais.nomeJogador2,
                    DadosGlobais.corJogador1,
                    DadosGlobais.corJogador2,
                    DadosGlobais.jogadorQueEscolheCor
            );
        }

        casas = new Circle[]{casa0, casa1, casa2, casa3, casa4, casa5, casa6, casa7, casaCentro};
        pecas = new Circle[]{pecaClara1, pecaClara2, pecaClara3, pecaClara4,
            pecaEscura1, pecaEscura2, pecaEscura3, pecaEscura4};

        ligarCasasDoModelo();
        ligarPecasDoModelo();
        ligarClicksNasPecas();
        ligarClicksNasCasas();

        atualizarJogadorAtual();
        Platform.runLater(this::atualizarDestaquesPecasMoveis);
    }

    // -------------------------------------------------------------------------
    // LIGAÇÃO GUI ↔ MODELO
    // -------------------------------------------------------------------------
    private void ligarPecasDoModelo() {
        Tabuleiro tab = jogo.getTabuleiro();

        mapaGuiParaModelo.clear();
        mapaModeloParaGui.clear();

        // Arrays de círculos por cor
        Circle[] claras = {pecaClara1, pecaClara2, pecaClara3, pecaClara4};
        Circle[] escuras = {pecaEscura1, pecaEscura2, pecaEscura3, pecaEscura4};
        int idxClara = 0;
        int idxEscura = 0;

        // Posições exteriores 0..7
        for (int i = 0; i < 8; i++) {
            Posicao pos = tab.getPosicao(i);
            Peca p = pos.getOcupante();
            if (p == null) {
                continue;
            }

            Circle casaGui = casas[i];
            Circle pecaGui;

            if ("claro".equals(p.getDono().getCor())) {
                pecaGui = claras[idxClara++];
            } else {
                pecaGui = escuras[idxEscura++];
            }

            pecaGui.setCenterX(casaGui.getCenterX());
            pecaGui.setCenterY(casaGui.getCenterY());

            ligarPeca(pecaGui, p);
        }

        // Posição do centro (8)
        Posicao posCentro = tab.getPosicao(8);
        Peca pCentro = posCentro.getOcupante();
        if (pCentro != null) {
            Circle pecaGui;
            if ("claro".equals(pCentro.getDono().getCor())) {
                pecaGui = claras[idxClara++];
            } else {
                pecaGui = escuras[idxEscura++];
            }

            pecaGui.setCenterX(casaCentro.getCenterX());
            pecaGui.setCenterY(casaCentro.getCenterY());

            ligarPeca(pecaGui, pCentro);
        }
    }

    private void ligarPeca(Circle pecaGui, Peca pecaModelo) {
        mapaGuiParaModelo.put(pecaGui, pecaModelo);
        mapaModeloParaGui.put(pecaModelo, pecaGui);
    }

    private void ligarCasasDoModelo() {
        Tabuleiro tab = jogo.getTabuleiro();

        mapaCasaGuiParaModelo.put(casa0, tab.getPosicao(0));
        mapaCasaGuiParaModelo.put(casa1, tab.getPosicao(1));
        mapaCasaGuiParaModelo.put(casa2, tab.getPosicao(2));
        mapaCasaGuiParaModelo.put(casa3, tab.getPosicao(3));
        mapaCasaGuiParaModelo.put(casa4, tab.getPosicao(4));
        mapaCasaGuiParaModelo.put(casa5, tab.getPosicao(5));
        mapaCasaGuiParaModelo.put(casa6, tab.getPosicao(6));
        mapaCasaGuiParaModelo.put(casa7, tab.getPosicao(7));
        mapaCasaGuiParaModelo.put(casaCentro, tab.getPosicao(8));
    }

    // -------------------------------------------------------------------------
    // INTERAÇÃO DO JOGADOR
    // -------------------------------------------------------------------------
    private void ligarClicksNasPecas() {
        for (Circle peca : pecas) {
            peca.setOnMouseClicked(e -> selecionarPeca(peca));
        }
    }

    private void selecionarPeca(Circle pecaGui) {

        Peca pecaModelo = mapaGuiParaModelo.get(pecaGui);

        if (!jogo.ePecaDoJogadorAtual(pecaModelo)) {
            return;
        }

        List<Posicao> movimentosValidos = jogo.obterMovimentosValidos(pecaModelo);

        if (movimentosValidos.isEmpty()) {
            return;
        }

        pecaSelecionada = pecaGui;

        limparStrokes();
        pecaGui.setStroke(Color.web("#1E90FF"));
        pecaGui.setStrokeWidth(3);
    }

    private void ligarClicksNasCasas() {
        for (Circle casa : casas) {
            casa.setOnMouseClicked(e -> moverPecaParaCasa(casa));
        }
    }

    private void moverPecaParaCasa(Circle casaGui) {

        if (pecaSelecionada == null) {
            return;
        }

        Peca pecaModelo = mapaGuiParaModelo.get(pecaSelecionada);
        Posicao destino = mapaCasaGuiParaModelo.get(casaGui);

        if (!jogo.movimentoValido(pecaModelo, destino)) {
            return;
        }

        jogo.fazerMovimento(pecaModelo, destino);

        pecaSelecionada.setCenterX(casaGui.getCenterX());
        pecaSelecionada.setCenterY(casaGui.getCenterY());
        pecaSelecionada.toFront(); // EVITA DESAPARECER

        limparStrokes();
        pecaSelecionada = null;

        atualizarJogadorAtual();
        atualizarDestaquesPecasMoveis();

        if (!jogadorTemMovimentos(jogo.getJogadorAtual())) {

            DadosGlobais.vencedor
                    = (jogo.getJogadorAtual() == jogo.getJogador1())
                    ? jogo.getJogador2().getNome()
                    : jogo.getJogador1().getNome();

            ScreenManager.show("/fxml/FimJogo.fxml");
        }
    }

    private boolean jogadorTemMovimentos(Jogador jog) {
        for (Peca p : jog.getPecas()) {
            if (!jogo.obterMovimentosValidos(p).isEmpty()) {
                return true;
            }
        }
        return false;
    }

    private void limparStrokes() {
        for (Circle p : pecas) {
            p.setStroke(null);
            p.setStrokeWidth(0);
        }
    }

    private void atualizarDestaquesPecasMoveis() {
        limparStrokes();

        for (Circle pecaGui : pecas) {
            Peca pecaModelo = mapaGuiParaModelo.get(pecaGui);

            if (jogo.ePecaDoJogadorAtual(pecaModelo)
                    && !jogo.obterMovimentosValidos(pecaModelo).isEmpty()) {
                pecaGui.setStroke(Color.web("#00BFFF"));
                pecaGui.setStrokeWidth(3);
            }
        }
    }

    private void atualizarJogadorAtual() {
        labelJogadorAtual.setText(jogo.getJogadorAtual().getNome());
    }

    // -------------------------------------------------------------------------
    // BOTÕES LATERAIS
    // -------------------------------------------------------------------------
    @FXML
    private void abrirDefinicoes() {
        ScreenManager.show("/fxml/Parametros.fxml");
    }

    // Mantive o teu método de guardar jogo
    @FXML
    private void guardarJogo() {
        FileChooser fc = new FileChooser();
        fc.setTitle("Guardar Jogo");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Ficheiros MuTorere", "*.mt"));

        File ficheiro = fc.showSaveDialog(null);

        if (ficheiro != null) {

            // Garantir extensão .mt
            if (!ficheiro.getName().endsWith(".mt")) {
                ficheiro = new File(ficheiro.getAbsolutePath() + ".mt");
            }

            GestorFicheiros.guardarJogo(jogo, ficheiro);
        }
    }

    @FXML
    private void mostrarMenuInicial() {
        ScreenManager.show("/fxml/MenuInicial.fxml");
    }
}
