# Sistemas Distribuídos: Relógios, Exclusão Mútua, Eleição e Pool de Threads

> Material compilado para estudo/repositório pessoal (Disciplina de Sistemas Distribuídos).
> Cada seção traz: teoria resumida em tabela + pseudocódigo/algoritmo + exemplo SIMPLES em Python (simulação single-process com threads, sem necessidade de rede real).

---

## 1. Relógios Físicos

Em um sistema distribuído não existe um clock global: cada máquina tem seu próprio cristal de quartzo, que sofre **drift** (desvio de frequência). Isso gera divergência entre relógios ao longo do tempo, exigindo sincronização.

| Conceito | Definição |
|---|---|
| **Skew (desvio)** | Diferença instantânea entre as leituras de dois relógios |
| **Drift (deriva)** | Taxa com que um relógio se afasta do tempo real (Hz de cristal imperfeito) |
| **Offset** | Diferença absoluta entre o valor de um relógio e o tempo de referência |
| **Sincronização externa** | Relógios sincronizados com uma fonte de tempo autoritativa (ex.: UTC/NTP) |
| **Sincronização interna** | Relógios sincronizados entre si, sem referência externa obrigatória |

### 1.1 Algoritmos de sincronização física

| Algoritmo | Tipo | Ideia central |
|---|---|---|
| **Cristian** | Centralizado, cliente-servidor | Cliente pede hora a um servidor de tempo; usa o RTT (round-trip time) da requisição para estimar e compensar o atraso de rede: `T_estimado = T_servidor + RTT/2` |
| **Berkeley** | Centralizado, coordenador ativo | Um coordenador consulta periodicamente todas as máquinas, calcula a **média (tolerante a outliers)** dos desvios e envia um ajuste (não a hora absoluta) para cada uma |
| **NTP (Network Time Protocol)** | Hierárquico (estratos) | Servidores em camadas (estrato 0 = relógio atômico/GPS) propagam tempo com estimativa de atraso e correção contínua; é o padrão usado na internet real |

**Limitação prática:** clocks físicos nunca ficam perfeitamente sincronizados (existe sempre um limite ε de imprecisão), por isso muitos sistemas distribuídos preferem ordenar eventos por **causalidade**, e não por tempo físico — o que leva aos relógios lógicos.

### 1.2 Exemplo simples em Python — Algoritmo de Cristian (simulado)

```python
import random
import time

def servidor_tempo():
    """Simula o relógio 'verdadeiro' do servidor de tempo."""
    return time.time()

def cliente_cristian(latencia_rede=0.05):
    """Cliente estima a hora do servidor compensando o RTT."""
    t0 = time.time()                          # instante do envio da requisição
    time.sleep(latencia_rede)                  # ida até o servidor
    hora_servidor = servidor_tempo()
    time.sleep(latencia_rede)                  # volta da resposta
    t1 = time.time()                           # instante do recebimento

    rtt = t1 - t0
    hora_estimada = hora_servidor + rtt / 2     # algoritmo de Cristian
    print(f"RTT: {rtt:.3f}s | Hora estimada no cliente: {hora_estimada:.3f}")
    return hora_estimada

cliente_cristian()
```

---

## 2. Relógios Lógicos

Relógios lógicos não medem tempo real; medem **ordem causal** de eventos. Baseiam-se na relação **happened-before** (`->`), definida por Leslie Lamport em 1978 ("Time, Clocks, and the Ordering of Events in a Distributed System"):

| Regra | Descrição |
|---|---|
| **Ordem local** | Se `a` e `b` ocorrem no mesmo processo e `a` antes de `b`, então `a -> b` |
| **Envio/recebimento** | Se `a` = envio de mensagem e `b` = recebimento da mesma mensagem, então `a -> b` |
| **Transitividade** | Se `a -> b` e `b -> c`, então `a -> c` |
| **Concorrência (`\|\|`)** | Se não existe cadeia causal entre `a` e `b`, eles são concorrentes — a ordem entre eles é indefinida |

### 2.1 Relógio de Lamport (Lamport Timestamps)

Cada processo mantém um contador inteiro `C`.

