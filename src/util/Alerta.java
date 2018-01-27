/***********************************************************************
 * Autor: Cassio Meira Silva
 * Matricula: 201610373
 * Inicio: --/--/--
 * Ultima alteracao: --/--/--
 * Nome: Alerta
 * Funcao:
 ***********************************************************************/
package util;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class Alerta {
    

    /**********************************************
     * Metodo: Alerta - Construtor 
     * Funcao: Constroi objetos da classe Alerta
     * Parametros: void 
     * Retorno: void
     *********************************************/
    public Alerta() {

    }

    
    /**********************************************
     * Metodo: Alerta - Construtor 
     * Funcao: Constroi objetos da classe Alerta
     * Parametros: void 
     * Retorno: void
     *********************************************/
    public static void erro(String titulo, String cabecalho, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(cabecalho);
        alert.setContentText(mensagem);
        //alert.setGraphic(new ImageView(getClass().getResource("/img/erro.png").toString()));
        alert.show();
    }

    /**********************************************
     * Metodo: Alerta - Construtor 
     * Funcao: Constroi objetos da classe Alerta
     * Parametros: void 
     * Retorno: void
     *********************************************/
    public static void erro(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        //alert.setHeaderText(cabecalho);
        alert.setContentText(mensagem);
        //alert.setGraphic(new ImageView(getClass().getResource("/img/erro.png").toString()));
        alert.show();
    }

    /**********************************************
     * Metodo: Alerta - Construtor 
     * Funcao: Constroi objetos da classe Alerta
     * Parametros: void 
     * Retorno: void
     *********************************************/
    public static void confirmacao(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.show();
        // Optional<ButtonType> result = alert.showAndWait();
        // if (result.get() == ButtonType.OK){
        //     // ... user chose OK
        // } else {
        //     // ... user chose CANCEL or closed the dialog
        // }
    }

    /**********************************************
     * Metodo: Alerta - Construtor 
     * Funcao: Constroi objetos da classe Alerta
     * Parametros: void 
     * Retorno: void
        *********************************************/
    public static void informacao(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.show();
    }

}//Fim class
