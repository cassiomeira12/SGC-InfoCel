/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.text.DecimalFormat;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.KeyEvent;

/**
 *
 * @author cassio
 */
public class Formatter {

    private static final String ALFA_NUMERICO = "[^a-zA-Z0-9]";//Letra e numero
    private static final String ALFA_NUMERICO_MINUSCULO = "[^a-z0-9]";//Letra e numero minusculo
    private static final String NUMERICO = "[^0-9]";//Apenas numero
    private static final String ALFA = "[^a-zA-Z]";//Letra maiuscula e minuscula

    public static TextFormatter<String> ALFA_NUMERICO() {
        return new TextFormatter<>(change -> {
            change.setText(change.getText().replaceAll(Formatter.ALFA_NUMERICO, ""));
            return change;
        });
    }

    public static TextFormatter<String> ALFA_NUMERICO_MINUSCULO() {
        return new TextFormatter<>(change -> {
            change.setText(change.getText().replaceAll(Formatter.ALFA_NUMERICO_MINUSCULO, ""));
            return change;
        });
    }

    public static TextFormatter<String> NUMERICO() {
        return new TextFormatter<>(change -> {
            change.setText(change.getText().replaceAll(Formatter.NUMERICO, ""));
            return change;
        });
    }

    public static TextFormatter<String> ALFA() {
        return new TextFormatter<>(change -> {
            change.setText(change.getText().replaceAll(Formatter.ALFA, ""));
            return change;
        });
    }

    /**
     * Não permitir que campos de textos com valores nulos
     */
    public static boolean isEmpty(TextField... field) {
        boolean vazio = false;
        for (TextField campo : field) {
            if (campo.getText().trim().isEmpty()) {
                //erro(campo, "Verificar valor vazio!");
                vazio = true;
            }
        }
        return vazio;
    }

    public static boolean noEmpty(TextField... field) {
        boolean vazio = false;
        for (TextField campo : field) {
            if (!campo.getText().trim().isEmpty()) {
                //erro(campo, "Verificar valor vazio!");
                vazio = true;
            }
        }
        return vazio;
    }

    /**
     * Não permitir que ComboBox estejam vazios
     */
    public static boolean isEmpty(ComboBox... box) {
        boolean vazio = false;
        for (ComboBox combo : box) {
            if (combo.getSelectionModel().getSelectedItem() == null) {
                vazio = true;
            }
        }
        return vazio;
    }

    /**
     * Limpar textos dos campos informados
     */
    public static void limpar(TextField... no) {
        for (TextField campo : no) {
            campo.setText("");
        }
    }

    /**
     * Limpar textos dos labels informados
     */
    public static void limpar(Label... no) {
        for (Label campo : no) {
            campo.setText("");
        }
    }

    /**
     * Limpar textos dos TextArea informado
     */
    public static void limpar(TextArea... no) {
        for (TextArea campo : no) {
            campo.setText("");
        }
    }

