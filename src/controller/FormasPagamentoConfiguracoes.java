/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import banco.ControleDAO;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javax.swing.SwingWorker;
import model.FormaPagamento;
import util.alerta.Alerta;

/**
 * FXML Controller class
 *
 * @author cassio
 */
public class FormasPagamentoConfiguracoes implements Initializable {

    List<FormaPagamento> listaFormasPagamento;
    
    @FXML
    private TableView<FormaPagamento> formasPagamentoTable;
    @FXML
    private TableColumn<FormaPagamento, String> descricaoColumn;
    @FXML
    private TableColumn<FormaPagamento, String> parcelasColumn;
    @FXML
    private Button editarButton;
    @FXML
    private Button removerButton;
    @FXML
    private VBox novaFormaPagamento;
    @FXML
    private Label formaPagamentoLabel;
    @FXML
    private TextField descricaoText;
    @FXML
    private TextField parcelasText;
    @FXML
    private Button salvarButton;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        sincronizarBancoDados();
    }    

    @FXML
    private void adicionar(ActionEvent event) {
    }

    @FXML
    private void editar(ActionEvent event) {
    }

    @FXML
    private void remover(ActionEvent event) {
    }

    @FXML
    private void cancelar(ActionEvent event) {
    }

    @FXML
    private void salvar(ActionEvent event) {
    }
    
    private void atualizarTabela() {
        //Transforma a lista em uma Lista Observavel
        ObservableList data = FXCollections.observableArrayList(listaFormasPagamento);
        
        this.descricaoColumn.setCellValueFactory(new PropertyValueFactory<>("descricao"));//Adiciona o valor da variavel Nome
        this.parcelasColumn.setCellValueFactory(new PropertyValueFactory<>("parcelas"));//Adiciona o valor da variavel Endereco
        this.formasPagamentoTable.setItems(data);//Adiciona a lista de clientes na Tabela
    }

    private void sincronizarBancoDados() {
        //Metodo executado numa Thread separada
        SwingWorker<List, List> worker = new SwingWorker<List, List>() {
            @Override
            protected List<FormaPagamento> doInBackground() throws Exception {
                return ControleDAO.getBanco().getFormaPagamentoDAO().listar();
            }

            //Metodo chamado apos terminar a execucao numa Thread separada
            @Override
            protected void done() {
                super.done(); //To change body of generated methods, choose Tools | Templates.
                try {
                    listaFormasPagamento = this.get();
                    atualizarTabela();
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
    
}
