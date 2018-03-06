package banco.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.CategoriaProduto;
import model.UnidadeMedida;

/**
 * DAO responsável pela ações realizadas na base de dados referentes as
 * categoria
 */
public class UnidadeMedidaDAO extends DAO {

    public UnidadeMedidaDAO() {
        super();
    }

    /**
     * Inserir categoria na base de dados
     */
    public Long inserir(UnidadeMedida unidadeMedida) {
        try {
            String sql = "INSERT INTO unidade_medida ( descricao_unidade ) VALUES (?)";

            stm = getConector().prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

            stm.setString(1, unidadeMedida.getDescricao());

            return super.inserir();
        } catch (Exception ex) {
            chamarAlertaErro("Erro ao inserir unidadeMedida na base de dados", ex.toString());
        }

        return null;
    }

    /**
     * Atualizar dados categoria na base de dados
     */
    public boolean editar(UnidadeMedida unidadeMedida) {
        try {
            String sql = "UPDATE unidade_medida SET descricao_unidade =? WHERE id_unidade_medida =?";

            stm = getConector().prepareStatement(sql);

            stm.setString(1, unidadeMedida.getDescricao());

            stm.setInt(2, unidadeMedida.getId().intValue());

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
            String sql = "DELETE FROM unidade_medida WHERE id_unidade_medida=?";

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
    public List<UnidadeMedida> listar() {

        List<UnidadeMedida> unidadeMedidas = new ArrayList<>();

        try {
            String sql = "SELECT unidade_media.* FROM unidade_medida";

            stm = getConector().prepareStatement(sql);
            rs = stm.executeQuery(sql);

            while (rs.next()) {
                UnidadeMedida unidadeMedida = new UnidadeMedida((long) rs.getInt(1), rs.getString(2), rs.getString(3));

                unidadeMedidas.add(unidadeMedida);
            }

            stm.close();
            rs.close();

        } catch (SQLException ex) {
            chamarAlertaErro("Erro ao consultar categoria na base de dados!", ex.toString());
        }

        return unidadeMedidas;
    }

    public List<CategoriaProduto> buscarPorDescricao(String descricao) {

        List<CategoriaProduto> categorias = new ArrayList<>();

        try {
            String sql = "SELECT categoria_produto.* FROM categoria_produto WHERE descricao_categoria LIKE '%" + descricao + "%'";

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
