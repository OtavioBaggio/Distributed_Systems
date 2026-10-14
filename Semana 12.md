# Semana 12 - Aula 14/10

## Resuminho Sistemas Distribuídos

---

## 1. String, StringBuffer e StringBuilder

| | **String** | **StringBuffer** | **StringBuilder** |
|---|---|---|---|
| Mutabilidade | **Imutável** — cada alteração cria um novo objeto | Mutável | Mutável |
| Thread-safe? | Sim (por ser imutável) | **Sim** — métodos `synchronized` | **Não** |
| Desempenho em alterações | Lento (muitos objetos criados) | Médio (custo da sincronização) | **Mais rápido** |
| Quando usar | Texto que não muda (constantes, chaves, mensagens fixas) | Texto alterado por **várias threads** ao mesmo tempo | Texto montado/alterado em **uma única thread** (ex.: concatenação em loop) |

```java
// String: cada += cria um objeto novo
String s = "";
for (int i = 0; i < 5; i++) {
    s += i;
}

// StringBuilder: altera o mesmo objeto (uma thread)
StringBuilder sb = new StringBuilder();
for (int i = 0; i < 5; i++) {
    sb.append(i);
}
String resultado = sb.toString();

// StringBuffer: mesma interface, mas sincronizado (várias threads)
StringBuffer buffer = new StringBuffer();
buffer.append("mensagem ").append("compartilhada");
```

> **Regra prática:** não vai mudar → `String`; vai mudar numa thread só → `StringBuilder`; vai mudar com várias threads mexendo no mesmo objeto → `StringBuffer`.

---

## 2. Sistemas Distribuídos

- **Heterogêneos:** as máquinas podem ter hardware, sistemas operacionais, linguagens e redes diferentes, e mesmo assim precisam se comunicar.

```
Sistemas Distribuídos
├── Cliente-Servidor
│   ├── TCP/IP (ip, domínio, porta, socket, UDP, TCP)
│   ├── Manual: socket + leitor + escritor + serialização
│   └── RPC
│       ├── rpcPython
│       └── RMI (Java)
└── Ponto-a-Ponto
    └── Multicast (grupo: ip classe D + porta)
```

---

## 3. Cliente-Servidor

### Conceitos de TCP/IP

| Conceito | Descrição |
|---|---|
| **IP** | Endereço da máquina na rede |
| **Domínio** | Nome legível que aponta para um IP |
| **Porta** | Identifica o processo/serviço dentro da máquina |
| **Socket** | Ponto de comunicação (IP + porta) |
| **TCP** | Orientado a conexão, confiável, garante ordem |
| **UDP** | Sem conexão, mais leve, sem garantia de entrega |

### Objetos usados

| Objeto | Papel |
|---|---|
| `Socket` | Lado cliente da conexão (e a conexão aceita no servidor) |
| `ServerSocket` | Servidor fica escutando a porta e aceita clientes (`accept()`) |
| Escritor de socket | Envia dados pelo socket (stream de saída) |
| Leitor de socket | Recebe dados do socket (stream de entrada) |
| Serialização | Transforma objetos em bytes para trafegar na rede (`Serializable`) |
| Threads | Permitem atender vários clientes / enviar e receber ao mesmo tempo |

### Comunicação manual

```
socket + leitor + escritor + serialização = comunicação TCP ou UDP
```

### RPC (Remote Procedure Call)

> Chamar um método de outra máquina como se fosse local — esconde o socket/leitor/escritor do programador.

| Tecnologia | Linguagem |
|---|---|
| **rpcPython** | Python |
| **RMI** | Java |

**Estrutura do RMI:**

| Parte | Responsabilidade |
|---|---|
| **Interface** | Define os métodos remotos (o "contrato") |
| **Classe** | Implementa a interface |
| **Servidor** | Cria o objeto e o registra para acesso remoto |
| **Cliente** | Localiza o objeto remoto e chama seus métodos |

```
Interface ──► Classe (implementa) ──► Servidor (registra) ◄── Cliente (localiza e chama)
```

---

## 4. Ponto-a-Ponto

### Multicast

| Item | Descrição |
|---|---|
| **Grupo** | IP de classe D (convenção `239.X.Y.W`) + porta de envio |
| **Socket** | `MulticastSocket` |
| **Escritor** | Envia mensagens ao grupo (`send`) |
| **Leitor** | Recebe mensagens do grupo (`receive`) |

> Detalhes e exemplos de código: **Semana 11 - Aula 09/10 (Multicast)**.

---

### Resumo rápido

| Arquitetura | Comunicação | Ferramentas |
|---|---|---|
| Cliente-Servidor (manual) | TCP ou UDP | Socket, ServerSocket, leitor, escritor, serialização, threads |
| Cliente-Servidor (RPC) | Chamada remota de métodos | rpcPython, RMI |
| Ponto-a-Ponto | Multicast (UDP) | MulticastSocket, grupo (IP + porta), leitor, escritor |
