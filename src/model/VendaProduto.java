package model;

public class VendaProduto {

    private float quantidade;
    private float precoTotal;
    private Venda venda;
    private Produto produto;

    public VendaProduto(float quantidade, float precoTotal, Venda venda, Produto produto) {
        this.quantidade = quantidade;
        this.precoTotal = precoTotal;
        this.venda = venda;
        this.produto = produto;
    }

    public float getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(float quantidade) {
        this.quantidade = quantidade;
    }

    public float getPrecoTotal() {
        return precoTotal;
    }

    public void setPrecoTotal(float precoTotal) {
        this.precoTotal = precoTotal;
    }

    public Venda getVenda() {
        return venda;
    }

    public void setVenda(Venda venda) {
        this.venda = venda;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

}
