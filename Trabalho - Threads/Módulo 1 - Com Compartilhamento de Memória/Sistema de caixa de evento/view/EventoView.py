class EventoView:
    """Exibe os resultados do evento."""

    def exibir_por_caixa(self, caixas):
        """Exibe a quantidade de fichas vendidas por cada caixa."""

        print("\n----VENDAS POR CAIXA----")
        for caixa in caixas:
            print(f"Caixa-{caixa.identificador}: {caixa.fichas_vendidas} fichas vendidas")


    def exibir_saldo_final(self, saldo_final):
        """Exibe o saldo final do evento."""

        print("\nSaldo final do evento: R$ %.2f" % saldo_final)