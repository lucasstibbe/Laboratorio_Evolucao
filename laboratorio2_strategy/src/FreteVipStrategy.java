public class FreteVipStrategy implements EstrategiaFrete {

    private static final double FRETE_GRATIS = 0.0;

    @Override
    public double calcularFrete(Cliente cliente) {
        return FRETE_GRATIS;
    }
}
