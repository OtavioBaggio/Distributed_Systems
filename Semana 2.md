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
|-----------|----------------|-----------------------------|
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
