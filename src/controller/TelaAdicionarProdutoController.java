/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import banco.ControleDAO;
import java.io.IOException;
import java.util.List;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import model.CategoriaProduto;
import model.Marca;
import model.Produto;
import util.Formatter;
import util.alerta.Alerta;

/**
 * FXML Controller class
 *
 * @author cassio
 */
public class TelaAdicionarProdutoController extends AnchorPane {
    
    private BorderPane painelPrincipal;
    
    private List<CategoriaProduto> listaCategoriaProdutos;
    private List<Marca> listaMarcas;
    
    @FXML
    private TextField descricaoText;
    
    @FXML
    private VBox categoriaBox;
    @FXML
    private ComboBox<CategoriaProduto> categoriaComboBox;
    
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
    private ComboBox<Marca> marcaComboBox;
    
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

        //Adicionando Formatador de Texto
        Formatter.decimal(custoProdutoText);
        Formatter.decimal(valorProdutoText);
        Formatter.decimal(quantidadeText);
        
        Formatter.toUpperCase(descricaoText, custoProdutoText, novaCategoriaText);
        
        this.sincronizarBancoDados();//Atualizando Listas com o Banco de Dados
        this.atualizarComboBoxs();//Adicionando itens nos ComboBox
        
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
        boolean vazio = Formatter.isEmpty(descricaoText, custoProdutoText, valorProdutoText, quantidadeText)
                || Formatter.isEmpty(categoriaComboBox, marcaComboBox);
        
        String descricao = descricaoText.getText();
        CategoriaProduto categoria;
        Marca marca;
        float custoProduto = Float.parseFloat(custoProdutoText.getText());
        float valorVenda = Float.parseFloat(valorProdutoText.getText());
        float quantidade = Float.parseFloat(quantidadeText.getText());
        
        if (vazio) {
            Alerta.erro("Preencha todos os Campos para cadastrar um novo Produto");
        } else {
            categoria = categoriaComboBox.getValue();
            marca = marcaComboBox.getValue();
            
            Produto novoProduto = new Produto(null, marca, descricao, categoria, custoProduto, valorVenda, quantidade, null);
            
            Long id = ControleDAO.getBanco().getProdutoDAO().inserir(novoProduto);
            
            if (id == null) {
                Alerta.erro("Erro ao adicionar novo Produto");
            } else {
                Alerta.info("Novo produto Adicionado com sucesso!");
                this.cancelarOperacao();
            }
            
        }
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
        
        boolean vazio = Formatter.isEmpty(novaCategoriaText);
        String novaCategoria = novaCategoriaText.getText();
        
        if (!vazio) {
            CategoriaProduto categoria = new CategoriaProduto(null, novaCategoria);
            
            Long id = ControleDAO.getBanco().getCategoriaProdutoDAO().inserir(categoria);
            
            if (id == null) {//Erro ao inserir item no Banco de Dados
                Alerta.erro("Erro ao criar nova Categoria de Produto");
            } else {
                categoria.setId(id);
                this.categoriaComboBox.getItems().add(categoria);
                this.categoriaComboBox.getSelectionModel().select(categoria);
            }
        }
        
        Formatter.limpar(novaCategoriaText);
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
        
        boolean vazio = Formatter.isEmpty(novaMarcaText);
        String novaMarca = novaMarcaText.getText();
        
        if (!vazio) {
            Marca marca = new Marca(null, novaMarca);
            
            Long id = ControleDAO.getBanco().getMarcaDAO().inserir(marca);
            
            if (id == null) {
                Alerta.erro("Erro ao criar um nova Categoria de Marca");
            } else {
                marca.setId(id);
                this.marcaComboBox.getItems().add(marca);
                this.marcaComboBox.getSelectionModel().select(marca);
            }
        }
        
        Formatter.limpar(novaMarcaText);
        Platform.runLater(() -> marcaComboBox.requestFocus());//Colocando o Foco
    }
    
    private void sincronizarBancoDados() {
        this.listaCategoriaProdutos = ControleDAO.getBanco().getCategoriaProdutoDAO().listar();
        this.listaMarcas = ControleDAO.getBanco().getMarcaDAO().listar();
    }
    
    private void atualizarComboBoxs() {
        //Transforma a lista em uma Lista Observavel
        ObservableList categoriaProdutos = FXCollections.observableArrayList(listaCategoriaProdutos);
        ObservableList categoriasMarcas = FXCollections.observableArrayList(listaMarcas);
        
        this.categoriaComboBox.setItems(categoriaProdutos);
        this.marcaComboBox.setItems(categoriasMarcas);
    }
    
}
