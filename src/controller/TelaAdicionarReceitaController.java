/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import banco.ControleDAO;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.swing.SwingWorker;
import model.Administrador;
import model.Bairro;
import model.Cidade;
import model.Cliente;
import model.Endereco;
import model.Receita;
import util.DateUtils;
import util.Formatter;
import util.alerta.Alerta;
import util.alerta.Dialogo;

/**
 * FXML Controller class
 *
 * @author cassio
 */
public class TelaAdicionarReceitaController extends AnchorPane {

    private BorderPane painelPrincipal;

    private Cliente clienteSelecionado;

    List<Cidade> listaCidades;
    List<Bairro> listaBairros;
    List<Administrador> listaAdministrador;

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
    private ComboBox<Administrador> vendedorComboBox;
    @FXML
    private TextField valorText;
    @FXML
    private TextArea descricaoText;
    @FXML
    private DatePicker dataDatePicker;

    @FXML
    private Button salvarButton;

    //Endereco endereco = new Endereco(1l, new Bairro(1l, "Centro", new Cidade(1l, "Vitória da Conquista")), "Rua francisco santos", "149 A");
    public TelaAdicionarReceitaController(BorderPane painelPrincipal) {
        //cliente.setEndereco(endereco);

        this.painelPrincipal = painelPrincipal;

        try {
            FXMLLoader fxml = new FXMLLoader(getClass().getResource("/view/TelaAdicionarReceita.fxml"));
            fxml.setRoot(this);
            fxml.setController(this);
            fxml.load();
        } catch (IOException ex) {
            System.out.println("[ERRO] : Erro na tela Adicionar Receita");
            System.out.println(ex.toString());
        }
    }

