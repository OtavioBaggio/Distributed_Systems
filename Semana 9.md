# Complemento das Anotações — Comunicador e Autenticadores

## Comunicador

O comunicador é o módulo responsável por realizar a troca de informações entre cliente e servidor.

### Funções
- Enviar dados
- Receber dados
- Gerenciar a comunicação entre as aplicações

### Tipos de dados que podem ser enviados
- **TCP**
  - Comunicação orientada à conexão.
  - Garante a entrega dos dados.
  - Mais confiável, porém um pouco mais lento.

- **UDP**
  - Comunicação sem conexão.
  - Não garante a entrega dos pacotes.
  - Mais rápido.
  - Ideal para aplicações onde velocidade é mais importante que confiabilidade.

- **Data Object**
  - Objeto utilizado para transportar informações entre cliente e servidor.
  - Pode conter diversos campos, como:
    - nome
    - email
    - token
    - mensagem
    - data/hora
  - Também é conhecido como DTO (Data Transfer Object).

---

# Autenticadores

O autenticador é responsável por gerar códigos temporários (Tokens) para validar a identidade de um usuário.

## Funcionamento

1. Existe um relógio (Timer).
2. A cada **X segundos** ele executa uma ação.
3. É gerado um novo Token.
4. O Token é enviado ao usuário.

Fluxo:

```
Relógio
    ↓
Tempo expirou
    ↓
Gerar Token
    ↓
Enviar Token
```

---

## Token

Um **Token** é um código temporário utilizado para autenticação.

Exemplos:

```
582143
AB29KF
98DFA1
```

Características:

- possui tempo de validade;
- muda periodicamente;
- normalmente é único para cada usuário.

---

## hashCode()

Nas anotações foi citado:

```java
hashCode()
```

O método `hashCode()` do Java gera um número inteiro baseado no conteúdo de um objeto.

Exemplo:

```java
String email = "usuario@email.com";

System.out.println(email.hashCode());
```

Saída (exemplo):

```
153829481
```

### Observação

Embora possa ser usado para gerar valores diferentes, **o `hashCode()` sozinho não é seguro para autenticação**, pois não foi criado para criptografia.

Em aplicações reais normalmente são utilizados:

- UUID
- SHA-256
- HMAC
- TOTP (Google Authenticator)

Mas, para fins da disciplina, o professor pode aceitar a utilização do `hashCode()` para gerar um token simples.

---

# Threads

Cada usuário deverá possuir uma Thread responsável pela geração dos seus Tokens.

Exemplo:

```
Servidor

Usuário A
    Thread A
        gera Token

Usuário B
    Thread B
        gera Token

Usuário C
    Thread C
        gera Token
```

Assim todos recebem novos Tokens ao mesmo tempo.

---

# Timer

O Timer controla o intervalo de geração.

Exemplo:

```
Tempo = 30 segundos

00:00 -> Token 1
00:30 -> Token 2
01:00 -> Token 3
01:30 -> Token 4
```

---

# Fluxo Geral do Exercício

```
Cliente
    ↓
Cadastro de usuário
    ↓
Servidor salva usuário
    ↓
Servidor cria Thread
    ↓
Thread gera Token periodicamente
    ↓
Cliente informa email
    ↓
Servidor localiza usuário
    ↓
Servidor envia Token via UDP
```

---

# Requisitos do Exercício (explicados)

## 1. O servidor não precisa ter interface gráfica

Pode ser executado apenas pelo terminal (console).

Exemplo:

```
Servidor iniciado...

Usuário cadastrado:
Maria

Novo Token:
Maria -> 847291
```

---

## 2. O usuário precisa estar cadastrado

Antes de solicitar um Token, o usuário deve existir na lista do servidor.

Caso contrário:

```
Usuário não encontrado.
```

---

## 3. Tokens diferentes para cada usuário

Mesmo que dois usuários recebam Tokens no mesmo instante:

```
João
Token: 192845

Maria
Token: 736194

Pedro
Token: 421857
```

Cada um deve possuir um Token próprio.

---

## 4. Geração simultânea via Threads

Cada Thread funciona de maneira independente.

```
Servidor

Thread João
    gera Token

Thread Maria
    gera Token

Thread Pedro
    gera Token
```

Todas executam paralelamente.

---

## 5. Interface do Cliente

A interface deve possuir duas áreas principais.

### Cadastro

Campos:

- Nome
- Email

Botão:

```
Cadastrar
```

---

### Solicitação do Token

Campo:

```
Email
```

Botão:

```
Receber Token
```

---

# Comunicação via UDP

Toda comunicação entre cliente e servidor deverá utilizar o protocolo UDP.

Fluxo:

```
Cliente
      │
      │ UDP
      ▼
Servidor

Servidor
      │
      │ UDP
      ▼
Cliente
```

Não será utilizado TCP neste exercício.

---

# Organização sugerida do projeto

```
Servidor
│
├── Main
├── ServidorUDP
├── Usuario
├── GeradorToken
├── ThreadToken
└── Comunicador

Cliente
│
├── Main
├── ClienteUDP
├── Interface
├── Comunicador
└── Usuario
```

---

# Conceitos importantes para revisar

- Comunicação Cliente ↔ Servidor
- UDP
- Socket UDP
- DatagramPacket (Java)
- Threads
- Timer
- hashCode()
- Token
- Autenticação
- Cadastro de usuários
- Data Transfer Object (DTO)
- Comunicação em rede
