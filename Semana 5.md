# Sistemas Distribuídos — Comunicação entre Máquinas

## 1. Comunicação entre máquinas

A **comunicação entre máquinas** é um dos principais focos de **Sistemas Distribuídos**. Ela permite que diferentes computadores troquem informações e compartilhem recursos.

### Compartilhamento de recursos

Um sistema distribuído pode compartilhar:

* **Memória**
* **Processador (CPU)**
* **GPU**
* Outros recursos computacionais

---

## 2. Modelo TCP/IP

O **modelo TCP/IP** organiza o funcionamento da comunicação em redes de computadores.

Em Sistemas Distribuídos, há um foco especial na **camada de transporte**, responsável pela comunicação entre aplicações.

### Camadas

* **Enlace**
* **Rede**
* **Transporte**
* **Sessão**
* **Apresentação**
* **Aplicação**
* **Middleware**

> **Middleware** é importante para a programação distribuída porque fornece **APIs e mecanismos que facilitam a comunicação entre sistemas**.

### Pacote

Um pacote de dados pode ser entendido, de forma simplificada, como contendo:

* **Remetente**
* **Destinatário**
* **Conteúdo**

A comunicação utiliza informações como:

* **Endereço IP** → identifica uma máquina na rede.
* **Porta lógica** → identifica um serviço/aplicação dentro da máquina.
* **Cliente** → normalmente inicia uma requisição.
* **Servidor** → normalmente aguarda e responde às requisições.

---

# 3. Formas de comunicação

Existem diferentes mecanismos para realizar a comunicação entre máquinas.

## 3.1 Comunicação orientada a mensagens

A comunicação ocorre por meio da troca de **mensagens** entre os participantes.

Um dos principais mecanismos utilizados é o **Socket**.

```text
Cliente → Mensagem → Servidor
Cliente ← Mensagem ← Servidor
```

---

## 3.2 Chamada de procedimento remoto (RPC)

A **RPC (Remote Procedure Call)** permite que um programa execute um procedimento que está em outra máquina como se estivesse fazendo uma chamada local.

Como os dados e parâmetros precisam atravessar a rede, é necessário utilizar mecanismos como a **serialização**.

Exemplos:

* **RPC** → Python
* **RMI (Remote Method Invocation)** → Java
* **SOAP** → XML

---

# 4. Serialização

A **serialização** é o processo de transformar um **objeto ou estrutura de dados** em uma sequência de dados que possa ser **armazenada ou transmitida pela rede**.

Em sistemas distribuídos, um objeto que está na memória de uma máquina não pode ser simplesmente enviado diretamente para outra máquina. Seus dados precisam ser convertidos para um formato transmissível.

### Fluxo básico

```text
Máquina A

Objeto
  ↓
Serialização
  ↓
Dados / bytes
  ↓
Rede
  ↓
Dados / bytes
  ↓
Desserialização
  ↓
Objeto

Máquina B
```

### Desserialização

A **desserialização** é o processo inverso da serialização:

> Converte os dados recebidos novamente em um objeto ou estrutura que possa ser utilizada pela aplicação.

### Exemplo

Considere um objeto:

```text
Pessoa
├── nome = "João"
└── idade = 20
```

Antes de ser enviado:

```text
Pessoa
   ↓
Serialização
   ↓
Sequência de dados / bytes
   ↓
Rede
```

Ao chegar ao destino:

```text
Sequência de dados / bytes
   ↓
Desserialização
   ↓
Pessoa
```

### Serialização em chamadas remotas

Em mecanismos como **RPC** e **RMI**, os parâmetros de uma chamada precisam ser serializados antes de serem enviados.

```text
Cliente
   │
   │ chama método remoto
   ↓
Serialização dos parâmetros
   │
   ↓
Rede
   │
   ↓
Desserialização
   │
   ↓
Servidor executa o método
   │
   ↓
Serialização do resultado
   │
   ↓
Rede
   │
   ↓
Desserialização
   │
   ↓
Cliente recebe o resultado
```

