/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package group15.mu_torere;

import java.io.*;
import java.util.Arrays;


/**
 * Classe responsável por guardar e carregar o estado completo do jogo Mu Torere.
 * 
 * O ficheiro guardado contém, por ordem:
 *  1. turno atual (1 ou 2)
 *  2. array das posições [0..8] com:
 *        0 = vazio
 *        1 = peça do jogador 1
 *        2 = peça do jogador 2
 *  3. nome do jogador 1
 *  4. cor do jogador 1
 *  5. nome do jogador 2
 *  6. cor do jogador 2
 */
public class GestorFicheiros {

    /**
     * Guarda o estado atual do jogo num ficheiro.
     *
     * @param jogo     objeto Jogo contendo todo o estado atual
     * @param ficheiro ficheiro onde o estado será guardado
     */
    public static void guardarJogo(Jogo jogo, File ficheiro) {
        try (PrintWriter pw = new PrintWriter(ficheiro)) {

            // 1. Guardar o turno atual
            pw.println(jogo.getTurno());

            // 2. Guardar o estado das posições
            pw.println(Arrays.toString(jogo.getPosicoes()));

            // 3. Guardar nome e cor do jogador 1
            pw.println(jogo.getJogador1().getNome());
            pw.println(jogo.getJogador1().getCor());

            // 4. Guardar nome e cor do jogador 2
            pw.println(jogo.getJogador2().getNome());
            pw.println(jogo.getJogador2().getCor());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Carrega o estado do jogo a partir de um ficheiro previamente guardado.
     *
     * @param ficheiro ficheiro contendo o estado guardado
     * @return objeto Jogo reconstruído com o estado exato do ficheiro
     */
    public static Jogo carregarJogo(File ficheiro) {
        try (BufferedReader br = new BufferedReader(new FileReader(ficheiro))) {

            // 1. Ler turno
            int turno = Integer.parseInt(br.readLine());

            // 2. Ler array de posições
            int[] posicoes = parseArray(br.readLine());

            // 3. Ler nome e cor do jogador 1
            String nome1 = br.readLine();
            String cor1 = br.readLine();

            // 4. Ler nome e cor do jogador 2
            String nome2 = br.readLine();
            String cor2 = br.readLine();

            // Criar jogo reconstruído com o novo construtor
            return new Jogo(turno, posicoes, nome1, cor1, nome2, cor2);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Converte uma linha no formato "[1, 0, 2, 1, ...]" para um array int[].
     *
     * @param linha linha lida do ficheiro
     * @return array de inteiros representando o estado das posições
     */
    private static int[] parseArray(String linha) {
        linha = linha.replace("[", "").replace("]", "");
        String[] partes = linha.split(",");
        int[] arr = new int[partes.length];

        for (int i = 0; i < partes.length; i++)
            arr[i] = Integer.parseInt(partes[i].trim());

        return arr;
    }
}
