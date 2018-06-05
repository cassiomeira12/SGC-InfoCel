package relatorio;

import banco.ConexaoBanco;
import banco.ControleDAO;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.view.JasperViewer;
import util.SoftwareSpecifications;

/**
 *
 * @author dhonl
 */
public class DescricaoManutencao extends Thread {

    ConexaoBanco conn = new ConexaoBanco();
    Statement stm;
    String query;
    Long idManutencao;
    ResultSet rs;
    JRResultSetDataSource jrRS;
    Map parameters;
    String srcArquivoJaper;
    String srcSalvarRelatorio;
    JasperPrint jp = null;
    JasperViewer view;

    // construtor da classe
    public DescricaoManutencao(Long id) {
        this.idManutencao = id;
    }

    @Override
    public void run() {
        try {
            sleep(2000);
        } catch (InterruptedException ex) {
            Logger.getLogger(DescricaoManutencao.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            descricaoManutencao(idManutencao);
        } catch (IOException ex) {
            Logger.getLogger(DescricaoManutencao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void descricaoManutencao(Long id) throws IOException {
        try {
            // fazendo conexao com o banco
            this.stm = conn.getConnection().createStatement();
        } catch (SQLException ex) {
            Logger.getLogger(DescricaoManutencao.class.getName()).log(Level.SEVERE, null, ex);
        }
        // consulta passada para o arquivo jasper
        query = "select nome_cliente, cpf_cliente, rg_cliente, telefone_cliente, nome_cidade_cliente, nome_bairro_cliente, rua_cliente, numero_cliente, "
                + "nome_administrador, descricao_forma_pagamento, quantidade_parcela, preco_total "
                + "from view_venda "
                + "where id = " + id.toString();
        // execute a query		
        try {
            rs = stm.executeQuery(query);
        } catch (SQLException ex) {
            Logger.getLogger(DescricaoManutencao.class.getName()).log(Level.SEVERE, null, ex);
        }
        jrRS = new JRResultSetDataSource(rs);
        // passagem de parametros para o jasper
        parameters = new HashMap();
        parameters.put("id_manutencao", id.toString());
        parameters.put("nome_empresa", SoftwareSpecifications.EMPRESA);
        parameters.put("cidade_empresa", SoftwareSpecifications.CIDADE);
        parameters.put("telefone_empresa", SoftwareSpecifications.TELEFONE);
        parameters.put("endereco_empresa", SoftwareSpecifications.ENDERECO);
        parameters.put("cep_empresa", SoftwareSpecifications.CEP);

        try {
            // passando data como parametros por cauda da formatacao
           parameters.put("data_venda", ControleDAO.getBanco().getVendaDAO().buscarPorId(id).getDataEditada());
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        // passando a conexao com o banco para o sub_report 
        parameters.put("REPORT_CONNECTION", conn.getConnection());
        // caminho logo da empresa
        parameters.put("src_logo", SoftwareSpecifications.SRC_LOGO);
        // caminho arquivo jasper
       srcArquivoJaper = new File("src/relatorio/descricaoVenda.jasper").getCanonicalPath();

        try {
            // cria arquivo jasper
            jp = JasperFillManager.fillReport(srcArquivoJaper, parameters, jrRS);

        } catch (JRException ex) {
            System.out.println("Error: " + ex);
        }
        // cria o arquivo de visao
        view = new JasperViewer(jp, false);

        // mostrar o arquivo de visao
        view.setVisible(true);
        
    }
}
