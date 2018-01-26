/***********************************************************************
* Autor: Cassio Meira Silva
* Nome: Classe
* Funcao: 
***********************************************************************/

package banco;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Informações dos dados para conexão com a base de dados
 *
 * @author Angelica Leite/ Editado por Pedro Cordeiro
 */
public class ConexaoBanco {

    private static ConexaoBanco instancia = new ConexaoBanco();
    private Connection connection;

    private ConexaoBanco() {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/infocel", "root", "root");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException ex) {
            System.out.println("Erro ao conectar-se com a base de dados\n" + ex);
        }
    }

    /**
     * Sigleton para conexão com a base de dados
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
     * @return 
     */
    public Connection getConnection() {
        return connection;
    }
}//fim class