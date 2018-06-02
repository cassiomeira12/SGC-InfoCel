package banco.dao;

import banco.ControleDAO;
import java.sql.PreparedStatement;
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
        if (bairro.getCidade().getId() == null) {
            Long id = ControleDAO.getBanco().getCidadeDAO().inserir(bairro.getCidade());
            bairro.getCidade().setId(id);
        }

        String sql = "INSERT INTO bairro ( nome, id_cidade ) VALUES (?, ?)";

        stm = getConector().prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

        stm.setString(1, bairro.getNome());
        stm.setInt(2, bairro.getCidade().getId().intValue());

        return super.inserir();
    }

    /**
     * Atualizar dados receita na base de dados
     */
    public boolean editar(Bairro bairro) throws SQLException {
        String sql = "UPDATE bairro SET nome =? WHERE id=?";

        stm = getConector().prepareStatement(sql);

        stm.setString(1, bairro.getNome());

        stm.setInt(2, bairro.getId().intValue());

        stm.executeUpdate();
        stm.close();
        return true;
    }

    /**
     * Excluir marca na base de dados
     */
    public boolean excluir(int id) throws SQLException {
        ControleDAO.getBanco().getEnderecoDAO().excluirEnderecosDoBairro(id);
        
        String sql = "DELETE FROM bairro WHERE id=?";

        stm = getConector().prepareStatement(sql);

        stm.setInt(1, id);
        stm.execute();

        stm.close();

        return true;
    }

    /**
     * Consultar todas receita cadastradas na base de dados
     */
    public List<Bairro> buscarPorCidade(Cidade cidade) throws SQLException {

        List<Bairro> bairros = new ArrayList<>();

        String sql = "SELECT * FROM view_bairro WHERE id_cidade = " + cidade.getId();
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

        List<Bairro> bairros = new ArrayList<>();

        String sql = "SELECT * FROM view_bairro WHERE id_cidade = " + cidade.getId() + " AND nome LIKE '%" + nome + "%'";
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
        String sql = "DELETE FROM bairro WHERE id_cidade=?";

        stm = getConector().prepareStatement(sql);

        stm.setInt(1, id);
        stm.execute();

        stm.close();

        return true;
    }

}
