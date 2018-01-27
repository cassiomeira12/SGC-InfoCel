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
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuBar;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import model.Administrador;

/**
 * FXML Controller class
 *
 * @author cassio
 */
public class LoginController extends AnchorPane {
    
    private BorderPane painelInterno;
    
    @FXML
    private TextField usuarioText;
    @FXML
    private PasswordField senhaPassword;
    
    
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
    }
    
    @FXML
    private void logar() {
        String login = usuarioText.getText();
         String senha = senhaPassword.getText();
        
      ControleDAO dao = new ControleDAO();
      
       //Veifica se o login está no BD
      boolean encontrouLogin = dao.getLoginDAO().autenticarLogin(login);
      if(!encontrouLogin){
        System.out.println("Login não encontrado");
        return;
      }
      
        //Veifica se a senha está correta
      boolean autenticouSenha = dao.getLoginDAO().autenticarSenha(login, senha);
      if(!autenticouSenha){
        System.out.println("Senha incorreta");
        return;
      }
      
      Administrador admLogado = dao.getLoginDAO().administradorLogado(login);
      
      TelaInicialController telaInicial = new TelaInicialController(painelInterno);
      this.adicionarPainelInterno(telaInicial);
      this.painelInterno.getTop().setVisible(true);//Deixando a Barra de menu visivel
    }
    
    private void adicionarPainelInterno(AnchorPane novaTela) {
        this.painelInterno.setCenter(novaTela);
    }

}
