package controller;

import java.io.IOException;
import javafx.application.Platform;
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
import model.Bairro;
import model.Cidade;
import org.apache.log4j.Logger;
import util.Formatter;

public class AdicionarCidadeBairroController extends AnchorPane {
    
    private Stage palco;
    public boolean RESULTADO = false;
    private Tipo tipo;
    private boolean inserir;
    
    private Cidade cidade;
    private Bairro bairro;
    
    @FXML
    private Label tituloLabel;
    
    @FXML
    private Button salvarButton;
    @FXML
    private TextField cidadeText;
    
    @FXML
    private VBox bairroBox;
    @FXML
    private TextField bairroText;
    
    
    public AdicionarCidadeBairroController(Stage palco, Tipo tipo, boolean inserir) {
        this.palco = palco;
        this.tipo = tipo;
        this.RESULTADO = false;
        this.inserir = inserir;
        
        try {
            FXMLLoader fxml = new FXMLLoader(getClass().getResource("/view/AdicionarCidadeBairro.fxml"));
            fxml.setRoot(this);
            fxml.setController(this);
            fxml.load();
        } catch (IOException ex) {
            //Logger.getLogger(getClass()).error(ex);
            ex.printStackTrace();
        }
    }
    
    public enum Tipo {
        CIDADE, BAIRRO
    }
    
    @FXML
    public void initialize() {
        Formatter.toUpperCase(cidadeText, bairroText);
        
        switch (tipo) {
            case CIDADE:
                bairroBox.setVisible(false);
                salvarButton.disableProperty().bind(cidadeText.textProperty().isEmpty());
                break;
            case BAIRRO:
                cidadeText.setDisable(true);
                bairroBox.setVisible(true);
                salvarButton.disableProperty().bind(cidadeText.textProperty().isEmpty().or(
                                                    bairroText.textProperty().isEmpty()));
                Platform.runLater(() -> bairroText.requestFocus());//Colocando o Foco
                break;
        }
        
        if (tipo == Tipo.CIDADE) {
            cidadeText.setOnKeyReleased((KeyEvent key) -> {
                if (key.getCode() == KeyCode.ENTER) {
                    salvar();
                }
            });
        } else if (tipo == Tipo.BAIRRO) {
            bairroText.setOnKeyReleased((KeyEvent key) -> {
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
        String cidade = cidadeText.getText();
        String bairro = bairroText.getText();
        switch (tipo) {
            case CIDADE:
                if (inserir) {
                    this.cidade = new Cidade(null, cidade);
                } else {
                    this.cidade.setNome(cidade);
                }
                this.RESULTADO = true;
                this.palco.close();
                break;
            case BAIRRO:
                if (inserir) {
                    this.bairro = new Bairro(null, bairro, this.cidade);
                } else {
                    this.bairro.setNome(bairro);
                }
                this.RESULTADO = true;
                this.palco.close();
                break;
        }
    }

    public Cidade getCidade() {
        return cidade;
    }

    public Bairro getBairro() {
        return bairro;
    }
    
    public void setCidade(Cidade cidade) {
        this.cidade = cidade;
        this.cidadeText.setText(cidade.getNome());
    }

    public void setBairro(Bairro bairro) {
        this.bairro = bairro;
        this.bairroText.setText(bairro.getNome());
    }
    
    public void setTitulo(String titulo) {
        this.tituloLabel.setText(titulo);
    }
    
}
