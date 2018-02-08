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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import model.Produto;

/**
 * FXML Controller class
 *
 * @author cassio
 */
public class TelaConsultarProdutosController extends AnchorPane {
    
    private BorderPane painelPrincipal;
    private List<Produto> listaProdutos;
    
    
    @FXML
    private TextField pesquisaText;
    @FXML
    private TableView<Produto> produtosTable;
    @FXML
    private TableColumn<Produto, String> categoriaColumn;
    @FXML
    private TableColumn<Produto, String> descricaoColumn;
    @FXML
    private TableColumn<Produto, String> marcaColumn;
    @FXML
    private TableColumn<Produto, String> quantidadeColumn;
    @FXML
    private TableColumn<Produto, String> precoColumn;

  
    public TelaConsultarProdutosController(BorderPane painelPrincipal) {
        this.painelPrincipal = painelPrincipal;
        
        try {
            FXMLLoader fxml = new FXMLLoader(getClass().getResource("/view/TelaConsultarProdutos.fxml"));
            fxml.setRoot(this);
            fxml.setController(this);
            fxml.load();
        } catch (IOException ex) {
            System.out.println("[ERRO] : Erro na tela Consultar Produtos");
            System.out.println(ex.toString());
        }
    }

    @FXML
    public void initialize() {
        // TODO
        this.sincronizarBancoDados();
        this.atualizarTabela();
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
    private void editarProduto() {
        
    }
    
    @FXML
    private void excluirProduto() {
        
    }
    
    private void atualizarTabela() {
        //Transforma a lista em uma Lista Observavel
        ObservableList data = FXCollections.observableArrayList(listaProdutos);
        
        //this.categoriaColumn.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        this.descricaoColumn.setCellValueFactory(new PropertyValueFactory<>("descricacao"));
        //this.marcaColumn.setCellValueFactory(new PropertyValueFactory<>("marca"));
        this.quantidadeColumn.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        this.precoColumn.setCellValueFactory(new PropertyValueFactory<>("preco"));
        
        this.produtosTable.setItems(data);//Adiciona a lista de clientes na Tabela
    }
    
    private void sincronizarBancoDados() {
        this.listaProdutos = ControleDAO.getBanco().getProdutoDAO().listar();
    }
}
