/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package gui;

import group15.mu_torere.DadosGlobais;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;


/**
 * Controlador do ecrã Inserir Jogadores.
 * Lê os nomes, valida e avança para o ecrã da roleta.
 */
public class InserirJogadoresController {

    @FXML private TextField txtJogador1; // Campo do Jogador 1
    @FXML private TextField txtJogador2; // Campo do Jogador 2

    /**
     * Ação do botão "Continuar".
     * Valida os nomes, guarda-os e muda para o ecrã da roleta.
     */
    @FXML
    private void abrirRoleta(ActionEvent event) {

        String nome1 = txtJogador1.getText().trim();
        String nome2 = txtJogador2.getText().trim();

        // Validação
        if (nome1.isBlank() || nome2.isBlank()) {
            Alert alerta = new Alert(Alert.AlertType.WARNING);
            alerta.setTitle("Nomes inválidos");
            alerta.setHeaderText(null);
            alerta.setContentText("Por favor, preenche os nomes dos dois jogadores.");
            alerta.showAndWait();
            return;
        }

        // Guardar nomes para o próximo ecrã
        DadosGlobais.nomeJogador1 = nome1;
        DadosGlobais.nomeJogador2 = nome2;

        // Avançar para a roleta
        ScreenManager.show("/fxml/Roleta.fxml");
    }
    
    /**
     * Ação do botão "Voltar".
     * Regressa ao menu inicial.
     */
    @FXML
    private void voltar(ActionEvent event) {
        ScreenManager.show("/fxml/MenuInicial.fxml");
    }
}

