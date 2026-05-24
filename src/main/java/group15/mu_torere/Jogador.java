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
 * Representa um jogador do Mu Torere.
 * Cada jogador tem um nome, uma cor e um conjunto de peças.
 */
public class Jogador {

    // Nome do jogador (ex: "Jogador 1")
    private String nome;

    // Cor das peças do jogador (ex: "escuro" ou "claro")
    private String cor;

    // Lista de peças que pertencem a este jogador
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
     * @param p
     */
    public void adicionarPeca(Peca p) {
        pecas.add(p);
    }

    /**
     * Devolve a lista de peças do jogador.
     * @return 
     */
    public List<Peca> getPecas() {
        return pecas;
    }

    /**
     * Devolve o nome do jogador.
     * @return 
     */
    public String getNome() {
        return nome;
    }

    /**
     * Devolve a cor das peças do jogador.
     * @return 
     */
    public String getCor() {
        return cor;
    }
}