| Regra do algoritmo | Ação |
|---|---|
| R1 | Antes de cada evento local, incrementa `C = C + 1` |
| R2 | Ao **enviar** mensagem, anexa o valor atual de `C` a ela |
| R3 | Ao **receber** mensagem com timestamp `T`, faz `C = max(C, T) + 1` |

**Limitação:** garante apenas ordem parcial. Se `C(a) < C(b)`, **não** se pode concluir que `a -> b` (pode ser concorrência) — só a volta é garantida: `a -> b ⟹ C(a) < C(b)`.

```python
class RelogioLamport:
    def __init__(self, nome):
        self.nome = nome
        self.tempo = 0

    def evento_local(self):
        self.tempo += 1
        print(f"[{self.nome}] evento local -> clock={self.tempo}")
        return self.tempo

    def enviar(self):
        self.tempo += 1
        print(f"[{self.nome}] envia mensagem -> clock={self.tempo}")
        return self.tempo  # timestamp anexado à mensagem

    def receber(self, timestamp_recebido):
        self.tempo = max(self.tempo, timestamp_recebido) + 1
        print(f"[{self.nome}] recebe mensagem(ts={timestamp_recebido}) -> clock={self.tempo}")


# --- Simulação: P1 envia para P2 ---
p1 = RelogioLamport("P1")
p2 = RelogioLamport("P2")

p1.evento_local()          # P1: clock=1
ts = p1.enviar()           # P1: clock=2, envia ts=2
p2.evento_local()          # P2: clock=1 (evento independente)
p2.receber(ts)             # P2: clock=max(1,2)+1=3
```

### 2.2 Relógio Vetorial (Vector Clock)

Estende o relógio de Lamport para capturar causalidade de forma **exata** (não só parcial). Cada processo mantém um **vetor** de contadores, um por processo do sistema.

| Regra do algoritmo | Ação |
|---|---|
| Inicialização | `V[i] = [0, 0, ..., 0]` (tamanho N = nº de processos) |
| Evento local em `Pi` | `V[i][i] += 1` |
| Envio em `Pi` | `V[i][i] += 1`; anexa o vetor inteiro à mensagem |
| Recebimento em `Pi` de vetor `Vm` | `V[i][k] = max(V[i][k], Vm[k])` para todo `k`; depois `V[i][i] += 1` |

**Comparação de vetores** `Va` e `Vb`:
- `Va -> Vb` (a aconteceu antes de b) se `Va[k] <= Vb[k]` para todo k, e `Va != Vb`
- Caso contrário (nenhum domina o outro), os eventos são **concorrentes**

```python
class RelogioVetorial:
    def __init__(self, nome, indice, n_processos):
        self.nome = nome
        self.i = indice
        self.vetor = [0] * n_processos

    def evento_local(self):
        self.vetor[self.i] += 1
        print(f"[{self.nome}] evento local -> {self.vetor}")
        return list(self.vetor)

    def enviar(self):
        self.vetor[self.i] += 1
        print(f"[{self.nome}] envia -> {self.vetor}")
        return list(self.vetor)

    def receber(self, vetor_recebido):
        for k in range(len(self.vetor)):
            self.vetor[k] = max(self.vetor[k], vetor_recebido[k])
        self.vetor[self.i] += 1
        print(f"[{self.nome}] recebe {vetor_recebido} -> {self.vetor}")


def compara(va, vb):
    menor_igual = all(a <= b for a, b in zip(va, vb))
    maior_igual = all(a >= b for a, b in zip(va, vb))
    if va == vb:
        return "iguais"
    if menor_igual:
        return "A aconteceu antes de B"
    if maior_igual:
        return "B aconteceu antes de A"
    return "concorrentes"


# --- Simulação com 3 processos: P0, P1, P2 ---
p0 = RelogioVetorial("P0", 0, 3)
p1 = RelogioVetorial("P1", 1, 3)
p2 = RelogioVetorial("P2", 2, 3)

v_env = p0.enviar()            # P0: [1,0,0]
va = p1.evento_local()         # P1: [0,1,0]  (concorrente com o envio de P0)
p1.receber(v_env)              # P1: max([0,1,0],[1,0,0])+1 no próprio índice -> [1,2,0]
vb = p2.evento_local()         # P2: [0,0,1]

print(compara(v_env, vb))      # -> concorrentes
```

