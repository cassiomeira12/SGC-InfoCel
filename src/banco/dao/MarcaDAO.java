package banco.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Celular;
import model.Marca;
import model.Produto;

/**
 * DAO responsável pela ações realizadas na base de dados referentes as marcas
 */
public class MarcaDAO extends DAO {

    public MarcaDAO() {
        super();
    }

    /**
     * Inserir marca na base de dados
     */
    public Long inserir(Marca marca) throws Exception {
        String sql = "INSERT INTO marca ( descricao ) VALUES (?)";

        stm = getConector().prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

        stm.setString(1, marca.getDescricao());

        return super.inserir();
    }

    /**
     * Atualizar dados marca na base de dados
     */
    public boolean editar(Marca marca) throws SQLException {
        String sql = "UPDATE marca SET descricao =? WHERE id =?";

        stm = getConector().prepareStatement(sql);

        stm.setString(1, marca.getDescricao());

        stm.setInt(2, marca.getId().intValue());

        stm.executeUpdate();
        stm.close();

        return true;
    }

    /**
     * Excluir marca na base de dados
     */
    public boolean excluir(int id) throws SQLException {
        String sql = "DELETE FROM marca WHERE id=?";

        stm = getConector().prepareStatement(sql);

        stm.setInt(1, id);
        stm.execute();

        stm.close();

        return true;
    }

    /**
     * Consultar todas marcas cadastradas na base de dados
     */
    public List<Marca> listar() throws SQLException {

        List<Marca> marcas = new ArrayList<>();

        String sql = "SELECT marca.* FROM marca";

        stm = getConector().prepareStatement(sql);
        rs = stm.executeQuery(sql);

        while (rs.next()) {
            Marca marca = new Marca((long) rs.getInt(1), rs.getString(2));

            marcas.add(marca);
        }

        stm.close();
        rs.close();

        return marcas;
    }

    public List<Marca> buscarPorDescricao(String busca) throws Exception {

        List<Marca> marcas = new ArrayList<>();

        String sql = "SELECT marca.* FROM marca WHERE descricao LIKE '%" + busca + "%'";

        stm = getConector().prepareStatement(sql);
        rs = stm.executeQuery(sql);

        while (rs.next()) {
            Marca marca = new Marca((long) rs.getInt(1), rs.getString(2));

            marcas.add(marca);
        }

        stm.close();
        rs.close();

        return marcas;
    }

}
