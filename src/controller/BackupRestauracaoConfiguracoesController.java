/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import app.Painel;
import backup.BackupRestauracao;
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
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javax.swing.SwingWorker;
import org.apache.log4j.Logger;
import util.Config;
import util.DateUtils;
import util.alerta.Alerta;
import util.alerta.Dialogo;

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
    private Button btnImportar, btnBackup;
    @FXML
    private TextField caminhoComprovantesText;

    @FXML
    private VBox backupBox, comprovantesBox;

    @FXML
    private StackPane stackPane;
    private ProgressIndicator indicator = new ProgressIndicator();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnBackup.setDisable(false);
        btnImportar.setDisable(false);

        config = Painel.config;
        stackPane.getChildren().add(indicator);
        indicator.setVisible(false);

        backupAutomaticoCheckBox.setSelected(config.BACKUP_AUTOMATICO);
        caminhoBackupText.appendText(config.DIRETORIO_BACKUP);
        caminhoComprovantesText.appendText(config.DIRETORIO_RELATORIOS);

        if (config.ULTIMO_BACKUP != null) {
            ultimoBackupLabel.setText(config.getUltimoBackup());
        }
        if (config.PROXIMO_BACKUP != null) {
            proximoBackupLabel.setText(config.getProximoBackup());
        }

        diasSpinner.disableProperty().bind(backupAutomaticoCheckBox.selectedProperty().not());
        ultimoBackupLabel.disableProperty().bind(backupAutomaticoCheckBox.selectedProperty().not());
        proximoBackupLabel.disableProperty().bind(backupAutomaticoCheckBox.selectedProperty().not());
        //btnBackup.disableProperty().bind(caminhoBackupText.textProperty().isEmpty());

        SpinnerValueFactory<Integer> valores = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 5, config.BACKUP_A_CADA_DIA);
        diasSpinner.setValueFactory(valores);

        diasSpinner.setOnMouseClicked((e) -> {
            int dias = diasSpinner.getValue();
            config.BACKUP_A_CADA_DIA = dias;
            LocalDate ultimoBackup = DateUtils.createLocalDate(config.ULTIMO_BACKUP);
            LocalDate proximoBackup = ultimoBackup.plusDays(dias);
            config.PROXIMO_BACKUP = DateUtils.getLong(proximoBackup);
            proximoBackupLabel.setText(config.getProximoBackup());
            try {
                config.salvarArquivo();
            } catch (Exception ex) {
                Logger.getLogger(getClass()).error(ex);
                ex.printStackTrace();
            }
        });

        backupAutomaticoCheckBox.setOnAction((e) -> {
            boolean selecionado = backupAutomaticoCheckBox.isSelected();
            config.BACKUP_AUTOMATICO = selecionado;
            if (selecionado) {

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
            try {
                config.salvarArquivo();
            } catch (Exception ex) {
                Logger.getLogger(getClass()).error(ex);
                ex.printStackTrace();
            }
        });
    }

    private void atualizarDatas(Long data) {
        if (config.BACKUP_AUTOMATICO) {
            int dias = diasSpinner.getValue();
            config.ULTIMO_BACKUP = data;

            LocalDate ultimoBackup = DateUtils.createLocalDate(data);
            LocalDate proximoBackup = ultimoBackup.plusDays(dias);
            config.PROXIMO_BACKUP = DateUtils.getLong(proximoBackup);

            proximoBackupLabel.setText(config.getProximoBackup());
            ultimoBackupLabel.setText(config.getUltimoBackup());

            try {
                config.salvarArquivo();
            } catch (Exception ex) {
                Logger.getLogger(getClass()).error(ex);
                ex.printStackTrace();
            }
        }
    }

    @FXML
    private void realizarBackup(ActionEvent event) {
        backupBox.setVisible(false);
        comprovantesBox.setVisible(false);
        btnBackup.setVisible(false);
        btnImportar.setVisible(false);
        indicator.setVisible(true);
        
        String nome = "Backup " + DateUtils.getDataHoraPonto(System.currentTimeMillis());
        String path = caminhoBackupText.getText()+Config.getBarra() + nome + ".sql";


        //Metodo executado numa Thread separada
        SwingWorker<Boolean, Boolean> worker = new SwingWorker<Boolean, Boolean>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                File pasta = new File(path);
                if (pasta.exists() == false) {
                    pasta.mkdirs();
                }
               
                return BackupRestauracao.exportar(path);
            }

            //Metodo chamado apos terminar a execucao numa Thread separada
            @Override
            protected void done() {
                backupBox.setVisible(true);
                comprovantesBox.setVisible(true);
                btnBackup.setVisible(true);
                btnImportar.setVisible(true);
                indicator.setVisible(false);

                super.done(); //To change body of generated methods, choose Tools | Templates.

                try {
                    if (get()) {
                        Alerta.info("Nome do arquivo: " + (new File(path)).getName(), "Backup realizado com sucesso!");
                        atualizarDatas(System.currentTimeMillis());
                    } else {
                        Alerta.erro("Erro ao realizar backup");
                    }
                } catch (Exception ex) {
                    Logger.getLogger(getClass()).error(ex);
                    Alerta.erro("Erro ao realizar backup", ex.getMessage());
                    ex.printStackTrace();
                }
            }
        };

        worker.execute();
    }

    @FXML
    private void importar(ActionEvent event) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Escolha o diretória para backup");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("SQL files (*.sql)", "*.sql");
        chooser.getExtensionFilters().add(extFilter);

        File arquivo = chooser.showOpenDialog(Painel.palco);

        String path = arquivo.getAbsolutePath();

        caminhoBackupText.setText(path);

        Dialogo.Resposta resposta = Alerta.confirmar("Deseja realmente importar o Banco de Dados " + arquivo.getName());
        if (resposta == Dialogo.Resposta.YES) {

        }

    }

    private void importarBancoDados() {

        indicator.setVisible(true);
        //Metodo executado numa Thread separada
        SwingWorker<Boolean, Boolean> worker = new SwingWorker<Boolean, Boolean>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                if (BackupRestauracao.importar(caminhoBackupText.getText())) {
                    return true;
                } else {
                    return false;
                }
            }

            //Metodo chamado apos terminar a execucao numa Thread separada
            @Override
            protected void done() {
                //btnAlterar.setDisable(false);
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
                } catch (Exception ex) {
                    Logger.getLogger(getClass()).error(ex);
                    Alerta.erro("Erro ao realizar importação", ex.getMessage());
                    ex.printStackTrace();
                }
            }
        };

        worker.execute();
    }

    @FXML
    private void alterarBackup(ActionEvent event) {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Escolha o diretório para backup");

        File pasta = new File(config.DIRETORIO_BACKUP);
        if (pasta.exists() == false) {
            pasta.mkdirs();
        }
        chooser.setInitialDirectory(new File(config.DIRETORIO_BACKUP));

        File arquivo = chooser.showDialog(Painel.palco);

        if (arquivo != null) {
            String diretorio = arquivo.getAbsolutePath();
            System.out.println(diretorio);

            caminhoBackupText.setText(diretorio);
            config.DIRETORIO_BACKUP = diretorio;
            try {
                config.salvarArquivo();
            } catch (Exception ex) {
                Logger.getLogger(getClass()).error(ex);
                ex.printStackTrace();
            }
        }
    }

    @FXML
    private void alterarComprovantes(ActionEvent event) {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Escolha o diretória para backup");

        File pasta = new File(config.DIRETORIO_RELATORIOS);
        if (pasta.exists() == false) {
            pasta.mkdirs();
        }
        chooser.setInitialDirectory(new File(config.DIRETORIO_RELATORIOS));

        File arquivo = chooser.showDialog(Painel.palco);

        if (arquivo != null) {
            String diretorio = arquivo.getAbsolutePath();

            caminhoComprovantesText.setText(diretorio);
            config.DIRETORIO_RELATORIOS = diretorio;

            try {
                config.salvarArquivo();
            } catch (Exception ex) {
                Logger.getLogger(getClass()).error(ex);
                ex.printStackTrace();
            }
        }
    }
}
