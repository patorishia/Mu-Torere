/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package group15.mu_torere;

import java.util.List;

/**
 *
 * @author patri
 */
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
     * Cria o tabuleiro, os jogadores e coloca as peças nas posições iniciais.
     * @param nome1 nome do primeiro jogador
     * @param nome2 nome do segundo jogador
     */
    public Jogo(String nome1, String nome2) {
        tabuleiro = new Tabuleiro();

        // Criação dos jogadores
        jogador1 = new Jogador(nome1, "escuro");
        jogador2 = new Jogador(nome2, "claro");

        // O jogador 1 começa
        jogadorAtual = jogador1;

        // Colocar as peças nas posições iniciais
        inicializarPecas();
    }

    /**
     * Coloca as peças dos jogadores nas posições iniciais do tabuleiro.
     * Jogador 1: posições 0, 1, 2, 3
     * Jogador 2: posições 4, 5, 6, 7
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
     * Devolve a lista de posições para onde uma peça pode mover,
     * de acordo com as regras:
     *  - só pode mover para posições adjacentes
     *  - a posição de destino tem de estar vazia
     * @param peca
     * @return 
     */
    public List<Posicao> obterMovimentosValidos(Peca peca) {
        return peca.getPosicaoAtual().getAdjacentes()
                .stream()
                .filter(pos -> !pos.estaOcupada())
                .toList();
    }

    /**
     * Verifica se um movimento é válido para uma dada peça e posição de destino.
     * @param peca
     * @param destino
     * @return 
     */
    public boolean validarMovimento(Peca peca, Posicao destino) {
        return obterMovimentosValidos(peca).contains(destino);
    }

    /**
     * Executa um movimento, se for válido, e troca o turno do jogador.
     * @param peca
     * @param destino
     */
    public void executarMovimento(Peca peca, Posicao destino) {
        if (validarMovimento(peca, destino)) {
            peca.moverPara(destino);
            alternarTurno();
        }
    }

    /**
     * Alterna o jogador atual (troca entre jogador1 e jogador2).
     */
    public void alternarTurno() {
        jogadorAtual = (jogadorAtual == jogador1) ? jogador2 : jogador1;
    }

    /**
     * Devolve o jogador que tem o turno atual.
     * @return 
     */
    public Jogador getJogadorAtual() {
        return jogadorAtual;
    }
}
