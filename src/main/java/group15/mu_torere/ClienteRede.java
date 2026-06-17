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

    /** Endereço IP do servidor ao qual este cliente se vai ligar. */
    private final String ipServidor;
    /** Porto TCP usado para estabelecer a ligação ao servidor. */
    private final int porto;
    /** Objeto usado para sincronizar o acesso à última mensagem recebida. */
    private final Object bloqueioMensagem = new Object();
    /** Socket que representa a ligação TCP ativa ao servidor. */
    private Socket socket;
    /** Leitor de texto usado para receber mensagens vindas do servidor. */
    private BufferedReader entrada;
    /** Escritor de texto usado para enviar mensagens para o servidor. */
    private PrintWriter saida;
    /** Indica se o cliente está ligado e deve continuar a receber mensagens. */
    private volatile boolean ligado;
    /** Última mensagem recebida através da ligação de rede. */
    private String ultimaMensagem;
    /** Contador de mensagens recebidas, usado pelos controllers para detetar novidades. */
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
        setDaemon(true);
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
        PrintWriter saidaAtual = saida;

        if (saidaAtual != null) {
            saidaAtual.println(mensagem);
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
     * Indica se o cliente esta ligado.
     *
     * @return true se estiver ligado, false caso contrario
     */
    public boolean isLigado() {
        return ligado;
    }

    /**
     * Fecha a ligacao e os recursos de rede.
     */
    public void fechar() {
        ligado = false;

        Socket socketAtual = socket;
        BufferedReader entradaAtual = entrada;
        PrintWriter saidaAtual = saida;

        socket = null;
        entrada = null;
        saida = null;

        try {
            if (socketAtual != null) {
                socketAtual.close();
            }

            if (entradaAtual != null) {
                entradaAtual.close();
            }

            if (saidaAtual != null) {
                saidaAtual.close();
            }
        } catch (IOException e) {
            guardarMensagem("Erro ao fechar cliente: " + e.getMessage());
        }
    }

    /**
     * Guarda a última mensagem recebida e incrementa o contador de mensagens.
     *
     * @param mensagem texto recebido através da ligação de rede
     */
    private void guardarMensagem(String mensagem) {
        synchronized (bloqueioMensagem) {
            ultimaMensagem = mensagem;
            numeroMensagensRecebidas++;
        }
    }
}
