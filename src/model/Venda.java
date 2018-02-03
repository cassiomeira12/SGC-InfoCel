package model;

import java.util.List;

public class Venda {

    private Long id;
    private Administrador administrador;
    private Cliente cliente;
    private List<VendaProduto> vendaProdutos;
    private float precoTotal;
    private int formaPagamento;
    private Long data;

    public Venda(Long id, Administrador administrador, Cliente cliente, List<VendaProduto> vendaProdutos, float precoTotal, int formaPagamento, Long data) {
        this.id = id;
        this.administrador = administrador;
        this.cliente = cliente;
        this.vendaProdutos = vendaProdutos;
        this.precoTotal = precoTotal;
        this.formaPagamento = formaPagamento;
        this.data = data;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Administrador getAdministrador() {
        return administrador;
    }

    public void setAdministrador(Administrador administrador) {
        this.administrador = administrador;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<VendaProduto> getVendaProdutos() {
        return vendaProdutos;
    }

    public void setVendaProdutos(List<VendaProduto> vendaProdutos) {
        this.vendaProdutos = vendaProdutos;
    }

    public float getPrecoTotal() {
        return precoTotal;
    }

    public void setPrecoTotal(float precoTotal) {
        this.precoTotal = precoTotal;
    }

    public int getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(int formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public Long getData() {
        return data;
    }

    public void setData(Long data) {
        this.data = data;
    }

}
