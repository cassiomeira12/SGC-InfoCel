/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import banco.ControleDAO;
import java.io.IOException;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import model.Cliente;
import util.alerta.Alerta;
import util.alerta.Dialogo;

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
    private TableView<Cliente> clientesTable;
    @FXML
    private TableColumn<Cliente, String> nomeColumn;
    @FXML
    private TableColumn<Cliente, String> enderecoColumn;
    @FXML
    private TableColumn<Cliente, String> telefoneColumn;
    @FXML
    private Button editarButton;
    @FXML
    private Button excluirButton;
    
  
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
        editarButton.disableProperty().bind(clientesTable.getSelectionModel().selectedItemProperty().isNull());
        excluirButton.disableProperty().bind(clientesTable.getSelectionModel().selectedItemProperty().isNull());
        atualizarTabela();
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
        try {
            Cliente cliente = clientesTable.getSelectionModel().getSelectedItem();
            
            TelaClienteController telaCliente = new TelaClienteController(painelPrincipal, cliente);
            //telaCliente.setCliente(cliente);
            this.adicionarPainelInterno(telaCliente);
            
        } catch (NullPointerException e) {
            Alerta.alerta("Selecione usuário na tabela para editar!");
        }
    }
    
    @FXML
    private void excluirCliente() {
        try {
            Cliente cliente = clientesTable.getSelectionModel().getSelectedItem();

            Dialogo.Resposta resposta = Alerta.confirmar("Excluir usuário " + cliente.getNome() + " ?");

            if (resposta == Dialogo.Resposta.YES) {
                ControleDAO.getBanco().getClienteDAO().excluir(cliente.getId().intValue());
                //sincronizarBase();
                atualizarTabela();
            }

            clientesTable.getSelectionModel().clearSelection();

        } catch (NullPointerException ex) {
            Alerta.alerta("Selecione usuário na tabela para exclusão!");
        }
    }
    
    private void atualizarTabela() {
        List<Cliente> clienteList = ControleDAO.getBanco().getClienteDAO().listar();
        ObservableList data = FXCollections.observableArrayList(clienteList);
        
        this.nomeColumn.setCellValueFactory(new PropertyValueFactory<>("nome"));
        this.enderecoColumn.setCellValueFactory(new PropertyValueFactory<>("endereco"));
        this.telefoneColumn.setCellValueFactory(new PropertyValueFactory<>("telefone"));
        
        this.clientesTable.setItems(data);
    }
    
}
