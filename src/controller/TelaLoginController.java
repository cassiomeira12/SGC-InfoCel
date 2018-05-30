/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import banco.ControleDAO;
import java.io.IOException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
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
import util.Formatter;
import util.alerta.Alerta;

/**
 * FXML Controller class
 *
 * @author cassio
 */
public class TelaLoginController extends AnchorPane {

    private BorderPane painelPrincipal;
    private ControleDAO dao;
    public static Administrador admLogado;

    @FXML
    private TextField usuarioText;
    @FXML
    private PasswordField senhaPassword;
    @FXML
    private Button entrarButton;
    @FXML
    private StackPane stackPane;
    private ProgressIndicator indicator = new ProgressIndicator();

    public TelaLoginController(BorderPane painelPrincipal) {

        this.painelPrincipal = painelPrincipal;

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

        usuarioText.setTextFormatter(Formatter.ALFA_NUMERICO());
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

        boolean vazio = Formatter.isEmpty(usuarioText, senhaPassword);

        String login = usuarioText.getText();
        String senha = senhaPassword.getText();

        if (vazio) {
            Alerta.alerta("Preencha os campos vazios", "Campos vazios");
        } else {
            indicator.setVisible(true);
            entrarButton.setDisable(true);
            usuarioText.setDisable(true);
            senhaPassword.setDisable(true);
            autenticarLogin(login, senha);
            //PainelInterno.telaInicial(painelPrincipal);
            //painelPrincipal.getTop().setVisible(true);//Deixando a Barra de menu visivel
        }
    }

    private void autenticarLogin(String login, String senha) {
        //Metodo executado numa Thread separada
        SwingWorker<Boolean, Boolean> worker = new SwingWorker<Boolean, Boolean>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                if (ControleDAO.getBanco().getLoginDAO().autenticarLogin(login)) {
                    admLogado = ControleDAO.getBanco().getLoginDAO().administradorLogado(login);
                    return true;
                } else {
                    return false;
                }
            }

            //Metodo chamado apos terminar a execucao numa Thread separada
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

                        entrarButton.setDisable(false);
                        usuarioText.setDisable(false);
                        senhaPassword.setDisable(false);
                    }
                } catch (Exception e) {
                    chamarAlerta("Erro", "Usuário não encontrado");
                    System.out.println("[ERRO]: " + e.toString());
                    limparLogin();//Apaga o texto que esta no TextField de Login
                    limparSenha();//Apaga o texto que esta no TextField de Login

                    entrarButton.setDisable(false);
                    usuarioText.setDisable(false);
                    senhaPassword.setDisable(false);
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

                        entrarButton.setDisable(false);
                        usuarioText.setDisable(false);
                        senhaPassword.setDisable(false);
                    }
                } catch (Exception e) {
                    chamarAlerta("Erro", "Ocorreu um erro ao realizar o Login");
                    System.out.println("[ERRO]: " + e);
                    limparSenha();//Apaga o texto que esta no TextField de Login

                    entrarButton.setDisable(false);
                    usuarioText.setDisable(false);
                    senhaPassword.setDisable(false);
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
                TelaInicialController telaInicial = new TelaInicialController(painelPrincipal);
                adicionarPainelInterno(telaInicial);
                painelPrincipal.getTop().setVisible(true);//Deixando a Barra de menu visivel
            }
        });
    }

    private void adicionarPainelInterno(AnchorPane novaTela) {
        this.painelPrincipal.setCenter(novaTela);
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
