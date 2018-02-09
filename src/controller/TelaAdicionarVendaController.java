/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import banco.ControleDAO;
import java.io.IOException;
import java.time.LocalDate;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
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
import util.DateUtils;
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
    private CheckBox editarClienteCheckBox;
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
        
        this.editarClienteCheckBox.setVisible(false);//Ocultando componente
        this.editarClienteCheckBox.setSelected(true);//Deixando o CheckBox selecionado
        
        this.editarClienteCheckBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                editarClienteCheckBox.setVisible(false);
                Platform.runLater(() -> nomeText.requestFocus());//Colocando o Foco
            }
        });
        
        //Campos ficam desativados enquanto CheckBox esta desativado
        nomeText.disableProperty().bind(editarClienteCheckBox.selectedProperty().not());
        telefoneText.disableProperty().bind(editarClienteCheckBox.selectedProperty().not());
        cpfText.disableProperty().bind(editarClienteCheckBox.selectedProperty().not());
        rgText.disableProperty().bind(editarClienteCheckBox.selectedProperty().not());
        cidadeText.disableProperty().bind(editarClienteCheckBox.selectedProperty().not());
        enderecoText.disableProperty().bind(editarClienteCheckBox.selectedProperty().not());
        
        Formatter.mascaraCPF(cpfText);//Formatador para CPF
        Formatter.mascaraRG(rgText);//Formatador para Rg
        Formatter.mascaraTelefone(telefoneText);//Formatador para Telefone
        
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
        boolean vazio = Formatter.noEmpty(nomeText, telefoneText, cpfText, rgText, cidadeText, enderecoText);
        boolean carrinhoVazio = novaVenda.isEmpty();
        
        if (cliente != null || vazio || !carrinhoVazio) {
            Dialogo.Resposta resposta = Alerta.confirmar("Deseja cancelar esta venda?");

            if (resposta == Dialogo.Resposta.YES) {
                TelaInicialController telaInicial = new TelaInicialController(painelPrincipal);
                this.adicionarPainelInterno(telaInicial);
            }
        } else {
            TelaInicialController telaInicial = new TelaInicialController(painelPrincipal);
            this.adicionarPainelInterno(telaInicial);
        }
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
            
            VendaProduto vendaProduto = novaVenda.containsProduto(produto);
            
            if (vendaProduto != null) {//Verificando se o produto Selecionado ja existe na lista de Compras
                float novaQuantidade = vendaProduto.getQuantidade() + quantidadeVendida;
                if (novaQuantidade > vendaProduto.getProduto().getEstoque()) {//Verificando se a nova quantidade ultrapassa a quantidade permitida
                    Alerta.alerta("A quantidade selecionada ultrapassa a disponível no estoque");
                } else {
                    vendaProduto.setQuantidade(novaQuantidade);
                    this.novaVenda.atualizarVenda();
                    this.produtosTable.refresh();
                    this.atualizarTabela();
                }
            } else {
                //Criando um novo VendaProduto
                vendaProduto = new VendaProduto(quantidadeVendida, novaVenda, produto);
                this.novaVenda.adicionarVendaProduto(vendaProduto);
                this.atualizarTabela();
            }
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
        
        Cliente cliente = null;
        boolean continuar = false;
        
        if (vazio || carrinhoVazio) {
            Alerta.alerta("Não é possivel finalizar essa Venda", "Erro");
        } else {
            Dialogo.Resposta resposta = Alerta.confirmar("Deseja concluir esta Venda ?");
            if (resposta == Dialogo.Resposta.YES) {
                
                if (novoCliente) {//Criar um Novo Cliente
                    cliente = criarCliente();
                    Long id = ControleDAO.getBanco().getClienteDAO().inserir(cliente);
                    if (id == null) {
                        Alerta.erro("Erro ao cadastrar Novo Usuário");
                    } else {
                        cliente.setId(id);
                        continuar = true;
                    }
                } else {//Cliente selecionado
                    cliente = atualizarCliente(this.cliente);
                    if (editarClienteCheckBox.isSelected()) {//Houve modificacoes
                        if (ControleDAO.getBanco().getClienteDAO().editar(cliente)) {
                            continuar = true;
                        } else {
                            Alerta.erro("Erro ao atualizar informações do Cliente");
                        }
                    } else {
                        continuar = true;
                    }
                }

                if (continuar) {
                    LocalDate data = dataDatePicker.getValue();
                    Administrador vendedor = vendedorComboBox.getValue();
                    int formaPagamento = 1;
                    switch (formarPagComboBox.getValue()) {
                        case "Dinheiro à Vista":
                            formaPagamento = 1;
                            break;
                        case "Cartão de Crédito":
                            formaPagamento = 2;
                            break;
                    }
                    this.novaVenda.setAdministrador(vendedor);
                    this.novaVenda.setCliente(cliente);
                    this.novaVenda.setFormaPagamento(formaPagamento);
                    this.novaVenda.setData(DateUtils.getLong(data));

                    Long id = ControleDAO.getBanco().getVendaDAO().inserir(novaVenda);

                    if (id == null) {
                        Alerta.erro("Erro ao adicionar nova Venda");
                    } else {
                        Alerta.info("Venda Realizada com sucesso!");
                        TelaInicialController telaInicial = new TelaInicialController(painelPrincipal);
                        this.adicionarPainelInterno(telaInicial);
                    }
                }
            }
        }
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
            this.editarClienteCheckBox.setVisible(true);//Mostando componente
            this.editarClienteCheckBox.setSelected(false);//Desativando selecao do CheckBox
            Platform.runLater(() -> dataDatePicker.requestFocus());//Colocando o Foco
        }
    }
    
    private Cliente criarCliente() {
        String nome = nomeText.getText();
        String telefone = telefoneText.getText();
        String cpf = cpfText.getText();
        String rg = rgText.getText();
        String cidade = cidadeText.getText();
        String endereco = enderecoText.getText();
        
        return new Cliente(null, nome, endereco, cpf, rg, telefone, cidade, null, 0);
    }
    
    private Cliente atualizarCliente(Cliente cliente) {
        cliente.setNome(nomeText.getText());
        cliente.setTelefone(telefoneText.getText());
        cliente.setCpf(cpfText.getText());
        cliente.setRg(rgText.getText());
        cliente.setCidade(cidadeText.getText());
        cliente.setEndereco(enderecoText.getText());
        return cliente;
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
