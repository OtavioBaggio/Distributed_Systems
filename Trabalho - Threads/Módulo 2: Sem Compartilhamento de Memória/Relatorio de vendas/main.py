"""
Módulo 2: Sem Compartilhamento de Memória (Message Passing/Isolation)
 
Exercício 2: Processamento de Relatório de Vendas por Filial
Versão com ThreadPoolExecutor.
 
Ponto de entrada do programa: monta o Model, a View e o Controller,
e dispara a execução.
"""

from model.filial import Model 
from view.relatorio import View
from controller.controllerRelatorio import Controller

if __name__ == "__main__":
    model = Model()
    view = View()
    controller = Controller(model, view)

    controller.executar()