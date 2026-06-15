package group15.mu_torere;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Cliente TCP simples para ligar ao servidor de outro jogador.
 *
 * @author Grupo 15
 * @version 1.0
 * @since 2026
 */
public class ClienteRede extends Thread {

    private final String ipServidor;
    private final int porto;
    private Socket socket;
    private BufferedReader entrada;
    private PrintWriter saida;
    private boolean ligado;
    private String ultimaMensagem;
    private int numeroMensagensRecebidas;

    /**
     * Cria um cliente de rede.
     *
     * @param ipServidor endereco IP do servidor
     * @param porto porto onde o servidor esta a escutar
     */
    public ClienteRede(String ipServidor, int porto) {
        this.ipServidor = ipServidor;
        this.porto = porto;
        this.ligado = false;
        this.ultimaMensagem = "";
        this.numeroMensagensRecebidas = 0;
    }

    /**
     * Liga ao servidor e fica a receber mensagens de texto.
     */
    @Override
    public void run() {
        try {
            socket = new Socket(ipServidor, porto);
            entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            saida = new PrintWriter(socket.getOutputStream(), true);
            ligado = true;

            String mensagem;
            while (ligado && (mensagem = entrada.readLine()) != null) {
                guardarMensagem(mensagem);
            }
        } catch (IOException e) {
            guardarMensagem("Erro no cliente: " + e.getMessage());
        } finally {
            fechar();
        }
    }

    /**
     * Envia uma mensagem para o servidor.
     *
     * @param mensagem texto a enviar
     */
    public synchronized void enviarMensagem(String mensagem) {
        if (saida != null) {
            saida.println(mensagem);
        }
    }

    /**
     * Envia o estado atual do jogo para o servidor.
     *
     * @param jogo jogo cujo estado vai ser enviado
     */
    public synchronized void enviarEstadoJogo(Jogo jogo) {
        if (jogo != null) {
            enviarMensagem(jogo.criarMensagemEstadoRede());
        }
    }

    /**
     * Envia uma jogada para o servidor.
     *
     * @param origem posição inicial da peça
     * @param destino posição final da peça
     */
    public synchronized void enviarJogada(int origem, int destino) {
        enviarMensagem("JOGADA|" + origem + "|" + destino);
    }

    /**
     * Devolve a ultima mensagem recebida.
     *
     * @return ultima mensagem recebida
     */
    public synchronized String getUltimaMensagem() {
        return ultimaMensagem;
    }

    /**
     * Devolve o número de mensagens recebidas.
     *
     * @return quantidade de mensagens recebidas
     */
    public synchronized int getNumeroMensagensRecebidas() {
        return numeroMensagensRecebidas;
    }

    /**
     * Indica se o cliente esta ligado.
     *
     * @return true se estiver ligado, false caso contrario
     */
    public synchronized boolean isLigado() {
        return ligado;
    }

    /**
     * Fecha a ligacao e os recursos de rede.
     */
    public synchronized void fechar() {
        ligado = false;

        try {
            if (entrada != null) {
                entrada.close();
            }

            if (saida != null) {
                saida.close();
            }

            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            guardarMensagem("Erro ao fechar cliente: " + e.getMessage());
        }
    }

    private synchronized void guardarMensagem(String mensagem) {
        ultimaMensagem = mensagem;
        numeroMensagensRecebidas++;
    }
}
