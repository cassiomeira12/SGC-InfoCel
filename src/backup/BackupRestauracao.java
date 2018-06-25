package backup;

/**
 *
 * @author pedro
 */
import banco.ConexaoBanco;
import banco.dao.DAO;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintStream;
import java.sql.SQLException;
import org.apache.log4j.Logger;

public class BackupRestauracao extends DAO {

    /* private static String url = "jdbc:mysql://neolig.com:3306/";
    //private static String url = "jdbc:mysql://localhost:3306/";
    private static String port = "3306";
    private static String database = "neoli831_teste";
    private static String user = "neoli831_teste";
    private static String pass = "teste";
     */

 /* private static String ip = "localhost";
   // private static String port = "3306";
    private static String database = "neoli831_teste";
    private static String user = "neoli831_teste";
    private static String pass = "teste";
   // private static String path = "/home/Admin/abc/";
     */
    public static boolean exportar(String path) {
        String dumpCommand = "mysqldump -u " + ConexaoBanco.USERNAME + " -p"
                + ConexaoBanco.PASSWORD + " " + ConexaoBanco.DATABASE + ConexaoBanco.getTabelas();

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
        } catch (Exception ex) {
            Logger.getLogger(BackupRestauracao.class).error(ex);
            return false;
        }

        return true;
    }

    public static boolean importar(String path) throws SQLException, FileNotFoundException {
     
        return true;

    }

}
