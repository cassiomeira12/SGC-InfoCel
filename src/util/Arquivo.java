package util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Arquivo {
    
    private static String arquivo = "config";
    
    public static boolean gravar(Object object, String diretorio) {
        boolean finalizado = false;
        try {
            FileOutputStream dataBase = new FileOutputStream(diretorio + arquivo);
            ObjectOutputStream objectOutput = new ObjectOutputStream(dataBase);
            objectOutput.writeObject(object);
            objectOutput.close();
            finalizado = true;
            System.out.println("Objeto gravado com sucesso!");
        } catch (FileNotFoundException ex) {
            System.err.println(ex.toString());
        } catch (IOException ex) {
            System.err.println(ex.toString());
        } finally {
            return finalizado;
        }
    }

    public static Object importar(String diretorio) {
        try {
            FileInputStream dataBase = new FileInputStream(diretorio + arquivo);
            ObjectInputStream objectInput = new ObjectInputStream(dataBase);
            Object object = objectInput.readObject();
            objectInput.close();
            System.out.println("Objeto importado com sucesso!");
            return object;
        } catch (FileNotFoundException ex) {
            System.err.println(ex.toString());
        } catch (IOException ex) {
            System.err.println(ex.toString());
        } catch (ClassNotFoundException ex) {
            System.err.println(ex.toString());
        }
        return null;
    }
}
