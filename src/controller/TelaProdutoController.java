/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import banco.ControleDAO;
import controller.AdicionarProdutoDescricaoController.Tipo;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.swing.SwingWorker;
import model.CategoriaProduto;
import model.Marca;
import model.Produto;
import model.UnidadeMedida;
import util.Formatter;
import util.alerta.Alerta;
import util.alerta.Dialogo;

/**
 * FXML Controller class
 *
 * @author cassio
 */
public class TelaProdutoController extends AnchorPane {

    private BorderPane painelPrincipal;
    
    private Produto produto;

    private List<CategoriaProduto> listaCategoriaProdutos;
    private List<Marca> listaMarcas;
    private List<UnidadeMedida> listaUnidadeMedidas;
    
    @FXML
    private TextField descricaoText;
    @FXML
    private ComboBox<CategoriaProduto> categoriaComboBox;
    @FXML
    private ComboBox<Marca> marcaComboBox;
    @FXML
    private ComboBox<UnidadeMedida> unidadeMedidaCombo;
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

    public TelaProdutoController(BorderPane painelPrincipal) {
        this.painelPrincipal = painelPrincipal;

        try {
            FXMLLoader fxml = new FXMLLoader(getClass().getResource("/view/TelaProduto.fxml"));
            fxml.setRoot(this);
            fxml.setController(this);
            fxml.load();
        } catch (IOException ex) {
            System.out.println("[ERRO] : Erro na tela Produto");
            ex.printStackTrace();
        }
    }

    @FXML
    public void initialize() {

        //Adicionando Formatador de Texto
        Formatter.decimal(custoProdutoText);
        Formatter.decimal(valorProdutoText);
        Formatter.decimal(quantidadeText);

        Formatter.toUpperCase(descricaoText, custoProdutoText);

        salvarButton.disableProperty().bind(categoriaComboBox.getSelectionModel().selectedItemProperty().isNull().or(
                                            marcaComboBox.getSelectionModel().selectedItemProperty().isNull().or(
                                            unidadeMedidaCombo.getSelectionModel().selectedItemProperty().isNull().or(
                                            descricaoText.textProperty().isEmpty().or(
                                            custoProdutoText.textProperty().isEmpty().or(
                                            valorProdutoText.textProperty().isEmpty().or(
                                            quantidadeText.textProperty().isEmpty())))))));
        
        boolean desabilitar = false;
        
        descricaoText.setEditable(desabilitar);
        categoriaComboBox.setDisable(!desabilitar);
        marcaComboBox.setDisable(!desabilitar);
        custoProdutoText.setEditable(desabilitar);
        unidadeMedidaCombo.setDisable(!desabilitar);
        
//        sincronizarBancoDadosCategoria();
//        sincronizarBancoDadosMarca();
//        sincronizarBancoDadosUnidade();
        
        calcularPercentual();
    }

    private void adicionarPainelInterno(AnchorPane novaTela) {
        this.painelPrincipal.setCenter(novaTela);
    }
    
    public void setProduto(Produto produto) {
        this.produto = produto;
        adicionarDados(produto);
    }
    
    private void adicionarDados(Produto produto) {
        String descricao = produto.getDescricao();
        CategoriaProduto categoria = produto.getCategoria();
        Marca marca = produto.getMarca();
        float custo = produto.getPrecoCompra();
        float valor = produto.getPrecoVenda();
        UnidadeMedida unidade = produto.getUnidadeMedida();
        float estoque = produto.getEstoque();
        
        this.descricaoText.setText(descricao);
        this.categoriaComboBox.getSelectionModel().select(categoria);
        this.marcaComboBox.getSelectionModel().select(marca);
        this.custoProdutoText.setText(String.valueOf(custo));
        this.valorProdutoText.setText(String.valueOf(valor));
        this.unidadeMedidaCombo.getSelectionModel().select(unidade);
        this.quantidadeText.setText(String.valueOf(estoque));
    }

    @FXML
    private void cancelarOperacao() {
        TelaConsultarProdutosController tela = new TelaConsultarProdutosController(painelPrincipal);
        this.adicionarPainelInterno(tela);
    }

