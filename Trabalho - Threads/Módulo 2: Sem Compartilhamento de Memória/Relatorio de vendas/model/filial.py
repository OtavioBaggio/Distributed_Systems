import random

class Model:
    """
    Model: representa a lógica de negócio do programa."""

    def popular_vendas(self, quantidade_registros):
        """Gera uma lista de vendas aleatórias, simulando o faturamento de uma filial.
        Cada venda é um valor inteiro entre 10 e 500."""
        return [random.randint(10, 500) for _ in range(quantidade_registros)]

    def somar_vendas(self, vendas):
        """Soma os valores de uma lista de vendas."""
        return sum(vendas)