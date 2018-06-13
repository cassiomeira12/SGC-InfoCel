/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import banco.ControleDAO;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.List;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Side;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javax.swing.SwingWorker;
import model.*;
import util.DateUtils;

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

    private AreaChart<Number, Number> areaChart;

    private NumberAxis eixoX;
    private NumberAxis eixoY;

    private XYChart.Series<Number, Number> vendas;
    private XYChart.Series<Number, Number> manutencoes;
    private XYChart.Series<Number, Number> receitas;
    private XYChart.Series<Number, Number> saidas;

    private String mes;
    private double ultimoDia;

    @FXML
    private Label vendasLabel;
    @FXML
    private Label manutencoesLabel;
    @FXML
    private Label receitasLabel;
    @FXML
    private Label saidasLabel;
    @FXML
    private Label totalReceitasLabel;
    @FXML
    private Label totalSaidasLabel;

    @FXML
    private StackPane stackPane;
    private ProgressIndicator indicator = new ProgressIndicator();

    @FXML
    private BorderPane painel;
    @FXML
    private DatePicker dataDatePicker;

    public TelaRelatorioMensalController(BorderPane painelPrincipal) {
        this.painelPrincipal = painelPrincipal;

        try {
            FXMLLoader fxml = new FXMLLoader(getClass().getResource("/view/TelaRelatorioMensal.fxml"));
            fxml.setRoot(this);
            fxml.setController(this);
            fxml.load();
        } catch (IOException ex) {
            System.out.println("[ERRO] : Erro na tela Relatorio Mensal");
            ex.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        this.data = LocalDate.now();
        this.dataDatePicker.setValue(data);

        mes = DateUtils.getMes(data);
        //ultimoDia = data.lengthOfMonth();
        ultimoDia = 31;

        eixoX = new NumberAxis(1, ultimoDia, 1);
        eixoY = new NumberAxis();

        areaChart = new AreaChart<Number, Number>(eixoX, eixoY);
        areaChart.setTitle("Relatório do Mês de " + mes);
        areaChart.setLegendSide(Side.BOTTOM);
        
        stackPane.getChildren().add(areaChart);

        eixoX.setLabel("Dias do Mês");
        eixoY.setLabel("Valores R$");

        vendas = new XYChart.Series<Number, Number>();
        manutencoes = new XYChart.Series<Number, Number>();
        receitas = new XYChart.Series<Number, Number>();
        saidas = new XYChart.Series<Number, Number>();
        
        vendas.setName("Vendas");
        manutencoes.setName("Manutenções");
        receitas.setName("Receitas");
        saidas.setName("Saídas");

        for (int dia = 1; dia <= ultimoDia; dia++) {
            vendas.getData().add(new XYChart.Data(dia, 0));
            manutencoes.getData().add(new XYChart.Data(dia, 0));
            receitas.getData().add(new XYChart.Data(dia, 0));
            saidas.getData().add(new XYChart.Data(dia, 0));
        }
        
        areaChart.getData().addAll(vendas, manutencoes, receitas, saidas);
        
        indicator.setMaxSize(200, 200);
        stackPane.getChildren().add(indicator);

        dataDatePicker.setOnAction((e) -> {
            //areaChart.setVisible(false);
            //setVisibleItens(false);
            
            data = dataDatePicker.getValue();

            mes = DateUtils.getMes(data);
            //ultimoDia = data.lengthOfMonth();
            ultimoDia = 31;

//            eixoX = new NumberAxis(1, ultimoDia, 1);
//            eixoY = new NumberAxis();
//            areaChart = new AreaChart<>(eixoX, eixoY);
            
            areaChart.setTitle("Relatório do Mês de " + mes);
            areaChart.setLegendSide(Side.BOTTOM);
            
            //areaChart.getData().clear();
            //areaChart.setData(ObservableList<Number, Number>(eixoX, eixoY));

            eixoX.setLabel("Dias do Mês");
            eixoY.setLabel("Valores R$");

//            vendas.getData().clear();
//            manutencoes.getData().clear();
//            receitas.getData().clear();
//            saidas.getData().clear();

//            for (int dia = 1; dia <= ultimoDia; dia++) {
//                vendas.getData().add(new XYChart.Data(dia, 0));
//                manutencoes.getData().add(new XYChart.Data(dia, 0));
//                receitas.getData().add(new XYChart.Data(dia, 0));
//                saidas.getData().add(new XYChart.Data(dia, 0));
//            }
//            
//            stackPane.getChildren().remove(areaChart);
//            
            sincronizarBancoDados(data);
        });

        sincronizarBancoDados(data);
    }

    private void sincronizarBancoDados(LocalDate data) {

        //-------------------------------------------
        listaSaida = null;
        listaManutencao = null;
        listaReceita = null;
        listaVenda = null;

        //areaChart.setVisible(false);
        //setVisibleItens(false);

        String dataInicio = DateUtils.formatDate(data.getYear(), data.getMonthValue(), 1);
        String dataFinal = DateUtils.formatDate(data.getYear(), data.getMonthValue(), data.lengthOfMonth());

        //Metodo executado numa Thread separada
        SwingWorker<List, List> worker = new SwingWorker<List, List>() {
            @Override
            protected List<Venda> doInBackground() throws Exception {
                listaVenda = ControleDAO.getBanco().getVendaDAO().buscarPorIntervalo(dataInicio, dataFinal);
                listaManutencao = ControleDAO.getBanco().getManutencaoDAO().buscarPorIntervalo(dataInicio, dataFinal);
                listaReceita = ControleDAO.getBanco().getReceitaDAO().buscarPorIntervalo(dataInicio, dataFinal);
                listaSaida = ControleDAO.getBanco().getSaidaDAO().buscarPorIntervalo(dataInicio, dataFinal);
                return null;
            }

            //Metodo chamado apos terminar a execucao numa Thread separada
            @Override
            protected void done() {
                super.done();

                Float total = 0f;
                Float totalReceitas = 0f;
                Float totalSaidas = 0f;

                if (listaVenda != null) {
                    total = 0f;
                    for (int i = 0; i < ultimoDia; i++) {
                        for (Venda venda : listaVenda) {
                            int dia = Integer.valueOf(venda.getDataEditada().substring(0, 2));
                            Number number = venda.getPrecoTotal();
                            if (i + 1 == dia) {
                                total += number.floatValue();
                                System.out.println("Dia " + dia + " Valor " + total + " Id " + venda.getId());
                                vendas.getData().get(i).setYValue(number);
                            } else {
                                vendas.getData().get(i).setYValue(0);
                            }
                        }
                    }
                    totalReceitas += total;
                    atualizarLabel(vendasLabel, total);
                }

//                if (listaManutencao != null) {
//                    total = 0f;
//                    for (int i = 0; i < ultimoDia; i++) {
//                        for (Manutencao manutencao : listaManutencao) {
//                            int dia = Integer.valueOf(manutencao.getDataEditada().substring(0, 2));
//                            Number number = manutencao.getPreco();
//                            if (manutencao.isFinalizado()) {
//                                total += number.floatValue();
//                                if (i + 1 == dia) {
//                                    manutencoes.getData().get(i).setYValue(number);
//                                } else {
//                                    manutencoes.getData().get(i).setYValue(0);
//                                }
//                            } else {
//                                manutencoes.getData().get(i).setYValue(0);
//                            }
//                        }
//                    }
//                    totalReceitas += total;
//                    atualizarLabel(manutencoesLabel, total);
//                }
//
//                if (listaReceita != null) {
//                    total = 0f;
//                    for (int i = 0; i < ultimoDia; i++) {
//                        for (Receita receita : listaReceita) {
//                            int dia = Integer.valueOf(receita.getDataEditada().substring(0, 2));
//                            Number number = receita.getValor();
//                            total += number.floatValue();
//                            if (i + 1 == dia) {
//                                receitas.getData().get(i).setYValue(number);
//                            } else {
//                                receitas.getData().get(i).setYValue(0);
//                            }
//                        }
//                    }
//                    totalReceitas += total;
//                    atualizarLabel(manutencoesLabel, total);
//                }
//
//                if (listaSaida != null) {
//                    total = 0f;
//                    for (int i = 0; i < ultimoDia; i++) {
//                        for (Saida saida : listaSaida) {
//                            int dia = Integer.valueOf(saida.getDataEditada().substring(0, 2));
//                            Number number = saida.getValor();
//                            total += number.floatValue();
//                            if (i + 1 == dia) {
//                                saidas.getData().get(i).setYValue(number);
//                            } else {
//                                saidas.getData().get(i).setYValue(0);
//                            }
//                        }
//                    }
//                    totalSaidas += total;
//                    atualizarLabel(saidasLabel, total);
//                }

                atualizarLabel(totalReceitasLabel, totalReceitas);
                atualizarLabel(totalSaidasLabel, totalSaidas);

                try {
                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                } finally {
                    Platform.runLater(() -> {
                        //areaChart.getData().addAll(vendas, manutencoes, receitas, saidas);
                        //stackPane.getChildren().add(areaChart);
                        areaChart.setVisible(true);
                    });
                    setVisibleItens(true);
                }
            }
        };

        worker.execute();
    }

    private void atualizarLabel(Label label, Float valor) {
        Platform.runLater(() -> {
            if (valor == 0) {
                label.setText("0.0");
            } else {
                label.setText(new DecimalFormat("#,###.00").format(valor.doubleValue()));
            }
        });
    }

    private void setVisibleItens(boolean visible) {
        Platform.runLater(() -> {
            indicator.setVisible(!visible);

            vendasLabel.setVisible(visible);
            manutencoesLabel.setVisible(visible);
            receitasLabel.setVisible(visible);
            saidasLabel.setVisible(visible);
            totalReceitasLabel.setVisible(visible);
            totalSaidasLabel.setVisible(visible);

        });
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
