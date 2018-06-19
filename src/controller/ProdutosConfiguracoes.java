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
import org.apache.log4j.Logger;

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

        editarCategoriaButton.disableProperty().bind(categoriaTable.getSelectionModel().selectedItemProperty().isNull());
        editarMarcaButton.disableProperty().bind(marcaTable.getSelectionModel().selectedItemProperty().isNull());
        editarUnidadeButton.disableProperty().bind(unidadesTable.getSelectionModel().selectedItemProperty().isNull());
        
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
        
        AdicionarProdutoDescricaoController tela = new AdicionarProdutoDescricaoController(palco, Tipo.CATEGORIA, true);
        tela.setTitulo("Adicionar Categoria");
        palco.setScene(new Scene(tela));
        palco.showAndWait();
        
        if (tela.RESULTADO) {
            CategoriaProduto novaCategoria = tela.getCategoriaProduto();
        
            Long id = null;
            try {
                id = ControleDAO.getBanco().getCategoriaProdutoDAO().inserir(novaCategoria);
            } catch (Exception ex) {
                Logger.getLogger(getClass()).error(ex);
                ex.printStackTrace();
            }

            if (id == null) {//Erro ao inserir item no Banco de Dados
                Alerta.erro("Erro ao criar nova Categoria de Produto");
            } else {
                Alerta.info("Categoria de Produto adicionada com sucesso!");
                novaCategoria.setId(id);
                categoriaTable.getItems().add(novaCategoria);
            }
        }
        
    }

    @FXML
    private void adicionarMarca(ActionEvent event) {
        Stage palco = new Stage();
        palco.initModality(Modality.APPLICATION_MODAL);//Impede de clicar na tela em plano de fundo
        palco.centerOnScreen();
        palco.initStyle(StageStyle.UNDECORATED);//Remove a barra de menu
        
        AdicionarProdutoDescricaoController tela = new AdicionarProdutoDescricaoController(palco, Tipo.MARCA, true);
        tela.setTitulo("Adicionar Marca");
        palco.setScene(new Scene(tela));
        palco.showAndWait();
        
        if (tela.RESULTADO) {
            Marca novaMarca = tela.getMarca();
        
            Long id = null;
            try {
                id = ControleDAO.getBanco().getMarcaDAO().inserir(novaMarca);
            } catch (Exception ex) {
                Logger.getLogger(getClass()).error(ex);
                ex.printStackTrace();
            }

            if (id == null) {//Erro ao inserir item no Banco de Dados
                Alerta.erro("Erro ao criar nova Marca");
            } else {
                Alerta.info("Marca adicionada com sucesso!");
                novaMarca.setId(id);
                marcaTable.getItems().add(novaMarca);
            }
        }
    }

    @FXML
    private void adicionarUnidade(ActionEvent event) {
        Stage palco = new Stage();
        palco.initModality(Modality.APPLICATION_MODAL);//Impede de clicar na tela em plano de fundo
        palco.centerOnScreen();
        palco.initStyle(StageStyle.UNDECORATED);//Remove a barra de menu
        
        AdicionarProdutoDescricaoController tela = new AdicionarProdutoDescricaoController(palco, Tipo.UNIDADE, true);
        tela.setTitulo("Adicionar Unidade de Medida");
        palco.setScene(new Scene(tela));
        palco.showAndWait();
        
        if (tela.RESULTADO) {
            UnidadeMedida novaUnidade = tela.getUnidadeMedida();
        
            Long id = null;
            try {
                id = ControleDAO.getBanco().getUnidadeMedidaDAO().inserir(novaUnidade);
            } catch (Exception ex) {
                Logger.getLogger(getClass()).error(ex);
                ex.printStackTrace();
            }

            if (id == null) {//Erro ao inserir item no Banco de Dados
                Alerta.erro("Erro ao criar nova Unidade de Medida");
            } else {
                Alerta.info("Unidade de Medida adicionada com sucesso!");
                novaUnidade.setId(id);
                unidadesTable.getItems().add(novaUnidade);
            }
        }
    }
    
    @FXML
    private void editarCategoria(ActionEvent event) {
        CategoriaProduto categoria = categoriaTable.getSelectionModel().getSelectedItem();
        
        Stage palco = new Stage();
        palco.initModality(Modality.APPLICATION_MODAL);//Impede de clicar na tela em plano de fundo
        palco.centerOnScreen();
        palco.initStyle(StageStyle.UNDECORATED);//Remove a barra de menu
        
        AdicionarProdutoDescricaoController editarCategoria = new AdicionarProdutoDescricaoController(palco, Tipo.CATEGORIA, false);
        editarCategoria.setCategoriaProduto(categoria);
        
        palco.setScene(new Scene(editarCategoria));
        palco.showAndWait();
        
        if (editarCategoria.RESULTADO) {
            categoria = editarCategoria.getCategoriaProduto();
            
            try {
                if (ControleDAO.getBanco().getCategoriaProdutoDAO().editar(categoria)) {
                    Alerta.info("Dados alterados com sucesso!");
                    sincronizarBancoDadosCategoria();
                } else {
                    Alerta.erro("Erro ao elterar dados!");
                }
            } catch (Exception ex) {
                Logger.getLogger(getClass()).error(ex);
                ex.printStackTrace();
            }
        }
    }

    @FXML
    private void editarMarca(ActionEvent event) {
        Marca marca = marcaTable.getSelectionModel().getSelectedItem();
        
        Stage palco = new Stage();
        palco.initModality(Modality.APPLICATION_MODAL);//Impede de clicar na tela em plano de fundo
        palco.centerOnScreen();
        palco.initStyle(StageStyle.UNDECORATED);//Remove a barra de menu
        
        AdicionarProdutoDescricaoController editarMarca = new AdicionarProdutoDescricaoController(palco, Tipo.MARCA, false);
        editarMarca.setMarca(marca);
        
        palco.setScene(new Scene(editarMarca));
        palco.showAndWait();
        
        if (editarMarca.RESULTADO) {
            marca = editarMarca.getMarca();
            
            try {
                if (ControleDAO.getBanco().getMarcaDAO().editar(marca)) {
                    Alerta.info("Dados alterados com sucesso!");
                    sincronizarBancoDadosMarca();
                } else {
                    Alerta.erro("Erro ao elterar dados!");
                }
            } catch (Exception ex) {
                Logger.getLogger(getClass()).error(ex);
                ex.printStackTrace();
            }
        }
    }

    @FXML
    private void editarUnidade(ActionEvent event) {
        UnidadeMedida unidade = unidadesTable.getSelectionModel().getSelectedItem();
        
        Stage palco = new Stage();
        palco.initModality(Modality.APPLICATION_MODAL);//Impede de clicar na tela em plano de fundo
        palco.centerOnScreen();
        palco.initStyle(StageStyle.UNDECORATED);//Remove a barra de menu
        
        AdicionarProdutoDescricaoController editarUnidade = new AdicionarProdutoDescricaoController(palco, Tipo.UNIDADE, false);
        editarUnidade.setUnidadeMedida(unidade);
        
        palco.setScene(new Scene(editarUnidade));
        palco.showAndWait();
        
        if (editarUnidade.RESULTADO) {
            unidade = editarUnidade.getUnidadeMedida();
            
            try {
                if (ControleDAO.getBanco().getUnidadeMedidaDAO().editar(unidade)) {
                    Alerta.info("Dados alterados com sucesso!");
                    sincronizarBancoDadosUnidade();
                } else {
                    Alerta.erro("Erro ao elterar dados!");
                }
            } catch (Exception ex) {
                Logger.getLogger(getClass()).error(ex);
                ex.printStackTrace();
            }
        }
    }
    
    @FXML
    private void removerCategoria(ActionEvent event) {
        CategoriaProduto categoria = categoriaTable.getSelectionModel().getSelectedItem();

        String alerta = "Deseja excluir categoria " + categoria.getDescricao() + " ? "
                + "Todas os produtos com essa categoria também serão excluídos!";
        Dialogo.Resposta resposta = Alerta.confirmar(alerta);

        if (resposta == Dialogo.Resposta.YES) {
            try {
                if (ControleDAO.getBanco().getCategoriaProdutoDAO().excluir(categoria)) {
                    Alerta.info("Categoria de Produto removida com sucesso!");
                } else {
                    Alerta.erro("Erro ao remover Categoria de Produto");
                }
            } catch (SQLException ex) {
                Logger.getLogger(getClass()).error(ex);
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

        String alerta = "Deseja excluir marca " + marca.getDescricao() + " ? "
                + "Todas os produtos com essa marca também serão excluídos!";
        Dialogo.Resposta resposta = Alerta.confirmar(alerta);

        if (resposta == Dialogo.Resposta.YES) {
            try {
                if (ControleDAO.getBanco().getMarcaDAO().excluir(marca)) {
                    Alerta.info("Marca removida com sucesso!");
                } else {
                    Alerta.erro("Erro ao remover Marca");
                }
            } catch (SQLException ex) {
                Logger.getLogger(getClass()).error(ex);
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

        String alerta = "Deseja excluir unidade " + unidade.getDescricao() + " ? "
                + "Todas os produtos com essa unidade também serão excluídos!";
        Dialogo.Resposta resposta = Alerta.confirmar(alerta);

        if (resposta == Dialogo.Resposta.YES) {
            try {
                if (ControleDAO.getBanco().getUnidadeMedidaDAO().excluir(unidade)) {
                    Alerta.info("Unidade de Medida removida com sucesso!");
                } else {
                    Alerta.erro("Erro ao remover Unidade de Medida");
                }
            } catch (SQLException ex) {
                Logger.getLogger(getClass()).error(ex);
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
                    Logger.getLogger(getClass()).error(ex);
                    chamarAlerta("Erro ao consultar Banco de Dados");
                    ex.printStackTrace();
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
                    Logger.getLogger(getClass()).error(ex);
                    chamarAlerta("Erro ao consultar Banco de Dados");
                    ex.printStackTrace();
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
                    Logger.getLogger(getClass()).error(ex);
                    chamarAlerta("Erro ao consultar Banco de Dados");
                    ex.printStackTrace();
                }
            }
        };

        worker.execute();
    }
    
    private void atualizarTabelaUnidade() {
        //Transforma a lista em uma Lista Observavel
        ObservableList data = FXCollections.observableArrayList(listaUnidadeMedidas);
        
        this.abreviaturaUnidadeColumn.setCellValueFactory(new PropertyValueFactory<>("abreviacao"));
        this.descricaoUnidadeColumn.setCellValueFactory(new PropertyValueFactory<>("descricao"));
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
