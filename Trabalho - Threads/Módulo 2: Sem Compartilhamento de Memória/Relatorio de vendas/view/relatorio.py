class View:
    """Classe responsável por exibir os resultados do relatório de vendas."""

    def exibir_por_filial(self, resultados):
        """Função que exibe o faturamento por filial."""

        print("\n----FATURAMENTO POR FILIAL----")
        for i, faturamento in enumerate(resultados, start=1):
            print(f"Filial {i}: R$ {faturamento:.2f}")

    def exibir_valor_total(self, valor_total):
        """Exibe o faturamento total."""
        print(f"\nFaturamento total: R$ {valor_total:.2f}")