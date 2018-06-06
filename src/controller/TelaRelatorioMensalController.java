/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import banco.ControleDAO;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Side;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javax.swing.SwingWorker;
import model.*;
import util.DateUtils;
import util.alerta.Alerta;

/**
 * FXML Controller class
 *
 * @author cassio
 */
public class TelaRelatorioMensalController extends AnchorPane {

    private BorderPane painelPrincipal;
    
    private LocalDate data;
    
    private List<Manutencao> listaManutencao;
    private List<Receita> listaReceita;
    private List<Saida> listaSaida;
    private List<Venda> listaVenda;
    
    private XYChart.Series<Number, Number> vendas;
    private XYChart.Series<Number, Number> manutencoes;
    private XYChart.Series<Number, Number> receitas;
    private XYChart.Series<Number, Number> saidas;

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
        this.data = LocalDate.now();
        
        String mes = DateUtils.getMes(data);
        double ultimoDia = data.lengthOfMonth();
        
        // TODO
        final NumberAxis xAxis = new NumberAxis(1, ultimoDia, 1);
        final NumberAxis yAxis = new NumberAxis();
        
        xAxis.setLabel("Dias do Mês");
        yAxis.setLabel("Valores");
        
        
        final AreaChart<Number, Number> areaChart = new AreaChart<Number, Number>(xAxis, yAxis);
        areaChart.setTitle("Relatório do Mês de " + mes);

        
        areaChart.setLegendSide(Side.LEFT);

        vendas = new XYChart.Series<Number, Number>();
        manutencoes = new XYChart.Series<Number, Number>();
        receitas = new XYChart.Series<Number, Number>();
        saidas = new XYChart.Series<Number, Number>();

        vendas.setName("Vendas");
        manutencoes.setName("Manutenções");
        receitas.setName("Receitas");
        saidas.setName("Saídas");
        
        for (int dia=1; dia<=ultimoDia; dia++) {
            vendas.getData().add(new XYChart.Data(dia, 0));
            manutencoes.getData().add(new XYChart.Data(dia, 0));
            receitas.getData().add(new XYChart.Data(dia, 0));
            saidas.getData().add(new XYChart.Data(dia, 0));
        }
        
        areaChart.getData().addAll(vendas, manutencoes, receitas, saidas);
        
        painel.setCenter(areaChart);

