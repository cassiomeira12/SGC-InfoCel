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
import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
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

        this.adicionarManutencao(listaOperacao, LocalDate.now());
        this.adicionarReceita(listaOperacao, LocalDate.now());
        this.adicionarSaida(listaOperacao, LocalDate.now());
        this.adicionarVenda(listaOperacao, LocalDate.now());
        
        this.atualizarTabela();
        
        dinheiroLabel.textProperty().bind(this.totalDiario.asString());
        
        Collections.sort(listaOperacao);//Ordenando as Operacoes
        
        
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
    
    private void adicionarManutencao(List<Operacao> lista, LocalDate data) {
        String dataInicio = DateUtils.formatDate(data.getYear(), data.getMonthValue(), data.getDayOfMonth());
        String dataFinal = DateUtils.formatDate(data.plusDays(1).getYear(), data.plusDays(1).getMonthValue(), data.plusDays(1).getDayOfMonth());
        
        List<Manutencao> listaManutencao = ControleDAO.getBanco().getManutencaoDAO().buscarPorIntervalo(dataInicio, dataFinal);
    
        for (Manutencao manutencao : listaManutencao) {
            lista.add(new Operacao(manutencao));
            this.totalDiario.set(totalDiario.get() + manutencao.getPreco());
        }
    }
    
    private void adicionarReceita(List<Operacao> lista, LocalDate data) {
        String dataInicio = DateUtils.formatDate(data.getYear(), data.getMonthValue(), data.getDayOfMonth());
        String dataFinal = DateUtils.formatDate(data.plusDays(1).getYear(), data.plusDays(1).getMonthValue(), data.plusDays(1).getDayOfMonth());
        
        List<Receita> listaReceita = ControleDAO.getBanco().getReceitaDAO().buscarPorIntervalo(dataInicio, dataFinal);
    
        for (Receita receita : listaReceita) {
            lista.add(new Operacao(receita));
            this.totalDiario.set(totalDiario.get() + receita.getValor());
        }
    }
    
    private void adicionarSaida(List<Operacao> lista, LocalDate data) {
        String dataInicio = DateUtils.formatDate(data.getYear(), data.getMonthValue(), data.getDayOfMonth());
        String dataFinal = DateUtils.formatDate(data.plusDays(1).getYear(), data.plusDays(1).getMonthValue(), data.plusDays(1).getDayOfMonth());
        
        List<Saida> listaSaida = ControleDAO.getBanco().getSaidaDAO().buscarPorIntervalo(dataInicio, dataFinal);
    
        for (Saida saida : listaSaida) {
            lista.add(new Operacao(saida));
            this.totalDiario.set(totalDiario.get() - saida.getValor());
        }
    }
    
    private void adicionarVenda(List<Operacao> lista, LocalDate data) {
        String dataInicio = DateUtils.formatDate(data.getYear(), data.getMonthValue(), data.getDayOfMonth());
        String dataFinal = DateUtils.formatDate(data.plusDays(1).getYear(), data.plusDays(1).getMonthValue(), data.plusDays(1).getDayOfMonth());
        
        //List<Venda> listaVenda = ControleDAO.getBanco().getVendaDAO().buscarPorIntervalo(dataInicio, dataFinal);
        List<Venda> listaVenda = ControleDAO.getBanco().getVendaDAO().listar();
        
        for (Venda venda : listaVenda) {
            lista.add(new Operacao(venda));
            this.totalDiario.set(totalDiario.get() + venda.getPrecoTotal());
        }
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
    
}
