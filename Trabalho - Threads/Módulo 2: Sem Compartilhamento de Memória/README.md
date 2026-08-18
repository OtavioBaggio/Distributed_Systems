### Módulo 2: Sem Compartilhamento de Memória (Message Passing/Isolation)
Foco em divisão de tarefas, junção de resultados e isolamento de escopo.

#### Exercício 2: Processamento de Relatório de Vendas por Filial (Dificuldade: Fácil)

* Contexto: Uma franquia precisa calcular o faturamento total anual somando os dados independentes de suas 4 filiais.

* Requisitos:
    1. Crie 4 listas independentes de números locais, cada um simulando as vendas de uma filial (ex: 10.000 registros por lista).
    2. Dispare 4 threads. Cada thread recebe apenas a lista da sua respectiva filial e calcula a soma localmente.
    3. As threads não podem acessar variáveis globais durante a execução.
    4. A thread principal deve aguardar o fim de todas e somar os 4 resultados finais.

* O que avalia: Conceito de Fork-Join e isolamento. Em Java, avalia o uso de join() com classes que estendem Thread/implementam Runnable (guardando o resultado em um atributo do objeto) ou Future/Callable. Em Python, avalia o uso de threading.Thread com retorno planejado ou concurrent.futures.
