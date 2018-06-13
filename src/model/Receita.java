package model;

import util.DateUtils;
import util.Formatter;

public class Receita implements Comparable<Receita> {

    private Long id;
    private Cliente cliente;
    private Administrador administrador;
    private String descricao;
    private Long data;
    private float valor;

    public Receita(Long id, Cliente cliente, Administrador administrador, String descricao, Long data, float valor) {
        this.id = id;
        this.cliente = cliente;
        this.administrador = administrador;
        this.descricao = descricao;
        this.data = data;
        this.valor = valor;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Administrador getAdministrador() {
        return administrador;
    }

    public void setAdministrador(Administrador administrador) {
        this.administrador = administrador;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
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

    public float getValor() {
        return valor;
    }

    public void setValor(float valor) {
        this.valor = valor;
    }
    
    public String getPrecoFormatado() {
        return Formatter.dinheiroFormatado(valor);
    }
    
    @Override
    public int compareTo(Receita receita) {
        int comparador = receita.getData().intValue();
        /* Do maior para o Menor*/
        //return this.getData().intValue() - comparador;

        /* Do menor para o Maior */
        return  comparador - this.getData().intValue();
    }

}
