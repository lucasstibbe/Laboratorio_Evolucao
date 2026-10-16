public class FreteSudesteStrategy implements EstrategiaFrete {

    private static final double VALOR_FRETE_SUDESTE = 15.0;

    @Override
    public double calcularFrete(Cliente cliente) {
        return VALOR_FRETE_SUDESTE;
    }
}
