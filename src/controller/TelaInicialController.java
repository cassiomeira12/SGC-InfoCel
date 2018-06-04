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
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javax.swing.SwingWorker;
import model.Administrador;
import model.Manutencao;
import model.Operacao;
import model.Receita;
import model.Saida;
import model.Venda;
import util.DateUtils;

/**
 * FXML Controller class
 *
 * @author cassio
 */
public class TelaInicialController extends AnchorPane {
    
    private BorderPane painelPrincipal;
    private List<Operacao> listaOperacao;
    private FloatProperty totalDiario = new SimpleFloatProperty(0);
    
    @FXML
    private Label dataLabel;
    @FXML
    private TableView<Operacao> relatorioTableView;
    @FXML
    private TableColumn<String, String> categoriaColumn;
    @FXML
    private TableColumn<String, String> clienteColumn;
    @FXML
    private TableColumn<String, String> descricaoColumn;
    @FXML
    private TableColumn<Administrador, String> funcionarioColumn;
    @FXML
    private TableColumn<Float, String> valorColumn;
    
    @FXML
    private Label dinheiroLabel;
    
  
    public TelaInicialController(BorderPane painelPrincipal) {
        this.painelPrincipal = painelPrincipal;
        
        try {
            FXMLLoader fxml = new FXMLLoader(getClass().getResource("/view/TelaInicial.fxml"));
            fxml.setRoot(this);
            fxml.setController(this);
            fxml.load();
        } catch (IOException ex) {
            System.out.println("[ERRO] : Erro na tela de Login");
            System.out.println(ex);
        }
    }

    @FXML
    public void initialize() {
        this.dataLabel.setText(DateUtils.formatDateExtenso(LocalDate.now()));
        
        this.listaOperacao = new ArrayList<>();

        dinheiroLabel.textProperty().bind(this.totalDiario.asString());
        
        atualizarOperacoes(LocalDate.now());
        
//        relatorioTableView.setRowFactory(tv -> new TableRow<Operacao>() {
//            @Override
//            public void updateItem(Operacao item, boolean empty) {
//                super.updateItem(item, empty);
//                if (item == null) {
//                    setStyle("");
//                } else if (item.getCategoria().equals("Saída")) {
//                    setStyle("-fx-background-color: #e84118;");
//                } else {
//                    setStyle("-fx-background-color: #44bd32;");
//                }
//            }
//        });
    }
    
    private void adicionarPainelInterno(AnchorPane novaTela) {
        this.painelPrincipal.getCenter().setVisible(true);
        this.painelPrincipal.setCenter(novaTela);
    }
    
    @FXML
    private void chamarTelaAdicionarManutencao() {
        TelaAdicionarManutencaoController telaAdicionarManutencao = new TelaAdicionarManutencaoController(painelPrincipal);
        this.adicionarPainelInterno(telaAdicionarManutencao);
        //PainelInterno.telaAdicionarManutencao(painelPrincipal);
    }
    
    @FXML
    private void chamarTelaAdicionarReceita() {
        TelaAdicionarReceitaController telaAdicionarReceita = new TelaAdicionarReceitaController(painelPrincipal);
        this.adicionarPainelInterno(telaAdicionarReceita);
    }
    
    @FXML
    private void chamarTelaAdicionarSaida() {
        TelaAdicionarSaidaController telaAdicionarSaida = new TelaAdicionarSaidaController(painelPrincipal);
        this.adicionarPainelInterno(telaAdicionarSaida);
    }
    
    @FXML
    private void chamarTelaAdicionarVenda() {
        TelaAdicionarVendaController telaAdicionarVenda = new TelaAdicionarVendaController(painelPrincipal);
        this.adicionarPainelInterno(telaAdicionarVenda);
    }
    
    private void atualizarOperacoes(LocalDate data) {
        String dataInicio = DateUtils.formatDate(data.getYear(), data.getMonthValue(), data.getDayOfMonth());
        String dataFinal = DateUtils.formatDate(data.plusDays(1).getYear(), data.plusDays(1).getMonthValue(), data.plusDays(1).getDayOfMonth());

        //Metodo executado numa Thread separada
        SwingWorker<List, List> worker = new SwingWorker<List, List>() {
            @Override
            protected List doInBackground() throws Exception {
                List<Operacao> lista = new ArrayList<>();
                
                List<Manutencao> listaManutencao = ControleDAO.getBanco().getManutencaoDAO().listar();
                List<Receita> listaReceita = ControleDAO.getBanco().getReceitaDAO().buscarPorIntervalo(dataInicio, dataFinal);
                List<Saida> listaSaida = ControleDAO.getBanco().getSaidaDAO().buscarPorIntervalo(dataInicio, dataFinal);
                List<Venda> listaVenda = ControleDAO.getBanco().getVendaDAO().listar();
                
                for (Manutencao manutencao : listaManutencao) {
                    lista.add(new Operacao(manutencao));
                    atualizarValorTotal(manutencao.getPreco());
                }
                
                for (Receita receita : listaReceita) {
                    lista.add(new Operacao(receita));
                    atualizarValorTotal(receita.getValor());
                }
                
                for (Saida saida : listaSaida) {
                    lista.add(new Operacao(saida));
                    atualizarValorTotal(saida.getValor());
                }
                
                for (Venda venda : listaVenda) {
                    lista.add(new Operacao(venda));
                    atualizarValorTotal(venda.getPrecoTotal());
                }
                
                return lista;
            }
            
            //Metodo chamado apos terminar a execucao numa Thread separada
            @Override
            protected void done() {
                super.done();
                
                try {
                    listaOperacao = this.get();
                    Collections.sort(listaOperacao);//Ordenando as Operacoes
                    atualizarTabela();
                } catch (InterruptedException | ExecutionException ex) {
                    Logger.getLogger(TelaInicialController.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
        };
        
        worker.execute();
    }
    
    private void atualizarTabela() {
        //Transforma a lista em uma Lista Observavel
        ObservableList data = FXCollections.observableArrayList(listaOperacao);
        
        this.categoriaColumn.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        this.clienteColumn.setCellValueFactory(new PropertyValueFactory<>("cliente"));
        this.descricaoColumn.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        this.funcionarioColumn.setCellValueFactory(new PropertyValueFactory<>("funcionario"));
        this.valorColumn.setCellValueFactory(new PropertyValueFactory<>("valor"));
        
        this.relatorioTableView.setItems(data);//Adiciona a lista de clientes na Tabela
    }
    
    private void atualizarValorTotal(float valor) {
        //forma usada para executar algo na Thread procipal, pois a manipulaçao das views deve ser feita na Thread principal
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                totalDiario.set(totalDiario.get() + valor);
            }
        });
    }
}
