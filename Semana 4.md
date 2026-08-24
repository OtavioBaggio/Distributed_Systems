# SEMANA 4 

# Resumo para a avaliação – Teoria Básica de Sistemas Distribuídos (com exemplos)

# Sistemas Distribuídos

Um **sistema distribuído** é um conjunto de computadores ou dispositivos independentes que trabalham juntos para executar tarefas e compartilhar recursos.

Cada computador possui seu próprio processamento e memória, mas eles conseguem se comunicar através de uma rede para realizar uma atividade em conjunto.

## Objetivo

O principal objetivo é compartilhar recursos:

- **CPU** → utilizar o poder de processamento de outras máquinas.
- **Memória RAM** → distribuir o uso de memória entre diferentes computadores.
- **Memória secundária** → compartilhar dados armazenados.

## Exemplo

Um serviço de streaming como a Netflix possui milhares de servidores.

Quando um usuário assiste a um filme:

- Um servidor pode armazenar o vídeo.
- Outro pode autenticar o usuário.
- Outro pode processar recomendações.

Para o usuário, tudo parece ser um único sistema, mas existem várias máquinas trabalhando juntas.

---

# Computação Concomitante x Computação Paralela

## Computação Concomitante

A computação concomitante ocorre quando várias tarefas avançam durante o mesmo período de tempo, mas não necessariamente executam exatamente no mesmo instante.

Normalmente utiliza **Threads**.

## Exemplo

Um navegador de internet:

Enquanto o usuário está assistindo um vídeo:

- Uma Thread carrega o vídeo.
- Outra Thread atualiza a interface.
- Outra Thread recebe comandos do teclado.

As tarefas acontecem de forma organizada e concorrente.

---

## Computação Paralela

A computação paralela acontece quando várias tarefas são executadas simultaneamente por diferentes unidades de processamento.

## Exemplo

Um programa precisa somar uma lista com 1 milhão de números.

Forma sequencial:

```
CPU:
1 + 2 + 3 + 4 + 5 ... até 1 milhão
```

Forma paralela:

```
CPU 1 → soma parte 1
CPU 2 → soma parte 2
CPU 3 → soma parte 3
CPU 4 → soma parte 4
```

Depois os resultados são unidos.

---

# Grid x Cluster

## Grid Computing

Um Grid utiliza computadores distribuídos, normalmente localizados em diferentes lugares.

Os recursos são compartilhados através da rede.

## Exemplo

Projeto de pesquisa científica:

Milhares de computadores voluntários cedem parte do processamento quando estão ociosos.

Cada computador executa uma pequena parte do cálculo.

---

## Cluster Computing

Um Cluster é formado por vários computadores próximos, trabalhando como se fossem uma única máquina.

Normalmente possui alta velocidade de comunicação.

## Exemplo

Um laboratório possui:

```
Servidor principal
       |
-----------------
|       |       |
PC1    PC2     PC3
```

Cada computador executa uma parte do processamento.

É utilizado em:

- Pesquisas científicas.
- Inteligência artificial.
- Simulações.

---

# Comunicação em Sistemas Distribuídos

Para computadores diferentes trocarem informações, eles precisam utilizar protocolos de comunicação.

O principal modelo utilizado é o **TCP/IP**.

---

# Endereço IP

O endereço IP identifica um dispositivo na rede.

## Exemplo

```
192.168.1.10
```

Um computador envia uma mensagem para esse endereço para encontrar o destino correto.

É semelhante ao endereço de uma casa:

- Rua → Rede.
- Número da casa → Dispositivo.

---

# Porta

A porta identifica qual aplicação receberá a comunicação.

Um computador pode executar vários serviços ao mesmo tempo.

## Exemplo

Mesmo computador:

```
IP: 192.168.1.10

Porta 80  → Servidor Web
Porta 25  → E-mail
Porta 3306 → Banco MySQL
```

O IP encontra o computador.

A porta encontra o programa.

---

# Máscara de Rede

Define quais endereços pertencem à mesma rede.

## Exemplo

```
IP:
192.168.1.10

Máscara:
255.255.255.0
```

Significa que dispositivos:

```
192.168.1.1
192.168.1.20
192.168.1.50
```

fazem parte da mesma rede.

---

# Socket

Um socket é um ponto de comunicação entre aplicações.

Ele combina:

- Endereço IP.
- Porta.

## Exemplo

Um navegador acessa:

```
Servidor:
IP: 142.250.79.14
Porta: 443
```

O socket identifica exatamente para onde enviar os dados.

---

# TCP x UDP

## TCP

É orientado à conexão e garante confiabilidade.

Características:

- Confirma recebimento.
- Reenvia dados perdidos.
- Mantém ordem das mensagens.

## Exemplo

Download de um arquivo:

Se um pacote chegar errado, o TCP solicita novamente.

