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
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
import javax.swing.SwingWorker;
import model.Administrador;
import model.Bairro;
import model.CategoriaProduto;
import model.Cidade;
import model.Cliente;
import model.Endereco;
import model.FormaPagamento;
import model.Marca;
import model.Venda;
import model.VendaProduto;
import util.DateUtils;
import util.Formatter;
import util.alerta.Alerta;

public class TelaVendaController extends AnchorPane {

    private BorderPane painelPrincipal;

    private Cliente cliente;
    private Venda venda;

    List<Cidade> listaCidades;
    List<Bairro> listaBairros;
    List<FormaPagamento> listaPagamentos;
    List<Administrador> listaAdministrador;

    List<VendaProduto> listaProdutoVenda;

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

    public TelaVendaController(BorderPane painelPrincipal) {
        this.painelPrincipal = painelPrincipal;

        try {
            FXMLLoader fxml = new FXMLLoader(getClass().getResource("/view/TelaVenda.fxml"));
            fxml.setRoot(this);
            fxml.setController(this);
            fxml.load();
        } catch (IOException ex) {
            System.out.println("[ERRO] : Erro na tela Venda");
            ex.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        Formatter.mascaraCPF(cpfText);//Formatador para CPF
        Formatter.mascaraRG(rgText);//Formatador para Rg
        Formatter.mascaraTelefone(telefoneText);//Formatador para Telefone

        Formatter.toUpperCase(nomeText, adicionarCidadeText, ruaText, adicionarBairroText, numeroText);

        editarClienteCheckBox.setVisible(false);

        //Campos ficam desativados enquanto CheckBox esta desativado
        nomeText.editableProperty().bind(editarClienteCheckBox.selectedProperty());
        telefoneText.editableProperty().bind(editarClienteCheckBox.selectedProperty());
        cpfText.editableProperty().bind(editarClienteCheckBox.selectedProperty());
        rgText.editableProperty().bind(editarClienteCheckBox.selectedProperty());
        cidadeBox.disableProperty().bind(editarClienteCheckBox.selectedProperty().not());
        bairroBox.disableProperty().bind(editarClienteCheckBox.selectedProperty().not());
        ruaText.editableProperty().bind(editarClienteCheckBox.selectedProperty());
        numeroText.editableProperty().bind(editarClienteCheckBox.selectedProperty());
        
        valorParcelaBox.setVisible(false);

        dataDatePicker.editableProperty().bind(editarClienteCheckBox.selectedProperty());
        vendedorComboBox.editableProperty().bind(editarClienteCheckBox.selectedProperty());
        formarPagComboBox.editableProperty().bind(editarClienteCheckBox.selectedProperty());
        parcelasSpinner.disableProperty().bind(editarClienteCheckBox.selectedProperty().not());
        
    }

    private void adicionarPainelInterno(AnchorPane novaTela) {
        this.painelPrincipal.setCenter(novaTela);
    }

    @FXML
    private void cancelarOperacao() {
        TelaInicialController telaInicial = new TelaInicialController(painelPrincipal);
        this.adicionarPainelInterno(telaInicial);
    }

    private void adicionarDadosCliente(Cliente cliente) {
        this.nomeText.setText(cliente.getNome());
        this.telefoneText.setText(cliente.getTelefone());
        this.cpfText.setText(cliente.getCpf());
        this.rgText.setText(cliente.getRg());

        Endereco endereco = cliente.getEndereco();
        Bairro bairro = endereco.getBairro();
        Cidade cidade = bairro.getCidade();

        //this.selecionarCidade();
        this.cidadeComboBox.setValue(cidade);
        //this.cidadeComboBox.getSelectionModel().select(cidade);

        //sincronizarBancoDadosBairro(cidade);
        bairroComboBox.setValue(bairro);

        this.ruaText.setText(endereco.getRua());
        this.numeroText.setText(endereco.getNumero());
    }

    private void adicionarDadosVenda(Venda venda) {
        Long data = venda.getData();
        Administrador vendedor = venda.getAdministrador();
        FormaPagamento formaPagamento = venda.getFormaPagamento();
        
        int parcelas = venda.getQuantidadeParcelas();
        
        this.dataDatePicker.setValue(DateUtils.createLocalDate(data));
        this.vendedorComboBox.setValue(vendedor);
        this.formarPagComboBox.setValue(formaPagamento);
        
        SpinnerValueFactory<Integer> valores = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 5, parcelas);
        parcelasSpinner.setValueFactory(valores);
        
        
        //this.parcelasSpinner.setValueFactory(valores);
    }

    public void setVenda(Venda venda) {
        this.venda = venda;
        adicionarDadosVenda(venda);
        this.cliente = venda.getCliente();
        adicionarDadosCliente(cliente);
        sincronizarBancoDadosProdutosVenda(venda);
    }

    private void sincronizarBancoDadosProdutosVenda(Venda venda) {
        //Metodo executado numa Thread separada
        SwingWorker<List, List> worker = new SwingWorker<List, List>() {
            @Override
            protected List<VendaProduto> doInBackground() throws Exception {
                return ControleDAO.getBanco().getVendaDAO().buscarVendaProduto(venda);
            }

            //Metodo chamado apos terminar a execucao numa Thread separada
            @Override
            protected void done() {
                super.done(); //To change body of generated methods, choose Tools | Templates.
                try {
                    listaProdutoVenda = this.get();
                    atualizarTabela();
                } catch (InterruptedException | ExecutionException ex) {
                    Alerta.erro("Erro ao consultar Banco de Dados");
                    ex.printStackTrace();
                }
            }
        };
        worker.execute();
    }
    
    private void atualizarTabela() {
        //Transforma a lista em uma Lista Observavel
        ObservableList data = FXCollections.observableArrayList(listaProdutoVenda);
        
        this.categoriaColumn.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        this.descricaoColumn.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        this.marcaColumn.setCellValueFactory(new PropertyValueFactory<>("marca"));
        this.precoColumn.setCellValueFactory(new PropertyValueFactory<>("precoProduto"));
        this.quantidadeColumn.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        this.totalColumn.setCellValueFactory(new PropertyValueFactory<>("precoTotal"));
        atualizarPrecoParcelas();

        Platform.runLater(() -> {
            this.totalLabel.setText(new DecimalFormat("#,###.00").format(venda.getPrecoTotal()));
        });
        
        this.produtosTable.setItems(data);//Adiciona a lista de clientes na Tabela
    }
    
    private void atualizarPrecoParcelas() {
        Integer parcela = parcelasSpinner.getValue();
        if (parcela != null) {
            if (parcela > 1 && !listaProdutoVenda.isEmpty()) {
                double valor = venda.getPrecoTotal()/parcela;
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
