package controller;

import banco.ControleDAO;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javax.swing.SwingWorker;
import model.CategoriaProduto;
import model.Marca;
import model.UnidadeMedida;
import util.alerta.Alerta;
import util.alerta.Dialogo;
import controller.AdicionarProdutoDescricaoController.Tipo;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.StageStyle;

public class ProdutosConfiguracoes implements Initializable {
    
    private List<CategoriaProduto> listaCategoriaProdutos;
    private List<Marca> listaMarcas;
    private List<UnidadeMedida> listaUnidadeMedidas;
    
    @FXML
    private TableView<CategoriaProduto> categoriaTable;
    @FXML
    private TableColumn<CategoriaProduto, String> descricaoCategoriaColumn;
    @FXML
    private Button removerCategoriaButton;
    @FXML
    private TableView<Marca> marcaTable;
    @FXML
    private TableColumn<Marca, String> descricaoMarcaColumn;
    @FXML
    private Button removerMarcaButton;
    @FXML
    private TableView<UnidadeMedida> unidadesTable;
    @FXML
    private TableColumn<UnidadeMedida, String> abreviaturaUnidadeColumn;
    @FXML
    private TableColumn<UnidadeMedida, String> descricaoUnidadeColumn;
    @FXML
    private Button removerUnidadeButton;
    @FXML
    private Button editarCategoriaButton;
    @FXML
    private Button editarMarcaButton;
    @FXML
    private Button editarUnidadeButton;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        removerCategoriaButton.disableProperty().bind(categoriaTable.getSelectionModel().selectedItemProperty().isNull());
        removerMarcaButton.disableProperty().bind(marcaTable.getSelectionModel().selectedItemProperty().isNull());
        removerUnidadeButton.disableProperty().bind(unidadesTable.getSelectionModel().selectedItemProperty().isNull());

