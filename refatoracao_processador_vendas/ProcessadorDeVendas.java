/**
 * Depois da refatoração, o processador só coordena o fluxo:
 * valida, pede os cálculos para quem sabe fazer e manda imprimir.
 * Refatoração: Extract Method + Extract Class + Rename Variable.
 */
public class ProcessadorDeVendas {

    private final Pedido pedido;
    private final CalculadoraDeImpostos calculadoraDeImpostos = new CalculadoraDeImpostos();
    private final CalculadoraDeFrete calculadoraDeFrete = new CalculadoraDeFrete();
    private final ImpressoraDeRecibo impressora = new ImpressoraDeRecibo();

    public ProcessadorDeVendas(Pedido pedido) {
        this.pedido = pedido;
    }

    public void processar() {
        System.out.println("Iniciando processamento...");

        Cliente cliente = pedido.getCliente();
        if (!cliente.possuiDocumentoValido()) {
            impressora.imprimirErroDocumentoInvalido();
            return;
        }

        double subtotal = pedido.calcularSubtotal();
        double impostos = calculadoraDeImpostos.calcularImpostos(pedido);
        double frete = calculadoraDeFrete.calcularFrete(cliente);
        double totalAPagar = subtotal + impostos + frete;

        impressora.imprimir(cliente, subtotal, impostos, frete, totalAPagar);
    }

    // Método main para testar se o comportamento continua o mesmo
    public static void main(String[] args) {
        Cliente cliente = new Cliente(
            "João Silva",
            "12345678901",
            TipoCliente.COMUM,
            "Rua das Flores, 123 - SP"
        );

        Pedido pedido = new Pedido(cliente);
        pedido.adicionarItem(new ItemPedido(50.0, 2));
        pedido.adicionarItem(new ItemPedido(150.0, 1));
        pedido.adicionarItem(new ItemPedido(30.0, 3));

        new ProcessadorDeVendas(pedido).processar();
    }
}
