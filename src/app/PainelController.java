/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

import controller.LoginController;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

/**
 * FXML Controller class
 *
 * @author cassio
 */
public class PainelController implements Initializable {

    @FXML
    private MenuBar barraMenu;
    @FXML
    private BorderPane painelInterno;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
        this.barraMenu.setVisible(false);//Deixando a Barra de Menu invisivel
        
        LoginController telaLogin = new LoginController(painelInterno);
        this.adicionarPainelInterno(telaLogin);
        
       
        
    }
    
    private void adicionarPainelInterno(AnchorPane novaTela) {
        painelInterno.setCenter(novaTela);
    }

}
