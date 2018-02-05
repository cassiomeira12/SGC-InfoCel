package banco.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.CategoriaProduto;
import model.CategoriaSaida;

/**
 * DAO responsável pela ações realizadas na base de dados referentes as
 * categoria
 */
public class CategoriaSaidaDAO extends DAO {

    public CategoriaSaidaDAO() {
        super();
    }

    /**
     * Inserir categoria na base de dados
     */
    public Long inserir(CategoriaSaida categoria) {
        try {
            String sql = "INSERT INTO categoria_saida ( descricao_categoria ) VALUES (?)";

            stm = getConector().prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

            stm.setString(1, categoria.getDescricao());

            return super.inserir(sql);
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
            String sql = "UPDATE categoria_saida SET descricao_categoria =? WHERE id_categoria =?";

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
            String sql = "DELETE FROM categoria_saida WHERE id_categoria=?";

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
    public List<CategoriaSaida> listar() {

        List<CategoriaSaida> categorias = new ArrayList<>();

        try {
            String sql = "SELECT categoria_saida.* FROM categoria_saida";

            stm = getConector().prepareStatement(sql);
            rs = stm.executeQuery(sql);

            while (rs.next()) {
                CategoriaSaida categoria = new CategoriaSaida((long) rs.getInt(1), rs.getString(2));

                categorias.add(categoria);
            }

            stm.close();
            rs.close();

        } catch (SQLException ex) {
            chamarAlertaErro("Erro ao consultar categoria na base de dados!", ex.toString());
        }

        return categorias;
    }

    public CategoriaProduto buscarPorId(int id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
