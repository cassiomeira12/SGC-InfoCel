package util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Arquivo {
    
    private static String arquivo = "config";
    
    public static void gravar(Object object, String diretorio) throws Exception {
        FileOutputStream dataBase = new FileOutputStream(diretorio + arquivo);
        ObjectOutputStream objectOutput = new ObjectOutputStream(dataBase);
        objectOutput.writeObject(object);
        objectOutput.close();
    }

    public static Object importar(String diretorio) throws Exception {
        FileInputStream dataBase = new FileInputStream(diretorio + arquivo);
        ObjectInputStream objectInput = new ObjectInputStream(dataBase);
        Object object = objectInput.readObject();
        objectInput.close();
        return object;
    }
}
