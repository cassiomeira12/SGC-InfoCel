package banco.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Celular;
import model.Marca;
import model.Produto;
import model.Saida;
import util.DateUtils;

/**
 * DAO responsável pela ações realizadas na base de dados referentes as saida
 */
public class SaidaDAO extends DAO {

    public SaidaDAO() {
        super();
    }

    /**
     * Inserir saida na base de dados
     */
    public Long inserir(Saida saida) {
        try {
            String sql = "INSERT INTO saida ( id_categoria, id_administrador, descricao_saida, data_saida, valor_saida ) VALUES (?, ?, ?, ?, ?)";

            stm = getConector().prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

            stm.setInt(1, saida.getCategoria().getId().intValue());
            stm.setInt(2, saida.getAdministrador().getId().intValue());
            stm.setString(3, saida.getDescricao());
            stm.setLong(4, saida.getData());
            stm.setFloat(5, saida.getValor());

            return super.inserir();
        } catch (Exception ex) {
            chamarAlertaErro("Erro ao inserir saida na base de dados", ex.toString());
        }

        return null;
    }

    /**
     * Atualizar dados saida na base de dados
     */
    public boolean editar(Saida saida) {
        try {
            String sql = "UPDATE saida SET id_categoria =?, id_administrador =?, descricao_saida =?, valor_saida =?, data_saida =? WHERE id_saida =?";

            stm = getConector().prepareStatement(sql);

            stm.setInt(1, saida.getCategoria().getId().intValue());
            stm.setInt(2, saida.getAdministrador().getId().intValue());
            stm.setString(3, saida.getDescricao());
            stm.setFloat(4, saida.getValor());
            stm.setLong(5, saida.getData());

            stm.setInt(6, saida.getId().intValue());

            stm.executeUpdate();
            stm.close();

        } catch (SQLException ex) {
            chamarAlertaErro("Erro ao atualizar saida na base de dados!", ex.toString());
            return false;
        }

        return true;
    }

    /**
     * Excluir saida na base de dados
     */
    public boolean excluir(int id) {
        try {
            String sql = "DELETE FROM saida WHERE id_saida=?";

            stm = getConector().prepareStatement(sql);

            stm.setInt(1, id);
            stm.execute();

            stm.close();
        } catch (SQLException ex) {
            chamarAlertaErro("Erro ao excluir saida na base de dados!", ex.toString());
            return false;
        }

        return true;
    }

    /**
     * Consultar todas saidas cadastradas na base de dados
     */
    private List<Saida> listar() {

        List<Saida> saidas = new ArrayList<>();

        try {
            String sql = "SELECT saida.* FROM saida";

            stm = getConector().prepareStatement(sql);
            rs = stm.executeQuery(sql);

            while (rs.next()) {
                Saida saida = new Saida(rs.getLong(1), null, rs.getString(3), null, rs.getFloat(5), rs.getLong(6));

                saidas.add(saida);
            }

            stm.close();
            rs.close();

        } catch (SQLException ex) {
            chamarAlertaErro("Erro ao consultar saidas na base de dados!", ex.toString());
        }

        return saidas;
    }

    private List<Saida> buscarPorDescricao(String busca) {

        List<Saida> saidas = new ArrayList<>();

        try {
            String sql = "SELECT saida.* FROM saida WHERE descricao_saida LIKE '%" + busca + "%'";

            stm = getConector().prepareStatement(sql);
            rs = stm.executeQuery(sql);

            while (rs.next()) {
                Saida saida = new Saida(rs.getLong(1), null, rs.getString(3), null, rs.getFloat(5), rs.getLong(6));

                saidas.add(saida);
            }

            stm.close();
            rs.close();

        } catch (SQLException ex) {
            chamarAlertaErro("Erro ao consultar saidas na base de dados!", ex.toString());
        }

        return saidas;
    }

    public List<Saida> buscarPorIntervalo(String dataInicio, String dataFinal) {
        Long inicio = DateUtils.getLongFromDate(dataInicio);
        Long finall = DateUtils.getLongFromDate(dataFinal);

        List<Saida> saidas = new ArrayList<>();

        try {
            String sql = "SELECT saida.* FROM saida WHERE data_saida >= " + inicio + " AND data_saida < " + finall;

            stm = getConector().prepareStatement(sql);
            rs = stm.executeQuery(sql);

            while (rs.next()) {
                Saida saida = new Saida(rs.getLong(1), null, rs.getString(3), null, rs.getFloat(5), rs.getLong(6));

                saidas.add(saida);
            }

            stm.close();
            rs.close();

        } catch (SQLException ex) {
            chamarAlertaErro("Erro ao consultar saidas na base de dados!", ex.toString());
        }

        return saidas;
    }

}
