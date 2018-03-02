package banco.dao;

import banco.ControleDAO;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Administrador;
import model.Celular;
import model.Cliente;
import model.Manutencao;
import model.Marca;
import model.Produto;
import util.DateUtils;

/**
 * DAO responsável pela ações realizadas na base de dados referentes as
 * manutencao
 */
public class ManutencaoDAO extends DAO {

    public ManutencaoDAO() {
        super();
    }

    /**
     * Inserir manutenção na base de dados
     */
    public Long inserir(Manutencao manutencao) {
        try {
            if (manutencao.getCliente().getId() == null) {
                Long id = ControleDAO.getBanco().getClienteDAO().inserir(manutencao.getCliente());
                manutencao.getCliente().setId(id);
            }

            String sql = "INSERT INTO manutencao ( cliente_id, administrador_id, descricao_manutencao, data_cadastro_manutencao, data_previsao, data_entrega, preco, finalizado, marca, modelo, imei, cor) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            stm = getConector().prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

            stm.setInt(1, manutencao.getCliente().getId().intValue());
            stm.setInt(2, manutencao.getAdministrador().getId().intValue());
            stm.setString(3, manutencao.getDescricao());
            stm.setLong(4, manutencao.getDataCadastro());
            stm.setLong(5, manutencao.getDataPrevisaoEntrega());
            stm.setLong(6, manutencao.getDataEntrega());
            stm.setFloat(7, manutencao.getPreco());
            stm.setBoolean(8, manutencao.isFinalizado());
            stm.setString(9, manutencao.getMarca());
            stm.setString(10, manutencao.getModelo());
            stm.setString(11, manutencao.getImei());
            stm.setString(12, manutencao.getCor());

            return super.inserir();
        } catch (Exception ex) {
            chamarAlertaErro("Erro ao inserir manutencao na base de dados", ex.toString());
        }

        return null;
    }

