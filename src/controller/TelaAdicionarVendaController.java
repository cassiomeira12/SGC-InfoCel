/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import banco.ControleDAO;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.CategoriaProduto;
import model.Cliente;
import model.Marca;
import model.Produto;
import util.Formatter;
import util.alerta.Alerta;

/**
 * FXML Controller class
 *
 * @author cassio
 */
public class TelaAdicionarVendaController extends AnchorPane {
    
    private BorderPane painelPrincipal;
    
    private Cliente cliente;
    private List<Produto> listaProdutos;
    private SimpleFloatProperty valorTotalCompra;
    
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
    @FXML
    private DatePicker dataDatePicker;
    @FXML
    private Label totalLabel;
    
    @FXML
    private TableView<Produto> produtosTable;
    @FXML
    private TableColumn<CategoriaProduto, String> categoriaColumn;
    @FXML
    private TableColumn<Produto, String> descricaoColumn;
    @FXML
    private TableColumn<Marca, String> marcaColumn;
    @FXML
    private TableColumn<Float, String> quantidadeColumn;
    @FXML
    private TableColumn<Float, String> precoColumn;

  
    public TelaAdicionarVendaController(BorderPane painelPrincipal) {
        this.painelPrincipal = painelPrincipal;
        
        try {
            FXMLLoader fxml = new FXMLLoader(getClass().getResource("/view/TelaAdicionarVenda.fxml"));
            fxml.setRoot(this);
            fxml.setController(this);
            fxml.load();
        } catch (IOException ex) {
            System.out.println("[ERRO] : Erro na tela Adicionar Venda");
            System.out.println(ex.toString());
        }
    }

    @FXML
    public void initialize() {
        Formatter.mascaraCPF(cpfText);
        Formatter.mascaraRG(rgText);
        Formatter.mascaraTelefone(telefoneText);
        
        this.valorTotalCompra = new SimpleFloatProperty(0);
        this.totalLabel.textProperty().bind(valorTotalCompra.asString());
        
        this.listaProdutos = new ArrayList<>();
        this.atualizarTabela();
        
        this.dataDatePicker.setValue(LocalDate.now());//Adicionando Data do dia atual
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
    private void adicionarProduto() {

        Stage palco = new Stage();
        palco.initModality(Modality.APPLICATION_MODAL);//Impede de clicar na tela em plano de fundo
        palco.centerOnScreen();
        palco.initStyle(StageStyle.UNDECORATED);//Remove a barra de menu

        TelaSelecionarProdutoController telaSelecionarProduto = new TelaSelecionarProdutoController(palco);
        
        palco.setScene(new Scene(telaSelecionarProduto));
        palco.showAndWait();
        
        if (telaSelecionarProduto.RESULTADO) {//Selecionou Produto
            Produto produto = telaSelecionarProduto.getProduto();
            float quantidadeVendida = telaSelecionarProduto.getQuantidade();
            produto.setEstoque(quantidadeVendida);
            
            this.valorTotalCompra.set(valorTotalCompra.get() + quantidadeVendida * produto.getPrecoVenda());
            
            listaProdutos.add(produto);
            this.atualizarTabela();
        }
        
    }
    
    @FXML
    private void removerProduto() {
        Stage palco = new Stage();
        palco.initModality(Modality.APPLICATION_MODAL);//Impede de clicar na tela em plano de fundo
        palco.centerOnScreen();
        palco.initStyle(StageStyle.UNDECORATED);//Remove a barra de menu

        TelaPesquisarClienteController telaPesquisarCliente = new TelaPesquisarClienteController(palco);
        
        palco.setScene(new Scene(telaPesquisarCliente));
        palco.showAndWait();
        
        if (telaPesquisarCliente.RESULTADO) {//Selecionou Produto
            this.cliente = telaPesquisarCliente.getCliente();
            this.adicionarDadosCliente(cliente);
        }
    }
    
    @FXML
    private void finalizarCompra() {
        //this.criarCliente();
    }
    
    private void criarCliente() {
        boolean vazio = Formatter.isEmpty(nomeText, telefoneText, cpfText, rgText, cidadeText, enderecoText);
        
        String nome = nomeText.getText();
        String telefone = telefoneText.getText();
        String cpf = cpfText.getText();
        String rg = rgText.getText();
        String cidade = cidadeText.getText();
        String endereco = enderecoText.getText();
        
        if (vazio) {
            Alerta.erro("Dados do cliente insuficientes", "Preencha as informações do Cliente");
        } else {
            Cliente cliente = new Cliente(null, nome, endereco, cpf, rg, telefone, cidade, null, 0);
            ControleDAO.getBanco().getClienteDAO().inserir(cliente);
            Alerta.info("Cadastro", "Cliente cadastrado com sucesso");
        }
       
    }
    
    private void adicionarDadosCliente(Cliente cliente) {
        this.nomeText.setText(cliente.getNome());
        this.telefoneText.setText(cliente.getTelefone());
        this.cpfText.setText(cliente.getCpf());
        this.rgText.setText(cliente.getRg());
        this.cidadeText.setText(cliente.getCidade());
        this.enderecoText.setText(cliente.getEndereco());
    }
    
    private void atualizarTabela() {
        //Transforma a lista em uma Lista Observavel
        ObservableList data = FXCollections.observableArrayList(listaProdutos);
        
        this.categoriaColumn.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        this.descricaoColumn.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        this.marcaColumn.setCellValueFactory(new PropertyValueFactory<>("marca"));
        this.quantidadeColumn.setCellValueFactory(new PropertyValueFactory<>("estoque"));
        this.precoColumn.setCellValueFactory(new PropertyValueFactory<>("precoVenda"));
        
        this.produtosTable.setItems(data);
    }

}
