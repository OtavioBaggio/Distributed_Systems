# Semana 11 - Aula 09/10

## Multicast

> Comunicação em **grupo**: um endereço virtual que os computadores acessam para compartilhar informações entre si. Só envia/recebe mensagens quem estiver no grupo.

---

### Modos de comunicação

| Modo | Relação | Quem recebe |
|---|---|---|
| **Unicast** | 1 → 1 | Um único destino |
| **Multicast** | 1 → N | Todos os membros do **mesmo grupo** |
| **Broadcast** | 1 → todos | Todas as máquinas da rede |

```
Unicast          Multicast                 Broadcast
A ──► B          A ──► [Grupo 239.1.2.3]   A ──► todos
                        ├──► B             ├──► B
                        └──► C             ├──► C
                      (D fora do grupo)    └──► D
```

---

### Características

| Item | Descrição |
|---|---|
| **Arquitetura** | Ponto-a-ponto (P2P) — cada máquina/estação é **servidor e cliente** ao mesmo tempo |
| **Protocolo de transporte** | **UDP** (padrão) |
| **Conexão** | Simultânea / online |
| **Base do conceito** | Grupo — só participa quem entrou no grupo |
| **Exemplo** | Microsoft Teams |

---

### Modelo TCP/IP no Multicast

| Elemento | Papel |
|---|---|
| Endereço de rede | Identifica a máquina na rede |
| Endereço de grupo | Endereço **classe D** (`224.0.0.0` – `239.255.255.255`) |
| Roteamento no grupo | Entrega da mensagem a todos os membros do grupo |

---

### Conceitos principais

| Conceito | Descrição |
|---|---|
| **IP do Grupo** | IP "falso"/virtual, por convenção `239.X.Y.W` |
| **Porta de conexão** | Porta de saída da estação, usada por todos do grupo |
| **Thread ouvidora / receptora** | Fica escutando o grupo continuamente |
| **Thread falante / enviadora** | Monta e envia as mensagens ao grupo |

---

### Operações UDP (todas bloqueantes)

| Operação | Comportamento |
|---|---|
| Montar mensagem | Bloqueante |
| Enviar mensagem | Bloqueante |
| Receber mensagem | Bloqueante — trava até chegar algo |

**Por que usar duas threads?** Como enviar e receber são bloqueantes, numa única thread a estação ficaria travada esperando receber e não conseguiria enviar (ou vice-versa). Separar em **thread receptora + thread enviadora** "quebra" o bloqueio e permite que as duas ações aconteçam ao mesmo tempo.

```
           Estação (servidor + cliente)
        ┌──────────────────────────────────┐
        │  Thread Enviadora  ──► send()  ──┼──► Grupo 239.X.Y.W : porta
        │  Thread Receptora  ◄── receive()◄┼───
        └──────────────────────────────────┘
```

---

## Exemplos em Java (aula)

> Todos usam a classe auxiliar `util.ComunicadorUDP` (métodos `montaMensagem` e `recebeMensagem`), grupo `239.1.2.3` e porta `3456`.

### Fluxo básico de um processo multicast

| Passo | Código |
|---|---|
| 1. Endereço do grupo | `InetAddress.getByName(enderecoGrupo)` |
| 2. Criar socket | `new MulticastSocket(porta)` |
| 3. Entrar no grupo | `socket.joinGroup(grupo)` |
| 4. Enviar / receber | `socket.send(pacote)` / `ComunicadorUDP.recebeMensagem(socket)` |
| 5. Sair do grupo | `socket.leaveGroup(grupo)` |
| 6. Fechar socket | `socket.close()` |

### Comparação dos exemplos

| Exemplo | Classes | Comportamento | Threads |
|---|---|---|---|
| **ex1** | `Emissor` + `Receptor` | Envia/recebe **uma única** mensagem fixa | Não |
| **ex2** | `Emissor` + `Receptor` | Loop lendo do teclado até digitar `sair` | Não |
| **ex3** | `EmissorReceptor` | Cada estação envia **e** recebe ao mesmo tempo (P2P) | Sim — `ThreadReceptora` + `ThreadEmissora` |

