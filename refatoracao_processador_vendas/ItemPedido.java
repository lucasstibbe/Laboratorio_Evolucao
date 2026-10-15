/**
 * Um item do pedido. Substitui os dois arrays paralelos val[] e qtd[],
 * que só faziam sentido juntos pelo mesmo índice.
 * Refatoração: Extract Class + Rename Variable.
 */
public class ItemPedido {

    private final double precoUnitario;
    private final int quantidade;

    public ItemPedido(double precoUnitario, int quantidade) {
        this.precoUnitario = precoUnitario;
        this.quantidade = quantidade;
    }

    public double calcularSubtotal() {
        return precoUnitario * quantidade;
    }

    public double getPrecoUnitario() {
        return precoUnitario;
    }

    public int getQuantidade() {
        return quantidade;
    }
}
