import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProcessadorDeVendas {

    private static final double LIMITE_PRODUTO_CARO = 100.0;
    private static final double ALIQUOTA_PRODUTO_CARO = 0.15;
    private static final double ALIQUOTA_PRODUTO_BARATO = 0.05;


    private final EstrategiaFrete estrategiaFrete;


    public ProcessadorDeVendas(EstrategiaFrete estrategiaFrete) {
        if (estrategiaFrete == null) {
            throw new IllegalArgumentException("A estrategia de frete e obrigatoria.");
        }
        this.estrategiaFrete = estrategiaFrete;
    }

    public void processar(Cliente cliente, List<Item> itens) {
        System.out.println("Iniciando processamento...");

        if (!cliente.isDocumentoValido()) {
            System.out.println("Erro: Documento invalido.");
            return;
        }

        double subtotal = calcularSubtotal(itens);
        double impostos = calcularImpostos(itens);

        double valorFrete = this.estrategiaFrete.calcularFrete(cliente);

        double totalFinal = subtotal + impostos + valorFrete;
        imprimirRecibo(cliente, subtotal, impostos, valorFrete, totalFinal);
    }

    private double calcularSubtotal(List<Item> itens) {
        double total = 0;
        for (Item item : itens) {
            total += item.getSubtotal();
        }
        return total;
    }

    private double calcularImpostos(List<Item> itens) {
        double impostoTotal = 0;
        for (Item item : itens) {
            if (item.getValor() > LIMITE_PRODUTO_CARO) {
                impostoTotal += item.getSubtotal() * ALIQUOTA_PRODUTO_CARO;
            } else {
                impostoTotal += item.getSubtotal() * ALIQUOTA_PRODUTO_BARATO;
            }
        }
        return impostoTotal;
    }

    private void imprimirRecibo(Cliente cliente, double subtotal, double impostos, double frete, double totalFinal) {
        System.out.println("=====================================");
        System.out.println("RECIBO DE VENDA");
        System.out.println("=====================================");
        System.out.println("Data: " + new Date());
        System.out.println("Cliente: " + cliente.getNome());
        System.out.println("Documento: " + cliente.getDocumento());
        System.out.println("-------------------------------------");
        System.out.println("Subtotal Itens: R$ " + subtotal);
        System.out.println("Total Impostos: R$ " + impostos);
        System.out.println("Frete: R$ " + frete);
        System.out.println("-------------------------------------");
        System.out.println("TOTAL A PAGAR: R$ " + totalFinal);
        System.out.println("=====================================");
    }


    public static void main(String[] args) {
        List<Item> itens = new ArrayList<>();
        itens.add(new Item(50.0, 2));
        itens.add(new Item(150.0, 1));
        itens.add(new Item(30.0, 3));

        Cliente clienteVip = new Cliente("Maria Souza", "98765432100", 1, "PR");
        Cliente clienteSudeste = new Cliente("João Silva", "12345678901", 2, "SP");
        Cliente clienteNacional = new Cliente("Carlos Lima", "11122233344", 2, "BA");

        new ProcessadorDeVendas(new FreteVipStrategy()).processar(clienteVip, itens);
        new ProcessadorDeVendas(new FreteSudesteStrategy()).processar(clienteSudeste, itens);
        new ProcessadorDeVendas(new FreteNacionalStrategy()).processar(clienteNacional, itens);
    }
}
