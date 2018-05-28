package banco.dao;

import java.sql.PreparedStatement;
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
        String sql = "INSERT INTO unidade_medida ( descricao ) VALUES (?)";

        stm = getConector().prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

        stm.setString(1, unidadeMedida.getDescricao());

        return super.inserir();
    }

    /**
     * Atualizar dados categoria na base de dados
     */
    public boolean editar(UnidadeMedida unidadeMedida) throws SQLException {
        String sql = "UPDATE unidade_medida SET descricao =? WHERE id =?";

        stm = getConector().prepareStatement(sql);

        stm.setString(1, unidadeMedida.getDescricao());

        stm.setInt(2, unidadeMedida.getId().intValue());

        stm.executeUpdate();
        stm.close();

        return true;
    }

    /**
     * Excluir categoria na base de dados
     */
    public boolean excluir(int id) throws SQLException {
        String sql = "DELETE FROM unidade_medida WHERE id=?";

        stm = getConector().prepareStatement(sql);

        stm.setInt(1, id);
        stm.execute();

        stm.close();
        return true;
    }

    /**
     * Consultar todas categoria cadastradas na base de dados
     */
    public List<UnidadeMedida> listar() throws SQLException {

        List<UnidadeMedida> unidadeMedidas = new ArrayList<>();

        String sql = "SELECT unidade_medida.* FROM unidade_medida";

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
