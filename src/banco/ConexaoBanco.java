/** *********************************************************************
 * Autor: Cassio Meira Silva
 * Nome: Conexao Banco
 * Funcao: Classe pra realizar a conexao com o Banco de Dados
 ********************************************************************** */
package banco;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Informações dos dados para conexão com a base de dados
 *
 * @author Angelica Leite/ Editado por Pedro Cordeiro / Editado por Cassio Meira
 */
public class ConexaoBanco {

    public static final String URL = "jdbc:mysql://neolig.com:3306/";
   // private static final String URL = "jdbc:mysql://localhost:3306/";
    public static final String DATABASE = "neoli831_teste";
    public static final String DRIVER = "com.mysql.jdbc.Driver";
    public static final String USERNAME = "neoli831_teste";
    public static final String PASSWORD = "teste";

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
            System.out.println("[ERRO]: Erro ao conectar-se com a base de dados\n" + ex.toString());
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
