/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import app.Painel;
import backup.Backup;
import banco.ControleDAO;
import static controller.LoginController.admLogado;
import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javax.swing.JFileChooser;
import javax.swing.SwingWorker;
import util.Config;
import util.DateUtils;
import util.alerta.Alerta;

/**
 * FXML Controller class
 *
 * @author cassio
 */
public class BackupRestauracaoConfiguracoesController implements Initializable {
    
    private Config config;

    @FXML
    private CheckBox backupAutomaticoCheckBox;
    @FXML
    private Spinner<Integer> diasSpinner;
    @FXML
    private TextField caminhoBackupText;
    @FXML
    private Label ultimoBackupLabel;
    @FXML
    private Label proximoBackupLabel;
    @FXML
    private Button btnImportar, btnBackup, btnAlterar;

    @FXML
    private StackPane stackPane;
    private ProgressIndicator indicator = new ProgressIndicator();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        config = Painel.config;
        stackPane.getChildren().add(indicator);
        indicator.setVisible(false);
        
        
        backupAutomaticoCheckBox.setSelected(config.BACKUP_AUTOMATICO);
        if (config.ULTIMO_BACKUP != null) {
            ultimoBackupLabel.setText(config.getUltimoBackup());
        }
        if (config.PROXIMO_BACKUP != null) {
            proximoBackupLabel.setText(config.getProximoBackup());
        }
        
        diasSpinner.disableProperty().bind(backupAutomaticoCheckBox.selectedProperty().not());
        ultimoBackupLabel.disableProperty().bind(backupAutomaticoCheckBox.selectedProperty().not());
        proximoBackupLabel.disableProperty().bind(backupAutomaticoCheckBox.selectedProperty().not());
        
        SpinnerValueFactory<Integer> valores = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 5, config.BACKUP_A_CADA_DIA);
        diasSpinner.setValueFactory(valores);
        
        diasSpinner.setOnMouseClicked((e) -> {
            int dias = diasSpinner.getValue();
            config.BACKUP_A_CADA_DIA = dias;
            LocalDate ultimoBackup = DateUtils.createLocalDate(config.ULTIMO_BACKUP);
            LocalDate proximoBackup = ultimoBackup.plusDays(dias);
            config.PROXIMO_BACKUP = DateUtils.getLong(proximoBackup);
            proximoBackupLabel.setText(config.getProximoBackup());
        });
        
        backupAutomaticoCheckBox.setOnAction((e) -> {
            if (backupAutomaticoCheckBox.isSelected()) {
                
                if (config.ULTIMO_BACKUP == null) {
                    Long hoje = System.currentTimeMillis();
                    config.ULTIMO_BACKUP = hoje;
                }
                
                int dias = diasSpinner.getValue();
                LocalDate ultimoBackup = DateUtils.createLocalDate(config.ULTIMO_BACKUP);
                LocalDate proximoBackup = ultimoBackup.plusDays(dias);
                config.PROXIMO_BACKUP = DateUtils.getLong(proximoBackup);
                
                Long hoje = System.currentTimeMillis();
                if (DateUtils.getLong(proximoBackup) < hoje) {
                    hoje = System.currentTimeMillis();
                    config.ULTIMO_BACKUP = hoje;
                    ultimoBackup = DateUtils.createLocalDate(config.ULTIMO_BACKUP);
                    proximoBackup = ultimoBackup.plusDays(dias);
                    config.PROXIMO_BACKUP = DateUtils.getLong(proximoBackup);
                }
                
                proximoBackupLabel.setText(config.getProximoBackup());
                ultimoBackupLabel.setText(config.getUltimoBackup());
            }
        });
    }

    @FXML
    private void realizarBackup(ActionEvent event) {
        btnAlterar.setDisable(true);
        btnBackup.setDisable(true);
        btnImportar.setDisable(true);
        caminhoBackupText.setDisable(true);
        indicator.setVisible(true);

        //Metodo executado numa Thread separada
        SwingWorker<Boolean, Boolean> worker = new SwingWorker<Boolean, Boolean>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                if (Backup.exportar(caminhoBackupText.getText())) {
                    return true;
                } else {
                    return false;
                }
            }

            //Metodo chamado apos terminar a execucao numa Thread separada
            @Override
            protected void done() {
                btnAlterar.setDisable(false);
                btnBackup.setDisable(false);
                btnImportar.setDisable(false);
                caminhoBackupText.setDisable(false);
                indicator.setVisible(false);
                super.done(); //To change body of generated methods, choose Tools | Templates.

                try {
                    if (get()) {
                        Alerta.info("Nome do arquivo: " + (new File(caminhoBackupText.getText())).getName(), "Backup realizado com sucesso!");
                    } else {
                        Alerta.erro("Erro ao realizar backup");
                    }
                } catch (Exception e) {
                    Alerta.erro("Erro ao realizar backup", e.getMessage());
                }
            }
        };

        worker.execute();
    }

    @FXML
    private void importar(ActionEvent event) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Escolha o diretória para backup");

        String path = chooser.showOpenDialog(null).getAbsoluteFile().getAbsolutePath();
        caminhoBackupText.setText(path);
        indicator.setVisible(true);

        //Metodo executado numa Thread separada
        SwingWorker<Boolean, Boolean> worker = new SwingWorker<Boolean, Boolean>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                if (Backup.importar(caminhoBackupText.getText())) {
                    return true;
                } else {
                    return false;
                }
            }

            //Metodo chamado apos terminar a execucao numa Thread separada
            @Override
            protected void done() {
                btnAlterar.setDisable(false);
                btnBackup.setDisable(false);
                btnImportar.setDisable(false);
                caminhoBackupText.setDisable(false);
                indicator.setVisible(false);
                super.done(); //To change body of generated methods, choose Tools | Templates.

                try {
                    if (get()) {
                        Alerta.info("Nome do arquivo: " + (new File(caminhoBackupText.getText())).getName(), "Importado com sucesso!");
                    } else {
                        Alerta.erro("Erro ao realizar importação");
                    }
                } catch (Exception e) {
                    Alerta.erro("Erro ao realizar importação", e.getMessage());
                }
            }
        };

        worker.execute();
    }

    @FXML
    private void alterar(ActionEvent event) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Escolha o diretória para backup");

        chooser.setInitialDirectory(new File("/home/pedro/Downloads/Infocel/"));
        String path = chooser.showSaveDialog(null).getAbsoluteFile().getAbsolutePath();
        caminhoBackupText.setText(path);

    }

}
