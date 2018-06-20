/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import banco.ControleDAO;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javax.swing.SwingWorker;
import model.Marca;
import model.Produto;
import org.apache.log4j.Logger;
import util.Formatter;
import util.alerta.Alerta;

/**
 * FXML Controller class
 *
 * @author cassio
 */
public class TelaSelecionarProdutoController extends AnchorPane {

    private Stage palco;
    public boolean RESULTADO = false;
    private Produto produtoSelecionado;
    private float quantidadeSelecionada;

    private List<Produto> listaProdutos;

    @FXML
    private TextField pesquisaText;
    @FXML
    private TableView<Produto> produtosTable;
    @FXML
    private TableColumn<Produto, String> descricaoColumn;
    @FXML
    private TableColumn<Marca, String> marcaColumn;
    @FXML
    private TableColumn<Float, String> quantidadeColumn;
    @FXML
    private TableColumn<Float, String> precoColumn;

    @FXML
    private ComboBox<Float> quantidadeComboBox;
    @FXML
    private Button adicionarButton;

    public TelaSelecionarProdutoController(Stage palco) {
        this.palco = palco;

        try {
            FXMLLoader fxml = new FXMLLoader(getClass().getResource("/view/SelecionarProdutoVenda.fxml"));
            fxml.setRoot(this);
            fxml.setController(this);
            fxml.load();
        } catch (IOException ex) {
            //Logger.getLogger(getClass()).error(ex);
            ex.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        //Desativa os Botoes de Editar e Excluir quando nenhum item na tabela esta selecionado
        adicionarButton.disableProperty().bind(produtosTable.getSelectionModel().selectedItemProperty().isNull());

        Formatter.toUpperCase(pesquisaText);

        pesquisaText.textProperty().addListener((obs, old, novo) -> {
            filtro(novo, listaProdutos, produtosTable);
        });

        this.sincronizarBancoDados();
        
        produtosTable.setRowFactory(tv -> new TableRow<Produto>() {
            @Override
            public void updateItem(Produto item, boolean empty) {
                super.updateItem(item, empty);
                
                if (item == null) {
                    setStyle("");
                } else if (item.getEstoque() > 0f) {
                    setStyle("-fx-border-color: #8BC34A;");
                } else {
                    setStyle("-fx-border-color: #F44336;");
                }
                
            }
        });

        produtosTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Produto produto = produtosTable.getSelectionModel().getSelectedItem();
                float quantidade = produto.getEstoque();
                List<Float> lista = new ArrayList<>();
                for (int i = 1; i <= quantidade; i++) {
                    lista.add((float) i);
                }
                quantidadeComboBox.setItems(FXCollections.observableArrayList(lista));
                quantidadeComboBox.getSelectionModel().select(0);
            }
        });
    }

    @FXML
    private void adicionarProduto() {
        this.RESULTADO = true;
        this.produtoSelecionado = produtosTable.getSelectionModel().getSelectedItem();
        this.quantidadeSelecionada = quantidadeComboBox.getValue();
        this.palco.close();
    }

    @FXML
    private void cancelarOperacao() {
        this.RESULTADO = false;
        this.palco.close();
    }

    private void filtro(String texto, List lista, TableView tabela) {
        ObservableList data = FXCollections.observableArrayList(lista);

        FilteredList<Produto> dadosFiltrados = new FilteredList(data, filtro -> true);

        dadosFiltrados.setPredicate(filtro -> {
            if (texto == null || texto.isEmpty()) {
                return true;
            }
            //Coloque aqui as verificacoes da Pesquisa
            if (filtro.getDescricao().toLowerCase().contains(texto.toLowerCase())) {
                return true;
            }
            if (filtro.getMarca().getDescricao().toLowerCase().contains(texto.toLowerCase())) {
                return true;
            }

            return false;
        });

        SortedList dadosOrdenados = new SortedList(dadosFiltrados);
        dadosOrdenados.comparatorProperty().bind(tabela.comparatorProperty());
        tabela.setItems(dadosOrdenados);
    }

    private void atualizarTabela() {
        //Transforma a lista em uma Lista Observavel
        ObservableList data = FXCollections.observableArrayList(listaProdutos);

        this.descricaoColumn.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        this.marcaColumn.setCellValueFactory(new PropertyValueFactory<>("marca"));
        this.quantidadeColumn.setCellValueFactory(new PropertyValueFactory<>("estoque"));
        this.precoColumn.setCellValueFactory(new PropertyValueFactory<>("precoVenda"));

        this.produtosTable.setItems(data);//Adiciona a lista de clientes na Tabela
    }

    private void sincronizarBancoDados() {
        //Metodo executado numa Thread separada
        SwingWorker<List, List> worker = new SwingWorker<List, List>() {
            @Override
            protected List<Produto> doInBackground() throws Exception {
                return ControleDAO.getBanco().getProdutoDAO().listar();
            }
            
            //Metodo chamado apos terminar a execucao numa Thread separada
            @Override
            protected void done() {
                super.done(); //To change body of generated methods, choose Tools | Templates.
                try {
                    listaProdutos = this.get();
                    atualizarTabela();
                } catch (InterruptedException | ExecutionException ex) {
                    Logger.getLogger(getClass()).error(ex);
                    chamarAlerta("Erro ao consultar Banco de Dados");
                    ex.printStackTrace();
                }
            }
        };

        worker.execute();
    }

    private void chamarAlerta(String mensagem) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Alerta.erro(mensagem);
            }
        });
    }

    public Produto getProduto() {
        return this.produtoSelecionado;
    }

    public float getQuantidade() {
        return this.quantidadeSelecionada;
    }

}
