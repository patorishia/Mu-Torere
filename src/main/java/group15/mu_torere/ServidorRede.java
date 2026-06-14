package group15.mu_torere;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Servidor TCP simples para permitir a ligacao de outro jogador pela rede.
 *
 * @author Grupo 15
 * @version 1.0
 * @since 2026
 */
public class ServidorRede extends Thread {

    private final int porto;
    private ServerSocket serverSocket;
    private Socket clienteSocket;
    private BufferedReader entrada;
    private PrintWriter saida;
    private boolean ligado;
    private boolean clienteLigado;
    private String ultimaMensagem;
    private int numeroMensagensRecebidas;

    /**
     * Cria um servidor associado a um porto.
     *
     * @param porto porto onde o servidor vai ficar a escutar ligacoes
     */
    public ServidorRede(int porto) {
        this.porto = porto;
        this.ligado = false;
        this.clienteLigado = false;
        this.ultimaMensagem = "";
        this.numeroMensagensRecebidas = 0;
    }

    /**
     * Inicia o servidor, espera por um cliente e recebe mensagens de texto.
     */
    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(porto);
            ligado = true;

            clienteSocket = serverSocket.accept();
            entrada = new BufferedReader(new InputStreamReader(clienteSocket.getInputStream()));
            saida = new PrintWriter(clienteSocket.getOutputStream(), true);
            clienteLigado = true;
            enviarMensagem("LIGACAO_ACEITE");

            String mensagem;
            while (ligado && (mensagem = entrada.readLine()) != null) {
                guardarMensagem(mensagem);
            }
        } catch (IOException e) {
            guardarMensagem("Erro no servidor: " + e.getMessage());
        } finally {
            fechar();
        }
    }

    /**
     * Envia uma mensagem para o cliente ligado ao servidor.
     *
     * @param mensagem texto a enviar
     */
    public synchronized void enviarMensagem(String mensagem) {
        if (saida != null) {
            saida.println(mensagem);
        }
    }

    /**
     * Envia o estado atual do jogo para o cliente.
     *
     * @param jogo jogo cujo estado vai ser enviado
     */
    public synchronized void enviarEstadoJogo(Jogo jogo) {
        if (jogo != null) {
            enviarMensagem(jogo.criarMensagemEstadoRede());
        }
    }

    /**
     * Envia uma jogada para o cliente.
     *
     * @param origem posição inicial da peça
     * @param destino posição final da peça
     */
    public synchronized void enviarJogada(int origem, int destino) {
        enviarMensagem("JOGADA|" + origem + "|" + destino);
    }

    /**
     * Devolve a ultima mensagem recebida pelo servidor.
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
     * Indica se o servidor esta ativo.
     *
     * @return true se o servidor estiver ativo, false caso contrario
     */
    public synchronized boolean isLigado() {
        return ligado;
    }

    /**
     * Indica se o servidor ja aceitou a ligacao de um cliente.
     *
     * @return true se existir cliente ligado, false caso contrario
     */
    public synchronized boolean isClienteLigado() {
        return clienteLigado;
    }

    /**
     * Fecha o servidor e os recursos de rede.
     */
    public synchronized void fechar() {
        ligado = false;
        clienteLigado = false;

        try {
            if (entrada != null) {
                entrada.close();
            }

            if (saida != null) {
                saida.close();
            }

            if (clienteSocket != null) {
                clienteSocket.close();
            }

            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            guardarMensagem("Erro ao fechar servidor: " + e.getMessage());
        }
    }

    private synchronized void guardarMensagem(String mensagem) {
        ultimaMensagem = mensagem;
        numeroMensagensRecebidas++;
    }
}
