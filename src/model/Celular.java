package model;

public class Celular extends Produto {

    private String modelo;
    private String imei;
    private String cor;

    public Celular(Long id, Marca marca, String descricao, CategoriaProduto categoria, float precoCompra, float precoVenda, float estoque, UnidadeMedida unidadeMedida) {
        super(id, marca, descricao, categoria, precoCompra, precoVenda, estoque, unidadeMedida);
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

}
