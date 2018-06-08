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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
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

    @FXML
    private PieChart graficoPie;
    @FXML
    private DatePicker dataDatePicker;

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

        PieChart.Data vendaSlice = new PieChart.Data("VENDA", 0);
        PieChart.Data manutencaoSlice = new PieChart.Data("MANUTENÇÃO", 0);
        PieChart.Data receitaSlice = new PieChart.Data("RECEITA", 0);
        PieChart.Data saidaSlice = new PieChart.Data("SAÍDA", 0);
        
        graficoPie.getData().addAll(vendaSlice, manutencaoSlice, receitaSlice, saidaSlice);
        graficoPie.setTitle("Relatório do dia " + DateUtils.formatDate(data));
        
        dataDatePicker.setOnAction((e) -> {
            data = dataDatePicker.getValue();
            sincronizarBancoDadosVendas(vendaSlice, data);
            sincronizarBancoDadosManutencoes(manutencaoSlice, data);
            sincronizarBancoDadosReceita(receitaSlice, data);
            sincronizarBancoDadosSaida(saidaSlice, data);
        });
        
//        sincronizarBancoDadosVendas(vendaSlice, data);
//        sincronizarBancoDadosManutencoes(manutencaoSlice, data);
//        sincronizarBancoDadosReceita(receitaSlice, data);
//        sincronizarBancoDadosSaida(saidaSlice, data);
    }

    private void sincronizarBancoDadosVendas(PieChart.Data slice, LocalDate data) {
        String dataInicio = DateUtils.formatDate(data.getYear(), data.getMonthValue(), data.getDayOfMonth());
        String dataFinal = DateUtils.formatDate(data.plusDays(1).getYear(), data.plusDays(1).getMonthValue(), data.plusDays(1).getDayOfMonth());
        
        //Metodo executado numa Thread separada
        SwingWorker<List, List> worker;
        worker = new SwingWorker<List, List>() {
            @Override
            protected List<Venda> doInBackground() throws Exception {
                return ControleDAO.getBanco().getVendaDAO().buscarPorIntervalo(dataInicio, dataFinal);
            }

            //Metodo chamado apos terminar a execucao numa Thread separada
            @Override
            protected void done() {
                super.done(); //To change body of generated methods, choose Tools | Templates.
                try {
                    Float total = 0f;
                    listaVendas = this.get();
                    for (Venda v : listaVendas) {
                        total += v.getPrecoTotal();
                    }
                    slice.setPieValue(total.doubleValue());
                } catch (InterruptedException | ExecutionException ex) {
                    chamarAlerta("Erro ao consultar Banco de Dados");
                    ex.printStackTrace();
                }
            }
        };

        worker.execute();
    }

    private void sincronizarBancoDadosManutencoes(PieChart.Data slice, LocalDate data) {
        String dataInicio = DateUtils.formatDate(data.getYear(), data.getMonthValue(), data.getDayOfMonth());
        String dataFinal = DateUtils.formatDate(data.plusDays(1).getYear(), data.plusDays(1).getMonthValue(), data.plusDays(1).getDayOfMonth());
        
        //Metodo executado numa Thread separada
        SwingWorker<List, List> worker = new SwingWorker<List, List>() {
            @Override
            protected List<Manutencao> doInBackground() throws Exception {
                return ControleDAO.getBanco().getManutencaoDAO().buscarPorIntervalo(dataInicio, dataFinal);
            }

            //Metodo chamado apos terminar a execucao numa Thread separada
            @Override
            protected void done() {
                super.done(); //To change body of generated methods, choose Tools | Templates.
                try {
                    Float total = 0f;
                    listaManutencoes = this.get();
                    for (Manutencao v : listaManutencoes) {
                        total += v.getPreco();
                    }
                    slice.setPieValue(total.doubleValue());
                } catch (InterruptedException | ExecutionException ex) {
                    chamarAlerta("Erro ao consultar Banco de Dados");
                    ex.printStackTrace();
                }
            }
        };

        worker.execute();
    }

    private void sincronizarBancoDadosReceita(PieChart.Data slice, LocalDate data) {
        String dataInicio = DateUtils.formatDate(data.getYear(), data.getMonthValue(), data.getDayOfMonth());
        String dataFinal = DateUtils.formatDate(data.plusDays(1).getYear(), data.plusDays(1).getMonthValue(), data.plusDays(1).getDayOfMonth());
        
        //Metodo executado numa Thread separada
        SwingWorker<List, List> worker = new SwingWorker<List, List>() {
            @Override
            protected List<Receita> doInBackground() throws Exception {
                return ControleDAO.getBanco().getReceitaDAO().buscarPorIntervalo(dataInicio, dataFinal);
            }

            //Metodo chamado apos terminar a execucao numa Thread separada
            @Override
            protected void done() {
                super.done(); //To change body of generated methods, choose Tools | Templates.
                try {
                    Float total = 0f;
                    listaReceitas = this.get();
                    for (Receita v : listaReceitas) {
                        total += v.getValor();
                    }
                    slice.setPieValue(total.doubleValue());
                } catch (InterruptedException | ExecutionException ex) {
                    chamarAlerta("Erro ao consultar Banco de Dados");
                    ex.printStackTrace();
                }
            }
        };

        worker.execute();
    }
    
    private void sincronizarBancoDadosSaida(PieChart.Data slice, LocalDate data) {
        String dataInicio = DateUtils.formatDate(data.getYear(), data.getMonthValue(), data.getDayOfMonth());
        String dataFinal = DateUtils.formatDate(data.plusDays(1).getYear(), data.plusDays(1).getMonthValue(), data.plusDays(1).getDayOfMonth());
        
        //Metodo executado numa Thread separada
        SwingWorker<List, List> worker = new SwingWorker<List, List>() {
            @Override
            protected List<Saida> doInBackground() throws Exception {
                return ControleDAO.getBanco().getSaidaDAO().buscarPorIntervalo(dataInicio, dataFinal);
            }

            //Metodo chamado apos terminar a execucao numa Thread separada
            @Override
            protected void done() {
                super.done(); //To change body of generated methods, choose Tools | Templates.
                try {
                    Float total = 0f;
                    listaSaidas = this.get();
                    for (Saida v : listaSaidas) {
                        total += v.getValor();
                    }
                    slice.setPieValue(total.doubleValue());
                } catch (InterruptedException | ExecutionException ex) {
                    chamarAlerta("Erro ao consultar Banco de Dados");
                    ex.printStackTrace();
                }
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

    @FXML
    private void gerarRelatorio(Date data) throws SQLException, JRException, IOException {
        
        
        
        ConexaoBanco conn = new ConexaoBanco();
        Statement stm = conn.getConnection().createStatement();
        String query;
        query = "select * from produto";
        ResultSet rs = stm.executeQuery(query);
        JRResultSetDataSource jrRS = new JRResultSetDataSource(rs);
        Map parameters = new HashMap();
        String src2 = new File("src/relatorio/produtos.jasper").getCanonicalPath();
        JasperPrint jp = null;

        try {
            jp = JasperFillManager.fillReport(src2, parameters, jrRS);
        } catch (JRException ex) {
            System.out.println("Error: " + ex);
        }

        JasperViewer view = new JasperViewer(jp, false);

        view.setVisible(true);
    }

}
