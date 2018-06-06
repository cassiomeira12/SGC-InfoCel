package banco.dao;

import banco.ControleDAO;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.Endereco;

/**
 * DAO responsável pela ações realizadas na base de dados referentes as receitas
 */
public class EnderecoDAO extends DAO {

    public EnderecoDAO() {
        super();
    }

    /**
     * Inserir marca na base de dados
     */
    public Long inserir(Endereco endereco) throws Exception {
        ResultSet rs;
        PreparedStatement stm;

        if (endereco.getBairro().getCidade().getId() == null) {
            Long id = ControleDAO.getBanco().getCidadeDAO().inserir(endereco.getBairro().getCidade());
            endereco.getBairro().getCidade().setId(id);
        }

        if (endereco.getBairro().getId() == null) {
            Long id = ControleDAO.getBanco().getBairroDAO().inserir(endereco.getBairro());
            endereco.getBairro().setId(id);
        }

        String sql = "INSERT INTO endereco ( rua, numero, id_bairro, status ) VALUES (?, ?, ?, ?)";

        stm = getConector().prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

        stm.setString(1, endereco.getRua());
        stm.setString(2, endereco.getNumero());
        stm.setInt(3, endereco.getBairro().getId().intValue());
        stm.setBoolean(4, endereco.getStatus());

        return super.inserir(stm);
    }

    /**
     * Atualizar dados receita na base de dados
     */
    public boolean editar(Endereco endereco) throws SQLException {
        ResultSet rs;
        PreparedStatement stm;

        String sql = "UPDATE endereco SET rua =?, numero = ?, id_bairro = ?, status =? WHERE id=?";

        stm = getConector().prepareStatement(sql);

        stm.setString(1, endereco.getRua());
        stm.setString(2, endereco.getNumero());
        stm.setLong(3, endereco.getBairro().getId());
        stm.setBoolean(4, endereco.getStatus());

        stm.setInt(5, endereco.getId().intValue());

        stm.executeUpdate();
        stm.close();
        return true;
    }

    /**
     * Excluir marca na base de dados
     */
    public boolean excluir(Endereco e) throws SQLException {
        ResultSet rs;
        PreparedStatement stm;

        try {
            String sql = "DELETE FROM endereco WHERE id=?";

            stm = getConector().prepareStatement(sql);

            stm.setInt(1, e.getId().intValue());
            stm.execute();

            stm.close();
        } catch (MySQLIntegrityConstraintViolationException ex) {
            e.setStatus(false);
            editar(e);
        }

        return true;
    }

    public boolean excluirEnderecosDoBairro(int id) throws SQLException {
        ResultSet rs;
        PreparedStatement stm;

        try {
            String sql = "DELETE FROM endereco WHERE id_bairro=?";

            stm = getConector().prepareStatement(sql);

            stm.setInt(1, id);
            stm.execute();

            stm.close();
        } catch (MySQLIntegrityConstraintViolationException ex) {
            String sql = "UPDATE endereco SET status = 0 WHERE id_bairro = " + id;

            stm = getConector().prepareStatement(sql);

            stm.setInt(1, id);
            stm.execute();

            stm.close();
        }

        return true;
    }
}
