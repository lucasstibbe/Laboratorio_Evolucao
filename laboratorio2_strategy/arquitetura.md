# Relatório de Arquitetura – Padrão Strategy nas Regras de Frete

## Estrutura final

```
src/
├── EstrategiaFrete.java        ← interface (o contrato)
├── FreteVipStrategy.java       ← retorna 0.0
├── FreteSudesteStrategy.java   ← retorna 15.0
├── FreteNacionalStrategy.java  ← retorna 50.0
├── ProcessadorDeVendas.java    ← recebe a estratégia pelo construtor
├── Cliente.java                ← sem alteração
└── Item.java                   ← sem alteração
```

A classe `CalculadoraFrete` foi **excluída**.

```
            ProcessadorDeVendas
                    │ depende de
                    ▼
         «interface» EstrategiaFrete
         + calcularFrete(Cliente): double
                    ▲ implementam
     ┌──────────────┼───────────────┐
FreteVipStrategy  FreteSudesteStrategy  FreteNacionalStrategy
```

---

## 1. Como o Strategy reduziu a Complexidade Ciclomática?

A Complexidade Ciclomática (CC) conta os caminhos independentes de um método: **1 + número de pontos de decisão** (`if`, `else if`, `&&`, `||`, `case`, laços).

**Antes – `CalculadoraFrete.calcular()`:**

| Ponto de decisão | Conta |
|---|---|
| `if (tipoCliente == 1)` | +1 |
| `else if (tipoCliente == 2)` | +1 |
| `if (estado.equals("SP") ...` | +1 |
| `\|\| estado.equals("RJ")` | +1 |
| `\|\| estado.equals("MG")` | +1 |
| **CC = 1 + 5** | **6** |

**Depois:**

| Método | CC |
|---|---|
| `FreteVipStrategy.calcularFrete()` | 1 |
| `FreteSudesteStrategy.calcularFrete()` | 1 |
| `FreteNacionalStrategy.calcularFrete()` | 1 |
| Linha do frete em `ProcessadorDeVendas.processar()` | +0 (é só uma chamada, sem `if`) |

Cada estratégia tem **um único caminho** de execução. O ponto mais importante é a tendência: no modelo antigo, cada transportadora nova (JadLog, PAC, Sedex), com regras de peso e região, somaria novos `if/else` **ao mesmo método**, e a CC cresceria sem limite. No modelo novo, a complexidade de cada transportadora fica **isolada na sua própria classe**, e o processador continua igual.

> Observação: a decisão de *qual* frete usar não desaparece; ela passa para quem monta o objeto (no nosso caso, o `main`, que injeta a estratégia). A diferença é que essa escolha fica num único ponto de configuração, e não espalhada dentro da regra de negócio.

---

## 2. Como a estrutura respeita o OCP e o DIP?

### OCP – Aberto para extensão, fechado para modificação

- **Fechado para modificação:** o `ProcessadorDeVendas` só conhece a interface `EstrategiaFrete`. Nenhuma linha dele cita "VIP", "Sudeste" ou valores de frete. Uma nova regra de frete não exige editar essa classe, que já está testada.
- **Aberto para extensão:** uma regra nova vira uma **classe nova** que implementa a interface.

Exemplo do pedido do Diretor (`FreteInternacionalStrategy`):

```java
public class FreteInternacionalStrategy implements EstrategiaFrete {
    private static final double VALOR_FRETE_INTERNACIONAL = 120.0;

    @Override
    public double calcularFrete(Cliente cliente) {
        return VALOR_FRETE_INTERNACIONAL;
    }
}

// uso:
new ProcessadorDeVendas(new FreteInternacionalStrategy()).processar(cliente, itens);
```

**Linhas alteradas em `ProcessadorDeVendas`: zero.** O mesmo vale para JadLog, Correios PAC e Sedex.

### DIP – Inversão de Dependência

> "Módulos de alto nível não devem depender de módulos de baixo nível. Ambos devem depender de abstrações."

- **Antes:** `private CalculadoraFrete calculadoraFrete = new CalculadoraFrete();` – o processador (alto nível, regra da venda) criava e dependia de uma classe concreta (baixo nível, detalhe do frete).
- **Depois:** `private final EstrategiaFrete estrategiaFrete;` – o processador depende só da **abstração**, e a implementação concreta é **injetada pelo construtor**. Ele não usa mais `new` em nenhuma classe de frete.

Com isso, as setas de dependência apontam para a interface: tanto o processador quanto as estratégias dependem de `EstrategiaFrete`, e não um do outro.

### Princípios que aparecem junto

- **ISP:** a interface tem um único método enxuto. Nenhuma estratégia é obrigada a implementar algo que não usa.
- **SRP:** cada estratégia tem um só motivo para mudar (o preço do VIP, do Sudeste ou do Nacional).
- **LSP:** o processador funciona com qualquer implementação da interface sem saber qual é. Por isso trocar `FreteVipStrategy` por `FreteNacionalStrategy` não quebra nada.

---

## 3. Validação

O recibo gerado pelo código novo foi comparado automaticamente com o do código base (com a antiga `CalculadoraFrete`), ignorando apenas a linha `Data:`, que muda a cada execução.

| Cliente | Estratégia injetada | Frete | Resultado |
|---|---|---|---|
| VIP | `FreteVipStrategy` | 0.0 | Idêntico |
| Comum – SP, RJ, MG | `FreteSudesteStrategy` | 15.0 | Idêntico |
| Comum – BA | `FreteNacionalStrategy` | 50.0 | Idêntico |
| Tipo desconhecido (3) | `FreteNacionalStrategy` | 50.0 | Idêntico |
| Documento inválido | qualquer | – | Idêntico (erro impresso) |

Saída do `main` (três clientes, três estratégias, o mesmo processador):

| Cliente | Frete | Total a pagar |
|---|---|---|
| Maria Souza (VIP) | R$ 0.0 | R$ 372.0 |
| João Silva (SP) | R$ 15.0 | R$ 387.0 |
| Carlos Lima (BA) | R$ 50.0 | R$ 422.0 |
