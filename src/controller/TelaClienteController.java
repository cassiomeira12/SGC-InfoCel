/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import banco.ControleDAO;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javax.swing.SwingWorker;
import model.Bairro;
import model.Cidade;
import model.Cliente;
import model.Endereco;
import model.Manutencao;
import model.Operacao;
import model.Receita;
import model.Saida;
import model.Venda;
import util.Formatter;
import util.alerta.Alerta;
import util.alerta.Dialogo;

/**
 * FXML Controller class
 *
 * @author cassio
 */
public class TelaClienteController extends AnchorPane {

    private BorderPane painelPrincipal;
    private Cliente cliente;

    private List<Operacao> listaOperacao;

    private List<Cidade> listaCidades;
    private List<Bairro> listaBairros;

    @FXML
    private CheckBox editarClienteCheckBox;
    @FXML
    private TextField nomeText;
    @FXML
    private TextField telefoneText;
    @FXML
    private TextField cpfText;
    @FXML
    private TextField rgText;

    @FXML
    private HBox cidadeBox;
    @FXML
    private ComboBox<Cidade> cidadeComboBox;

    @FXML
    private HBox adicionarCidadeBox;
    @FXML
    private TextField adicionarCidadeText;

    @FXML
    private HBox bairroBox;
    @FXML
    private ComboBox<Bairro> bairroComboBox;

    @FXML
    private HBox adicionarBairroBox;
    @FXML
    private TextField adicionarBairroText;

    @FXML
    private TextField ruaText;
    @FXML
    private TextField numeroText;

    @FXML
    private TableView<Operacao> operacoesTable;
    @FXML
    private TableColumn<Operacao, String> categoriaColumn;
    @FXML
    private TableColumn<Operacao, String> descricaoColumn;
    @FXML
    private TableColumn<Operacao, String> funcionarioColumn;
    @FXML
    private TableColumn<Operacao, String> dataColumn;
    @FXML
    private TableColumn<Operacao, Float> valorColumn;

