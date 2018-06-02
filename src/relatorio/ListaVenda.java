/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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

import util.DateUtils;

/**
 *
 * @author dhonl
 */
public class ListaVenda extends Thread {

    ConexaoBanco conn = new ConexaoBanco();
    Statement stm;
    String query;

    ResultSet rs;
    JRResultSetDataSource jrRS;
    Map parameters;
    String src;
    JasperPrint jp;
    Long data = 1346524199000l;
    //Venda venda;

    JasperViewer view;

    @Override
    public void run() {
        try {
            sleep(0);
            listaVenda();
        } catch (InterruptedException ex) {
            Logger.getLogger(ListaVenda.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void listaVenda() {

        try {
            this.stm = conn.getConnection().createStatement();
        } catch (SQLException ex) {
            Logger.getLogger(DescricaoVenda.class.getName()).log(Level.SEVERE, null, ex);
        }
        // consulta a ser mostrada no relatorio
        query = "select * from venda";

        try {
            rs = stm.executeQuery(query);

        } catch (SQLException ex) {
            Logger.getLogger(DescricaoVenda.class.getName()).log(Level.SEVERE, null, ex);
        }
        jrRS = new JRResultSetDataSource(rs);
        parameters = new HashMap();
        parameters.put("data_venda", DateUtils.formatDate(data)); //DateUtils.formatDate(data)
        parameters.put("data_venda_2", "21/21/21");

        try { // caminho do arquivo jasper
            src = new File("src/relatorio/listaVendas.jasper").getCanonicalPath();
            //System.out.println("caminho" + src);

        } catch (IOException ex) {
            System.out.println("[ERRO] : Erro ao gerar relatorio de vendas");
        }

        try {
            jp = JasperFillManager.fillReport(src, parameters, jrRS);

        } catch (JRException ex) {
            System.out.println("Error: " + ex);
        }

        view = new JasperViewer(jp, false);
        view.setVisible(true);

    }

}
