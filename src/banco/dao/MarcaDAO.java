package banco.dao;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Marca;

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
        ResultSet rs;
        PreparedStatement stm;

        String sql = "INSERT INTO marca ( descricao, status ) VALUES (?, ?)";

        stm = getConector().prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

        stm.setString(1, marca.getDescricao());
        stm.setBoolean(2, marca.getStatus());

        return super.inserir(stm);
    }

    /**
     * Atualizar dados marca na base de dados
     */
    public boolean editar(Marca marca) throws SQLException {
        ResultSet rs;
        PreparedStatement stm;

        String sql = "UPDATE marca SET descricao =?, status =? WHERE id =?";

        stm = getConector().prepareStatement(sql);

        stm.setString(1, marca.getDescricao());
        stm.setBoolean(2, marca.getStatus());

        stm.setInt(3, marca.getId().intValue());

        stm.executeUpdate();
        stm.close();

        return true;
    }

    /**
     * Excluir marca na base de dados
     */
    public boolean excluir(Marca marca) throws SQLException {
        ResultSet rs;
        PreparedStatement stm;

        try {
            String sql = "DELETE FROM marca WHERE id=?";

            stm = getConector().prepareStatement(sql);

            stm.setInt(1, marca.getId().intValue());
            stm.execute();

            stm.close();
        } catch (MySQLIntegrityConstraintViolationException e) {
            marca.setStatus(false);
            editar(marca);
        }

        return true;
    }

    /**
     * Consultar todas marcas cadastradas na base de dados
     */
    public List<Marca> listar() throws SQLException {
        ResultSet rs;
        PreparedStatement stm;

        List<Marca> marcas = new ArrayList<>();

        String sql = "SELECT marca.* FROM marca WHERE status = 1";

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
        ResultSet rs;
        PreparedStatement stm;

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
