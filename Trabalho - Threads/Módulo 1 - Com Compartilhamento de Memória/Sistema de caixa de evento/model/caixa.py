import threading
import time


class SaldoCentral:
    """Saldo compartilhado do evento. Protegido por Lock contra race condition."""

    def __init__(self):
        self._saldo = 0
        self.lock = threading.Lock()

    def registrar_venda(self, valor):
        """Registra a venda de fichas, somando o valor ao saldo central."""
        with self.lock:
            self._saldo += valor

    @property
    def saldo(self):
        """Retorna o saldo atual do evento."""
        with self.lock:
            return self._saldo


class Caixa(threading.Thread):
    """Um caixa físico do evento. Roda em thread própria e vende fichas."""

    preco_ficha = 10.00

    def __init__(self, identificador, saldo_central, quantidade_fichas):
        super().__init__(name=f"Caixa-{identificador}")
        self.identificador = identificador
        self.saldo_central = saldo_central
        self.quantidade_fichas = quantidade_fichas
        self.fichas_vendidas = 0


    def run(self):
        """Simula a venda de fichas pelo caixa."""

        for _ in range(self.quantidade_fichas):
            self.saldo_central.registrar_venda(Caixa.preco_ficha)
            self.fichas_vendidas += 1
            time.sleep(0.01)
        print(f"{self.name} finalizou a venda de {self.quantidade_fichas} fichas.")