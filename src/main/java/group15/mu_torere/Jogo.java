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
        int[] posicoesClaras = {4, 5, 6, 7};
        int[] posicoesEscuras = {0, 1, 2, 3};
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
}
