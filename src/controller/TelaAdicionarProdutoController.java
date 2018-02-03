/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author cassio
 */
public class TelaAdicionarProdutoController extends AnchorPane {
    
    private BorderPane painelPrincipal;
    
    @FXML
    private TextField descricaoText;
    
    @FXML
    private VBox categoriaBox;
    @FXML
    private ComboBox categoriaComboBox;
    
    @FXML
    private VBox novaCategoriaBox;
    @FXML
    private TextField novaCategoriaText;

    @FXML
    private VBox novaMarcaBox;
    @FXML
    private TextField novaMarcaText;
    
    @FXML
    private VBox marcaBox;
    @FXML
    private ComboBox marcaComboBox;
    
    @FXML
    private TextField custoProdutoText;
    @FXML
    private TextField valorProdutoText;
    @FXML
    private Label percentualLabel;
    @FXML
    private TextField quantidadeText;

  
    public TelaAdicionarProdutoController(BorderPane painelPrincipal) {
        this.painelPrincipal = painelPrincipal;
        
        try {
            FXMLLoader fxml = new FXMLLoader(getClass().getResource("/view/TelaAdicionarProduto.fxml"));
            fxml.setRoot(this);
            fxml.setController(this);
            fxml.load();
        } catch (IOException ex) {
            System.out.println("[ERRO] : Erro na tela Adicionar Produto");
            System.out.println(ex.toString());
        }
    }

    @FXML
    public void initialize() {
        this.novaCategoriaBox.setVisible(false);//Ocultando Nova Categoria
        this.novaMarcaBox.setVisible(false);//Ocultando Nova Marca
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
    private void salvarProduto() {
        
    }
    
    @FXML
    private void adicionarNovaCategoria() {
        this.categoriaBox.setVisible(false);
        this.novaCategoriaBox.setVisible(true);
    }
    
    @FXML
    private void salvarNovaCategoria() {
        this.novaCategoriaBox.setVisible(false);
        this.categoriaBox.setVisible(true);
        Platform.runLater(() -> categoriaComboBox.requestFocus());//Colocando o Foco
    }
    
    @FXML
    private void adicionarNovaMarca() {
        this.marcaBox.setVisible(false);
        this.novaMarcaBox.setVisible(true);
    }
    
    @FXML
    private void salvarNovaMarca() {
        this.novaMarcaBox.setVisible(false);
        this.marcaBox.setVisible(true);
        Platform.runLater(() -> marcaComboBox.requestFocus());//Colocando o Foco
    }
    
}
