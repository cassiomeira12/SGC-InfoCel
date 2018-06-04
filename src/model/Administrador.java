package model;

import util.DateUtils;

public class Administrador {

    private Long id;
    private String nome;//
    private String login;
    private String senha;
    private String email;//
    private String cpf;//
    private String rg;//
    private Long dataCadastro;//
    private Endereco endereco;
    private boolean status;//

    public Administrador(Long id, String nome, String login, String senha, Endereco endereco, String email, String cpf, String rg, Long dataCadastro, boolean status) {
        this.id = id;
        this.nome = nome;
        this.login = login;
        this.senha = senha;
        this.endereco = endereco;
        this.email = email;
        this.cpf = cpf;
        this.rg = rg;
        this.dataCadastro = dataCadastro;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Long getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(Long dataCadastro) {
        this.dataCadastro = dataCadastro;
    }
    
    public String getDataEditada() {
        return DateUtils.formatDate(dataCadastro);
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    @Override
    public String toString() {
        return this.nome;
    }

   /* @Override
    public boolean equals(Object obj) {
        Administrador adm = (Administrador) obj;
        
        return id.equals(adm.getId()); //To change body of generated methods, choose Tools | Templates.
    }*/

}
