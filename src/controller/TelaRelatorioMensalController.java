/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Side;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
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
    private BorderPane painel;
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
        String mes = "Junho";
        double ultimoDia = 31;
        
        // TODO
        final NumberAxis xAxis = new NumberAxis(1, ultimoDia, 1);
        final NumberAxis yAxis = new NumberAxis();
        
        xAxis.setLabel("Dias do Mês");
        yAxis.setLabel("Valores");
        
        
        final AreaChart<Number, Number> areaChart = new AreaChart<Number, Number>(xAxis, yAxis);
        areaChart.setTitle("Relatório do Mês de " + mes);

        
        areaChart.setLegendSide(Side.LEFT);

        // Series data of 2014
        XYChart.Series<Number, Number> vendas = new XYChart.Series<Number, Number>();

        vendas.setName("Vendas");
        
        //XYChart.Series series = new XYChart.Series();
        //series.getData().add(new XYChart.Data("January", 100));
        
        for (int dia=1; dia<=31; dia++) {
            vendas.getData().add(new XYChart.Data(dia, 0));
        }
        
//        vendas.getData().add(new XYChart.Data(1, 400));
//        vendas.getData().add(new XYChart.Data(2, 1000));
//        vendas.getData().add(new XYChart.Data(3, 1500));
//        vendas.getData().add(new XYChart.Data(4, 800));
//        vendas.getData().add(new XYChart.Data(5, 500));
//        vendas.getData().add(new XYChart.Data(6, 1800));
//        vendas.getData().add(new XYChart.Data(7, 1500));
//        vendas.getData().add(new XYChart.Data(8, 1300));
//        vendas.getData().add(new XYChart.Data(9, 400));
//        vendas.getData().add(new XYChart.Data(10, 1000));
//        vendas.getData().add(new XYChart.Data(11, 400));
//        vendas.getData().add(new XYChart.Data(12, 1000));
//        vendas.getData().add(new XYChart.Data(13, 1500));
//        vendas.getData().add(new XYChart.Data(14, 800));
//        vendas.getData().add(new XYChart.Data(15, 500));
//        vendas.getData().add(new XYChart.Data(16, 1800));
//        vendas.getData().add(new XYChart.Data(17, 1500));
//        vendas.getData().add(new XYChart.Data(18, 1300));
//        vendas.getData().add(new XYChart.Data(19, 400));
//        vendas.getData().add(new XYChart.Data(20, 1000));
//        vendas.getData().add(new XYChart.Data(21, 400));
//        vendas.getData().add(new XYChart.Data(22, 1000));
//        vendas.getData().add(new XYChart.Data(23, 1500));
//        vendas.getData().add(new XYChart.Data(24, 800));
//        vendas.getData().add(new XYChart.Data(25, 500));
//        vendas.getData().add(new XYChart.Data(26, 1800));
//        vendas.getData().add(new XYChart.Data(27, 1500));
//        vendas.getData().add(new XYChart.Data(28, 1300));
//        vendas.getData().add(new XYChart.Data(29, 400));
//        vendas.getData().add(new XYChart.Data(30, 1500));
//        vendas.getData().add(new XYChart.Data(31, 1300));
        
        areaChart.getData().addAll(vendas);
        
        painel.setCenter(areaChart);

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
