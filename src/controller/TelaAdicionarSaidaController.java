/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.time.LocalDate;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import util.Formatter;

/**
 * FXML Controller class
 *
 * @author cassio
 */
public class TelaAdicionarSaidaController extends AnchorPane {
    
    private BorderPane painelPrincipal;
        
    @FXML
    private DatePicker dataDatePicker;
    @FXML
    private TextField descricaoArea;

    public TelaAdicionarSaidaController(BorderPane painelPrincipal) {
        this.painelPrincipal = painelPrincipal;
        
        try {
            FXMLLoader fxml = new FXMLLoader(getClass().getResource("/view/TelaAdicionarSaida.fxml"));
            fxml.setRoot(this);
            fxml.setController(this);
            fxml.load();
        } catch (IOException ex) {
            System.out.println("[ERRO] : Erro na tela Adicionar Saida");
            System.out.println(ex.toString());
        }
    }

    @FXML
    public void initialize() {
        
        Formatter.toUpperCase(descricaoArea);
        
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
    
}