### Exemplos

* **Java RMI** → utiliza serialização de objetos.
* **RPC** → precisa representar parâmetros e resultados em formato transmissível.
* **SOAP** → utiliza **XML** para representar os dados transmitidos.

### Resumo

| Conceito            | Função                                     |
| ------------------- | ------------------------------------------ |
| **Serialização**    | Objeto/dados → formato transmissível       |
| **Transmissão**     | Envio dos dados pela rede                  |
| **Desserialização** | Dados recebidos → objeto/dados utilizáveis |

> **Ideia principal:** serialização permite transformar o estado de um objeto em uma representação que pode ser **transmitida pela rede**, enquanto a desserialização reconstrói os dados no destino.

---

# 5. Tipos de comunicação

A comunicação pode ser classificada considerando **sincronismo** e **persistência**.

## 5.1 Quanto ao sincronismo

### Comunicação síncrona

O remetente **espera uma resposta/retorno** do destinatário.

Características:

* Associada, nas anotações, ao **TCP**.
* Geralmente possui comportamento **bloqueante**.
* O processo pode esperar uma confirmação ou resposta.
* Pode utilizar **buffers** para armazenar temporariamente os dados.

Exemplo:

```text
Cliente
   │
   │ requisição
   ↓
Servidor
   │
   │ resposta
   ↓
Cliente continua
```

### Comunicação assíncrona

O remetente **não precisa esperar uma resposta imediata**.

Características:

* Associada, nas anotações, ao **UDP**.
* Geralmente possui comportamento **não bloqueante**.
* É adequada para aplicações em que a velocidade é importante.
* Exemplos:

  * Áudio
  * Vídeo
  * Streaming

Exemplo:

```text
Cliente
   │
   │ dados
   ↓
Servidor

Cliente continua sua execução
```

> **Atenção:** TCP e UDP são protocolos da camada de transporte. "Síncrona" e "assíncrona" descrevem o comportamento da comunicação. A associação TCP = síncrona e UDP = assíncrona é uma simplificação didática.

---

## 5.2 Quanto à persistência

### Comunicação transiente

A mensagem só é enviada se o destinatário **estiver disponível/online**.

Características:

* O destinatário precisa existir e estar disponível no momento da comunicação.
* Se estiver desligado ou offline, a mensagem pode ser perdida.

```text
Remetente
    │
    ↓
Mensagem
    │
    ↓
Destinatário online
```

### Comunicação persistente

A mensagem pode ser enviada mesmo que o destinatário esteja **offline ou desligado**.

Características:

* A mensagem pode ser armazenada até o destinatário ficar disponível.
* É comum em arquiteturas baseadas em **cliente-servidor** e sistemas de mensagens.

```text
Remetente
    │
    ↓
Servidor
    │
    ↓
Mensagem armazenada
    │
    ↓
Destinatário conecta
    │
    ↓
Recebe a mensagem
```

---

# 6. Tratamento de sincronismo

Em sistemas distribuídos, diferentes processos ou **threads** podem acessar recursos simultaneamente.

O **Sistema Operacional (SO)** fornece mecanismos para controlar essa concorrência.

Principais mecanismos:

* **Semáforos**
* **Monitores**

Eles ajudam a:

* Controlar o acesso a recursos compartilhados.
* Evitar conflitos entre processos.
* Coordenar a execução concorrente.
* Garantir sincronização.

---

# 7. Sockets

**Sockets** são mecanismos utilizados para permitir a comunicação entre processos, inclusive entre processos executando em máquinas diferentes.

Características:

* Utilizados no contexto de redes de computadores desde a década de **1980**.
* Trabalham com protocolos da **camada de transporte**, principalmente:

  * **TCP**
  * **UDP**
* São muito utilizados na arquitetura **cliente-servidor**.

---

## 7.1 Sockets em Java

Em Java, a comunicação utilizando sockets pode ser feita de maneira relativamente **explícita**, exigindo que o programador trate vários detalhes da comunicação.

