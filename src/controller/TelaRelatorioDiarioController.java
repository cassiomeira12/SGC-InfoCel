/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import banco.ControleDAO;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.PieChart;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javax.swing.SwingWorker;
import model.Manutencao;
import model.Receita;
import model.Saida;
import model.Venda;
import org.apache.log4j.Logger;
import util.DateUtils;
import util.Formatter;
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
    private PieChart graficoPie;
    @FXML
    private DatePicker dataDatePicker;
    @FXML
    private Label caption;
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
            //Logger.getLogger(getClass()).error(ex);
            ex.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        this.data = LocalDate.now();
        this.dataDatePicker.setValue(data);

        vendaSlice = new PieChart.Data("VENDA", 0);
        manutencaoSlice = new PieChart.Data("MANUTENÇÃO", 0);
        saidaSlice = new PieChart.Data("SAÍDA", 0);
        receitaSlice = new PieChart.Data("RECEITA", 0);
        
        graficoPie.getData().addAll(saidaSlice, vendaSlice, manutencaoSlice, receitaSlice);
        //graficoPie.setTitle("Relatório do dia " + DateUtils.formatDate(data));
        graficoPie.setLabelLineLength(50);
        
//        vendaSlice.getNode().addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent event) {
//                TelaConsultarVendasController tela = new TelaConsultarVendasController(painelPrincipal);
//                adicionarPainelInterno(tela);
//            }
//        });
//        
//        manutencaoSlice.getNode().addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent event) {
//                TelaConsultarManutencoesController tela = new TelaConsultarManutencoesController(painelPrincipal);
//                adicionarPainelInterno(tela);
//            }
//        });
//        
//        receitaSlice.getNode().addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent event) {
//                TelaReceitaController tela = new TelaReceitaController(painelPrincipal);
//                adicionarPainelInterno(tela);
//            }
//        });
//        
//        saidaSlice.getNode().addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent event) {
//                TelaSaidaController tela = new TelaSaidaController(painelPrincipal);
//                adicionarPainelInterno(tela);
//            }
//        });
        
        indicator.setMaxSize(200, 200);
        stackPane.getChildren().add(indicator);

        dataDatePicker.setOnAction((e) -> {
            data = dataDatePicker.getValue();
            sincronizarBancoDados(data);
        });
        
        sincronizarBancoDados(data);
    }
    
    private void adicionarPainelInterno(AnchorPane novaTela) {
        this.painelPrincipal.setCenter(novaTela);
    }

    private void sincronizarBancoDados(LocalDate data) {
        String dataInicio = DateUtils.formatDate(data.getYear(), data.getMonthValue(), data.getDayOfMonth());
        String dataFinal = DateUtils.formatDate(data.plusDays(1).getYear(), data.plusDays(1).getMonthValue(), data.plusDays(1).getDayOfMonth());
        
        graficoPie.setVisible(false);
        indicator.setVisible(true);
        
        vendasLabel.setVisible(false);
        manutencoesLabel.setVisible(false);
        receitasLabel.setVisible(false);
        saidasLabel.setVisible(false);
        totalReceitasLabel.setVisible(false);
        totalSaidasLabel.setVisible(false);
        
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
                Float totalReceitas = 0f;
                Float totalSaidas  = 0f;
                
                if (listaVendas != null) {
                    for (Venda v : listaVendas) {
                        total += v.getPrecoTotal();
                    }
                    totalReceitas += total;
                    vendaSlice.setPieValue(total.doubleValue());
                    atualizarLabel(vendasLabel, total);
                }
                
                if (listaManutencoes != null) {
                    total = 0f;
                    for (Manutencao m : listaManutencoes) {
                        if (m.isFinalizado()) {
                            total += m.getPreco();
                        }
                    }
                    totalReceitas += total;
                    manutencaoSlice.setPieValue(total.doubleValue());
                    atualizarLabel(manutencoesLabel, total);
                }
                
                if (listaReceitas != null) {
                    total = 0f;
                    for (Receita r : listaReceitas) {
                        total += r.getValor();
                    }
                    totalReceitas += total;
                    receitaSlice.setPieValue(total.doubleValue());
                    atualizarLabel(receitasLabel, total);
                }
                
                if (listaSaidas != null) {
                    total = 0f;
                    for (Saida s : listaSaidas) {
                        total += s.getValor();
                    }
                    totalSaidas += total;
                    saidaSlice.setPieValue(total.doubleValue());
                    atualizarLabel(saidasLabel, total);
                }
                
                atualizarLabel(totalReceitasLabel, totalReceitas);
                atualizarLabel(totalSaidasLabel, totalSaidas);
                
                graficoPie.setVisible(true);
                indicator.setVisible(false);
                
                vendasLabel.setVisible(true);
                manutencoesLabel.setVisible(true);
                receitasLabel.setVisible(true);
                saidasLabel.setVisible(true);
                totalReceitasLabel.setVisible(true);
                totalSaidasLabel.setVisible(true);
            }
        };

        worker.execute();
    }
    
    private void atualizarLabel(Label label, Float valor) {
        Platform.runLater(() -> {
            if (valor == 0) {
                label.setText("0.0");
            } else {
                label.setText(Formatter.dinheiroFormatado(valor));
            }
        });
    } 
    
    private void chamarAlerta(String mensagem) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Alerta.erro(mensagem);
            }
        });
    }

    @FXML
    private void cancelarOperacao() {
        TelaInicialController telaInicial = new TelaInicialController(painelPrincipal);
        this.adicionarPainelInterno(telaInicial);
    }
    
}
