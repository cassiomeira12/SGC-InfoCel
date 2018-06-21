package relatorio;

import app.Painel;
import banco.ConexaoBanco;
import banco.ControleDAO;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import org.apache.log4j.Logger;
import util.DateUtils;
import util.SoftwareSpecifications;

/**
 *
 * @author dhonl
 */
public class DescricaoVenda extends Thread {

    ConexaoBanco conn = new ConexaoBanco();
    Statement stm;
    String query;
    Long idVenda;
    ResultSet rs;
    JRResultSetDataSource jrRS;
    InputStream resourceAsStream;
    Map parameters;
    String srcSalvarRelatorio;
    String nomeClienteVenda;
    JasperPrint jp;
    JasperViewer view;
    boolean mostrar = false;

    // construtor da classe
    public DescricaoVenda(Long id) {
        this.idVenda = id;
    }

    @Override
    public void run() {

        try {
            // metodo que gerar o arquivo e salva como pdf
            gerarDescricaoVenda(idVenda);
        } catch (IOException ex) {
            Logger.getLogger(getClass()).error(ex);
            ex.printStackTrace();
        }
        if (mostrar) {
            // metodo que mostra a descricao da venda na tela
            mostrarDescricaoVenda();
        }
    }

    public void gerarDescricaoVenda(Long id) throws IOException {
        String barra = System.getProperty("file.separator");
        try {
            // fazendo conexao com o banco
            this.stm = conn.getConnection().createStatement();
            // consulta passada para o arquivo jasper
            query = "select nome_cliente, cpf_cliente, rg_cliente, telefone_cliente, "
                    + "nome_administrador, descricao_forma_pagamento, quantidade_parcela, preco_total "
                    + "from view_venda "
                    + "where id = " + id.toString();
            // execute a query		
            rs = stm.executeQuery(query);
        } catch (SQLException ex) {
            Logger.getLogger(getClass()).error(ex);
            ex.printStackTrace();
        }
        
        jrRS = new JRResultSetDataSource(rs);
        // passagem de parametros para o jasper
        parameters = new HashMap();
        parameters.put("id_venda", id.toString());
        parameters.put("nome_empresa", SoftwareSpecifications.EMPRESA);
        parameters.put("cidade_empresa", SoftwareSpecifications.CIDADE);
        parameters.put("telefone_empresa", SoftwareSpecifications.TELEFONE);
        parameters.put("endereco_empresa", SoftwareSpecifications.ENDERECO);
        parameters.put("cep_empresa", SoftwareSpecifications.CEP);

        try {
            // passando endereço do cliente
            parameters.put("endereco_cliente", ControleDAO.getBanco().getVendaDAO().buscarPorId(id).getEnderecoCliente());
            // passando data como parametros por cauda da formatacao
            parameters.put("data_venda", ControleDAO.getBanco().getVendaDAO().buscarPorId(id).getDataEditada());
            // passando a conexao com o banco para o sub_report 
            parameters.put("REPORT_CONNECTION", conn.getConnection());
            // caminho logo da empresa
            String diretorio = SoftwareSpecifications.CAMINHO_LOGO;
            String resource = getClass().getResource(diretorio).toString();
            //setando o icones
            parameters.put("src_logo", resource);
            // caminho arquivo jasper
            resourceAsStream = this.getClass().getResourceAsStream("descricaoVenda.jasper");
            // caminho
            //srcSalvarRelatorio = new File("relatorios/vendas/" + DateUtils.formatDate2(ControleDAO.getBanco().getVendaDAO().buscarPorId(id).getData())).getCanonicalPath();
            srcSalvarRelatorio = new File(Painel.config.DIRETORIO_RELATORIOS + "Vendas" + barra + DateUtils.formatDate2(ControleDAO.getBanco().getVendaDAO().buscarPorId(id).getData())).getCanonicalPath();
            File file = new File(srcSalvarRelatorio);
            // verificar se um caminho  existe
            if (file.exists() == false) {
                // criar se nao existe
                file.mkdirs();
            }
            // pega nome do cliente
            nomeClienteVenda = ControleDAO.getBanco().getVendaDAO().buscarPorId(id).getCliente().getNome();
        } catch (SQLException ex) {
            Logger.getLogger(getClass()).error(ex);
            ex.printStackTrace();
        }
        try {
            // cria arquivo jasper
            jp = JasperFillManager.fillReport(resourceAsStream, parameters, jrRS);
            //impressao
            JasperExportManager.exportReportToPdfFile(jp, srcSalvarRelatorio + barra + id.toString() + "_" + nomeClienteVenda + ".pdf");
        } catch (JRException ex) {
            Logger.getLogger(getClass()).error(ex);
            ex.printStackTrace();
        }

    }

    public void mostrarDescricaoVenda() {
        // cria o arquivo de visao
        view = new JasperViewer(jp, false);
        // mostrar o arquivo de visao
        view.setVisible(true);
    }

    public void setMostrar(boolean mostrar) {
        this.mostrar = mostrar;
    }
}
