package group15.mu_torere;

public class Launcher {

    private static final int PORTO_SERVIDOR = 5000;

    public static void main(String[] args) {
        if (args.length > 0 && args[0].equals("servidor")) {
            ServidorRede servidorRede = new ServidorRede(PORTO_SERVIDOR);
            servidorRede.start();
        }

        // Isto vai chamar o teu jogo original contornando o bloqueio de módulos
        Mu_Torere.main(args);
    }
}
