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
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Controlador do ecrã Jogo Responsável por: - ligar GUI ao modelo - permitir
 * selecionar peças - permitir mover peças - validar movimentos através do
 * modelo - atualizar o jogador atual - detetar fim de jogo
 */
public class JogoController implements Initializable {

    /**
     * Cria o controlador do ecrã principal do jogo.
     */
    public JogoController() {
    }

    /** Label que apresenta o jogador com o turno atual. */
    @FXML
    private Label labelJogadorAtual;

    /** Label que mostra mensagens de estado do modo de rede. */
    @FXML
    private Label labelEstadoRede;

    /** Separador que contém a conversa entre jogadores no modo de rede. */
    @FXML
    private TabPane tabPaneConversa;

    /** Lista visual das mensagens de conversa recebidas e enviadas. */
    @FXML
    private ListView<String> listaMensagensConversa;

    /** Campo de texto usado para escrever mensagens de conversa. */
    @FXML
    private TextField campoMensagemConversa;

    /** Botão usado para enviar mensagens de conversa. */
    @FXML
    private Button btnEnviarMensagemConversa;

    /** Botão usado para guardar a partida local. */
    @FXML
    private Button btnGuardarJogo;

    /** Painel exterior usado para calcular a escala responsiva do tabuleiro. */
    @FXML
    private StackPane painelTabuleiro;

    /** Pane que contém as casas e peças do tabuleiro. */
    @FXML
    private Pane tabuleiro;

    /** Círculos que representam visualmente as casas do tabuleiro. */
    @FXML
    private Circle casaCentro, casa0, casa1, casa2, casa3, casa4, casa5, casa6, casa7;

    /** Círculos que representam as quatro peças claras. */
    @FXML
    private Circle pecaClara1, pecaClara2, pecaClara3, pecaClara4;

    /** Círculos que representam as quatro peças escuras. */
    @FXML
    private Circle pecaEscura1, pecaEscura2, pecaEscura3, pecaEscura4;

    /** Peça atualmente selecionada na interface gráfica. */
    private Circle pecaSelecionada;

    /** Modelo lógico da partida em curso. */
    private Jogo jogo;

    /** Associação entre cada peça gráfica e a peça correspondente no modelo. */
    private final Map<Circle, Peca> mapaGuiParaModelo = new HashMap<>();

    /** Associação entre cada peça do modelo e a peça correspondente na interface. */
    private final Map<Peca, Circle> mapaModeloParaGui = new HashMap<>();

    /** Associação entre cada casa gráfica e a posição correspondente no modelo. */
    private final Map<Circle, Posicao> mapaCasaGuiParaModelo = new HashMap<>();

    /** Array com todas as casas do tabuleiro na interface. */
    private Circle[] casas;

    /** Array com todas as peças na interface. */
    private Circle[] pecas;

    /** Array com as peças claras na interface. */
    private Circle[] pecasClaras;

    /** Array com as peças escuras na interface. */
    private Circle[] pecasEscuras;

    /** Indica se a thread de receção de jogadas de rede deve continuar ativa. */
    private boolean receberJogadasRede;

    /** Número da última mensagem de rede já processada por este controller. */
    private int numeroMensagensRede;

    /**
     * Inicializa a partida, liga a interface ao modelo e prepara eventos, rede e escala.
     *
     * @param url localização usada para resolver caminhos relativos, fornecida pelo JavaFX
     * @param rb recursos de internacionalização, fornecidos pelo JavaFX
     */
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

        if ("rede".equals(DadosGlobais.modoJogo)) {
            btnGuardarJogo.setVisible(false);
            btnGuardarJogo.setManaged(false);
        }

        casas = new Circle[]{casa0, casa1, casa2, casa3, casa4, casa5, casa6, casa7, casaCentro};
        pecasClaras = new Circle[]{pecaClara1, pecaClara2, pecaClara3, pecaClara4};
        pecasEscuras = new Circle[]{pecaEscura1, pecaEscura2, pecaEscura3, pecaEscura4};
        pecas = new Circle[]{pecaClara1, pecaClara2, pecaClara3, pecaClara4,
            pecaEscura1, pecaEscura2, pecaEscura3, pecaEscura4};

        ligarCasasDoModelo();
        ligarPecasDoModelo();

        Platform.runLater(() -> {
            atualizarPecasGui();
            atualizarJogadorAtual();
            atualizarDestaquesPecasMoveis();
        });

