package banco.dao;

import banco.ControleDAO;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Administrador;
import model.Celular;
import model.Cliente;
import model.Marca;
import model.Produto;
import model.Receita;
import util.DateUtils;

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
    public Long inserir(Receita receita) {
        try {
            if (receita.getCliente().getId() == null) {
                Long id = ControleDAO.getBanco().getClienteDAO().inserir(receita.getCliente());
                receita.getCliente().setId(id);

            }

            String sql = "INSERT INTO receita ( cliente_id, administrador_id, descricao_receita, data_receita, valor_receita ) VALUES (?, ?, ?, ?, ?)";

            stm = getConector().prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

            stm.setInt(1, receita.getCliente().getId().intValue());
            stm.setInt(2, receita.getAdministrador().getId().intValue());
            stm.setString(3, receita.getDescricao());
            stm.setLong(4, receita.getData());
            stm.setFloat(5, receita.getValor());

            return super.inserir();
        } catch (Exception ex) {
            chamarAlertaErro("Erro ao inserir marca na base de dados", ex.toString());
        }

        return null;
    }

    /**
     * Atualizar dados receita na base de dados
     */
    public boolean editar(Receita receita) {
        try {
            String sql = "UPDATE receita SET cliente_id =?, administrador_id =?, descricao_receita =?, valor_receita =?, data_receita =? WHERE id_receita =?";

            stm = getConector().prepareStatement(sql);

            stm.setInt(1, receita.getCliente().getId().intValue());
            stm.setInt(2, receita.getAdministrador().getId().intValue());
            stm.setString(3, receita.getDescricao());
            stm.setFloat(4, receita.getValor());
            stm.setLong(5, receita.getData());

            stm.setInt(6, receita.getId().intValue());

            stm.executeUpdate();
            stm.close();

        } catch (SQLException ex) {
            chamarAlertaErro("Erro ao atualizar receita na base de dados!", ex.toString());
            return false;
        }

        return true;
    }

    /**
     * Excluir marca na base de dados
     */
    public boolean excluir(int id) {
        try {
            String sql = "DELETE FROM receita WHERE id_receita=?";

            stm = getConector().prepareStatement(sql);

            stm.setInt(1, id);
            stm.execute();

            stm.close();
        } catch (SQLException ex) {
            chamarAlertaErro("Erro ao excluir receita na base de dados!", ex.toString());
            return false;
        }

        return true;
    }

    /**
     * Consultar todas receita cadastradas na base de dados
     */
    private List<Receita> listar() {

        List<Receita> receitas = new ArrayList<>();

        try {
            String sql = "SELECT receita.*, cliente.*, administrador.* "
                    + "FROM receita"
                    + "\nINNER JOIN cliente cliente ON receita.cliente_id = cliente.id_cliente"
                    + "\nINNER JOIN administrador administrador ON receita.administrador_id = administrador.id_administrador";

            stm = getConector().prepareStatement(sql);
            rs = stm.executeQuery(sql);

            while (rs.next()) {
                Administrador adm = new Administrador(rs.getLong("administrador_id"), rs.getString("nome_administrador"), "", "", rs.getString("endereco_administrador"), rs.getString("email_administrador"), rs.getString("cpf_administrador"), rs.getString("rg_administrador"), null, rs.getInt("status_administrador"));
                Cliente cliente = new Cliente(rs.getLong("cliente_id"), rs.getString("nome_cliente"), rs.getString("endereco_cliente"), rs.getString("cpf_cliente"), rs.getString("rg_cliente"), rs.getString("telefone_cliente"), rs.getString("cidade_cliente"), null, rs.getInt("status_cliente"));

                Receita receita = new Receita(rs.getLong(1), cliente, adm, rs.getString(4), rs.getLong(5), rs.getFloat(6));

                receitas.add(receita);
            }

            stm.close();
            rs.close();

        } catch (SQLException ex) {
            chamarAlertaErro("Erro ao consultar marcas na base de dados!", ex.toString());
        }

        return receitas;
    }

    private List<Receita> buscarPorDescricao(String busca) {

        List<Receita> receitas = new ArrayList<>();

        try {
            String sql = "SELECT receita.*, cliente.*, administrador.* "
                    + "FROM receita"
                    + "\nINNER JOIN cliente cliente ON receita.cliente_id = cliente.id_cliente"
                    + "\nINNER JOIN administrador administrador ON receita.administrador_id = administrador.id_administrador"
                    + "\nWHERE descricao_receita LIKE '%" + busca + "%'";

            stm = getConector().prepareStatement(sql);
            rs = stm.executeQuery(sql);

            while (rs.next()) {
                Administrador adm = new Administrador(rs.getLong("administrador_id"), rs.getString("nome_administrador"), "", "", rs.getString("endereco_administrador"), rs.getString("email_administrador"), rs.getString("cpf_administrador"), rs.getString("rg_administrador"), null, rs.getInt("status_administrador"));
                Cliente cliente = new Cliente(rs.getLong("cliente_id"), rs.getString("nome_cliente"), rs.getString("endereco_cliente"), rs.getString("cpf_cliente"), rs.getString("rg_cliente"), rs.getString("telefone_cliente"), rs.getString("cidade_cliente"), null, rs.getInt("status_cliente"));

                Receita receita = new Receita(rs.getLong(1), cliente, adm, rs.getString(4), rs.getLong(5), rs.getFloat(6));

                receitas.add(receita);
            }

            stm.close();
            rs.close();

        } catch (SQLException ex) {
            chamarAlertaErro("Erro ao consultar marcas na base de dados!", ex.toString());
        }

        return receitas;
    }

    public List<Receita> buscarPorIntervalo(String dataInicio, String dataFinal) {
        Long inicio = DateUtils.getLongFromDate(dataInicio);
        Long finall = DateUtils.getLongFromDate(dataFinal);

        List<Receita> receitas = new ArrayList<>();

        try {
            String sql = "SELECT receita.*, cliente.*, administrador.* "
                    + "FROM receita"
                    + "\nINNER JOIN cliente cliente ON receita.cliente_id = cliente.id_cliente"
                    + "\nINNER JOIN administrador administrador ON receita.administrador_id = administrador.id_administrador"
                    + "\nWHERE data_receita >= " + inicio + " AND data_receita < " + finall;

            stm = getConector().prepareStatement(sql);
            rs = stm.executeQuery(sql);

            while (rs.next()) {
                Administrador adm = new Administrador(rs.getLong("administrador_id"), rs.getString("nome_administrador"), "", "", rs.getString("endereco_administrador"), rs.getString("email_administrador"), rs.getString("cpf_administrador"), rs.getString("rg_administrador"), null, rs.getInt("status_administrador"));
                Cliente cliente = new Cliente(rs.getLong("cliente_id"), rs.getString("nome_cliente"), rs.getString("endereco_cliente"), rs.getString("cpf_cliente"), rs.getString("rg_cliente"), rs.getString("telefone_cliente"), rs.getString("cidade_cliente"), null, rs.getInt("status_cliente"));

                Receita receita = new Receita(rs.getLong(1), cliente, adm, rs.getString(4), rs.getLong(5), rs.getFloat(6));

                receitas.add(receita);
            }

            stm.close();
            rs.close();

        } catch (SQLException ex) {
            chamarAlertaErro("Erro ao consultar marcas na base de dados!", ex.toString());
        }

        return receitas;
    }

}
