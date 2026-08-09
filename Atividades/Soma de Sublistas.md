# Divisão e Conquista: Soma de Sublistas

## Enunciado

**Contexto:** O processamento de grandes volumes de dados numéricos.

**Problema:** Dado um vetor ou lista com 10.000 números inteiros aleatórios, divida essa lista em 4 partes iguais.

**Ação:** Crie 4 threads. Cada thread recebe apenas uma das partes como parâmetro de entrada, calcula a soma dos elementos dessa sublista e retorna o valor final.

**Encerramento:** A thread principal aguarda o fim das 4 threads, coleta as 4 somas parciais e calcula a soma total.

**Requisitos adicionais:**
- Sem memória compartilhada (uso de `Runnable`)
- Arquitetura MVC (Model-View-Controller)
- Orientado a objetos

---

## Solução (Java)

### `Model/Sublista.java`

```java
package Model;

public class Sublista implements Runnable{

    private final int[] dados;
    private long soma;

    public Sublista(int[] dados){
        this.dados = dados;
    }

    public void run(){
        long total = 0;
        for (int valor : dados){
            total += valor;
        }
        soma = total;
    }

    public long getSoma(){
        return soma;
    }
}
```

### `View/Exibir.java`

```java
package View;

public class Exibir {

    public void mostrarParcial(int indice, long soma){
        System.out.println("Thread " + indice + " -> soma parcial: " + soma);
    }

    public void mostrarTotal(long total){
        System.out.println("Soma total: " + total);
    }
}
```

### `Controller/Main.java`

```java
package Controller;

import Model.Sublista;
import View.Exibir;

import java.util.Arrays;
import java.util.Random;


public class Main {
    public static void main(String[] args) throws InterruptedException {

        final int TAMANHO = 10_000;
        final int NUM_THREADS = 4;

        Exibir view =  new Exibir();

        int[] dados = new int[TAMANHO];
        //Random rand = new Random();

        for(int i = 0; i < TAMANHO; i++){
            dados[i] = i + 1;
        }

        // Dividindo em 4 partes:
        int tamanhoParte = (TAMANHO + NUM_THREADS - 1) / NUM_THREADS;
        Sublista[] partes = new Sublista[NUM_THREADS];
        Thread[] threads = new Thread[NUM_THREADS];

        for(int t = 0; t < NUM_THREADS; t++){
            int inicio = t * tamanhoParte;
            int fim = Math.min(inicio + tamanhoParte, TAMANHO);

            int[] pedaco = Arrays.copyOfRange(dados, inicio, fim);

            partes[t] = new Sublista(pedaco);
            threads[t] = new Thread(partes[t]);
        }


        for(Thread thread : threads){
            thread.start();
        }

        for(Thread thread : threads){
            thread.join();
        }


        long somaTotal = 0;
        for(int t = 0; t < NUM_THREADS; t++){
            long parcial = partes[t].getSoma();
            view.mostrarParcial(t, parcial);
            somaTotal += parcial;
        }

        view.mostrarTotal(somaTotal);


    }
}
```

---

## Solução (Python)

### `model/sublista.py`

```python
class Sublistas:
    def __init__(self, dados):
        self.dados = dados
        self.soma = 0

    def run(self):
        total = 0
        for valor in self.dados:
            total += valor
        self.soma = total
```

### `view/exibir.py`

```python
class Visualizar:
    def mostrar_parcial(self, indice, soma):
        print(f"Thread {indice} -> soma parcial: {soma}")   

    def mostrar_total(self, total):
        print(f"Soma total final: {total}")
```

### `main.py`

```python
from view.exibir import Visualizar
from model.sublista import Sublistas
import random
import threading


def main():
    TAMANHO = 10_000
    NUM_THREADS = 4
    TAMANHO_PARTE = TAMANHO // NUM_THREADS

    v = Visualizar()
    dados = [random.randint(1, 100) for _ in range(TAMANHO)]

    partes = [
        Sublistas(dados[i * TAMANHO_PARTE:(i + 1) * TAMANHO_PARTE])
        for i in range(NUM_THREADS)
    ]
    threads = [threading.Thread(target=parte.run) for parte in partes]

    for t in threads:
        t.start()
    for t in threads:
        t.join()

    soma_total = 0
    for i, parte in enumerate(partes):
        v.mostrar_parcial(i, parte.soma)
        soma_total += parte.soma

    v.mostrar_total(soma_total)


if __name__ == "__main__":
    main()
```