    /**
     * Atualizar dados marca na base de dados
     */
    public boolean editar(Manutencao manutencao) {
        try {
            String sql = "UPDATE manutencao SET  cliente_id =?, administrador_id =?, descricao_manutencao =?, data_previsao =?, data_entrega =?, preco =?, finalizado =?, marca =?, modelo =?, imei =?, cor =? WHERE id_manutencao =?";

            stm = getConector().prepareStatement(sql);

            stm.setInt(1, manutencao.getCliente().getId().intValue());
            stm.setInt(2, manutencao.getAdministrador().getId().intValue());
            stm.setString(3, manutencao.getDescricao());
            stm.setLong(4, manutencao.getDataCadastro());
            stm.setLong(5, manutencao.getDataPrevisaoEntrega());
            stm.setLong(6, manutencao.getDataEntrega());
            stm.setFloat(7, manutencao.getPreco());
            stm.setBoolean(8, manutencao.isFinalizado());
            stm.setString(9, manutencao.getMarca());
            stm.setString(10, manutencao.getModelo());
            stm.setString(11, manutencao.getImei());
            stm.setString(12, manutencao.getCor());

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
     * Excluir manutencao na base de dados
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

    //Consultar todas Manutencao cadastradas na base de dados
    public List<Manutencao> listar() {

        List<Manutencao> manuntencoes = new ArrayList<>();

        try {
            String sql = "SELECT manutencao.*, administrador.*, cliente.*"
                    + "\nFROM manutencao"
                    + "\nINNER JOIN administrador administrador ON manutencao.administrador_id = administrador.id_administrador"
                    + "\nINNER JOIN cliente cliente ON manutencao.cliente_id = cliente.id_cliente";

            System.out.println(sql);
            stm = getConector().prepareStatement(sql);
            rs = stm.executeQuery(sql);

            while (rs.next()) {
                Administrador adm = new Administrador(rs.getLong("administrador_id"), rs.getString("nome_administrador"), "", "", rs.getString("endereco_administrador"), rs.getString("email_administrador"), rs.getString("cpf_administrador"), rs.getString("rg_administrador"), null, rs.getInt("status_administrador"));
                Cliente cliente = new Cliente(rs.getLong("cliente_id"), rs.getString("nome_cliente"), rs.getString("endereco_cliente"), rs.getString("cpf_cliente"), rs.getString("rg_cliente"), rs.getString("telefone_cliente"), rs.getString("cidade_cliente"), null, rs.getInt("status_cliente"));

                manuntencoes.add(new Manutencao(rs.getLong("id_manutencao"), rs.getString("descricao_manutencao"), cliente, adm, rs.getString("marca"), rs.getString("modelo"), rs.getString("imei"), rs.getString("cor"), rs.getLong("data_cadastro_manutencao"), rs.getLong("data_previsao"), rs.getLong("data_entrega"), rs.getFloat("preco"), rs.getBoolean("finalizado")));
            }

            stm.close();
            rs.close();

        } catch (Exception ex) {
            chamarAlertaErro("Erro ao consultar manutencoes na base de dados!", ex.toString());
        }

        return manuntencoes;
    }

    public List<Manutencao> buscarPorCliente(Cliente c) {

        List<Manutencao> manuntencoes = new ArrayList<>();

        try {
            String sql = "SELECT manutencao.*, administrador.*, cliente.*"
                    + "\nFROM manutencao"
                    + "\nINNER JOIN administrador administrador ON manutencao.administrador_id = administrador.id_administrador"
                    + "\nINNER JOIN cliente cliente ON manutencao.cliente_id = cliente.id_cliente"
                    + "\nWHERE manutencao.id_cliente = " + c.getId();

            System.out.println(sql);
            stm = getConector().prepareStatement(sql);
            rs = stm.executeQuery(sql);

            while (rs.next()) {
                Administrador adm = new Administrador(rs.getLong("administrador_id"), rs.getString("nome_administrador"), "", "", rs.getString("endereco_administrador"), rs.getString("email_administrador"), rs.getString("cpf_administrador"), rs.getString("rg_administrador"), null, rs.getInt("status_administrador"));
                Cliente cliente = new Cliente(rs.getLong("cliente_id"), rs.getString("nome_cliente"), rs.getString("endereco_cliente"), rs.getString("cpf_cliente"), rs.getString("rg_cliente"), rs.getString("telefone_cliente"), rs.getString("cidade_cliente"), null, rs.getInt("status_cliente"));

                manuntencoes.add(new Manutencao(rs.getLong("id_manutencao"), rs.getString("descricao_manutencao"), cliente, adm, rs.getString("marca"), rs.getString("modelo"), rs.getString("imei"), rs.getString("cor"), rs.getLong("data_cadastro_manutencao"), rs.getLong("data_previsao"), rs.getLong("data_entrega"), rs.getFloat("preco"), rs.getBoolean("finalizado")));
            }

            stm.close();
            rs.close();

        } catch (Exception ex) {
            chamarAlertaErro("Erro ao consultar manutencoes na base de dados!", ex.toString());
        }

        return manuntencoes;
    }

    public List<Manutencao> buscarFinalizadas() {

        List<Manutencao> manuntencoes = new ArrayList<>();

        try {
            String sql = "SELECT manutencao.*, administrador.*, cliente.*"
                    + "\nFROM manutencao"
                    + "\nINNER JOIN administrador administrador ON manutencao.administrador_id = administrador.id_administrador"
                    + "\nINNER JOIN cliente cliente ON manutencao.cliente_id = cliente.id_cliente"
                    + "\nWHERE manutencao.finalizado = true";

            System.out.println(sql);
            stm = getConector().prepareStatement(sql);
            rs = stm.executeQuery(sql);

            while (rs.next()) {
                Administrador adm = new Administrador(rs.getLong("administrador_id"), rs.getString("nome_administrador"), "", "", rs.getString("endereco_administrador"), rs.getString("email_administrador"), rs.getString("cpf_administrador"), rs.getString("rg_administrador"), null, rs.getInt("status_administrador"));
                Cliente cliente = new Cliente(rs.getLong("cliente_id"), rs.getString("nome_cliente"), rs.getString("endereco_cliente"), rs.getString("cpf_cliente"), rs.getString("rg_cliente"), rs.getString("telefone_cliente"), rs.getString("cidade_cliente"), null, rs.getInt("status_cliente"));

                manuntencoes.add(new Manutencao(rs.getLong("id_manutencao"), rs.getString("descricao_manutencao"), cliente, adm, rs.getString("marca"), rs.getString("modelo"), rs.getString("imei"), rs.getString("cor"), rs.getLong("data_cadastro_manutencao"), rs.getLong("data_previsao"), rs.getLong("data_entrega"), rs.getFloat("preco"), rs.getBoolean("finalizado")));
            }

            stm.close();
            rs.close();

        } catch (Exception ex) {
            chamarAlertaErro("Erro ao consultar manutencoes na base de dados!", ex.toString());
        }

        return manuntencoes;
    }

    public List<Manutencao> buscarPendentes() {

        List<Manutencao> manuntencoes = new ArrayList<>();

        try {
            String sql = "SELECT manutencao.*, administrador.*, cliente.*"
                    + "\nFROM manutencao"
                    + "\nINNER JOIN administrador administrador ON manutencao.administrador_id = administrador.id_administrador"
                    + "\nINNER JOIN cliente cliente ON manutencao.cliente_id = cliente.id_cliente"
                    + "\nWHERE manutencao.finalizado = false";

            System.out.println(sql);
            stm = getConector().prepareStatement(sql);
            rs = stm.executeQuery(sql);

            while (rs.next()) {
                Administrador adm = new Administrador(rs.getLong("administrador_id"), rs.getString("nome_administrador"), "", "", rs.getString("endereco_administrador"), rs.getString("email_administrador"), rs.getString("cpf_administrador"), rs.getString("rg_administrador"), null, rs.getInt("status_administrador"));
                Cliente cliente = new Cliente(rs.getLong("cliente_id"), rs.getString("nome_cliente"), rs.getString("endereco_cliente"), rs.getString("cpf_cliente"), rs.getString("rg_cliente"), rs.getString("telefone_cliente"), rs.getString("cidade_cliente"), null, rs.getInt("status_cliente"));

                manuntencoes.add(new Manutencao(rs.getLong("id_manutencao"), rs.getString("descricao_manutencao"), cliente, adm, rs.getString("marca"), rs.getString("modelo"), rs.getString("imei"), rs.getString("cor"), rs.getLong("data_cadastro_manutencao"), rs.getLong("data_previsao"), rs.getLong("data_entrega"), rs.getFloat("preco"), rs.getBoolean("finalizado")));
            }

            stm.close();
            rs.close();

        } catch (Exception ex) {
            chamarAlertaErro("Erro ao consultar manutencoes na base de dados!", ex.toString());
        }

        return manuntencoes;
    }

    //datas no formato dd/MM/yyyy
    public List<Manutencao> buscarPorIntervalo(String dataInicio, String dataFinal) {

        List<Manutencao> manuntencoes = new ArrayList<>();

        try {
            Long inicio = DateUtils.getLongFromDate(dataInicio);
            Long finall = DateUtils.getLongFromDate(dataFinal);

            String sql = "SELECT manutencao.*, administrador.*, cliente.*"
                    + "\nFROM manutencao"
                    + "\nINNER JOIN administrador administrador ON manutencao.administrador_id = administrador.id_administrador"
                    + "\nINNER JOIN cliente cliente ON manutencao.cliente_id = cliente.id_cliente"
                    + "\nWHERE manutencao.data_cadastro_manutencao >= " + inicio + " AND manutencao.data_cadastro_manutencao < " + finall;

            System.out.println(sql);
            stm = getConector().prepareStatement(sql);
            rs = stm.executeQuery(sql);

            while (rs.next()) {
                Administrador adm = new Administrador(rs.getLong("administrador_id"), rs.getString("nome_administrador"), "", "", rs.getString("endereco_administrador"), rs.getString("email_administrador"), rs.getString("cpf_administrador"), rs.getString("rg_administrador"), null, rs.getInt("status_administrador"));
                Cliente cliente = new Cliente(rs.getLong("cliente_id"), rs.getString("nome_cliente"), rs.getString("endereco_cliente"), rs.getString("cpf_cliente"), rs.getString("rg_cliente"), rs.getString("telefone_cliente"), rs.getString("cidade_cliente"), null, rs.getInt("status_cliente"));

                manuntencoes.add(new Manutencao(rs.getLong("id_manutencao"), rs.getString("descricao_manutencao"), cliente, adm, rs.getString("marca"), rs.getString("modelo"), rs.getString("imei"), rs.getString("cor"), rs.getLong("data_cadastro_manutencao"), rs.getLong("data_previsao"), rs.getLong("data_entrega"), rs.getFloat("preco"), rs.getBoolean("finalizado")));
            }

            stm.close();
            rs.close();

        } catch (Exception ex) {
            chamarAlertaErro("Erro ao consultar manutencoes na base de dados!", ex.toString());
        }

        return manuntencoes;
    }

}
