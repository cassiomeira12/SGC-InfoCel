/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import banco.ConexaoBanco;
import banco.ControleDAO;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.PieChart;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javax.swing.SwingWorker;
import model.Manutencao;
import model.Receita;
import model.Saida;
import model.Venda;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import util.DateUtils;
import util.alerta.Alerta;

/**
 * FXML Controller class
 *
 * @author cassio
 */
public class TelaRelatorioDiarioController extends AnchorPane {

    private BorderPane painelPrincipal;

    private LocalDate data;
    
    private List<Venda> listaVendas;
    private List<Manutencao> listaManutencoes;
    private List<Receita> listaReceitas;
    private List<Saida> listaSaidas;
    
    private PieChart.Data vendaSlice;
    private PieChart.Data manutencaoSlice;
    private PieChart.Data receitaSlice;
    private PieChart.Data saidaSlice;

    @FXML
    private PieChart graficoPie;
    @FXML
    private DatePicker dataDatePicker;
    @FXML
    private StackPane stackPane;
    private ProgressIndicator indicator = new ProgressIndicator();

    public TelaRelatorioDiarioController(BorderPane painelPrincipal) {
        this.painelPrincipal = painelPrincipal;

        try {
            FXMLLoader fxml = new FXMLLoader(getClass().getResource("/view/TelaRelatorioDiario.fxml"));
            fxml.setRoot(this);
            fxml.setController(this);
            fxml.load();
        } catch (IOException ex) {
            System.out.println("[ERRO] : Erro na tela Relatorio Diario");
            ex.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        this.data = LocalDate.now();
        this.dataDatePicker.setValue(data);

        vendaSlice = new PieChart.Data("VENDA", 0);
        manutencaoSlice = new PieChart.Data("MANUTENÇÃO", 0);
        receitaSlice = new PieChart.Data("RECEITA", 0);
        saidaSlice = new PieChart.Data("SAÍDA", 0);
        
        graficoPie.getData().addAll(vendaSlice, manutencaoSlice, receitaSlice, saidaSlice);
        graficoPie.setTitle("Relatório do dia " + DateUtils.formatDate(data));
        
        dataDatePicker.setOnAction((e) -> {
            data = dataDatePicker.getValue();
            sincronizarBancoDados(data);
        });
        
        stackPane.getChildren().add(indicator);
        indicator.setMaxSize(200, 200);
        
        sincronizarBancoDados(data);
    }

    private void sincronizarBancoDados(LocalDate data) {
        String dataInicio = DateUtils.formatDate(data.getYear(), data.getMonthValue(), data.getDayOfMonth());
        String dataFinal = DateUtils.formatDate(data.plusDays(1).getYear(), data.plusDays(1).getMonthValue(), data.plusDays(1).getDayOfMonth());
        
        graficoPie.setVisible(false);
        indicator.setVisible(true);
        //Metodo executado numa Thread separada
        SwingWorker<List, List> worker;
        worker = new SwingWorker<List, List>() {
            @Override
            protected List<Venda> doInBackground() throws Exception {
                listaVendas =  ControleDAO.getBanco().getVendaDAO().buscarPorIntervalo(dataInicio, dataFinal);
                listaManutencoes = ControleDAO.getBanco().getManutencaoDAO().buscarPorIntervalo(dataInicio, dataFinal);
                listaReceitas = ControleDAO.getBanco().getReceitaDAO().buscarPorIntervalo(dataInicio, dataFinal);
                listaSaidas = ControleDAO.getBanco().getSaidaDAO().buscarPorIntervalo(dataInicio, dataFinal);
                return null;
            }

            //Metodo chamado apos terminar a execucao numa Thread separada
            @Override
            protected void done() {
                super.done(); //To change body of generated methods, choose Tools | Templates.
                
                Float total = 0f;
                
                if (listaVendas != null) {
                    for (Venda v : listaVendas) {
                        total += v.getPrecoTotal();
                    }
                    vendaSlice.setPieValue(total.doubleValue());
                }
                
                if (listaManutencoes != null) {
                    total = 0f;
                    for (Manutencao m : listaManutencoes) {
                        total += m.getPreco();
                    }
                    manutencaoSlice.setPieValue(total.doubleValue());
                }
                
                if (listaReceitas != null) {
                    total = 0f;
                    for (Receita r : listaReceitas) {
                        total += r.getValor();
                    }
                    receitaSlice.setPieValue(total.doubleValue());
                }
                
                if (listaSaidas != null) {
                    total = 0f;
                    for (Saida s : listaSaidas) {
                        total += s.getValor();
                    }
                    saidaSlice.setPieValue(total.doubleValue());
                }

                graficoPie.setVisible(true);
                indicator.setVisible(false);
            }
        };

        worker.execute();
    }
    
    private void chamarAlerta(String mensagem) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Alerta.erro(mensagem);
            }
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
