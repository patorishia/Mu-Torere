/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package group15.mu_torere;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author patri
 */
/**
 * Representa o tabuleiro do Mu Torere.
 * O tabuleiro tem 9 posições:
 *  - 8 posições exteriores (kewai) em círculo: 0 a 7
 *  - 1 posição central (putahi): 8
 *
 * As adjacências são definidas de acordo com as regras do jogo.
 */
public class Tabuleiro {

    // Lista de todas as posições do tabuleiro
    private List<Posicao> posicoes;

    /**
     * Construtor do tabuleiro.
     * Cria as 9 posições e define as adjacências entre elas.
     */
    public Tabuleiro() {
        posicoes = new ArrayList<>();

        // Criar 9 posições (0 a 8)
        for (int i = 0; i < 9; i++) {
            posicoes.add(new Posicao(i));
        }

        definirAdjacencias();
    }

    /**
     * Define as posições adjacentes de cada casa do tabuleiro.
     *  - As 8 posições exteriores formam um círculo
     *  - Todas as exteriores ligam ao centro (posição 8)
     *  - O centro liga a todas as exteriores
     */
    private void definirAdjacencias() {
        // Posições 0 a 7 (kewai) em círculo
        for (int i = 0; i < 8; i++) {
            Posicao atual = posicoes.get(i);
            Posicao anterior = posicoes.get((i + 7) % 8); // posição anterior no círculo
            Posicao seguinte = posicoes.get((i + 1) % 8); // posição seguinte no círculo

            // Ligações laterais (círculo)
            atual.adicionarAdjacente(anterior);
            atual.adicionarAdjacente(seguinte);

            // Ligação ao centro (putahi)
            atual.adicionarAdjacente(posicoes.get(8));
        }

        // Centro (putahi) liga a todas as exteriores
        Posicao centro = posicoes.get(8);
        for (int i = 0; i < 8; i++) {
            centro.adicionarAdjacente(posicoes.get(i));
        }
    }

    /**
     * Devolve a posição com o id indicado.
     * @param id identificador da posição (0 a 8)
     * @return 
     */
    public Posicao getPosicao(int id) {
        return posicoes.get(id);
    }
}
