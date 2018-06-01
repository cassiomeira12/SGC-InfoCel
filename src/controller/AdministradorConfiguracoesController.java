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
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javax.swing.SwingWorker;
import model.Administrador;
import model.Bairro;
import model.Cidade;
import model.Endereco;
import util.Formatter;
import util.alerta.Alerta;
import util.alerta.Dialogo;

/**
 * FXML Controller class
 *
 * @author cassio
 */
public class AdministradorConfiguracoesController implements Initializable {

    private List<Administrador> listaAdministrador;
    private Administrador administradorSelecioado;
    
    private List<Cidade> listaCidades;
    private List<Bairro> listaBairro;
    
    @FXML
    private Button editarButton;
    @FXML
    private Button excluirButton;
    @FXML
    private TableView<Administrador> administradoresTable;
    @FXML
    private TableColumn<Administrador, String> nomeColumn;
    @FXML
    private TableColumn<Administrador, String> loginColumn;
    @FXML
    private TableColumn<Administrador, String> rgColumn;
    @FXML
    private TableColumn<Administrador, String> cpfColumn;
    @FXML
    private TableColumn<Endereco, String> enderecoColumn;
    @FXML
    private TableColumn<Administrador, String> dataColumn;
    @FXML
    private CheckBox editarCheckBox;
    @FXML
    private TextField nomeText;
    @FXML
    private TextField cpfText;
    @FXML
    private TextField rgText;
    @FXML
    private HBox cidadeBox;
    @FXML
    private ComboBox<Cidade> cidadeComboBox;
    @FXML
    private HBox adicionarCidadeBox;
    @FXML
    private TextField adicionarCidadeText;
    @FXML
    private HBox adicionarBairroBox;
    @FXML
    private TextField adicionarBairroText;
    @FXML
    private HBox bairroBox;
    @FXML
    private ComboBox<Bairro> bairroComboBox;
    @FXML
    private TextField ruaText;
    @FXML
    private TextField numeroText;
    @FXML
    private TextField loginText;
    @FXML
    private Label senhaLabel;
    @FXML
    private PasswordField senhaPassword;
    @FXML
    private Label confirmarSenhaLabel;
    @FXML
    private PasswordField confirmarSenhaPassword;
    @FXML
    private Label alertaSenhaLabel;
    @FXML
    private Button salvarButton;
    @FXML
    private VBox painelAdministradores;
    @FXML
    private VBox painelAdministrador;
    @FXML
    private GridPane dadosGridPane;
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        administradorSelecioado = null;
        //Desativa os Botoes de Editar e Excluir quando nenhum item na tabela esta selecionado
        editarButton.disableProperty().bind(administradoresTable.getSelectionModel().selectedItemProperty().isNull());
        excluirButton.disableProperty().bind(administradoresTable.getSelectionModel().selectedItemProperty().isNull());
        dadosGridPane.disableProperty().bind(editarCheckBox.selectedProperty().not());
        salvarButton.disableProperty().bind(nomeText.textProperty().isEmpty().or(
                                            cpfText.textProperty().isEmpty().or(
                                            rgText.textProperty().isEmpty().or(
                                            cidadeComboBox.selectionModelProperty().isNull().or(
                                            bairroComboBox.selectionModelProperty().isNull().or(
                                            ruaText.textProperty().isEmpty().or(
                                            numeroText.textProperty().isEmpty().or(
                                            loginText.textProperty().isEmpty().or(
                                            senhaPassword.textProperty().isEmpty().or(
                                            confirmarSenhaPassword.textProperty().isEmpty()))))))))));
        
        alertaSenhaLabel.setVisible(false);
        
        Formatter.mascaraCPF(cpfText);//Formatador para CPF
        Formatter.mascaraRG(rgText);//Formatador para Rg
        
        Formatter.toUpperCase(nomeText, cpfText, rgText, ruaText, numeroText, adicionarCidadeText, adicionarBairroText);
        
        cidadeComboBox.setOnAction((e) -> {
            Cidade cidade = cidadeComboBox.getValue();
            bairroComboBox.getSelectionModel().clearSelection();
            sincronizarBancoDadosBairro(cidade);
        });
        
