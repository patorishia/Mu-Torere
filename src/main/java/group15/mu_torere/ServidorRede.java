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

    /** Porto TCP onde o servidor fica à escuta de ligações. */
    private final int porto;
    /** Objeto usado para sincronizar o acesso à última mensagem recebida. */
    private final Object bloqueioMensagem = new Object();
    /** Socket de servidor responsável por aceitar a ligação do cliente. */
    private ServerSocket serverSocket;
    /** Socket que representa o cliente atualmente ligado ao servidor. */
    private Socket clienteSocket;
    /** Leitor de texto usado para receber mensagens do cliente. */
    private BufferedReader entrada;
    /** Escritor de texto usado para enviar mensagens ao cliente. */
    private PrintWriter saida;
    /** Indica se o servidor deve continuar ativo. */
    private volatile boolean ligado;
    /** Indica se já existe um cliente ligado ao servidor. */
    private volatile boolean clienteLigado;
    /** Última mensagem recebida através da ligação de rede. */
    private String ultimaMensagem;
    /** Contador de mensagens recebidas, usado pelos controllers para detetar novidades. */
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
        PrintWriter saidaAtual = saida;

        if (saidaAtual != null) {
            saidaAtual.println(mensagem);
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
    public String getUltimaMensagem() {
        synchronized (bloqueioMensagem) {
            return ultimaMensagem;
        }
    }

    /**
     * Devolve o número de mensagens recebidas.
     *
     * @return quantidade de mensagens recebidas
     */
    public int getNumeroMensagensRecebidas() {
        synchronized (bloqueioMensagem) {
            return numeroMensagensRecebidas;
        }
    }

    /**
     * Indica se o servidor esta ativo.
     *
     * @return true se o servidor estiver ativo, false caso contrario
     */
    public boolean isLigado() {
        return ligado;
    }

    /**
     * Indica se o servidor ja aceitou a ligacao de um cliente.
     *
     * @return true se existir cliente ligado, false caso contrario
     */
    public boolean isClienteLigado() {
        return clienteLigado;
    }

    /**
     * Fecha o servidor e os recursos de rede.
     */
    public void fechar() {
        ligado = false;
        clienteLigado = false;

        ServerSocket serverSocketAtual = serverSocket;
        Socket clienteSocketAtual = clienteSocket;
        BufferedReader entradaAtual = entrada;
        PrintWriter saidaAtual = saida;

        serverSocket = null;
        clienteSocket = null;
        entrada = null;
        saida = null;

        try {
            if (clienteSocketAtual != null) {
                clienteSocketAtual.close();
            }

            if (serverSocketAtual != null) {
                serverSocketAtual.close();
            }

            if (entradaAtual != null) {
                entradaAtual.close();
            }

            if (saidaAtual != null) {
                saidaAtual.close();
            }
        } catch (IOException e) {
            guardarMensagem("Erro ao fechar servidor: " + e.getMessage());
        }
    }

    /**
     * Guarda a última mensagem recebida e incrementa o contador de mensagens.
     *
     * @param mensagem texto recebido pela ligação de rede
     */
    private void guardarMensagem(String mensagem) {
        synchronized (bloqueioMensagem) {
            ultimaMensagem = mensagem;
            numeroMensagensRecebidas++;
        }
    }
}