---

## UDP

É mais rápido, mas não garante entrega.

Características:

- Não verifica se chegou.
- Não corrige erros.
- Menor atraso.

## Exemplo

Jogos online:

É melhor perder um pacote de posição do jogador do que atrasar a partida.

---

# Comunicação Bloqueante

Uma comunicação bloqueante faz uma tarefa esperar até receber uma resposta.

## Exemplo

Um programa tenta ler uma mensagem:

```
Consumidor:
"Preciso receber dados"

↓

Aguarda...

↓

Produtor:
"Enviei os dados"

↓

Consumidor continua
```

Enquanto a informação não chega, o consumidor fica parado.

---

# Threads

Uma Thread é uma unidade de execução dentro de um processo.

Ela permite dividir um programa em várias tarefas menores.

## Exemplo

Um editor de texto:

Thread 1:
- Digitar texto.

Thread 2:
- Salvar automaticamente.

Thread 3:
- Verificar ortografia.

Todas pertencem ao mesmo programa.

---

# Threads sem memória compartilhada

Cada Thread trabalha com seus próprios dados.

## Exemplo

Duas Threads calculando:

```
Thread 1:
Lista A → soma

Thread 2:
Lista B → soma
```

Uma não interfere na outra.

---

# Threads com memória compartilhada

As Threads acessam os mesmos dados.

## Exemplo

Um sistema bancário:

```
Saldo:
R$1000
```

Thread 1:
- Faz saque de R$500.

Thread 2:
- Faz saque de R$700.

Se ocorrer ao mesmo tempo, pode gerar erro.

Por isso é necessário sincronizar.

---

# Seção Crítica

A seção crítica é o trecho do código que acessa dados compartilhados.

## Exemplo

Variável:

```
contador = 10
```

Duas Threads executam:

```
Thread A:
contador + 1

Thread B:
contador + 1
```

Sem controle, o resultado pode ser:

```
11
```

quando deveria ser:

```
12
```

---

# Sincronismo

É o controle utilizado para organizar o acesso das Threads.

---

# Lock

Um Lock funciona como uma chave.

Quando uma Thread entra:

```
🔒 Recurso ocupado
```

Outras esperam.

Quando termina:

```
🔓 Recurso liberado
```

## Exemplo

Um arquivo sendo salvo:

- Apenas uma Thread pode escrever por vez.

---

# Relógios em Sistemas Distribuídos

Cada computador possui seu próprio relógio.

O problema:

```
Computador A:
10:00:01

Computador B:
10:00:03
```

Qual evento aconteceu primeiro?

---

# Relógio Físico

Utiliza o tempo real.

## Exemplo

Servidores sincronizados pelo relógio mundial.

Problema:

- Pode existir diferença de alguns milissegundos.

---

# Relógio Lógico de Lamport

Não tenta descobrir o horário real.

Ele apenas organiza a ordem dos eventos.

## Exemplo

Evento A:

```
Enviar mensagem
```

Evento B:

```
Receber mensagem
```

O relógio lógico garante:

```
A aconteceu antes de B
```

---

# Exclusão Mútua

Garante que apenas uma Thread ou processo utilize um recurso por vez.

## Exemplo

Impressora compartilhada:

Sem exclusão:

```
Usuário A imprime
Usuário B imprime junto
```

Resultado:

Documento misturado.

Com exclusão:

```
Usuário A imprime
Depois usuário B imprime
```

---

# Eleição

Em sistemas distribuídos, pode ser necessário escolher um coordenador.

## Exemplo

Um servidor principal falha.

Os outros servidores precisam escolher um novo líder.

Processo:

```
Servidor A
Servidor B
Servidor C

↓

Escolhem Servidor B como líder
```

---

# Pool de Threads

Um Pool de Threads mantém várias Threads prontas para executar tarefas.

Ao invés de criar uma Thread toda vez:

```
Criar Thread
Executar
Destruir
Criar outra
```

Usa-se:

```
Thread Pool

Thread 1 → tarefa
Thread 2 → tarefa
Thread 3 → aguardando
```

---

# Exemplo de uso

Servidor web:

Mil usuários acessam ao mesmo tempo.

Sem Pool:

```
Usuário → cria Thread nova
Usuário → cria Thread nova
Usuário → cria Thread nova
```

Com Pool:

```
Servidor possui 100 Threads prontas.

Cada usuário utiliza uma disponível.
```

---

# Ideia Geral

Sistemas distribuídos permitem que várias máquinas trabalhem juntas.

Para isso é necessário controlar:

- Comunicação entre computadores.
- Execução concorrente através de Threads.
- Compartilhamento de memória.
- Sincronização.
- Exclusão mútua.
- Organização dos eventos através de relógios.
- Gerenciamento eficiente de Threads através de Pools.
