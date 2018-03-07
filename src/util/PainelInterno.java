/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import controller.TelaAdicionarManutencaoController;
import controller.TelaAdicionarReceitaController;
import controller.TelaAdicionarSaidaController;
import controller.TelaAdicionarVendaController;
import controller.TelaInicialController;
import controller.TelaLoginController;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

/**
 *
 * @author cassio
 */
public class PainelInterno {
    
    //public static BorderPane painel;
    
    private static TelaLoginController login;
    private static TelaInicialController telaInicial;
    private static TelaAdicionarVendaController adicionarVenda;
    private static TelaAdicionarManutencaoController adicionarManutencao;
    private static TelaAdicionarReceitaController adicionarReceita;
    private static TelaAdicionarSaidaController adicionarSaida;
    
    private static void trocarPainel(BorderPane antigo, AnchorPane novo) {
        antigo.getChildren().clear();
        antigo.setCenter(novo);
    }
    
    
    
    public static void telaLogin(BorderPane painel) {
        login = new TelaLoginController(painel);
        trocarPainel(painel, login);
    }
    
    public static void telaInicial(BorderPane painel) {
        telaInicial = new TelaInicialController(painel);
        trocarPainel(painel, telaInicial);
    }
    
    public static void telaAdicionarVenda(BorderPane painel) {
        adicionarVenda = new TelaAdicionarVendaController(painel);
        trocarPainel(painel, adicionarVenda);
    }
    
    public static void telaAdicionarManutencao(BorderPane painel) {
        adicionarManutencao = new TelaAdicionarManutencaoController(painel);
        trocarPainel(painel, adicionarManutencao);
    }
    
    public static void telaAdicionarReceita(BorderPane painel) {
        adicionarReceita = new TelaAdicionarReceitaController(painel);
        trocarPainel(painel, adicionarReceita);
    }
    
    public static void telaAdicionarSaida(BorderPane painel) {
        adicionarSaida = new TelaAdicionarSaidaController(painel);
        trocarPainel(painel, adicionarSaida);
    }
    
}
