package banco.dao;

import java.sql.PreparedStatement;
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
    public Long inserir(CategoriaProduto categoria) throws Exception {
        String sql = "INSERT INTO categoria_produto ( descricao ) VALUES (?)";

        stm = getConector().prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

        stm.setString(1, categoria.getDescricao());

        return super.inserir();
    }

    /**
     * Atualizar dados categoria na base de dados
     */
    public boolean editar(CategoriaProduto categoria) throws SQLException {
        String sql = "UPDATE categoria_produto SET descricao =? WHERE id =?";

        stm = getConector().prepareStatement(sql);

        stm.setString(1, categoria.getDescricao());

        stm.setInt(2, categoria.getId().intValue());

        stm.executeUpdate();
        stm.close();

        return true;
    }

    /**
     * Excluir categoria na base de dados
     */
    public boolean excluir(int id) throws SQLException {
        String sql = "DELETE FROM categoria_produto WHERE id=?";

        stm = getConector().prepareStatement(sql);

        stm.setInt(1, id);
        stm.execute();

        stm.close();

        return true;
    }

    /**
     * Consultar todas categoria cadastradas na base de dados
     */
    public List<CategoriaProduto> listar() throws SQLException {

        List<CategoriaProduto> categorias = new ArrayList<>();

        String sql = "SELECT categoria_produto.* FROM categoria_produto";

        stm = getConector().prepareStatement(sql);
        rs = stm.executeQuery(sql);

        while (rs.next()) {
            CategoriaProduto categoria = new CategoriaProduto((long) rs.getInt(1), rs.getString(2));

            categorias.add(categoria);
        }

        stm.close();
        rs.close();

        return categorias;
    }

    public List<CategoriaProduto> buscarPorDescricao(String descricao) throws SQLException {

        List<CategoriaProduto> categorias = new ArrayList<>();
        String sql = "SELECT categoria_produto.* FROM categoria_produto WHERE descricao LIKE '%" + descricao + "%'";

        stm = getConector().prepareStatement(sql);
        rs = stm.executeQuery(sql);

        while (rs.next()) {
            CategoriaProduto categoria = new CategoriaProduto((long) rs.getInt(1), rs.getString(2));

            categorias.add(categoria);
        }

        stm.close();
        rs.close();

        return categorias;
    }

}
