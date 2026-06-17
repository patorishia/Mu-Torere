/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package group15.mu_torere;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa uma posição do tabuleiro de Mu Torere.
 * Cada posição pode ter uma peça e tem uma lista de posições adjacentes.
 */
public class Posicao {

    /** Identificador da posição no tabuleiro, entre 0 e 8. */
    private int id;

    /** Peça que está atualmente nesta posição, ou null se estiver vazia. */
    private Peca ocupante;
    
    /** Lista de posições adjacentes usadas para validar movimentos. */
    private List<Posicao> adjacentes;

    /**
     * Construtor da posição.
     * @param id identificador único da posição
     */
    public Posicao(int id) {
        this.id = id;
        this.adjacentes = new ArrayList<>();
    }

    /**
     * Devolve o id da posição.
     *
     * @return identificador da posição
     */
    public int getId() {
        return id;
    }

    /**
     * Indica se a posição está ocupada por uma peça.
     *
     * @return true se existir uma peça na posição, false caso contrário
     */
    public boolean estaOcupada() {
        return ocupante != null;
    }

    /**
     * Devolve a peça que ocupa esta posição (ou null se estiver vazia).
     *
     * @return peça ocupante, ou null se a posição estiver vazia
     */
    public Peca getOcupante() {
        return ocupante;
    }

    /**
     * Define a peça que ocupa esta posição.
     *
     * @param peca peça a colocar na posição, ou null para deixar a posição vazia
     */
    public void setOcupante(Peca peca) {
        this.ocupante = peca;
    }

    /**
     * Adiciona uma posição adjacente a esta.
     * Usado para definir as ligações do tabuleiro.
     *
     * @param p posição adjacente a adicionar
     */
    public void adicionarAdjacente(Posicao p) {
        adjacentes.add(p);
    }

    /**
     * Devolve a lista de posições adjacentes.
     *
     * @return lista de posições para onde é possível haver ligação
     */
    public List<Posicao> getAdjacentes() {
        return adjacentes;
    }
}
