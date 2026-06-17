/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package group15.mu_torere;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa um jogador do Mu Torere.
 * Cada jogador tem um nome, uma cor e um conjunto de peças.
 */
public class Jogador {

    /** Nome apresentado para identificar o jogador. */
    private String nome;

    /** Cor das peças do jogador, normalmente "escuro" ou "claro". */
    private String cor;

    /** Lista de peças que pertencem ao jogador. */
    private List<Peca> pecas;

    /**
     * Construtor do jogador.
     * @param nome nome do jogador
     * @param cor cor das peças do jogador
     */
    public Jogador(String nome, String cor) {
        this.nome = nome;
        this.cor = cor;
        this.pecas = new ArrayList<>();
    }

    /**
     * Adiciona uma peça à lista de peças do jogador.
     *
     * @param p peça a adicionar ao jogador
     */
    public void adicionarPeca(Peca p) {
        pecas.add(p);
    }

    /**
     * Devolve a lista de peças do jogador.
     *
     * @return lista de peças pertencentes ao jogador
     */
    public List<Peca> getPecas() {
        return pecas;
    }

    /**
     * Devolve o nome do jogador.
     *
     * @return nome do jogador
     */
    public String getNome() {
        return nome;
    }

    /**
     * Devolve a cor das peças do jogador.
     *
     * @return cor atribuída ao jogador
     */
    public String getCor() {
        return cor;
    }
    
}
