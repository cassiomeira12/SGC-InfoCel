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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javax.swing.SwingWorker;
import model.Administrador;
import model.Cliente;
import model.Manutencao;
import util.alerta.Alerta;

/**
 * FXML Controller class
 *
 * @author cassio
 */
public class TelaConsultarManutencoesController extends AnchorPane {
    
    private BorderPane painelPrincipal;

    private List<Manutencao> listaManutencoes;
    
    @FXML
    private ComboBox<String> mesComboBox;
    @FXML
    private ComboBox<String> anoComboBox;
    
    
    @FXML
    private TableView<Manutencao> manutencoesTable;
    @FXML
    private TableColumn<Cliente, String> clienteColumn;
    @FXML
    private TableColumn<Cliente, String> enderecoColumn;
    @FXML
    private TableColumn<Administrador, String> vendedorColumn;
    @FXML
    private TableColumn<String, String> marcaColumn;
    @FXML
    private TableColumn<String, String> modeloColumn;
    @FXML
    private TableColumn<Long, String> dataColumn;
    @FXML
    private TableColumn<Float, String> precoColumn;
    
    
    
  
    public TelaConsultarManutencoesController(BorderPane painelPrincipal) {
        this.painelPrincipal = painelPrincipal;
        
        try {
            FXMLLoader fxml = new FXMLLoader(getClass().getResource("/view/TelaConsultarManutencoes.fxml"));
            fxml.setRoot(this);
            fxml.setController(this);
            fxml.load();
        } catch (IOException ex) {
            System.out.println("[ERRO] : Erro na tela Consultar Manutencoes");
            System.out.println(ex.toString());
        }
    }

    @FXML
    public void initialize() {
        // TODO
        sincronizarBancoDados();
        
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
    private void editarManutencao() {
        
    }
    
    @FXML
    private void excluirManutencao() {
        
    }
    
    @FXML
    private void pesquisarCliente() {
        
    }
    
    private void sincronizarBancoDados() {
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
                    atualizarTabela();
                } catch (InterruptedException | ExecutionException ex) {
                    chamarAlerta("Erro ao consultar Banco de Dados");
                }
            }
        };

        worker.execute();
    }
    
    private void atualizarTabela() {
        //Transforma a lista em uma Lista Observavel
        ObservableList data = FXCollections.observableArrayList(listaManutencoes);
        
        this.clienteColumn.setCellValueFactory(new PropertyValueFactory<>("cliente"));
        //this.enderecoColumn.setCellValueFactory(new PropertyValueFactory<>(""));
        this.vendedorColumn.setCellValueFactory(new PropertyValueFactory<>("administrador"));
        this.marcaColumn.setCellValueFactory(new PropertyValueFactory<>("marca"));
        this.modeloColumn.setCellValueFactory(new PropertyValueFactory<>("modelo"));
        this.dataColumn.setCellValueFactory(new PropertyValueFactory<>("dataCadastro"));
        this.precoColumn.setCellValueFactory(new PropertyValueFactory<>("preco"));
        this.manutencoesTable.setItems(data);//Adiciona a lista de clientes na Tabela
    }
    
    private void chamarAlerta(String mensagem) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Alerta.erro(mensagem);
            }
        });
    }
    
}
