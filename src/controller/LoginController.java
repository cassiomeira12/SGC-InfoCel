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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javax.swing.SwingWorker;
import model.Administrador;

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
            System.out.println("[ERRO] : Erro na tela de Login");
            System.out.println(ex.toString());
        }
    }

    @FXML
    public void initialize() {
        // TODO
        stackPane.getChildren().add(indicator);
        indicator.setVisible(false);
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
                dao = new ControleDAO();
                return dao.getLoginDAO().autenticarLogin(login);
            }

            //Método chamado após terminar a execução numa Thread searada
            @Override
            protected void done() {
                super.done(); //To change body of generated methods, choose Tools | Templates.
                try {
                    //se autenticou login
                    if (get()) {
                        autenticarSenha(login, senha);
                    } else {
                        System.out.println("Login não encontrado");
                    }
                } catch (Exception e) {
                    System.out.println("Login não encontrado");
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
                if (dao.getLoginDAO().autenticarSenha(login, senha)) {
                    admLogado = dao.getLoginDAO().administradorLogado(login);
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
                        System.out.println("Senha incorreta");
                    }
                } catch (Exception e) {
                    System.out.println("Ocorreu um erro: " + e);
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

}