---

## 3. Exclusão Mútua Distribuída

Objetivo: garantir que apenas um processo por vez execute uma **seção crítica (SC)** que acessa um recurso compartilhado, sem memória compartilhada — só troca de mensagens.

| Propriedade exigida | Significado |
|---|---|
| **Segurança (safety)** | No máximo um processo na SC por vez |
| **Vivacidade (liveness)** | Toda solicitação é eventualmente atendida (sem deadlock) |
| **Justiça/ordem (fairness)** | Pedidos são atendidos na ordem em que foram feitos (geralmente por timestamp) |

### 3.1 Comparativo dos algoritmos clássicos

| Algoritmo | Categoria | Mensagens por entrada na SC | Ponto fraco |
|---|---|---|---|
| **Centralizado** | Coordenador único | 3 (request, grant, release) | Coordenador é ponto único de falha/gargalo |
| **Anel (Token Ring)** | Baseado em token | 0 a N (token circula continuamente) | Token perdido = precisa de regeneração; latência se anel for grande |
| **Lamport** | Baseado em permissão (broadcast) | 3(N−1) (request, reply, release) | Alto custo de mensagens; falha de 1 processo trava o sistema |
| **Ricart–Agrawala** | Baseado em permissão (broadcast) | 2(N−1) (request, reply — sem release) | Mesma fragilidade a falhas; ainda O(N) mensagens |

### 3.2 Algoritmo Centralizado

Um coordenador mantém uma fila FIFO de pedidos.

```
Pi quer SC  -> envia REQUEST ao coordenador
Coordenador -> se recurso livre: envia GRANT; senão enfileira Pi
Pi sai da SC -> envia RELEASE ao coordenador
Coordenador -> libera o próximo da fila com GRANT
```

```python
import threading
import queue
import time

class CoordenadorSC:
    def __init__(self):
        self.livre = True
        self.fila = queue.Queue()
        self.lock = threading.Lock()

    def request(self, processo, evento_grant):
        with self.lock:
            if self.livre:
                self.livre = False
                print(f"[Coordenador] GRANT imediato para {processo}")
                evento_grant.set()
            else:
                self.fila.put((processo, evento_grant))
                print(f"[Coordenador] {processo} enfileirado")

    def release(self, processo):
        with self.lock:
            print(f"[Coordenador] {processo} liberou a SC")
            if not self.fila.empty():
                proximo, evento_proximo = self.fila.get()
                print(f"[Coordenador] GRANT (da fila) para {proximo}")
                evento_proximo.set()
            else:
                self.livre = True


def processo_worker(nome, coordenador):
    evento_grant = threading.Event()
    coordenador.request(nome, evento_grant)
    evento_grant.wait()   # bloqueia até o coordenador conceder a SC (sem espera ativa)
    time.sleep(0.2)       # "dentro" da seção crítica
    coordenador.release(nome)


coord = CoordenadorSC()
threads = [threading.Thread(target=processo_worker, args=(f"P{i}", coord)) for i in range(3)]
for t in threads: t.start()
for t in threads: t.join()
```

### 3.3 Algoritmo de Ricart–Agrawala (essência)

Usa relógio de Lamport para dar prioridade ao pedido mais antigo (timestamp menor; empate desempatado por ID do processo).

```
Para entrar na SC, Pi:
  1. incrementa seu relógio de Lamport
  2. envia REQUEST(timestamp, i) para todos os outros processos
  3. espera REPLY de todos

Ao receber REQUEST(ts_j, j), Pj:
  - se Pj não quer a SC, ou quer mas (ts_j, j) > (ts_i, i): responde REPLY imediatamente
  - se Pj está na SC, ou quer com prioridade maior (ts menor): adia a resposta (guarda em fila)

Ao sair da SC, Pi envia REPLY para todos os pedidos que tinha adiado
```

