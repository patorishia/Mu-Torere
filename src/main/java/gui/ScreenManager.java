/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gui;
import group15.mu_torere.DadosGlobais;
import java.net.URL;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Classe responsável por gerir a troca de ecrãs (FXML) na aplicação.
 * 
 * Esta classe mantém uma referência estática ao Stage principal e permite
 * carregar qualquer interface FXML através do método show().
 * 
 * Vantagens:
 *  - Centraliza a navegação entre ecrãs
 *  - Evita duplicação de código
 *  - Permite aplicar CSS global a toda a aplicação
 */
public class ScreenManager {

    // Stage principal da aplicação (a janela)
    private static Stage stage;

    /**
     * Define o Stage principal.
     * Este método deve ser chamado uma única vez no início da aplicação,
     * normalmente no método start() da classe Main.
     *
     * @param s Stage principal fornecido pelo JavaFX
     */
    public static void setStage(Stage s) {
        stage = s;
    }

    /**
     * Carrega um ficheiro FXML e apresenta-o no Stage principal.
     * 
     * @param fxml Caminho do ficheiro FXML dentro do projeto
     */
    public static void show(String fxml) {
        try {
            // Carrega o ficheiro FXML e cria a árvore de nodos correspondente
            Parent root = FXMLLoader.load(ScreenManager.class.getResource(fxml));
            aplicarTema(root);

            Scene scene = stage.getScene();

            if (scene == null) {
                scene = new Scene(root);
            } else {
                scene.setRoot(root);
            }

            // Aplica o ficheiro CSS global à Scene
            // Isto garante que TODOS os ecrãs partilham o mesmo estilo
            String css = ScreenManager.class.getResource("/fxml/interface.css").toExternalForm();

            if (!scene.getStylesheets().contains(css)) {
                scene.getStylesheets().add(css);
            }

            // Define a Scene no Stage principal
            stage.setScene(scene);

            // Mostra a janela (caso ainda não esteja visível)
            stage.show();
            URL cssUrl = ScreenManager.class.getResource("/fxml/interface.css");
System.out.println("CSS URL = " + cssUrl);


        } catch (Exception e) {
            // Em caso de erro, imprime a stack trace para facilitar debugging
            e.printStackTrace();
        }
    }

    private static void aplicarTema(Parent root) {
        root.getStyleClass().remove("tema-claro");
        root.getStyleClass().remove("tema-escuro");

        if ("Escuro".equals(DadosGlobais.temaAtual)) {
            root.getStyleClass().add("tema-escuro");
        } else {
            root.getStyleClass().add("tema-claro");
        }
    }
}
