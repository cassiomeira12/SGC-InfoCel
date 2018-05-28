/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import banco.ControleDAO;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javax.swing.SwingWorker;
import model.Administrador;
import model.Bairro;
import model.Cidade;
import model.Endereco;
import util.DateUtils;
import util.Formatter;
import util.alerta.Alerta;
import util.alerta.Dialogo;

/**
 * FXML Controller class
 *
 * @author cassio
 */
public class TelaConfiguracoesController extends AnchorPane {
    
    private BorderPane painelPrincipal;
    private Administrador administradorSelecionado;
    private List<Administrador> administradorList;
    Endereco endereco = new Endereco(1l, new Bairro(1l, "Centro", new Cidade(1l, "Vitória da Conquista")), "Rua francisco santos", "149 A");
    
    @FXML
    private TableView<Administrador> administradoresTable;
    @FXML
    private TableColumn<Administrador, String> nomeColumn;
    @FXML
    private TableColumn<Administrador, String> rgColumn;
    @FXML
    private TableColumn<Administrador, String> cpfColumn;
    @FXML
    private TableColumn<Administrador, String> enderecoColumn;
    
    @FXML
    private GridPane administradorPane;
    @FXML
    private CheckBox editarCheckBox;
    @FXML
    private CheckBox statusCheckBox;
    @FXML
    private TextField nomeText;
    @FXML
    private Label dataLabel;
    @FXML
    private TextField enderecoText;
    @FXML
    private TextField emailText;
    @FXML
    private TextField rgText;
    @FXML
    private TextField cpfText;
    @FXML
    private TextField loginText;
    @FXML
    private Button alterarSenhaButton;
    @FXML
    private Label senhaAntigaLabel;
    @FXML
    private PasswordField senhaAntigaText;
    @FXML
    private Label novaSenhaLabel;
    @FXML
    private PasswordField novaSenhaText;
    @FXML
    private Button cancelarSenhaButton;
    @FXML
    private Button salvarSenhaButton;
    
    @FXML
    private Button adicionarButton;
    @FXML
    private Button editarButton;
    @FXML
    private Button excluirButton;
  
