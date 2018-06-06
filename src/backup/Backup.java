package backup;

/**
 *
 * @author pedro
 */
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.*;

import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Backup {

    private static String url = "jdbc:mysql://neolig.com:3306/";
    //private static String url = "jdbc:mysql://localhost:3306/";
    private static String port = "3306";
    private static String database = "neoli831_teste";
    private static String user = "neoli831_teste";
    private static String pass = "teste";

    public static boolean exportar(String path) {
        String dumpCommand = "mysqldump " + database + " -h " + url + " -u " + user + " -p" + pass;
        Runtime rt = Runtime.getRuntime();
        File test = new File(path);
        PrintStream ps;
        try {
            Process child = rt.exec(dumpCommand);
            ps = new PrintStream(test);
            InputStream in = child.getInputStream();
            int ch;
            while ((ch = in.read()) != -1) {
                ps.write(ch);
            }

            InputStream err = child.getErrorStream();
            while ((ch = err.read()) != -1) {
                System.out.write(ch);
            }
        } catch (Exception exc) {
            exc.printStackTrace();
            return false;
        }

        return true;
    }

    public static boolean importar(String path) {
        try {
            String comando = "/usr/bin/mysql";
            ProcessBuilder pb = new ProcessBuilder(comando, "--user=" + user, "--password=" + pass, "localhost/" + database, "--execute=source " + path);
            pb.start();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return true;
    }
}