        sincronizarBancoDadosVenda(data, vendas);
        sincronizarBancoDadosManutencao(data, manutencoes);
        sincronizarBancoDadosReceita(data, receitas);
        sincronizarBancoDadosSaida(data, saidas);

    }

    private void adicionarPainelInterno(AnchorPane novaTela) {
        this.painelPrincipal.setCenter(novaTela);
    }

    @FXML
    private void cancelarOperacao() {
        TelaInicialController telaInicial = new TelaInicialController(painelPrincipal);
        this.adicionarPainelInterno(telaInicial);
    }
    
    private void sincronizarBancoDadosVenda(LocalDate data, XYChart.Series<Number, Number> chart) {
        String dataInicio = DateUtils.formatDate(data.getYear(), data.getMonthValue(), 1);
        String dataFinal = DateUtils.formatDate(data.getYear(), data.getMonthValue(), data.lengthOfMonth());
        int ultimoDia = data.lengthOfMonth();
        
        //Metodo executado numa Thread separada
        SwingWorker<List, List> worker = new SwingWorker<List, List>() {
            @Override
            protected List<Venda> doInBackground() throws Exception {
                return ControleDAO.getBanco().getVendaDAO().buscarPorIntervalo(dataInicio, dataFinal);
            }
            
            //Metodo chamado apos terminar a execucao numa Thread separada
            @Override
            protected void done() {
                super.done();
                
                List<Venda> lista = null;
                
                try {
                    lista = this.get();
                } catch (InterruptedException | ExecutionException ex) {
                    Alerta.erro("Erro do consultar Banco de Dados");
                    ex.printStackTrace();
                }
                
                if (lista != null) {
                    for (int i=0; i<=ultimoDia; i++) {
                        for (Venda venda : lista) {
                            int dia = Integer.valueOf(venda.getDataEditada().substring(0, 2));
                            if (i+1 == dia) {
                                Number number = venda.getPrecoTotal();
                                chart.getData().get(i).setYValue(number);
                            }
                        }
                    }
                }
            }
        };
        
        worker.execute();
    }
    
    private void sincronizarBancoDadosManutencao(LocalDate data, XYChart.Series<Number, Number> chart) {
        String dataInicio = DateUtils.formatDate(data.getYear(), data.getMonthValue(), 1);
        String dataFinal = DateUtils.formatDate(data.getYear(), data.getMonthValue(), data.lengthOfMonth());
        int ultimoDia = data.lengthOfMonth();
        
        //Metodo executado numa Thread separada
        SwingWorker<List, List> worker = new SwingWorker<List, List>() {
            @Override
            protected List<Manutencao> doInBackground() throws Exception {
                //return ControleDAO.getBanco().getManutencaoDAO().listar();
                return ControleDAO.getBanco().getManutencaoDAO().buscarPorIntervalo(dataInicio, dataFinal);
            }
            
            //Metodo chamado apos terminar a execucao numa Thread separada
            @Override
            protected void done() {
                super.done();
                
                List<Manutencao> lista = null;
                
                try {
                    lista = this.get();
                } catch (InterruptedException | ExecutionException ex) {
                    Alerta.erro("Erro do consultar Banco de Dados");
                    ex.printStackTrace();
                }
                
                if (lista != null) {
                    for (int i=0; i<=ultimoDia; i++) {
                        for (Manutencao manutencao : lista) {
                            int dia = Integer.valueOf(manutencao.getDataEditada().substring(0, 2));
                            if (i+1 == dia) {
                                Number number = manutencao.getPreco();
                                chart.getData().get(i).setYValue(number);
                            }
                        }
                    }
                }
            }
        };
        
        worker.execute();
    }
    
    private void sincronizarBancoDadosReceita(LocalDate data, XYChart.Series<Number, Number> chart) {
        String dataInicio = DateUtils.formatDate(data.getYear(), data.getMonthValue(), 1);
        String dataFinal = DateUtils.formatDate(data.getYear(), data.getMonthValue(), data.lengthOfMonth());
        int ultimoDia = data.lengthOfMonth();
        
        //Metodo executado numa Thread separada
        SwingWorker<List, List> worker = new SwingWorker<List, List>() {
            @Override
            protected List<Receita> doInBackground() throws Exception {
                return ControleDAO.getBanco().getReceitaDAO().listar();
                //return ControleDAO.getBanco().getReceitaDAO().buscarPorIntervalo(dataInicio, dataFinal);
            }
            
            //Metodo chamado apos terminar a execucao numa Thread separada
            @Override
            protected void done() {
                super.done();
                
                List<Receita> lista = null;
                
                try {
                    lista = this.get();
                } catch (InterruptedException | ExecutionException ex) {
                    Alerta.erro("Erro do consultar Banco de Dados");
                    ex.printStackTrace();
                }

                if (lista != null) {
                    for (int i=0; i<=ultimoDia; i++) {
                        for (Receita receita : lista) {
                            int dia = Integer.valueOf(receita.getDataEditada().substring(0, 2));
                            if (i+1 == dia) {
                                Number number = receita.getValor();
                                chart.getData().get(i).setYValue(number);
                            }
                        }
                    }
                }
            }
        };
        
        worker.execute(); 
    }
    
    private void sincronizarBancoDadosSaida(LocalDate data, XYChart.Series<Number, Number> chart) {
        String dataInicio = DateUtils.formatDate(data.getYear(), data.getMonthValue(), 1);
        String dataFinal = DateUtils.formatDate(data.getYear(), data.getMonthValue(), data.lengthOfMonth());
        int ultimoDia = data.lengthOfMonth();
        
        //Metodo executado numa Thread separada
        SwingWorker<List, List> worker = new SwingWorker<List, List>() {
            @Override
            protected List<Saida> doInBackground() throws Exception {
                return ControleDAO.getBanco().getSaidaDAO().listar();
                //return ControleDAO.getBanco().getSaidaDAO().buscarPorIntervalo(dataInicio, dataFinal);
            }
            
            //Metodo chamado apos terminar a execucao numa Thread separada
            @Override
            protected void done() {
                super.done();
                
                List<Saida> lista = null;
                
                try {
                    lista = this.get();
                } catch (InterruptedException | ExecutionException ex) {
                    Alerta.erro("Erro do consultar Banco de Dados");
                    ex.printStackTrace();
                }
                
                if (lista != null) {
                    for (int i=0; i<=ultimoDia; i++) {
                        for (Saida saida : lista) {
                            int dia = Integer.valueOf(saida.getDataEditada().substring(0, 2));
                            if (i+1 == dia) {
                                Number number = saida.getValor();
                                chart.getData().get(i).setYValue(number);
                            }
                        }
                    }
                }
            }
        };
        
        worker.execute();
    }
}
