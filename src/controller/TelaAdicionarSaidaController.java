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
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javax.swing.SwingWorker;
import model.Administrador;
import model.CategoriaSaida;
import model.Saida;
import util.Formatter;
import util.alerta.Alerta;
import util.alerta.Dialogo;

/**
 * FXML Controller class
 *
 * @author cassio
 */
public class TelaAdicionarSaidaController extends AnchorPane {

    private BorderPane painelPrincipal;
    
    private List<CategoriaSaida> listaCategoriaSaida;
    private List<Administrador> listaAdministrador;

    @FXML
    private HBox categoriaBox;
    @FXML
    private ComboBox<CategoriaSaida> categoriaComboBox;
    
    @FXML
    private HBox adicionarCategoriaBox;
    @FXML
    private TextField adicionarCategoriaText;
    @FXML
    private ComboBox<Administrador> administradorCombo;
    @FXML
    private DatePicker dataDatePicker;
    @FXML
    private TextArea descricaoArea;
    @FXML
    private TextField valorText;
    @FXML
    private Button finalizarButton;

    public TelaAdicionarSaidaController(BorderPane painelPrincipal) {
        this.painelPrincipal = painelPrincipal;

        try {
            FXMLLoader fxml = new FXMLLoader(getClass().getResource("/view/TelaAdicionarSaida.fxml"));
            fxml.setRoot(this);
            fxml.setController(this);
            fxml.load();
        } catch (IOException ex) {
            System.out.println("[ERRO] : Erro na tela Adicionar Saida");
            ex.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        this.dataDatePicker.setValue(LocalDate.now());//Adicionando Data do dia atual
        Formatter.decimal(valorText);
        Formatter.toUpperCase(descricaoArea);
        
        finalizarButton.disableProperty().bind(categoriaComboBox.selectionModelProperty().isNull().or(
                                               descricaoArea.textProperty().isEmpty().or(
                                               valorText.textProperty().isEmpty())));
        
        sincronizarBancoDadosSaida();
        sincronizarBancoDadosAdministrador();
    }

    private void adicionarPainelInterno(AnchorPane novaTela) {
        this.painelPrincipal.setCenter(novaTela);
    }
    
    @FXML
    private void adicionarCategoria() {
        categoriaBox.setVisible(false);
        adicionarCategoriaBox.setVisible(true);
        
        finalizarButton.disableProperty().bind(adicionarCategoriaText.textProperty().isEmpty().or(
                                               descricaoArea.textProperty().isEmpty().or(
                                               valorText.textProperty().isEmpty())));
        
        Platform.runLater(() -> adicionarCategoriaText.requestFocus());//Colocando o Foco
    }
    
    @FXML
    private void selecionarCategoria() {
        categoriaBox.setVisible(true);
        adicionarCategoriaBox.setVisible(false);
        Formatter.limpar(adicionarCategoriaText);
        
        finalizarButton.disableProperty().bind(categoriaComboBox.selectionModelProperty().isNull().or(
                                               descricaoArea.textProperty().isEmpty().or(
                                               valorText.textProperty().isEmpty())));
        
        Platform.runLater(() -> categoriaComboBox.requestFocus());//Colocando o Foco
    }

    @FXML
    private void cancelarOperacao() {
        TelaInicialController telaInicial = new TelaInicialController(painelPrincipal);
        this.adicionarPainelInterno(telaInicial);
    }
    
    @FXML
    private void finalizar() {
        boolean novaCategoria = adicionarCategoriaBox.isVisible();
        CategoriaSaida categoriaSaida;
        Long data = dataDatePicker.getValue().toEpochDay();
        float valor = Float.parseFloat(valorText.getText());
        String descricao = descricaoArea.getText();
        Administrador administrador = administradorCombo.getValue();
        
        Dialogo.Resposta resposta = Alerta.confirmar("Deseja Adicionar uma nova Saída ?");
        if (resposta == Dialogo.Resposta.YES) {
            
            if (novaCategoria) {
                String descricaoCategoria = adicionarCategoriaText.getText();
                categoriaSaida = new CategoriaSaida(null, descricaoCategoria);
            } else {
                categoriaSaida = categoriaComboBox.getValue();
            }
            
            Saida novaSaida = new Saida(null, administrador, descricao, categoriaSaida, valor, data);
            
            try {
                ControleDAO.getBanco().getSaidaDAO().inserir(novaSaida);
                Alerta.info("Saída adicionada com sucesso!");
                cancelarOperacao();
            } catch (Exception ex) {
                Alerta.erro("Erro ao adicionar uma nova Saída");
                ex.printStackTrace();
            }
            
        }
    }
    
    private void sincronizarBancoDadosSaida() {
        //Metodo executado numa Thread separada
        SwingWorker<List, List> worker = new SwingWorker<List, List>() {
            @Override
            protected List<CategoriaSaida> doInBackground() throws Exception {
                return ControleDAO.getBanco().getCategoriaSaidaDAO().listar();
            }

            //Metodo chamado apos terminar a execucao numa Thread separada
            @Override
            protected void done() {
                super.done(); //To change body of generated methods, choose Tools | Templates.
                try {
                    listaCategoriaSaida = this.get();
                    ObservableList itens = FXCollections.observableArrayList(listaCategoriaSaida);
                    categoriaComboBox.setItems(itens);
                } catch (InterruptedException | ExecutionException ex) {
                    Alerta.erro("Erro ao consultar Banco de Dados");
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
                    administradorCombo.setItems(administradores);

                    administradorCombo.getSelectionModel().select(LoginController.admLogado);
                } catch (InterruptedException | ExecutionException ex) {
                    Alerta.erro("Erro ao consultar Banco de Dados");
                    ex.printStackTrace();
                }
            }
        };
        worker.execute();
    }

}
