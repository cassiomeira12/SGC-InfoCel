/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

import controller.*;
import java.io.BufferedInputStream;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;



import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

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
    private void chamarTelaConfiguracoes(ActionEvent event) {
        TelaConfiguracoes2Controller telaConfiguracoes = new TelaConfiguracoes2Controller(painelPrincipal);
        this.adicionarPainelInterno(telaConfiguracoes);
    }

    @FXML
    private void chamarTelaAdicionarReceita(ActionEvent event) {
        TelaAdicionarReceitaController tela = new TelaAdicionarReceitaController(painelPrincipal);
        this.adicionarPainelInterno(tela);
    }

    @FXML
    private void chamarTelaConsultarReceita(ActionEvent event) {
        TelaConsultarReceitasController tela = new TelaConsultarReceitasController(painelPrincipal);
        this.adicionarPainelInterno(tela);
    }

    @FXML
    private void chamarTelaAdicionarSaida(ActionEvent event) {
        TelaAdicionarSaidaController tela = new TelaAdicionarSaidaController(painelPrincipal);
        this.adicionarPainelInterno(tela);
    }

    @FXML
    private void chamarTelaConsultarSaida(ActionEvent event) {
        TelaConsultarSaidasController tela = new TelaConsultarSaidasController(painelPrincipal);
        this.adicionarPainelInterno(tela);
    }
    
    private static boolean downloadArquivoExcel() {
        InputStream is = null;
        BufferedInputStream buf = null;
        FileOutputStream out = null;
        boolean ok = false;
        try {
            URL url = new URL("ftp://User_Name:password@host_ftp/Nome_do_arquivo.xls");
            url.getHost();
            url.getFile();
            url.getPort();
            url.getUserInfo();
            URLConnection con = url.openConnection();
            buf = new BufferedInputStream(con.getInputStream());
            out = new FileOutputStream("c:\\excelDown.xls");
            int i = 0;
            byte[] bytesIn = new byte[1024];
            while ((i = buf.read(bytesIn)) >= 0) {
                out.write(bytesIn, 0, i);
            }
            ok = true;
        } catch (MalformedURLException ex) {
            //Logger.getLogger(TesteDownloadArqExcel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ioe) {
            //Logger.getLogger(TesteDownloadArqExcel.class.getName()).log(Level.SEVERE, null, ioe);
        } finally {
            try {
                if (buf != null) {
                    buf.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException ex) {
                //Logger.getLogger(TesteDownloadArqExcel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (ok) {
            return ok;
        }
        return false;
    }

}
