package banco.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Administrador;
import model.Celular;
import model.Cliente;
import model.Manuntencao;
import model.Marca;
import model.Produto;

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
    public Long inserir(Manuntencao manutencao) {
        try {
            String sql = "INSERT INTO manutencao ( id_cliente, id_produto, id_administrador, descricao_manutencao, data_cadastro, data_previsao, data_entrega, preco, finalizado ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

            stm = getConector().prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

            stm.setInt(1, manutencao.getCliente().getId().intValue());
            stm.setInt(2, manutencao.getCelular().getId().intValue());
            stm.setInt(3, manutencao.getAdministrador().getId().intValue());
            stm.setString(4, manutencao.getDescricao());
            stm.setLong(5, System.currentTimeMillis());
            stm.setLong(6, manutencao.getDataPrevisaoEntrega());
            stm.setLong(7, manutencao.getDataEntrega());
            stm.setFloat(8, manutencao.getPreco());
            stm.setBoolean(9, manutencao.isFinalizado());

            return super.inserir();
        } catch (Exception ex) {
            chamarAlertaErro("Erro ao inserir manutencao na base de dados", ex.toString());
        }

        return null;
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

    /**
     * Consultar todas Manutencao cadastradas na base de dados
     */
    public List<Manuntencao> listar() {

        List<Manuntencao> manuntencoes = new ArrayList<>();

        try {
            String sql = "SELECT manutencao.*, administrador.*, cliente.*, produto.*, marca.*"
                    + "\nFROM manutencao"
                    + "\nINNER JOIN administrador administrador ON manutencao.id_administrador = administrador.id_administrador"
                    + "\nINNER JOIN cliente cliente ON manutencao.id_cliente = cliente.id_cliente"
                    + "\nINNER JOIN produto produto ON manutencao.id_produto = produto.id_produto"
                    + "\nINNER JOIN marca ON produto.id_marca = marca.id_marca";

            System.out.println(sql);
            stm = getConector().prepareStatement(sql);
            rs = stm.executeQuery(sql);

            while (rs.next()) {
                Administrador adm = new Administrador(rs.getLong("id_administrador"), rs.getString("nome_administrador"), "", "", rs.getString("endereco_administrador"), rs.getString("email_administrador"), rs.getString("cpf_administrador"), rs.getString("rg_administrador"), null, rs.getInt("status_administrador"));
                Cliente cliente = new Cliente(rs.getLong("id_cliente"), rs.getString("nome_cliente"), rs.getString("endereco_cliente"), rs.getString("cpf_cliente"), rs.getString("rg_cliente"), rs.getString("telefone_cliente"), rs.getString("cidade_cliente"), null, rs.getInt("status_cliente"));
                Celular celular = new Celular(rs.getLong("id_produto"), new Marca(null, rs.getString("descricao_marca")), rs.getString("descricao_produto"), null, 0, 0, 0);
                celular.setCor(rs.getString("cor"));
                celular.setImei(rs.getString("imei"));
                celular.setModelo(rs.getString("modelo"));

                manuntencoes.add(new Manuntencao(rs.getLong("id_manutencao"), rs.getString("descricao_manutencao"), cliente, adm, celular, rs.getLong("data_cadastro_manutencao"), rs.getLong("data_previsao"), rs.getLong("data_entrega"), rs.getFloat("preco"), rs.getBoolean("finalizado")));
            }

            stm.close();
            rs.close();

        } catch (Exception ex) {
            chamarAlertaErro("Erro ao consultar manutencoes na base de dados!", ex.toString());
        }

        return manuntencoes;
    }

    public List<Manuntencao> buscarPorCliente(Cliente c) {

        List<Manuntencao> manuntencoes = new ArrayList<>();

        try {
            String sql = "SELECT manutencao.*, administrador.*, cliente.*, produto.*, marca.*"
                    + "\nFROM manutencao"
                    + "\nINNER JOIN administrador administrador ON manutencao.id_administrador = administrador.id_administrador"
                    + "\nINNER JOIN cliente cliente ON manutencao.id_cliente = cliente.id_cliente"
                    + "\nINNER JOIN produto produto ON manutencao.id_produto = produto.id_produto"
                    + "\nINNER JOIN marca ON produto.id_marca = marca.id_marca"
                    + "\nWHERE manutencao.id_cliente = " + c.getId();

            System.out.println(sql);
            stm = getConector().prepareStatement(sql);
            rs = stm.executeQuery(sql);

            while (rs.next()) {
                Administrador adm = new Administrador(rs.getLong("id_administrador"), rs.getString("nome_administrador"), "", "", rs.getString("endereco_administrador"), rs.getString("email_administrador"), rs.getString("cpf_administrador"), rs.getString("rg_administrador"), null, rs.getInt("status_administrador"));
                Cliente cliente = new Cliente(rs.getLong("id_cliente"), rs.getString("nome_cliente"), rs.getString("endereco_cliente"), rs.getString("cpf_cliente"), rs.getString("rg_cliente"), rs.getString("telefone_cliente"), rs.getString("cidade_cliente"), null, rs.getInt("status_cliente"));
                Celular celular = new Celular(rs.getLong("id_produto"), new Marca(null, rs.getString("descricao_marca")), rs.getString("descricao_produto"), null, 0, 0, 0);
                celular.setCor(rs.getString("cor"));
                celular.setImei(rs.getString("imei"));
                celular.setModelo(rs.getString("modelo"));

                manuntencoes.add(new Manuntencao(rs.getLong("id_manutencao"), rs.getString("descricao_manutencao"), cliente, adm, celular, rs.getLong("data_cadastro_manutencao"), rs.getLong("data_previsao"), rs.getLong("data_entrega"), rs.getFloat("preco"), rs.getBoolean("finalizado")));
            }

            stm.close();
            rs.close();

        } catch (Exception ex) {
            chamarAlertaErro("Erro ao consultar manutencoes na base de dados!", ex.toString());
        }

        return manuntencoes;
    }

    public List<Manuntencao> buscarFinalizadas() {

        List<Manuntencao> manuntencoes = new ArrayList<>();

        try {
            String sql = "SELECT manutencao.*, administrador.*, cliente.*, produto.*, marca.*"
                    + "\nFROM manutencao"
                    + "\nINNER JOIN administrador administrador ON manutencao.id_administrador = administrador.id_administrador"
                    + "\nINNER JOIN cliente cliente ON manutencao.id_cliente = cliente.id_cliente"
                    + "\nINNER JOIN produto produto ON manutencao.id_produto = produto.id_produto"
                    + "\nINNER JOIN marca ON produto.id_marca = marca.id_marca"
                    + "\nWHERE manutencao.finalizado = true";

            System.out.println(sql);
            stm = getConector().prepareStatement(sql);
            rs = stm.executeQuery(sql);

            while (rs.next()) {
                Administrador adm = new Administrador(rs.getLong("id_administrador"), rs.getString("nome_administrador"), "", "", rs.getString("endereco_administrador"), rs.getString("email_administrador"), rs.getString("cpf_administrador"), rs.getString("rg_administrador"), null, rs.getInt("status_administrador"));
                Cliente cliente = new Cliente(rs.getLong("id_cliente"), rs.getString("nome_cliente"), rs.getString("endereco_cliente"), rs.getString("cpf_cliente"), rs.getString("rg_cliente"), rs.getString("telefone_cliente"), rs.getString("cidade_cliente"), null, rs.getInt("status_cliente"));
                Celular celular = new Celular(rs.getLong("id_produto"), new Marca(null, rs.getString("descricao_marca")), rs.getString("descricao_produto"), null, 0, 0, 0);
                celular.setCor(rs.getString("cor"));
                celular.setImei(rs.getString("imei"));
                celular.setModelo(rs.getString("modelo"));

                manuntencoes.add(new Manuntencao(rs.getLong("id_manutencao"), rs.getString("descricao_manutencao"), cliente, adm, celular, rs.getLong("data_cadastro_manutencao"), rs.getLong("data_previsao"), rs.getLong("data_entrega"), rs.getFloat("preco"), rs.getBoolean("finalizado")));
            }

            stm.close();
            rs.close();

        } catch (Exception ex) {
            chamarAlertaErro("Erro ao consultar manutencoes na base de dados!", ex.toString());
        }

        return manuntencoes;
    }

    public List<Manuntencao> buscarPendentes() {

        List<Manuntencao> manuntencoes = new ArrayList<>();

        try {
            String sql = "SELECT manutencao.*, administrador.*, cliente.*, produto.*, marca.*"
                    + "\nFROM manutencao"
                    + "\nINNER JOIN administrador administrador ON manutencao.id_administrador = administrador.id_administrador"
                    + "\nINNER JOIN cliente cliente ON manutencao.id_cliente = cliente.id_cliente"
                    + "\nINNER JOIN produto produto ON manutencao.id_produto = produto.id_produto"
                    + "\nINNER JOIN marca ON produto.id_marca = marca.id_marca"
                    + "\nWHERE manutencao.finalizado = false";

            System.out.println(sql);
            stm = getConector().prepareStatement(sql);
            rs = stm.executeQuery(sql);

            while (rs.next()) {
                Administrador adm = new Administrador(rs.getLong("id_administrador"), rs.getString("nome_administrador"), "", "", rs.getString("endereco_administrador"), rs.getString("email_administrador"), rs.getString("cpf_administrador"), rs.getString("rg_administrador"), null, rs.getInt("status_administrador"));
                Cliente cliente = new Cliente(rs.getLong("id_cliente"), rs.getString("nome_cliente"), rs.getString("endereco_cliente"), rs.getString("cpf_cliente"), rs.getString("rg_cliente"), rs.getString("telefone_cliente"), rs.getString("cidade_cliente"), null, rs.getInt("status_cliente"));
                Celular celular = new Celular(rs.getLong("id_produto"), new Marca(null, rs.getString("descricao_marca")), rs.getString("descricao_produto"), null, 0, 0, 0);
                celular.setCor(rs.getString("cor"));
                celular.setImei(rs.getString("imei"));
                celular.setModelo(rs.getString("modelo"));

                manuntencoes.add(new Manuntencao(rs.getLong("id_manutencao"), rs.getString("descricao_manutencao"), cliente, adm, celular, rs.getLong("data_cadastro_manutencao"), rs.getLong("data_previsao"), rs.getLong("data_entrega"), rs.getFloat("preco"), rs.getBoolean("finalizado")));
            }

            stm.close();
            rs.close();

        } catch (Exception ex) {
            chamarAlertaErro("Erro ao consultar manutencoes na base de dados!", ex.toString());
        }

        return manuntencoes;
    }

}