        sincronizarBancoDadosCategoria();
        sincronizarBancoDadosMarca();
        sincronizarBancoDadosUnidade();
        
    }    

    @FXML
    private void adicionarCategoria(ActionEvent event) {
        Stage palco = new Stage();
        palco.initModality(Modality.APPLICATION_MODAL);//Impede de clicar na tela em plano de fundo
        palco.centerOnScreen();
        palco.initStyle(StageStyle.UNDECORATED);//Remove a barra de menu
        
        AdicionarProdutoDescricaoController adicionarCategoria = new AdicionarProdutoDescricaoController(palco, Tipo.CATEGORIA);
        palco.setScene(new Scene(adicionarCategoria));
        palco.showAndWait();
        
        CategoriaProduto novaCategoria = adicionarCategoria.getCategoriaProduto();
        
        Long id = null;
        try {
            id = ControleDAO.getBanco().getCategoriaProdutoDAO().inserir(novaCategoria);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (id == null) {//Erro ao inserir item no Banco de Dados
            Alerta.erro("Erro ao criar nova Categoria de Produto");
        } else {
            novaCategoria.setId(id);
        }
    }

    @FXML
    private void adicionarMarca(ActionEvent event) {
        Stage palco = new Stage();
        palco.initModality(Modality.APPLICATION_MODAL);//Impede de clicar na tela em plano de fundo
        palco.centerOnScreen();
        palco.initStyle(StageStyle.UNDECORATED);//Remove a barra de menu
        
        AdicionarProdutoDescricaoController adicionarMarca = new AdicionarProdutoDescricaoController(palco, Tipo.MARCA);
        palco.setScene(new Scene(adicionarMarca));
        palco.showAndWait();
        
        Marca novaMarca = adicionarMarca.getMarca();
        
        Long id = null;
        try {
            id = ControleDAO.getBanco().getMarcaDAO().inserir(novaMarca);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (id == null) {//Erro ao inserir item no Banco de Dados
            Alerta.erro("Erro ao criar nova Marca");
        } else {
            novaMarca.setId(id);
        }
    }

    @FXML
    private void adicionarUnidade(ActionEvent event) {
        Stage palco = new Stage();
        palco.initModality(Modality.APPLICATION_MODAL);//Impede de clicar na tela em plano de fundo
        palco.centerOnScreen();
        palco.initStyle(StageStyle.UNDECORATED);//Remove a barra de menu
        
        AdicionarProdutoDescricaoController adicionarUnidade = new AdicionarProdutoDescricaoController(palco, Tipo.UNIDADE);
        palco.setScene(new Scene(adicionarUnidade));
        palco.showAndWait();
        
        UnidadeMedida novaUnidade = adicionarUnidade.getUnidadeMedida();
        
        Long id = null;
        try {
            id = ControleDAO.getBanco().getUnidadeMedidaDAO().inserir(novaUnidade);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (id == null) {//Erro ao inserir item no Banco de Dados
            Alerta.erro("Erro ao criar nova Unidade de Medida");
        } else {
            novaUnidade.setId(id);
        }
    }
    
    @FXML
    private void editarCategoria(ActionEvent event) {
    }

    @FXML
    private void editarMarca(ActionEvent event) {
    }

    @FXML
    private void editarUnidade(ActionEvent event) {
    }
    
    @FXML
    private void removerCategoria(ActionEvent event) {
        CategoriaProduto categoria = categoriaTable.getSelectionModel().getSelectedItem();

        Dialogo.Resposta resposta = Alerta.confirmar("Excluir categoria " + categoria.getDescricao() + " ?");

        if (resposta == Dialogo.Resposta.YES) {
            try {
                ControleDAO.getBanco().getCategoriaProdutoDAO().excluir(categoria.getId().intValue());
            } catch (SQLException ex) {
                //Logger.getLogger(TelaConsultarClientesController.class.getName()).log(Level.SEVERE, null, ex);
                ex.printStackTrace();
            }
            this.sincronizarBancoDadosCategoria();
            this.atualizarTabelaCategoria();
        }

        categoriaTable.getSelectionModel().clearSelection();
    }

    @FXML
    private void removerMarca(ActionEvent event) {
        Marca marca = marcaTable.getSelectionModel().getSelectedItem();

        Dialogo.Resposta resposta = Alerta.confirmar("Excluir marca " + marca.getDescricao() + " ?");

        if (resposta == Dialogo.Resposta.YES) {
            try {
                ControleDAO.getBanco().getMarcaDAO().excluir(marca.getId().intValue());
            } catch (SQLException ex) {
                //Logger.getLogger(TelaConsultarClientesController.class.getName()).log(Level.SEVERE, null, ex);
                ex.printStackTrace();
            }
            this.sincronizarBancoDadosMarca();
            this.atualizarTabelaMarca();
        }

        marcaTable.getSelectionModel().clearSelection();
    }

    @FXML
    private void removerUnidade(ActionEvent event) {
        UnidadeMedida unidade = unidadesTable.getSelectionModel().getSelectedItem();

        Dialogo.Resposta resposta = Alerta.confirmar("Excluir unidade " + unidade.getDescricao() + " ?");

        if (resposta == Dialogo.Resposta.YES) {
            try {
                ControleDAO.getBanco().getUnidadeMedidaDAO().excluir(unidade.getId().intValue());
            } catch (SQLException ex) {
                //Logger.getLogger(TelaConsultarClientesController.class.getName()).log(Level.SEVERE, null, ex);
                ex.printStackTrace();
            }
            this.sincronizarBancoDadosUnidade();
            this.atualizarTabelaUnidade();
        }

        unidadesTable.getSelectionModel().clearSelection();
    }
    
    private void sincronizarBancoDadosCategoria() {
        //Metodo executado numa Thread separada
        SwingWorker<List, List> worker = new SwingWorker<List, List>() {
            @Override
            protected List<CategoriaProduto> doInBackground() throws Exception {
                return ControleDAO.getBanco().getCategoriaProdutoDAO().listar();
            }

            //Metodo chamado apos terminar a execucao numa Thread separada
            @Override
            protected void done() {
                super.done(); //To change body of generated methods, choose Tools | Templates.
                try {
                    listaCategoriaProdutos = this.get();
                    atualizarTabelaCategoria();
                } catch (InterruptedException | ExecutionException ex) {
                    chamarAlerta("Erro ao consultar Banco de Dados");
                }
            }
        };

        worker.execute();
    }
    
    private void atualizarTabelaCategoria() {
        //Transforma a lista em uma Lista Observavel
        ObservableList data = FXCollections.observableArrayList(listaCategoriaProdutos);
        
        this.descricaoCategoriaColumn.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        this.categoriaTable.setItems(data);
    }
    
    private void sincronizarBancoDadosMarca() {
        //Metodo executado numa Thread separada
        SwingWorker<List, List> worker = new SwingWorker<List, List>() {
            @Override
            protected List<Marca> doInBackground() throws Exception {
                return ControleDAO.getBanco().getMarcaDAO().listar();
            }

            //Metodo chamado apos terminar a execucao numa Thread separada
            @Override
            protected void done() {
                super.done(); //To change body of generated methods, choose Tools | Templates.
                try {
                    listaMarcas = this.get();
                    atualizarTabelaMarca();
                } catch (InterruptedException | ExecutionException ex) {
                    chamarAlerta("Erro ao consultar Banco de Dados");
                }
            }
        };

        worker.execute();
    }
    
    private void atualizarTabelaMarca() {
        //Transforma a lista em uma Lista Observavel
        ObservableList data = FXCollections.observableArrayList(listaMarcas);
        
        this.descricaoMarcaColumn.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        this.marcaTable.setItems(data);
    }
    
    private void sincronizarBancoDadosUnidade() {
        //Metodo executado numa Thread separada
        SwingWorker<List, List> worker = new SwingWorker<List, List>() {
            @Override
            protected List<UnidadeMedida> doInBackground() throws Exception {
                return ControleDAO.getBanco().getUnidadeMedidaDAO().listar();
            }

            //Metodo chamado apos terminar a execucao numa Thread separada
            @Override
            protected void done() {
                super.done(); //To change body of generated methods, choose Tools | Templates.
                try {
                    listaUnidadeMedidas = this.get();
                    atualizarTabelaUnidade();
                } catch (InterruptedException | ExecutionException ex) {
                    chamarAlerta("Erro ao consultar Banco de Dados");
                }
            }
        };

        worker.execute();
    }
    
    private void atualizarTabelaUnidade() {
        //Transforma a lista em uma Lista Observavel
        ObservableList data = FXCollections.observableArrayList(listaUnidadeMedidas);
        
        this.descricaoMarcaColumn.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        this.abreviaturaUnidadeColumn.setCellValueFactory(new PropertyValueFactory<>("abreviacao"));
        this.unidadesTable.setItems(data);
    }
    
    private void chamarAlerta(String mensagem) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Alerta.erro(mensagem);
            }
        });
    }

    
    
}
