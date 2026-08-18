"""
Módulo 1: Com Compartilhamento de Memória (Threads/State)
Exercício 1: Sistema de Caixa Centralizado de Evento
"""

from model.caixa import SaldoCentral, Caixa
from controller.saldoCentral import EventoController
from view.EventoView import EventoView

if __name__ == "__main__":
    controller = EventoController()
    view = EventoView()

    controller.preparar_caixas(quantidade_caixas=5, fichas_por_caixa=1000)
    controller.processar_vendas()

    view.exibir_por_caixa(controller.caixas)
    view.exibir_saldo_final(controller.saldo_final())