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
public class Celular {

    private Long _id;
    private String descricao;

    public Celular(Long _id, String descricao) {
        this._id = _id;
        this.descricao = descricao;
    }

    public Long getId() {
        return _id;
    }

    public String getDescricao() {
        return descricao;
    }

}
