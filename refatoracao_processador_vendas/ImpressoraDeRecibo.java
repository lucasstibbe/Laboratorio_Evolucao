import java.util.Date;

/**
 * Responsável só pela apresentação (o recibo no console).
 * Refatoração: Extract Class + Extract Method.
 * O texto impresso é exatamente o mesmo do código original.
 */
public class ImpressoraDeRecibo {

    private static final String LINHA_DUPLA = "=====================================";
    private static final String LINHA_SIMPLES = "-------------------------------------";

    public void imprimir(Cliente cliente, double subtotal, double impostos, double frete, double totalAPagar) {
        imprimirCabecalho();
        imprimirDadosDoCliente(cliente);
        imprimirValores(subtotal, impostos, frete);
        imprimirTotal(totalAPagar);
    }

    public void imprimirErroDocumentoInvalido() {
        System.out.println("Erro: Documento invalido.");
    }

    private void imprimirCabecalho() {
        System.out.println(LINHA_DUPLA);
        System.out.println("RECIBO DE VENDA");
        System.out.println(LINHA_DUPLA);
        System.out.println("Data: " + new Date());
    }

    private void imprimirDadosDoCliente(Cliente cliente) {
        System.out.println("Cliente: " + cliente.getNome());
        System.out.println("Documento: " + cliente.getDocumento());
        System.out.println(LINHA_SIMPLES);
    }

    private void imprimirValores(double subtotal, double impostos, double frete) {
        System.out.println("Subtotal Itens: R$ " + subtotal);
        System.out.println("Total Impostos: R$ " + impostos);
        System.out.println("Frete: R$ " + frete);
        System.out.println(LINHA_SIMPLES);
    }

    private void imprimirTotal(double totalAPagar) {
        System.out.println("TOTAL A PAGAR: R$ " + totalAPagar);
        System.out.println(LINHA_DUPLA);
    }
}
