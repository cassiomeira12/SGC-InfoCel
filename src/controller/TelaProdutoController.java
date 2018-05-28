/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author cassio
 */
public class TelaProdutoController extends AnchorPane {
    
    private Stage palco;

    public TelaProdutoController(Stage palco) {
        this.palco = palco;
        
        try {
            FXMLLoader fxml = new FXMLLoader(getClass().getResource("/view/TelaProduto.fxml"));
            fxml.setRoot(this);
            fxml.setController(this);
            fxml.load();
        } catch (IOException ex) {
            System.out.println("[ERRO] : Erro na tela Cliente");
            System.out.println(ex.toString());
        }
    }

    @FXML
    public void initialize() {
        // TODO
    }
    
    @FXML
    private void adicionarProduto() {
        
    }

    @FXML
    private void cancelarOperacao() {
        palco.close();
    }
    
}
