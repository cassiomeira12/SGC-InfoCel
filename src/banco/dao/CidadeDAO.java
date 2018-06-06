package banco.dao;

import banco.ControleDAO;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Cidade;

/**
 * DAO responsável pela ações realizadas na base de dados referentes as marcas
 */
public class CidadeDAO extends DAO {

    public CidadeDAO() {
        super();
    }

    /**
     * Inserir marca na base de dados
     */
    public Long inserir(Cidade cidade) throws Exception {
        ResultSet rs;
        PreparedStatement stm;

        String sql = "INSERT INTO cidade ( nome, status ) VALUES (?, ?)";

        stm = getConector().prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

        stm.setString(1, cidade.getNome());
        stm.setBoolean(2, cidade.getStatus());

        return super.inserir(stm);
    }

    /**
     * Atualizar dados marca na base de dados
     */
    public boolean editar(Cidade cidade) throws SQLException {
        ResultSet rs;
        PreparedStatement stm;

        String sql = "UPDATE cidade SET nome =?, status =? WHERE id =?";

        stm = getConector().prepareStatement(sql);

        stm.setString(1, cidade.getNome());
        stm.setBoolean(2, cidade.getStatus());

        stm.setInt(3, cidade.getId().intValue());

        stm.executeUpdate();
        stm.close();

        return true;
    }

    /**
     * Excluir marca na base de dados
     */
    public boolean excluir(Cidade c) throws SQLException {
        ResultSet rs;
        PreparedStatement stm;

        try {
            String sql = "DELETE FROM cidade WHERE id=?";

            stm = getConector().prepareStatement(sql);

            stm.setInt(1, c.getId().intValue());
            stm.execute();

            stm.close();
        } catch (MySQLIntegrityConstraintViolationException e) {
            c.setStatus(false);
            editar(c);
        }

        return true;
    }

    /**
     * Consultar todas marcas cadastradas na base de dados
     */
    public List<Cidade> listar() throws SQLException {
        ResultSet rs;
        PreparedStatement stm;

        List<Cidade> cidades = new ArrayList<>();

        String sql = "SELECT cidade.* FROM cidade WHERE status = 1";

        stm = getConector().prepareStatement(sql);
        rs = stm.executeQuery(sql);

        while (rs.next()) {
            Cidade cidade = new Cidade((long) rs.getInt(1), rs.getString(2));

            cidades.add(cidade);
        }

        stm.close();
        rs.close();

        return cidades;
    }

    public List<Cidade> buscarPorNome(String busca) throws Exception {
        ResultSet rs;
        PreparedStatement stm;

        List<Cidade> cidades = new ArrayList<>();

        String sql = "SELECT cidade.* FROM cidade WHERE nome LIKE '%" + busca + "%' AND status = 1";

        stm = getConector().prepareStatement(sql);
        rs = stm.executeQuery(sql);

        while (rs.next()) {
            Cidade cidade = new Cidade((long) rs.getInt(1), rs.getString(2));

            cidades.add(cidade);
        }

        stm.close();
        rs.close();

        return cidades;
    }

}
