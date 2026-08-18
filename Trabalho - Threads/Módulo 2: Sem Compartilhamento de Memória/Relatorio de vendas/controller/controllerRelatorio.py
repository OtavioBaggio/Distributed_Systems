from concurrent.futures import ThreadPoolExecutor


class Controller:
    """
    CONTROLLER: gera as 4 listas locais e usa um ThreadPoolExecutor
    para calcular a soma de cada uma em paralelo.
    """

    def __init__(self, model, view):
        """Construtor do Controller."""
        self.model = model
        self.view = view

    def executar(self):
        """Executa o processamento do relatório de vendas por filial."""
        filial_1 = self.model.popular_vendas(1000)
        filial_2 = self.model.popular_vendas(1000)
        filial_3 = self.model.popular_vendas(1000)
        filial_4 = self.model.popular_vendas(1000)


        with ThreadPoolExecutor() as executor:
            resultados = list(executor.map(
                self.model.somar_vendas,
                [filial_1, filial_2, filial_3, filial_4],
            ))

        self.view.exibir_por_filial(resultados)

        soma_final = sum(resultados)
        self.view.exibir_valor_total(soma_final)