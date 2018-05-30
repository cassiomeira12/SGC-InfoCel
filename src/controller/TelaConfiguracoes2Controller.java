package controller;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class TelaConfiguracoes2Controller extends AnchorPane {
    
    private BorderPane painelPrincipal;
    
    @FXML
    private TabPane configuracoesTab;
    @FXML
    private Tab administradoresTab;
    @FXML
    private Tab pagamentoTab;
    @FXML
    private Tab produtosTab;
    
    
    public TelaConfiguracoes2Controller(BorderPane painelPrincipal) {
        this.painelPrincipal = painelPrincipal;
        
        try {
            FXMLLoader fxml = new FXMLLoader(getClass().getResource("/view/TelaConfiguracoes2.fxml"));
            fxml.setRoot(this);
            fxml.setController(this);
            fxml.load();
        } catch (IOException ex) {
            System.out.println("[ERRO] : Erro na tela Configuracoes");
            ex.printStackTrace();
        }
    }
    
    @FXML
    public void initialize() {
        
    }
    
    private void adicionarPainelInterno(AnchorPane novaTela) {
        this.painelPrincipal.setCenter(novaTela);
    }
    
    @FXML
    private void voltar() {
        //if (administradorPane.isVisible()) {
        //    administradorPane.setVisible(false);
        //    administradoresTable.setVisible(true);
        //} else {
            TelaInicialController telaInicial = new TelaInicialController(painelPrincipal);
            this.adicionarPainelInterno(telaInicial);
        //}
    }
    
    @FXML
    private void administradoresTab() {
        
    }
    
    @FXML
    private void pagamentoTab() {
        
    }
    
    @FXML
    private void produtosTab() throws IOException {
        HBox painel = FXMLLoader.load(getClass().getResource("/view/ProdutosConfiguracoes.fxml"));
        produtosTab.setContent(painel);
    }
    
}
