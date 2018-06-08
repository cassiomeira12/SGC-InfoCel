package controller;

import banco.ControleDAO;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ExecutionException;
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
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.swing.SwingWorker;
import model.Administrador;
import model.Bairro;
import model.CategoriaProduto;
import model.Cidade;
import model.Cliente;
import model.Endereco;
import model.FormaPagamento;
import model.Marca;
import model.Produto;
import model.Venda;
import model.VendaProduto;
import relatorio.DescricaoVenda;
import util.DateUtils;
import util.Formatter;
import util.alerta.Alerta;
import util.alerta.Dialogo;

public class TelaAdicionarVendaController extends AnchorPane {

    private BorderPane painelPrincipal;

    private Cliente cliente;
    private Venda novaVenda;
    
    List<Cidade> listaCidades;
    List<Bairro> listaBairros;
    List<FormaPagamento> listaPagamentos;
    List<Administrador> listaAdministrador;

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
    private HBox cidadeBox;
    @FXML
    private ComboBox<Cidade> cidadeComboBox;
    
    @FXML
    private HBox adicionarCidadeBox;
    @FXML
    private TextField adicionarCidadeText;
    
    @FXML
    private HBox bairroBox;
    @FXML
    private ComboBox<Bairro> bairroComboBox;
    
    @FXML
    private HBox adicionarBairroBox;
    @FXML
    private TextField adicionarBairroText;
    
    @FXML
    private TextField ruaText;
    @FXML
    private TextField numeroText;
    
    @FXML
    private DatePicker dataDatePicker;
    @FXML
    private ComboBox<Administrador> vendedorComboBox;
    @FXML
    private ComboBox<FormaPagamento> formarPagComboBox;
    @FXML
    private Spinner<Integer> parcelasSpinner;
    @FXML
    private Button removerButton;
    @FXML
    private Label totalLabel;
    @FXML
    private Label parcelasLabel;

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
    
    @FXML
    private HBox valorParcelaBox;
    @FXML
    private Label valorParcelasLabel;

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
        this.novaVenda = new Venda(null, null, null, null, null, 1, null);

        Formatter.mascaraCPF(cpfText);//Formatador para CPF
        Formatter.mascaraRG(rgText);//Formatador para Rg
        Formatter.mascaraTelefone(telefoneText);//Formatador para Telefone

        Formatter.toUpperCase(nomeText, adicionarCidadeText, ruaText, adicionarBairroText, numeroText);

        this.editarClienteCheckBox.setVisible(false);//Ocultando componente
        this.editarClienteCheckBox.setSelected(true);//Deixando o CheckBox selecionado
        this.editarClienteCheckBox.setOnAction((e) -> {
            editarClienteCheckBox.setVisible(false);
            Platform.runLater(() -> nomeText.requestFocus());//Colocando o Foco
        });
        
        //Campos ficam desativados enquanto CheckBox esta desativado
        nomeText.disableProperty().bind(editarClienteCheckBox.selectedProperty().not());
        telefoneText.disableProperty().bind(editarClienteCheckBox.selectedProperty().not());
        cpfText.disableProperty().bind(editarClienteCheckBox.selectedProperty().not());
        rgText.disableProperty().bind(editarClienteCheckBox.selectedProperty().not());
        cidadeBox.disableProperty().bind(editarClienteCheckBox.selectedProperty().not());
        bairroBox.disableProperty().bind(editarClienteCheckBox.selectedProperty().not());
        ruaText.disableProperty().bind(editarClienteCheckBox.selectedProperty().not());
        numeroText.disableProperty().bind(editarClienteCheckBox.selectedProperty().not());

        //Desativa os Botoes de Excluir quando nenhum item na tabela esta selecionado
        removerButton.disableProperty().bind(produtosTable.getSelectionModel().selectedItemProperty().isNull());

        this.dataDatePicker.setValue(LocalDate.now());//Adicionando Data do dia atual
        
