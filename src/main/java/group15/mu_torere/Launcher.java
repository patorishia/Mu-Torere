package group15.mu_torere;

/**
 * Classe de arranque alternativa da aplicação.
 *
 * Permite iniciar o servidor por argumento de linha de comandos ou arrancar
 * normalmente a interface JavaFX.
 */
public class Launcher {

    /**
     * Cria o launcher da aplicação.
     */
    public Launcher() {
    }

    /** Porto usado quando o launcher é executado em modo servidor. */
    private static final int PORTO_SERVIDOR = 5000;

    /**
     * Ponto de entrada alternativo da aplicação.
     *
     * @param args argumentos de linha de comandos; "servidor" inicia apenas o servidor
     */
    public static void main(String[] args) {
        if (args.length > 0 && args[0].equals("servidor")) {
            ServidorRede servidorRede = new ServidorRede(PORTO_SERVIDOR);
            servidorRede.start();
            return;
        }

        // Isto vai chamar o teu jogo original contornando o bloqueio de módulos
        Mu_Torere.main(args);
    }
}
