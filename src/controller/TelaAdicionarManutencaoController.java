/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import banco.ControleDAO;
import java.io.IOException;
import java.time.LocalDate;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import model.Cliente;
import model.Manuntencao;
import util.alerta.Alerta;

/**
 * FXML Controller class
 *
 * @author cassio
 */
public class TelaAdicionarManutencaoController extends AnchorPane {

    private BorderPane painelPrincipal;
    private Cliente cliente;

    @FXML
    private TextField nomeText;
    @FXML
    private TextField cpfText;
    @FXML
    private TextField rgText;
    @FXML
    private TextField cidadeText;
    @FXML
    private TextField enderecoText;
    @FXML
    private TextField telefoneText;
    @FXML
    private TextArea descricaoArea;
    @FXML
    private TextField marcaText;
    @FXML
    private TextField modeloText;
    @FXML
    private TextField imeiText;
    @FXML
    private TextField precoText;
    @FXML
    private DatePicker dataDatePicker;

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
        this.dataDatePicker.setValue(LocalDate.now());//Adicionando Data do dia atual
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
        if (cliente == null) {
            cliente = new Cliente(null, nomeText.getText(), enderecoText.getText(), cpfText.getText(), rgText.getText(), telefoneText.getText(), cidadeText.getText(), System.currentTimeMillis(), 1);
        }

        Manuntencao m = new Manuntencao(null, descricaoArea.getText(), cliente, LoginController.admLogado, marcaText.getText(), modeloText.getText(), imeiText.getText(), "cor", dataDatePicker.getValue().toEpochDay(), 0l, 0l, Float.parseFloat(precoText.getText()), true);

        if (ControleDAO.getBanco().getManutencaoDAO().inserir(m) == null) {
            Alerta.erro("Ocorreu um erro ao inserir manutenção!");
        } else {
            Alerta.info("Manutenção cadastrada!");
            this.cancelarOperacao();
        }
    }
}
