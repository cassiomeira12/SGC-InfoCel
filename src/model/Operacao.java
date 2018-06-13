/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import util.DateUtils;
import util.Formatter;

/**
 *
 * @author cassio
 */
public class Operacao implements Comparable<Operacao> {
    
    private Venda venda = null;
    private Manutencao manutencao = null;
    private Receita receita = null;
    private Saida saida = null;
    
    private String categoria = "";
    private String cliente = "";
    private String descricao = "";
    private Administrador funcionario;
    private float valor = 0;
    private Long data;
    
    public Operacao(Venda venda) {
        this.venda = venda;
        this.categoria = "VENDA";
        this.cliente = venda.getCliente().getNome();
        //this.descricao = venda.toString();
        this.funcionario = venda.getAdministrador();
        this.valor = venda.getPrecoTotal();
        this.data = venda.getData();
    }
    
    public Operacao(Manutencao manutencao) {
        this.manutencao = manutencao;
        this.categoria = "MANUTENÇÃO";
        this.cliente = manutencao.getCliente().getNome();
        this.descricao = manutencao.getDescricao();
        this.funcionario = manutencao.getAdministrador();
        this.valor = manutencao.getPreco();
        this.data = manutencao.getDataCadastro();
    }
    
    public Operacao(Receita receita) {
        this.receita = receita;
        this.categoria = "RECEITA";
        this.cliente = receita.getCliente().getNome();
        this.descricao = receita.getDescricao();
        this.funcionario = receita.getAdministrador();
        this.valor = receita.getValor();
        this.data = receita.getData();
    }
    
    public Operacao(Saida saida) {
        this.saida = saida;
        this.categoria = "SAÍDA";
        //this.cliente = 
        this.descricao = saida.getDescricao();
        this.funcionario = saida.getAdministrador();
        this.valor = -saida.getValor();
        this.data = saida.getData();
    }

    public String getCategoria() {
        return categoria;
    }

    public String getCliente() {
        return cliente;
    }

    public String getDescricao() {
        return descricao;
    }

    public Administrador getFuncionario() {
        return funcionario;
    }

    public float getValor() {
        return valor;
    }
    
    public String getValorFormatado() {
        return Formatter.dinheiroFormatado(valor);
    }
    
    public Long getData() {
        return data;
    }
    
    public String getDataEditada() {
        return DateUtils.formatDate(data);
    }
    
    @Override
    public int compareTo(Operacao operacao) {
        int comparador = operacao.getData().intValue();
        
        /* Do maior para o Menor*/
        //return this.getData().intValue() - comparador;

        /* Do menor para o Maior */
        return  comparador - this.getData().intValue();
    }

    public Venda getVenda() {
        return venda;
    }

    public Manutencao getManutencao() {
        return manutencao;
    }

    public Receita getReceita() {
        return receita;
    }

    public Saida getSaida() {
        return saida;
    }
    
}
