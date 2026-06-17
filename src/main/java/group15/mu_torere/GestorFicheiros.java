package group15.mu_torere;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;

/**
 * Classe responsável por guardar e carregar uma partida de Mu Torere.
 *
 * O ficheiro usa texto simples, uma informação por linha.
 */
public class GestorFicheiros {

    /**
     * Cria o gestor de ficheiros.
     */
    public GestorFicheiros() {
    }

    /**
     * Guarda o estado atual do jogo num ficheiro.
     *
     * @param jogo jogo a guardar
     * @param ficheiro ficheiro escolhido pelo utilizador
     */
    public static void guardarJogo(Jogo jogo, File ficheiro) {
        PrintWriter pw = null;

        try {
            pw = new PrintWriter(new FileWriter(ficheiro));

            pw.println("MU_TORERE_1");
            pw.println(DadosGlobais.modoJogo);
            pw.println(DadosGlobais.ipServidor == null ? "" : DadosGlobais.ipServidor);
            pw.println(jogo.getJogador1().getNome());
            pw.println(jogo.getJogador1().getCor());
            pw.println(jogo.getJogador2().getNome());
            pw.println(jogo.getJogador2().getCor());
            pw.println(jogo.getJogadorAtual().getNome());
            pw.println(juntarPosicoes(jogo.getPosicoesDoJogador(jogo.getJogador1())));
            pw.println(juntarPosicoes(jogo.getPosicoesDoJogador(jogo.getJogador2())));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (pw != null) {
                pw.close();
            }
        }
    }

    /**
     * Carrega uma partida guardada num ficheiro.
     *
     * @param ficheiro ficheiro escolhido pelo utilizador
     * @return jogo carregado, ou null se existir erro
     */
    public static Jogo carregarJogo(File ficheiro) {
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(ficheiro));

            String versao = br.readLine();
            if (!"MU_TORERE_1".equals(versao)) {
                return null;
            }

            DadosGlobais.modoJogo = br.readLine();
            DadosGlobais.ipServidor = br.readLine();

            String nome1 = br.readLine();
            String cor1 = br.readLine();
            String nome2 = br.readLine();
            String cor2 = br.readLine();
            String jogadorAtual = br.readLine();
            int[] posicoesJogador1 = separarPosicoes(br.readLine());
            int[] posicoesJogador2 = separarPosicoes(br.readLine());

            return new Jogo(nome1, nome2, cor1, cor2, jogadorAtual, posicoesJogador1, posicoesJogador2);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Converte um array de posições para texto separado por vírgulas.
     *
     * @param posicoes identificadores das posições a converter
     * @return texto no formato "id,id,id"
     */
    private static String juntarPosicoes(int[] posicoes) {
        String texto = "";

        for (int i = 0; i < posicoes.length; i++) {
            if (i > 0) {
                texto += ",";
            }
            texto += posicoes[i];
        }

        return texto;
    }

    /**
     * Converte texto separado por vírgulas num array de posições.
     *
     * @param texto texto no formato "id,id,id"
     * @return array com os identificadores das posições
     */
    private static int[] separarPosicoes(String texto) {
        String[] partes = texto.split(",");
        int[] posicoes = new int[partes.length];

        for (int i = 0; i < partes.length; i++) {
            posicoes[i] = Integer.parseInt(partes[i]);
        }

        return posicoes;
    }
}
