/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package group15.mu_torere;

/**
 * Classe usada para guardar dados globais que precisam de ser
 * partilhados entre diferentes ecrãs da aplicação.
 *
 * Não tem lógica — é apenas um "contentor" de variáveis públicas.
 */
public class DadosGlobais {

    // ---------------------------------------------------------
    // JOGADORES
    // ---------------------------------------------------------

    /** Nome do jogador 1 (inserido no ecrã Inserir Jogadores) */
    public static String nomeJogador1;

    /** Nome do jogador 2 */
    public static String nomeJogador2;

    /** Cor escolhida pelo jogador 1 ("claro" ou "escuro") */
    public static String corJogador1;

    /** Cor escolhida pelo jogador 2 ("claro" ou "escuro") */
    public static String corJogador2;

    /** Jogador que ganhou a roleta e escolhe a cor */
    public static String jogadorQueEscolheCor;

    /** Nome do jogador que esta a usar este computador */
    public static String nomeJogadorLocal;

    /** Cor do jogador que esta a usar este computador */
    public static String corJogadorLocal;

    // ---------------------------------------------------------
    // FIM DE JOGO
    // ---------------------------------------------------------

    /** Nome do vencedor do jogo (usado no ecrã Fim de Jogo) */
    public static String vencedor;

    // ---------------------------------------------------------
    // DEFINIÇÕES
    // ---------------------------------------------------------

    /** Som ativado/desativado */
    public static boolean somAtivo = true;

    /** Tema atual ("Claro" ou "Escuro") */
    public static String temaAtual = "Escuro";
    
    
    public static String ipServidor;

    /** Porto usado para a ligação em rede */
    public static int portoServidor = 5000;

    /** Servidor criado pelo jogador anfitrião */
    public static ServidorRede servidorRede;

    /** Cliente usado pelo jogador que entra no servidor */
    public static ClienteRede clienteRede;

    /** Modo atual da partida: "local" ou "rede" */
    public static String modoJogo = "local";

    /** Jogo carregado a partir de ficheiro */
    public static Jogo jogoCarregado;

    /**
     * Fecha as ligacoes de rede ativas.
     */
    public static void fecharLigacoesRede() {
        if (servidorRede != null) {
            servidorRede.fechar();
        }

        if (clienteRede != null) {
            clienteRede.fechar();
        }

        servidorRede = null;
        clienteRede = null;
    }

    /**
     * Limpa os dados da partida atual para permitir iniciar outro jogo.
     */
    public static void limparJogoAtual() {
        fecharLigacoesRede();

        nomeJogador1 = null;
        nomeJogador2 = null;
        corJogador1 = null;
        corJogador2 = null;
        jogadorQueEscolheCor = null;
        nomeJogadorLocal = null;
        corJogadorLocal = null;
        vencedor = null;
        ipServidor = null;
        modoJogo = "local";
        jogoCarregado = null;
    }

}
