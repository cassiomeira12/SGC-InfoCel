/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import app.PainelController;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author cassio
 */
public class LoginController extends AnchorPane {
    
    private AnchorPane painelInterno;
    
    @FXML
    private TextField usuarioText;
    @FXML
    private PasswordField senhaPassword;
    
    
    public LoginController(AnchorPane painelInterno) {
        
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
        TelaInicialController telaInicial = new TelaInicialController(painelInterno);
        this.adicionarPainelInterno(telaInicial);
    }
    
    private void adicionarPainelInterno(AnchorPane novaTela) {
        this.painelInterno.getChildren().clear();
        this.painelInterno.getChildren().add(novaTela);
    }
}
