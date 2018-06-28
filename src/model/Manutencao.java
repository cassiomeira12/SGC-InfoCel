package model;

import util.DateUtils;
import util.Formatter;

public class Manutencao implements Comparable<Manutencao> {

    private Long id;
    private String descricao;
    private Cliente cliente;
    private Administrador administrador;
    private FormaPagamento formaPagamento;
    private String marca;
    private String modelo;
    private String imei;
    private String cor;
    private Long dataCadastro;
    private Long dataPrevisaoEntrega;
    private Long dataEntrega;
    private float preco;
    private boolean finalizado;
    private int quantidadeParcelas;

    public Manutencao(Long id, String descricao, Cliente cliente, Administrador administrador, String marca, String modelo, String imei, String cor, Long dataCadastro, Long dataPrevisaoEntrega, Long dataEntrega, float preco, boolean finalizado, FormaPagamento formaPagamento, int quantidadeParcelas) {
        this.id = id;
        this.descricao = descricao;
        this.cliente = cliente;
        this.administrador = administrador;
        this.marca = marca;
        this.modelo = modelo;
        this.imei = imei;
        this.cor = cor;
        this.dataCadastro = dataCadastro;
        this.dataPrevisaoEntrega = dataPrevisaoEntrega;
        this.dataEntrega = dataEntrega;
        this.preco = preco;
        this.finalizado = finalizado;
        this.formaPagamento = formaPagamento;
        this.quantidadeParcelas = quantidadeParcelas;
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

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Administrador getAdministrador() {
        return administrador;
    }

    public void setAdministrador(Administrador administrador) {
        this.administrador = administrador;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public Long getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(Long dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public Long getDataPrevisaoEntrega() {
        return dataPrevisaoEntrega;
    }

    public String getDataPrevisaoEntregaFormatado() {

        if (dataPrevisaoEntrega != null) {
            return DateUtils.formatDate(dataPrevisaoEntrega);
        }
        return "00/00/00";
    }

    public void setDataPrevisaoEntrega(Long dataPrevisaoEntrega) {
        this.dataPrevisaoEntrega = dataPrevisaoEntrega;
    }

    public Long getDataEntrega() {
        return dataEntrega;
    }

    public void setDataEntrega(Long dataEntrega) {
        this.dataEntrega = dataEntrega;
    }

    public String getDataEditada() {
        return DateUtils.formatDate(dataCadastro);
    }

    public float getPreco() {
        return preco;
    }

    public void setPreco(float preco) {
        this.preco = preco;
    }

    public String getPrecoFormatado() {
        return Formatter.dinheiroFormatado(preco);
    }

    public boolean isFinalizado() {
        return finalizado;
    }

    public String getFinalizadoEditado() {
        return finalizado ? "Sim" : "Não";
    }

    public void setFinalizado(boolean finalizado) {
        this.finalizado = finalizado;
    }

    public FormaPagamento getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(FormaPagamento formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public int getQuantidadeParcelas() {
        return quantidadeParcelas;
    }

    public void setQuantidadeParcelas(int quantidadeParcelas) {
        this.quantidadeParcelas = quantidadeParcelas;
    }

    @Override
    public int compareTo(Manutencao manutencao) {
        int comparador = manutencao.getDataCadastro().intValue();
        /* Do maior para o Menor*/
        //return this.getData().intValue() - comparador;

        /* Do menor para o Maior */
        return comparador - this.getDataCadastro().intValue();
    }

    public String getEnderecoCliente() {
        return cliente.getEndereco().toString();
    }

}
