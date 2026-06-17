/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package gui;

import group15.mu_torere.ClienteRede;
import group15.mu_torere.DadosGlobais;
import group15.mu_torere.ServidorRede;
import java.net.InetAddress;
import java.net.UnknownHostException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * Controlador do ecrã Inserir IP em Jogo de Rede.
 * Responsável por:
 *  - criar um servidor para o adversário entrar
 *  - ler o IP introduzido pelo jogador
 *  - avançar para o ecrã de espera
 */
public class InserirIPController {

    /**
     * Cria o controlador do ecrã de inserção de IP.
     */
    public InserirIPController() {
    }

    /** Campo onde o jogador cliente introduz o IP do servidor. */
    @FXML private TextField campoIP;

    /** Label onde é apresentado o IP local quando este jogador cria o servidor. */
    @FXML private Label labelIPLocal;

    /**
     * Cria um servidor TCP e mostra o IP que o outro jogador deve usar.
     */
    @FXML
    private void criarServidor() {
        DadosGlobais.modoJogo = "rede";
        DadosGlobais.clienteRede = null;

        if (DadosGlobais.servidorRede != null) {
            DadosGlobais.servidorRede.fechar();
        }

        DadosGlobais.servidorRede = new ServidorRede(DadosGlobais.portoServidor);
        DadosGlobais.servidorRede.start();

        try {
            DadosGlobais.ipServidor = InetAddress.getLocalHost().getHostAddress();
            labelIPLocal.setText("IP do servidor: " + DadosGlobais.ipServidor);
        } catch (UnknownHostException e) {
            DadosGlobais.ipServidor = "nao encontrado";
            labelIPLocal.setText("IP do servidor: nao encontrado");
        }

        ScreenManager.show("/fxml/Espera.fxml");
    }

    /**
     * Tenta conectar ao servidor usando o IP introduzido.
     */
    @FXML
    private void conectar() {

        String ip = campoIP.getText().trim();

        if (ip.isEmpty()) {
            campoIP.setPromptText("Introduza um IP válido");
            return;
        }

        // Guardar IP globalmente para o ecrã de espera
        DadosGlobais.ipServidor = ip;
        DadosGlobais.modoJogo = "rede";
        DadosGlobais.servidorRede = null;

        if (DadosGlobais.clienteRede != null) {
            DadosGlobais.clienteRede.fechar();
        }

        DadosGlobais.clienteRede = new ClienteRede(ip, DadosGlobais.portoServidor);
        DadosGlobais.clienteRede.start();

        // Avançar para o ecrã de espera
        ScreenManager.show("/fxml/Espera.fxml");
    }

    /**
     * Regressa ao menu inicial e limpa os dados da partida atual.
     */
    @FXML
    private void mostrarMenuInicial() {
        DadosGlobais.limparJogoAtual();
        ScreenManager.show("/fxml/MenuInicial.fxml");
    }
}
