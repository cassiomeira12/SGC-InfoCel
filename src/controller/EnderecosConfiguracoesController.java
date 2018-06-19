/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import banco.ControleDAO;
import controller.AdicionarCidadeBairroController.Tipo;
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
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.swing.SwingWorker;
import model.Bairro;
import model.Cidade;
import org.apache.log4j.Logger;
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
    private Button adicionarBairroButton;
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
        
        adicionarBairroButton.disableProperty().bind(cidadesTable.getSelectionModel().selectedItemProperty().isNull());
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
        Stage palco = new Stage();
        palco.initModality(Modality.APPLICATION_MODAL);//Impede de clicar na tela em plano de fundo
        palco.centerOnScreen();
        palco.initStyle(StageStyle.UNDECORATED);//Remove a barra de menu
        
        AdicionarCidadeBairroController adicionarCidadeBairro = new AdicionarCidadeBairroController(palco, Tipo.CIDADE, true);
        adicionarCidadeBairro.setTitulo("Adicionar Cidade");
        palco.setScene(new Scene(adicionarCidadeBairro));
        palco.showAndWait();
        
        if (adicionarCidadeBairro.RESULTADO) {
            Cidade cidade = adicionarCidadeBairro.getCidade();
        
            Long id = null;
            try {
                id = ControleDAO.getBanco().getCidadeDAO().inserir(cidade);
            } catch (Exception ex) {
                Logger.getLogger(getClass()).error(ex);
                ex.printStackTrace();
            }

            if (id == null) {//Erro ao inserir item no Banco de Dados
                Alerta.erro("Erro ao criar nova Cidade");
            } else {
                Alerta.info("Cidade adicionada com sucesso!");
                cidade.setId(id);
                cidadesTable.getItems().add(cidade);
            }
        }
    }

    @FXML
    private void editarCidade(ActionEvent event) {
        Cidade cidade = cidadesTable.getSelectionModel().getSelectedItem();
        
        Stage palco = new Stage();
        palco.initModality(Modality.APPLICATION_MODAL);//Impede de clicar na tela em plano de fundo
        palco.centerOnScreen();
        palco.initStyle(StageStyle.UNDECORATED);//Remove a barra de menu
        
        AdicionarCidadeBairroController editarCidade = new AdicionarCidadeBairroController(palco, Tipo.CIDADE, false);
        editarCidade.setTitulo("Editar Cidade");
        editarCidade.setCidade(cidade);
        
        palco.setScene(new Scene(editarCidade));
        palco.showAndWait();
        
        if (editarCidade.RESULTADO) {
            cidade = editarCidade.getCidade();
            
            try {
                if (ControleDAO.getBanco().getCidadeDAO().editar(cidade)) {
                    Alerta.info("Dados alterados com sucesso!");
                    sincronizarBancoDadosCidade();
                } else {
                    Alerta.erro("Erro ao elterar dados!");
                }
            } catch (Exception ex) {
                Logger.getLogger(getClass()).error(ex);
                ex.printStackTrace();
            }
        }
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
                Logger.getLogger(getClass()).error(ex);
                Alerta.erro("Erro ao exlucir Cidade");
                ex.printStackTrace();
            }
        }

        cidadesTable.getSelectionModel().clearSelection();
    }

    @FXML
    private void adicionarBairro(ActionEvent event) {
        Stage palco = new Stage();
        palco.initModality(Modality.APPLICATION_MODAL);//Impede de clicar na tela em plano de fundo
        palco.centerOnScreen();
        palco.initStyle(StageStyle.UNDECORATED);//Remove a barra de menu
        
        AdicionarCidadeBairroController adicionarCidadeBairro = new AdicionarCidadeBairroController(palco, Tipo.BAIRRO, true);
        adicionarCidadeBairro.setTitulo("Adicionar Bairro");
        adicionarCidadeBairro.setCidade(cidadesTable.getSelectionModel().getSelectedItem());
        palco.setScene(new Scene(adicionarCidadeBairro));
        palco.showAndWait();
        
        if (adicionarCidadeBairro.RESULTADO) {
            Bairro bairro = adicionarCidadeBairro.getBairro();
        
            Long id = null;
            try {
                id = ControleDAO.getBanco().getBairroDAO().inserir(bairro);
            } catch (Exception ex) {
                Logger.getLogger(getClass()).error(ex);
                ex.printStackTrace();
            }

            if (id == null) {//Erro ao inserir item no Banco de Dados
                Alerta.erro("Erro ao criar novo Bairro");
            } else {
                Alerta.info("Bairro adicionado com sucesso!");
                bairro.setId(id);
                bairrosTable.getItems().add(bairro);
            }
        }
    }

    @FXML
    private void editarBairro(ActionEvent event) {
        Bairro bairro = bairrosTable.getSelectionModel().getSelectedItem();
        
        Stage palco = new Stage();
        palco.initModality(Modality.APPLICATION_MODAL);//Impede de clicar na tela em plano de fundo
        palco.centerOnScreen();
        palco.initStyle(StageStyle.UNDECORATED);//Remove a barra de menu
        
        AdicionarCidadeBairroController editarBairro = new AdicionarCidadeBairroController(palco, Tipo.BAIRRO, false);
        editarBairro.setTitulo("Editar Bairro");
        editarBairro.setCidade(cidadesTable.getSelectionModel().getSelectedItem());
        editarBairro.setBairro(bairro);
        
        palco.setScene(new Scene(editarBairro));
        palco.showAndWait();
        
        if (editarBairro.RESULTADO) {
            bairro = editarBairro.getBairro();
            
            try {
                if (ControleDAO.getBanco().getBairroDAO().editar(bairro)) {
                    Alerta.info("Dados alterados com sucesso!");
                    sincronizarBancoDadosBairro(cidadesTable.getSelectionModel().getSelectedItem());
                } else {
                    Alerta.erro("Erro ao elterar dados!");
                }
            } catch (Exception ex) {
                Logger.getLogger(getClass()).error(ex);
                ex.printStackTrace();
            }
        }
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
                Logger.getLogger(getClass()).error(ex);
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
                    Logger.getLogger(getClass()).error(ex);
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
                    Logger.getLogger(getClass()).error(ex);
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
