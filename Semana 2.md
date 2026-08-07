# Aula 1

## Comunicação

A comunicação pode ser analisada em três aspectos:

- **Lexemas**: unidades básicas (palavras/símbolos).
- **Sintaxe**: regras de organização dos elementos.
- **Semântica**: significado da informação transmitida.

---

## Protocolo TCP/IP

O papel do **TCP/IP** é padronizar a comunicação e permitir a troca de dados entre computadores e redes diferentes.

Suas principais funções são:

- Dividir as informações em pequenos blocos (pacotes).
- Encontrar o caminho adequado pela rede.
- Garantir que os dados cheguem corretamente ao destino, sem erros.

---

## Bloqueio e Seção Crítica

### Bloqueante

- Existe um **meio** que pode ser considerado uma **Seção Crítica**.

### Compartilhamento de Memória

- O compartilhamento de memória cria uma **Seção Crítica**, pois vários fluxos de execução podem acessar os mesmos dados.

### Memória Compartilhada ≈ Seção Crítica

Aspectos importantes:

- **Sincronismo**
  - Tempo
  - Bloqueio

---

# Threads

## Threads com Compartilhamento de Memória (Padrão)

### Como funciona

Todas as threads leem e escrevem nas mesmas variáveis, heap e ponteiros do processo pai.

### Vantagens

- Comunicação extremamente rápida.
- Baixo custo de troca de dados.
- Basta passar o endereço de uma variável para outra thread acessá-la.

### Desvantagens

- Alto risco de **Race Condition (Condição de Corrida)**.
- Possibilidade de corrupção de dados caso duas threads alterem o mesmo recurso simultaneamente.
- Necessidade de mecanismos de sincronização, como:
  - Mutex
  - Semáforos

---

## Threads sem Compartilhamento de Memória

### Como funciona

Cada unidade de execução possui seu próprio espaço de endereçamento de memória, isolado pelo sistema operacional.

### Vantagens

- Isolamento entre tarefas.
- Segurança contra interferências acidentais.
- Se uma tarefa falhar ou corromper sua memória, as demais permanecem intactas.

### Desvantagens

- Comunicação mais lenta e custosa.
- Necessidade de mecanismos formais de **IPC (Interprocess Communication)**, como:
  - Pipes
  - Sockets
  - Serialização de dados

---

# Exemplo de Criação de Threads

```text
1º) Objeto Thread(nome)
    -> popular_lista(listaA, 100);

2º) new Thread
    -> popular_lista(listaB, 1000);
    -> popular_lista(listaC, 50);
```

---

# Threads por Linguagem

| Linguagem | Uso de Threads | Particularidade / Limitação |
| ---------- | -------------- | --------------------------- |
| **Java** | Suporte completo, fácil e robusto com Executor Framework | Muito utilizado em sistemas distribuídos |
| **Python** | Suporte para threads | O GIL limita o paralelismo real; ideal para aplicações **I/O-bound**, não **CPU-bound** |
| **C#** | Suporte completo com `async/await` e `Task` | Amplamente utilizado em aplicações de servidor |

---

## Resumo

- **TCP/IP** padroniza a comunicação entre redes e garante a entrega correta dos dados.
- **Seção Crítica** ocorre quando múltiplas execuções acessam um recurso compartilhado.
- O acesso à memória compartilhada exige **sincronização** para evitar conflitos.
- Threads com memória compartilhada são mais rápidas, porém exigem mecanismos de controle.
- Threads sem memória compartilhada oferecem maior segurança, mas a comunicação é mais lenta devido ao uso de IPC.

---

# Aula 2

## Revisão sobre Arquiteturas

### Arquitetura Cliente-Servidor

- **Modelo centralizado:** um ou mais servidores fornecem serviços, dados ou recursos.
- **Clientes** solicitam serviços ou recursos aos servidores.
- Os servidores processam e respondem às requisições dos clientes.
- Comunicação baseada em requisição: o cliente faz o pedido e o servidor responde.
- Exemplo: navegador (cliente) acessando um servidor web.

#### Características

- **Centralização:** os servidores são o núcleo do sistema.
- **Dependência:** se o servidor falhar, o serviço pode ficar indisponível.
- **Gerenciamento:** administração centralizada e mais simples.

---

### Arquitetura Ponto a Ponto (P2P)

- **Modelo descentralizado:** todos os nós podem atuar como clientes e servidores.
- Cada nó pode solicitar e fornecer recursos diretamente a outros nós, sem um servidor central.
- Comunicação direta entre os pares.
- Exemplo: redes de compartilhamento de arquivos, como **BitTorrent**.

#### Características

- **Descentralização:** não existe ponto único de falha.
- **Escalabilidade:** o sistema cresce facilmente, pois cada nó contribui com recursos.
- **Resiliência:** a falha de um nó não interrompe o funcionamento do sistema.

---

## Exemplo de Threads em Java

### Classe da Thread

```java
import java.util.ArrayList;
import java.util.Random;

class TarefaPopular extends Thread {

    ArrayList<Integer> lista;
    int quantidade;

    public TarefaPopular(ArrayList<Integer> lista, int quantidade) {
        this.lista = lista;
        this.quantidade = quantidade;
    }

    @Override
    public void run() {
        Random gerador = new Random();

        for (int i = 0; i < quantidade; i++) {
            lista.add(gerador.nextInt(200));
        }
    }
}
```

### Classe Principal

```java
import java.util.ArrayList;
import java.util.Random;

public class ExemploThread {

    public static void main(String[] args) {

        ArrayList<Integer> listaA = new ArrayList<>();
        ArrayList<Integer> listaB = new ArrayList<>();
        Random gerador = new Random();

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                listaB.add(gerador.nextInt(200));
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 5000; i++) {
                listaB.add(gerador.nextInt(200));
            }
        });

        t1.start();
        t2.start();

        // Exemplo utilizando herança da classe Thread

        ArrayList<Integer> listaC = new ArrayList<>();
        ArrayList<Integer> listaD = new ArrayList<>();

        TarefaPopular t3 = new TarefaPopular(listaC, 500);
        TarefaPopular t4 = new TarefaPopular(listaD, 500);

        t3.start();
        t4.start();
    }
}
```

---

## Observações

- Uma thread pode ser criada implementando `Runnable` (expressões lambda) ou estendendo a classe `Thread`.
- O método `start()` inicia uma nova thread de execução, enquanto `run()` contém o código executado por ela.
- É necessário criar a `Thread` primeiro e chamar `start()` depois, pois `start()` retorna `void`.
- Quando várias threads acessam a mesma estrutura de dados (como um `ArrayList`), pode ocorrer **Race Condition**, sendo necessário utilizar sincronização ou coleções thread-safe.
