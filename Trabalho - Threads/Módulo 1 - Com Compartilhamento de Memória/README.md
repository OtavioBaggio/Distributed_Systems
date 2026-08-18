### Módulo 1: Com Compartilhamento de Memória (Threads/State)
Foco em sincronização, condições de corrida e exclusão mútua.

#### Exercício 1: Sistema de Caixa Centralizado de Evento (Dificuldade: Média)

* Contexto: Um grande festival de música possui 5 caixas físicos vendendo fichas de alimentação simultaneamente. Todos os caixas atualizam o mesmo saldo bancário centralizado do evento.

* Requisitos:
    1. Crie uma variável global/compartilhada chamada saldo_central.
    2. Instancie 5 threads (representando os caixas).
    3. Cada thread deve simular a venda de 1.000 fichas (cada ficha custa R$ 10,00), somando o valor ao saldo_central.
    4. O saldo final esperado deve ser exatamente R$ 50.000,00.

* O que avalia: utilização de mecanismos de sincronização (synchronized/ReentrantLock em Java ou threading.Lock em Python) para garantir a consistência do saldo.

