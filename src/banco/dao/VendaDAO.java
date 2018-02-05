package banco.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Celular;
import model.Marca;
import model.Produto;

/**
 * DAO responsável pela ações realizadas na base de dados referentes as marcas
 */
public class VendaDAO extends DAO {

    public VendaDAO() {
        super();
    }

    /**
     * Inserir marca na base de dados
     */
    private boolean inserir(Marca marca) {
        try {
            String sql = "INSERT INTO marca ( descricao_marca ) VALUES (?)";

            stm = getConector().prepareStatement(sql);

            stm.setString(1, marca.getDescricao());

            stm.executeUpdate();
            stm.close();

        } catch (SQLException ex) {
            chamarAlertaErro("Erro ao inserir marca na base de dados", ex.toString());
            return false;
        }

        return true;
    }

    /**
     * Atualizar dados marca na base de dados
     */
    private boolean editar(Marca marca) {
        try {
            String sql = "UPDATE marca SET descricao_marca =? WHERE id_marca =?";

            stm = getConector().prepareStatement(sql);

            stm.setString(1, marca.getDescricao());

            stm.setInt(2, marca.getId().intValue());

            stm.executeUpdate();
            stm.close();

        } catch (SQLException ex) {
            chamarAlertaErro("Erro ao atualizar marca na base de dados!", ex.toString());
            return false;
        }

        return true;
    }

    /**
     * Excluir marca na base de dados
     */
    private boolean excluir(int id) {
        try {
            String sql = "DELETE FROM marca WHERE id_marca=?";

            stm = getConector().prepareStatement(sql);

            stm.setInt(1, id);
            stm.execute();

            stm.close();
        } catch (SQLException ex) {
            chamarAlertaErro("Erro ao excluir marca na base de dados!", ex.toString());
            return false;
        }

        return true;
    }

    /**
     * Consultar todas marcas cadastradas na base de dados
     */
    private List<Marca> listar() {

        List<Marca> marcas = new ArrayList<>();

        try {
            String sql = "SELECT marca.* FROM marca";

            stm = getConector().prepareStatement(sql);
            rs = stm.executeQuery(sql);

            while (rs.next()) {
                Marca marca = new Marca((long) rs.getInt(1), rs.getString(2));

                marcas.add(marca);
            }

            stm.close();
            rs.close();

        } catch (SQLException ex) {
            chamarAlertaErro("Erro ao consultar marcas na base de dados!", ex.toString());
        }

        return marcas;
    }

    private Marca buscarPorId(int aInt) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
