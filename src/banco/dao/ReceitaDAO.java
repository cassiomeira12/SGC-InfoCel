package banco.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Celular;
import model.Marca;
import model.Produto;
import model.Receita;

/**
 * DAO responsável pela ações realizadas na base de dados referentes as receitas
 */
public class ReceitaDAO extends DAO {

    public ReceitaDAO() {
        super();
    }

    /**
     * Inserir marca na base de dados
     */
    public void inserir(Receita receita) {
        try {
            String sql = "INSERT INTO receita ( id_cliente, id_administrador, descricao_receita, data, valor ) VALUES (?, ?, ?, ?, ?)";

            stm = getConector().prepareStatement(sql);

            stm.setInt(1, receita.getCliente().getId().intValue());
            stm.setInt(2, receita.getAdministrador().getId().intValue());
            stm.setString(3, receita.getDescricao());
            stm.setLong(4, System.currentTimeMillis());
            stm.setFloat(5, receita.getValor());

            stm.executeUpdate();
            stm.close();

        } catch (SQLException ex) {
            chamarAlertaErro("Erro ao inserir marca na base de dados", ex.toString());
        }
    }

    /**
     * Atualizar dados receita na base de dados
     */
    public void editar(Receita receita) {
        try {
            String sql = "UPDATE receita SET id_cliente =?, id_administrador =?, descricao_receita =?, valor =? WHERE id_receita =?";

            stm = getConector().prepareStatement(sql);

            stm.setInt(1, receita.getCliente().getId().intValue());
            stm.setInt(2, receita.getAdministrador().getId().intValue());
            stm.setString(3, receita.getDescricao());
            stm.setFloat(4, receita.getValor());

            stm.setInt(5, receita.getId().intValue());

            stm.executeUpdate();
            stm.close();

        } catch (SQLException ex) {
            chamarAlertaErro("Erro ao atualizar receita na base de dados!", ex.toString());
        }
    }

    /**
     * Excluir marca na base de dados
     */
    public void excluir(int id) {
        try {
            String sql = "DELETE FROM receita WHERE id_receita=?";

            stm = getConector().prepareStatement(sql);

            stm.setInt(1, id);
            stm.execute();

            stm.close();
        } catch (SQLException ex) {
            chamarAlertaErro("Erro ao excluir receita na base de dados!", ex.toString());
        }
    }

    /**
     * Consultar todas receita cadastradas na base de dados
     */
    public List<Receita> listar() {

        List<Receita> receitas = new ArrayList<>();

        try {
            String sql = "SELECT receita.* FROM receita";

            stm = getConector().prepareStatement(sql);
            rs = stm.executeQuery(sql);

            while (rs.next()) {
                Receita receita = new Receita(rs.getLong(1), null, null, rs.getString(4), rs.getLong(5), rs.getFloat(6));

                receitas.add(receita);
            }

            stm.close();
            rs.close();

        } catch (SQLException ex) {
            chamarAlertaErro("Erro ao consultar marcas na base de dados!", ex.toString());
        }

        return receitas;
    }

    Marca buscarPorId(int aInt) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}