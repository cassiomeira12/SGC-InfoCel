package model;

public class Bairro {

    private Long id;
    private String nome;
    private Cidade cidade;
    private boolean status = true;

    public Bairro(Long id, String descricao, Cidade cidade) {
        this.id = id;
        this.nome = descricao;
        this.cidade = cidade;
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

    public Cidade getCidade() {
        return cidade;
    }

    public void setCidade(Cidade cidade) {
        this.cidade = cidade;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return this.nome;
    }

}
