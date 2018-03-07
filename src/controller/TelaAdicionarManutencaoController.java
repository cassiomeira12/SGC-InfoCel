/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import banco.ControleDAO;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Administrador;
import model.Cliente;
import model.FormaPagamento;
import model.Manutencao;
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
    private TextField cidadeText;
    @FXML
    private TextField enderecoText;
    @FXML
    private DatePicker dataDatePicker;
    @FXML
    private TextField precoText;
    @FXML
    private ComboBox<Administrador> vendedorComboBox;
    @FXML
    private ComboBox<FormaPagamento> formaPagamentoComboBox;
    @FXML
    private Spinner parcelasSpinner;

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
            System.out.println("[ERRO] : Erro na tela Adicionar Manutencao");
            System.out.println(ex.toString());
        }
    }

    @FXML
    public void initialize() {
        this.novaManutencao = new Manutencao(null, null, null, null, null, null, null, null, null, null, null, 00, false);
        this.dataDatePicker.setValue(LocalDate.now());//Adicionando Data do dia atual

        Formatter.toUpperCase(nomeText, cidadeText, enderecoText, marcaText, modeloText);
        Formatter.mascaraCPF(cpfText);//Formatador para CPF
        Formatter.mascaraRG(rgText);//Formatador para Rg
        Formatter.mascaraTelefone(telefoneText);//Formatador para Telefone
        Formatter.decimal(precoText);//Formatador para Dinheiro
        this.imeiText.setTextFormatter(Formatter.NUMERICO());//Formatador Numerico
        Formatter.maxField(imeiText, 15);

        this.editarClienteCheckBox.setVisible(false);//Ocultando componente
        this.editarClienteCheckBox.setSelected(true);//Deixando o CheckBox selecionado
        this.editarClienteCheckBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                editarClienteCheckBox.setVisible(false);
                Platform.runLater(() -> nomeText.requestFocus());//Colocando o Foco
            }
        });
        //Campos ficam desativados enquanto CheckBox esta desativado
        nomeText.disableProperty().bind(editarClienteCheckBox.selectedProperty().not());
        telefoneText.disableProperty().bind(editarClienteCheckBox.selectedProperty().not());
        cpfText.disableProperty().bind(editarClienteCheckBox.selectedProperty().not());
        rgText.disableProperty().bind(editarClienteCheckBox.selectedProperty().not());
        cidadeText.disableProperty().bind(editarClienteCheckBox.selectedProperty().not());
        enderecoText.disableProperty().bind(editarClienteCheckBox.selectedProperty().not());

        //Adicionando estados de manutencao no ComboBox
        this.estadoComboBox.getItems().add("Aberto");
        this.estadoComboBox.getItems().add("Finalizado");
        //Selecionando o primeiro Item
        this.estadoComboBox.getSelectionModel().select(0);//Selecionando o primeiro item
        try {
            this.formaPagamentoComboBox.getItems().addAll(ControleDAO.getBanco().getFormaPagamentoDAO().listar());
        } catch (SQLException ex) {
            Alerta.erro(ex.toString());
        }

        corColorPicker.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                corRectangle.setFill(corColorPicker.getValue());
            }
        });

    }

    private void adicionarPainelInterno(AnchorPane novaTela) {
        this.painelPrincipal.setCenter(novaTela);
    }

    @FXML
    private void cancelarOperacao() {
//        boolean vazio = Formatter.noEmpty(nomeText, telefoneText, cpfText, rgText, cidadeText, enderecoText, precoText, marcaText, modeloText, imeiText);
//        boolean carrinhoVazio = novaVenda.isEmpty();
//        
//        if (cliente != null || vazio || !carrinhoVazio) {
//            Dialogo.Resposta resposta = Alerta.confirmar("Deseja cancelar esta Manutenção?");
//
//            if (resposta == Dialogo.Resposta.YES) {
        TelaInicialController telaInicial = new TelaInicialController(painelPrincipal);
        this.adicionarPainelInterno(telaInicial);
//            }
//        } else {
//            TelaInicialController telaInicial = new TelaInicialController(painelPrincipal);
//            this.adicionarPainelInterno(telaInicial);
//        }
    }

    @FXML
    private void finalizar() {
        boolean novoCliente = this.cliente == null;
        boolean vazio = Formatter.isEmpty(nomeText, telefoneText, cpfText, rgText, cidadeText, enderecoText);
        Cliente cliente = null;
        boolean continuar = false;

        if (vazio) {
            Alerta.alerta("Não é possivel finalizar essa Manutenção", "Erro");
        } else {
            Dialogo.Resposta resposta = Alerta.confirmar("Deseja concluir esta Manutenção ?");
            // se quer cadastrar uma nova manutencao
            if (resposta == Dialogo.Resposta.YES) {
                System.out.println("ele deseja concluir a manutenção");

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
                    } catch (Exception e) {
                        Alerta.erro("Erro ao cadastrar Novo Usuário\n" + e.toString());
                    }
                } else {//Cliente selecionado
                    cliente = atualizarCliente(this.cliente);
                    if (editarClienteCheckBox.isSelected()) {//Houve modificacoes
                        try {
                            if (ControleDAO.getBanco().getClienteDAO().editar(cliente)) {
                                continuar = true;
                            } else {
                                Alerta.erro("Erro ao atualizar informações do Cliente");
                            }
                        } catch (Exception e) {
                            Alerta.erro("Erro ao atualizar informações do Cliente\n" + e.toString());
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
                        case "Aberto":
                            finalizado = false;
                            break;
                        case "Finalizado":
                            finalizado = true;
                            break;
                    }

                    this.novaManutencao.setCliente(cliente);
                    this.novaManutencao.setAdministrador(LoginController.admLogado);
                    this.novaManutencao.setDescricao(descricaoArea.getText());
                    this.novaManutencao.setPreco(Integer.parseInt(precoText.getText()));
                    // nao sei se essa data esta sendo pega corretamente
                    this.novaManutencao.setDataCadastro(dataDatePicker.getValue().toEpochDay());
                    this.novaManutencao.setDataEntrega(dataDatePicker.getValue().toEpochDay());
                    this.novaManutencao.setDataPrevisaoEntrega(dataDatePicker.getValue().toEpochDay());
                    this.novaManutencao.setMarca(marcaText.getText());
                    this.novaManutencao.setCor(corColorPicker.getValue().toString());
                    this.novaManutencao.setModelo(modeloText.getText());
                    this.novaManutencao.setImei(imeiText.getText());

                    try {
                        Long id = ControleDAO.getBanco().getManutencaoDAO().inserir(novaManutencao);

                        // id esta ficando nulo
                        if (id == null) {
                            System.out.println("id null");
                            Alerta.erro("Erro ao adicionar nova Manutenção!");
                        } else {
                            Alerta.info("Manutenção cadastrada com sucesso!");
                            TelaInicialController telaInicial = new TelaInicialController(painelPrincipal);
                            this.adicionarPainelInterno(telaInicial);
                        }
                    } catch (Exception e) {
                        Alerta.erro("Erro ao adicionar nova Manutenção!\n" + e.toString());
                    }

                }

            } // não quer adicionar uma noma manutenção
            else {
                System.out.println("ele NÃO deseja concluir a manutenção");
            }

        }

        /*
        if (cliente == null) {
            cliente = new Cliente(null, nomeText.getText(), enderecoText.getText(), cpfText.getText(), rgText.getText(), telefoneText.getText(), cidadeText.getText(), System.currentTimeMillis(), 1);
        }

        Manutencao m = new Manutencao(null, descricaoArea.getText(), cliente, LoginController.admLogado, marcaText.getText(), modeloText.getText(), imeiText.getText(), "cor", dataDatePicker.getValue().toEpochDay(), 0l, 0l, Float.parseFloat(precoText.getText()), true);

        if (ControleDAO.getBanco().getManutencaoDAO().inserir(m) == null) {
            Alerta.erro("Ocorreu um erro ao inserir manutenção!");
        } else {
            Alerta.info("Manutenção cadastrada!");
            this.cancelarOperacao();
        }*/
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
            Platform.runLater(() -> dataDatePicker.requestFocus());//Colocando o Foco
        }
    }

    @FXML
    private void adicionarNovaFormaPagamento() {
        this.formaPagamentoComboBox.setVisible(false);
        //this.novaFormaPagamentoButton.setVisible(false);

        //this.novaFormaPagamentoText.setVisible(true);
        //this.salvarFormaPagamentoButton.setVisible(true);
    }

    @FXML
    private void salvarNovaFormaPagamento() {
        try {
            //ControleDAO.getBanco().getFormaPagamentoDAO().inserir(new FormaPagamento(null, novaFormaPagamentoText.getText(), 12));

            this.formaPagamentoComboBox.getItems().addAll(ControleDAO.getBanco().getFormaPagamentoDAO().listar());
        } catch (SQLException ex) {
            Logger.getLogger(TelaAdicionarManutencaoController.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.formaPagamentoComboBox.setVisible(true);
        //this.novaFormaPagamentoButton.setVisible(true);

        //this.novaFormaPagamentoText.setVisible(false);
        //this.salvarFormaPagamentoButton.setVisible(false);
    }

    private Cliente criarCliente() {
        String nome = nomeText.getText();
        String telefone = telefoneText.getText();
        String cpf = cpfText.getText();
        String rg = rgText.getText();
        String cidade = cidadeText.getText();
        String endereco = enderecoText.getText();

        return new Cliente(null, nome, endereco, cpf, rg, telefone, cidade, null, 0);
    }

    private Cliente atualizarCliente(Cliente cliente) {
        cliente.setNome(nomeText.getText());
        cliente.setTelefone(telefoneText.getText());
        cliente.setCpf(cpfText.getText());
        cliente.setRg(rgText.getText());
        cliente.setCidade(cidadeText.getText());
        cliente.setEndereco(enderecoText.getText());
        return cliente;
    }

    private void adicionarDadosCliente(Cliente cliente) {
        this.nomeText.setText(cliente.getNome());
        this.telefoneText.setText(cliente.getTelefone());
        this.cpfText.setText(cliente.getCpf());
        this.rgText.setText(cliente.getRg());
        this.cidadeText.setText(cliente.getCidade());
        this.enderecoText.setText(cliente.getEndereco());
    }
}