    @FXML
    private void salvarProduto() {
        
        String descricao = descricaoText.getText();
        CategoriaProduto categoria = categoriaComboBox.getValue();
        Marca marca = marcaComboBox.getValue();
        UnidadeMedida unidadeMedida = unidadeMedidaCombo.getValue();

        float custoProduto = Float.parseFloat(custoProdutoText.getText());
        float valorVenda = Float.parseFloat(valorProdutoText.getText());
        float quantidade = Float.parseFloat(quantidadeText.getText());
        
        Dialogo.Resposta resposta = Alerta.confirmar("Deseja salvar os dados editados ?");
        if (resposta == Dialogo.Resposta.YES) {
            
            produto.setDescricao(descricao);
            produto.setCategoria(categoria);
            produto.setMarca(marca);
            produto.setUnidadeMedida(unidadeMedida);
            produto.setPrecoCompra(custoProduto);
            produto.setPrecoVenda(valorVenda);
            produto.setEstoque(quantidade);
            
            
            try {
                ControleDAO.getBanco().getProdutoDAO().editar(produto);
                Alerta.info("Produto editado com sucesso!");
                cancelarOperacao(); 
            } catch (SQLException ex) {
                Alerta.erro("Erro ao editar produto");
                ex.printStackTrace();
            }
        }
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

    private void sincronizarBancoDadosCategoria() {
        //Metodo executado numa Thread separada
        SwingWorker<List, List> worker = new SwingWorker<List, List>() {
            @Override
            protected List<CategoriaProduto> doInBackground() throws Exception {
                return ControleDAO.getBanco().getCategoriaProdutoDAO().listar();
            }

            //Metodo chamado apos terminar a execucao numa Thread separada
            @Override
            protected void done() {
                super.done(); //To change body of generated methods, choose Tools | Templates.
                try {
                    listaCategoriaProdutos = this.get();
                    ObservableList categoriaProdutos = FXCollections.observableArrayList(listaCategoriaProdutos);
                    categoriaComboBox.setItems(categoriaProdutos);
                } catch (InterruptedException | ExecutionException ex) {
                    chamarAlerta("Erro ao consultar Banco de Dados");
                }
            }
        };

        worker.execute();
    }
        
    private void sincronizarBancoDadosMarca() {
        //Metodo executado numa Thread separada
        SwingWorker<List, List> worker = new SwingWorker<List, List>() {
            @Override
            protected List<Marca> doInBackground() throws Exception {
                return ControleDAO.getBanco().getMarcaDAO().listar();
            }

            //Metodo chamado apos terminar a execucao numa Thread separada
            @Override
            protected void done() {
                super.done(); //To change body of generated methods, choose Tools | Templates.
                try {
                    listaMarcas = this.get();
                    ObservableList categoriasMarcas = FXCollections.observableArrayList(listaMarcas);
                    marcaComboBox.setItems(categoriasMarcas);
                    
                } catch (InterruptedException | ExecutionException ex) {
                    chamarAlerta("Erro ao consultar Banco de Dados");
                }
            }
        };

        worker.execute();
    }
    
    private void sincronizarBancoDadosUnidade() {
        //Metodo executado numa Thread separada
        SwingWorker<List, List> worker = new SwingWorker<List, List>() {
            @Override
            protected List<UnidadeMedida> doInBackground() throws Exception {
                return ControleDAO.getBanco().getUnidadeMedidaDAO().listar();
            }

            //Metodo chamado apos terminar a execucao numa Thread separada
            @Override
            protected void done() {
                super.done(); //To change body of generated methods, choose Tools | Templates.
                try {
                    listaUnidadeMedidas = this.get();
                    ObservableList unidadesMedidas = FXCollections.observableArrayList(listaUnidadeMedidas);
                    unidadeMedidaCombo.setItems(unidadesMedidas);
                } catch (InterruptedException | ExecutionException ex) {
                    chamarAlerta("Erro ao consultar Banco de Dados");
                }
            }
        };

        worker.execute();
    }

    private void chamarAlerta(String mensagem) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Alerta.erro(mensagem);
            }
        });
    }
}
