/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

import controller.LoginController;
import controller.TelaConsultarClientesController;
import controller.TelaConsultarManutencoesController;
import controller.TelaConsultarProdutosController;
import controller.TelaManutencaoController;
import controller.TelaRelatorioMensalController;
import controller.TelaSobreController;
import controller.TelaVendaController;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
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
    private BorderPane painelPrincipal;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.barraMenu.setVisible(false);//Deixando a Barra de Menu invisivel
        LoginController telaLogin = new LoginController(painelPrincipal);
        this.adicionarPainelInterno(telaLogin);
    }
    
    public void setStage(Stage palco) {
        this.palco = palco;
    }
    
    private void adicionarPainelInterno(AnchorPane novaTela) {
        painelPrincipal.setCenter(novaTela);
    }
    
    @FXML
    private void fecharPrograma() {
        System.exit(0);
    }
    
    @FXML
    private void sair() {
        this.barraMenu.setVisible(false);//Deixando a Barra de Menu invisivel
        LoginController telaLogin = new LoginController(painelPrincipal);
        this.adicionarPainelInterno(telaLogin);
    }
    
    @FXML
    private void chamarTelaAdicionarManutencao() {
        TelaManutencaoController telaAdicionarManutencao = new TelaManutencaoController(painelPrincipal);
        this.adicionarPainelInterno(telaAdicionarManutencao);
    }
    
    @FXML
    private void chamarTelaAdicionarVenda() {
        TelaVendaController telaAdicionarVenda = new TelaVendaController(painelPrincipal);
        this.adicionarPainelInterno(telaAdicionarVenda);
    }

    @FXML
    private void chamarTelaConsultarProdutos(ActionEvent event) {
        TelaConsultarProdutosController telaConsultarProdutos = new TelaConsultarProdutosController(painelPrincipal);
        this.adicionarPainelInterno(telaConsultarProdutos);
    }

    @FXML
    private void chamarTelaConsultarManutencoes(ActionEvent event) {
        TelaConsultarManutencoesController telaConsultarManutencoes = new TelaConsultarManutencoesController(painelPrincipal);
        this.adicionarPainelInterno(telaConsultarManutencoes);
    }

    @FXML
    private void chamarTelaConsultarClientes(ActionEvent event) {
        TelaConsultarClientesController telaConsultarClientes = new TelaConsultarClientesController(painelPrincipal);
        this.adicionarPainelInterno(telaConsultarClientes);
    }

    @FXML
    private void chamarTelaRelatorioMensal(ActionEvent event) {
        TelaRelatorioMensalController telaRelatorioMensal = new TelaRelatorioMensalController(painelPrincipal);
        this.adicionarPainelInterno(telaRelatorioMensal);
    }

    @FXML
    private void chamarTelaSobre(ActionEvent event) {
        TelaSobreController telaSobre = new TelaSobreController(painelPrincipal);
        this.adicionarPainelInterno(telaSobre);
    }

}
