/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import app.PainelController;
import banco.ControleDAO;
import banco.dao.DAO;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuBar;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javax.swing.SwingWorker;
import model.Administrador;
import util.Alerta;

/**
 * FXML Controller class
 *
 * @author cassio
 */
public class LoginController extends AnchorPane {

    private BorderPane painelInterno;
    private ControleDAO dao;
    private Administrador admLogado;

    @FXML
    private TextField usuarioText;
    @FXML
    private PasswordField senhaPassword;
    @FXML
    private StackPane stackPane;
    private ProgressIndicator indicator = new ProgressIndicator();

    public LoginController(BorderPane painelInterno) {

        this.painelInterno = painelInterno;

        try {
            FXMLLoader fxml = new FXMLLoader(getClass().getResource("/view/Login.fxml"));
            fxml.setRoot(this);
            fxml.setController(this);
            fxml.load();
        } catch (IOException ex) {
            this.chamarAlerta("Erro - Tela de Login", "Ocorreu um erro ao abrir a tela de Login");
            System.out.println(ex.toString());
        }
    }

    @FXML
    public void initialize() {
        // TODO
        stackPane.getChildren().add(indicator);
        indicator.setVisible(false);
        
        senhaPassword.setOnKeyReleased((KeyEvent key) -> {
            if (key.getCode() == KeyCode.ENTER) {
                logar();
            }
        });
    }

    @FXML
    private void logar() {
        String login = usuarioText.getText();
        String senha = senhaPassword.getText();

        indicator.setVisible(true);
        autenticarLogin(login, senha);
    }

    private void autenticarLogin(String login, String senha) {
        //método executado numa Thread separada
        SwingWorker<Boolean, Boolean> worker = new SwingWorker<Boolean, Boolean>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                return ControleDAO.getBanco().getLoginDAO().autenticarLogin(login);
            }

            //Método chamado após terminar a execução numa Thread searada
            @Override
            protected void done() {
                indicator.setVisible(false);
                super.done(); //To change body of generated methods, choose Tools | Templates.
                try {
                    //se autenticou login
                    if (get()) {
                        autenticarSenha(login, senha);
                    } else {
                        chamarAlerta("Erro", "Usuário não encontrado");
                        System.out.println("Login não encontrado");
                        limparLogin();//Apaga o texto que esta no TextField de Login
                        limparSenha();//Apaga o texto que esta no TextField de Login
                    }
                } catch (Exception e) {
                    chamarAlerta("Erro", "Usuário não encontrado");
                    System.out.println("[ERRO]: " + e.toString());
                    limparLogin();//Apaga o texto que esta no TextField de Login
                    limparSenha();//Apaga o texto que esta no TextField de Login
                }
                
            }
        };

        worker.execute();
    }

    private void autenticarSenha(String login, String senha) {
        //método executado numa Thread separada
        SwingWorker<Boolean, Boolean> worker = new SwingWorker<Boolean, Boolean>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                if (ControleDAO.getBanco().getLoginDAO().autenticarSenha(login, senha)) {
                    admLogado = ControleDAO.getBanco().getLoginDAO().administradorLogado(login);
                    return true;
                } else {
                    return false;
                }
            }

            //Método chamado após terminar a execução numa Thread searada
            @Override
            protected void done() {
                indicator.setVisible(false);
                super.done(); //To change body of generated methods, choose Tools | Templates.
                try {
                    //se autenticou senha
                    if (get()) {
                        abrirTelaInicial();
                    } else {
                        chamarAlerta("Erro", "Senha incorreta");
                        limparSenha();//Apaga o texto que esta no TextField de Login
                    }
                } catch (Exception e) {
                    chamarAlerta("Erro", "Ocorreu um erro ao realizar o Login");
                    System.out.println("[ERRO]: " + e);
                    limparSenha();//Apaga o texto que esta no TextField de Login
                }
                
            }

        };

        worker.execute();
    }

    private void abrirTelaInicial() {
        //forma usada para executar algo na Thread procipal, pois a manipulaçao das views deve ser feita na Thread principal
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                TelaInicialController telaInicial = new TelaInicialController(painelInterno);
                adicionarPainelInterno(telaInicial);
                painelInterno.getTop().setVisible(true);//Deixando a Barra de menu visivel
            }
        });
    }

    private void adicionarPainelInterno(AnchorPane novaTela) {
        this.painelInterno.setCenter(novaTela);
    }
    
    private void chamarAlerta(String titulo, String mensagem) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Alerta.erro(titulo, mensagem);
            }
        });
    }
    
    private void limparLogin() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                usuarioText.clear();
            }
        });
    }
    
    private void limparSenha() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                senhaPassword.clear();
            }
        });
    }
}
