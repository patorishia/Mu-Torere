/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package gui;


import group15.mu_torere.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Controlador do ecrã Jogo Local.
 * Responsável por:
 *  - ligar GUI ao modelo
 *  - permitir selecionar peças
 *  - permitir mover peças
 *  - validar movimentos através do modelo
 *  - atualizar o jogador atual
 */
public class JogoLocalController implements Initializable {

    // Label lateral que mostra o jogador atual
    @FXML private Label labelJogadorAtual;

    // Casas do tabuleiro (GUI)
    @FXML private Circle casaCentro, casa0, casa1, casa2, casa3, casa4, casa5, casa6, casa7;

    // Peças claras (GUI)
    @FXML private Circle pecaClara1, pecaClara2, pecaClara3, pecaClara4;

    // Peças escuras (GUI)
    @FXML private Circle pecaEscura1, pecaEscura2, pecaEscura3, pecaEscura4;

    // Peça selecionada pelo jogador
    private Circle pecaSelecionada;

    // Modelo do jogo
    private Jogo jogo;

    // Mapas GUI ↔ Modelo
    private final Map<Circle, Peca> mapaGuiParaModelo = new HashMap<>();
    private final Map<Peca, Circle> mapaModeloParaGui = new HashMap<>();
    private final Map<Circle, Posicao> mapaCasaGuiParaModelo = new HashMap<>();

    // Arrays úteis
    private Circle[] casas;
    private Circle[] pecas;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // Criar jogo com nomes e cores escolhidas no ecrã anterior
        jogo = new Jogo(
                DadosGlobais.nomeJogador1,
                DadosGlobais.nomeJogador2,
                DadosGlobais.corJogador1,
                DadosGlobais.corJogador2
        );

        // Arrays para facilitar loops
        casas = new Circle[]{casa0, casa1, casa2, casa3, casa4, casa5, casa6, casa7, casaCentro};
        pecas = new Circle[]{pecaClara1, pecaClara2, pecaClara3, pecaClara4,
                             pecaEscura1, pecaEscura2, pecaEscura3, pecaEscura4};

        // Ligar GUI ao Modelo
        ligarPecasDoModelo();
        ligarCasasDoModelo();

        // Ligar eventos de clique
        ligarClicksNasPecas();
        ligarClicksNasCasas();

        // Mostrar jogador inicial
        atualizarJogadorAtual();
    }

    // -------------------------------------------------------------------------
    // LIGAÇÃO GUI ↔ MODELO
    // -------------------------------------------------------------------------

    /**
     * Liga cada peça da GUI à peça correspondente no modelo.
     * As posições iniciais são:
     * Jogador 1 → 0,1,2,3
     * Jogador 2 → 4,5,6,7
     */
    private void ligarPecasDoModelo() {
        Tabuleiro tab = jogo.getTabuleiro();

        // Jogador 1 (claro)
        Peca p1 = tab.getPosicao(0).getOcupante();
        Peca p2 = tab.getPosicao(1).getOcupante();
        Peca p3 = tab.getPosicao(2).getOcupante();
        Peca p4 = tab.getPosicao(3).getOcupante();

        mapaGuiParaModelo.put(pecaClara1, p1);
        mapaGuiParaModelo.put(pecaClara2, p2);
        mapaGuiParaModelo.put(pecaClara3, p3);
        mapaGuiParaModelo.put(pecaClara4, p4);

        mapaModeloParaGui.put(p1, pecaClara1);
        mapaModeloParaGui.put(p2, pecaClara2);
        mapaModeloParaGui.put(p3, pecaClara3);
        mapaModeloParaGui.put(p4, pecaClara4);

        // Jogador 2 (escuro)
        Peca e1 = tab.getPosicao(4).getOcupante();
        Peca e2 = tab.getPosicao(5).getOcupante();
        Peca e3 = tab.getPosicao(6).getOcupante();
        Peca e4 = tab.getPosicao(7).getOcupante();

        mapaGuiParaModelo.put(pecaEscura1, e1);
        mapaGuiParaModelo.put(pecaEscura2, e2);
        mapaGuiParaModelo.put(pecaEscura3, e3);
        mapaGuiParaModelo.put(pecaEscura4, e4);

        mapaModeloParaGui.put(e1, pecaEscura1);
        mapaModeloParaGui.put(e2, pecaEscura2);
        mapaModeloParaGui.put(e3, pecaEscura3);
        mapaModeloParaGui.put(e4, pecaEscura4);
    }

    /**
     * Liga cada casa da GUI à posição correspondente no modelo.
     */
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

    /**
     * Liga o clique às peças.
     */
    private void ligarClicksNasPecas() {
        for (Circle peca : pecas) {
            peca.setOnMouseClicked(e -> selecionarPeca(peca));
        }
    }

    /**
     * Seleciona uma peça se ela pertencer ao jogador atual.
     */
    private void selecionarPeca(Circle pecaGui) {

        limparStrokes();

        Peca pecaModelo = mapaGuiParaModelo.get(pecaGui);

        // Só pode selecionar peças do jogador atual
        if (!jogo.ePecaDoJogadorAtual(pecaModelo)) return;

        pecaSelecionada = pecaGui;

        // Highlight azul
        pecaGui.setStroke(Color.BLUE);
        pecaGui.setStrokeWidth(3);
    }

    /**
     * Liga o clique às casas.
     */
    private void ligarClicksNasCasas() {
        for (Circle casa : casas) {
            casa.setOnMouseClicked(e -> moverPecaParaCasa(casa));
        }
    }

    /**
     * Tenta mover a peça selecionada para a casa clicada.
     */
    private void moverPecaParaCasa(Circle casaGui) {

        if (pecaSelecionada == null) return;

        Peca pecaModelo = mapaGuiParaModelo.get(pecaSelecionada);
        Posicao destino = mapaCasaGuiParaModelo.get(casaGui);

        // Validar movimento no modelo
        if (!jogo.movimentoValido(pecaModelo, destino)) return;

        // Atualizar modelo
        jogo.fazerMovimento(pecaModelo, destino);

        // Atualizar GUI
        pecaSelecionada.setCenterX(casaGui.getCenterX());
        pecaSelecionada.setCenterY(casaGui.getCenterY());

        limparStrokes();
        pecaSelecionada = null;

        atualizarJogadorAtual();
    }

    /**
     * Remove highlight de todas as peças.
     */
    private void limparStrokes() {
        for (Circle p : pecas) {
            p.setStroke(null);
            p.setStrokeWidth(0);
        }
    }

    /**
     * Atualiza o texto do jogador atual.
     */
    private void atualizarJogadorAtual() {
        labelJogadorAtual.setText(jogo.getJogadorAtual().getNome());
    }

    // -------------------------------------------------------------------------
    // BOTÕES LATERAIS
    // -------------------------------------------------------------------------

    @FXML
    private void abrirDefinicoes() {
        ScreenManager.show("/gui/parametros/Parametros.fxml");
    }

    @FXML
    private void mostrarMenuInicial() {
        ScreenManager.show("/gui/menu/MenuInicial.fxml");
    }
}
