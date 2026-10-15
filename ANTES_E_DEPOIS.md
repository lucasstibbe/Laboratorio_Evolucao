# Laboratório de Refatoração – ProcessadorDeVendas

## 1. Diagnóstico: Code Smells identificados

| # | Code Smell | Onde aparecia no código original | Por que é um problema |
|---|---|---|---|
| 1 | **Long Method** | `processar()` fazia validação, cálculo de subtotal, impostos, frete e impressão num único método | Difícil de ler, testar e alterar só uma parte (ex.: só o frete) |
| 2 | **Large Class / Divergent Change** | `ProcessadorDeVendas` guardava dados do cliente, dos itens e ainda cuidava de regras e apresentação | Qualquer mudança (frete, imposto, recibo) obriga a mexer na mesma classe |
| 3 | **Poor Naming (nomes ruins)** | `n`, `doc`, `tc`, `end`, `val`, `qtd`, `t`, `imp`, `f`, `sub` | O leitor precisa adivinhar ou depender de comentários para entender |
| 4 | **Magic Numbers** | `11`, `100`, `0.15`, `0.05`, `1`, `2`, `15.0`, `50.0`, `"SP"/"RJ"/"MG"` | Valores de negócio sem nome; mudar uma regra exige caçar números pelo código |
| 5 | **Data Clumps** | `n, doc, tc, end` sempre andam juntos (são o cliente); `val[]` e `qtd[]` são arrays paralelos (são os itens) | Dados que formam um conceito ficam espalhados e podem sair de sincronia |
| 6 | **Primitive Obsession / Type Code** | Tipo de cliente representado por `int` (`1` = VIP, `2` = comum) | Nada impede passar `7`; o significado só existe no comentário |
| 7 | **Duplicated Code** | `f = 50.0` aparece duas vezes; padrão `imp += sub * X` repetido nos dois ramos do `if` | Ao mudar o frete padrão, é fácil alterar um lugar e esquecer o outro |
| 8 | **Mistura de responsabilidades (negócio + apresentação)** | `System.out.println` do recibo no meio da lógica de cálculo | Não dá para reaproveitar o cálculo (ex.: numa API) sem levar a impressão junto |

## 2. Execução: tabela "Antes e Depois"

| Code Smell | Técnica de Fowler aplicada | Antes | Depois |
|---|---|---|---|
| Poor Naming | **Rename Variable** | `n`, `doc`, `tc`, `end` | `nome`, `documento`, `tipo`, `endereco` (em `Cliente`) |
| Poor Naming | **Rename Variable** | `val[]`, `qtd[]`, `sub` | `precoUnitario`, `quantidade`, `calcularSubtotal()` (em `ItemPedido`) |
| Poor Naming | **Rename Variable** | `t`, `imp`, `f`, `totalFinal` | `subtotal`, `impostos`, `frete`, `totalAPagar` |
| Magic Numbers | **Replace Magic Number with Symbolic Constant** | `doc.length() != 11` | `TAMANHO_DOCUMENTO` em `Cliente` |
| Magic Numbers | **Replace Magic Number with Symbolic Constant** | `val[i] > 100`, `0.15`, `0.05` | `LIMITE_PRODUTO_CARO`, `ALIQUOTA_PRODUTO_CARO`, `ALIQUOTA_PRODUTO_BARATO` |
| Magic Numbers | **Replace Magic Number with Symbolic Constant** | `0`, `15.0`, `50.0`, `"SP"`, `"RJ"`, `"MG"` | `FRETE_GRATIS`, `FRETE_REGIAO_SUDESTE`, `FRETE_PADRAO`, `ESTADOS_COM_FRETE_REDUZIDO` |
| Magic Numbers / Type Code | **Replace Magic Number with Symbolic Constant** (via enum) | `tc == 1`, `tc == 2` | `enum TipoCliente { VIP, COMUM, OUTRO }` |
| Long Method | **Extract Method** | Validação dentro de `processar()` | `Cliente.possuiDocumentoValido()` |
| Long Method | **Extract Method** | Laço de subtotal dentro de `processar()` | `Pedido.calcularSubtotal()` e `ItemPedido.calcularSubtotal()` |
| Long Method | **Extract Method** | `if/else` de imposto dentro do laço | `calcularImpostoDoItem()` e `aliquotaPara()` |
| Long Method / Duplicated Code | **Extract Method** | `if/else if/else` do frete com `50.0` repetido | `calcularFrete()`, `calcularFretePorRegiao()`, `enderecoEmEstadoComFreteReduzido()` |
| Long Method | **Extract Method** | 13 `println` seguidos | `imprimirCabecalho()`, `imprimirDadosDoCliente()`, `imprimirValores()`, `imprimirTotal()` |
| Large Class / Data Clumps | **Extract Class** | Campos do cliente no processador | Classe `Cliente` |
| Data Clumps (arrays paralelos) | **Extract Class** | `double[] val` + `int[] qtd` | Classe `ItemPedido` |
| Large Class | **Extract Class** | Itens e cliente soltos no processador | Classe `Pedido` |
| Large Class | **Extract Class** | Regra de frete no processador | Classe `CalculadoraDeFrete` |
| Large Class | **Extract Class** | Regra de impostos no processador | Classe `CalculadoraDeImpostos` |
| Negócio + apresentação | **Extract Class** | `println` do recibo no processador | Classe `ImpressoraDeRecibo` |

## 3. Estrutura final

| Classe | Responsabilidade |
|---|---|
| `ProcessadorDeVendas` | Só coordena o fluxo: valida, pede os cálculos, manda imprimir |
| `Cliente` | Dados do cliente e validação do documento |
| `TipoCliente` | Enum com os tipos de cliente |
| `ItemPedido` | Preço unitário, quantidade e subtotal do item |
| `Pedido` | Cliente + lista de itens; subtotal do pedido |
| `CalculadoraDeImpostos` | Regra de impostos por item |
| `CalculadoraDeFrete` | Regra de frete (onde entra o novo ticket) |
| `ImpressoraDeRecibo` | Formatação e impressão do recibo |

## 4. Validação

A saída do código original e a do refatorado foram comparadas automaticamente, ignorando apenas a linha `Data:`, que muda a cada execução por usar `new Date()`.

Cenários testados (todos com saída **idêntica**):

| Cenário | Resultado |
|---|---|
| Exemplo do `main` (cliente comum, SP) | Idêntico |
| Cliente VIP | Idêntico |
| Cliente comum fora do Sudeste (PR, BA) | Idêntico |
| Cliente comum em MG, com item de preço exatamente 100 | Idêntico |
| Tipo de cliente desconhecido (3) | Idêntico |
| Documento com tamanho errado | Idêntico |
| Documento nulo | Idêntico |

Saída do `main` após a refatoração:

```
Iniciando processamento...
=====================================
RECIBO DE VENDA
=====================================
Data: <data e hora da execução>
Cliente: João Silva
Documento: 12345678901
-------------------------------------
Subtotal Itens: R$ 340.0
Total Impostos: R$ 32.0
Frete: R$ 15.0
-------------------------------------
TOTAL A PAGAR: R$ 387.0
=====================================
```

Cuidado tomado para manter a saída igual: a ordem das somas foi preservada (subtotal + impostos + frete, impostos acumulados item a item na mesma ordem), para que os valores `double` não mudem nem na última casa decimal.
