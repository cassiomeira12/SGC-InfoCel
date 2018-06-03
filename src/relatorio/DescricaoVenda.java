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
public class DescricaoVenda extends Thread{

    ConexaoBanco conn = new ConexaoBanco();
    Statement stm;
    String query;
    String idVenda;
    ResultSet rs;
    JRResultSetDataSource jrRS;
    Map parameters;
    String src;
    JasperPrint jp = null;
    JasperViewer view;
    
    public DescricaoVenda(String id){
    this.idVenda = id;
    }
    
    @Override
    public void run(){
        try {
            sleep(2000);
        } catch (InterruptedException ex) {
            Logger.getLogger(DescricaoVenda.class.getName()).log(Level.SEVERE, null, ex);
        }
        descricaoVenda(idVenda);
    }
    
    public void descricaoVenda(String id) {
        try {
            this.stm = conn.getConnection().createStatement();
        } catch (SQLException ex) {
            Logger.getLogger(DescricaoVenda.class.getName()).log(Level.SEVERE, null, ex);
        }
        // consulta a ser mostrada no relatorio
        query = "select V.id, C.nome, C.cpf, C.rg, C.telefone, CI.nome   AS nome_cidade, B.nome AS nome_bairro, E.rua, E.numero, V.preco_total " +
                "from venda V, cliente C, cidade CI, bairro B, endereco E " +
                "where V.id_cliente = C.id and " +
			"C.id_endereco = E.id and " +
			"E.id_bairro = B.id and " +
			"B.id_cidade = CI.id and " +
			"V.id = " + id ;
			
        try {
            rs = stm.executeQuery(query);
        } catch (SQLException ex) {
            Logger.getLogger(DescricaoVenda.class.getName()).log(Level.SEVERE, null, ex);
        }
        jrRS = new JRResultSetDataSource(rs);
        parameters = new HashMap();
        parameters.put("id_venda", id);
        parameters.put("REPORT_CONNECTION",conn.getConnection());
        try { // caminho do arquivo jasper
            src = new File("src/relatorio/descricaoVenda.jasper").getCanonicalPath();
        } catch (IOException ex) {
            Logger.getLogger(DescricaoVenda.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            jp = JasperFillManager.fillReport(src, parameters, jrRS);
        } catch (JRException ex) {
            System.out.println("Error: " + ex);
        }
         view = new JasperViewer(jp, false);
         view.setVisible (true); 
    }

   
}