# Sistema de Cadastro Acadêmico — Fluxo

Dois programas se comunicam via Socket TCP na porta 50000:
o **servidor** guarda os dados e aplica as regras; o **cliente** coleta e exibe.

## Fluxo

1. **Servidor inicia** o `ServerSocket` e espera no `accept()` (em thread separada).
2. **Usuário preenche** nome e data no cliente e clica em **Enviar**.
3. **Cliente valida** os campos e cria um objeto `Pessoa(nome, data)`.
4. **Cliente envia** o objeto com `writeObject` (em thread separada).
5. **Servidor aceita** a conexão e cria uma thread `AtendeCliente`.
6. **Servidor cadastra** a pessoa:
   - já existe (mesmo nome e data) → devolve a existente;
   - não existe → gera o e-mail `primeiro.ultimo.ano@ufn.edu.br`, adiciona na lista e atualiza a tela.
7. **Servidor devolve** a `Pessoa` com o e-mail.
8. **Cliente exibe** nome, e-mail e data nos campos bloqueados e limpa os campos de entrada.

## Conceitos principais

| Pergunta | Solução |
|---|---|
| Como a `Pessoa` trafega na rede? | Implementando `Serializable` |
| Como a interface não trava? | Operações de rede em threads separadas; tela atualizada com `SwingUtilities.invokeLater` |
| Como evitar race condition? | Método `cadastrar` com `synchronized`, uma thread por vez acessa a lista |
