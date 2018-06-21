package controller;

import java.io.IOException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import model.Administrador;
import model.Bairro;
import model.Cidade;
import model.Cliente;
import model.Endereco;
import model.FormaPagamento;
import model.Manutencao;
import org.apache.log4j.Logger;
import util.DateUtils;

public class TelaManutencaoController extends AnchorPane {
    
    private BorderPane painelPrincipal;
    private Cliente cliente;
    private Manutencao manutencao;
    
    private boolean voltarTelaInicial = false;
    
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
    private DatePicker entregaDatePicker;
    @FXML
    private TextField precoText;
    @FXML
    private ComboBox<Administrador> vendedorComboBox;
    @FXML
    private ComboBox<FormaPagamento> formaPagamentoComboBox;
    @FXML
    private Spinner<Integer> parcelasSpinner;
    
    @FXML
    private TextArea descricaoArea;
    @FXML
    private TextField marcaText;
    @FXML
    private Rectangle corRectangle;
    @FXML
    private ColorPicker corColorPicker;
    @FXML
    private TextField modeloText;
    @FXML
    private TextField imeiText;
    @FXML
    private ComboBox<String> estadoComboBox;
    
    
    
    public TelaManutencaoController(BorderPane painelPrincipal) {
        this.painelPrincipal = painelPrincipal;

        try {
            FXMLLoader fxml = new FXMLLoader(getClass().getResource("/view/TelaManutencao.fxml"));
            fxml.setRoot(this);
            fxml.setController(this);
            fxml.load();
        } catch (IOException ex) {
            //Logger.getLogger(getClass()).error(ex);
            ex.printStackTrace();
        }
    }
    
    @FXML
    public void initialize() {
        boolean desabilitar = true;
        
        this.nomeText.setDisable(desabilitar);
        this.telefoneText.setDisable(desabilitar);
        this.cpfText.setDisable(desabilitar);
        this.rgText.setDisable(desabilitar);
        this.cidadeBox.setDisable(desabilitar);
        this.bairroBox.setDisable(desabilitar);
        this.ruaText.setDisable(desabilitar);
        this.numeroText.setDisable(desabilitar);
        this.vendedorComboBox.setDisable(desabilitar);
        this.dataDatePicker.setDisable(desabilitar);
        
        
        this.estadoComboBox.getItems().add("ABERTO");
        this.estadoComboBox.getItems().add("FINALIZADO");
        
        
        corColorPicker.setOnAction((e) -> {
            corRectangle.setFill(corColorPicker.getValue());
        });
        
        Platform.runLater(() -> descricaoArea.requestFocus() );
    }
    
    private void adicionarPainelInterno(AnchorPane novaTela) {
        this.painelPrincipal.setCenter(novaTela);
    }
    
    @FXML
    private void cancelarOperacao() {
        if (voltarTelaInicial) {
            TelaInicialController tela = new TelaInicialController(painelPrincipal);
            this.adicionarPainelInterno(tela);
        } else {
            TelaConsultarManutencoesController tela = new TelaConsultarManutencoesController(painelPrincipal);
            this.adicionarPainelInterno(tela);
        }
    }
    
    public void voltarTelaInicial(boolean telaInicial) {
        this.voltarTelaInicial = telaInicial;
    }

    @FXML
    private void finalizar() {
        
    }
    
    public void setManutencao(Manutencao manutencao) {
        this.manutencao = manutencao;
        this.cliente = manutencao.getCliente();
        adicionarDadosCliente(cliente);
        adicionarDadosManutencao(manutencao);
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

    private void adicionarDadosManutencao(Manutencao manutencao) {
        Long dataCadastro = manutencao.getDataCadastro();
        Long dataEntrega = manutencao.getDataPrevisaoEntrega();
        Administrador vendedor = manutencao.getAdministrador();
        FormaPagamento formaPagamento = manutencao.getFormaPagamento();
        
        int parcelas = manutencao.getQuantidadeParcelas();
        
        this.dataDatePicker.setValue(DateUtils.createLocalDate(dataCadastro));
        this.entregaDatePicker.setValue(DateUtils.createLocalDate(dataEntrega));
        this.vendedorComboBox.setValue(vendedor);
        this.formaPagamentoComboBox.setValue(formaPagamento);
        this.precoText.setText(String.valueOf(manutencao.getPreco()));
        
        SpinnerValueFactory<Integer> valores = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 5, parcelas);
        parcelasSpinner.setValueFactory(valores);
        
        this.descricaoArea.setText(manutencao.getDescricao());
        this.marcaText.setText(manutencao.getMarca());
        this.modeloText.setText(manutencao.getModelo());
        this.imeiText.setText(manutencao.getImei());
        this.corColorPicker.setValue(Color.valueOf(manutencao.getCor()));
        this.corRectangle.setFill(corColorPicker.getValue());
        
        this.estadoComboBox.setValue(manutencao.isFinalizado() ? "FINALIZADO" : "ABERTO");
        
    }
    
}
