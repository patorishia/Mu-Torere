/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package group15.mu_torere;

import java.util.List;


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
     */
    public Jogo(String nome1, String nome2, String corJog1, String corJog2) {

        tabuleiro = new Tabuleiro();

        // Criação dos jogadores com as cores escolhidas
        jogador1 = new Jogador(nome1, corJog1);
        jogador2 = new Jogador(nome2, corJog2);

        // Jogador 1 começa sempre
        jogadorAtual = jogador1;

        // Colocar as peças nas posições iniciais
        inicializarPecas();
    }

    /**
     * Coloca as peças dos jogadores nas posições iniciais do tabuleiro.
     * Jogador 1 → posições 0,1,2,3
     * Jogador 2 → posições 4,5,6,7
     */
    private void inicializarPecas() {

        // Peças do jogador 1
        for (int i = 0; i < 4; i++) {
            Peca p = new Peca(jogador1, tabuleiro.getPosicao(i));
            jogador1.adicionarPeca(p);
        }

        // Peças do jogador 2
        for (int i = 4; i < 8; i++) {
            Peca p = new Peca(jogador2, tabuleiro.getPosicao(i));
            jogador2.adicionarPeca(p);
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
     * Verifica se um movimento é válido segundo as regras:
     *  - a posição de destino tem de estar vazia
     *  - a posição de destino tem de ser adjacente à posição atual da peça
     *
     * @param peca peça a mover
     * @param destino posição de destino
     * @return true se o movimento for permitido
     */
    public boolean movimentoValido(Peca peca, Posicao destino) {

        // destino tem de estar livre
        if (destino.estaOcupada()) {
            return false;
        }

        // destino tem de ser adjacente
        List<Posicao> adj = peca.getPosicaoAtual().getAdjacentes();
        return adj.contains(destino);
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
