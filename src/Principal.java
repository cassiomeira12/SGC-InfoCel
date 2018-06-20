/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import app.Painel;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.event.EventHandler;
import javafx.stage.WindowEvent;
import org.apache.log4j.Logger;

public class Principal extends Application {
    
    @Override
    public void start(Stage palco) {
        
        try {
            Painel painel = new Painel();
            painel.start(palco);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        /*********************************************
        * Metodo: setOnCloseRequest
        * Funcao: Finaliza o programa por completo ao Fechar 
        * Parametros: EventHandler Retorno: void
        *********************************************/
        palco.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                System.out.println("Fechou o Programa");
                t.consume();
                palco.close();
                System.exit(0);
            }
        });

    }//Fim start

    public static void main(String[] args) {
        Application.launch(args);
    }

}//Fim class
