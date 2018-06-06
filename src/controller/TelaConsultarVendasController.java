package controller;

import banco.ControleDAO;
import java.io.IOException;
import java.time.LocalDate;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javax.swing.SwingWorker;
import model.Administrador;
import model.Cliente;
import model.Venda;
import util.DateUtils;
import util.Formatter;
import util.alerta.Alerta;

public class TelaConsultarVendasController extends AnchorPane {

    private BorderPane painelPrincipal;

    private List<Venda> listaVendas;
    
    private LocalDate dataInicio;
    private LocalDate dataFim;
    
    
    @FXML
    private TextField filtroText;
    @FXML
    private DatePicker inicioDatePicker;
    @FXML
    private DatePicker fimDatePicker;

    @FXML
    private TableView<Venda> vendasTable;
    @FXML
    private TableColumn<Venda, Cliente> clienteColumn;
    @FXML
    private TableColumn<Cliente, String> enderecoColumn;
    @FXML
    private TableColumn<Administrador, String> vendedorColumn;
    @FXML
    private TableColumn<Venda, Long> dataColumn;
    @FXML
    private TableColumn<Venda, Float> totalColumn;
    @FXML
    private Button visualizarButton;
    

    public TelaConsultarVendasController(BorderPane painelPrincipal) {
        this.painelPrincipal = painelPrincipal;

        try {
            FXMLLoader fxml = new FXMLLoader(getClass().getResource("/view/TelaConsultarVendas.fxml"));
            fxml.setRoot(this);
            fxml.setController(this);
            fxml.load();
        } catch (IOException ex) {
            System.out.println("[ERRO] : Erro na tela Consultar Vendas");
            System.out.println(ex.toString());
        }
    }

    @FXML
    public void initialize() {
        this.dataInicio = LocalDate.now();
        this.dataFim = LocalDate.now().plusDays(1);
        
        Formatter.toUpperCase(filtroText);
        
        //Desativa os Botoes de Editar e Excluir quando nenhum item na tabela esta selecionado
        visualizarButton.disableProperty().bind(vendasTable.getSelectionModel().selectedItemProperty().isNull());
        
        vendasTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton().equals(MouseButton.PRIMARY)) {
                    if (event.getClickCount() == 2) {
                        visualizarVenda();
                    }
                }
            }
        });
        
        inicioDatePicker.setValue(dataInicio);
        fimDatePicker.setValue(dataFim);
        
        inicioDatePicker.setOnAction((e) -> {
            this.dataInicio = inicioDatePicker.getValue();
            sincronizarBancoDados(DateUtils.formatDate(dataInicio), DateUtils.formatDate(dataFim));
        });
        
        fimDatePicker.setOnAction((e) -> {
            this.dataFim = fimDatePicker.getValue();
            sincronizarBancoDados(DateUtils.formatDate(dataInicio), DateUtils.formatDate(dataFim));
        });
        
        filtroText.textProperty().addListener((e) -> {
            String texto = filtroText.getText();
            filtro(texto, listaVendas, vendasTable);
        });
        
        sincronizarBancoDados(DateUtils.formatDate(dataInicio), DateUtils.formatDate(dataFim));

    }

    private void filtro(String texto, List lista, TableView tabela) {
        ObservableList data = FXCollections.observableArrayList(lista);

        FilteredList<Venda> dadosFiltrados = new FilteredList(data, filtro -> true);

        dadosFiltrados.setPredicate(filtro -> {
            if (texto == null || texto.isEmpty()) {
                return true;
            }
            //Coloque aqui as verificacoes da Pesquisa
            if (filtro.getCliente().getNome().toLowerCase().contains(texto.toLowerCase())) {
                return true;
            }
            
            if (filtro.getCliente().getEndereco().toString().toLowerCase().contains(texto.toLowerCase())) {
                return true;
            }
            
            if (filtro.getAdministrador().getNome().toLowerCase().contains(texto.toLowerCase())) {
                return true;
            }
            
            if (DateUtils.formatDate(filtro.getData()).contains(texto.toLowerCase())) {
                return true;
            }
            
            return false;
        });

        SortedList dadosOrdenados = new SortedList(dadosFiltrados);
        dadosOrdenados.comparatorProperty().bind(tabela.comparatorProperty());
        tabela.setItems(dadosOrdenados);
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
    private void visualizarVenda() {
//        Cliente cliente = vendasTable.getSelectionModel().getSelectedItem();
//
//        TelaClienteController telaCliente = new TelaClienteController(painelPrincipal, cliente);
//        this.adicionarPainelInterno(telaCliente);
    }

    private void atualizarTabela() {
        //Transforma a lista em uma Lista Observavel
        ObservableList data = FXCollections.observableArrayList(listaVendas);
        
        this.clienteColumn.setCellValueFactory(new PropertyValueFactory<>("cliente"));
        //this.enderecoColumn.setCellValueFactory(new PropertyValueFactory<>(""));
        this.vendedorColumn.setCellValueFactory(new PropertyValueFactory<>("administrador"));
        this.dataColumn.setCellValueFactory(new PropertyValueFactory<>("dataEditada"));//Adiciona o valor da variavel Telefone
        this.totalColumn.setCellValueFactory(new PropertyValueFactory<>("precoTotal"));
        this.vendasTable.setItems(data);//Adiciona a lista de clientes na Tabela
    }

    private void sincronizarBancoDados(String dataInicio, String dataFinal) {
        //Metodo executado numa Thread separada
        SwingWorker<List, List> worker = new SwingWorker<List, List>() {
            @Override
            protected List<Venda> doInBackground() throws Exception {
                return ControleDAO.getBanco().getVendaDAO().buscarPorIntervalo(dataInicio, dataFinal);
            }

            //Metodo chamado apos terminar a execucao numa Thread separada
            @Override
            protected void done() {
                super.done(); //To change body of generated methods, choose Tools | Templates.
                try {
                    listaVendas = this.get();
                    atualizarTabela();
                } catch (InterruptedException | ExecutionException ex) {
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

}
