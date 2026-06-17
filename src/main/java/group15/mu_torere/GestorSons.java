package group15.mu_torere;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * Classe responsavel por tocar os sons principais do jogo.
 *
 * @author Grupo 15
 * @version 1.0
 * @since 2026
 */
public class GestorSons {

    /**
     * Cria o gestor de sons.
     */
    public GestorSons() {
    }

    /** Caminho do recurso de som tocado quando uma jogada válida é feita. */
    private static final String SOM_JOGADA = "/sons/jogada.wav";
    /** Caminho do recurso de som tocado quando ocorre uma ação inválida. */
    private static final String SOM_ERRO = "/sons/erro.wav";
    /** Caminho do recurso de som tocado quando a partida termina com vitória. */
    private static final String SOM_VITORIA = "/sons/vitoria.wav";
    /** MediaPlayer atualmente usado para tocar o último som pedido. */
    private static MediaPlayer playerAtual;

    /**
     * Toca o som de uma jogada valida.
     */
    public static void tocarJogada() {
        tocar(SOM_JOGADA);
    }

    /**
     * Toca o som de erro.
     */
    public static void tocarErro() {
        tocar(SOM_ERRO);
    }

    /**
     * Toca o som de vitoria.
     */
    public static void tocarVitoria() {
        tocar(SOM_VITORIA);
    }

    /**
     * Toca o ficheiro de som indicado, respeitando a definição global de som.
     *
     * @param caminho caminho do recurso de áudio dentro de resources
     */
    private static void tocar(String caminho) {
        if (!DadosGlobais.somAtivo) {
            return;
        }

        try {
            Media media = new Media(GestorSons.class.getResource(caminho).toExternalForm());
            playerAtual = new MediaPlayer(media);
            playerAtual.play();
        } catch (Exception e) {
            System.out.println("Nao foi possivel tocar o som: " + caminho);
        }
    }
}
