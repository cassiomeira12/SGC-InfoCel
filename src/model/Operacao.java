/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author cassio
 */
public class Operacao {
    
    private String categoria = "";
    private String cliente = "";
    private String descricao = "";
    private Administrador funcionario;
    private float valor = 0;
    
    public Operacao(Venda venda) {
        this.categoria = "Venda";
        this.cliente = venda.getCliente().getNome();
        //this.descricao = 
        this.funcionario = venda.getAdministrador();
        this.valor = venda.getPrecoTotal();
    }
    
    public Operacao(Manutencao manutencao) {
        this.categoria = "Manutenção";
        this.cliente = manutencao.getCliente().getNome();
        this.descricao = manutencao.getDescricao();
        this.funcionario = manutencao.getAdministrador();
        this.valor = manutencao.getPreco();
    }
    
    public Operacao(Receita receita) {
        this.categoria = "Receita";
        this.cliente = receita.getCliente().getNome();
        this.descricao = receita.getDescricao();
        this.funcionario = receita.getAdministrador();
        this.valor = receita.getValor();
    }
    
    public Operacao(Saida saida) {
        this.categoria = "Saída";
        //this.cliente = 
        this.descricao = saida.getDescricao();
        this.funcionario = saida.getAdministrador();
        this.valor = saida.getValor();
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
    
    
    
}
