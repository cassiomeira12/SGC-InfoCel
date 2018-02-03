/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

/**
 * FXML Controller class
 *
 * @author cassio
 */
public class TelaConsultarClientesController extends AnchorPane {
    
    private BorderPane painelPrincipal;

    
    @FXML
    private TextField pesquisaText;
    @FXML
    private TableView clientesTable;
    @FXML
    private TableColumn nomeColumn;
    @FXML
    private TableColumn enderecoColumn;
    @FXML
    private TableColumn telefoneColumn;
    
  
    public TelaConsultarClientesController(BorderPane painelPrincipal) {
        this.painelPrincipal = painelPrincipal;
        
        try {
            FXMLLoader fxml = new FXMLLoader(getClass().getResource("/view/TelaConsultarClientes.fxml"));
            fxml.setRoot(this);
            fxml.setController(this);
            fxml.load();
        } catch (IOException ex) {
            System.out.println("[ERRO] : Erro na tela Consultar Clientes");
            System.out.println(ex.toString());
        }
    }

    @FXML
    public void initialize() {
        
    }
    
    private void adicionarPainelInterno(AnchorPane novaTela) {
        this.painelPrincipal.setCenter(novaTela);
    }
    
    @FXML
    private void cancelarOperacao() {
        TelaInicialController telaInicial = new TelaInicialController(painelPrincipal);
        this.adicionarPainelInterno(telaInicial);
    }
    
    @FXML
    private void editarCliente() {
        TelaClienteController telaCliente = new TelaClienteController(painelPrincipal);
        this.adicionarPainelInterno(telaCliente);
    }
    
    @FXML
    private void excluirCliente() {
        
    }
    
}
