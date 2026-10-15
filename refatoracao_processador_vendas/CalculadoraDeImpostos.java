/**
 * Regra de impostos isolada do resto do processamento.
 * Refatoração: Extract Class + Replace Magic Number with Symbolic Constant.
 */
public class CalculadoraDeImpostos {

    private static final double LIMITE_PRODUTO_CARO = 100.0;
    private static final double ALIQUOTA_PRODUTO_CARO = 0.15;   // 15%
    private static final double ALIQUOTA_PRODUTO_BARATO = 0.05; // 5%

    public double calcularImpostos(Pedido pedido) {
        double totalImpostos = 0;
        for (ItemPedido item : pedido.getItens()) {
            totalImpostos += calcularImpostoDoItem(item);
        }
        return totalImpostos;
    }

    private double calcularImpostoDoItem(ItemPedido item) {
        return item.calcularSubtotal() * aliquotaPara(item);
    }

    private double aliquotaPara(ItemPedido item) {
        if (item.getPrecoUnitario() > LIMITE_PRODUTO_CARO) {
            return ALIQUOTA_PRODUTO_CARO;
        }
        return ALIQUOTA_PRODUTO_BARATO;
    }
}
