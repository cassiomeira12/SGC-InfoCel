/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import banco.ControleDAO;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
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
    Endereco endereco = new Endereco(1l, new Bairro(1l, "Centro", new Cidade(1l, "Vitória da Conquista")), "Rua francisco santos", "149 A");

    private List<Operacao> listaOperacao;
    
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
            System.out.println(ex.toString());
        }
    }

    @FXML
    public void initialize() {
        this.adicionarDadosCliente();
        
        Formatter.toUpperCase(nomeText, cidadeText, enderecoText);
        
        Formatter.mascaraTelefone(telefoneText);
        Formatter.mascaraRG(rgText);
        Formatter.mascaraCPF(cpfText);

        //Campos ficam desativados enquanto CheckBox esta desativado
        nomeText.disableProperty().bind(editarClienteCheckBox.selectedProperty().not());
        telefoneText.disableProperty().bind(editarClienteCheckBox.selectedProperty().not());
        cpfText.disableProperty().bind(editarClienteCheckBox.selectedProperty().not());
        rgText.disableProperty().bind(editarClienteCheckBox.selectedProperty().not());
        cidadeText.disableProperty().bind(editarClienteCheckBox.selectedProperty().not());
        enderecoText.disableProperty().bind(editarClienteCheckBox.selectedProperty().not());

        atualizarOperacoes(cliente);
    }

    public void adicionarDadosCliente() {
        this.nomeText.setText(cliente.getNome());
        this.telefoneText.setText(cliente.getTelefone());
        this.cpfText.setText(cliente.getCpf());
        this.rgText.setText(cliente.getRg());
        this.cidadeText.setText(cliente.getEndereco().getBairro().getCidade().getNome());
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
                    Logger.getLogger(TelaClienteController.class.getName()).log(Level.SEVERE, null, ex);
                }
                //Collections.sort(listaOperacao);//Ordenando as Operacoes
                
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

}
