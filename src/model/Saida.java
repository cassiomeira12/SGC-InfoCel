package model;

import util.DateUtils;
import util.Formatter;

public class Saida implements Comparable<Saida> {

    private Long id;
    private Administrador administrador;
    private String descricao;
    private CategoriaSaida categoria;
    private float valor;
    private Long data;

    public Saida(Long id, Administrador administrador, String descricao, CategoriaSaida categoria, float valor, Long data) {
        this.id = id;
        this.administrador = administrador;
        this.descricao = descricao;
        this.categoria = categoria;
        this.valor = valor;
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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public CategoriaSaida getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaSaida categoria) {
        this.categoria = categoria;
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

    public Long getData() {
        return data;
    }
    
    public String getDataEditada() {
        return DateUtils.formatDate(data);
    }

    public void setData(Long data) {
        this.data = data;
    }
    
    @Override
    public int compareTo(Saida saida) {
        int comparador = saida.getData().intValue();
        /* Do maior para o Menor*/
        //return this.getData().intValue() - comparador;

        /* Do menor para o Maior */
        return  comparador - this.getData().intValue();
    }

}
