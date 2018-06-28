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
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.swing.SwingWorker;
import model.Administrador;
import model.Bairro;
import model.Cidade;
import model.Cliente;
import model.Endereco;
import model.FormaPagamento;
import model.Manutencao;
import org.apache.log4j.Logger;
import relatorio.DescricaoManutencao;
import util.DateUtils;
import util.Formatter;
import util.alerta.Alerta;
import util.alerta.Dialogo;

/**
 * FXML Controller class
 *
 * @author cassio
 */
public class TelaAdicionarManutencaoController extends AnchorPane {

    private BorderPane painelPrincipal;
    private Cliente cliente;
    private Manutencao novaManutencao;

    List<Cidade> listaCidades;
    List<Bairro> listaBairros;
    List<FormaPagamento> listaPagamentos;
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
    private DatePicker dataDatePicker;
    @FXML
    private DatePicker entregaDatePicker;
    @FXML
    private TextField precoText;
    @FXML
    private ComboBox<Administrador> vendedorComboBox;
    @FXML
    private ComboBox<FormaPagamento> formaPagamentoComboBox;
    @FXML
    private Spinner<Integer> parcelasSpinner;

    @FXML
    private TextArea descricaoArea;
    @FXML
    private TextField marcaText;
    @FXML
    private Rectangle corRectangle;
    @FXML
    private ColorPicker corColorPicker;
    @FXML
    private TextField modeloText;
    @FXML
    private TextField imeiText;
    @FXML
    private ComboBox<String> estadoComboBox;

