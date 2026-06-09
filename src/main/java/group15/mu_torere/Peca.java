/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package group15.mu_torere;

/**
 *
 * @author patri
 */

/**
 * Representa uma peça de um jogador no jogo Mu Torere.
 * Cada peça pertence a um jogador e ocupa uma posição no tabuleiro.
 */
public class Peca {

    // Jogador a que esta peça pertence
    private Jogador dono;

    // Posição atual da peça no tabuleiro
    private Posicao posicaoAtual;

    /**
     * Construtor da peça.
     * @param dono jogador dono da peça
     * @param posicaoInicial posição onde a peça é colocada inicialmente
     */
    public Peca(Jogador dono, Posicao posicaoInicial) {
        this.dono = dono;
        this.posicaoAtual = posicaoInicial;
        posicaoInicial.setOcupante(this);
    }

    /**
     * Devolve o jogador dono da peça.
     * @return 
     */
    public Jogador getDono() {
        return dono;
    }

    /**
     * Devolve a posição atual da peça.
     * @return 
     */
    public Posicao getPosicaoAtual() {
        return posicaoAtual;
    }

    /**
     * Move a peça para uma nova posição.
     * Remove a peça da posição anterior e coloca-a na nova.
     * @param destino
     */
    public void moverPara(Posicao destino) {
        // Liberta a posição antiga
        posicaoAtual.setOcupante(null);

        // Ocupa a nova posição
        destino.setOcupante(this);

        // Atualiza a referência interna
        posicaoAtual = destino;
    }

    public void setCenterX(double centerX) {
       
    }

    public void setCenterY(double centerY) {
       
    }
}

