/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import banco.ControleDAO;
import java.net.URL;
import java.sql.SQLException;
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
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javax.swing.SwingWorker;
import model.FormaPagamento;
import org.apache.log4j.Logger;
import util.Formatter;
import util.alerta.Alerta;
import util.alerta.Dialogo;

/**
 * FXML Controller class
 *
 * @author cassio
 */
public class FormasPagamentoConfiguracoes implements Initializable {

    private List<FormaPagamento> listaFormasPagamento;
    
    private FormaPagamento pagamentoSelecionado;
    
    @FXML
    private TableView<FormaPagamento> formasPagamentoTable;
    @FXML
    private TableColumn<FormaPagamento, String> descricaoColumn;
    @FXML
    private TableColumn<FormaPagamento, String> maximoParcelasColumn;
    @FXML
    private Button editarButton;
    @FXML
    private VBox novaFormaPagamento;
    @FXML
    private Label formaPagamentoLabel;
    @FXML
    private TextField descricaoText;
    @FXML
    private Spinner<Integer> parcelasSpinner;
    @FXML
    private Button salvarButton;
    @FXML
    private Button excluirButton;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.pagamentoSelecionado = null;
        
        Formatter.toUpperCase(descricaoText);
        
        //Desativa os Botoes de Editar e Excluir quando nenhum item na tabela esta selecionado
        editarButton.disableProperty().bind(formasPagamentoTable.getSelectionModel().selectedItemProperty().isNull());
        excluirButton.disableProperty().bind(formasPagamentoTable.getSelectionModel().selectedItemProperty().isNull());
        
        salvarButton.disableProperty().bind(descricaoText.textProperty().isEmpty());
        
        SpinnerValueFactory<Integer> valores = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 12, 1);
        this.parcelasSpinner.setValueFactory(valores);
        
        this.novaFormaPagamento.setVisible(false);
        
        sincronizarBancoDados();
    }    

    @FXML
    private void adicionar(ActionEvent event) {
        this.novaFormaPagamento.setVisible(true);
        this.pagamentoSelecionado = null;
    }

    @FXML
    private void editar(ActionEvent event) {
        this.novaFormaPagamento.setVisible(true);
        this.pagamentoSelecionado = formasPagamentoTable.getSelectionModel().getSelectedItem();
        adicionarDados(pagamentoSelecionado);
    }
    
    private void adicionarDados(FormaPagamento pagamento) {
        this.descricaoText.setText(pagamento.getDescricao());
        this.parcelasSpinner.getValueFactory().setValue(pagamento.getMaximoParcelas());
    }

    @FXML
    private void excluir(ActionEvent event) {
        FormaPagamento pagamento = formasPagamentoTable.getSelectionModel().getSelectedItem();
        
        Dialogo.Resposta resposta = Alerta.confirmar("Excluir Forma de Pagamento " + pagamento.getDescricao() + " ?");

        if (resposta == Dialogo.Resposta.YES) {
            try {
                if (ControleDAO.getBanco().getFormaPagamentoDAO().excluir(pagamento)) {
                    this.sincronizarBancoDados();
                    Alerta.info("Forma de Pagamento excluída com sucesso");
                } else {
                    chamarAlerta("Erro ao excluir a Forma de Pagamento");
                }
            } catch (Exception ex) {
                Logger.getLogger(getClass()).error(ex);
                ex.printStackTrace();
            }
        }
    }

    @FXML
    private void cancelar(ActionEvent event) {
        this.novaFormaPagamento.setVisible(false);
        this.pagamentoSelecionado = null;
        
        this.descricaoText.setText("");
        this.parcelasSpinner.getValueFactory().setValue(1);
    }

    @FXML
    private void salvar(ActionEvent event) {
        String descricao = descricaoText.getText();
        int maxParcelas = parcelasSpinner.getValue();
        
        FormaPagamento novaFormaPagamento = null;
        
        if (pagamentoSelecionado == null) {
            novaFormaPagamento = new FormaPagamento(null, descricao, maxParcelas);
            Long id = null;

            try {
                id = ControleDAO.getBanco().getFormaPagamentoDAO().inserir(novaFormaPagamento);
            } catch (Exception ex) {
                Logger.getLogger(getClass()).error(ex);
                ex.printStackTrace();
            }

            if (id == null) {
                chamarAlerta("Erro ao adicionar nova Forma de Pagamento");
                return;
            } 

            novaFormaPagamento.setId(id);
            formasPagamentoTable.getItems().add(novaFormaPagamento);
            formasPagamentoTable.refresh();
            Alerta.info("Nova Forma de Pagamento adicionada com sucesso!");
            cancelar(null);
            
        } else {
            novaFormaPagamento = pagamentoSelecionado;
            
            novaFormaPagamento.setDescricao(descricao);
            novaFormaPagamento.setMaximoParcelas(maxParcelas);
            
            try {
                if (ControleDAO.getBanco().getFormaPagamentoDAO().editar(novaFormaPagamento)) {
                    Alerta.info("Dados alterados com sucesso!");
                    cancelar(null);
                    sincronizarBancoDados();
                } else {
                    chamarAlerta("Erro ao alterar informações");
                }
            } catch (SQLException ex) {
                Logger.getLogger(getClass()).error(ex);
                ex.printStackTrace();
            }
        }
    }
    
    private void atualizarTabela() {
        //Transforma a lista em uma Lista Observavel
        ObservableList data = FXCollections.observableArrayList(listaFormasPagamento);
        
        this.descricaoColumn.setCellValueFactory(new PropertyValueFactory<>("descricao"));//Adiciona o valor da variavel Nome
        this.maximoParcelasColumn.setCellValueFactory(new PropertyValueFactory<>("maximoParcelas"));//Adiciona o valor da variavel Endereco
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
                    Logger.getLogger(getClass()).error(ex);
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
