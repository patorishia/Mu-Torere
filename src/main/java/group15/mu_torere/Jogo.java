/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package group15.mu_torere;

import java.util.List;
import java.util.ArrayList;


/**
 * Classe que representa o jogo de Mu Torere.
 * Gere:
 *  - o tabuleiro
 *  - os dois jogadores
 *  - as peças
 *  - o jogador atual
 *  - a validação e execução de movimentos
 */
public class Jogo {

    private Jogador jogador1;
    private Jogador jogador2;
    private Jogador jogadorAtual;
    private Tabuleiro tabuleiro;

    /**
     * Construtor do jogo.
     * Agora recebe também as cores escolhidas no ecrã anterior.
     *
     * @param nome1 nome do primeiro jogador
     * @param nome2 nome do segundo jogador
     * @param corJog1 cor do jogador 1 ("claro" ou "escuro")
     * @param corJog2 cor do jogador 2 ("claro" ou "escuro")
     * @param nomeJogadorInicial nome do jogador que começa a partida
     */
    public Jogo(String nome1, String nome2, String corJog1, String corJog2, String nomeJogadorInicial) {

        tabuleiro = new Tabuleiro();

        // Criação dos jogadores com as cores escolhidas
        jogador1 = new Jogador(nome1, corJog1);
        jogador2 = new Jogador(nome2, corJog2);

        // O jogador sorteado começa a partida
        if (nomeJogadorInicial != null && nomeJogadorInicial.equals(nome2)) {
            jogadorAtual = jogador2;
        } else {
            jogadorAtual = jogador1;
        }

        // Colocar as peças nas posições iniciais
        inicializarPecas();
    }

    /**
     * Construtor usado para reconstruir uma partida guardada em ficheiro.
     *
     * @param nome1 nome do primeiro jogador
     * @param nome2 nome do segundo jogador
     * @param corJog1 cor do primeiro jogador
     * @param corJog2 cor do segundo jogador
     * @param nomeJogadorAtual nome do jogador que tem o turno
     * @param posicoesJogador1 posições das peças do jogador 1
     * @param posicoesJogador2 posições das peças do jogador 2
     */
    public Jogo(String nome1, String nome2, String corJog1, String corJog2,
            String nomeJogadorAtual, int[] posicoesJogador1, int[] posicoesJogador2) {

        tabuleiro = new Tabuleiro();
        jogador1 = new Jogador(nome1, corJog1);
        jogador2 = new Jogador(nome2, corJog2);

        adicionarPecasGuardadas(jogador1, posicoesJogador1);
        adicionarPecasGuardadas(jogador2, posicoesJogador2);

        if (nomeJogadorAtual != null && nomeJogadorAtual.equals(nome2)) {
            jogadorAtual = jogador2;
        } else {
            jogadorAtual = jogador1;
        }
    }

    /**
     * Coloca as peças dos jogadores nas posições iniciais do tabuleiro.
     * As peças são colocadas nas casas onde a GUI mostra essa cor.
     */
    private void inicializarPecas() {
        adicionarPecasIniciais(jogador1);
        adicionarPecasIniciais(jogador2);
    }

    /**
     * Coloca as peças iniciais de um jogador de acordo com a sua cor.
     *
     * @param jogador jogador a quem pertencem as peças
     */
    private void adicionarPecasIniciais(Jogador jogador) {
        int[] posicoesClaras = {5, 6, 7, 0};
        int[] posicoesEscuras = {1, 2, 3, 4};
        int[] posicoesIniciais;

        if (jogador.getCor().equals("claro")) {
            posicoesIniciais = posicoesClaras;
        } else {
            posicoesIniciais = posicoesEscuras;
        }

        for (int i = 0; i < posicoesIniciais.length; i++) {
            Peca p = new Peca(jogador, tabuleiro.getPosicao(posicoesIniciais[i]));
            jogador.adicionarPeca(p);
        }
    }

