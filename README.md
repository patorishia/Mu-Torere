# Mu Torere — Grupo 15

Implementação do jogo de tabuleiro tradicional Maori **Mu Torere** em Java com JavaFX, desenvolvida no âmbito da unidade curricular de Laboratório de Programação (2025/2026) do curso de Engenharia da Computação Gráfica e Multimédia do Instituto Politécnico de Viana do Castelo.

**Autores:** André Freitas (N.º 33782) · Patrícia Pereira (N.º 22304)  
**Docente:** Luís Romero

---

## Sobre o jogo

O Mu Torere é um jogo de tabuleiro para dois jogadores, originário da Nova Zelândia (povo Maori). O tabuleiro tem a forma de uma estrela de oito pontas com uma posição central (*putahi*). Cada jogador possui quatro peças e o objetivo é bloquear o adversário, impedindo-o de realizar qualquer movimento válido.

---

## Funcionalidades

- Jogo local (dois jogadores no mesmo computador)
- Jogo em rede local via TCP/IP (dois computadores distintos)
- Chat integrado durante partidas em rede
- Gravação e carregamento de partidas (formato `.mt`)
- Animação da roleta para sorteio da cor das peças
- Destaque visual das peças com movimentos válidos
- Tabuleiro responsivo (redimensionável)
- Modo ecrã completo (tecla F11)
- Temas visuais claro e escuro
- Efeitos sonoros (jogada válida, inválida e vitória)

---

## Requisitos

- Java Development Kit (JDK) 21 ou superior
- Apache Maven 3.8 ou superior
- JavaFX 21.0.6 (gerido automaticamente pelo Maven)
- IDE: NetBeans ou IntelliJ IDEA (opcional)

---

## Compilação e execução

### Com Maven (linha de comandos)

Na pasta raiz do projeto (onde se encontra o `pom.xml`):

```bash
mvn clean javafx:run
```

### Com NetBeans / IntelliJ IDEA

1. Abrir o projeto apontando para a pasta `Mu_torere_ldp` (contém o `pom.xml`)
2. Aguardar o download automático das dependências Maven
3. Executar a classe `group15.mu_torere.Mu_Torere` ou `group15.mu_torere.Launcher`

### Modo servidor (sem interface gráfica)

```bash
java -cp target/classes group15.mu_torere.Launcher servidor
```

---

## Estrutura do projeto

```
Mu_torere_ldp/
├── pom.xml
├── src/main/java/
│   ├── group15/mu_torere/       # Modelo de dados, rede e launcher
│   │   ├── Mu_Torere.java
│   │   ├── Launcher.java
│   │   ├── Jogo.java
│   │   ├── Jogador.java
│   │   ├── Peca.java
│   │   ├── Posicao.java
│   │   ├── Tabuleiro.java
│   │   ├── DadosGlobais.java
│   │   ├── GestorFicheiros.java
│   │   ├── GestorSons.java
│   │   ├── ServidorRede.java
│   │   └── ClienteRede.java
│   └── gui/                     # Controladores JavaFX/FXML
│       ├── MenuInicialController.java
│       ├── InserirJogadoresController.java
│       ├── InserirIPController.java
│       ├── EsperaController.java
│       ├── RoletaController.java
│       ├── EscolherCorController.java
│       ├── JogoController.java
│       ├── ParametrosController.java
│       ├── FimJogoController.java
│       └── ScreenManager.java
└── src/main/resources/
    ├── fxml/                    # Ecrãs FXML e folha de estilos CSS
    └── sons/                    # Efeitos sonoros (.wav)
```

---

## Como jogar

### Jogo local

1. No menu inicial, escolher **Jogo Local**
2. Introduzir os nomes dos dois jogadores
3. A roleta sorteia qual jogador escolhe a cor das peças
4. No tabuleiro, clicar numa peça (as jogáveis ficam destacadas a verde) e depois na casa de destino

### Jogo em rede

**Anfitrião:**
1. Escolher **Jogo em Rede** → **Criar Servidor**
2. Partilhar o IP apresentado com o adversário
3. Aguardar a ligação no ecrã de espera

**Convidado:**
1. Escolher **Jogo em Rede** → **Ligar**
2. Introduzir o IP do anfitrião (porta 5000)

### Gravar e carregar uma partida

- **Guardar:** botão *Guardar Jogo* durante uma partida local → guarda ficheiro `.mt`
- **Carregar:** botão *Carregar Jogo* no menu inicial → selecionar ficheiro `.mt`

---

## Protocolo de rede

A comunicação entre os dois clientes é feita por mensagens de texto separadas pelo carácter `|` sobre uma ligação TCP na porta 5000:

| Mensagem | Formato | Descrição |
|---|---|---|
| `LIGACAO_ACEITE` | `LIGACAO_ACEITE` | Ligação aceite pelo servidor |
| `JOGADA` | `JOGADA\|origem\|destino` | Jogada efetuada |
| `ESTADO_JOGO` | `ESTADO_JOGO\|...` | Estado completo do jogo |
| `CHAT` | `CHAT\|texto` | Mensagem de conversa |
| `DESISTENCIA` | `DESISTENCIA` | Jogador abandonou a partida |

---

## Resolução de problemas

| Problema | Solução |
|---|---|
| Erro de módulos JavaFX ao correr o `.jar` | Usar `mvn javafx:run` ou a classe `Launcher` |
| Não consegue ligar em rede | Confirmar que ambos estão na mesma rede local e que a porta 5000 não está bloqueada pela firewall |
| Sons não reproduzem | Verificar se o som está ativo nas Definições e se os ficheiros `.wav` existem em `resources/sons` |
| Ficheiro `.mt` não carrega | Confirmar que a primeira linha do ficheiro contém `MU_TORERE_1` |

---

## Documentação técnica

Para gerar a documentação Javadoc em HTML, executar na raiz do projeto:

```bash
javadoc -d docs -sourcepath src/main/java -subpackages group15.mu_torere:gui
```

---

## Tecnologias utilizadas

- Java 21
- JavaFX 21.0.6
- Apache Maven 3.8
- Sockets TCP/IP (`java.net`)
- Git / GitHub
