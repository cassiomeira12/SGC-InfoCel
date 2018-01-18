/** *********************************************************************
 * Autor: Cassio Meira Silva
 * Matricula: 201610373
 * Inicio: 24/10/17
 * Ultima alteracao: 08/11/17
 * Nome: Principal
 * Funcao: Chamar Tela do Programa
 ********************************************************************** */

import app.Painel;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.event.EventHandler;
import javafx.stage.WindowEvent;

public class Principal extends Application {

    @Override
    public void start(Stage palco) {
        
        try {
            Painel painel = new Painel();
            painel.start(palco);
        } catch (Exception e) {
            System.out.println("Erro ao iniciar Painel");
            System.out.println(e.toString());
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
        System.exit(0);
    }

}//Fim class
