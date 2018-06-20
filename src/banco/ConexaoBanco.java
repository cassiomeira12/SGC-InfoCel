/** *********************************************************************
 * Autor: Cassio Meira Silva
 * Nome: Conexao Banco
 * Funcao: Classe pra realizar a conexao com o Banco de Dados
 ********************************************************************** */
package banco;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.apache.log4j.Logger;

/**
 * Informações dos dados para conexão com a base de dados
 *
 * @author Angelica Leite/ Editado por Pedro Cordeiro / Editado por Cassio Meira
 */
public class ConexaoBanco {
    
    //BANCO DE DADOS NA REDE
    public static final String URL = "jdbc:mysql://neolig.com:3306/";
    public static final String DATABASE = "neoli831_teste";
    public static final String DRIVER = "com.mysql.jdbc.Driver";
    public static final String USERNAME = "neoli831_teste";
    public static final String PASSWORD = "teste";

    //BANCO DE DADOS LOCAL
//    private static final String URL = "jdbc:mysql://localhost:3306/";
//    public static final String DATABASE = "sgc_infocel";
//    public static final String DRIVER = "com.mysql.jdbc.Driver";
//    public static final String USERNAME = "root";
//    public static final String PASSWORD = "root";

    public static ConexaoBanco instancia = new ConexaoBanco();
    private static Connection connection;

    public static String getTabelas() {
        return " marca unidade_medida categoria_produto produto categoria_saida saida cidade bairro endereco administrador cliente receita forma_pagamento manutencao venda venda_produto";
    }

    public ConexaoBanco() {
        try {
            Class.forName(DRIVER).newInstance();
            connection = DriverManager.getConnection(URL + DATABASE, USERNAME, PASSWORD);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException ex) {
            Logger.getLogger(getClass()).error(ex);
            ex.printStackTrace();
        }
    }

    /**
     * Sigleton para conexão com a base de dados
     *
     * @return
     */
    public static ConexaoBanco instancia() {
        if (instancia == null) {
            instancia = new ConexaoBanco();
        }
        return instancia;
    }

    /**
     * Obter a conexão com a base de dados
     *
     * @return
     */
    public Connection getConnection() {
        return connection;
    }

}//fim class
