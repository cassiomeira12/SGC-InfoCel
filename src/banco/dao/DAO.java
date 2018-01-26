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
 * @author Angelica Leite, Editado por PEdro Cordeiro
 */
public class DAO {

    protected Connection conector = ConexaoBanco.instancia().getConnection();
    protected ResultSet rs;
    protected PreparedStatement stm;

    public DAO() {
    }
}
