from model.caixa import SaldoCentral, Caixa


class EventoController:
    """Cria os caixas, dispara as threads (fork) e espera terminarem (join)."""

    def __init__(self):
        self.saldo_central = SaldoCentral()
        self.caixas = []

    def preparar_caixas(self, quantidade_caixas, fichas_por_caixa):
        """Prepara os caixas para o evento, instanciando as threads."""

        for i in range(1, quantidade_caixas + 1):
            self.caixas.append(Caixa(i, self.saldo_central, fichas_por_caixa))

    def processar_vendas(self):
        """Inicia as threads e aguarda a finalização de todas."""

        for caixa in self.caixas:
            caixa.start()
        for caixa in self.caixas:
            caixa.join()


    def saldo_final(self):
        """Retorna o saldo final do evento."""
        return self.saldo_central.saldo