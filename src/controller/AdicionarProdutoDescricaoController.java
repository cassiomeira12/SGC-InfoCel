package controller;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.CategoriaProduto;
import model.Marca;
import model.UnidadeMedida;

public class AdicionarProdutoDescricaoController extends AnchorPane {
    
    private Stage palco;
    public boolean RESULTADO = false;
    private Tipo tipo;
    
    private CategoriaProduto categoriaProduto;
    private Marca marca;
    private UnidadeMedida unidadeMedida;
    
    @FXML
    private Label tituloLabel;
    
    @FXML
    private Button salvarButton;
    @FXML
    private TextField descricaoText;
    
    @FXML
    private VBox abreviacaoBox;
    @FXML
    private TextField abreviacaoText;
    
    
    public AdicionarProdutoDescricaoController(Stage palco, Tipo tipo) {
        this.palco = palco;
        this.tipo = tipo;

        try {
            FXMLLoader fxml = new FXMLLoader(getClass().getResource("/view/AdicionarProdutosDescricao.fxml"));
            fxml.setRoot(this);
            fxml.setController(this);
            fxml.load();
        } catch (IOException ex) {
            System.out.println("[ERRO] : Erro na tela Adicionar Produtos Descricao");
            System.out.println(ex.toString());
        }
    }
    
    public enum Tipo {
        CATEGORIA, MARCA, UNIDADE
    }
    
    @FXML
    public void initialize() {
        if (tipo == Tipo.CATEGORIA || tipo == Tipo.MARCA) {
            abreviacaoBox.setVisible(false);
            salvarButton.disableProperty().bind(descricaoText.textProperty().isEmpty());
        } else {
            salvarButton.disableProperty().bind(descricaoText.textProperty().isEmpty().or(abreviacaoText.textProperty().isEmpty()));
        }
    }
    
    @FXML
    private void cancelarOperacao() {
        this.RESULTADO = false;
        this.palco.close();
    }
    
    @FXML
    private void salvar() {
        String descricao = descricaoText.getText();
        String abreviacao = abreviacaoText.getText();
        switch (tipo) {
            case CATEGORIA:
                categoriaProduto = new CategoriaProduto(null, descricao);
                this.RESULTADO = true;
                this.palco.close();
                break;
            case MARCA:
                marca = new Marca(null, descricao);
                this.RESULTADO = true;
                this.palco.close();
                break;
            case UNIDADE:
                unidadeMedida = new UnidadeMedida(null, descricao, abreviacao);
                this.RESULTADO = true;
                this.palco.close();
                break;
        }
    }

    public CategoriaProduto getCategoriaProduto() {
        return categoriaProduto;
    }

    public Marca getMarca() {
        return marca;
    }

    public UnidadeMedida getUnidadeMedida() {
        return unidadeMedida;
    }
    
}
