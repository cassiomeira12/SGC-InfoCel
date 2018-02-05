package banco.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Manuntencao;
import model.Marca;

/**
 * DAO responsável pela ações realizadas na base de dados referentes as
 * manutencao
 */
public class ManutencaoDAO extends DAO {

    public ManutencaoDAO() {
        super();
    }

    /**
     * Inserir marca na base de dados
     */
    public boolean inserir(Manuntencao manutencao) {
        try {
            String sql = "INSERT INTO manutencao ( id_cliente, id_produto, id_administrador, descricao_manutencao, data_cadastro, data_previsao, data_entrega, preco, finalizado ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

            stm = getConector().prepareStatement(sql);

            stm.setInt(1, manutencao.getCliente().getId().intValue());
            stm.setInt(2, manutencao.getCelular().getId().intValue());
            stm.setInt(3, manutencao.getAdministrador().getId().intValue());
            stm.setString(4, manutencao.getDescricao());
            stm.setLong(5, System.currentTimeMillis());
            stm.setLong(6, manutencao.getDataPrevisaoEntrega());
            stm.setLong(7, manutencao.getDataEntrega());
            stm.setFloat(8, manutencao.getPreco());
            stm.setBoolean(9, manutencao.isFinalizado());

            stm.executeUpdate();
            stm.close();

        } catch (SQLException ex) {
            chamarAlertaErro("Erro ao inserir manutencao na base de dados", ex.toString());
            return false;
        }

        return true;
    }

    /**
     * Atualizar dados marca na base de dados
     */
    public boolean editar(Manuntencao manutencao) {
        try {
            String sql = "UPDATE manutencao SET  id_cliente =?, id_produto =?, id_administrador =?, descricao_manutencao =?, data_previsao =?, data_entrega =?, preco =?, finalizado =? WHERE id_manutencao =?";

            stm = getConector().prepareStatement(sql);

            stm.setInt(1, manutencao.getCliente().getId().intValue());
            stm.setInt(2, manutencao.getCelular().getId().intValue());
            stm.setInt(3, manutencao.getAdministrador().getId().intValue());
            stm.setString(4, manutencao.getDescricao());
            stm.setLong(5, manutencao.getDataPrevisaoEntrega());
            stm.setLong(6, manutencao.getDataEntrega());
            stm.setFloat(7, manutencao.getPreco());
            stm.setBoolean(8, manutencao.isFinalizado());

            stm.setInt(9, manutencao.getId().intValue());

            stm.executeUpdate();
            stm.close();

        } catch (SQLException ex) {
            chamarAlertaErro("Erro ao atualizar manutencao na base de dados!", ex.toString());
            return false;
        }

        return true;
    }

    /**
     * Excluir marca na base de dados
     */
    public boolean excluir(int id) {
        try {
            String sql = "DELETE FROM manutencao WHERE id_manutencao=?";

            stm = getConector().prepareStatement(sql);

            stm.setInt(1, id);
            stm.execute();

            stm.close();
        } catch (SQLException ex) {
            chamarAlertaErro("Erro ao excluir manutencao na base de dados!", ex.toString());
            return false;
        }

        return true;
    }

    /**
     * Consultar todas Manutencao cadastradas na base de dados
     */
    private List<Manuntencao> listar() {

        List<Manuntencao> manuntencoes = new ArrayList<>();

        try {
            String sql = "SELECT manutencao.* FROM manutencao";

            stm = getConector().prepareStatement(sql);
            rs = stm.executeQuery(sql);

            while (rs.next()) {
                Manuntencao manuntencao = new Manuntencao(rs.getLong(1), rs.getString(2), null, null, null, null, null, null, 0, true);

                manuntencoes.add(manuntencao);
            }

            stm.close();
            rs.close();

        } catch (SQLException ex) {
            chamarAlertaErro("Erro ao consultar marcas na base de dados!", ex.toString());
        }

        return manuntencoes;
    }

    Marca buscarPorId(int aInt) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
