/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import banco.ControleDAO;
import java.io.IOException;
import java.time.LocalDate;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import model.Cliente;
import model.Receita;
import util.alerta.Alerta;

/**
 * FXML Controller class
 *
 * @author cassio
 */
public class TelaAdicionarReceitaController extends AnchorPane {
    
    private BorderPane painelPrincipal;

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
    private TextArea decricaoText;
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
}
