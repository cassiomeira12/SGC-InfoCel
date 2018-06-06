package banco.dao;

import banco.ControleDAO;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Bairro;
import model.Cidade;

/**
 * DAO responsável pela ações realizadas na base de dados referentes as receitas
 */
public class BairroDAO extends DAO {

    public BairroDAO() {
        super();
    }

    /**
     * Inserir marca na base de dados
     */
    public Long inserir(Bairro bairro) throws Exception {
        ResultSet rs;
        PreparedStatement stm;

        if (bairro.getCidade().getId() == null) {
            Long id = ControleDAO.getBanco().getCidadeDAO().inserir(bairro.getCidade());
            bairro.getCidade().setId(id);
        }

        String sql = "INSERT INTO bairro ( nome, id_cidade, status) VALUES (?, ?, ?)";

        stm = getConector().prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

        stm.setString(1, bairro.getNome());
        stm.setInt(2, bairro.getCidade().getId().intValue());
        stm.setBoolean(3, bairro.getStatus());

        return super.inserir(stm);
    }

    /**
     * Atualizar dados receita na base de dados
     */
    public boolean editar(Bairro bairro) throws SQLException {
        ResultSet rs;
        PreparedStatement stm;

        String sql = "UPDATE bairro SET nome =?, status =? WHERE id=?";

        stm = getConector().prepareStatement(sql);

        stm.setString(1, bairro.getNome());
        stm.setBoolean(2, bairro.getStatus());

        stm.setInt(3, bairro.getId().intValue());

        stm.executeUpdate();
        stm.close();
        return true;
    }

    /**
     * Excluir marca na base de dados
     */
    public boolean excluir(Bairro bairro) throws SQLException {
        ResultSet rs;
        PreparedStatement stm;

        try {
            String sql = "DELETE FROM bairro WHERE id=?";

            stm = getConector().prepareStatement(sql);

            stm.setInt(1, bairro.getId().intValue());
            stm.execute();

            stm.close();
        } catch (MySQLIntegrityConstraintViolationException e) {
            bairro.setStatus(false);
            editar(bairro);
        }
        return true;
    }

    /**
     * Consultar todas receita cadastradas na base de dados
     */
    public List<Bairro> buscarPorCidade(Cidade cidade) throws SQLException {
        ResultSet rs;
        PreparedStatement stm;

        List<Bairro> bairros = new ArrayList<>();

        String sql = "SELECT * FROM view_bairro WHERE id_cidade = " + cidade.getId() + " AND status = 1";
        stm = getConector().prepareStatement(sql);
        rs = stm.executeQuery(sql);

        while (rs.next()) {
            bairros.add(new Bairro(rs.getLong("id"), rs.getString("nome"), cidade));
        }

        stm.close();
        rs.close();

        return bairros;
    }

    public List<Bairro> buscarPorNomeECidade(String nome, Cidade cidade) throws SQLException {
        ResultSet rs;
        PreparedStatement stm;

        List<Bairro> bairros = new ArrayList<>();

        String sql = "SELECT * FROM view_bairro WHERE id_cidade = " + cidade.getId() + " AND nome LIKE '%" + nome + "%'" + " AND staus = 1";;
        stm = getConector().prepareStatement(sql);
        rs = stm.executeQuery(sql);

        while (rs.next()) {
            bairros.add(new Bairro(rs.getLong("id"), rs.getString("nome"), cidade));
        }

        stm.close();
        rs.close();

        return bairros;
    }

    public boolean excluirBairrosDaCidade(int id) throws SQLException {
        ResultSet rs;
        PreparedStatement stm;

        String sql = "DELETE FROM bairro WHERE id_cidade=?";

        stm = getConector().prepareStatement(sql);

        stm.setInt(1, id);
        stm.execute();

        stm.close();

        return true;
    }

}
