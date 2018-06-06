/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author cassio
 */
public class BackupRestauracaoConfiguracoesController implements Initializable {

    @FXML
    private CheckBox backupAutomaticoCheckBox;
    @FXML
    private Spinner<?> diasSpinner;
    @FXML
    private TextField caminhoBackupText;
    @FXML
    private Label ultimoBackupLabel;
    @FXML
    private Label proximoBackupLabel;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void realizarBackup(ActionEvent event) {
    }

    @FXML
    private void importar(ActionEvent event) {
    }

    @FXML
    private void alterar(ActionEvent event) {
    }
    
}
