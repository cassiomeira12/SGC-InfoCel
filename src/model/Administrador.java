package model;

public class Administrador {

    private Long id;
    private String nome;
    private String login;
    private String senha;
    private String endereco;
    private String email;
    private String cpf;
    private Long dataCadastro;
    private int status;

    public Administrador(Long id, String nome, String login, String senha, String endereco, String email, String cpf, Long dataCadastro, int status) {
        this.id = id;
        this.nome = nome;
        this.login = login;
        this.senha = senha;
        this.endereco = endereco;
        this.email = email;
        this.cpf = cpf;
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

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
