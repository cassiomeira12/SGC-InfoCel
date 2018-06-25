package controller;

import banco.ControleDAO;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.logging.Level;
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
import relatorio.DescricaoManutencao;
import util.DateUtils;
import util.Formatter;
import util.alerta.Alerta;
import util.alerta.Dialogo;

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
        
        Formatter.toUpperCase(nomeText, adicionarCidadeText, adicionarBairroText, marcaText, modeloText, descricaoArea, ruaText, numeroText);
        Formatter.mascaraCPF(cpfText);//Formatador para CPF
        Formatter.mascaraRG(rgText);//Formatador para Rg
        Formatter.mascaraTelefone(telefoneText);//Formatador para Telefone
        Formatter.decimal(precoText);//Formatador para Dinheiro
        this.imeiText.setTextFormatter(Formatter.NUMERICO());//Formatador Numerico
        Formatter.maxField(imeiText, 15);
        
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
        Dialogo.Resposta resposta = Alerta.confirmar("Deseja editar esta Manutenção ?");
        // se quer cadastrar uma nova manutencao
        if (resposta == Dialogo.Resposta.YES) {
            //Administrador vendedor = vendedorComboBox.getValue();
            boolean finalizado = false;
            // olha os estado do aparelho, aberto ou finalizado
            switch (estadoComboBox.getValue()) {
                case "ABERTO":
                   finalizado = false;
                    break;
                case "FINALIZADO":
                    finalizado = true;
                     break;
            }

            String descricao = descricaoArea.getText();
            float preco = Float.parseFloat(precoText.getText());

            LocalDate date = entregaDatePicker.getValue();
            LocalDate hoje = LocalDate.now();
            Long dataPrevisao = null;
            if (entregaDatePicker.getValue() != null) {
                dataPrevisao = DateUtils.getLong(entregaDatePicker.getValue());
            
                if (date.isEqual(hoje)) {
                    dataPrevisao = System.currentTimeMillis();
                }
            }

            String marca = marcaText.getText();
            String cor = corColorPicker.getValue().toString();
            String modelo = modeloText.getText();
            String imei = imeiText.getText();
            FormaPagamento pagamento = formaPagamentoComboBox.getValue();
            int parcelas = parcelasSpinner.getValue();
            
            manutencao.setPreco(preco);
            manutencao.setFormaPagamento(pagamento);
            manutencao.setQuantidadeParcelas(parcelas);
            manutencao.setDataPrevisaoEntrega(dataPrevisao);
            manutencao.setDataEntrega(dataPrevisao);
            manutencao.setDescricao(descricao);
            manutencao.setMarca(marca);
            manutencao.setModelo(modelo);
            manutencao.setImei(imei);
            manutencao.setCor(cor);
            manutencao.setFinalizado(finalizado);
            
            try {
                if(ControleDAO.getBanco().getManutencaoDAO().editar(manutencao)) {
                    Dialogo.Resposta abrirPDF = Alerta.confirmar("Manutenção editada com sucesso!\n"
                                                                        + "Deseja abrir o Comprovante da Manutenção ?");
                    
                    // gerar descricao manutencao pdf
                    DescricaoManutencao relatorio = new DescricaoManutencao(manutencao.getId());
                    if (abrirPDF == Dialogo.Resposta.YES) {
                        relatorio.setMostrar(true);
                    }
                    relatorio.start();
                    
                    cancelarOperacao();
                }
            } catch (SQLException ex) {
                Logger.getLogger(getClass()).error(ex);
                ex.printStackTrace();
                Alerta.erro("Erro ao editar Manutenção !");
            }
            
                
        }
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
        System.out.println(dataEntrega);
        this.dataDatePicker.setValue(DateUtils.createLocalDate(dataCadastro));
        if (dataEntrega != 0) {
            this.entregaDatePicker.setValue(DateUtils.createLocalDate(dataEntrega));
        }
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