```python
import threading

class NoRicartAgrawala:
    def __init__(self, id_no, todos):
        self.id = id_no
        self.todos = todos          # lista de todos os nós (referência compartilhada)
        self.clock = 0
        self.quer_sc = False
        self.na_sc = False
        self.timestamp_pedido = None
        self.adiados = []
        self.lock = threading.Lock()
        self.replies_recebidos = threading.Event()
        self.contagem_replies = 0

    def solicitar_sc(self):
        with self.lock:
            self.clock += 1
            self.timestamp_pedido = (self.clock, self.id)
            self.quer_sc = True
            self.contagem_replies = 0
        print(f"[No {self.id}] pede SC com timestamp {self.timestamp_pedido}")
        for outro in self.todos:
            if outro.id != self.id:
                outro.receber_request(self.timestamp_pedido, self)
        self.replies_recebidos.wait(timeout=2)
        self.na_sc = True
        print(f"[No {self.id}] ENTROU na SC")

    def receber_request(self, ts_remetente, remetente):
        with self.lock:
            self.clock = max(self.clock, ts_remetente[0]) + 1
            prioridade_maior = self.quer_sc and (self.timestamp_pedido < ts_remetente)
            if self.na_sc or prioridade_maior:
                self.adiados.append(remetente)
                return
        remetente.receber_reply()

    def receber_reply(self):
        with self.lock:
            self.contagem_replies += 1
            if self.contagem_replies == len(self.todos) - 1:
                self.replies_recebidos.set()

    def sair_sc(self):
        with self.lock:
            self.na_sc = False
            self.quer_sc = False
            pendentes, self.adiados = self.adiados, []
        print(f"[No {self.id}] saiu da SC")
        for p in pendentes:
            p.receber_reply()


# Exemplo simples com 3 nós disputando a SC
nos = []
nos.extend([NoRicartAgrawala(i, nos) for i in range(3)])

threads = [threading.Thread(target=lambda n=n: (n.solicitar_sc(), n.sair_sc())) for n in nos]
for t in threads: t.start()
for t in threads: t.join()
```

---

## 4. Algoritmos de Eleição

Usados para escolher um **coordenador/líder** entre processos, tipicamente após a falha do líder anterior. Assumem que cada processo tem um identificador único e comparável.

| Algoritmo | Topologia assumida | Ideia central | Complexidade de mensagens |
|---|---|---|---|
| **Bully (Valentão)** | Rede totalmente conectada | O processo com **maior ID** vence; quem detecta falha do líder "atropela" os de ID menor | Pior caso: O(N²) |
| **Anel (Ring)** | Processos logicamente em anel | Mensagem de eleição circula coletando IDs; o maior ID visto vence e o resultado circula de novo anunciando o vencedor | 2(N−1) no melhor caso, até 3N no pior |

### 4.1 Algoritmo Bully (Valentão)

```
Pi detecta que o coordenador caiu:
  1. Pi envia ELECTION para todos os processos com ID maior que o seu
  2. Se ninguém responder OK dentro do timeout -> Pi vence, envia COORDINATOR a todos
  3. Se algum processo de ID maior responder OK -> Pi desiste e espera o COORDINATOR
  4. Todo processo que recebe ELECTION de ID menor responde OK e inicia sua própria eleição
```

```python
class NoBully:
    def __init__(self, id_no, nos_dict):
        self.id = id_no
        self.nos = nos_dict   # dict {id: NoBully}
        self.ativo = True
        self.coordenador = None

    def iniciar_eleicao(self):
        if not self.ativo:
            return
        print(f"[No {self.id}] inicia eleição")
        maiores = [n for i, n in self.nos.items() if i > self.id and n.ativo]

        if not maiores:
            self.vencer_eleicao()
            return

        respondeu = False
        for n in maiores:
            if n.receber_election(self.id):
                respondeu = True

        if not respondeu:
            self.vencer_eleicao()
        # se alguém respondeu, aquele nó de ID maior continuará a eleição

    def receber_election(self, id_remetente):
        if not self.ativo:
            return False
        print(f"[No {self.id}] respondeu OK para {id_remetente} e assume a eleição")
        self.iniciar_eleicao()
        return True

    def vencer_eleicao(self):
        print(f"[No {self.id}] venceu! Anuncia COORDINATOR a todos")
        for n in self.nos.values():
            if n.ativo:
                n.coordenador = self.id


# Exemplo: 5 nós (ids 1..5), nó 5 (líder atual) cai, nó 2 detecta e inicia eleição
nos = {}
for i in range(1, 6):
    nos[i] = NoBully(i, nos)

nos[5].ativo = False   # líder falhou
nos[2].iniciar_eleicao()

print("Coordenador final:", nos[1].coordenador)  # esperado: 4
```

