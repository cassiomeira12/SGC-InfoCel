package model;

public class Bairro {

    private Long id;
    private String nome;
    private Cidade cidade;

    public Bairro(Long id, String descricao, Cidade cidade) {
        this.id = id;
        this.nome = descricao;
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
    
    @Override
    public String toString() {
        return this.nome;
    }

}
