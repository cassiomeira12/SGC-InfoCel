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
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import model.Cliente;
import util.Formatter;
import util.alerta.Alerta;
import util.alerta.Dialogo;

/**
 * FXML Controller class
 *
 * @author cassio
 */
public class TelaConsultarClientesController extends AnchorPane {
    
    private BorderPane painelPrincipal;

    private List<Cliente> listaClientes;
    
    @FXML
    private TextField pesquisaText;
    @FXML
    private TableView<Cliente> clientesTable;
    @FXML
    private TableColumn<Cliente, String> nomeColumn;
    @FXML
    private TableColumn<Cliente, String> enderecoColumn;
    @FXML
    private TableColumn<Cliente, String> telefoneColumn;
    @FXML
    private Button editarButton;
    @FXML
    private Button excluirButton;
    
  
    public TelaConsultarClientesController(BorderPane painelPrincipal) {
        this.painelPrincipal = painelPrincipal;
        
        try {
            FXMLLoader fxml = new FXMLLoader(getClass().getResource("/view/TelaConsultarClientes.fxml"));
            fxml.setRoot(this);
            fxml.setController(this);
            fxml.load();
        } catch (IOException ex) {
            System.out.println("[ERRO] : Erro na tela Consultar Clientes");
            System.out.println(ex.toString());
        }
    }

    @FXML
    public void initialize() {
        //Desativa os Botoes de Editar e Excluir quando nenhum item na tabela esta selecionado
        editarButton.disableProperty().bind(clientesTable.getSelectionModel().selectedItemProperty().isNull());
        excluirButton.disableProperty().bind(clientesTable.getSelectionModel().selectedItemProperty().isNull());
        
        this.sincronizarBancoDados();
        this.atualizarTabela();
        
        pesquisaText.textProperty().addListener((obs, old, novo) -> {
            filtro(novo, listaClientes, clientesTable);
        });
    }

    private void adicionarPainelInterno(AnchorPane novaTela) {
        this.painelPrincipal.setCenter(novaTela);
    }
    
    @FXML
    private void cancelarOperacao() {
        TelaInicialController telaInicial = new TelaInicialController(painelPrincipal);
        this.adicionarPainelInterno(telaInicial);
    }
    
    @FXML
    private void editarCliente() {
        try {
            Cliente cliente = clientesTable.getSelectionModel().getSelectedItem();
            
            TelaClienteController telaCliente = new TelaClienteController(painelPrincipal, cliente);
            this.adicionarPainelInterno(telaCliente);
            
        } catch (NullPointerException e) {
            Alerta.alerta("Selecione usuário na tabela para editar!");
        }
    }
    
    @FXML
    private void excluirCliente() {
        try {
            Cliente cliente = clientesTable.getSelectionModel().getSelectedItem();

            Dialogo.Resposta resposta = Alerta.confirmar("Excluir usuário " + cliente.getNome() + " ?");
            
            if (resposta == Dialogo.Resposta.YES) {
                ControleDAO.getBanco().getClienteDAO().excluir(cliente.getId().intValue());
                //sincronizarBase();
                atualizarTabela();
            }

            clientesTable.getSelectionModel().clearSelection();

        } catch (NullPointerException ex) {
            Alerta.alerta("Selecione usuário na tabela para exclusão!");
        }
    }
    
    private void filtro(String texto, List lista, TableView tabela) {
        ObservableList data = FXCollections.observableArrayList(lista);
        
        FilteredList<Cliente> dadosFiltrados = new FilteredList(data, filtro -> true);
        
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
        dadosOrdenados.comparatorProperty().bind(tabela.comparatorProperty());
        tabela.setItems(dadosOrdenados);
    }
    
    private void atualizarTabela() {
        //Transforma a lista em uma Lista Observavel
        ObservableList data = FXCollections.observableArrayList(listaClientes);
        
        this.nomeColumn.setCellValueFactory(new PropertyValueFactory<>("nome"));//Adiciona o valor da variavel Nome
        this.enderecoColumn.setCellValueFactory(new PropertyValueFactory<>("endereco"));//Adiciona o valor da variavel Endereco
        this.telefoneColumn.setCellValueFactory(new PropertyValueFactory<>("telefone"));//Adiciona o valor da variavel Telefone
        
        this.clientesTable.setItems(data);//Adiciona a lista de clientes na Tabela
    }
    
    private void sincronizarBancoDados() {
        this.listaClientes = ControleDAO.getBanco().getClienteDAO().listar();
    }
    
}
