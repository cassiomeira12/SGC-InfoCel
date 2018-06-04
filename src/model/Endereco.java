package model;

public class Endereco {

    private Long id;
    private Bairro bairro;
    private String rua;
    private String numero;
    private boolean status = true;

    public Endereco(Long id, Bairro bairro, String rua, String numero) {
        this.id = id;
        this.bairro = bairro;
        this.rua = rua;
        this.numero = numero;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Bairro getBairro() {
        return bairro;
    }

    public void setBairro(Bairro bairro) {
        this.bairro = bairro;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    @Override
    public String toString() {
        return bairro.getCidade().getNome() + ", " + bairro.getNome() + ", " + rua + ", " + numero;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

}
