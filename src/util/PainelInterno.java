/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import controller.*;
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
    
    //private static TelaConsultarVendasController consultarVendas;
    private static TelaAdicionarProdutoController adicionarProduto;
    private static TelaConsultarProdutosController consultarProdutos;
    private static TelaConsultarManutencoesController consultarManutencoes;
    private static TelaConsultarClientesController consultarClientes;
    private static TelaRelatorioDiarioController relatorioDiario;
    private static TelaRelatorioMensalController relatorioMensal;
    private static TelaAdministradoresController administradores;
    private static TelaBackupRecuperacaoController bancoDeDados;
    private static TelaConfiguracoesController configuracoes;
    private static TelaSobreController sobre;
    
    private static void trocarPainel(BorderPane antigo, AnchorPane novo) {
        //antigo.getChildren().clear();
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
    
    
    public static void telaConsultarVendas(BorderPane painel) {
        //consultarVendas = new TelaConsultarVendas(painel);
        //trocarPainel(painel, consultarVendas);
    }
    
    public static void telaAdicionarProduto(BorderPane painel) {
        adicionarProduto = new TelaAdicionarProdutoController(painel);
        trocarPainel(painel, adicionarProduto);
    }
    
    public static void telaConsultarProdutos(BorderPane painel) {
        consultarProdutos = new TelaConsultarProdutosController(painel);
        trocarPainel(painel, consultarProdutos);
    }
    
    public static void telaConsultarManutencoes(BorderPane painel) {
        consultarManutencoes = new TelaConsultarManutencoesController(painel);
        trocarPainel(painel, consultarManutencoes);
    }
    
    public static void telaConsultarClientes(BorderPane painel) {
        consultarClientes = new TelaConsultarClientesController(painel);
        trocarPainel(painel, consultarClientes);
    }
    
    public static void telaRelatorioDiario(BorderPane painel) {
        relatorioDiario = new TelaRelatorioDiarioController(painel);
        trocarPainel(painel, relatorioDiario);
    }
    
    public static void telaRelatorioMensal(BorderPane painel) {
        relatorioMensal = new TelaRelatorioMensalController(painel);
        trocarPainel(painel, relatorioMensal);
    }
    
    public static void telaAdministradores(BorderPane painel) {
        administradores = new TelaAdministradoresController(painel);
        trocarPainel(painel, administradores);
    }
    
    public static void telaBancoDeDados(BorderPane painel) {
        bancoDeDados = new TelaBackupRecuperacaoController(painel);
        trocarPainel(painel, bancoDeDados);
    }
    
    public static void telaConfiguracoes(BorderPane painel) {
        configuracoes = new TelaConfiguracoesController(painel);
        trocarPainel(painel, configuracoes);
    }
    
    public static void telaSobre(BorderPane painel) {
        sobre = new TelaSobreController(painel);
        trocarPainel(painel, sobre);
    }
}
