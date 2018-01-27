/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

import controller.LoginController;
import controller.TelaManutencaoController;
import controller.TelaReceitaController;
import controller.TelaSaidaController;
import controller.TelaVendaController;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author cassio
 */
public class PainelController implements Initializable {
    
    private Stage palco;
    
    @FXML
    private MenuBar barraMenu;
    @FXML
    private BorderPane painelInterno;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.barraMenu.setVisible(false);//Deixando a Barra de Menu invisivel
        LoginController telaLogin = new LoginController(painelInterno);
        this.adicionarPainelInterno(telaLogin);
    }
    
    public void setStage(Stage palco) {
        this.palco = palco;
    }
    
    private void adicionarPainelInterno(AnchorPane novaTela) {
        painelInterno.setCenter(novaTela);
    }
    
    @FXML
    private void fecharPrograma() {
        System.exit(0);
    }
    
    @FXML
    private void sair() {
        this.barraMenu.setVisible(false);//Deixando a Barra de Menu invisivel
        LoginController telaLogin = new LoginController(painelInterno);
        this.adicionarPainelInterno(telaLogin);
    }
    
    @FXML
    private void chamarTelaAdicionarManutencao() {
        TelaManutencaoController telaAdicionarManutencao = new TelaManutencaoController(painelInterno);
        this.adicionarPainelInterno(telaAdicionarManutencao);
    }
    
    @FXML
    private void chamarTelaAdicionarVenda() {
        TelaVendaController telaAdicionarVenda = new TelaVendaController(painelInterno);
        this.adicionarPainelInterno(telaAdicionarVenda);
    }

}