    private void adicionarPecasGuardadas(Jogador jogador, int[] posicoes) {
        for (int i = 0; i < posicoes.length; i++) {
            Peca p = new Peca(jogador, tabuleiro.getPosicao(posicoes[i]));
            jogador.adicionarPeca(p);
        }
    }

    /**
     * Verifica se uma peça pertence ao jogador que tem o turno atual.
     *
     * @param p peça a verificar
     * @return true se a peça pertence ao jogador atual
     */
    public boolean ePecaDoJogadorAtual(Peca p) {
        return p.getDono() == jogadorAtual;
    }

    /**
     * Devolve todas as posições para onde uma peça pode mover.
     *
     * @param peca peça a analisar
     * @return lista de posições válidas
     */
    public List<Posicao> obterMovimentosValidos(Peca peca) {
        List<Posicao> movimentos = new ArrayList<>();
        List<Posicao> adjacentes = peca.getPosicaoAtual().getAdjacentes();

        for (Posicao destino : adjacentes) {
            if (movimentoValido(peca, destino)) {
                movimentos.add(destino);
            }
        }

        return movimentos;
    }
    
    /**
     * Verifica se um movimento é válido segundo as regras:
     *  - a peça tem de pertencer ao jogador atual
     *  - a posição de destino tem de estar vazia
     *  - a posição de destino tem de ser adjacente à posição atual da peça
     *  - uma peça exterior só pode entrar no centro se for kawhena
     *
     * @param peca peça a mover
     * @param destino posição de destino
     * @return true se o movimento for permitido
     */
    public boolean movimentoValido(Peca peca, Posicao destino) {

        if (peca == null || destino == null) {
            return false;
        }

        if (!ePecaDoJogadorAtual(peca)) {
            return false;
        }

        // destino tem de estar livre
        if (destino.estaOcupada()) {
            return false;
        }

        // destino tem de ser adjacente
        List<Posicao> adj = peca.getPosicaoAtual().getAdjacentes();
        if (!adj.contains(destino)) {
            return false;
        }

        // Movimento normal entre posições exteriores vizinhas
        if (peca.getPosicaoAtual().getId() != 8 && destino.getId() != 8) {
            return true;
        }

        // Movimento do centro para uma posição exterior livre
        if (peca.getPosicaoAtual().getId() == 8 && destino.getId() != 8) {
            return true;
        }

        // Regra do kawhena: só entra no centro se tocar numa peça adversária
        return eKawhena(peca);
    }

