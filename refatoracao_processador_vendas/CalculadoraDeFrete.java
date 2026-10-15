import java.util.List;

/**
 * Regra de frete isolada. É aqui que o ticket da nova regra de frete
 * vai mexer, sem precisar tocar em impostos, recibo ou validação.
 * Refatoração: Extract Class + Replace Magic Number with Symbolic Constant.
 */
public class CalculadoraDeFrete {

    private static final double FRETE_GRATIS = 0.0;
    private static final double FRETE_REGIAO_SUDESTE = 15.0;
    private static final double FRETE_PADRAO = 50.0;
    private static final List<String> ESTADOS_COM_FRETE_REDUZIDO = List.of("SP", "RJ", "MG");

    public double calcularFrete(Cliente cliente) {
        switch (cliente.getTipo()) {
            case VIP:
                return FRETE_GRATIS;
            case COMUM:
                return calcularFretePorRegiao(cliente.getEndereco());
            default:
                return FRETE_PADRAO;
        }
    }

    private double calcularFretePorRegiao(String endereco) {
        if (enderecoEmEstadoComFreteReduzido(endereco)) {
            return FRETE_REGIAO_SUDESTE;
        }
        return FRETE_PADRAO;
    }

    private boolean enderecoEmEstadoComFreteReduzido(String endereco) {
        for (String estado : ESTADOS_COM_FRETE_REDUZIDO) {
            if (endereco.contains(estado)) {
                return true;
            }
        }
        return false;
    }
}
