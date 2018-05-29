/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

/**
 * FXML Controller class
 *
 * @author cassio
 */
public class TelaRelatorioMensalController extends AnchorPane {
    
    private BorderPane painelPrincipal;

    @FXML
    private BarChart<String, Number> graficoBar;
    
  
    public TelaRelatorioMensalController(BorderPane painelPrincipal) {
        this.painelPrincipal = painelPrincipal;
        
        try {
            FXMLLoader fxml = new FXMLLoader(getClass().getResource("/view/TelaRelatorioMensal.fxml"));
            fxml.setRoot(this);
            fxml.setController(this);
            fxml.load();
        } catch (IOException ex) {
            System.out.println("[ERRO] : Erro na tela Relatorio Mensal");
            System.out.println(ex.toString());
        }
    }

    @FXML
    public void initialize() {
        // TODO
        
       CategoryAxis xAxis = new CategoryAxis();
       xAxis.setLabel("Programming Language");
 
       NumberAxis yAxis = new NumberAxis();
       yAxis.setLabel("Percent");
       
       
       
       BarChart<String, Number> barChart = new BarChart<String, Number>(xAxis, yAxis);
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
