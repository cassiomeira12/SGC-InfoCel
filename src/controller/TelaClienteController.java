/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import banco.ControleDAO;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import model.Bairro;
import model.Cidade;
import model.Cliente;
import model.Endereco;
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
    Endereco endereco = new Endereco(1l, new Bairro(1l, "Centro", new Cidade(1l, "Vitória da Conquista")), "Rua francisco santos", "149 A");
    
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
            System.out.println(ex.toString());
        }
    }
    
    @FXML
    public void initialize() {
        this.adicionarDadosCliente();

        //Campos ficam desativados enquanto CheckBox esta desativado
        nomeText.disableProperty().bind(editarClienteCheckBox.selectedProperty().not());
        telefoneText.disableProperty().bind(editarClienteCheckBox.selectedProperty().not());
        cpfText.disableProperty().bind(editarClienteCheckBox.selectedProperty().not());
        rgText.disableProperty().bind(editarClienteCheckBox.selectedProperty().not());
        cidadeText.disableProperty().bind(editarClienteCheckBox.selectedProperty().not());
        enderecoText.disableProperty().bind(editarClienteCheckBox.selectedProperty().not());
        
        Formatter.toUpperCase(nomeText, cidadeText, enderecoText);
    }
    
    public void adicionarDadosCliente() {
        this.nomeText.setText(cliente.getNome());
        this.telefoneText.setText(cliente.getTelefone());
        this.cpfText.setText(cliente.getCpf());
        this.rgText.setText(cliente.getRg());
        this.cidadeText.setText(cliente.getCidade());
        this.enderecoText.setText(cliente.getEndereco().toString());
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
        try {
            boolean vazio = Formatter.isEmpty(nomeText, telefoneText, cpfText, rgText, cidadeText, enderecoText);
            
            String nome = nomeText.getText();
            String telefone = telefoneText.getText();
            String cpf = cpfText.getText();
            String rg = rgText.getText();
            String cidade = cidadeText.getText();
            
            if (vazio) {
                Alerta.alerta("Prencha todos os compos do Cliente");
            } else {
                Dialogo.Resposta resposta = Alerta.confirmar("Deseja salvar as modificações do cliente " + cliente.getNome() + " ?");
                
                if (resposta == Dialogo.Resposta.YES) {
                    
                    cliente.setNome(nome);
                    cliente.setTelefone(telefone);
                    cliente.setCpf(cpf);
                    cliente.setRg(rg);
                    cliente.setCidade(cidade);
                    cliente.setEndereco(endereco);
                    
                    try {
                        if (ControleDAO.getBanco().getClienteDAO().editar(cliente)) {
                            Alerta.info("Cliente editado com sucesso");
                        }
                    } catch (Exception e) {
                        Alerta.erro(e.toString());
                    }
                    this.cancelarOperacao();
                }
                
            }
            
        } catch (NullPointerException ex) {
            Alerta.erro("Erro ao salvar as informações do Cliente");
        }
    }
    
}
