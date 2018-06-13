package model;

import java.util.ArrayList;
import java.util.List;
import util.DateUtils;
import util.Formatter;

public class Venda implements Comparable<Venda> {

    private Long id;
    private Administrador administrador;
    private Cliente cliente;
    private List<VendaProduto> vendaProdutos;
    private float precoTotal;
    private FormaPagamento formaPagamento;
    private int quantidadeParcelas;
    private Long data;
    
    public Venda(Long id, Administrador administrador, Cliente cliente, List<VendaProduto> vendaProdutos, FormaPagamento formaPagamento, int quantidadeParcelas, Long data) {
        this.id = id;
        this.administrador = administrador;
        this.cliente = cliente;
        this.vendaProdutos = vendaProdutos;
        this.precoTotal = calcularPrecoTotal();
        this.formaPagamento = formaPagamento;
        this.quantidadeParcelas = quantidadeParcelas;
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

    public int getQuantidadeParcelas() {
        return quantidadeParcelas;
    }

    public void setQuantidadeParcelas(int quantidadeParcelas) {
        this.quantidadeParcelas = quantidadeParcelas;
    }

    public void setVendaProdutos(List<VendaProduto> vendaProdutos) {
        this.vendaProdutos = vendaProdutos;
        precoTotal = calcularPrecoTotal();
    }

    public float getPrecoTotal() {
        return precoTotal;
    }
    
    public String getPrecoFormatado() {
        return Formatter.dinheiroFormatado(precoTotal);
    }

    public void setPrecoTotal(float precoTotal) {
        this.precoTotal = precoTotal;
    }

    public FormaPagamento getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(FormaPagamento formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public Long getData() {
        return data;
    }
    
    public String getDataEditada() {
        return DateUtils.formatDate(data);
    }

    public void setData(Long data) {
        this.data = data;
    }

    private float calcularPrecoTotal() {
        float preco = 0.0f;
        if (vendaProdutos == null) {
            return preco;
        }

        for (VendaProduto vp : vendaProdutos) {
            preco = preco + vp.getPrecoTotal();
        }

        return preco;
    }

    public void adicionarVendaProduto(VendaProduto vp) {
        if (vendaProdutos == null) {
            vendaProdutos = new ArrayList<>();
        }

        vendaProdutos.add(vp);
        precoTotal = calcularPrecoTotal();
    }

    public void removerVendaProduto(VendaProduto vp) {
        if (vendaProdutos != null) {
            vendaProdutos.remove(vp);
            precoTotal = calcularPrecoTotal();
        }
    }

    public boolean isEmpty() {
        if (vendaProdutos == null) {
            return true;
        } else {
            return vendaProdutos.isEmpty();
        }
    }

    public VendaProduto containsProduto(Produto produto) {
        if (this.isEmpty()) {
            return null;
        } else {
            for (VendaProduto vp : vendaProdutos) {
                if (vp.getProduto().getId().equals(produto.getId())) {
                    return vp;
                }
            }
            return null;
        }
    }

    public void atualizarVenda() {
        precoTotal = calcularPrecoTotal();
    }
    
    @Override
    public int compareTo(Venda venda) {
        int comparador = venda.getData().intValue();
        /* Do maior para o Menor*/
        //return this.getData().intValue() - comparador;

        /* Do menor para o Maior */
        return  comparador - this.getData().intValue();
    }
    
    public String getEnderecoCliente() {
        return cliente.getEndereco().toString();
    }
}