        ligarClicksNasPecas();
        ligarClicksNasCasas();

        atualizarEstadoRedeInicial();
        configurarConversaRede();
        configurarTabuleiroResponsivo();
        iniciarRececaoJogadasRede();
        //Platform.runLater(this::aplicarTema);
        
        Platform.runLater(this::atualizarDestaquesPecasMoveis);



    }

    // -------------------------------------------------------------------------
    // LIGAÇÃO GUI ↔ MODELO
    // -------------------------------------------------------------------------
    /**
     * Liga todas as peças do modelo às peças gráficas correspondentes.
     */
    private void ligarPecasDoModelo() {
        ligarPecasDoJogador(jogo.getJogador1());
        ligarPecasDoJogador(jogo.getJogador2());
    }

    /**
     * Liga as peças de um jogador às peças gráficas da cor correspondente.
     *
     * @param jogador jogador cujas peças serão ligadas à interface
     */
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

    /**
     * Regista a correspondência entre uma peça gráfica e uma peça do modelo.
     *
     * @param pecaGui círculo que representa a peça na interface
     * @param pecaModelo peça correspondente no modelo do jogo
     */
    private void ligarPeca(Circle pecaGui, Peca pecaModelo) {
        mapaGuiParaModelo.put(pecaGui, pecaModelo);
        mapaModeloParaGui.put(pecaModelo, pecaGui);
    }

    /**
     * Liga cada casa gráfica à posição correspondente do tabuleiro lógico.
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
     * Associa o clique de cada peça gráfica ao método de seleção de peças.
     */
    private void ligarClicksNasPecas() {
        for (Circle peca : pecas) {
            peca.setOnMouseClicked(e -> selecionarPeca(peca));
        }
    }

    /**
     * Seleciona uma peça, valida se pode mover e executa o primeiro movimento válido.
     *
     * @param pecaGui círculo que representa a peça selecionada
     */
    private void selecionarPeca(Circle pecaGui) {

        Peca pecaModelo = mapaGuiParaModelo.get(pecaGui);

        if (!pecaPertenceAoJogadorLocal(pecaModelo)) {
            GestorSons.tocarErro();
            return;
        }

        if (!jogo.ePecaDoJogadorAtual(pecaModelo)) {
            GestorSons.tocarErro();
            return;
        }

        List<Posicao> movimentosValidos = jogo.obterMovimentosValidos(pecaModelo);

        if (movimentosValidos.isEmpty()) {
            GestorSons.tocarErro();
            return;
        }

        Posicao destino = movimentosValidos.get(0);
        Circle casaDestino = obterCasaGui(destino);

        if (casaDestino == null) {
            GestorSons.tocarErro();
            return;
        }

        pecaSelecionada = pecaGui;

        moverPecaParaCasa(casaDestino);
    }

    /**
     * Associa o clique de cada casa gráfica ao método de movimento.
     */
    private void ligarClicksNasCasas() {
        for (Circle casa : casas) {
            casa.setOnMouseClicked(e -> moverPecaParaCasa(casa));
        }
    }

    /**
     * Move a peça selecionada para a casa indicada, se o movimento for válido.
     *
     * @param casaGui círculo que representa a casa de destino
     */
    private void moverPecaParaCasa(Circle casaGui) {

        if (pecaSelecionada == null) {
            GestorSons.tocarErro();
            return;
        }

        Peca pecaModelo = mapaGuiParaModelo.get(pecaSelecionada);
        Posicao destino = mapaCasaGuiParaModelo.get(casaGui);

        if (!pecaPertenceAoJogadorLocal(pecaModelo)) {
            GestorSons.tocarErro();
            return;
        }

        if (!jogo.movimentoValido(pecaModelo, destino)) {
            GestorSons.tocarErro();
            return;
        }

        int origemRede = pecaModelo.getPosicaoAtual().getId();
        int destinoRede = destino.getId();

        jogo.fazerMovimento(pecaModelo, destino);
        GestorSons.tocarJogada();

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

    /**
     * Envia uma jogada pela ligação de rede ativa.
     *
     * @param origem identificador da posição de origem
     * @param destino identificador da posição de destino
     */
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

    /**
     * Inicia a receção assíncrona de jogadas, estados, conversa e desistências pela rede.
     */
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
                    } else if (mensagem.startsWith("DESISTENCIA")) {
                        Platform.runLater(() -> adversarioDesistiu());
                        receberJogadasRede = false;
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

    /**
     * Obtém a última mensagem recebida pela ligação de rede ativa.
     *
     * @return última mensagem recebida, ou null se não houver ligação
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
     * Obtém o número de mensagens recebidas pela ligação de rede ativa.
     *
     * @return quantidade de mensagens recebidas
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
     * Aplica uma jogada recebida pela rede ao modelo e atualiza a interface.
     *
     * @param mensagem mensagem no formato "JOGADA|origem|destino"
     */
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
            GestorSons.tocarJogada();
            atualizarPecasGui();
            atualizarJogadorAtual();
            atualizarDestaquesPecasMoveis();
            indicarJogadaRecebida();
            verificarFimJogo();
        }
    }

    /**
     * Aplica um estado completo de jogo recebido pela rede.
     *
     * @param mensagem mensagem de estado criada pelo modelo do jogo
     */
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

    /**
     * Verifica se o jogador que ficou com o turno ainda tem movimentos disponíveis.
     */
    private void verificarFimJogo() {
        // Verificar se o jogador seguinte tem movimentos
        if (!jogadorTemMovimentos(jogo.getJogadorAtual())) {

            // O jogador atual NÃO tem movimentos → perdeu
            // Logo, o vencedor é o outro jogador
            DadosGlobais.vencedor
                    = (jogo.getJogadorAtual() == jogo.getJogador1())
                    ? jogo.getJogador2().getNome()
                    : jogo.getJogador1().getNome();

            receberJogadasRede = false;
            ScreenManager.show("/fxml/FimJogo.fxml");
        }
    }

    /**
     * Atualiza a posição gráfica de todas as peças com base no modelo.
     */
    private void atualizarPecasGui() {
        for (Circle pecaGui : pecas) {
            Peca pecaModelo = mapaGuiParaModelo.get(pecaGui);

            if (pecaModelo != null) {
                moverPecaGuiParaPosicao(pecaGui, pecaModelo.getPosicaoAtual());
            }
        }
    }

    /**
     * Envia o estado completo do jogo pela ligação de rede ativa.
     */
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

    /**
     * Obtém a casa gráfica associada a uma posição do modelo.
     *
     * @param posicao posição lógica procurada
     * @return círculo da casa correspondente, ou null se não existir associação
     */
    private Circle obterCasaGui(Posicao posicao) {
        for (Circle casa : casas) {
            if (mapaCasaGuiParaModelo.get(casa) == posicao) {
                return casa;
            }
        }

        return null;
    }

    /**
     * Move uma peça gráfica para as coordenadas da posição lógica indicada.
     *
     * @param pecaGui círculo que representa a peça
     * @param posicao posição lógica de destino
     */
    private void moverPecaGuiParaPosicao(Circle pecaGui, Posicao posicao) {
        Circle casaGui = obterCasaGui(posicao);

        if (casaGui != null) {
            pecaGui.setCenterX(casaGui.getCenterX());
            pecaGui.setCenterY(casaGui.getCenterY());
        }
    }

    /**
     * Remove os contornos visuais de todas as peças.
     */
    private void limparStrokes() {
        for (Circle p : pecas) {
            p.setStroke(null);
            p.setStrokeWidth(0);
        }
    }

    /**
     * Destaca visualmente as peças do jogador local que têm movimentos válidos.
     */
    private void atualizarDestaquesPecasMoveis() {
        limparStrokes();

        if (!jogadorLocalTemTurno()) {
            return;
        }

        for (Circle pecaGui : pecas) {
            Peca pecaModelo = mapaGuiParaModelo.get(pecaGui);

            if (jogo.ePecaDoJogadorAtual(pecaModelo)
                    && pecaPertenceAoJogadorLocal(pecaModelo)
                    && !jogo.obterMovimentosValidos(pecaModelo).isEmpty()) {
                pecaGui.setStroke(Color.GREEN);
                pecaGui.setStrokeWidth(3);
            }
        }
    }

    /**
     * Atualiza a label que indica o jogador com o turno atual.
     */
    private void atualizarJogadorAtual() {
        labelJogadorAtual.setText(jogo.getJogadorAtual().getNome());
    }

    /**
     * Atualiza a mensagem inicial de estado consoante o modo local ou rede.
     */
    private void atualizarEstadoRedeInicial() {
        if ("rede".equals(DadosGlobais.modoJogo)) {
            labelEstadoRede.setText("A tua cor é: " + DadosGlobais.corJogadorLocal);
        } else {
            labelEstadoRede.setText("");
        }

        atualizarDestaquesPecasMoveis();
    }

    /**
     * Mostra uma mensagem indicando que o jogador local deve aguardar o adversário.
     */
    private void indicarEsperaAdversario() {
        if ("rede".equals(DadosGlobais.modoJogo)) {
            labelEstadoRede.setText("À espera do adversário...");
        }
    }

    /**
     * Mostra uma mensagem indicando que foi recebida uma jogada do adversário.
     */
    private void indicarJogadaRecebida() {
        if ("rede".equals(DadosGlobais.modoJogo)) {
            labelEstadoRede.setText("Jogada recebida. A tua vez.");
        }
    }

    /**
     * Verifica se uma peça pertence ao jogador local em modo de rede.
     *
     * @param peca peça a verificar
     * @return true se a peça puder ser jogada neste computador
     */
    private boolean pecaPertenceAoJogadorLocal(Peca peca) {
        if (!"rede".equals(DadosGlobais.modoJogo)) {
            return true;
        }

        if (peca == null || DadosGlobais.nomeJogadorLocal == null) {
            return false;
        }

        return peca.getDono().getNome().equals(DadosGlobais.nomeJogadorLocal);
    }

    /**
     * Indica se é a vez do jogador local jogar.
     *
     * @return true em jogo local ou quando o jogador local tem o turno em rede
     */
    private boolean jogadorLocalTemTurno() {
        if (!"rede".equals(DadosGlobais.modoJogo)) {
            return true;
        }

        if (DadosGlobais.nomeJogadorLocal == null) {
            return false;
        }

        return jogo.getJogadorAtual().getNome().equals(DadosGlobais.nomeJogadorLocal);
    }

    /**
     * Configura a área de conversa, ativando-a apenas no modo de rede.
     */
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
        adicionarMensagemConversa("Conversa iniciada.");
        campoMensagemConversa.setDisable(false);
        btnEnviarMensagemConversa.setDisable(false);
    }

    /**
     * Envia a mensagem escrita pelo jogador para a conversa em rede.
     */
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
        adicionarMensagemConversa("Eu: " + texto);
        campoMensagemConversa.clear();
    }

    /**
     * Envia uma mensagem de conversa pela ligação de rede ativa.
     *
     * @param mensagem mensagem já formatada para o protocolo da conversa
     */
    private void enviarMensagemConversaRede(String mensagem) {
        if (DadosGlobais.servidorRede != null && DadosGlobais.servidorRede.isClienteLigado()) {
            DadosGlobais.servidorRede.enviarMensagem(mensagem);
        }

        if (DadosGlobais.clienteRede != null && DadosGlobais.clienteRede.isLigado()) {
            DadosGlobais.clienteRede.enviarMensagem(mensagem);
        }
    }

    /**
     * Envia uma mensagem de desistência ao adversário em modo de rede.
     */
    private void enviarDesistenciaRede() {
        if (!"rede".equals(DadosGlobais.modoJogo)) {
            return;
        }

        if (DadosGlobais.servidorRede != null && DadosGlobais.servidorRede.isClienteLigado()) {
            DadosGlobais.servidorRede.enviarMensagem("DESISTENCIA");
        }

        if (DadosGlobais.clienteRede != null && DadosGlobais.clienteRede.isLigado()) {
            DadosGlobais.clienteRede.enviarMensagem("DESISTENCIA");
        }
    }

    /**
     * Trata a desistência do adversário, fecha a rede e mostra o ecrã final.
     */
    private void adversarioDesistiu() {
        receberJogadasRede = false;
        DadosGlobais.fecharLigacoesRedeAssincrono();
        DadosGlobais.vencedor = "Parabéns! É o vencedor, o seu adversário desistiu.";
        ScreenManager.show("/fxml/FimJogo.fxml");
    }

    /**
     * Recebe uma mensagem de conversa e apresenta-a na lista.
     *
     * @param mensagem mensagem no formato "CHAT|texto"
     */
    private void receberMensagemConversa(String mensagem) {
        String texto = mensagem.substring("CHAT|".length());

        if (!texto.isEmpty()) {
            adicionarMensagemConversa("Adversário: " + texto);
        }
    }

    /**
     * Adiciona uma mensagem à lista visual da conversa.
     *
     * @param mensagem texto a adicionar à conversa
     */
    private void adicionarMensagemConversa(String mensagem) {
        listaMensagensConversa.getItems().add(mensagem);

        Platform.runLater(() -> {
            listaMensagensConversa.scrollTo(listaMensagensConversa.getItems().size() - 1);
        });
    }

    /**
     * Prepara o texto da conversa para não interferir com o separador do protocolo.
     *
     * @param texto texto introduzido pelo jogador
     * @return texto normalizado para envio pela rede
     */
    private String prepararTextoConversa(String texto) {
        return texto.replace("|", "/");
    }

    /**
     * Configura listeners para manter o tabuleiro ajustado ao tamanho disponível.
     */
    private void configurarTabuleiroResponsivo() {
        painelTabuleiro.widthProperty().addListener((obs, antigo, novo) -> atualizarEscalaTabuleiro());
        painelTabuleiro.heightProperty().addListener((obs, antigo, novo) -> atualizarEscalaTabuleiro());
        Platform.runLater(() -> atualizarEscalaTabuleiro());
    }

    /**
     * Atualiza a escala visual do tabuleiro para caber no painel disponível.
     */
    private void atualizarEscalaTabuleiro() {
        double escalaLargura = painelTabuleiro.getWidth() / 500;
        double escalaAltura = painelTabuleiro.getHeight() / 500;
        double escala = Math.min(escalaLargura, escalaAltura);

        if (escala <= 0) {
            escala = 1;
        }

        tabuleiro.setScaleX(escala);
        tabuleiro.setScaleY(escala);
    }

    /**
     * Verifica se um jogador tem pelo menos uma peça com movimentos válidos.
     *
     * @param jog jogador a verificar
     * @return true se existir pelo menos um movimento possível
     */
    private boolean jogadorTemMovimentos(Jogador jog) {
        for (Peca p : jog.getPecas()) {
            if (!jogo.obterMovimentosValidos(p).isEmpty()) {
                return true;
            }
        }
        return false;
    }

    // -------------------------------------------------------------------------
    // BOTÕES LATERAIS
    // -------------------------------------------------------------------------
    /*@FXML
    private void abrirDefinicoes() {
        DadosGlobais.ecrãAnterior = "/fxml/Jogo.fxml";
        abrirPopupDefinicoes();

    }*/

    /**
     * Guarda a partida atual num ficheiro escolhido pelo utilizador.
     */
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

    /**
     * Pede confirmação para voltar ao menu inicial e termina a partida atual.
     */
    @FXML
    private void mostrarMenuInicial() {

        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("Confirmar saída");
        alerta.setHeaderText("Deseja realmente voltar ao menu?");
        alerta.setContentText("O jogo atual será perdido.");

        ButtonType sim = new ButtonType("Sim");
        ButtonType nao = new ButtonType("Não", ButtonBar.ButtonData.CANCEL_CLOSE);

        alerta.getButtonTypes().setAll(sim, nao);

        alerta.showAndWait().ifPresent(resposta -> {
            if (resposta == sim) {
                // Se estiver em modo rede, enviar desistência
                if ("rede".equals(DadosGlobais.modoJogo)) {
                    enviarDesistenciaRede();
                }

                receberJogadasRede = false;
                DadosGlobais.limparJogoAtualAssincrono();
                ScreenManager.show("/fxml/MenuInicial.fxml");
            }
        });
    }

   /* private void abrirPopupDefinicoes() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Parametros.fxml"));
            Parent root = loader.load();

            Stage popup = new Stage();
            popup.setTitle("Definições");
            popup.setScene(new Scene(root));
            popup.initModality(Modality.APPLICATION_MODAL); // bloqueia o jogo
            popup.initOwner(labelJogadorAtual.getScene().getWindow()); // janela pai

            popup.showAndWait(); // espera até fechar

            aplicarTema();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void aplicarTema() {
        if (labelJogadorAtual.getScene() == null) {
            return;
        }

        var root = labelJogadorAtual.getScene().getRoot();

        root.getStyleClass().remove("tema-claro");
        root.getStyleClass().remove("tema-escuro");

        String tema = DadosGlobais.temaAtual;

        if (tema != null && tema.toLowerCase().contains("claro")) {
            root.getStyleClass().add("tema-claro");
        } else {
            root.getStyleClass().add("tema-escuro");
        }
    }*/

}
