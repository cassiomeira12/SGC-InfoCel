/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import banco.ControleDAO;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import model.CategoriaProduto;
import model.Marca;
import model.Produto;
import model.UnidadeMedida;
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
    private List<UnidadeMedida> listaUnidadeMedidas;

    @FXML
    private TextField descricaoText;

    @FXML
    private Label categoriaLabel;
    @FXML
    private HBox categoriaBox;
    @FXML
    private ComboBox<CategoriaProduto> categoriaComboBox;
    @FXML
    private HBox novaCategoriaBox;
    @FXML
    private TextField novaCategoriaText;

    @FXML
    private Label marcaLabel;
    @FXML
    private HBox marcaBox;
    @FXML
    private ComboBox<Marca> marcaComboBox;

    @FXML
    private ComboBox<UnidadeMedida> unidadeMedidaComboBox;

    @FXML
    private HBox novaMarcaBox;
    @FXML
    private TextField novaMarcaText;

    @FXML
    private TextField custoProdutoText;
    @FXML
    private TextField valorProdutoText;
    @FXML
    private Label percentualLabel;
    @FXML
    private TextField quantidadeText;
    @FXML
    private Button salvarButton;
    

    public TelaAdicionarProdutoController(BorderPane painelPrincipal) {
        this.painelPrincipal = painelPrincipal;

        try {
            FXMLLoader fxml = new FXMLLoader(getClass().getResource("/view/TelaAdicionarProduto.fxml"));
            fxml.setRoot(this);
            fxml.setController(this);
            fxml.load();
        } catch (IOException ex) {
            System.out.println("[ERRO] : Erro na tela Adicionar Produto");
            ex.printStackTrace();
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

        Formatter.toUpperCase(descricaoText, custoProdutoText, novaCategoriaText, novaMarcaText);
        
        
        salvarButton.disableProperty().bind(descricaoText.textProperty().isEmpty().or(
                                            categoriaComboBox.selectionModelProperty().isNull().or(
                                            marcaComboBox.selectionModelProperty().isNull().or(
                                            custoProdutoText.textProperty().isEmpty().or(
                                            valorProdutoText.textProperty().isEmpty().or(
                                            unidadeMedidaComboBox.selectionModelProperty().isNull().or(
                                            quantidadeText.textProperty().isEmpty())))))));
        
        

        this.sincronizarBancoDados();//Atualizando Listas com o Banco de Dados
        this.atualizarComboBoxs();//Adicionando itens nos ComboBox
        calcularPercentual();
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

            try {
                if (ControleDAO.getBanco().getProdutoDAO().inserir(novoProduto) == null) {
                    Alerta.erro("Erro ao adicionar novo Produto");
                } else {
                    Alerta.info("Novo produto Adicionado com sucesso!");
                    this.cancelarOperacao();
                }
            } catch (Exception ex) {
                Alerta.erro("Erro ao adicionar novo Produto" + ex.toString());
            }

        }
    }

    @FXML
    private void adicionarNovaCategoria() {
        this.categoriaLabel.setText("Nova Categoria");
        this.categoriaBox.setVisible(false);
        this.novaCategoriaBox.setVisible(true);
    }

    @FXML
    private void salvarNovaCategoria() {
        this.categoriaLabel.setText("Categoria");
        this.novaCategoriaBox.setVisible(false);
        this.categoriaBox.setVisible(true);

        boolean vazio = Formatter.isEmpty(novaCategoriaText);
        String novaCategoria = novaCategoriaText.getText();

        if (!vazio) {
            CategoriaProduto categoria = new CategoriaProduto(null, novaCategoria);

            Long id = null;
            try {
                id = ControleDAO.getBanco().getCategoriaProdutoDAO().inserir(categoria);
            } catch (Exception ex) {
                Logger.getLogger(TelaAdicionarProdutoController.class.getName()).log(Level.SEVERE, null, ex);
            }

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
        this.marcaLabel.setText("Nova Marca");
        this.marcaBox.setVisible(false);
        this.novaMarcaBox.setVisible(true);
    }

    @FXML
    private void salvarNovaMarca() {
        this.marcaLabel.setText("Marca");
        this.novaMarcaBox.setVisible(false);
        this.marcaBox.setVisible(true);

        boolean vazio = Formatter.isEmpty(novaMarcaText);
        String novaMarca = novaMarcaText.getText();

        if (!vazio) {
            Marca marca = new Marca(null, novaMarca);

            try {
                Long id = ControleDAO.getBanco().getMarcaDAO().inserir(marca);
                if (id == null) {
                    Alerta.erro("Erro ao criar um nova Categoria de Marca");
                } else {
                    marca.setId(id);
                    this.marcaComboBox.getItems().add(marca);
                    this.marcaComboBox.getSelectionModel().select(marca);
                }

            } catch (Exception ex) {
                Logger.getLogger(TelaAdicionarProdutoController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        Formatter.limpar(novaMarcaText);
        Platform.runLater(() -> marcaComboBox.requestFocus());//Colocando o Foco
    }

    private void sincronizarBancoDados() {
        try {
            this.listaCategoriaProdutos = ControleDAO.getBanco().getCategoriaProdutoDAO().listar();
            this.listaMarcas = ControleDAO.getBanco().getMarcaDAO().listar();
            this.listaUnidadeMedidas = ControleDAO.getBanco().getUnidadeMedidaDAO().listar();
        } catch (SQLException ex) {
            Alerta.erro("Ocorreu um erro:\n" + ex.toString());
        }
    }

    private void atualizarComboBoxs() {
        //Transforma a lista em uma Lista Observavel
        ObservableList categoriaProdutos = FXCollections.observableArrayList(listaCategoriaProdutos);
        ObservableList categoriasMarcas = FXCollections.observableArrayList(listaMarcas);
        ObservableList unidadesMedidas = FXCollections.observableArrayList(listaUnidadeMedidas);

        this.categoriaComboBox.setItems(categoriaProdutos);
        this.marcaComboBox.setItems(categoriasMarcas);
        this.unidadeMedidaComboBox.setItems(unidadesMedidas);
        
        //this.unidadeMedidaComboBox.getSelectionModel().select(3);
    }

    private void calcularPercentual() {
        valorProdutoText.textProperty().addListener((ov, oldValue, newValue) -> {

            if (custoProdutoText.getText().isEmpty()) {
                Platform.runLater(() -> custoProdutoText.requestFocus());//Colocando o Foco
                Formatter.limpar(valorProdutoText);
                percentualLabel.setText("0%");
                return;
            }

            if (!valorProdutoText.getText().isEmpty()) {
                float custoProduto = Float.parseFloat(custoProdutoText.getText());
                float valorVenda = Float.parseFloat(valorProdutoText.getText());
                float lucro = valorVenda - custoProduto;

                float percentual = (lucro / custoProduto) * 100;

                DecimalFormat df = new DecimalFormat("#,###.##");

                if (valorVenda >= custoProduto) {
                    percentualLabel.setText(df.format(percentual) + "%");
                } else {
                    percentualLabel.setText("0%");
                }
            }

        });
    }
}
