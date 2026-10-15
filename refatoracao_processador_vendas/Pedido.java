import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Junta o cliente e os itens comprados.
 * Refatoração: Extract Class + Extract Method (cálculo do subtotal saiu do método gigante).
 */
public class Pedido {

    private final Cliente cliente;
    private final List<ItemPedido> itens = new ArrayList<>();

    public Pedido(Cliente cliente) {
        this.cliente = cliente;
    }

    public void adicionarItem(ItemPedido item) {
        itens.add(item);
    }

    public double calcularSubtotal() {
        double subtotal = 0;
        for (ItemPedido item : itens) {
            subtotal += item.calcularSubtotal();
        }
        return subtotal;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public List<ItemPedido> getItens() {
        return Collections.unmodifiableList(itens);
    }
}