    public static void mascaraNumeroInteiro(TextField textField) {

        textField.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (!newValue.matches("\\d*")) {
                textField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

    }

    public static void mascaraNumero(TextField textField) {

        textField.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            newValue = newValue.replaceAll(",", ".");
            if (newValue.length() > 0) {
                try {
                    Double.parseDouble(newValue);
                    textField.setText(newValue.replaceAll(",", "."));
                } catch (Exception e) {
                    textField.setText(oldValue);
                }
            }
        });

    }

    public static void decimal(TextField campo) {
        campo.lengthProperty().addListener((ObservableValue<? extends Number> obs, Number old, Number novo) -> {
            if (novo.intValue() > old.intValue()) {
                char ch = campo.getText().charAt(old.intValue());
                if (!(ch >= '0' && ch <= '9' || ch == '.')) {
                    campo.setText(campo.getText().substring(0, campo.getText().length() - 1));
                }
            }
        });
    }

    public static void mascaraCEP(TextField textField) {

        String val = "";

        textField.setOnKeyTyped((KeyEvent event) -> {
            if ("0123456789".contains(event.getCharacter()) == false) {
                event.consume();
            }

            if (event.getCharacter().trim().length() == 0) { // apagando

                if (textField.getText().length() == 6) {
                    textField.setText(textField.getText().substring(0, 5));
                    textField.positionCaret(textField.getText().length());
                }

            } else { // escrevendo

                if (textField.getText().length() == 9) {
                    event.consume();
                }

                if (textField.getText().length() == 5) {
                    textField.setText(textField.getText() + "-");
                    textField.positionCaret(textField.getText().length());
                }

            }
        });

        textField.setOnKeyReleased((KeyEvent evt) -> {

            if (!textField.getText().matches("\\d-*")) {
                textField.setText(textField.getText().replaceAll("[^\\d-]", ""));
                textField.positionCaret(textField.getText().length());
            }
        });

    }

    public static void mascaraData(TextField textField) {

        textField.setOnKeyTyped((KeyEvent event) -> {
            if ("0123456789".contains(event.getCharacter()) == false) {
                event.consume();
            }

            if (event.getCharacter().trim().length() == 0) { // apagando

                if (textField.getText().length() == 3) {
                    textField.setText(textField.getText().substring(0, 2));
                    textField.positionCaret(textField.getText().length());
                }
                if (textField.getText().length() == 6) {
                    textField.setText(textField.getText().substring(0, 5));
                    textField.positionCaret(textField.getText().length());
                }

            } else { // escrevendo

                if (textField.getText().length() == 10) {
                    event.consume();
                }

                if (textField.getText().length() == 2) {
                    textField.setText(textField.getText() + "/");
                    textField.positionCaret(textField.getText().length());
                }
                if (textField.getText().length() == 5) {
                    textField.setText(textField.getText() + "/");
                    textField.positionCaret(textField.getText().length());
                }

            }
        });

        textField.setOnKeyReleased((KeyEvent evt) -> {

            if (!textField.getText().matches("\\d/*")) {
                textField.setText(textField.getText().replaceAll("[^\\d/]", ""));
                textField.positionCaret(textField.getText().length());
            }
        });

    }

    public static void mascaraData(DatePicker datePicker) {

        datePicker.getEditor().setOnKeyTyped((KeyEvent event) -> {
            if ("0123456789".contains(event.getCharacter()) == false) {
                event.consume();
            }

            if (event.getCharacter().trim().length() == 0) { // apagando
                if (datePicker.getEditor().getText().length() == 3) {
                    datePicker.getEditor().setText(datePicker.getEditor().getText().substring(0, 2));
                    datePicker.getEditor().positionCaret(datePicker.getEditor().getText().length());
                }
                if (datePicker.getEditor().getText().length() == 6) {
                    datePicker.getEditor().setText(datePicker.getEditor().getText().substring(0, 5));
                    datePicker.getEditor().positionCaret(datePicker.getEditor().getText().length());
                }

            } else { // escrevendo

                if (datePicker.getEditor().getText().length() == 10) {
                    event.consume();
                }

                if (datePicker.getEditor().getText().length() == 2) {
                    datePicker.getEditor().setText(datePicker.getEditor().getText() + "/");
                    datePicker.getEditor().positionCaret(datePicker.getEditor().getText().length());
                }
                if (datePicker.getEditor().getText().length() == 5) {
                    datePicker.getEditor().setText(datePicker.getEditor().getText() + "/");
                    datePicker.getEditor().positionCaret(datePicker.getEditor().getText().length());
                }

            }
        });

        datePicker.getEditor().setOnKeyReleased((KeyEvent evt) -> {

            if (!datePicker.getEditor().getText().matches("\\d/*")) {
                datePicker.getEditor().setText(datePicker.getEditor().getText().replaceAll("[^\\d/]", ""));
                datePicker.getEditor().positionCaret(datePicker.getEditor().getText().length());
            }
        });

    }

    public static void mascaraCPF(TextField textField) {

        textField.setOnKeyTyped((KeyEvent event) -> {
            if ("0123456789".contains(event.getCharacter()) == false) {
                event.consume();
            }

            if (event.getCharacter().trim().length() == 0) { // apagando

                if (textField.getText().length() == 4) {
                    textField.setText(textField.getText().substring(0, 3));
                    textField.positionCaret(textField.getText().length());
                }
                if (textField.getText().length() == 8) {
                    textField.setText(textField.getText().substring(0, 7));
                    textField.positionCaret(textField.getText().length());
                }
                if (textField.getText().length() == 12) {
                    textField.setText(textField.getText().substring(0, 11));
                    textField.positionCaret(textField.getText().length());
                }

            } else { // escrevendo

                if (textField.getText().length() == 14) {
                    event.consume();
                }

                if (textField.getText().length() == 3) {
                    textField.setText(textField.getText() + ".");
                    textField.positionCaret(textField.getText().length());
                }
                if (textField.getText().length() == 7) {
                    textField.setText(textField.getText() + ".");
                    textField.positionCaret(textField.getText().length());
                }
                if (textField.getText().length() == 11) {
                    textField.setText(textField.getText() + "-");
                    textField.positionCaret(textField.getText().length());
                }

            }
        });

        textField.setOnKeyReleased((KeyEvent evt) -> {

            if (!textField.getText().matches("\\d.-*")) {
                textField.setText(textField.getText().replaceAll("[^\\d.-]", ""));
                textField.positionCaret(textField.getText().length());
            }
        });

    }

    public static void mascaraRG(TextField textField) {

        textField.setOnKeyTyped((KeyEvent event) -> {
            if ("0123456789".contains(event.getCharacter()) == false) {
                event.consume();
            }

            if (event.getCharacter().trim().length() == 0) { // apagando

                if (textField.getText().length() == 4) {
                    textField.setText(textField.getText().substring(0, 3));
                    textField.positionCaret(textField.getText().length());
                }
                if (textField.getText().length() == 8) {
                    textField.setText(textField.getText().substring(0, 7));
                    textField.positionCaret(textField.getText().length());
                }
                if (textField.getText().length() == 12) {
                    textField.setText(textField.getText().substring(0, 11));
                    textField.positionCaret(textField.getText().length());
                }

            } else { // escrevendo

                if (textField.getText().length() == 13) {
                    event.consume();
                }

                if (textField.getText().length() == 2) {
                    textField.setText(textField.getText() + ".");
                    textField.positionCaret(textField.getText().length());
                }
                if (textField.getText().length() == 6) {
                    textField.setText(textField.getText() + ".");
                    textField.positionCaret(textField.getText().length());
                }
                if (textField.getText().length() == 10) {
                    textField.setText(textField.getText() + "-");
                    textField.positionCaret(textField.getText().length());
                }

            }
        });

        textField.setOnKeyReleased((KeyEvent evt) -> {

            if (!textField.getText().matches("\\d.-*")) {
                textField.setText(textField.getText().replaceAll("[^\\d.-]", ""));
                textField.positionCaret(textField.getText().length());
            }
        });

    }

    public static void mascaraCNPJ(TextField textField) {

        textField.setOnKeyTyped((KeyEvent event) -> {
            if ("0123456789".contains(event.getCharacter()) == false) {
                event.consume();
            }

            if (event.getCharacter().trim().length() == 0) { // apagando

                if (textField.getText().length() == 3) {
                    textField.setText(textField.getText().substring(0, 2));
                    textField.positionCaret(textField.getText().length());
                }
                if (textField.getText().length() == 7) {
                    textField.setText(textField.getText().substring(0, 6));
                    textField.positionCaret(textField.getText().length());
                }
                if (textField.getText().length() == 11) {
                    textField.setText(textField.getText().substring(0, 10));
                    textField.positionCaret(textField.getText().length());
                }
                if (textField.getText().length() == 16) {
                    textField.setText(textField.getText().substring(0, 15));
                    textField.positionCaret(textField.getText().length());
                }

            } else { // escrevendo

                if (textField.getText().length() == 18) {
                    event.consume();
                }

                if (textField.getText().length() == 2) {
                    textField.setText(textField.getText() + ".");
                    textField.positionCaret(textField.getText().length());
                }
                if (textField.getText().length() == 6) {
                    textField.setText(textField.getText() + ".");
                    textField.positionCaret(textField.getText().length());
                }
                if (textField.getText().length() == 10) {
                    textField.setText(textField.getText() + "/");
                    textField.positionCaret(textField.getText().length());
                }
                if (textField.getText().length() == 15) {
                    textField.setText(textField.getText() + "-");
                    textField.positionCaret(textField.getText().length());
                }

            }
        });

        textField.setOnKeyReleased((KeyEvent evt) -> {

            if (!textField.getText().matches("\\d./-*")) {
                textField.setText(textField.getText().replaceAll("[^\\d./-]", ""));
                textField.positionCaret(textField.getText().length());
            }
        });

    }

    public static void mascaraEmail(TextField textField) {

        textField.setOnKeyTyped((KeyEvent event) -> {
            if ("ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyz._-@".contains(event.getCharacter()) == false) {
                event.consume();
            }

            if ("@".equals(event.getCharacter()) && textField.getText().contains("@")) {
                event.consume();
            }

            if ("@".equals(event.getCharacter()) && textField.getText().length() == 0) {
                event.consume();
            }
        });

    }

    public static void mascaraTelefone(TextField textField) {

        textField.setOnKeyTyped((KeyEvent event) -> {
            if ("0123456789".contains(event.getCharacter()) == false) {
                event.consume();
            }

            if (event.getCharacter().trim().length() == 0) { // apagando

                if (textField.getText().length() == 10 && textField.getText().substring(9, 10).equals("-")) {
                    textField.setText(textField.getText().substring(0, 9));
                    textField.positionCaret(textField.getText().length());
                }
                if (textField.getText().length() == 9 && textField.getText().substring(8, 9).equals("-")) {
                    textField.setText(textField.getText().substring(0, 8));
                    textField.positionCaret(textField.getText().length());
                }
                if (textField.getText().length() == 4) {
                    textField.setText(textField.getText().substring(0, 3));
                    textField.positionCaret(textField.getText().length());
                }
                if (textField.getText().length() == 1) {
                    textField.setText("");
                }

            } else { //escrevendo

                if (textField.getText().length() == 14) {
                    event.consume();
                }

                if (textField.getText().length() == 0) {
                    textField.setText("(" + event.getCharacter());
                    textField.positionCaret(textField.getText().length());
                    event.consume();
                }
                if (textField.getText().length() == 3) {
                    textField.setText(textField.getText() + ")" + event.getCharacter());
                    textField.positionCaret(textField.getText().length());
                    event.consume();
                }
                if (textField.getText().length() == 8) {
                    textField.setText(textField.getText() + "-" + event.getCharacter());
                    textField.positionCaret(textField.getText().length());
                    event.consume();
                }
                if (textField.getText().length() == 9 && textField.getText().substring(8, 9) != "-") {
                    textField.setText(textField.getText() + "-" + event.getCharacter());
                    textField.positionCaret(textField.getText().length());
                    event.consume();
                }
                if (textField.getText().length() == 13 && textField.getText().substring(8, 9).equals("-")) {
                    textField.setText(textField.getText().substring(0, 8) + textField.getText().substring(9, 10) + "-" + textField.getText().substring(10, 13) + event.getCharacter());
                    textField.positionCaret(textField.getText().length());
                    event.consume();
                }

            }

        });

        textField.setOnKeyReleased((KeyEvent evt) -> {

            if (!textField.getText().matches("\\d()-*")) {
                textField.setText(textField.getText().replaceAll("[^\\d()-]", ""));
                textField.positionCaret(textField.getText().length());
            }
        });

    }

    public static void maxField(TextField textField, Integer length) {
        textField.textProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue == null || newValue.length() > length) {
                textField.setText(oldValue);
            }
        });
    }

    public static void toUpperCase(TextInputControl... textFields) {
        for (TextInputControl tf : textFields) {
            tf.textProperty().addListener((ov, oldValue, newValue) -> {
                tf.setText(newValue.toUpperCase());
            });
        }
    }

    public static String dinheiroFormatado(Number num) {
        return new DecimalFormat("#,###.00").format(num.doubleValue());
    }
    
    public static void teste(TextField textField) {

        textField.setOnKeyTyped(event -> {
            String typedCharacter = event.getCharacter();
            event.consume();

            if (typedCharacter.matches("\\d*")) {
                String currentText = textField.getText().replaceAll("\\.", "").replace(",", "");
                long longVal = Long.parseLong(currentText.concat(typedCharacter));
                textField.setText(new DecimalFormat("#,###").format(longVal));
            }
        });

    }
}
