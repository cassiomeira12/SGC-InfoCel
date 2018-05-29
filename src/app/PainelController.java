/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

import controller.TelaLoginController;
import controller.TelaAdicionarProdutoController;
import controller.TelaConsultarClientesController;
import controller.TelaConsultarManutencoesController;
import controller.TelaConsultarProdutosController;
import controller.TelaAdicionarManutencaoController;
import controller.TelaRelatorioMensalController;
import controller.TelaSobreController;
import controller.TelaAdicionarVendaController;
import controller.TelaConsultarVendasController;
import controller.TelaRelatorioDiarioController;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import util.PainelInterno;

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
        TelaLoginController telaLogin = new TelaLoginController(painelPrincipal);
        this.adicionarPainelInterno(telaLogin);
        //PainelInterno.telaLogin(painelPrincipal);
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
        TelaLoginController telaLogin = new TelaLoginController(painelPrincipal);
        this.adicionarPainelInterno(telaLogin);
        this.barraMenu.setVisible(false);//Deixando a Barra de Menu invisivel
    }
    
    @FXML
    private void chamarTelaAdicionarManutencao() {
        TelaAdicionarManutencaoController telaAdicionarManutencao = new TelaAdicionarManutencaoController(painelPrincipal);
        this.adicionarPainelInterno(telaAdicionarManutencao);
    }
    
    @FXML
    private void chamarTelaAdicionarVenda() {
        TelaAdicionarVendaController telaAdicionarVenda = new TelaAdicionarVendaController(painelPrincipal);
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

    @FXML
    private void chamarTelaAdicionarProduto(ActionEvent event) {
        TelaAdicionarProdutoController telaAdicionarProduto = new TelaAdicionarProdutoController(painelPrincipal);
        this.adicionarPainelInterno(telaAdicionarProduto);
    }

    @FXML
    private void chamarTelaConsultarVendas(ActionEvent event) {
        TelaConsultarVendasController telaConsultarVenda = new TelaConsultarVendasController(painelPrincipal);
        this.adicionarPainelInterno(telaConsultarVenda);
    }

    @FXML
    private void chamarTelaRelatorioDiario(ActionEvent event) {
        TelaRelatorioDiarioController telaRelatorioDiario = new TelaRelatorioDiarioController(painelPrincipal);
        this.adicionarPainelInterno(telaRelatorioDiario);
    }

    @FXML
    private void chamarTelaAdministradores(ActionEvent event) {
       // PainelInterno.telaAdministradores(painelPrincipal);
    }

    @FXML
    private void chamarTelaBancoDeDados(ActionEvent event) {
        //PainelInterno.telaBancoDeDados(painelPrincipal);
    }

    @FXML
    private void chamarTelaConfiguracoes(ActionEvent event) {
        //PainelInterno.telaConfiguracoes(painelPrincipal);
    }

}