---

### Exemplo 1 — Mensagem única

**Emissor.java**

```java
package ex1;

import java.io.IOException;
import java.net.*;
import util.ComunicadorUDP;

public class Emissor {

    public Emissor() {
        int porta = 3456;
        String enderecoGrupo = "239.1.2.3";
        try {
            //especificação do endereço do grupo multicast
            InetAddress grupo = InetAddress.getByName(enderecoGrupo);

            //criação do socket multicast
            MulticastSocket socket = new MulticastSocket(porta);

            //processo entra no grupo multicast
            socket.joinGroup(grupo);

            //criação do pacote multicast (mesmo que pacote UDP)
            DatagramPacket pacote = ComunicadorUDP.montaMensagem("Turma de Sistemas Distribuídos", enderecoGrupo, porta);

            //envio do pacote
            socket.send(pacote);

            socket.leaveGroup(grupo);
            socket.close();
        } catch (IOException ex) {
        }
    }

    public static void main(String[] args) {
        Emissor e = new Emissor();
    }
}
```

**Receptor.java**

```java
package ex1;

import java.net.*;
import util.ComunicadorUDP;

public class Receptor {

    public Receptor() {
        try {
            int porta = 3456;
            String enderecoGrupo = "239.1.2.3";
            
            //especificação do endereço do grupo multicast
            InetAddress grupo = InetAddress.getByName(enderecoGrupo);
            
            //criação do socket multicast
            MulticastSocket socket = new MulticastSocket(porta);
            
            //processo entra no grupo multicast
            socket.joinGroup(grupo);
                        
            //aguarda um pacote datagrama chegar de um cliente pela porta 3456
            DatagramPacket pacote  = ComunicadorUDP.recebeMensagem(socket);
            
            //crio uma String com os dados do pacote datagrama
            String msgRecebida = new String(pacote.getData(), 0, pacote.getLength());
            
            System.out.println("Enviaram :" + msgRecebida);
            
            //processo deixa o grupo multicast
            socket.leaveGroup(grupo);
            socket.close();
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Receptor r = new Receptor();
    }
}
```

---

### Exemplo 2 — Loop até "sair"

**Emissor.java**

```java
package ex2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Scanner;
import util.ComunicadorUDP;

public class Emissor {

    public Emissor() {
        int porta = 3456;
        String enderecoGrupo = "239.1.2.3";
        String msg = "";
        try {
            //criação do Scanner para leitura do teclado
            Scanner scanner = new Scanner(System.in);

            //especificação do endereço do grupo multicast
            InetAddress grupo = InetAddress.getByName(enderecoGrupo);

            //criação do socket multicast
            MulticastSocket socket = new MulticastSocket(porta);

            //processo entra no grupo multicast
            socket.joinGroup(grupo);
            do {
                //leitura do teclado
                System.out.println("Digite: ");
                msg = scanner.nextLine();

                //criação do pacote multicast (mesmo que pacote UDP)
                DatagramPacket pacote = ComunicadorUDP.montaMensagem(msg, enderecoGrupo, porta);

                //envio do pacote
                socket.send(pacote);

                System.out.println("Mensagem enviada");
            } while (!msg.equals("sair"));
            socket.close();
            socket.leaveGroup(grupo);
        } catch (IOException ex) {
        }
    }

    public static void main(String[] args) {
        Emissor e = new Emissor();
    }
}
```

**Receptor.java**

```java
package ex2;

import java.net.*;
import util.ComunicadorUDP;

public class Receptor {

    public Receptor() {
        int porta = 3456;
        String enderecoGrupo = "239.1.2.3";
        try {
            //especificação do endereço do grupo multicast
            InetAddress grupo = InetAddress.getByName(enderecoGrupo);

            //criação do socket multicast
            MulticastSocket socket = new MulticastSocket(porta);

            //processo entra no grupo multicast
            socket.joinGroup(grupo);

            String msgRecebida = "";
            do {
                //aguarda um pacote datagrama chegar de um cliente pela porta 3456
                DatagramPacket pacote = ComunicadorUDP.recebeMensagem(socket);

                //crio uma String com os dados do pacote datagrama
                msgRecebida = new String(pacote.getData(), 0, pacote.getLength());

                System.out.println("Enviaram :" + msgRecebida);
            } while (!msgRecebida.equals("sair"));

            //processo deixa o grupo multicast
            socket.leaveGroup(grupo);
            socket.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Receptor r = new Receptor();
    }
}
```

