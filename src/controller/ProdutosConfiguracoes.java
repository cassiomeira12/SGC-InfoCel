package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class ProdutosConfiguracoes implements Initializable {

    @FXML
    private TableView<?> categoriaTable;
    @FXML
    private TableColumn<?, ?> descricaoCategoriaColumn;
    @FXML
    private Button removerCategoriaButton;
    @FXML
    private TableView<?> marcaTable;
    @FXML
    private TableColumn<?, ?> descricaoMarcaColumn;
    @FXML
    private Button removerMarcaButton;
    @FXML
    private TableView<?> unidadesTable;
    @FXML
    private TableColumn<?, ?> abreviaturaUnidadeColumn;
    @FXML
    private TableColumn<?, ?> descricaoUnidadeColumn;
    @FXML
    private Button removerUnidadeButton;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void removerCategoria(ActionEvent event) {
    }

    @FXML
    private void removerMarca(ActionEvent event) {
    }

    @FXML
    private void removerUnidade(ActionEvent event) {
    }
    
}
