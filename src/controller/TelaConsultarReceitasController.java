package controller;

import banco.ControleDAO;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javax.swing.SwingWorker;
import model.Administrador;
import model.Cliente;
import model.Receita;
import org.apache.log4j.Logger;
import util.DateUtils;
import util.Formatter;
import util.alerta.Alerta;
import util.alerta.Dialogo;

public class TelaConsultarReceitasController extends AnchorPane {

    private BorderPane painelPrincipal;

    private List<Receita> listaReceitas;
    
    private LocalDate dataInicio;
    private LocalDate dataFim;
    
    @FXML
    private TextField filtroText;
    @FXML
    private DatePicker inicioDatePicker;
    @FXML
    private DatePicker fimDatePicker;

    @FXML
    private TableView<Receita> receitasTable;
    @FXML
    private TableColumn<Receita, Cliente> clienteColumn;
    @FXML
    private TableColumn<Receita, Cliente> descricaoColumn;
    @FXML
    private TableColumn<Receita, Administrador> vendedorColumn;
    @FXML
    private TableColumn<Receita, Long> dataColumn;
    @FXML
    private TableColumn<Receita, String> valorColumn;
    @FXML
    private Button visualizarButton;
    @FXML
    private Button excluirButton;

    public TelaConsultarReceitasController(BorderPane painelPrincipal) {
        this.painelPrincipal = painelPrincipal;

        try {
            FXMLLoader fxml = new FXMLLoader(getClass().getResource("/view/TelaConsultarReceitas.fxml"));
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
        this.dataInicio = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonthValue(), 1);
        this.dataFim = LocalDate.of(dataInicio.getYear(), dataInicio.getMonthValue(), dataInicio.lengthOfMonth());
        
        Formatter.toUpperCase(filtroText);
        
        //Desativa os Botoes de Editar e Excluir quando nenhum item na tabela esta selecionado
        visualizarButton.disableProperty().bind(receitasTable.getSelectionModel().selectedItemProperty().isNull());
        excluirButton.disableProperty().bind(receitasTable.getSelectionModel().selectedItemProperty().isNull());
        
        receitasTable.setOnMouseClicked((event) -> {
            if (event.getButton().equals(MouseButton.PRIMARY)) {
                if (event.getClickCount() == 2) {
                    visualizarReceita();
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
            filtro(texto, listaReceitas, receitasTable);
        });
        
        sincronizarBancoDados(DateUtils.formatDate(dataInicio), DateUtils.formatDate(dataFim));

    }

    private void filtro(String texto, List lista, TableView tabela) {
        ObservableList data = FXCollections.observableArrayList(lista);

        FilteredList<Receita> dadosFiltrados = new FilteredList(data, filtro -> true);

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
    private void excluir() {
        Receita receita = receitasTable.getSelectionModel().getSelectedItem();
        
        Dialogo.Resposta resposta = Alerta.confirmar("Deseja excluir a Receita ?");

        if (resposta == Dialogo.Resposta.YES) {
            try {
                ControleDAO.getBanco().getReceitaDAO().excluir(receita.getId().intValue());
            } catch (SQLException ex) {
                Logger.getLogger(getClass()).error(ex);
                Alerta.erro("Erro ao excluir Receita");
                ex.printStackTrace();
            }
            sincronizarBancoDados(DateUtils.formatDate(dataInicio), DateUtils.formatDate(dataFim));
        }

        receitasTable.getSelectionModel().clearSelection();
    }
    
    @FXML
    private void visualizarReceita() {
        Receita receita = receitasTable.getSelectionModel().getSelectedItem();
        
        TelaReceitaController tela = new TelaReceitaController(painelPrincipal);
        tela.setReceita(receita);
        
        this.adicionarPainelInterno(tela);
    }

    private void atualizarTabela() {
        //Transforma a lista em uma Lista Observavel
        ObservableList data = FXCollections.observableArrayList(listaReceitas);
        
        this.clienteColumn.setCellValueFactory(new PropertyValueFactory<>("cliente"));
        this.descricaoColumn.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        this.vendedorColumn.setCellValueFactory(new PropertyValueFactory<>("administrador"));
        this.dataColumn.setCellValueFactory(new PropertyValueFactory<>("dataEditada"));//Adiciona o valor da variavel Telefone
        this.valorColumn.setCellValueFactory(new PropertyValueFactory<>("precoFormatado"));
        
        this.receitasTable.setItems(data);//Adiciona a lista de clientes na Tabela
    }

    private void sincronizarBancoDados(String dataInicio, String dataFinal) {
        //Metodo executado numa Thread separada
        SwingWorker<List, List> worker = new SwingWorker<List, List>() {
            @Override
            protected List<Receita> doInBackground() throws Exception {
                return ControleDAO.getBanco().getReceitaDAO().buscarPorIntervalo(dataInicio, dataFinal);
            }

            //Metodo chamado apos terminar a execucao numa Thread separada
            @Override
            protected void done() {
                super.done(); //To change body of generated methods, choose Tools | Templates.
                try {
                    listaReceitas = this.get();
                    Collections.sort(listaReceitas);//Ordenando as Operacoes
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

}
