package banco.dao;

import banco.ControleDAO;
import java.sql.PreparedStatement;
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
        String sql = "INSERT INTO cidade ( nome ) VALUES (?)";

        stm = getConector().prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

        stm.setString(1, cidade.getNome());

        return super.inserir();
    }

    /**
     * Atualizar dados marca na base de dados
     */
    public boolean editar(Cidade cidade) throws SQLException {
        String sql = "UPDATE cidade SET nome =? WHERE id =?";

        stm = getConector().prepareStatement(sql);

        stm.setString(1, cidade.getNome());

        stm.setInt(2, cidade.getId().intValue());

        stm.executeUpdate();
        stm.close();

        return true;
    }

    /**
     * Excluir marca na base de dados
     */
    public boolean excluir(int id) throws SQLException {
        ControleDAO.getBanco().getBairroDAO().excluirBairrosDaCidade(id);
        String sql = "DELETE FROM cidade WHERE id=?";

        stm = getConector().prepareStatement(sql);

        stm.setInt(1, id);
        stm.execute();

        stm.close();

        return true;
    }

    /**
     * Consultar todas marcas cadastradas na base de dados
     */
    public List<Cidade> listar() throws SQLException {

        List<Cidade> cidades = new ArrayList<>();

        String sql = "SELECT cidade.* FROM cidade";

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

        List<Cidade> cidades = new ArrayList<>();

        String sql = "SELECT cidade.* FROM cidade WHERE nome LIKE '%" + busca + "%'";

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
