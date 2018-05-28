package banco.dao;

import banco.ControleDAO;
import java.sql.PreparedStatement;
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
        if (endereco.getBairro().getCidade().getId() == null) {
            Long id = ControleDAO.getBanco().getCidadeDAO().inserir(endereco.getBairro().getCidade());
            endereco.getBairro().getCidade().setId(id);
        }
        
        if (endereco.getBairro().getId() == null) {
            Long id = ControleDAO.getBanco().getBairroDAO().inserir(endereco.getBairro());
            endereco.getBairro().setId(id);
        }

        String sql = "INSERT INTO endereco ( rua, numero, id_bairro ) VALUES (?, ?, ?)";

        stm = getConector().prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

        stm.setString(1, endereco.getRua());
         stm.setString(2, endereco.getNumero());
        stm.setInt(3, endereco.getBairro().getId().intValue());

        return super.inserir();
    }

    /**
     * Atualizar dados receita na base de dados
     */
    public boolean editar(Endereco endereco) throws SQLException {
        String sql = "UPDATE endereco SET rua =?, numero = ?, id_bairro = ? WHERE id=?";

        stm = getConector().prepareStatement(sql);

        stm.setString(1, endereco.getRua());
        stm.setString(2, endereco.getNumero());
        stm.setLong(3, endereco.getBairro().getId());

        stm.setInt(4, endereco.getId().intValue());

        stm.executeUpdate();
        stm.close();
        return true;
    }

    /**
     * Excluir marca na base de dados
     */
    public boolean excluir(int id) throws SQLException {
        String sql = "DELETE FROM endereco WHERE id=?";

        stm = getConector().prepareStatement(sql);

        stm.setInt(1, id);
        stm.execute();

        stm.close();

        return true;
    }
}