---

### Exemplo 3 — Emissor e Receptor com threads (P2P)

> Aqui a mesma estação é **cliente e servidor**: a `ThreadReceptora` fica bloqueada no `recebeMensagem` enquanto a `ThreadEmissora` fica bloqueada no `nextLine()`/`send`, sem uma travar a outra.

**EmissorReceptor.java**

```java
package ex3;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Scanner;
import util.ComunicadorUDP;

public class EmissorReceptor {

    //ESPECIFICO UMA THREAD RECEPTORA DE MENSAGENS DO GRUPO
    class ThreadReceptora extends Thread {

        @Override
        public void run() {
            /*
             * PARTE RESPONSAVEL POR RECEBER AS MENSAGENS DO GRUPO
             */
            while (true) {
                try {
                    //BLOQUEIO ATÉ RECEBER A MENSAGEM
                    final DatagramPacket pacote = ComunicadorUDP.recebeMensagem(socket);
                    
                    // TRANSFORMO A MENSAGEM EM STRING (POIS ELA VEM EM BYTES)
                    final String msgRecebida = new String(pacote.getData(), 0, pacote.getLength());
                    
                    // MOSTRO A MENASGEM NA TELA
                    System.out.println(msgRecebida);
                } catch (final Exception e) {
                }
            }
        }
    }

    // ESPECIFICO UMA THREAD EMISSORA DE MENSAGENS DO GRUPO
    class ThreadEmissora extends Thread {

        @Override
        public void run() {
            /*
             * PARTE RESPONSAVEL POR ENVIAR MENSAGENS AO GRUPO
             */
            // PREPARO A LEITURA DO TECLADO
            String msg = "";
            final Scanner scanner = new Scanner(System.in);
            
            while (true) {
                try {
                    // LE A MENSAGEM DO TECLADO
                    msg = scanner.nextLine();
                    
                    // CRIA O PACOTE DATAGRAMA
                    final DatagramPacket pacote = ComunicadorUDP.montaMensagem(msg, enderecoGrupo, porta);
                    
                    // ENVIA A MENSAGEM PRO GRUPO MULTICAST
                    socket.send(pacote);
                } catch (final Exception e) {

                }
            }
        }
    }

    public EmissorReceptor() {
        // PARTE EMISSORA
        try {
            /*
             * CONFIGURAÇÃO DO EMISSOR/RECEPTOR
             */
            // DEFINO O IP DO GRUPO
            grupo = InetAddress.getByName(enderecoGrupo);
            
            // CRIO O SOCKET MULTICAST COM A PORTA ESPECIFICADA
            socket = new MulticastSocket(porta);
            
            // ENTRA NO GRUPO MULTICAST PARA RECEBER AS MENSAGENS
            socket.joinGroup(grupo);
            
            // CRIO A THREAD PARA RECEBER AS MENSAGENS
            final ThreadReceptora tR = new ThreadReceptora();
            tR.start();
            
            final ThreadEmissora tE = new ThreadEmissora();
            tE.start();
        } catch (final Exception e) {
        }
    }


    InetAddress grupo;
    MulticastSocket socket;
    int porta = 3456;
    String enderecoGrupo = "239.1.2.3";
    
    public static void main(final String args[]) {
        final EmissorReceptor er = new EmissorReceptor();
    }
}
```

---

### Resumo rápido

| Pergunta | Resposta |
|---|---|
| O que é? | Comunicação 1 → N dentro de um grupo |
| Arquitetura? | Ponto-a-ponto (cada estação é cliente e servidor) |
| Transporte? | UDP |
| Endereço do grupo? | Classe D, convenção `239.X.Y.W` |
| Por que threads? | Enviar e receber são bloqueantes |
| Exemplo real? | Teams |
