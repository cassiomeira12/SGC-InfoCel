package banco.dao;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.UnidadeMedida;

/**
 * DAO responsável pela ações realizadas na base de dados referentes as
 * categoria
 */
public class UnidadeMedidaDAO extends DAO {

    public UnidadeMedidaDAO() {
        super();
    }

    /**
     * Inserir categoria na base de dados
     */
    public Long inserir(UnidadeMedida unidadeMedida) throws Exception {
        ResultSet rs;
        PreparedStatement stm;

        String sql = "INSERT INTO unidade_medida ( descricao, abreviacao, status ) VALUES (?, ?, ?)";

        stm = getConector().prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

        stm.setString(1, unidadeMedida.getDescricao());
        stm.setString(2, unidadeMedida.getAbreviacao());
        stm.setBoolean(3, unidadeMedida.getStatus());

        return super.inserir(stm);
    }

    /**
     * Atualizar dados categoria na base de dados
     */
    public boolean editar(UnidadeMedida unidadeMedida) throws SQLException {
        ResultSet rs;
        PreparedStatement stm;

        String sql = "UPDATE unidade_medida SET descricao =?, status =? WHERE id =?";

        stm = getConector().prepareStatement(sql);

        stm.setString(1, unidadeMedida.getDescricao());
        stm.setBoolean(2, unidadeMedida.getStatus());

        stm.setInt(3, unidadeMedida.getId().intValue());

        stm.executeUpdate();
        stm.close();

        return true;
    }

    /**
     * Excluir categoria na base de dados
     */
    public boolean excluir(UnidadeMedida um) throws SQLException {
        ResultSet rs;
        PreparedStatement stm;

        try {
            String sql = "DELETE FROM unidade_medida WHERE id=?";

            stm = getConector().prepareStatement(sql);

            stm.setInt(1, um.getId().intValue());
            stm.execute();

            stm.close();
        } catch (MySQLIntegrityConstraintViolationException e) {
            um.setStatus(false);
            editar(um);
        }

        return true;
    }

    /**
     * Consultar todas categoria cadastradas na base de dados
     */
    public List<UnidadeMedida> listar() throws SQLException {
        ResultSet rs;
        PreparedStatement stm;

        List<UnidadeMedida> unidadeMedidas = new ArrayList<>();

        String sql = "SELECT unidade_medida.* FROM unidade_medida WHERE status = 1";

        stm = getConector().prepareStatement(sql);
        rs = stm.executeQuery(sql);

        while (rs.next()) {
            UnidadeMedida unidadeMedida = new UnidadeMedida((long) rs.getInt(1), rs.getString(2), rs.getString(3));

            unidadeMedidas.add(unidadeMedida);
        }

        stm.close();
        rs.close();

        return unidadeMedidas;
    }
}
