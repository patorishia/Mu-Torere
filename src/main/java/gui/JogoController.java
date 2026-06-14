/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package gui;

import group15.mu_torere.DadosGlobais;
import group15.mu_torere.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;


/**
 * Controlador do ecrã Jogo
 * Responsável por:
 *  - ligar GUI ao modelo
 *  - permitir selecionar peças
 *  - permitir mover peças
 *  - validar movimentos através do modelo
 *  - atualizar o jogador atual
 *  - detetar fim de jogo
 */
public class JogoController implements Initializable {

    @FXML private Label labelJogadorAtual;
    @FXML private Label labelEstadoRede;
    @FXML private TabPane tabPaneConversa;
    @FXML private ListView<String> listaMensagensConversa;
    @FXML private TextField campoMensagemConversa;
    @FXML private Button btnEnviarMensagemConversa;

    @FXML private Circle casaCentro, casa0, casa1, casa2, casa3, casa4, casa5, casa6, casa7;

    @FXML private Circle pecaClara1, pecaClara2, pecaClara3, pecaClara4;
    @FXML private Circle pecaEscura1, pecaEscura2, pecaEscura3, pecaEscura4;

    private Circle pecaSelecionada;

    private Jogo jogo;

    private final Map<Circle, Peca> mapaGuiParaModelo = new HashMap<>();
    private final Map<Peca, Circle> mapaModeloParaGui = new HashMap<>();
    private final Map<Circle, Posicao> mapaCasaGuiParaModelo = new HashMap<>();

    private Circle[] casas;
    private Circle[] pecas;
    private Circle[] pecasClaras;
    private Circle[] pecasEscuras;
    private boolean receberJogadasRede;
    private int numeroMensagensRede;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        if (DadosGlobais.jogoCarregado != null) {
            jogo = DadosGlobais.jogoCarregado;
            DadosGlobais.jogoCarregado = null;
        } else {
            jogo = new Jogo(
                    DadosGlobais.nomeJogador1,
                    DadosGlobais.nomeJogador2,
                    DadosGlobais.corJogador1,
                    DadosGlobais.corJogador2,
                    DadosGlobais.jogadorQueEscolheCor
            );
        }

        casas = new Circle[]{casa0, casa1, casa2, casa3, casa4, casa5, casa6, casa7, casaCentro};
        pecasClaras = new Circle[]{pecaClara1, pecaClara2, pecaClara3, pecaClara4};
        pecasEscuras = new Circle[]{pecaEscura1, pecaEscura2, pecaEscura3, pecaEscura4};
        pecas = new Circle[]{pecaClara1, pecaClara2, pecaClara3, pecaClara4,
                pecaEscura1, pecaEscura2, pecaEscura3, pecaEscura4};

        ligarCasasDoModelo();
        ligarPecasDoModelo();
        ligarClicksNasPecas();
        ligarClicksNasCasas();

