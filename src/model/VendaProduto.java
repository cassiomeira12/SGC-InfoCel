package model;

public class VendaProduto {

    private float quantidade;
    private float precoTotal;
    private Venda venda;
    private Produto produto;

    public VendaProduto(float quantidade, Venda venda, Produto produto) {
        this.quantidade = quantidade;
        this.venda = venda;
        this.produto = produto;

        if (produto != null) {
            this.precoTotal = produto.getPrecoVenda() * quantidade;
        }
    }

    public float getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(float quantidade) {
        this.quantidade = quantidade;
        precoTotal = quantidade * getProduto().getPrecoVenda();
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
    
    public CategoriaProduto getCategoria() {
        return this.produto.getCategoria();
    }
    
    public String getDescricao() {
        return this.produto.getDescricao();
    }
    
    public Marca getMarca() {
        return this.produto.getMarca();
    }
    
    public float getPrecoProduto() {
        return this.produto.getPrecoVenda();
    }

}
