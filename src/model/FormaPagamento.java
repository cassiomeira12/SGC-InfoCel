/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author pedro
 */
public class FormaPagamento {

    private Long id;
    private String descricao;
    private int maximoParcelas;
    private boolean status = true;

    public FormaPagamento(Long id, String descricao, int maximoParcelas) {
        this.id = id;
        this.descricao = descricao;
        this.maximoParcelas = maximoParcelas;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getMaximoParcelas() {
        return maximoParcelas;
    }

    public void setMaximoParcelas(int maximoParcelas) {
        this.maximoParcelas = maximoParcelas;
    }

    @Override
    public String toString() {
        return getDescricao();
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

}
