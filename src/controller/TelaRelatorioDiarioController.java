/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import banco.ControleDAO;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.PieChart;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javax.swing.SwingWorker;
import model.Manutencao;
import model.Receita;
import model.Saida;
import model.Venda;
import util.alerta.Alerta;

/**
 * FXML Controller class
 *
 * @author cassio
 */
public class TelaRelatorioDiarioController extends AnchorPane {
    
    private BorderPane painelPrincipal;
    
    private List<Venda> listaVendas;
    private List<Manutencao> listaManutencoes;
    private List<Receita> listaReceitas;
    private List<Saida> listaSaidas;

    @FXML
    private PieChart graficoPie;
    
  
    public TelaRelatorioDiarioController(BorderPane painelPrincipal) {
        this.painelPrincipal = painelPrincipal;
        
        try {
            FXMLLoader fxml = new FXMLLoader(getClass().getResource("/view/TelaRelatorioDiario.fxml"));
            fxml.setRoot(this);
            fxml.setController(this);
            fxml.load();
        } catch (IOException ex) {
            System.out.println("[ERRO] : Erro na tela Relatorio Diario");
            System.out.println(ex.toString());
        }
    }

    @FXML
    public void initialize() {
        // TODO
        
       PieChart.Data slice1 = new PieChart.Data("USA", 17947195);
       PieChart.Data slice2 = new PieChart.Data("EU", 11540278);
       PieChart.Data slice3 = new PieChart.Data("China", 10982829);
       PieChart.Data slice4 = new PieChart.Data("Japan", 4116242);
       PieChart.Data slice5 = new PieChart.Data("Others", 28584442);
       
       
       
       graficoPie.getData().addAll(slice1, slice2, slice3, slice4, slice5);
    }
    
    private void sincronizarBancoDadosVendas() {
        //Metodo executado numa Thread separada
        SwingWorker<List, List> worker = new SwingWorker<List, List>() {
            @Override
            protected List<Venda> doInBackground() throws Exception {
                return ControleDAO.getBanco().getVendaDAO().listar();
            }

            //Metodo chamado apos terminar a execucao numa Thread separada
            @Override
            protected void done() {
                super.done(); //To change body of generated methods, choose Tools | Templates.
                try {
                    listaVendas = this.get();
                    //atualizarTabela();
                } catch (InterruptedException | ExecutionException ex) {
                    chamarAlerta("Erro ao consultar Banco de Dados");
                }
            }
        };

        worker.execute();
    }
    
    private void sincronizarBancoDadosManutencoes() {
        //Metodo executado numa Thread separada
        SwingWorker<List, List> worker = new SwingWorker<List, List>() {
            @Override
            protected List<Manutencao> doInBackground() throws Exception {
                return ControleDAO.getBanco().getManutencaoDAO().listar();
            }

            //Metodo chamado apos terminar a execucao numa Thread separada
            @Override
            protected void done() {
                super.done(); //To change body of generated methods, choose Tools | Templates.
                try {
                    listaManutencoes = this.get();
                    //atualizarTabela();
                } catch (InterruptedException | ExecutionException ex) {
                    chamarAlerta("Erro ao consultar Banco de Dados");
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
    
}