        sincronizarBancoDadosCidade();
        sincronizarBancoDadosAdministradores();
    }    
    
    @FXML
    private void adicionar(ActionEvent event) {
        this.painelAdministradores.setVisible(false);
        this.painelAdministrador.setVisible(true);
        this.editarCheckBox.setVisible(false);
        this.editarCheckBox.setSelected(true);
    
        
        confirmarSenhaPassword.textProperty().addListener((ov, oldValue, newValue) -> {
            if (senhaPassword.getText().equals(newValue)) {
                alertaSenhaLabel.setVisible(false);
            } else {
                alertaSenhaLabel.setVisible(true);
            }
            if (senhaPassword.getText().isEmpty()) {
                alertaSenhaLabel.setVisible(false);
            }
        });
    
    }

    @FXML
    private void editar(ActionEvent event) {
        this.painelAdministradores.setVisible(false);
        this.painelAdministrador.setVisible(true);
        this.administradorSelecioado = administradoresTable.getSelectionModel().getSelectedItem();
        
        inserirDadosAdministrador(administradorSelecioado);
        
        this.senhaLabel.setText("Senha Antiga");
        this.confirmarSenhaLabel.setText("Nova Senha");
        
        confirmarSenhaPassword.textProperty().addListener((ov, oldValue, newValue) -> {
//            if (senhaPassword.getText().equals(newValue)) {
//                alertaSenhaLabel.setVisible(false);
//            } else {
//                alertaSenhaLabel.setVisible(true);
//            }
//            if (senhaPassword.getText().isEmpty()) {
//                alertaSenhaLabel.setVisible(false);
//            }
        });
    }
    
    private void inserirDadosAdministrador(Administrador adm) {
        this.nomeText.setText(adm.getNome());
        this.cpfText.setText(adm.getCpf());
        this.rgText.setText(adm.getRg());
        this.ruaText.setText(adm.getEndereco().getRua());
        this.numeroText.setText(adm.getEndereco().getNumero());
        this.loginText.setText(adm.getLogin());
    }
    
    @FXML
    private void excluir(ActionEvent event) {
    }

    private void atualizarTabelaAdministradores() {
        //Transforma a lista em uma Lista Observavel
        ObservableList data = FXCollections.observableArrayList(listaAdministrador);
        
        this.nomeColumn.setCellValueFactory(new PropertyValueFactory<>("nome"));//Adiciona o valor da variavel Nome
        this.loginColumn.setCellValueFactory(new PropertyValueFactory<>("login"));
        this.rgColumn.setCellValueFactory(new PropertyValueFactory<>("rg"));
        this.cpfColumn.setCellValueFactory(new PropertyValueFactory<>("cpf"));
        this.enderecoColumn.setCellValueFactory(new PropertyValueFactory<>("endereco"));
        this.dataColumn.setCellValueFactory(new PropertyValueFactory<>("dataEditada"));
        this.administradoresTable.setItems(data);//Adiciona a lista de clientes na Tabela
    }

    private void sincronizarBancoDadosAdministradores() {
        //Metodo executado numa Thread separada
        SwingWorker<List, List> worker = new SwingWorker<List, List>() {
            @Override
            protected List<Administrador> doInBackground() throws Exception {
                return ControleDAO.getBanco().getAdministradorDAO().listar();
            }
            
            //Metodo chamado apos terminar a execucao numa Thread separada
            @Override
            protected void done() {
                super.done(); //To change body of generated methods, choose Tools | Templates.
                try {
                    listaAdministrador = this.get();
                    atualizarTabelaAdministradores();
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

    @FXML
    private void adicionarCidade() {
        cidadeBox.setVisible(false);
        adicionarCidadeBox.setVisible(true);
        adicionarBairro();
        Platform.runLater(() -> adicionarCidadeText.requestFocus());//Colocando o Foco
    }
    
    @FXML
    private void selecionarCidade() {
        cidadeBox.setVisible(true);
        adicionarCidadeBox.setVisible(false);
        Formatter.limpar(adicionarCidadeText);
        Platform.runLater(() -> cidadeComboBox.requestFocus());//Colocando o Foco
    }
    
    @FXML
    private void adicionarBairro() {
        bairroBox.setVisible(false);
        adicionarBairroBox.setVisible(true);
        Platform.runLater(() -> adicionarBairroText.requestFocus());//Colocando o Foco
    }
    
    @FXML
    private void selecionarBairro() {
        bairroBox.setVisible(true);
        adicionarBairroBox.setVisible(false);
        Formatter.limpar(adicionarBairroText);
        Platform.runLater(() -> bairroComboBox.requestFocus());//Colocando o Foco
    }

    @FXML
    private void cancelar(ActionEvent event) {
        this.painelAdministradores.setVisible(true);
        this.painelAdministrador.setVisible(false);
        this.administradorSelecioado = null;
    }

    @FXML
    private void salvar(ActionEvent event) {
        String nome = nomeText.getText();
        String cpf = cpfText.getText();
        String rg = rgText.getText();
        String rua = ruaText.getText();
        String numero = numeroText.getText();
        String login = loginText.getText();
        String senha = senhaPassword.getText();
        
        Cidade cidade = null;
        Bairro bairro = null;
        Endereco endereco = null;
        
        Dialogo.Resposta resposta = Alerta.confirmar("Deseja adicionar um novo Administrador ?");
        if (resposta == Dialogo.Resposta.YES) {
            
            if (!senha.equals(confirmarSenhaPassword.getText())) {
                Alerta.erro("Erro, as senhas digitadas não conferem");
                return;
            }

            if (cidadeBox.isVisible()) {
                cidade = cidadeComboBox.getValue();
            } else if (adicionarCidadeBox.isVisible()) {
                cidade = new Cidade(null, adicionarCidadeText.getText());
            }

            if (bairroBox.isVisible()) {
                bairro = bairroComboBox.getValue();
            } else if (adicionarBairroBox.isVisible()) {
                bairro = new Bairro(null, adicionarBairroText.getText(), cidade);
            }

            endereco = new Endereco(null, bairro, rua, numero);

            Administrador novoAdministrador = new Administrador(null, nome, login, senha, endereco, null, cpf, rg, null, true);

            Long id = null;

            try {
                id = ControleDAO.getBanco().getAdministradorDAO().inserir(novoAdministrador);
                novoAdministrador.setId(id);
                administradoresTable.getItems().add(novoAdministrador);
                Alerta.info("Administrador adicionado com sucesso!");
            } catch (Exception ex) {
                Alerta.erro("Erro ao adicionar um novo Administrador");
                ex.printStackTrace();
            } finally {
                cancelar(null);
            }
        }
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
                    cidadeComboBox.setItems(cidades);
                } catch (InterruptedException | ExecutionException ex) {
                    chamarAlerta("Erro ao consultar Banco de Dados");
                    ex.printStackTrace();
                }
            }
        };

        worker.execute();
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
                    bairroComboBox.setItems(bairros);
                } catch (InterruptedException | ExecutionException ex) {
                    chamarAlerta("Erro ao consultar Banco de Dados");
                    ex.printStackTrace();
                }
            }
        };

        worker.execute();
    }
    
}
