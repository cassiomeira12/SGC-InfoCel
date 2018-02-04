/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import model.Cliente;

/**
 * FXML Controller class
 *
 * @author cassio
 */
public class TelaClienteController extends AnchorPane {
    
    private BorderPane painelPrincipal;
    private Cliente cliente;

    @FXML
    private TextField pesquisaText;
    @FXML
    private TextField nomeText;
    @FXML
    private TextField telefoneText;
    @FXML
    private TextField cpfText;
    @FXML
    private TextField rgText;
    @FXML
    private TextField cidadeText;
    @FXML
    private TextField enderecoText;
    
  
    public TelaClienteController(BorderPane painelPrincipal, Cliente cliente) {
        this.painelPrincipal = painelPrincipal;
        this.cliente = cliente;
        
        try {
            FXMLLoader fxml = new FXMLLoader(getClass().getResource("/view/TelaCliente.fxml"));
            fxml.setRoot(this);
            fxml.setController(this);
            fxml.load();
        } catch (IOException ex) {
            System.out.println("[ERRO] : Erro na tela Cliente");
            System.out.println(ex.toString());
        }
    }

    @FXML
    public void initialize() {
        this.nomeText.setText(cliente.getNome());
        this.telefoneText.setText(cliente.getTelefone());
        this.cpfText.setText(cliente.getCpf());
        this.rgText.setText(cliente.getRg());
        this.cidadeText.setText(cliente.getCidade());
        this.enderecoText.setText(cliente.getEndereco());
    }
    
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
    
    private void adicionarPainelInterno(AnchorPane novaTela) {
        this.painelPrincipal.setCenter(novaTela);
    }
    
    @FXML
    private void cancelarOperacao() {
        TelaConsultarClientesController telaConsultarClientes = new TelaConsultarClientesController(painelPrincipal);
        this.adicionarPainelInterno(telaConsultarClientes);
    }
    
}