    public TelaClienteController(BorderPane painelPrincipal, Cliente cliente) {
        this.painelPrincipal = painelPrincipal;
        this.cliente = cliente;

        try {
            FXMLLoader fxml = new FXMLLoader(getClass().getResource("/view/TelaCliente.fxml"));
            fxml.setRoot(this);
            fxml.setController(this);
            fxml.load();
        } catch (IOException ex) {
            System.out.println("[ERRO] : Erro na tela Cliente");
            ex.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        this.adicionarDadosCliente();

        Formatter.toUpperCase(nomeText, adicionarCidadeText, adicionarBairroText, ruaText, numeroText);

        Formatter.mascaraTelefone(telefoneText);
        Formatter.mascaraRG(rgText);
        Formatter.mascaraCPF(cpfText);

        //Campos ficam desativados enquanto CheckBox esta desativado
        nomeText.disableProperty().bind(editarClienteCheckBox.selectedProperty().not());
        telefoneText.disableProperty().bind(editarClienteCheckBox.selectedProperty().not());
        cpfText.disableProperty().bind(editarClienteCheckBox.selectedProperty().not());
        rgText.disableProperty().bind(editarClienteCheckBox.selectedProperty().not());
        cidadeBox.disableProperty().bind(editarClienteCheckBox.selectedProperty().not());
        bairroBox.disableProperty().bind(editarClienteCheckBox.selectedProperty().not());
        adicionarCidadeBox.disableProperty().bind(editarClienteCheckBox.selectedProperty().not());
        adicionarBairroBox.disableProperty().bind(editarClienteCheckBox.selectedProperty().not());
        ruaText.disableProperty().bind(editarClienteCheckBox.selectedProperty().not());
        numeroText.disableProperty().bind(editarClienteCheckBox.selectedProperty().not());
        
        cidadeComboBox.setOnAction((e) -> {
            Cidade cidade = cidadeComboBox.getValue();
            bairroComboBox.getSelectionModel().select(null);
            sincronizarBancoDadosBairro(cidade);
        });

        atualizarOperacoes(cliente);
        sincronizarBancoDadosCidade();
    }

    public void adicionarDadosCliente() {
        this.nomeText.setText(cliente.getNome());
        this.telefoneText.setText(cliente.getTelefone());
        this.cpfText.setText(cliente.getCpf());
        this.rgText.setText(cliente.getRg());

        Endereco endereco = cliente.getEndereco();
        Bairro bairro = endereco.getBairro();
        Cidade cidade = bairro.getCidade();

        this.selecionarCidade();
        this.cidadeComboBox.getSelectionModel().select(cidade);

        sincronizarBancoDadosBairro(cidade);
        bairroComboBox.setValue(bairro);

        this.ruaText.setText(endereco.getRua());
        this.numeroText.setText(endereco.getNumero());
    }

    private void adicionarPainelInterno(AnchorPane novaTela) {
        this.painelPrincipal.setCenter(novaTela);
    }

    @FXML
    private void cancelarOperacao() {
        TelaConsultarClientesController telaConsultarClientes = new TelaConsultarClientesController(painelPrincipal);
        this.adicionarPainelInterno(telaConsultarClientes);
    }

    @FXML
    private void salvarCliente() {
        String nome = nomeText.getText();
        String telefone = telefoneText.getText();
        String cpf = cpfText.getText();
        String rg = rgText.getText();
        String rua = ruaText.getText();
        String numero = numeroText.getText();

        Cidade cidade = null;
        Bairro bairro = null;
        Endereco endereco = null;

        if (cidadeBox.isVisible()) {
            cidade = cidadeComboBox.getValue();
        } else if (adicionarCidadeBox.isVisible()) {
            cidade = new Cidade(null, adicionarCidadeText.getText());
        }

        if (bairroBox.isVisible()) {
            bairro = bairroComboBox.getValue();
        } else if (adicionarBairroBox.isVisible()) {
            bairro = new Bairro(null, adicionarBairroText.getText(), cidade);
        }

        Dialogo.Resposta resposta = Alerta.confirmar("Deseja salvar os dados editados ?");
        if (resposta == Dialogo.Resposta.YES) {

            endereco = cliente.getEndereco();
            endereco.setBairro(bairro);
            endereco.setNumero(numero);
            endereco.setRua(rua);

            cliente.setNome(nome);
            cliente.setTelefone(telefone);
            cliente.setCpf(cpf);
            cliente.setRg(rg);
            cliente.setEndereco(endereco);

            try {
                ControleDAO.getBanco().getClienteDAO().editar(cliente);
                Alerta.info("Dados alterados com sucesso!");
                adicionarDadosCliente();
            } catch (SQLException ex) {
                Alerta.erro("Erro ao editar informações do Cliente");
                ex.printStackTrace();
            }
        }
    }

    private void atualizarOperacoes(Cliente cliente) {
        //String dataInicio = DateUtils.formatDate(data.getYear(), data.getMonthValue(), data.getDayOfMonth());
        //String dataFinal = DateUtils.formatDate(data.plusDays(1).getYear(), data.plusDays(1).getMonthValue(), data.plusDays(1).getDayOfMonth());

        //Metodo executado numa Thread separada
        SwingWorker<List, List> worker = new SwingWorker<List, List>() {
            @Override
            protected List doInBackground() throws Exception {
                List<Operacao> lista = new ArrayList<>();

                List<Manutencao> listaManutencao = ControleDAO.getBanco().getManutencaoDAO().buscarPorCliente(cliente);
                List<Venda> listaVenda = ControleDAO.getBanco().getVendaDAO().buscarPorCliente(cliente);

                for (Manutencao manutencao : listaManutencao) {
                    lista.add(new Operacao(manutencao));
                }

                for (Venda venda : listaVenda) {
                    lista.add(new Operacao(venda));
                }

                return lista;
            }

            //Metodo chamado apos terminar a execucao numa Thread separada
            @Override
            protected void done() {
                super.done();
                try {
                    listaOperacao = this.get();
                    atualizarTabela();
                } catch (InterruptedException | ExecutionException ex) {
                    ex.printStackTrace();
                }
            }
        };

        worker.execute();
    }

    private void atualizarTabela() {
        //Transforma a lista em uma Lista Observavel
        ObservableList data = FXCollections.observableArrayList(listaOperacao);

        this.categoriaColumn.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        this.descricaoColumn.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        this.funcionarioColumn.setCellValueFactory(new PropertyValueFactory<>("funcionario"));
        this.dataColumn.setCellValueFactory(new PropertyValueFactory<>("dataEditada"));
        this.valorColumn.setCellValueFactory(new PropertyValueFactory<>("valor"));

        this.operacoesTable.setItems(data);//Adiciona a lista de clientes na Tabela
    }

    @FXML
    private void adicionarCidade() {
        cidadeBox.setVisible(false);
        adicionarCidadeBox.setVisible(true);
        adicionarBairro();
        Platform.runLater(() -> adicionarCidadeText.requestFocus());//Colocando o Foco
    }

    @FXML
    private void selecionarCidade() {
        cidadeBox.setVisible(true);
        adicionarCidadeBox.setVisible(false);
        Formatter.limpar(adicionarCidadeText);
        Platform.runLater(() -> cidadeComboBox.requestFocus());//Colocando o Foco
    }

    @FXML
    private void adicionarBairro() {
        bairroBox.setVisible(false);
        adicionarBairroBox.setVisible(true);
        Platform.runLater(() -> adicionarBairroText.requestFocus());//Colocando o Foco
    }

    @FXML
    private void selecionarBairro() {
        bairroBox.setVisible(true);
        adicionarBairroBox.setVisible(false);
        Formatter.limpar(adicionarBairroText);
        Platform.runLater(() -> bairroComboBox.requestFocus());//Colocando o Foco
    }

    private void sincronizarBancoDadosCidade() {
        //Metodo executado numa Thread separada
        SwingWorker<List, List> worker = new SwingWorker<List, List>() {
            @Override
            protected List<Cidade> doInBackground() throws Exception {
                return ControleDAO.getBanco().getCidadeDAO().listar();
            }

            //Metodo chamado apos terminar a execucao numa Thread separada
            @Override
            protected void done() {
                super.done(); //To change body of generated methods, choose Tools | Templates.
                try {
                    listaCidades = this.get();
                    ObservableList cidades = FXCollections.observableArrayList(listaCidades);
                    cidadeComboBox.setItems(cidades);
                } catch (InterruptedException | ExecutionException ex) {
                    chamarAlerta("Erro ao consultar Banco de Dados");
                    ex.printStackTrace();
                }
            }
        };
        worker.execute();
    }

    private void sincronizarBancoDadosBairro(Cidade cidade) {
        //Metodo executado numa Thread separada
        SwingWorker<List, List> worker = new SwingWorker<List, List>() {
            @Override
            protected List<Bairro> doInBackground() throws Exception {
                return ControleDAO.getBanco().getBairroDAO().buscarPorCidade(cidade);
            }

            //Metodo chamado apos terminar a execucao numa Thread separada
            @Override
            protected void done() {
                super.done(); //To change body of generated methods, choose Tools | Templates.
                try {
                    listaBairros = this.get();
                    ObservableList bairros = FXCollections.observableArrayList(listaBairros);
                    bairroComboBox.setItems(bairros);
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