O programador precisa lidar com:

* Endereço IP.
* Porta lógica.
* Estabelecimento da conexão.
* Leitura dos dados.
* Escrita dos dados.
* Fechamento da conexão.
* Sincronização.
* Threads, quando necessárias.

### Principais elementos

* **Classes**
* **Interfaces**
* **Métodos**
* **Atributos**

Esses elementos permitem implementar a comunicação entre máquinas.

---

# 8. Principais operações de Socket

| Operação             | Função                                                     |
| -------------------- | ---------------------------------------------------------- |
| `Socket`             | Representa o mecanismo utilizado para comunicação          |
| `bind()`             | Associa um socket a um endereço IP e uma porta             |
| `listen()`           | Coloca o socket do servidor aguardando conexões            |
| `accept()`           | Aceita uma conexão de um cliente; normalmente é bloqueante |
| `connect()`          | Inicia uma conexão com o servidor                          |
| `read()` / `INPUT`   | Lê dados recebidos pelo socket                             |
| `write()` / `OUTPUT` | Envia/escreve dados através do socket                      |
| `close()`            | Encerra a conexão                                          |

---

# 9. Fluxo básico de comunicação com TCP

Em uma arquitetura **cliente-servidor**, o fluxo básico pode ser representado assim:

## Servidor

```text
Socket
  ↓
bind()
  ↓
listen()
  ↓
accept()
  ↓
read() / write()
  ↓
close()
```

## Cliente

```text
Socket
  ↓
connect()
  ↓
write() / read()
  ↓
close()
```

## Comunicação completa

```text
                 REDE
                  │
      ┌───────────┴───────────┐
      │                       │
   CLIENTE                  SERVIDOR
      │                       │
   Socket                  Socket
      │                       │
 connect()                bind()
      │                       │
      ├───────────────────→ listen()
      │                       │
      │                    accept()
      │                       │
   write() ───────────────→ read()
      │                       │
   read()  ←────────────── write()
      │                       │
   close()                close()
```

---

# 10. Resumo para prova

## Sistemas Distribuídos

> Permitem que diferentes máquinas/processos se comuniquem e compartilhem recursos.

## Comunicação

Pode ocorrer por:

* **Mensagens**
* **Sockets**
* **Chamadas remotas**
* **RPC**
* **RMI**
* **SOAP**

## Serialização

> **Objeto → dados/bytes → rede → dados/bytes → objeto**

É necessária quando objetos ou estruturas precisam ser representados em um formato que possa ser transmitido.

## Sincronismo

| Tipo           | Característica               | Associação didática |
| -------------- | ---------------------------- | ------------------- |
| **Síncrona**   | Espera resposta/retorno      | TCP                 |
| **Assíncrona** | Não espera resposta imediata | UDP                 |

## Persistência

| Tipo            | Característica                                                   |
| --------------- | ---------------------------------------------------------------- |
| **Transiente**  | Destinatário precisa estar disponível                            |
| **Persistente** | Mensagem pode ser armazenada até o destinatário ficar disponível |

## Sockets

### Servidor

```text
bind → listen → accept → read/write → close
```

### Cliente

```text
connect → read/write → close
```

## Protocolos

### TCP

* Orientado à conexão.
* Confiável.
* Garante ordenação e entrega dos dados.
* Associado, nas anotações, à comunicação síncrona.

### UDP

* Não orientado à conexão.
* Menor overhead.
* Não garante entrega ou ordenação.
* Adequado para aplicações como áudio, vídeo e streaming.
* Associado, nas anotações, à comunicação assíncrona.

---

# 11. Conceitos-chave

Para revisar antes da prova:

**Sistemas Distribuídos**
→ comunicação entre máquinas
→ compartilhamento de recursos
→ TCP/IP
→ camada de transporte
→ TCP / UDP
→ cliente-servidor
→ sockets
→ sincronismo
→ persistência
→ threads
→ semáforos / monitores
→ serialização / desserialização
→ RPC / RMI / SOAP
→ middleware
