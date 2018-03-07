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
    public Long inserir(Manutencao manutencao) throws Exception {
        if (manutencao.getCliente().getId() == null) {
            Long id = ControleDAO.getBanco().getClienteDAO().inserir(manutencao.getCliente());
            manutencao.getCliente().setId(id);
        }

        String sql = "INSERT INTO manutencao ( id_cliente, id_administrador, descricao, data_cadastro, data_previsao, data_entrega, preco, finalizado, marca, modelo, imei, cor) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

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

    }

    /**
     * Atualizar dados marca na base de dados
     */
    public boolean editar(Manutencao manutencao) throws SQLException {
        String sql = "UPDATE manutencao SET id_cliente =?, id_administrador =?, descricao =?, data_previsao =?, data_entrega =?, preco =?, finalizado =?, marca =?, modelo =?, imei =?, cor =? WHERE id =?";

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

        return true;
    }

    /**
     * Excluir manutencao na base de dados
     */
    public boolean excluir(int id) throws SQLException {
        String sql = "DELETE FROM manutencao WHERE id=?";

        stm = getConector().prepareStatement(sql);

        stm.setInt(1, id);
        stm.execute();

        stm.close();

        return true;
    }

    //Consultar todas Manutencao cadastradas na base de dados
    public List<Manutencao> listar() throws SQLException {

        List<Manutencao> manuntencoes = new ArrayList<>();

        String sql = "SELECT * FROM view_manutencao";

        stm = getConector().prepareStatement(sql);
        rs = stm.executeQuery(sql);

        while (rs.next()) {
            Administrador adm = new Administrador(rs.getLong("id_administrador"), rs.getString("nome_administrador"), null, null, null, null, null, null, null, 1);
            Cliente cliente = new Cliente(rs.getLong("id_cliente"), rs.getString("nome_cliente"), null, null, null, null, null, null, 1);

            manuntencoes.add(new Manutencao(rs.getLong("id"), rs.getString("descricao"), cliente, adm, rs.getString("marca"), rs.getString("modelo"), rs.getString("imei"), rs.getString("cor"), rs.getLong("data_cadastro"), rs.getLong("data_previsao"), rs.getLong("data_entrega"), rs.getFloat("preco"), rs.getBoolean("finalizado")));
        }

        stm.close();
        rs.close();

        return manuntencoes;
    }

    public List<Manutencao> buscarPorCliente(Cliente c) throws SQLException {

        List<Manutencao> manuntencoes = new ArrayList<>();

        String sql = "SELECT * FROM view_manutencao WHERE id_cliente = " + c.getId();

        stm = getConector().prepareStatement(sql);
        rs = stm.executeQuery(sql);

        while (rs.next()) {
            Administrador adm = new Administrador(rs.getLong("id_administrador"), rs.getString("nome_administrador"), null, null, null, null, null, null, null, 1);
            Cliente cliente = new Cliente(rs.getLong("id_cliente"), rs.getString("nome_cliente"), null, null, null, null, null, null, 1);

            manuntencoes.add(new Manutencao(rs.getLong("id"), rs.getString("descricao"), cliente, adm, rs.getString("marca"), rs.getString("modelo"), rs.getString("imei"), rs.getString("cor"), rs.getLong("data_cadastro"), rs.getLong("data_previsao"), rs.getLong("data_entrega"), rs.getFloat("preco"), rs.getBoolean("finalizado")));
        }

        stm.close();
        rs.close();

        return manuntencoes;
    }

    public List<Manutencao> buscarFinalizadas() throws SQLException {

        List<Manutencao> manuntencoes = new ArrayList<>();

        String sql = "SELECT * FROM view_manutencao WHERE finalizado = true";

        stm = getConector().prepareStatement(sql);
        rs = stm.executeQuery(sql);

        while (rs.next()) {
            Administrador adm = new Administrador(rs.getLong("id_administrador"), rs.getString("nome_administrador"), null, null, null, null, null, null, null, 1);
            Cliente cliente = new Cliente(rs.getLong("id_cliente"), rs.getString("nome_cliente"), null, null, null, null, null, null, 1);

            manuntencoes.add(new Manutencao(rs.getLong("id"), rs.getString("descricao"), cliente, adm, rs.getString("marca"), rs.getString("modelo"), rs.getString("imei"), rs.getString("cor"), rs.getLong("data_cadastro"), rs.getLong("data_previsao"), rs.getLong("data_entrega"), rs.getFloat("preco"), rs.getBoolean("finalizado")));
        }

        stm.close();
        rs.close();

        return manuntencoes;
    }

    public List<Manutencao> buscarPendentes() throws SQLException {

        List<Manutencao> manuntencoes = new ArrayList<>();

        String sql = "SELECT * FROM view_manutencao WHERE finalizado = false";

        stm = getConector().prepareStatement(sql);
        rs = stm.executeQuery(sql);

        while (rs.next()) {
            Administrador adm = new Administrador(rs.getLong("id_administrador"), rs.getString("nome_administrador"), null, null, null, null, null, null, null, 1);
            Cliente cliente = new Cliente(rs.getLong("id_cliente"), rs.getString("nome_cliente"), null, null, null, null, null, null, 1);

            manuntencoes.add(new Manutencao(rs.getLong("id"), rs.getString("descricao"), cliente, adm, rs.getString("marca"), rs.getString("modelo"), rs.getString("imei"), rs.getString("cor"), rs.getLong("data_cadastro"), rs.getLong("data_previsao"), rs.getLong("data_entrega"), rs.getFloat("preco"), rs.getBoolean("finalizado")));
        }

        stm.close();
        rs.close();

        return manuntencoes;
    }

    //datas no formato dd/MM/yyyy
    public List<Manutencao> buscarPorIntervalo(String dataInicio, String dataFinal) throws SQLException {

        List<Manutencao> manuntencoes = new ArrayList<>();

        Long inicio = DateUtils.getLongFromDate(dataInicio);
        Long finall = DateUtils.getLongFromDate(dataFinal);

        String sql = "SELECT * FROM view_manutencao"
                + "\nWHERE manutencao.data_cadastro >= " + inicio + " AND manutencao.data_cadastro < " + finall;

        System.out.println(sql);
        stm = getConector().prepareStatement(sql);
        rs = stm.executeQuery(sql);

        while (rs.next()) {
            Administrador adm = new Administrador(rs.getLong("id_administrador"), rs.getString("nome_administrador"), null, null, null, null, null, null, null, 1);
            Cliente cliente = new Cliente(rs.getLong("id_cliente"), rs.getString("nome_cliente"), null, null, null, null, null, null, 1);

            manuntencoes.add(new Manutencao(rs.getLong("id"), rs.getString("descricao"), cliente, adm, rs.getString("marca"), rs.getString("modelo"), rs.getString("imei"), rs.getString("cor"), rs.getLong("data_cadastro"), rs.getLong("data_previsao"), rs.getLong("data_entrega"), rs.getFloat("preco"), rs.getBoolean("finalizado")));
        }

        stm.close();
        rs.close();

        return manuntencoes;
    }

}
