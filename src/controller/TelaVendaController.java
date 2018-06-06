package controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
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
import util.Formatter;

public class TelaVendaController extends AnchorPane {
    
    private BorderPane painelPrincipal;

    private Cliente cliente;
    private Venda venda;
    
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
        nomeText.disableProperty().bind(editarClienteCheckBox.selectedProperty().not());
        telefoneText.disableProperty().bind(editarClienteCheckBox.selectedProperty().not());
        cpfText.disableProperty().bind(editarClienteCheckBox.selectedProperty().not());
        rgText.disableProperty().bind(editarClienteCheckBox.selectedProperty().not());
        cidadeBox.disableProperty().bind(editarClienteCheckBox.selectedProperty().not());
        bairroBox.disableProperty().bind(editarClienteCheckBox.selectedProperty().not());
        ruaText.disableProperty().bind(editarClienteCheckBox.selectedProperty().not());
        numeroText.disableProperty().bind(editarClienteCheckBox.selectedProperty().not());
        
        dataDatePicker.disableProperty().bind(editarClienteCheckBox.selectedProperty().not());
        vendedorComboBox.disableProperty().bind(editarClienteCheckBox.selectedProperty().not());
        formarPagComboBox.disableProperty().bind(editarClienteCheckBox.selectedProperty().not());
        parcelasSpinner.disableProperty().bind(editarClienteCheckBox.selectedProperty().not());
    }
    
    private void adicionarPainelInterno(AnchorPane novaTela) {
        this.painelPrincipal.setCenter(novaTela);
    }
    
    @FXML
    private void cancelarOperacao() {
//        boolean vazio = Formatter.noEmpty(nomeText, telefoneText, cpfText, rgText, ruaText);
//        boolean carrinhoVazio = novaVenda.isEmpty();
//
//        if (cliente != null || vazio || !carrinhoVazio) {
//            Dialogo.Resposta resposta = Alerta.confirmar("Deseja cancelar esta Venda?");
//
//            if (resposta == Dialogo.Resposta.YES) {
//                TelaInicialController telaInicial = new TelaInicialController(painelPrincipal);
//                this.adicionarPainelInterno(telaInicial);
//            }
//        } else {
            TelaInicialController telaInicial = new TelaInicialController(painelPrincipal);
            this.adicionarPainelInterno(telaInicial);
//        }
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
        //this.cidadeComboBox.getSelectionModel().select(cidade);
        
        //sincronizarBancoDadosBairro(cidade);
        //bairroComboBox.setValue(bairro);
        
        this.ruaText.setText(endereco.getRua());
        this.numeroText.setText(endereco.getNumero());
    }
    
    private void adicionarDadosVenda(Venda venda) {
        Long data = venda.getData();
        Administrador vendedor = venda.getAdministrador();
        FormaPagamento formaPagamento = venda.getFormaPagamento();
        int parcelas = venda.getQuantidadeParcelas();
        
        this.dataDatePicker.setValue(LocalDate.ofEpochDay(data));
        this.vendedorComboBox.setValue(vendedor);
        this.formarPagComboBox.setValue(formaPagamento);
        this.parcelasSpinner.getValueFactory().setValue(parcelas);
    }
    
    public void setVenda(Venda venda) {
        this.venda = venda;
        //adicionarDadosVenda(venda);
        this.cliente = venda.getCliente();
        adicionarDadosCliente(cliente);
    }
}