        atualizarJogadorAtual();
        atualizarEstadoRedeInicial();
        configurarConversaRede();
        Platform.runLater(() -> atualizarDestaquesPecasMoveis());
        iniciarRececaoJogadasRede();
    }

    // -------------------------------------------------------------------------
    // LIGAÇÃO GUI ↔ MODELO
    // -------------------------------------------------------------------------

    private void ligarPecasDoModelo() {
        ligarPecasDoJogador(jogo.getJogador1());
        ligarPecasDoJogador(jogo.getJogador2());
    }

    private void ligarPecasDoJogador(Jogador jogador) {
        Circle[] pecasGui;

        if (jogador.getCor().equals("claro")) {
            pecasGui = pecasClaras;
        } else {
            pecasGui = pecasEscuras;
        }

        for (int i = 0; i < jogador.getPecas().size(); i++) {
            ligarPeca(pecasGui[i], jogador.getPecas().get(i));
            moverPecaGuiParaPosicao(pecasGui[i], jogador.getPecas().get(i).getPosicaoAtual());
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

        if (!jogo.ePecaDoJogadorAtual(pecaModelo)) return;

        List<Posicao> movimentosValidos = jogo.obterMovimentosValidos(pecaModelo);

        if (movimentosValidos.isEmpty()) return;

        Posicao destino = movimentosValidos.get(0);
        Circle casaDestino = obterCasaGui(destino);

        if (casaDestino == null) return;

        pecaSelecionada = pecaGui;

        moverPecaParaCasa(casaDestino);
    }

    private void ligarClicksNasCasas() {
        for (Circle casa : casas) {
            casa.setOnMouseClicked(e -> moverPecaParaCasa(casa));
        }
    }

    private void moverPecaParaCasa(Circle casaGui) {

        if (pecaSelecionada == null) return;

        Peca pecaModelo = mapaGuiParaModelo.get(pecaSelecionada);
        Posicao destino = mapaCasaGuiParaModelo.get(casaGui);

        if (!jogo.movimentoValido(pecaModelo, destino)) return;

        int origemRede = pecaModelo.getPosicaoAtual().getId();
        int destinoRede = destino.getId();

        jogo.fazerMovimento(pecaModelo, destino);

        pecaSelecionada.setCenterX(casaGui.getCenterX());
        pecaSelecionada.setCenterY(casaGui.getCenterY());

        limparStrokes();
        pecaSelecionada = null;

        atualizarJogadorAtual();
        atualizarDestaquesPecasMoveis();
        enviarJogadaRede(origemRede, destinoRede);
        enviarEstadoJogoRede();
        indicarEsperaAdversario();

        verificarFimJogo();
    }

    private void enviarJogadaRede(int origem, int destino) {
        if (!"rede".equals(DadosGlobais.modoJogo)) {
            return;
        }

        if (DadosGlobais.servidorRede != null && DadosGlobais.servidorRede.isClienteLigado()) {
            DadosGlobais.servidorRede.enviarJogada(origem, destino);
        }

        if (DadosGlobais.clienteRede != null && DadosGlobais.clienteRede.isLigado()) {
            DadosGlobais.clienteRede.enviarJogada(origem, destino);
        }
    }

    private void iniciarRececaoJogadasRede() {
        if (!"rede".equals(DadosGlobais.modoJogo)) {
            return;
        }

        receberJogadasRede = true;
        numeroMensagensRede = 0;

        Thread thread = new Thread(() -> {
            while (receberJogadasRede) {
                String mensagem = obterMensagemRede();
                int numeroMensagens = obterNumeroMensagensRede();

                if (mensagem != null && numeroMensagens > numeroMensagensRede) {
                    numeroMensagensRede = numeroMensagens;

                    if (mensagem.startsWith("JOGADA|")) {
                        Platform.runLater(() -> aplicarJogadaRecebida(mensagem));
                    } else if (mensagem.startsWith("ESTADO_JOGO|")) {
                        Platform.runLater(() -> aplicarEstadoRecebido(mensagem));
                    } else if (mensagem.startsWith("CHAT|")) {
                        Platform.runLater(() -> receberMensagemConversa(mensagem));
                    }
                }

                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    receberJogadasRede = false;
                }
            }
        });

        thread.start();
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

    private void aplicarJogadaRecebida(String mensagem) {
        String[] partes = mensagem.split("\\|");

        if (partes.length != 3) {
            return;
        }

        int origem;
        int destino;

        try {
            origem = Integer.parseInt(partes[1]);
            destino = Integer.parseInt(partes[2]);
        } catch (NumberFormatException e) {
            return;
        }

        if (jogo.aplicarJogadaRede(origem, destino)) {
            atualizarPecasGui();
            atualizarJogadorAtual();
            atualizarDestaquesPecasMoveis();
            indicarJogadaRecebida();
            verificarFimJogo();
        }
    }

    private void aplicarEstadoRecebido(String mensagem) {
        try {
            if (jogo.aplicarMensagemEstadoRede(mensagem)) {
                mapaGuiParaModelo.clear();
                mapaModeloParaGui.clear();
                mapaCasaGuiParaModelo.clear();

                ligarCasasDoModelo();
                ligarPecasDoModelo();
                atualizarPecasGui();
                atualizarJogadorAtual();
                atualizarDestaquesPecasMoveis();
                indicarJogadaRecebida();
                verificarFimJogo();
            }
        } catch (NumberFormatException e) {
            return;
        }
    }

    private void verificarFimJogo() {
        // Verificar se o jogador seguinte tem movimentos
        if (!jogadorTemMovimentos(jogo.getJogadorAtual())) {

            // O jogador atual NÃO tem movimentos → perdeu
            // Logo, o vencedor é o outro jogador
            DadosGlobais.vencedor =
                    (jogo.getJogadorAtual() == jogo.getJogador1())
                            ? jogo.getJogador2().getNome()
                            : jogo.getJogador1().getNome();

            ScreenManager.show("/fxml/FimJogo.fxml");
        }
    }

    private void atualizarPecasGui() {
        for (Circle pecaGui : pecas) {
            Peca pecaModelo = mapaGuiParaModelo.get(pecaGui);

            if (pecaModelo != null) {
                moverPecaGuiParaPosicao(pecaGui, pecaModelo.getPosicaoAtual());
            }
        }
    }

    private void enviarEstadoJogoRede() {
        if (!"rede".equals(DadosGlobais.modoJogo)) {
            return;
        }

        if (DadosGlobais.servidorRede != null && DadosGlobais.servidorRede.isClienteLigado()) {
            DadosGlobais.servidorRede.enviarEstadoJogo(jogo);
        }

        if (DadosGlobais.clienteRede != null && DadosGlobais.clienteRede.isLigado()) {
            DadosGlobais.clienteRede.enviarEstadoJogo(jogo);
        }
    }

    private Circle obterCasaGui(Posicao posicao) {
        for (Circle casa : casas) {
            if (mapaCasaGuiParaModelo.get(casa) == posicao) {
                return casa;
            }
        }

        return null;
    }

    private void moverPecaGuiParaPosicao(Circle pecaGui, Posicao posicao) {
        Circle casaGui = obterCasaGui(posicao);

        if (casaGui != null) {
            pecaGui.setCenterX(casaGui.getCenterX());
            pecaGui.setCenterY(casaGui.getCenterY());
        }
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
                pecaGui.setStroke(Color.GREEN);
                pecaGui.setStrokeWidth(3);
            }
        }
    }

    private void atualizarJogadorAtual() {
        labelJogadorAtual.setText(jogo.getJogadorAtual().getNome());
    }

    private void atualizarEstadoRedeInicial() {
        if ("rede".equals(DadosGlobais.modoJogo)) {
            labelEstadoRede.setText("A jogar em rede");
        } else {
            labelEstadoRede.setText("");
        }
    }

    private void indicarEsperaAdversario() {
        if ("rede".equals(DadosGlobais.modoJogo)) {
            labelEstadoRede.setText("À espera do adversário...");
        }
    }

    private void indicarJogadaRecebida() {
        if ("rede".equals(DadosGlobais.modoJogo)) {
            labelEstadoRede.setText("Jogada recebida. A tua vez.");
        }
    }

    private void configurarConversaRede() {
        boolean modoRede = "rede".equals(DadosGlobais.modoJogo);

        tabPaneConversa.setVisible(modoRede);
        tabPaneConversa.setManaged(modoRede);

        if (!modoRede) {
            campoMensagemConversa.setDisable(true);
            btnEnviarMensagemConversa.setDisable(true);
            return;
        }

        listaMensagensConversa.getItems().clear();
        listaMensagensConversa.getItems().add("Conversa iniciada.");
        campoMensagemConversa.setDisable(false);
        btnEnviarMensagemConversa.setDisable(false);
    }

    @FXML
    private void enviarMensagemConversa() {
        if (!"rede".equals(DadosGlobais.modoJogo)) {
            return;
        }

        String texto = campoMensagemConversa.getText();

        if (texto == null) {
            return;
        }

        texto = texto.trim();

        if (texto.isEmpty()) {
            return;
        }

        texto = prepararTextoConversa(texto);
        enviarMensagemConversaRede("CHAT|" + texto);
        listaMensagensConversa.getItems().add("Eu: " + texto);
        campoMensagemConversa.clear();
    }

    private void enviarMensagemConversaRede(String mensagem) {
        if (DadosGlobais.servidorRede != null && DadosGlobais.servidorRede.isClienteLigado()) {
            DadosGlobais.servidorRede.enviarMensagem(mensagem);
        }

        if (DadosGlobais.clienteRede != null && DadosGlobais.clienteRede.isLigado()) {
            DadosGlobais.clienteRede.enviarMensagem(mensagem);
        }
    }

    private void receberMensagemConversa(String mensagem) {
        String texto = mensagem.substring("CHAT|".length());

        if (!texto.isEmpty()) {
            listaMensagensConversa.getItems().add("Adversário: " + texto);
        }
    }

    private String prepararTextoConversa(String texto) {
        return texto.replace("|", "/");
    }

    private boolean jogadorTemMovimentos(Jogador jog) {
        for (Peca p : jog.getPecas()) {
            if (!jogo.obterMovimentosValidos(p).isEmpty()) return true;
        }
        return false;
    }

    // -------------------------------------------------------------------------
    // BOTÕES LATERAIS
    // -------------------------------------------------------------------------

    @FXML
    private void abrirDefinicoes() {
        ScreenManager.show("/fxml/Parametros.fxml");
    }

    @FXML
    private void guardarJogo() {
        FileChooser fc = new FileChooser();
        fc.setTitle("Guardar Jogo");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Ficheiros Mu Torere", "*.mt"));

        File ficheiro = fc.showSaveDialog(null);

        if (ficheiro != null) {
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
