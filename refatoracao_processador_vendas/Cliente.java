/**
 * Dados do cliente que antes estavam soltos no ProcessadorDeVendas (n, doc, tc, end).
 * Refatoração: Extract Class + Rename Variable.
 */
public class Cliente {

    private static final int TAMANHO_DOCUMENTO = 11; // CPF tem 11 dígitos

    private final String nome;
    private final String documento;
    private final TipoCliente tipo;
    private final String endereco;

    public Cliente(String nome, String documento, TipoCliente tipo, String endereco) {
        this.nome = nome;
        this.documento = documento;
        this.tipo = tipo;
        this.endereco = endereco;
    }

    public boolean possuiDocumentoValido() {
        return documento != null && documento.length() == TAMANHO_DOCUMENTO;
    }

    public String getNome() {
        return nome;
    }

    public String getDocumento() {
        return documento;
    }

    public TipoCliente getTipo() {
        return tipo;
    }

    public String getEndereco() {
        return endereco;
    }
}