    /**
     * Verifica a regra do kawhena.
     * Uma peça é kawhena quando está numa posição exterior e tem pelo menos
     * uma peça adversária numa das duas posições exteriores vizinhas.
     *
     * @param peca peça a verificar
     * @return true se a peça for kawhena
     */
    private boolean eKawhena(Peca peca) {
        if (peca.getPosicaoAtual().getId() == 8) {
            return false;
        }

        List<Posicao> adjacentes = peca.getPosicaoAtual().getAdjacentes();

        for (Posicao posicao : adjacentes) {
            if (posicao.getId() != 8 && posicao.estaOcupada()) {
                Peca ocupante = posicao.getOcupante();

                if (ocupante.getDono() != peca.getDono()) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Executa um movimento válido:
     *  - move a peça no modelo
     *  - troca o turno
     *
     * @param peca peça a mover
     * @param destino posição de destino
     */
    public void fazerMovimento(Peca peca, Posicao destino) {
        peca.moverPara(destino);
        alternarTurno();
    }

    /**
     * Aplica uma jogada recebida pela rede.
     *
     * @param origem id da posição onde está a peça
     * @param destino id da posição para onde a peça vai
     * @return true se a jogada foi aplicada, false caso contrário
     */
    public boolean aplicarJogadaRede(int origem, int destino) {
        Peca peca = getPecaNaPosicao(origem);
        Posicao posicaoDestino = tabuleiro.getPosicao(destino);

        if (peca == null || posicaoDestino == null) {
            return false;
        }

        if (!movimentoValido(peca, posicaoDestino)) {
            return false;
        }

        fazerMovimento(peca, posicaoDestino);
        return true;
    }

    /**
     * Procura a peça que está numa posição do tabuleiro.
     *
     * @param idPosicao id da posição procurada
     * @return peça encontrada, ou null se a posição estiver vazia
     */
    public Peca getPecaNaPosicao(int idPosicao) {
        Posicao posicao = tabuleiro.getPosicao(idPosicao);

        if (posicao == null) {
            return null;
        }

        return posicao.getOcupante();
    }

    /**
     * Alterna o jogador atual (troca entre jogador1 e jogador2).
     */
    public void alternarTurno() {
        jogadorAtual = (jogadorAtual == jogador1) ? jogador2 : jogador1;
    }

    /**
     * @return jogador que tem o turno atual
     */
    public Jogador getJogadorAtual() {
        return jogadorAtual;
    }

    public Jogador getJogador1() {
        return jogador1;
    }

    public Jogador getJogador2() {
        return jogador2;
    }

    public Tabuleiro getTabuleiro() {
        return tabuleiro;
    }

    /**
     * Devolve as posições atuais das peças de um jogador.
     *
     * @param jogador jogador pretendido
     * @return ids das posições das suas peças
     */
    public int[] getPosicoesDoJogador(Jogador jogador) {
        int[] posicoes = new int[jogador.getPecas().size()];

        for (int i = 0; i < jogador.getPecas().size(); i++) {
            posicoes[i] = jogador.getPecas().get(i).getPosicaoAtual().getId();
        }

        return posicoes;
    }

    /**
     * Cria uma mensagem de texto com o estado atual do jogo para enviar pela rede.
     *
     * @return estado do jogo numa unica linha de texto
     */
    public String criarMensagemEstadoRede() {
        String mensagem = "ESTADO_JOGO";

        mensagem += "|" + jogador1.getNome();
        mensagem += "|" + jogador1.getCor();
        mensagem += "|" + jogador2.getNome();
        mensagem += "|" + jogador2.getCor();
        mensagem += "|" + jogadorAtual.getNome();
        mensagem += "|" + juntarPosicoes(getPosicoesDoJogador(jogador1));
        mensagem += "|" + juntarPosicoes(getPosicoesDoJogador(jogador2));

        return mensagem;
    }

    /**
     * Atualiza o jogo com uma mensagem de estado recebida pela rede.
     *
     * @param mensagem mensagem recebida pela rede
     * @return true se o estado foi aplicado, false caso contrário
     */
    public boolean aplicarMensagemEstadoRede(String mensagem) {
        String[] partes = mensagem.split("\\|");

        if (partes.length != 8 || !"ESTADO_JOGO".equals(partes[0])) {
            return false;
        }

        int[] posicoesJogador1 = separarPosicoes(partes[6]);
        int[] posicoesJogador2 = separarPosicoes(partes[7]);

        tabuleiro = new Tabuleiro();
        jogador1 = new Jogador(partes[1], partes[2]);
        jogador2 = new Jogador(partes[3], partes[4]);

        adicionarPecasGuardadas(jogador1, posicoesJogador1);
        adicionarPecasGuardadas(jogador2, posicoesJogador2);

        if (partes[5].equals(jogador2.getNome())) {
            jogadorAtual = jogador2;
        } else {
            jogadorAtual = jogador1;
        }

        return true;
    }

    private String juntarPosicoes(int[] posicoes) {
        String texto = "";

        for (int i = 0; i < posicoes.length; i++) {
            if (i > 0) {
                texto += ",";
            }

            texto += posicoes[i];
        }

        return texto;
    }

    private int[] separarPosicoes(String texto) {
        String[] partes = texto.split(",");
        int[] posicoes = new int[partes.length];

        for (int i = 0; i < partes.length; i++) {
            posicoes[i] = Integer.parseInt(partes[i]);
        }

        return posicoes;
    }
}