        formarPagComboBox.setOnAction((e) -> {
            FormaPagamento pagamento = formarPagComboBox.getSelectionModel().getSelectedItem();
            SpinnerValueFactory<Integer> valores = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, pagamento.getMaximoParcelas(), 1);
            parcelasSpinner.setValueFactory(valores);
        });
        
        adicionarCidadeBox.setVisible(false);
        adicionarBairroBox.setVisible(false);
        valorParcelaBox.setVisible(false);
        
        cidadeComboBox.setOnAction((e) -> {
            Cidade cidade = cidadeComboBox.getValue();
            bairroComboBox.getSelectionModel().select(null);
            sincronizarBancoDadosBairro(cidade);
        });
        
        parcelasSpinner.setOnMouseClicked((e) -> {
            atualizarPrecoParcelas();
        });
        
        sincronizarBancoDadosAdministrador();
        sincronizarBancoDadosPagamento();
        sincronizarBancoDadosCidade();
    }

    private void adicionarPainelInterno(AnchorPane novaTela) {
        this.painelPrincipal.setCenter(novaTela);
    }

    @FXML
    private void cancelarOperacao() {
        boolean vazio = Formatter.noEmpty(nomeText, telefoneText, cpfText, rgText, ruaText);
        boolean carrinhoVazio = novaVenda.isEmpty();

        if (cliente != null || vazio || !carrinhoVazio) {
            Dialogo.Resposta resposta = Alerta.confirmar("Deseja cancelar esta Venda?");

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
        boolean vazio = Formatter.isEmpty(nomeText, telefoneText, cpfText, rgText, ruaText, numeroText);
        boolean enderecoVazio = true;
        boolean carrinhoVazio = novaVenda.isEmpty();
        
        if (cidadeBox.isVisible() && bairroBox.isVisible()) {
            enderecoVazio = Formatter.isEmpty(cidadeComboBox, bairroComboBox);
        } else if (cidadeBox.isVisible() && adicionarBairroBox.isVisible()) {
            enderecoVazio = Formatter.isEmpty(cidadeComboBox) || adicionarBairroText.getText().isEmpty();
        } else if (adicionarCidadeBox.isVisible() && adicionarBairroBox.isVisible()) {
            enderecoVazio = adicionarCidadeText.getText().isEmpty() || adicionarBairroText.getText().isEmpty();
        }
        
        Cliente cliente = null;
        boolean continuar = false;
        
        if (vazio || carrinhoVazio || enderecoVazio) {
            Alerta.alerta("Não é possivel finalizar essa Venda", "Erro");
        } else {
            Dialogo.Resposta resposta = Alerta.confirmar("Deseja concluir esta Venda ?");
            if (resposta == Dialogo.Resposta.YES) {

                if (novoCliente) {//Criar um Novo Cliente
                    try {
                        cliente = criarCliente();
                        Long id = ControleDAO.getBanco().getClienteDAO().inserir(cliente);
                        if (id == null) {
                            Alerta.erro("Erro ao cadastrar Novo Usuário: ");
                        } else {
                            cliente.setId(id);
                            this.cliente = cliente;
                            continuar = true;
                        }
                    } catch (Exception e) {
                        Alerta.erro("Erro ao cadastrar Novo Usuário: " + e.getMessage());
                        e.printStackTrace();
                    }
                } else {//Cliente selecionado
                    try {
                        cliente = atualizarCliente(this.cliente);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (editarClienteCheckBox.isSelected()) {//Houve modificacoes
                        try {
                            if (ControleDAO.getBanco().getClienteDAO().editar(cliente)) {
                                continuar = true;
                            } else {
                                Alerta.erro("Erro ao atualizar informações do Cliente");
                            }
                        } catch (Exception e) {
                            Alerta.erro("Erro ao atualizar informações do Cliente: " + e.getMessage());
                        }
                    } else {
                        continuar = true;
                    }
                }

                if (continuar) {
                    Long data = DateUtils.getLong(dataDatePicker.getValue());
                    Administrador vendedor = vendedorComboBox.getValue();
                    FormaPagamento formaPagamento = formarPagComboBox.getValue();
                    int parcelas = parcelasSpinner.getValue();
                    
                    LocalDate date = dataDatePicker.getValue();
                    LocalDate hoje = LocalDate.now();
                    if (date.isEqual(hoje)) {
                        data = System.currentTimeMillis();
                    }
                    
                    this.novaVenda.setAdministrador(vendedor);
                    this.novaVenda.setCliente(cliente);
                    this.novaVenda.setFormaPagamento(formaPagamento);
                    this.novaVenda.setQuantidadeParcelas(parcelas);
                    this.novaVenda.setData(data);
                    
                    try {
                        Long id = ControleDAO.getBanco().getVendaDAO().inserir(novaVenda);

                        if (id == null) {
                            Alerta.erro("Erro ao adicionar nova Venda");
                        } else {
                            
                            Dialogo.Resposta abrirPDF = Alerta.confirmar("Venda finalizada com sucesso!\n"
                                                                        + "Deseja abrir o Comprovante de Venda ?");
                            
                            DescricaoVenda dv = new DescricaoVenda(id);
                            
                            if (abrirPDF == Dialogo.Resposta.YES) {
                                dv.setMostrar(true);
                            }
                            
                            dv.run();
                            
                            TelaInicialController telaInicial = new TelaInicialController(painelPrincipal);
                            this.adicionarPainelInterno(telaInicial);
                        }
                    } catch (Exception e) {
                        Alerta.erro("Erro ao adicionar nova Venda: " + e.getMessage());
                        e.printStackTrace();
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

    private Cliente criarCliente() throws Exception {
        String nome = nomeText.getText();
        String telefone = telefoneText.getText();
        String cpf = cpfText.getText();
        String rg = rgText.getText();
        String rua = ruaText.getText();
        String numero = numeroText.getText();
        LocalDate data = dataDatePicker.getValue();
        
        Cidade cidade = null;
        Bairro bairro = null;
        Endereco endereco = null;
        
        if (cidadeBox.isVisible()) {
            cidade = cidadeComboBox.getValue();
        } else if (adicionarCidadeBox.isVisible()) {
            cidade = new Cidade(null, adicionarCidadeText.getText());
        }
        
        if (bairroBox.isVisible()) {
            bairro = bairroComboBox.getValue();
        } else if (adicionarBairroBox.isVisible()) {
            bairro = new Bairro(null, adicionarBairroText.getText(), cidade);
        }
        
        endereco = new Endereco(null, bairro, rua, numero);

        return new Cliente(null, nome, endereco, cpf, rg, telefone, DateUtils.getLong(data), true);
    }

    private Cliente atualizarCliente(Cliente cliente) throws Exception {
        cliente.setNome(nomeText.getText());
        cliente.setTelefone(telefoneText.getText());
        cliente.setCpf(cpfText.getText());
        cliente.setRg(rgText.getText());
        String rua = ruaText.getText();
        String numero = numeroText.getText();
        
        Cidade cidade = null;
        Bairro bairro = null;
        
        if (cidadeBox.isVisible()) {
            cidade = cidadeComboBox.getValue();
        } else if (adicionarCidadeBox.isVisible()) {
            cidade = new Cidade(null, adicionarCidadeText.getText());
            Long id = ControleDAO.getBanco().getCidadeDAO().inserir(cidade);
            cidade.setId(id);
        }
        
        if (bairroBox.isVisible()) {
            bairro = bairroComboBox.getValue();
        } else if (adicionarBairroBox.isVisible()) {
            bairro = new Bairro(null, adicionarBairroText.getText(), cidade);
            Long id = ControleDAO.getBanco().getBairroDAO().inserir(bairro);
            bairro.setId(id);
        }
        
        cliente.getEndereco().setBairro(bairro);
        cliente.getEndereco().setRua(rua);
        cliente.getEndereco().setNumero(numero);
        
        return cliente;
    }

    private void adicionarDadosCliente(Cliente cliente) {
        this.nomeText.setText(cliente.getNome());
        this.telefoneText.setText(cliente.getTelefone());
        this.cpfText.setText(cliente.getCpf());
        this.rgText.setText(cliente.getRg());
        
        Endereco endereco = cliente.getEndereco();
        Bairro bairro = endereco.getBairro();
        Cidade cidade = bairro.getCidade();
        
        this.selecionarCidade();
        this.cidadeComboBox.getSelectionModel().select(cidade);
        
        sincronizarBancoDadosBairro(cidade);
        bairroComboBox.setValue(bairro);
        
        this.ruaText.setText(endereco.getRua());
        this.numeroText.setText(endereco.getNumero());
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
        this.totalLabel.setText(new DecimalFormat("#,###.00").format(novaVenda.getPrecoTotal()));
        atualizarPrecoParcelas();
    }
    
    @FXML
    private void adicionarCidade() {
        cidadeBox.setVisible(false);
        adicionarCidadeBox.setVisible(true);
        adicionarBairro();
        Platform.runLater(() -> adicionarCidadeText.requestFocus());//Colocando o Foco
    }
    
    @FXML
    private void selecionarCidade() {
        cidadeBox.setVisible(true);
        adicionarCidadeBox.setVisible(false);
        Formatter.limpar(adicionarCidadeText);
        Platform.runLater(() -> cidadeComboBox.requestFocus());//Colocando o Foco
    }
    
    @FXML
    private void adicionarBairro() {
        bairroBox.setVisible(false);
        adicionarBairroBox.setVisible(true);
        Platform.runLater(() -> adicionarBairroText.requestFocus());//Colocando o Foco
    }
    
    @FXML
    private void selecionarBairro() {
        bairroBox.setVisible(true);
        adicionarBairroBox.setVisible(false);
        Formatter.limpar(adicionarBairroText);
        Platform.runLater(() -> bairroComboBox.requestFocus());//Colocando o Foco
    }
    
    private void sincronizarBancoDadosCidade() {
        //Metodo executado numa Thread separada
        SwingWorker<List, List> worker = new SwingWorker<List, List>() {
            @Override
            protected List<Cidade> doInBackground() throws Exception {
                return ControleDAO.getBanco().getCidadeDAO().listar();
            }

            //Metodo chamado apos terminar a execucao numa Thread separada
            @Override
            protected void done() {
                super.done(); //To change body of generated methods, choose Tools | Templates.
                try {
                    listaCidades = this.get();
                    ObservableList cidades = FXCollections.observableArrayList(listaCidades);
                    cidadeComboBox.setItems(cidades);
                } catch (InterruptedException | ExecutionException ex) {
                    chamarAlerta("Erro ao consultar Banco de Dados");
                    ex.printStackTrace();
                }
            }
        };
        worker.execute();
    }
    
    private void sincronizarBancoDadosBairro(Cidade cidade) {
        //Metodo executado numa Thread separada
        SwingWorker<List, List> worker = new SwingWorker<List, List>() {
            @Override
            protected List<Bairro> doInBackground() throws Exception {
                return ControleDAO.getBanco().getBairroDAO().buscarPorCidade(cidade);
            }

            //Metodo chamado apos terminar a execucao numa Thread separada
            @Override
            protected void done() {
                super.done(); //To change body of generated methods, choose Tools | Templates.
                try {
                    listaBairros = this.get();
                    ObservableList bairros = FXCollections.observableArrayList(listaBairros);
                    bairroComboBox.setItems(bairros);
                } catch (InterruptedException | ExecutionException ex) {
                    chamarAlerta("Erro ao consultar Banco de Dados");
                    ex.printStackTrace();
                }
            }
        };

        worker.execute();
    }
    
    private void sincronizarBancoDadosAdministrador() {
        //Metodo executado numa Thread separada
        SwingWorker<List, List> worker = new SwingWorker<List, List>() {
            @Override
            protected List<Administrador> doInBackground() throws Exception {
                return ControleDAO.getBanco().getAdministradorDAO().listar();
            }

            //Metodo chamado apos terminar a execucao numa Thread separada
            @Override
            protected void done() {
                super.done(); //To change body of generated methods, choose Tools | Templates.
                try {
                    listaAdministrador = this.get();
                    ObservableList administradores = FXCollections.observableArrayList(listaAdministrador);
                    vendedorComboBox.setItems(administradores);
                    
                    vendedorComboBox.getSelectionModel().select(LoginController.admLogado);
                } catch (InterruptedException | ExecutionException ex) {
                    chamarAlerta("Erro ao consultar Banco de Dados");
                    ex.printStackTrace();
                }
            }
        };
        worker.execute();
    }
    
    private void sincronizarBancoDadosPagamento() {
        //Metodo executado numa Thread separada
        SwingWorker<List, List> worker = new SwingWorker<List, List>() {
            @Override
            protected List<FormaPagamento> doInBackground() throws Exception {
                return ControleDAO.getBanco().getFormaPagamentoDAO().listar();
            }

            //Metodo chamado apos terminar a execucao numa Thread separada
            @Override
            protected void done() {
                super.done(); //To change body of generated methods, choose Tools | Templates.
                try {
                    listaPagamentos = this.get();
                    ObservableList pagamentos = FXCollections.observableArrayList(listaPagamentos);
                    formarPagComboBox.setItems(pagamentos);
                } catch (InterruptedException | ExecutionException ex) {
                    chamarAlerta("Erro ao consultar Banco de Dados");
                    ex.printStackTrace();
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
    
    private void atualizarPrecoParcelas() {
        Integer parcela = parcelasSpinner.getValue();
        if (parcela != null) {
            if (parcela > 1 && !novaVenda.getVendaProdutos().isEmpty()) {
                double valor = novaVenda.getPrecoTotal()/parcela;
                setParcelasLabel(valor);
                valorParcelaBox.setVisible(true);
            } else {
                setParcelasLabel(0);
                valorParcelaBox.setVisible(false);
            }
        }
    }
    
    private void setParcelasLabel(double valor) {
        Platform.runLater(()-> {
            valorParcelasLabel.setText(new DecimalFormat("#,###.00").format(valor));
        });
    }
    
}