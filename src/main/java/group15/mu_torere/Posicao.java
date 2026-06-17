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
 * Representa uma posição do tabuleiro de Mu Torere.
 * Cada posição pode ter uma peça e tem uma lista de posições adjacentes.
 */
public class Posicao {

    // Identificador da posição (0 a 8)
    private int id;

    // Peça que está atualmente nesta posição (null se estiver vazia)
    private Peca ocupante;
    
    // Lista de posições adjacentes (para validar movimentos)
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
     * @return 
     */
    public int getId() {
        return id;
    }

    /**
     * Indica se a posição está ocupada por uma peça.
     * @return 
     */
    public boolean estaOcupada() {
        return ocupante != null;
    }

    /**
     * Devolve a peça que ocupa esta posição (ou null se estiver vazia).
     * @return 
     */
    public Peca getOcupante() {
        return ocupante;
    }

    /**
     * Define a peça que ocupa esta posição.
     * @param peca
     */
    public void setOcupante(Peca peca) {
        this.ocupante = peca;
    }

    /**
     * Adiciona uma posição adjacente a esta.
     * Usado para definir as ligações do tabuleiro.
     * @param p
     */
    public void adicionarAdjacente(Posicao p) {
        adjacentes.add(p);
    }

    /**
     * Devolve a lista de posições adjacentes.
     * @return 
     */
    public List<Posicao> getAdjacentes() {
        return adjacentes;
    }
}
