/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package mutorere;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;

/**
 * FXML Controller class
 *
 * @author patri
 */
public class InterfaceController implements Initializable {

    @FXML
    private BorderPane menuInicial;
    @FXML
    private Button btnJogoLocal;
    @FXML
    private Button btnJogoRede;
    @FXML
    private Button btnDefinicoes;
    @FXML
    private Button btnSair;
    @FXML
    private BorderPane inserirJogadores;
    @FXML
    private Button btnContinuarInserir;
    @FXML
    private BorderPane jogoLocal;
    @FXML
    private Pane tabuleiro;
    @FXML
    private Circle casaCentro;
    @FXML
    private Circle casa0;
    @FXML
    private Circle casa1;
    @FXML
    private Circle casa2;
    @FXML
    private Circle casa3;
    @FXML
    private Circle casa4;
    @FXML
    private Circle casa5;
    @FXML
    private Circle casa6;
    @FXML
    private Circle casa7;
    @FXML
    private Circle pecaClara1;
    @FXML
    private Circle pecaClara2;
    @FXML
    private Circle pecaClara3;
    @FXML
    private Circle pecaClara4;
    @FXML
    private Circle pecaEscura1;
    @FXML
    private Circle pecaEscura2;
    @FXML
    private Circle pecaEscura3;
    @FXML
    private Circle pecaEscura4;
    @FXML
    private Button btnDefinicoesJogoLocal;
    @FXML
    private Button btnVoltarMenuJogoLocal;
    @FXML
    private BorderPane jogoRede;
    @FXML
    private Button btnConectarRede;
    @FXML
    private Button btnVoltarRede;
    @FXML
    private BorderPane espera;
    @FXML
    private Label labelIPServidor;
    @FXML
    private Button btnCancelarEspera;
    @FXML
    private BorderPane fimJogo;
    @FXML
    private Button btnVoltarMenuFim;
    @FXML
    private BorderPane parametros;
    @FXML
    private ComboBox<?> temaCombo;
    @FXML
    private Button btnGuardarParametros;
    @FXML
    private Button btnVoltarParametros;
    @FXML
    private BorderPane roleta;
    @FXML
    private Label labelResultado;
    @FXML
    private Button btnContinuarRoleta;
    @FXML
    private Label seta;
    @FXML
    private BorderPane escolherCor;
    @FXML
    private Button btnConfirmarCor;
    @FXML
    private Circle circuloClara;
    @FXML
    private Circle circuloEscura;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void abrirInserirJogadores(ActionEvent event) {
    }

    @FXML
    private void abrirJogoRede(ActionEvent event) {
    }

    @FXML
    private void abrirDefinicoes(ActionEvent event) {
    }

    @FXML
    private void sairJogo(ActionEvent event) {
    }

    @FXML
    private void abrirRoleta(ActionEvent event) {
    }

    @FXML
    private void mostrarMenuInicial(ActionEvent event) {
    }

    @FXML
    private void abrirEscolherCor(ActionEvent event) {
    }

    @FXML
    private void abrirJogoLocal(ActionEvent event) {
    }

    @FXML
    private void selecionarClara(MouseEvent event) {
    }

    @FXML
    private void selecionarEscura(MouseEvent event) {
    }
    
}
