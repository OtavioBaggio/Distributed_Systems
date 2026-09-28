# Semana 10 - Aula 28/09
# RPC (Remote Procedure Call): Resumo

**Sistemas Distribuídos · Semana 10 · Aula de 28/09**
Base: anotações de aula + slides "Chamadas de Procedimentos Remotos em Python" (A. Zamberlan e G. Kurtz, UFN, Python Santa Maria 2019)

---

## 1. O que é RPC

Protocolo de comunicação que um programa usa para requisitar um serviço de outro programa, localizado em outro computador na rede, **sem se preocupar com os detalhes de rede**.

- Termo cunhado por **Bruce Jay Nelson (1981)**.
- Na computação antiga, as "funções" eram separadas em:
  - **Procedure** → não retorna valor (`void`)
  - **Function** → retorna valor (`return`)

### Como funciona (fluxo dos slides)

Cliente e servidor têm, cada um, um **stub** (no servidor, também chamado *skeleton*) que esconde a comunicação por **sockets**:

1. Uma *client function* chama o **client stub** (passos 1–2)
2. O stub empacota a chamada e envia pela rede via sockets (passo 3)
3. No servidor, o **server stub** desempacota e chama a *server function* (passos 4–5)
4. O resultado faz o caminho inverso (passos 6–10)

Ideia geral: `Call P(X, Y, Z)` do cliente → `Return(P)` do servidor.

## 2. Conceitos atrelados

- Sistemas Distribuídos + Sistemas Operacionais + Redes
- Arquitetura **Cliente-Servidor**
- **Camada de transporte**
  - **TCP (síncrono)**: sistema *bloqueante*, o servidor processa e o cliente espera
  - **UDP (assíncrono)**: *não bloqueante*
- **Sockets**
- **Threads**
- **Programação Orientada a Objetos**
- **Serialização de objetos**

## 3. Implementações / variações

| Implementação | Origem / observações |
|---|---|
| **RPC tradicional** | O modelo original de chamada de procedimento remoto |
| **RMI** (Remote Method Invocation) | A versão do **Java**. É uma API que permite um objeto invocar métodos de outro objeto rodando em **outra JVM** |
| **XML-RPC** ("xRPC") | Nas anotações: desenvolvido pelo C#. Nos slides: atribuído à **Microsoft**, voltado a *business-to-business* (e-commerce), com a ideia de ser *human-readable-and-writable*, baseado em um script padrão que pode ser "parseado" |
| **RPC em Python** | Mesma funcionalidade do RPC tradicional e do RMI Java, porém **mais simples de usar** |

> **Nota:** No exemplo do professor, o Python usa o módulo `xmlrpc` (`SimpleXMLRPCServer` / `ServerProxy`), ou seja, o RPC do Python na prática trafega **XML sobre HTTP**.

## 4. Exemplo Java RMI (slides 13–16)

Serviço que devolve data/hora do servidor.

- **Interface remota** `IHoje_eh extends Remote`, com o método `pegaDataHora() throws RemoteException`
- **Implementação** `Hoje_eh extends UnicastRemoteObject implements IHoje_eh`
- **Objeto trafegado** `DataHora implements Serializable`, com os atributos `Date data` e `Time hora`. A serialização é o que permite o objeto ir pela rede
- **Servidor**
  - cria o registry (`LocateRegistry.createRegistry`)
  - instancia o objeto remoto
  - publica com `Naming.bind("rmi://localhost/Hoje_eh", objetoRemoto)`
- **Cliente**
  - `Naming.lookup("rmi://localhost/Hoje_eh")`
  - chama `d.pegaDataHora()` como se o objeto fosse local

## 5. Exemplo Python RPC (slides 18–19)

**servidor.py**
```python
from xmlrpc.server import SimpleXMLRPCServer
import datetime, xmlrpc.client

def hoje_eh():
    hoje = datetime.datetime.today()
    return xmlrpc.client.DateTime(hoje)

servidor = SimpleXMLRPCServer(("localhost", 8000))
print("Ouvindo a porta 8000...")
servidor.register_function(hoje_eh, "hoje")
servidor.serve_forever()
```

**cliente.py**
```python
import xmlrpc.client, datetime

servidor = xmlrpc.client.ServerProxy("http://localhost:8000/")
hoje = servidor.hoje()

# converte a string ISO8601 para datetime
data_hora_convertida = datetime.datetime.strptime(hoje.value, "%Y%m%dT%H:%M:%S")
print("Hoje é: %s" % data_hora_convertida.strftime("%d.%m.%Y, %H:%M:%S"))
```

**Comparação com o Java RMI**
- Python: ~11 linhas no servidor e ~9 no cliente; basta **registrar uma função** e chamá-la pelo proxy
- Java: interface + implementação + classe serializável + servidor + cliente
- No log do servidor Python aparecem requisições `POST / HTTP/1.1 200`, o que confirma o transporte por HTTP

## 6. Evolução histórico-tecnológica (anotações de aula)

- Antigamente era comum **SOAP/XML**
- Esse modelo foi substituído pelo padrão **REST com JSON**, que se popularizou (nas anotações: "enterrado pelo Google")

## 7. Considerações finais (slides 21–22)

- RPC em Python é **simples e rápido** para "produção" e "entrega"
- O **desempenho é pior que o de sockets** puros
  - trade-off: **simplificar o desenvolvimento × melhorar o desempenho**
  - para "serviços" simples, a diferença não importa
- RPC é uma **abstração para o programador**: esconde sistemas distribuídos, SO, redes, cliente-servidor, TCP/UDP, sockets, threads, POO e serialização

## 8. Resumo rápido (para revisão)

- **RPC** = chamar procedimento em outra máquina como se fosse local
- **RMI** = RPC orientado a objetos do **Java** (objeto chama método de objeto em outra JVM)
- **XML-RPC** = RPC com XML sobre HTTP (slides atribuem à Microsoft)
- **Python** = RPC simples via `xmlrpc`
- Vantagem: simplicidade · Desvantagem: desempenho pior que sockets
- Mercado atual: **REST + JSON** no lugar de SOAP/XML

---

*Contatos do material original: alexz@ufn.edu.br · guilhermechagaskurtz@ufn.edu.br · www.lapinf.com.br · github.com/alexandrezamberlan/sistemasDistribuidos*
