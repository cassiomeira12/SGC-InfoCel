package banco.dao;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
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
    public Long inserir(CategoriaProduto categoria) throws Exception {
        ResultSet rs;
        PreparedStatement stm;

        String sql = "INSERT INTO categoria_produto ( descricao, status ) VALUES (?, ?)";

        stm = getConector().prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

        stm.setString(1, categoria.getDescricao());
        stm.setBoolean(2, categoria.getStatus());

        return super.inserir(stm);
    }

    /**
     * Atualizar dados categoria na base de dados
     */
    public boolean editar(CategoriaProduto categoria) throws SQLException {
        ResultSet rs;
        PreparedStatement stm;

        String sql = "UPDATE categoria_produto SET descricao =?, status =? WHERE id =?";

        stm = getConector().prepareStatement(sql);

        stm.setString(1, categoria.getDescricao());
        stm.setBoolean(2, categoria.getStatus());

        stm.setInt(3, categoria.getId().intValue());

        stm.executeUpdate();
        stm.close();

        return true;
    }

    /**
     * Excluir categoria na base de dados
     */
    public boolean excluir(CategoriaProduto cp) throws SQLException {
        ResultSet rs;
        PreparedStatement stm;

        try {
            String sql = "DELETE FROM categoria_produto WHERE id=?";

            stm = getConector().prepareStatement(sql);

            stm.setInt(1, cp.getId().intValue());
            stm.execute();

            stm.close();
        } catch (MySQLIntegrityConstraintViolationException e) {
            cp.setStatus(false);
            editar(cp);
        }

        return true;
    }

    /**
     * Consultar todas categoria cadastradas na base de dados
     */
    public List<CategoriaProduto> listar() throws SQLException {
        ResultSet rs;
        PreparedStatement stm;

        List<CategoriaProduto> categorias = new ArrayList<>();

        String sql = "SELECT categoria_produto.* FROM categoria_produto WHERE status = 1";

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
        ResultSet rs;
        PreparedStatement stm;

        List<CategoriaProduto> categorias = new ArrayList<>();
        String sql = "SELECT categoria_produto.* FROM categoria_produto WHERE descricao LIKE '%" + descricao + "%'" + " AND status = 1";

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
