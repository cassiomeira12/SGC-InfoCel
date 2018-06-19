/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import banco.ControleDAO;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import model.Administrador;
import model.Bairro;
import model.Cidade;
import model.Cliente;
import model.Endereco;
import model.Receita;
import org.apache.log4j.Logger;
import util.DateUtils;
import util.Formatter;
import util.alerta.Alerta;
import util.alerta.Dialogo;

/**
 * FXML Controller class
 *
 * @author cassio
 */
public class TelaReceitaController extends AnchorPane {

    private BorderPane painelPrincipal;

    private Cliente clienteSelecionado;
    
    private Receita receita;
    
    private boolean voltarTelaInicial = false;

    List<Cidade> listaCidades;
    List<Bairro> listaBairros;
    List<Administrador> listaAdministrador;

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
    private ComboBox<Administrador> vendedorComboBox;
    @FXML
    private TextField valorText;
    @FXML
    private TextArea descricaoText;
    @FXML
    private DatePicker dataDatePicker;

    @FXML
    private Button salvarButton;

    public TelaReceitaController(BorderPane painelPrincipal) {
        this.painelPrincipal = painelPrincipal;
        
        try {
            FXMLLoader fxml = new FXMLLoader(getClass().getResource("/view/TelaReceita.fxml"));
            fxml.setRoot(this);
            fxml.setController(this);
            fxml.load();
        } catch (IOException ex) {
            Logger.getLogger(getClass()).error(ex);
            ex.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        this.dataDatePicker.setValue(LocalDate.now());//Adicionando Data do dia atual

        Formatter.mascaraCPF(cpfText);//Formatador para CPF
        Formatter.mascaraRG(rgText);//Formatador para Rg
        Formatter.mascaraTelefone(telefoneText);//Formatador para Telefone
        Formatter.decimal(valorText);//Formatador para Dinheiro
        
        Formatter.toUpperCase(nomeText, adicionarCidadeText, ruaText, adicionarBairroText, numeroText, descricaoText);

//        this.editarClienteCheckBox.setVisible(false);//Ocultando componente
//        this.editarClienteCheckBox.setSelected(true);//Deixando o CheckBox selecionado
//        this.editarClienteCheckBox.setOnAction((e) -> {
//            editarClienteCheckBox.setVisible(false);
//            Platform.runLater(() -> nomeText.requestFocus());//Colocando o Foco
//        });

        salvarButton.disableProperty().bind(nomeText.textProperty().isEmpty().or(
                                            cpfText.textProperty().isEmpty().or(
                                            rgText.textProperty().isEmpty().or(
                                            cidadeComboBox.selectionModelProperty().isNull().or(
                                            bairroComboBox.selectionModelProperty().isNull().or(
                                            ruaText.textProperty().isEmpty().or(
                                            numeroText.textProperty().isEmpty().or(
                                            vendedorComboBox.selectionModelProperty().isNull().or(
                                            valorText.textProperty().isEmpty().or(
                                            descricaoText.textProperty().isEmpty()))))))))));


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
        
        this.dataDatePicker.setValue(LocalDate.now());//Adicionando Data do dia atual

        adicionarCidadeBox.setVisible(false);
        adicionarBairroBox.setVisible(false);

        cidadeComboBox.setOnAction((e) -> {
            Cidade cidade = cidadeComboBox.getValue();
            bairroComboBox.getSelectionModel().select(null);
            //sincronizarBancoDadosBairro(cidade);
        });

        //sincronizarBancoDadosAdministrador();
        //sincronizarBancoDadosCidade();
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
            TelaConsultarReceitasController tela = new TelaConsultarReceitasController(painelPrincipal);
            this.adicionarPainelInterno(tela);
        }
    }
    
    public void voltarTelaInicial(boolean telaInicial) {
        this.voltarTelaInicial = telaInicial;
    }
    
    public void setReceita(Receita receita) {
        this.receita = receita;
        adicionarDados(receita);
        this.clienteSelecionado = receita.getCliente();
        adicionarDadosCliente(clienteSelecionado);
    }
    
    public void adicionarDados(Receita receita) {
        Long data = receita.getData();
        Administrador vendedor = receita.getAdministrador();
        float valor = receita.getValor();
        String descricao = receita.getDescricao();
        
        this.dataDatePicker.setValue(DateUtils.createLocalDate(data));
        this.vendedorComboBox.setValue(vendedor);
        this.descricaoText.setText(descricao);
        this.valorText.setText(String.valueOf(valor));
    }
    
    public void adicionarDadosCliente(Cliente cliente) {
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

    @FXML
    private void salvarReceita() {
        float valor = Float.valueOf(valorText.getText());
        String descricao = descricaoText.getText();
        
        Dialogo.Resposta resposta = Alerta.confirmar("Deseja salvar as alterações ?");
        if (resposta == Dialogo.Resposta.YES) {
            
            receita.setValor(valor);
            receita.setDescricao(descricao);
            
            try {
                if (ControleDAO.getBanco().getReceitaDAO().editar(receita)) {
                    Alerta.info("Receita editada com sucesso!");
                    cancelarOperacao();
                } else {
                    Alerta.erro("Erro ao alterar a Receita");
                }
            } catch (SQLException ex) {
                Logger.getLogger(getClass()).error(ex);
                Alerta.erro("Erro ao alterar a Receita");
                ex.printStackTrace();
            }
        }
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
