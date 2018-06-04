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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.SwingWorker;
import model.Bairro;
import model.Cidade;
import util.alerta.Alerta;
import util.alerta.Dialogo;

/**
 * FXML Controller class
 *
 * @author cassio
 */
public class EnderecosConfiguracoesController implements Initializable {

    private List<Cidade> listaCidades;
    private List<Bairro> listaBairro;
    
    
    @FXML
    private TableView<Cidade> cidadesTable;
    @FXML
    private TableColumn<Cidade, String> nomeCidadeColumn;
    @FXML
    private TableColumn<Bairro, String> nomeBairroColumn;
    @FXML
    private Button editarCidadeButton;
    @FXML
    private Button excluirCidadeButton;
    @FXML
    private TableView<Bairro> bairrosTable;
    @FXML
    private Button editarBairroButton;
    @FXML
    private Button excluirBairroButton;
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        editarCidadeButton.disableProperty().bind(cidadesTable.getSelectionModel().selectedItemProperty().isNull());
        excluirCidadeButton.disableProperty().bind(cidadesTable.getSelectionModel().selectedItemProperty().isNull());
        
        editarBairroButton.disableProperty().bind(bairrosTable.getSelectionModel().selectedItemProperty().isNull());
        excluirBairroButton.disableProperty().bind(bairrosTable.getSelectionModel().selectedItemProperty().isNull());
        
        cidadesTable.setOnMouseClicked((e) -> {
            Cidade cidade = cidadesTable.getSelectionModel().getSelectedItem();
            sincronizarBancoDadosBairro(cidade);
        });
        
        sincronizarBancoDadosCidade();
    }    

    @FXML
    private void adicionarCidade(ActionEvent event) {
        
    }

    @FXML
    private void editarCidade(ActionEvent event) {
    }

    @FXML
    private void excluirCidade(ActionEvent event) {
        Cidade cidade = cidadesTable.getSelectionModel().getSelectedItem();
        
        Dialogo.Resposta resposta = Alerta.confirmar("Deseja remover a Cidade  " + cidade.getNome() + " ?");

        if (resposta == Dialogo.Resposta.YES) {
            try {
                ControleDAO.getBanco().getCidadeDAO().excluir(cidade);
                Alerta.info("Cidade excluida com sucesso!");
                sincronizarBancoDadosCidade();
            } catch (SQLException ex) {
                Alerta.erro("Erro ao exlucir Cidade");
                ex.printStackTrace();
            }
        }

        cidadesTable.getSelectionModel().clearSelection();
    }

    @FXML
    private void adicionarBairro(ActionEvent event) {
    }

    @FXML
    private void editarBairro(ActionEvent event) {
    }

    @FXML
    private void excluirBairro(ActionEvent event) {
        Bairro bairro = bairrosTable.getSelectionModel().getSelectedItem();
        
        Dialogo.Resposta resposta = Alerta.confirmar("Deseja remover o Bairro  " + bairro.getNome() + " ?");

        if (resposta == Dialogo.Resposta.YES) {
            try {
                ControleDAO.getBanco().getBairroDAO().excluir(bairro);
                Alerta.info("Bairro excluido com sucesso!");
                sincronizarBancoDadosCidade();
            } catch (SQLException ex) {
                Alerta.erro("Erro ao exlucir Bairro");
                ex.printStackTrace();
            }
        }

        bairrosTable.getSelectionModel().clearSelection();
    }
    
    private void sincronizarBancoDadosCidade() {
        //Metodo executado numa Thread separada
        SwingWorker<List, List> worker = new SwingWorker<List, List>() {
            @Override
            protected List<Cidade> doInBackground() throws Exception {
                return ControleDAO.getBanco().getCidadeDAO().listar();
            }

            //Metodo chamado apos terminar a execucao numa Thread separada
            @Override
            protected void done() {
                super.done(); //To change body of generated methods, choose Tools | Templates.
                try {
                    listaCidades = this.get();
                    ObservableList cidades = FXCollections.observableArrayList(listaCidades);
                    atualizarTabelaCidades();
                } catch (InterruptedException | ExecutionException ex) {
                    chamarAlerta("Erro ao consultar Banco de Dados");
                    ex.printStackTrace();
                }
            }
        };

        worker.execute();
    }
    
    private void atualizarTabelaCidades() {
        //Transforma a lista em uma Lista Observavel
        ObservableList data = FXCollections.observableArrayList(listaCidades);
        
        this.nomeCidadeColumn.setCellValueFactory(new PropertyValueFactory<>("nome"));//Adiciona o valor da variavel Nome
        this.cidadesTable.setItems(data);//Adiciona a lista de clientes na Tabela
    }
    
    private void sincronizarBancoDadosBairro(Cidade cidade) {
        //Metodo executado numa Thread separada
        SwingWorker<List, List> worker = new SwingWorker<List, List>() {
            @Override
            protected List<Bairro> doInBackground() throws Exception {
                return ControleDAO.getBanco().getBairroDAO().buscarPorCidade(cidade);
            }

            //Metodo chamado apos terminar a execucao numa Thread separada
            @Override
            protected void done() {
                super.done(); //To change body of generated methods, choose Tools | Templates.
                try {
                    listaBairro = this.get();
                    ObservableList bairros = FXCollections.observableArrayList(listaBairro);
                    atualizarTabelaBairros();
                } catch (InterruptedException | ExecutionException ex) {
                    chamarAlerta("Erro ao consultar Banco de Dados");
                    ex.printStackTrace();
                }
            }
        };

        worker.execute();
    }
    
    private void atualizarTabelaBairros() {
        //Transforma a lista em uma Lista Observavel
        ObservableList data = FXCollections.observableArrayList(listaBairro);
        
        this.nomeBairroColumn.setCellValueFactory(new PropertyValueFactory<>("nome"));//Adiciona o valor da variavel Nome
        this.bairrosTable.setItems(data);//Adiciona a lista de clientes na Tabela
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
