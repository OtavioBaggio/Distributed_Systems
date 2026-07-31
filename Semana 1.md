# SISTEMAS DISTRIBUÍDOS

---

# AULA 1

## Notas da disciplina

- Todos os códigos devem possuir documentação (**JavaDoc**);
- Todos os códigos devem ser desenvolvidos utilizando **Orientação a Objetos (POO)**;
- As anotações das aulas deverão estar no **GitHub pessoal** do aluno (serão avaliadas);
- Anotações marcadas com **(*)** são consideradas importantes.

---

# ARQUITETURA DE SISTEMAS

## MVC (Model, View, Controller)

- **Model (M):** dados
- **View (V):** interface
- **Controller (C):** regras de negócio

---

## 1) Cliente-Servidor

**Modelo TCP/IP** → modelo prático

**Modelo OSI** → modelo teórico (7 camadas)

### TCP/IP (4 camadas)

- Aplicação
- Transporte
- Internet
- Acesso à Rede

### Camada de Aplicação

Utilizando frameworks, garante:

- Boas práticas;
- Reuso de código;
- Releases constantes.

---

## 2) Ponto-a-Ponto (P2P)

Modelo: **TCP/IP**

Exemplos:

- BitTorrent;
- Firebase.

### Comunicação

**Enviar = send() = write()**

Pode enviar:

- bytes;
- String (serializada);
- Objetos (serializados).

> Serializar = transformar um objeto em bytes para transmissão ("salame fatiado").

**Receber = receive() = read()**

- Desserializa os dados recebidos.

---

### Concomitante ≠ Paralelo

- **Concomitante:** várias tarefas alternam execução.
- **Paralelo:** várias tarefas executam ao mesmo tempo.

---

# THREADS

- São mini processos (fluxos de execução).

Cada thread possui:

- **ID (*)**
- nome
- memória + CPU
- tempo de execução
- processo pai

Pode ser:

- criada;
- iniciada;
- pausada;
- reiniciada;
- finalizada (morta).

---

## Threads sem compartilhamento de memória

- Independentes;
- Menor necessidade de sincronização.

---

## Threads com compartilhamento de memória

Necessitam de sincronismo.

### Bloqueio

- Monitor (`synchronized`);
- Semáforo;
- Deadlock.

---

# AULA 2

## Qual a diferença entre Sistemas Paralelos e Sistemas Distribuídos?

# SISTEMAS PARALELOS

- Homogêneos:
  - mesmo hardware;
  - mesmo sistema operacional;
  - mesma linguagem (geralmente).

- Fortemente acoplados;
- Computadores no mesmo ambiente físico;
- Comunicação via TCP/IP:
  - endereço IP;
  - porta lógica;
  - máscara de rede;
  - protocolos de transporte.

### Cluster Computacional

Arquitetura geralmente Ponto-a-Ponto.

Objetivos:

- Tolerância a falhas;
- Escalabilidade;
- Segurança;
- Manutenção/atualização.

> Cluster ≠ Grid  
> Cluster = máquinas próximas trabalhando juntas.

---

# SISTEMAS DISTRIBUÍDOS

- Heterogêneos:
  - hardwares diferentes;
  - sistemas operacionais diferentes;
  - linguagens diferentes.

- Fracamente acoplados;
- Distribuídos geograficamente;
- Comunicação via TCP/IP:
  - endereço IP;
  - porta lógica;
  - máscara de rede;
  - protocolos de transporte.

### Grid Computacional

Modelo de computação distribuída que une vários computadores através da rede para trabalharem em conjunto.

### Arquiteturas

- Cliente-Servidor;
- Ponto-a-Ponto (P2P);
- Híbrida.

Objetivos:

- Tolerância a falhas;
- Escalabilidade;
- Segurança;
- Manutenção/atualização.

---

## Objetivo

Compartilhar recursos:

- processador;
- memória.

Ao compartilhar recursos, é necessário controlar o **SINCRONISMO**.

### Sincronismo

- Relógio:
  - lógico;
  - físico.

- Recurso:
  - exclusão mútua.

---

## Dependência do Sistema Operacional

Os Sistemas Distribuídos dependem fortemente do SO para:

- gerenciamento de processamento;
- gerenciamento de comunicação;
- gerenciamento das camadas de serviço.

### Observação (*)

A comunicação em Sistemas Distribuídos é feita, na essência, através de **Sockets**.

Socket utiliza:

- IP;
- Porta;
- Objetos de leitura/escrita.

A comunicação é **BLOQUEANTE**.

Solução durante a programação:

- uso de **Threads**.

---

# Características básicas

## Arquiteturas

- Cliente-Servidor;
- Ponto-a-Ponto (P2P);
- Híbrida.

---

## Comunicação bloqueante

- Escrever (`write()`);
- Ler (`read()`).

---

## Programação multitarefa (Threads)

- Thread = mini processo dentro de um processo;
- Permite execução concomitante;
- Libera a comunicação bloqueante em Sistemas Distribuídos.

### Thread com memória compartilhada

Necessita sincronismo:

- Monitor;
- Semáforo.

### Thread sem memória compartilhada

- Execução independente.