### 4.2 Algoritmo de Anel (Ring)

```
Pi detecta falha do coordenador:
  1. Pi cria mensagem ELECTION contendo seu próprio ID
  2. Envia ao próximo nó ativo do anel
  3. Cada nó que recebe ELECTION:
       - se seu ID não está na lista, adiciona seu ID e repassa adiante
       - se reconhece a mensagem como a que ele mesmo originou, escolhe o maior ID da lista
         como coordenador e envia uma mensagem COORDINATOR percorrendo o anel novamente
```

```python
class NoAnel:
    def __init__(self, id_no):
        self.id = id_no
        self.proximo = None   # referência para o próximo nó ativo no anel
        self.ativo = True
        self.coordenador = None

    def iniciar_eleicao(self):
        print(f"[No {self.id}] inicia eleição no anel")
        self.proximo.receber_election([self.id], self.id)

    def receber_election(self, lista_ids, id_origem):
        if self.id == id_origem:
            # mensagem deu a volta completa: decide o vencedor
            vencedor = max(lista_ids)
            print(f"[No {self.id}] fechou o anel. Vencedor: {vencedor}")
            self.proximo.receber_coordinator(vencedor, id_origem)
            return
        if self.ativo:
            lista_ids.append(self.id)
        self.proximo.receber_election(lista_ids, id_origem)

    def receber_coordinator(self, vencedor, id_origem):
        self.coordenador = vencedor
        if self.id == id_origem:
            print(f"[No {self.id}] anúncio de coordenador ({vencedor}) deu a volta e parou")
            return
        self.proximo.receber_coordinator(vencedor, id_origem)


# Monta um anel lógico com 4 nós: 10 -> 20 -> 30 -> 40 -> volta a 10
ids = [10, 20, 30, 40]
nos_anel = {i: NoAnel(i) for i in ids}
for idx, i in enumerate(ids):
    nos_anel[i].proximo = nos_anel[ids[(idx + 1) % len(ids)]]

nos_anel[20].iniciar_eleicao()
print("Coordenador visto por todos:", {i: n.coordenador for i, n in nos_anel.items()})
```

---

## 5. Pool de Threads (Thread Pool)

### 5.1 Teoria

| Conceito | Explicação |
|---|---|
| **Problema motivador** | Criar/destruir uma thread do zero para cada tarefa tem overhead real (alocação de pilha, registro no escalonador do SO); em cargas com muitas tarefas curtas isso degrada throughput e pode esgotar recursos |
| **Ideia do pool** | Manter um número **fixo** (ou limitado) de threads "trabalhadoras" (workers) já criadas, reutilizadas para várias tarefas ao longo do tempo, em vez de 1 thread nova por tarefa |
| **Fila de tarefas (task queue)** | Estrutura FIFO thread-safe onde os clientes depositam tarefas; os workers retiram (pop) tarefas da fila quando ficam ociosos |
| **Worker (thread trabalhadora)** | Loop infinito: pega uma tarefa da fila -> executa -> volta a esperar. Agnóstico ao tipo de tarefa que executa |
| **Padrão também chamado de** | *Replicated workers* / *worker-crew model* |
| **Analogia** | Uma cozinha de restaurante: em vez de contratar um cozinheiro novo a cada pedido, mantém-se uma equipe fixa; os pedidos (tarefas) esperam numa fila até um cozinheiro (worker) ficar livre |

**Benefícios:**
- Reuso de threads elimina custo repetido de criação/destruição
- Controle de recursos: limita quantas threads concorrentes existem (evita sobrecarga do SO)
- Desacopla quem produz tarefas de quem as executa (produtor/consumidor)