    @FXML
    public void initialize() {
        this.dataDatePicker.setValue(LocalDate.now());//Adicionando Data do dia atual

        Formatter.mascaraCPF(cpfText);//Formatador para CPF
        Formatter.mascaraRG(rgText);//Formatador para Rg
        Formatter.mascaraTelefone(telefoneText);//Formatador para Telefone
        Formatter.decimal(valorText);//Formatador para Dinheiro
        
        Formatter.toUpperCase(nomeText, adicionarCidadeText, ruaText, adicionarBairroText, numeroText, descricaoText);

        this.editarClienteCheckBox.setVisible(false);//Ocultando componente
        this.editarClienteCheckBox.setSelected(true);//Deixando o CheckBox selecionado
        this.editarClienteCheckBox.setOnAction((e) -> {
            editarClienteCheckBox.setVisible(false);
            Platform.runLater(() -> nomeText.requestFocus());//Colocando o Foco
        });

        salvarButton.disableProperty().bind(nomeText.textProperty().isEmpty().or(
                                            cpfText.textProperty().isEmpty().or(
                                            rgText.textProperty().isEmpty().or(
                                            cidadeComboBox.selectionModelProperty().isNull().or(
                                            bairroComboBox.selectionModelProperty().isNull().or(
                                            ruaText.textProperty().isEmpty().or(
                                            numeroText.textProperty().isEmpty().or(
                                            vendedorComboBox.selectionModelProperty().isNull().or(
                                            valorText.textProperty().isEmpty().or(
                                            descricaoText.textProperty().isEmpty()))))))))));

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

        this.dataDatePicker.setValue(LocalDate.now());//Adicionando Data do dia atual

        adicionarCidadeBox.setVisible(false);
        adicionarBairroBox.setVisible(false);

        cidadeComboBox.setOnAction((e) -> {
            Cidade cidade = cidadeComboBox.getValue();
            bairroComboBox.getSelectionModel().select(null);
            sincronizarBancoDadosBairro(cidade);
        });

        sincronizarBancoDadosAdministrador();
        sincronizarBancoDadosCidade();
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
    private void salvarReceita() {
        
        float valor = Float.parseFloat(valorText.getText());
        String descricao = descricaoText.getText();
        Administrador vendedor = vendedorComboBox.getValue();
        Long data = DateUtils.getLong(dataDatePicker.getValue());
        
        LocalDate date = dataDatePicker.getValue();
        LocalDate hoje = LocalDate.now();
        if (date.isEqual(hoje)) {
            data = System.currentTimeMillis();
        }

        Dialogo.Resposta resposta = Alerta.confirmar("Deseja Adicionar uma nova Receita ?");
        if (resposta == Dialogo.Resposta.YES) {

            if (clienteSelecionado == null) {
                this.clienteSelecionado = criarCliente();
            } else {
                this.clienteSelecionado = atualizarCliente(clienteSelecionado);
            }
            
            Receita receita = new Receita(null, clienteSelecionado, vendedor, descricao, data, valor);

            try {
                ControleDAO.getBanco().getReceitaDAO().inserir(receita);
                Alerta.info("Nova Receita adicionada com sucesso!");
                cancelarOperacao();
            } catch (Exception ex) {
                Alerta.erro("Erro ao adicionar uma nova Receita!");
                ex.printStackTrace();
            }
        }
    }

    @FXML
    private void pesquisarCliente() {
        Stage palco = new Stage();
        palco.initModality(Modality.APPLICATION_MODAL);//Impede de clicar na tela em plano de fundo
        palco.centerOnScreen();
        palco.initStyle(StageStyle.UNDECORATED);//Remove a barra de menu

        TelaPesquisarClienteController telaPesquisarCliente = new TelaPesquisarClienteController(palco);

        palco.setScene(new Scene(telaPesquisarCliente));
        palco.showAndWait();

        if (telaPesquisarCliente.RESULTADO) {//Selecionou Produto
            this.clienteSelecionado = telaPesquisarCliente.getCliente();
            this.adicionarDadosCliente(clienteSelecionado);
            this.editarClienteCheckBox.setVisible(true);//Mostando componente
            this.editarClienteCheckBox.setSelected(false);//Desativando selecao do CheckBox
            Platform.runLater(() -> dataDatePicker.requestFocus());//Colocando o Foco
        }
    }

    private Cliente criarCliente() {
        String nome = nomeText.getText();
        String telefone = telefoneText.getText();
        String cpf = cpfText.getText();
        String rg = rgText.getText();
        String rua = ruaText.getText();
        String numero = numeroText.getText();
        LocalDate data = dataDatePicker.getValue();

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

        endereco = new Endereco(null, bairro, rua, numero);

        return new Cliente(null, nome, endereco, cpf, rg, telefone, DateUtils.getLong(data), true);
    }

    private Cliente atualizarCliente(Cliente cliente) {
        cliente.setNome(nomeText.getText());
        cliente.setTelefone(telefoneText.getText());
        cliente.setCpf(cpfText.getText());
        cliente.setRg(rgText.getText());
        String rua = ruaText.getText();
        String numero = numeroText.getText();

        Cidade cidade = null;
        Bairro bairro = null;

        if (cidadeBox.isVisible()) {
            cidade = cidadeComboBox.getValue();
        } else if (adicionarCidadeBox.isVisible()) {
            cidade = new Cidade(null, adicionarCidadeText.getText());
//            Long id = ControleDAO.getBanco().getCidadeDAO().inserir(cidade);
//            cidade.setId(id);
        }

        if (bairroBox.isVisible()) {
            bairro = bairroComboBox.getValue();
        } else if (adicionarBairroBox.isVisible()) {
            bairro = new Bairro(null, adicionarBairroText.getText(), cidade);
//            Long id = ControleDAO.getBanco().getBairroDAO().inserir(bairro);
//            bairro.setId(id);
        }

        cliente.getEndereco().setBairro(bairro);
        cliente.getEndereco().setRua(rua);
        cliente.getEndereco().setNumero(numero);

        return cliente;
    }

    private void adicionarDadosCliente(Cliente cliente) {
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

    private void sincronizarBancoDadosAdministrador() {
        //Metodo executado numa Thread separada
        SwingWorker<List, List> worker = new SwingWorker<List, List>() {
            @Override
            protected List<Administrador> doInBackground() throws Exception {
                return ControleDAO.getBanco().getAdministradorDAO().listar();
            }

            //Metodo chamado apos terminar a execucao numa Thread separada
            @Override
            protected void done() {
                super.done(); //To change body of generated methods, choose Tools | Templates.
                try {
                    listaAdministrador = this.get();
                    ObservableList administradores = FXCollections.observableArrayList(listaAdministrador);
                    vendedorComboBox.setItems(administradores);

                    vendedorComboBox.getSelectionModel().select(LoginController.admLogado);
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
