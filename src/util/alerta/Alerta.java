package util.alerta;

import javafx.application.Platform;
import util.alerta.Dialogo.Resposta;

/**
 * Criação de mensagem apartir do classe de dialogo
 */
public class Alerta {

    private Alerta() {
        
    }

    public static void info(String mensagem) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Dialogo.mensagens("INFO", "Informação", mensagem);
            }
        });
    }

    public static void info(String mensagem, String titulo) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Dialogo.mensagens("INFO", titulo, mensagem);
            }
        });
    }

    public static void erro(String mensagem) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Dialogo.mensagens("ERRO", "Erro", mensagem);
            }
        });
    }

    public static void erro(String mensagem, String titulo) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Dialogo.mensagens("ERRO", titulo, mensagem);
            }
        });
    }

    public static void alerta(String mensagem) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Dialogo.mensagens("ALERTA", "Alerta", mensagem);
            }
        });
    }

    public static void alerta(String mensagem, String titulo) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Dialogo.mensagens("ALERTA", titulo, mensagem);
            }
        });
    }

    public static Resposta confirmar(String mensagem) {
//        Platform.runLater(new Runnable() {
//            @Override
//            public void run() {
//                Dialogo.mensagens("INFO", "Informação", mensagem);
//            }
//        });
        return Dialogo.mensageConfirmar("Confirmar", mensagem);
    }

    public static Resposta confirmar(String titulo, String mensagem) {
//        Platform.runLater(new Runnable() {
//            @Override
//            public void run() {
//                Dialogo.mensagens("INFO", "Informação", mensagem);
//            }
//        });
        return Dialogo.mensageConfirmar(titulo, mensagem);
    }
}
