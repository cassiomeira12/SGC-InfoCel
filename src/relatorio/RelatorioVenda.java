package relatorio;

import banco.ConexaoBanco;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author dhonl
 */
public class RelatorioVenda {

    ConexaoBanco conn = new ConexaoBanco();
    Statement stm;
    String query;

    ResultSet rs;
    JRResultSetDataSource jrRS;
    Map parameters;
    String src;
    JasperPrint jp = null;

    JasperViewer view = new JasperViewer(jp, false);

    public RelatorioVenda(String id) {
        try {
            this.stm = conn.getConnection().createStatement();
        } catch (SQLException ex) {
            Logger.getLogger(RelatorioVenda.class.getName()).log(Level.SEVERE, null, ex);
        }
        // consulta a ser mostrada no relatorio
        query = "select * from venda where id = " + id;
       
        try {
            rs = stm.executeQuery(query);
        } catch (SQLException ex) {
            Logger.getLogger(RelatorioVenda.class.getName()).log(Level.SEVERE, null, ex);
        }
        jrRS = new JRResultSetDataSource(rs);
        parameters = new HashMap();
        
        try { // caminho do arquivo jasper
            src = new File("src/relatorio/relatorioVenda.jasper").getCanonicalPath();
        } catch (IOException ex) {
            Logger.getLogger(RelatorioVenda.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            jp = JasperFillManager.fillReport(src, parameters, jrRS);
        } catch (JRException ex) {
            System.out.println("Error: " + ex);
        }
        
         view.setVisible (true); 
    }

   
}