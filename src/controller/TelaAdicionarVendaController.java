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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
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
import model.Administrador;
import model.CategoriaProduto;
import model.Cliente;
import model.Marca;
import model.Produto;
import model.Venda;
import model.VendaProduto;
import util.Formatter;
import util.alerta.Alerta;
import util.alerta.Dialogo;

/**
 * FXML Controller class
 *
 * @author cassio
 */
public class TelaAdicionarVendaController extends AnchorPane {
    
    private BorderPane painelPrincipal;
    
    private Cliente cliente;
    private Venda novaVenda;
    
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
    private ComboBox<Administrador> vendedorComboBox;
    @FXML
    private ComboBox<String> formarPagComboBox;
    @FXML
    private Button removerButton;
    @FXML
    private Label totalLabel;
    
    @FXML
    private TableView<VendaProduto> produtosTable;
    @FXML
    private TableColumn<CategoriaProduto, String> categoriaColumn;
    @FXML
    private TableColumn<String, String> descricaoColumn;
    @FXML
    private TableColumn<Marca, String> marcaColumn;
    @FXML
    private TableColumn<Float, String> precoColumn;
    @FXML
    private TableColumn<Float, String> quantidadeColumn;
    @FXML
    private TableColumn<Float, String> totalColumn;
    

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
        this.novaVenda = new Venda(null, null, null, null, 0, null);
        
        Formatter.mascaraCPF(cpfText);
        Formatter.mascaraRG(rgText);
        Formatter.mascaraTelefone(telefoneText);
        
        //Desativa os Botoes de Excluir quando nenhum item na tabela esta selecionado
        removerButton.disableProperty().bind(produtosTable.getSelectionModel().selectedItemProperty().isNull());
        
        //Adicionando formas de pagamento no ComboBox
        this.formarPagComboBox.getItems().add("Dinheiro à Vista");
        this.formarPagComboBox.getItems().add("Cartão de Crédito");
        //Selecionando primeira forma de Pagamento
        this.formarPagComboBox.getSelectionModel().select(0);//Selecionando o primeiro item
        //Adicionando os Administradores no ComboBox
        this.vendedorComboBox.setItems(FXCollections.observableArrayList(ControleDAO.getBanco().getAdministradorDAO().listar()));
        //Selecionando o Administrador que fez o Login
        this.vendedorComboBox.getSelectionModel().select(LoginController.admLogado);
        
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
            //Criando um novo VendaProduto
            VendaProduto vendaProduto = new VendaProduto(quantidadeVendida, novaVenda, produto);
            
            this.novaVenda.adicionarVendaProduto(vendaProduto);
            this.atualizarTabela();
        }
        
    }
    
    @FXML
    private void removerProduto() {
        VendaProduto vendaProduto = produtosTable.getSelectionModel().getSelectedItem();
        
        Dialogo.Resposta resposta = Alerta.confirmar("Remover produto  " + vendaProduto.getDescricao() + " ?");

        if (resposta == Dialogo.Resposta.YES) {
            novaVenda.removerVendaProduto(vendaProduto);
            atualizarTabela();
        }
        
        produtosTable.getSelectionModel().clearSelection();
    }
    
    @FXML
    private void finalizarCompra() {
        
        boolean novoCliente = this.cliente == null;
        boolean vazio = Formatter.isEmpty(nomeText, telefoneText, cpfText, rgText, cidadeText, enderecoText);
        boolean carrinhoVazio = novaVenda.isEmpty();
        
        String nome = nomeText.getText();
        String telefone = telefoneText.getText();
        String cpf = cpfText.getText();
        String rg = rgText.getText();
        String cidade = cidadeText.getText();
        String endereco = enderecoText.getText();
        
        Cliente cliente = null;
        
        if (vazio) {//Caso os Campos estejam vazios
            Alerta.alerta("Preencha os campos com as informações do Cliente");
        } else {
            if (novoCliente) {//Caso for criar um Novo Cliente
                cliente = new Cliente(null, nome, endereco, cpf, rg, telefone, cidade, null, 0);
                Long id = ControleDAO.getBanco().getClienteDAO().inserir(cliente);

                if (id == null) {
                    Alerta.erro("Erro ao cadastrar Novo Usuário");
                } else {
                    cliente.setId(id);
                }

            } else {//Caso for usar um Cliente ja Cadastrado
                cliente = this.cliente;//Recendo Cliente Selecionado
                //Atualizando informacoes do Cliente
                cliente.setNome(nome);
                cliente.setEndereco(endereco);
                cliente.setCpf(cpf);
                cliente.setRg(rg);
                cliente.setTelefone(telefone);
                cliente.setCidade(cidade);
            }
        }
        //Verificando se foi adicionado algum produto 
        if (carrinhoVazio) {
            Alerta.alerta("O carrinho de produtos está vazio");
        }

        LocalDate data = dataDatePicker.getValue();
        
    }
    
    @FXML
    private void pesquisarCliente() {
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
        ObservableList data = FXCollections.observableArrayList(novaVenda.getVendaProdutos());
        
        this.categoriaColumn.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        this.descricaoColumn.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        this.marcaColumn.setCellValueFactory(new PropertyValueFactory<>("marca"));
        this.precoColumn.setCellValueFactory(new PropertyValueFactory<>("precoProduto"));
        this.quantidadeColumn.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        this.totalColumn.setCellValueFactory(new PropertyValueFactory<>("precoTotal"));
        
        this.produtosTable.setItems(data);
        this.totalLabel.setText(String.valueOf(novaVenda.getPrecoTotal()));
    }

}
