package banco.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.CategoriaProduto;

/**
 * DAO responsável pela ações realizadas na base de dados referentes as
 * categoria
 */
public class CategoriaProdutoDAO extends DAO {

    public CategoriaProdutoDAO() {
        super();
    }

    /**
     * Inserir categoria na base de dados
     */
    public Long inserir(CategoriaProduto categoria) {
        try {
            String sql = "INSERT INTO categoria_produto ( descricao_categoria ) VALUES (?)";

            stm = getConector().prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

            stm.setString(1, categoria.getDescricao());

            return super.inserir();
        } catch (Exception ex) {
            chamarAlertaErro("Erro ao inserir categoria na base de dados", ex.toString());
        }

        return null;
    }

    /**
     * Atualizar dados categoria na base de dados
     */
    public boolean editar(CategoriaProduto categoria) {
        try {
            String sql = "UPDATE categoria_produto SET descricao =? WHERE id =?";

            stm = getConector().prepareStatement(sql);

            stm.setString(1, categoria.getDescricao());

            stm.setInt(2, categoria.getId().intValue());

            stm.executeUpdate();
            stm.close();

        } catch (SQLException ex) {
            chamarAlertaErro("Erro ao atualizar categoria na base de dados!", ex.toString());
            return false;
        }

        return true;
    }

    /**
     * Excluir categoria na base de dados
     */
    public boolean excluir(int id) {
        try {
            String sql = "DELETE FROM categoria_produto WHERE id=?";

            stm = getConector().prepareStatement(sql);

            stm.setInt(1, id);
            stm.execute();

            stm.close();
        } catch (SQLException ex) {
            chamarAlertaErro("Erro ao excluir categoria na base de dados!", ex.toString());
            return false;
        }

        return true;
    }

    /**
     * Consultar todas categoria cadastradas na base de dados
     */
    public List<CategoriaProduto> listar() {

        List<CategoriaProduto> categorias = new ArrayList<>();

        try {
            String sql = "SELECT categoria_produto.* FROM categoria_produto";

            stm = getConector().prepareStatement(sql);
            rs = stm.executeQuery(sql);

            while (rs.next()) {
                CategoriaProduto categoria = new CategoriaProduto((long) rs.getInt(1), rs.getString(2));

                categorias.add(categoria);
            }

            stm.close();
            rs.close();

        } catch (SQLException ex) {
            chamarAlertaErro("Erro ao consultar categoria na base de dados!", ex.toString());
        }

        return categorias;
    }

    public List<CategoriaProduto> buscarPorDescricao(String descricao) {

        List<CategoriaProduto> categorias = new ArrayList<>();

        try {
            String sql = "SELECT categoria_produto.* FROM categoria_produto WHERE descricao LIKE '%" + descricao + "%'";

            stm = getConector().prepareStatement(sql);
            rs = stm.executeQuery(sql);

            while (rs.next()) {
                CategoriaProduto categoria = new CategoriaProduto((long) rs.getInt(1), rs.getString(2));

                categorias.add(categoria);
            }

            stm.close();
            rs.close();

        } catch (SQLException ex) {
            chamarAlertaErro("Erro ao consultar categoria na base de dados!", ex.toString());
        }

        return categorias;
    }

}
