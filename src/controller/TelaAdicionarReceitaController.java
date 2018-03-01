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
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Cliente;
import model.Receita;
import util.Formatter;
import util.alerta.Alerta;

/**
 * FXML Controller class
 *
 * @author cassio
 */
public class TelaAdicionarReceitaController extends AnchorPane {
    
    private BorderPane painelPrincipal;

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
    private TextField valorText;
    @FXML
    private TextArea descricaoText;
    @FXML
    private DatePicker dataDatePicker;
    
    private Cliente cliente;
            
    public TelaAdicionarReceitaController(BorderPane painelPrincipal) {
        this.painelPrincipal = painelPrincipal;
        
        try {
            FXMLLoader fxml = new FXMLLoader(getClass().getResource("/view/TelaAdicionarReceita.fxml"));
            fxml.setRoot(this);
            fxml.setController(this);
            fxml.load();
        } catch (IOException ex) {
            System.out.println("[ERRO] : Erro na tela Adicionar Receita");
            System.out.println(ex.toString());
        }
    }

    @FXML
    public void initialize() {
        this.dataDatePicker.setValue(LocalDate.now());//Adicionando Data do dia atual
        
        Formatter.mascaraCPF(cpfText);//Formatador para CPF
        Formatter.mascaraRG(rgText);//Formatador para Rg
        Formatter.mascaraTelefone(telefoneText);//Formatador para Telefone
        Formatter.decimal(valorText);//Formatador para Dinheiro
        
        Formatter.toUpperCase(nomeText, cidadeText, enderecoText, descricaoText);
        
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
    private void salvarReceita(){
//        if(cliente == null){
//        cliente = new Cliente(null, nomeText.getText(), enderecoText.getText(), cpfText.getText(), rgText.getText(), telefoneText.getText(), cidadeText.getText(), System.currentTimeMillis(), 1);
//        }
//        Receita receita = new Receita(null, cliente, LoginController.admLogado, decricaoText.getText(), dataDataPicker.getValue().toEpochDay(), Float.parseFloat(valorText.getText()));
//        if(ControleDAO.getBanco().getReceitaDAO().inserir(receita)==null){
//            Alerta.erro("não foi possivel inserir!");
//        }
//        else{
//            Alerta.info("receita inserida com sucesso!");
//            cliente = null;
//            nomeText.setText("");
//            telefoneText.setText("");
//            cpfText.setText("");
//            rgText.setText("");
//            cidadeText.setText("");
//            enderecoText.setText("");
//            valorText.setText("");
//            decricaoText.setText("");
//            telefoneText.setText("");
//            cpfText.setText("");
//            rgText.setText("");
//            cidadeText.setText("");
//            enderecoText.setText("");
//            valorText.setText("");
//            decricaoText.setText("");
//            
//        }
    
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
}
