public class FreteNacionalStrategy implements EstrategiaFrete {

    private static final double VALOR_FRETE_NACIONAL = 50.0;

    @Override
    public double calcularFrete(Cliente cliente) {
        return VALOR_FRETE_NACIONAL;
    }
}