**Cuidados / trade-offs:**
- Tamanho do pool é um parâmetro de ajuste (poucos workers = fila cresce e atrasa; workers demais = overhead de troca de contexto e consumo de memória)
- Tarefa que trava (ex.: espera infinita) pode "prender" um worker indefinidamente
- Em Python (CPython), o **GIL (Global Interpreter Lock)** permite que só uma thread execute bytecode Python por vez — por isso um thread pool em Python só traz ganho real de desempenho em tarefas **I/O-bound** (rede, disco, banco de dados), pois a thread libera o GIL enquanto espera I/O. Para tarefas **CPU-bound**, o ganho é limitado; nesses casos usa-se `ProcessPoolExecutor` (multiprocessing) em vez de thread pool.

| Quando usar Thread Pool em Python | Quando usar Process Pool |
|---|---|
| Requisições HTTP, leitura/escrita de arquivos, consultas a banco de dados | Cálculos matemáticos pesados, processamento de imagem, compressão |
| Tarefas passam a maior parte do tempo **esperando** (I/O) | Tarefas passam a maior parte do tempo **calculando** (CPU) |

### 5.2 Implementação "manual" simples (para entender o mecanismo interno)

```python
import threading
import queue
import time

class PoolDeThreadsSimples:
    def __init__(self, n_workers):
        self.tarefas = queue.Queue()
        self.workers = []
        for i in range(n_workers):
            t = threading.Thread(target=self._loop_worker, args=(i,), daemon=True)
            t.start()
            self.workers.append(t)

    def _loop_worker(self, worker_id):
        while True:
            funcao, args = self.tarefas.get()      # bloqueia até haver tarefa
            if funcao is None:                       # sinal de encerramento
                break
            print(f"[Worker {worker_id}] executando tarefa {args}")
            funcao(*args)
            self.tarefas.task_done()

    def submeter(self, funcao, *args):
        self.tarefas.put((funcao, args))

    def encerrar(self, n_workers):
        self.tarefas.join()                          # espera todas as tarefas terminarem
        for _ in range(n_workers):
            self.tarefas.put((None, ()))             # acorda cada worker para poder sair


def tarefa_exemplo(n):
    time.sleep(0.3)
    print(f"  -> tarefa {n} concluída")

pool = PoolDeThreadsSimples(n_workers=3)
for i in range(6):
    pool.submeter(tarefa_exemplo, i)
pool.encerrar(3)
```

### 5.3 Usando `concurrent.futures.ThreadPoolExecutor` (forma idiomática em Python)

```python
from concurrent.futures import ThreadPoolExecutor, as_completed
import time

def baixar_arquivo(arquivo_id):
    time.sleep(0.5)  # simula I/O (ex.: download)
    return f"arquivo_{arquivo_id}.txt baixado"

with ThreadPoolExecutor(max_workers=4) as executor:
    futures = {executor.submit(baixar_arquivo, i): i for i in range(8)}

    for future in as_completed(futures):
        resultado = future.result()
        print(resultado)
# ao sair do "with", o pool aguarda todas as tarefas e encerra automaticamente
```

```python
# Forma ainda mais simples com .map() quando não precisa dos Futures individualmente
from concurrent.futures import ThreadPoolExecutor
import time

def processar(item):
    time.sleep(0.2)
    return item * 2

with ThreadPoolExecutor(max_workers=5) as executor:
    resultados = list(executor.map(processar, range(10)))

print(resultados)
```

---

## 6. Referências para aprofundamento

- Lamport, L. *"Time, Clocks, and the Ordering of Events in a Distributed System"*, Communications of the ACM, 1978.
- Ricart, G.; Agrawala, A. *"An Optimal Algorithm for Mutual Exclusion in Computer Networks"*, Communications of the ACM, 1981.
- Garcia-Molina, H. *"Elections in a Distributed Computing System"* (algoritmo Bully), IEEE Transactions on Computers, 1982.
- Documentação oficial: `concurrent.futures` — https://docs.python.org/3/library/concurrent.futures.html
- Wikipedia: *Lamport timestamp*, *Vector clock*, *Ricart–Agrawala algorithm*, *Thread pool*
