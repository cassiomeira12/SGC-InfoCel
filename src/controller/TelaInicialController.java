/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import util.DateUtils;

/**
 * FXML Controller class
 *
 * @author cassio
 */
public class TelaInicialController extends AnchorPane {
    
    private BorderPane painelPrincipal;

   
    @FXML
    private TableView relatorioTableView;
    @FXML
    private Label dataLabel;
    @FXML
    private Label dinheiroLabel;
    
  
    public TelaInicialController(BorderPane painelPrincipal) {
        this.painelPrincipal = painelPrincipal;
        
        try {
            FXMLLoader fxml = new FXMLLoader(getClass().getResource("/view/TelaInicial.fxml"));
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
        this.dataLabel.setText(DateUtils.formatDateExtenso(LocalDate.now()));
    }
    
    private void adicionarPainelInterno(AnchorPane novaTela) {
        this.painelPrincipal.getCenter().setVisible(true);
        this.painelPrincipal.setCenter(novaTela);
    }
    
    @FXML
    private void chamarTelaAdicionarManutencao() {
        TelaAdicionarManutencaoController telaAdicionarManutencao = new TelaAdicionarManutencaoController(painelPrincipal);
        this.adicionarPainelInterno(telaAdicionarManutencao);
    }
    
    @FXML
    private void chamarTelaAdicionarReceita() {
        TelaAdicionarReceitaController telaAdicionarReceita = new TelaAdicionarReceitaController(painelPrincipal);
        this.adicionarPainelInterno(telaAdicionarReceita);
    }
    
    @FXML
    private void chamarTelaAdicionarSaida() {
        TelaAdicionarSaidaController telaAdicionarSaida = new TelaAdicionarSaidaController(painelPrincipal);
        this.adicionarPainelInterno(telaAdicionarSaida);
    }
    
    @FXML
    private void chamarTelaAdicionarVenda() {
        TelaAdicionarVendaController telaAdicionarVenda = new TelaAdicionarVendaController(painelPrincipal);
        this.adicionarPainelInterno(telaAdicionarVenda);
    }
    
    
    
    
    
}
