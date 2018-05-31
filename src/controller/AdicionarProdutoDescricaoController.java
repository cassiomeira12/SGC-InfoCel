package controller;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.CategoriaProduto;
import model.Marca;
import model.UnidadeMedida;
import util.Formatter;

public class AdicionarProdutoDescricaoController extends AnchorPane {
    
    private Stage palco;
    public boolean RESULTADO = false;
    private Tipo tipo;
    private boolean inserir;
    
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
    
    
    public AdicionarProdutoDescricaoController(Stage palco, Tipo tipo, boolean inserir) {
        this.palco = palco;
        this.tipo = tipo;
        this.RESULTADO = false;
        this.inserir = inserir;
        
        try {
            FXMLLoader fxml = new FXMLLoader(getClass().getResource("/view/AdicionarProdutosDescricao.fxml"));
            fxml.setRoot(this);
            fxml.setController(this);
            fxml.load();
        } catch (IOException ex) {
            System.out.println("[ERRO] : Erro na tela Adicionar Produtos Descricao");
            ex.printStackTrace();
        }
    }
    
    public enum Tipo {
        CATEGORIA, MARCA, UNIDADE
    }
    
    @FXML
    public void initialize() {
        Formatter.toUpperCase(descricaoText, abreviacaoText);
        
        switch (tipo) {
            case CATEGORIA:
                abreviacaoBox.setVisible(false);
                salvarButton.disableProperty().bind(descricaoText.textProperty().isEmpty());
                break;
            case MARCA:
                abreviacaoBox.setVisible(false);
                salvarButton.disableProperty().bind(descricaoText.textProperty().isEmpty());
                break;
            case UNIDADE:
                salvarButton.disableProperty().bind(descricaoText.textProperty().isEmpty().or(abreviacaoText.textProperty().isEmpty()));
                break;
        }
        
        if (tipo == Tipo.CATEGORIA || tipo == Tipo.MARCA) {
            descricaoText.setOnKeyReleased((KeyEvent key) -> {
                if (key.getCode() == KeyCode.ENTER) {
                    salvar();
                }
            });
        } else {
            abreviacaoText.setOnKeyReleased((KeyEvent key) -> {
                if (key.getCode() == KeyCode.ENTER) {
                    salvar();
                }
            });
        }
    }
    
    @FXML
    private void cancelarOperacao() {
        this.palco.close();
    }
    
    @FXML
    private void salvar() {
        String descricao = descricaoText.getText();
        String abreviacao = abreviacaoText.getText();
        switch (tipo) {
            case CATEGORIA:
                if (inserir) {
                    categoriaProduto = new CategoriaProduto(null, descricao);
                } else {
                    categoriaProduto.setDescricao(descricao);
                }
                this.RESULTADO = true;
                this.palco.close();
                break;
            case MARCA:
                if (inserir) {
                    marca = new Marca(null, descricao);
                } else {
                    marca.setDescricao(descricao);
                }
                this.RESULTADO = true;
                this.palco.close();
                break;
            case UNIDADE:
                if (inserir) {
                    unidadeMedida = new UnidadeMedida(null, descricao, abreviacao);
                } else {
                    unidadeMedida.setAbreviacao(abreviacao);
                    unidadeMedida.setDescricao(descricao);
                }
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

    public void setCategoriaProduto(CategoriaProduto categoriaProduto) {
        this.categoriaProduto = categoriaProduto;
        this.descricaoText.setText(categoriaProduto.getDescricao());
    }

    public void setMarca(Marca marca) {
        this.marca = marca;
        this.descricaoText.setText(marca.getDescricao());
    }

    public void setUnidadeMedida(UnidadeMedida unidadeMedida) {
        this.unidadeMedida = unidadeMedida;
        this.descricaoText.setText(unidadeMedida.getDescricao());
        this.abreviacaoText.setText(unidadeMedida.getAbreviacao());
    }
    
}