    public TelaConfiguracoesController(BorderPane painelPrincipal) {
        this.painelPrincipal = painelPrincipal;
        
        try {
            FXMLLoader fxml = new FXMLLoader(getClass().getResource("/view/TelaConfiguracoes.fxml"));
            fxml.setRoot(this);
            fxml.setController(this);
            fxml.load();
        } catch (IOException ex) {
            System.out.println("[ERRO] : Erro na tela Configuracoes");
            ex.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        //Desativa os Botoes de Editar e Excluir quando nenhum item na tabela esta selecionado
        editarButton.disableProperty().bind(administradoresTable.getSelectionModel().selectedItemProperty().isNull());
        excluirButton.disableProperty().bind(administradoresTable.getSelectionModel().selectedItemProperty().isNull());

        Formatter.toUpperCase(nomeText, enderecoText, emailText);
        Formatter.mascaraRG(rgText);
        Formatter.mascaraCPF(cpfText);
        Formatter.mascaraEmail(emailText);
        
        this.sincronizarBancoDados();
    }
    
    private void adicionarPainelInterno(AnchorPane novaTela) {
        this.painelPrincipal.setCenter(novaTela);
    }
    
    @FXML
    private void adicionarAdministrador() {
        
    }
    
    
    
    @FXML
    private void editarAdministrador() {
        this.editarButton.setVisible(false);
        this.excluirButton.getStyleClass().remove("button-excluir");
        this.excluirButton.getStyleClass().add("button-salvar");
        this.excluirButton.setText("Salvar");
        
        
        this.administradorSelecionado = administradoresTable.getSelectionModel().getSelectedItem();
        this.administradoresTable.setVisible(false);//Ocultando tabela de administradores
        
        this.adicionarDados(administradorSelecionado);
        
        //Campos ficam desativados enquanto CheckBox esta desativado
        statusCheckBox.disableProperty().bind(editarCheckBox.selectedProperty().not());
        nomeText.disableProperty().bind(editarCheckBox.selectedProperty().not());
        enderecoText.disableProperty().bind(editarCheckBox.selectedProperty().not());
        emailText.disableProperty().bind(editarCheckBox.selectedProperty().not());
        rgText.disableProperty().bind(editarCheckBox.selectedProperty().not());
        cpfText.disableProperty().bind(editarCheckBox.selectedProperty().not());
        loginText.disableProperty().bind(editarCheckBox.selectedProperty().not());
        alterarSenhaButton.disableProperty().bind(editarCheckBox.selectedProperty().not());
        
        salvarSenhaButton.disableProperty().bind(senhaAntigaText.textProperty().isEmpty().or(novaSenhaText.textProperty().isEmpty()));

        this.administradorPane.setVisible(true);//Mostrando paniel com os dados do administrador
    }
    
    private void adicionarDados(Administrador administrador) {
        this.statusCheckBox.setSelected(administrador.getStatus());
        this.dataLabel.setText(DateUtils.formatDate(administrador.getDataCadastro()));
        this.nomeText.setText(administrador.getNome());
        this.enderecoText.setText(administrador.getEndereco().toString());
        this.emailText.setText(administrador.getEmail());
        this.rgText.setText(administrador.getRg());
        this.cpfText.setText(administrador.getCpf());
        this.loginText.setText(administrador.getLogin());
    }

    @FXML
    private void excluirAdministrador() {
        
        if (excluirButton.getText().equals("Salvar")) {
            
            boolean vazio = Formatter.isEmpty(nomeText,enderecoText,emailText,rgText,cpfText,loginText);
            
            if (vazio) {
                Alerta.alerta("Prencha todos os compos do Administrador");
                return;
            }
            
            Dialogo.Resposta resposta = Alerta.confirmar("Deseja salvar as alterações do Administrador " + administradorSelecionado.getNome() + " ?");

            if (resposta == Dialogo.Resposta.YES) {
                
                this.atualizarDadosAdministrador();
                
                try {
                    if (ControleDAO.getBanco().getAdministradorDAO().editar(administradorSelecionado)) {
                        Alerta.info("Administrador Alterado com sucesso");
                    }
                } catch (SQLException ex) {
                    //Logger.getLogger(TelaConsultarClientesController.class.getName()).log(Level.SEVERE, null, ex);
                    Alerta.erro(ex.toString());
                }
                this.sincronizarBancoDados();
            }
            
            this.editarButton.setVisible(true);
            this.excluirButton.getStyleClass().remove("button-salvar");
            this.excluirButton.getStyleClass().add("button-excluir");
            this.excluirButton.setText("Excluir");
            
            this.voltar();
            
        } else {
            
        }
        
        
        
//        Cliente cliente = clientesTable.getSelectionModel().getSelectedItem();
//
//        Dialogo.Resposta resposta = Alerta.confirmar("Excluir usuário " + cliente.getNome() + " ?");
//
//        if (resposta == Dialogo.Resposta.YES) {
//            try {
//                ControleDAO.getBanco().getClienteDAO().excluir(cliente.getId().intValue());
//            } catch (SQLException ex) {
//                Logger.getLogger(TelaConsultarClientesController.class.getName()).log(Level.SEVERE, null, ex);
//            }
//            this.sincronizarBancoDados();
//            this.atualizarTabela();
//        }
//
//        clientesTable.getSelectionModel().clearSelection();
    }
    
    private void atualizarDadosAdministrador() {
       boolean status = statusCheckBox.isSelected();
       String nome = nomeText.getText();
       String email = emailText.getText();
       String rg = rgText.getText();
       String cpf = cpfText.getText();
       String login = loginText.getText();
       
       this.administradorSelecionado.setStatus(status);
       this.administradorSelecionado.setNome(nome);
       this.administradorSelecionado.setEndereco(endereco);
       this.administradorSelecionado.setEmail(email);
       this.administradorSelecionado.setRg(rg);
       this.administradorSelecionado.setCpf(cpf);
       this.administradorSelecionado.setLogin(login);
    }
    
    @FXML
    private void voltar() {
        if (administradorPane.isVisible()) {
            administradorPane.setVisible(false);
            administradoresTable.setVisible(true);
        } else {
            TelaInicialController telaInicial = new TelaInicialController(painelPrincipal);
            this.adicionarPainelInterno(telaInicial);
        }
    }
    
    @FXML
    private void alterarSenha() {
        this.alterarSenhaButton.setVisible(false);//Ocultando button de alterar senha
        
        this.senhaAntigaLabel.setVisible(true);
        this.senhaAntigaText.setVisible(true);
        this.novaSenhaLabel.setVisible(true);
        this.novaSenhaText.setVisible(true);
        this.cancelarSenhaButton.setVisible(true);
        this.salvarSenhaButton.setVisible(true);
    }
    
    @FXML
    private void cancelarNovaSenha() {
        this.alterarSenhaButton.setVisible(true);//Deixando visivel o button alterarSenha
        
        this.senhaAntigaLabel.setVisible(false);
        this.senhaAntigaText.setVisible(false);
        this.novaSenhaLabel.setVisible(false);
        this.novaSenhaText.setVisible(false);
        this.cancelarSenhaButton.setVisible(false);
        this.salvarSenhaButton.setVisible(false);

        Formatter.limpar(senhaAntigaText, novaSenhaText);
    }
    
    @FXML
    private void salvarNovaSenha() {
        String senhaAtiga = senhaAntigaText.getText();
        String senhaNova = novaSenhaText.getText();
        
        //Caso a senha antiga esteja certa
        if (administradorSelecionado.getSenha().equals(senhaAtiga)) {
            this.alterarSenhaButton.setVisible(true);//Deixando visivel o button
            
            
            Dialogo.Resposta resposta = Alerta.confirmar("Deseja alterar a senha do Administrador?");
            
            if (resposta == Dialogo.Resposta.YES) {
                this.administradorSelecionado.setSenha(senhaNova);
                
                try {
                    if (ControleDAO.getBanco().getAdministradorDAO().editar(administradorSelecionado)) {
                        Alerta.info("Senha alterada com sucesso!");
                    }
                } catch (SQLException e) {
                    Alerta.erro(e.toString());
                }
            }
            
            Formatter.limpar(senhaAntigaText, novaSenhaText);
            
            this.senhaAntigaLabel.setVisible(false);
            this.senhaAntigaText.setVisible(false);
            this.novaSenhaLabel.setVisible(false);
            this.novaSenhaText.setVisible(false);
            this.cancelarSenhaButton.setVisible(false);
            this.salvarSenhaButton.setVisible(false);
            
        } else {//Caso a senha antiga esteja errada
            chamarAlerta("Senha antiga incorreta");
            Formatter.limpar(senhaAntigaText);
            Platform.runLater(() -> senhaAntigaText.requestFocus());//Colocando o Foco
        }
    }
    
    @FXML
    private void cancelarOperacao() {
        TelaInicialController telaInicial = new TelaInicialController(painelPrincipal);
        this.adicionarPainelInterno(telaInicial);
    }
    
    private void atualizarTabela() {
        //Transforma a lista em uma Lista Observavel
        ObservableList data = FXCollections.observableArrayList(administradorList);

        this.nomeColumn.setCellValueFactory(new PropertyValueFactory<>("nome"));//Adiciona o valor da variavel Nome
        this.rgColumn.setCellValueFactory(new PropertyValueFactory<>("rg"));//Adiciona o valor da variavel Endereco
        this.cpfColumn.setCellValueFactory(new PropertyValueFactory<>("cpf"));//Adiciona o valor da variavel Telefone
        this.enderecoColumn.setCellValueFactory(new PropertyValueFactory<>("endereco"));//Adiciona o valor da variavel Telefone

        this.administradoresTable.setItems(data);//Adiciona a lista de clientes na Tabela
    }
    
    private void sincronizarBancoDados() {
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
                    administradorList = this.get();
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
