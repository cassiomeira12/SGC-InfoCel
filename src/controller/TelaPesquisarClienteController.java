/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import banco.ControleDAO;
import java.io.IOException;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Cliente;

/**
 * FXML Controller class
 *
 * @author cassio
 */
public class TelaPesquisarClienteController extends AnchorPane {
    
    private Stage palco;
    public boolean RESULTADO = false;
    private Cliente clienteSelecionado;
    
    private List<Cliente> listaClientes;
    
    @FXML
    private TextField pesquisaText;
    @FXML
    private ListView<Cliente> clientesListView;
    @FXML
    private Button selecionarButton;
  
    public TelaPesquisarClienteController(Stage palco) {
        this.palco = palco;
        
        try {
            FXMLLoader fxml = new FXMLLoader(getClass().getResource("/view/PesquisarCliente.fxml"));
            fxml.setRoot(this);
            fxml.setController(this);
            fxml.load();
        } catch (IOException ex) {
            System.out.println("[ERRO] : Erro na tela Pesquisar Cliente");
            System.out.println(ex.toString());
        }
    }

    @FXML
    public void initialize() {
        this.sincronizarBancoDados();
        this.atualizarListView();
        
        selecionarButton.disableProperty().bind(clientesListView.getSelectionModel().selectedItemProperty().isNull());
        
        
        pesquisaText.textProperty().addListener((obs, old, novo) -> {
            filtro(novo, listaClientes);
        });
        
        
        clientesListView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
                    if(mouseEvent.getClickCount() == 2){
                        selecionarCliente();
                    }
                }
            }
        });
    }
    

    @FXML
    private void cancelarOperacao() {
        this.RESULTADO = false;
        this.palco.close();
    }
    
    @FXML
    private void selecionarCliente() {
        this.clienteSelecionado = clientesListView.getSelectionModel().getSelectedItem();
        this.RESULTADO = true;
        this.palco.close();
    }
    
    private void filtro(String texto, List lista) {
        ObservableList data = FXCollections.observableArrayList(lista);

        FilteredList<Cliente> dadosFiltrados = new FilteredList(data, s -> true);
        
        dadosFiltrados.setPredicate(filtro -> {
            if (texto == null || texto.isEmpty()) {
                return true;
            }
            //Coloque aqui as verificacoes da Pesquisa
            if (filtro.getNome().toLowerCase().contains(texto.toLowerCase())) {
                return true;
            }
            
            return false;
        });
        
        SortedList dadosOrdenados = new SortedList(dadosFiltrados);
        this.clientesListView.setItems(dadosOrdenados);
    }

    private void atualizarListView() {
        //Transforma a lista em uma Lista Observavel
        ObservableList data = FXCollections.observableArrayList(listaClientes);
        this.clientesListView.setItems(data);
    }
    
    private void sincronizarBancoDados() {
        this.listaClientes = ControleDAO.getBanco().getClienteDAO().listar();
    }
   
    public Cliente getCliente() {
        return this.clienteSelecionado;
    }
    
}