    public TelaAdicionarManutencaoController(BorderPane painelPrincipal) {
        this.painelPrincipal = painelPrincipal;

        try {
            FXMLLoader fxml = new FXMLLoader(getClass().getResource("/view/TelaAdicionarManutencao.fxml"));
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
        this.novaManutencao = new Manutencao(null, null, null, null, null, null, null, null, null, null, null, 00, false, null, 0);
        this.dataDatePicker.setValue(LocalDate.now());//Adicionando Data do dia atual

        Formatter.toUpperCase(nomeText, adicionarCidadeText, adicionarBairroText, marcaText, modeloText, descricaoArea, ruaText, numeroText);
        Formatter.mascaraCPF(cpfText);//Formatador para CPF
        Formatter.mascaraRG(rgText);//Formatador para Rg
        Formatter.mascaraTelefone(telefoneText);//Formatador para Telefone
        Formatter.decimal(precoText);//Formatador para Dinheiro
        this.imeiText.setTextFormatter(Formatter.NUMERICO());//Formatador Numerico
        Formatter.maxField(imeiText, 15);

        this.editarClienteCheckBox.setVisible(false);//Ocultando componente
        this.editarClienteCheckBox.setSelected(true);//Deixando o CheckBox selecionado
        this.editarClienteCheckBox.setOnAction((e) -> {
            editarClienteCheckBox.setVisible(false);
            Platform.runLater(() -> nomeText.requestFocus());//Colocando o Foco
        });
        //Campos ficam desativados enquanto CheckBox esta desativado
        nomeText.disableProperty().bind(editarClienteCheckBox.selectedProperty().not());
        telefoneText.disableProperty().bind(editarClienteCheckBox.selectedProperty().not());
        cpfText.disableProperty().bind(editarClienteCheckBox.selectedProperty().not());
        rgText.disableProperty().bind(editarClienteCheckBox.selectedProperty().not());
        adicionarCidadeBox.disableProperty().bind(editarClienteCheckBox.selectedProperty().not());
        adicionarBairroBox.disableProperty().bind(editarClienteCheckBox.selectedProperty().not());
        cidadeBox.disableProperty().bind(editarClienteCheckBox.selectedProperty().not());
        bairroBox.disableProperty().bind(editarClienteCheckBox.selectedProperty().not());
        ruaText.disableProperty().bind(editarClienteCheckBox.selectedProperty().not());
        numeroText.disableProperty().bind(editarClienteCheckBox.selectedProperty().not());

        //Adicionando estados de manutencao no ComboBox
        this.estadoComboBox.getItems().add("ABERTO");
        this.estadoComboBox.getItems().add("FINALIZADO");
        //Selecionando o primeiro Item
        this.estadoComboBox.getSelectionModel().select(0);//Selecionando o primeiro item

        this.dataDatePicker.setValue(LocalDate.now());//Adicionando Data do dia atual

        formaPagamentoComboBox.setOnAction((e) -> {
            FormaPagamento pagamento = formaPagamentoComboBox.getSelectionModel().getSelectedItem();
            SpinnerValueFactory<Integer> valores = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, pagamento.getMaximoParcelas(), 1);
            parcelasSpinner.setValueFactory(valores);
        });

        cidadeComboBox.setOnAction((e) -> {
            Cidade cidade = cidadeComboBox.getValue();
            bairroComboBox.getSelectionModel().select(null);
            sincronizarBancoDadosBairro(cidade);
        });

        corColorPicker.setOnAction((e) -> {
            corRectangle.setFill(corColorPicker.getValue());
        });

        sincronizarBancoDadosAdministrador();
        sincronizarBancoDadosCidade();
        sincronizarBancoDadosPagamento();
        
        Formatter.teste(precoText);
        
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
    private void finalizar() {
        boolean novoCliente = this.cliente == null;
        boolean vazio = Formatter.isEmpty(nomeText, telefoneText, cpfText, rgText, ruaText, numeroText);
        boolean enderecoVazio = true;

        if (cidadeBox.isVisible() && bairroBox.isVisible()) {
            enderecoVazio = Formatter.isEmpty(cidadeComboBox, bairroComboBox);
        } else if (cidadeBox.isVisible() && adicionarBairroBox.isVisible()) {
            enderecoVazio = Formatter.isEmpty(cidadeComboBox) || adicionarBairroText.getText().isEmpty();
        } else if (adicionarCidadeBox.isVisible() && adicionarBairroBox.isVisible()) {
            enderecoVazio = adicionarCidadeText.getText().isEmpty() || adicionarBairroText.getText().isEmpty();
        }

        Cliente cliente = null;
        boolean continuar = false;

        if (vazio || enderecoVazio) {
            Alerta.alerta("Não é possivel finalizar essa Manutencao", "Erro");
        } else {
            Dialogo.Resposta resposta = Alerta.confirmar("Deseja concluir esta Manutenção ?");
            // se quer cadastrar uma nova manutencao
            if (resposta == Dialogo.Resposta.YES) {

                if (novoCliente) {//Criar um Novo Cliente
                    cliente = criarCliente();
                    try {
                        Long id = ControleDAO.getBanco().getClienteDAO().inserir(cliente);
                        if (id == null) {
                            Alerta.erro("Erro ao cadastrar Novo Usuário");
                        } else {
                            cliente.setId(id);
                            this.cliente = cliente;
                            continuar = true;
                        }
                    } catch (Exception ex) {
                        Logger.getLogger(getClass()).error(ex);
                        Alerta.erro("Erro ao cadastrar Novo Usuário: " + ex.getMessage());
                        ex.printStackTrace();
                    }
                } else {
                    try {
                        cliente = atualizarCliente(this.cliente);
                    } catch (Exception ex) {
                        Logger.getLogger(getClass()).error(ex);
                        ex.printStackTrace();
                    }
                    if (editarClienteCheckBox.isSelected()) {//Houve modificacoes
                        try {
                            if (ControleDAO.getBanco().getClienteDAO().editar(cliente)) {
                                continuar = true;
                            } else {
                                Alerta.erro("Erro ao atualizar informações do Cliente");
                            }
                        } catch (Exception ex) {
                            Logger.getLogger(getClass()).error(ex);
                            Alerta.erro("Erro ao atualizar informações do Cliente: " + ex.getMessage());
                            ex.printStackTrace();
                        }
                    } else {
                        continuar = true;
                    }
                }

                if (continuar) {

                    LocalDate data = dataDatePicker.getValue();
                    //Administrador vendedor = vendedorComboBox.getValue();
                    boolean finalizado = false;
                    // olha os estado do aparelho, aberto ou finalizado
                    switch (estadoComboBox.getValue()) {
                        case "ABERTO":
                            finalizado = false;
                            break;
                        case "FINALIZADO":
                            finalizado = true;
                            break;
                    }

                    Administrador vendedor = vendedorComboBox.getValue();
                    String descricao = descricaoArea.getText();
                    int preco = Integer.parseInt(precoText.getText());
                    Long dataCadastro = DateUtils.getLong(dataDatePicker.getValue());
                    
                    LocalDate date = dataDatePicker.getValue();
                    LocalDate hoje = LocalDate.now();
                    if (date.isEqual(hoje)) {
                        dataCadastro = System.currentTimeMillis();
                    }

                    Long dataPrevisao = null;
                    if (entregaDatePicker.getValue() != null) {
                        dataPrevisao = DateUtils.getLong(entregaDatePicker.getValue());
                        System.out.println("pegou valor da data piker: " + dataPrevisao);
                    }

                    String marca = marcaText.getText();
                    String cor = corColorPicker.getValue().toString();
                    String modelo = modeloText.getText();
                    String imei = imeiText.getText();
                    FormaPagamento pagamento = formaPagamentoComboBox.getValue();
                    int parcelas = parcelasSpinner.getValue();

                    this.novaManutencao.setCliente(cliente);
                    this.novaManutencao.setAdministrador(vendedor);
                    this.novaManutencao.setDescricao(descricao);
                    this.novaManutencao.setPreco(preco);
                    this.novaManutencao.setFormaPagamento(pagamento);
                    this.novaManutencao.setQuantidadeParcelas(parcelas);
                    this.novaManutencao.setDataCadastro(dataCadastro);
                    this.novaManutencao.setDataEntrega(null);
                    this.novaManutencao.setDataPrevisaoEntrega(dataPrevisao);
                    this.novaManutencao.setMarca(marca);
                    this.novaManutencao.setCor(cor);
                    this.novaManutencao.setModelo(modelo);
                    this.novaManutencao.setImei(imei);
                    this.novaManutencao.setFinalizado(finalizado);

                    try {
                        Long id = ControleDAO.getBanco().getManutencaoDAO().inserir(novaManutencao);

                        // id esta ficando nulo
                        if (id == null) {
                            Alerta.erro("Erro ao adicionar nova Manutenção!");
                            cancelarOperacao();
                        } else {
                            
                            Dialogo.Resposta abrirPDF = Alerta.confirmar("Manutenção finalizada com sucesso!\n"
                                                                        + "Deseja abrir o Comprovante da Manutenção ?");
                            
                            // gerar descricao manutencao pdf
                            DescricaoManutencao relatorio = new DescricaoManutencao(id);
                            if (abrirPDF == Dialogo.Resposta.YES) {
                                relatorio.setMostrar(true);
                            }
                            relatorio.start();
                            
                            TelaInicialController telaInicial = new TelaInicialController(painelPrincipal);
                            this.adicionarPainelInterno(telaInicial);
                        }
                    } catch (Exception ex) {
                        Logger.getLogger(getClass()).error(ex);
                        Alerta.erro("Erro ao adicionar nova Manutenção: " + ex.getMessage());
                        ex.printStackTrace();
                    }
                }
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
            this.cliente = telaPesquisarCliente.getCliente();
            this.adicionarDadosCliente(cliente);
            this.editarClienteCheckBox.setVisible(true);//Mostando componente
            this.editarClienteCheckBox.setSelected(false);//Desativando selecao do CheckBox
            Platform.runLater(() -> vendedorComboBox.requestFocus());//Colocando o Foco
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

    private Cliente atualizarCliente(Cliente cliente) throws Exception {
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
            Long id = ControleDAO.getBanco().getCidadeDAO().inserir(cidade);
            cidade.setId(id);
        }

        if (bairroBox.isVisible()) {
            bairro = bairroComboBox.getValue();
        } else if (adicionarBairroBox.isVisible()) {
            bairro = new Bairro(null, adicionarBairroText.getText(), cidade);
            Long id = ControleDAO.getBanco().getBairroDAO().inserir(bairro);
            bairro.setId(id);
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
                    Logger.getLogger(getClass()).error(ex);
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
                    Logger.getLogger(getClass()).error(ex);
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
                } catch (InterruptedException | ExecutionException ex) {
                    Logger.getLogger(getClass()).error(ex);
                    chamarAlerta("Erro ao consultar Banco de Dados");
                    ex.printStackTrace();
                }
            }
        };
        worker.execute();
    }

    private void sincronizarBancoDadosPagamento() {
        //Metodo executado numa Thread separada
        SwingWorker<List, List> worker = new SwingWorker<List, List>() {
            @Override
            protected List<FormaPagamento> doInBackground() throws Exception {
                return ControleDAO.getBanco().getFormaPagamentoDAO().listar();
            }

            //Metodo chamado apos terminar a execucao numa Thread separada
            @Override
            protected void done() {
                super.done(); //To change body of generated methods, choose Tools | Templates.
                try {
                    listaPagamentos = this.get();
                    ObservableList pagamentos = FXCollections.observableArrayList(listaPagamentos);
                    formaPagamentoComboBox.setItems(pagamentos);
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
