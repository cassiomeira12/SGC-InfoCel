package banco.dao;

import banco.ConexaoBanco;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Possui objetos necessários para implementar os CRUDs a partir da base de
 * dados, através do conector que indicará de qual classe serão chamadas as
 * consultas.
 *
 * @author Angelica Leite, Editado por Pedro Cordeiro
 */
public class DAO {

    private Connection conector = ConexaoBanco.instancia().getConnection();
   
    public DAO() {
    }

    public Connection getConector() {
        if (conector == null) {
            ConexaoBanco.instancia = new ConexaoBanco();
            conector = ConexaoBanco.instancia().getConnection();
        }

        return conector;
    }

    protected Long inserir(PreparedStatement stm) throws Exception {
        Long id = null;

        stm.executeUpdate();

        ResultSet resultado = stm.getGeneratedKeys();
        resultado.next();
        id = (long) resultado.getInt(1);

        resultado.close();

        return id;
    }
}
