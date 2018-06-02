package banco.dao;

import banco.ControleDAO;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Administrador;
import model.Bairro;
import model.Cidade;
import model.Cliente;
import model.Endereco;
import model.FormaPagamento;
import model.Manutencao;
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

        String sql = "INSERT INTO manutencao ( id_cliente, id_administrador,id_forma_pagamento, descricao, data_cadastro, data_previsao, data_entrega, preco, finalizado, marca, modelo, imei, cor) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        stm = getConector().prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

        stm.setInt(1, manutencao.getCliente().getId().intValue());
        stm.setInt(2, manutencao.getAdministrador().getId().intValue());
        stm.setLong(3, manutencao.getFormaPagamento().getId());
        stm.setString(4, manutencao.getDescricao());
        stm.setLong(5, manutencao.getDataCadastro());
        stm.setLong(6, manutencao.getDataPrevisaoEntrega());
        stm.setLong(7, manutencao.getDataEntrega());
        stm.setFloat(8, manutencao.getPreco());
        stm.setBoolean(9, manutencao.isFinalizado());
        stm.setString(10, manutencao.getMarca());
        stm.setString(11, manutencao.getModelo());
        stm.setString(12, manutencao.getImei());
        stm.setString(13, manutencao.getCor());

        return super.inserir();

    }

    /**
     * Atualizar dados marca na base de dados
     */
    public boolean editar(Manutencao manutencao) throws SQLException {
        String sql = "UPDATE manutencao SET id_cliente =?, id_administrador =?, id_forma_pagamento = ?, descricao =?, data_previsao =?, data_entrega =?, preco =?, finalizado =?, marca =?, modelo =?, imei =?, cor =? WHERE id =?";

        stm = getConector().prepareStatement(sql);

        stm.setInt(1, manutencao.getCliente().getId().intValue());
        stm.setInt(2, manutencao.getAdministrador().getId().intValue());
        stm.setInt(3, manutencao.getFormaPagamento().getId().intValue());
        stm.setString(4, manutencao.getDescricao());
        stm.setLong(5, manutencao.getDataCadastro());
        stm.setLong(6, manutencao.getDataPrevisaoEntrega());
        stm.setLong(7, manutencao.getDataEntrega());
        stm.setFloat(8, manutencao.getPreco());
        stm.setBoolean(9, manutencao.isFinalizado());
        stm.setString(10, manutencao.getMarca());
        stm.setString(11, manutencao.getModelo());
        stm.setString(12, manutencao.getImei());
        stm.setString(13, manutencao.getCor());

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

        String sql = "SELECT * FROM view_manutenca";

        stm = getConector().prepareStatement(sql);
        rs = stm.executeQuery(sql);

        while (rs.next()) {
            Cidade cidadeAdm = new Cidade(rs.getLong("id_cidade_administrador"), rs.getString("nome_cidade_administrador"));
            Bairro bairroAdm = new Bairro(rs.getLong("id_bairro_administrador"), rs.getString("nome_bairro_administrador"), cidadeAdm);
            Endereco enderecoAdm = new Endereco(rs.getLong("id_endereco_administrador"), bairroAdm, rs.getString("rua_administrador"), rs.getString("numero_administrador"));

            Administrador adm = new Administrador(rs.getLong("id_administrador"), rs.getString("nome_administrador"),
                    null, null, enderecoAdm, null, rs.getString("cpf_administrador"), null, null, true);

            Cidade cidadeC = new Cidade(rs.getLong("id_cidade_cliente"), rs.getString("nome_cidade_cliente"));
            Bairro bairroC = new Bairro(rs.getLong("id_bairro_cliente"), rs.getString("nome_bairro_cliente"), cidadeAdm);
            Endereco enderecoC = new Endereco(rs.getLong("id_endereco_cliente"), bairroAdm, rs.getString("rua_cliente"), rs.getString("numero_cliente"));

            Cliente cliente = new Cliente(rs.getLong("id_cliente"), rs.getString("nome_cliente"), enderecoC,
                    rs.getString("cpf_cliente"), rs.getString("rg_cliente"), rs.getString("telefone_cliente"), null, true);

            FormaPagamento formaPagamento = new FormaPagamento(rs.getLong("id_forma_pagamento"), rs.getString("descricao_forma_pagamento"), 1);

            manuntencoes.add(new Manutencao(rs.getLong("id"), rs.getString("descricao"), cliente, adm, rs.getString("marca"), rs.getString("modelo"), rs.getString("imei"), rs.getString("cor"), rs.getLong("data_cadastro"), rs.getLong("data_previsao"), rs.getLong("data_entrega"), rs.getFloat("preco"), rs.getBoolean("finalizado"), formaPagamento));
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
            Cidade cidadeAdm = new Cidade(rs.getLong("id_cidade_administrador"), rs.getString("nome_cidadea_dministrador"));
            Bairro bairroAdm = new Bairro(rs.getLong("id_bairro_administrador"), rs.getString("nome_bairro_administrador"), cidadeAdm);
            Endereco enderecoAdm = new Endereco(rs.getLong("id_endereco_administrador"), bairroAdm, rs.getString("rua_administrador"), rs.getString("numero_administrador"));

            Administrador adm = new Administrador(rs.getLong("id_administrador"), rs.getString("nome_administrador"),
                    null, null, enderecoAdm, null, rs.getString("cpf_administrador"), null, null, true);

            Cidade cidadeC = new Cidade(rs.getLong("id_cidade_clienter"), rs.getString("nome_cidadea_cliente"));
            Bairro bairroC = new Bairro(rs.getLong("id_bairro_cliente"), rs.getString("nome_bairro_cliente"), cidadeAdm);
            Endereco enderecoC = new Endereco(rs.getLong("id_endereco_cliente"), bairroAdm, rs.getString("rua_cliente"), rs.getString("numero_cliente"));

            Cliente cliente = new Cliente(rs.getLong("id_cliente"), rs.getString("nome_cliente"), enderecoC,
                    rs.getString("cpf_cliente"), rs.getString("rg_cliente"), rs.getString("telefone_cliente"), null, true);

            FormaPagamento formaPagamento = new FormaPagamento(rs.getLong("id_forma_pagamento"), rs.getString("descricao_forma_pagamento"), 1);

            manuntencoes.add(new Manutencao(rs.getLong("id"), rs.getString("descricao"), cliente, adm, rs.getString("marca"), rs.getString("modelo"), rs.getString("imei"), rs.getString("cor"), rs.getLong("data_cadastro"), rs.getLong("data_previsao"), rs.getLong("data_entrega"), rs.getFloat("preco"), rs.getBoolean("finalizado"), formaPagamento));
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
            Cidade cidadeAdm = new Cidade(rs.getLong("id_cidade_administrador"), rs.getString("nome_cidadea_dministrador"));
            Bairro bairroAdm = new Bairro(rs.getLong("id_bairro_administrador"), rs.getString("nome_bairro_administrador"), cidadeAdm);
            Endereco enderecoAdm = new Endereco(rs.getLong("id_endereco_administrador"), bairroAdm, rs.getString("rua_administrador"), rs.getString("numero_administrador"));

            Administrador adm = new Administrador(rs.getLong("id_administrador"), rs.getString("nome_administrador"),
                    null, null, enderecoAdm, null, rs.getString("cpf_administrador"), null, null, true);

            Cidade cidadeC = new Cidade(rs.getLong("id_cidade_clienter"), rs.getString("nome_cidadea_cliente"));
            Bairro bairroC = new Bairro(rs.getLong("id_bairro_cliente"), rs.getString("nome_bairro_cliente"), cidadeAdm);
            Endereco enderecoC = new Endereco(rs.getLong("id_endereco_cliente"), bairroAdm, rs.getString("rua_cliente"), rs.getString("numero_cliente"));

            Cliente cliente = new Cliente(rs.getLong("id_cliente"), rs.getString("nome_cliente"), enderecoC,
                    rs.getString("cpf_cliente"), rs.getString("rg_cliente"), rs.getString("telefone_cliente"), null, true);

            FormaPagamento formaPagamento = new FormaPagamento(rs.getLong("id_forma_pagamento"), rs.getString("descricao_forma_pagamento"), 1);

            manuntencoes.add(new Manutencao(rs.getLong("id"), rs.getString("descricao"), cliente, adm, rs.getString("marca"), rs.getString("modelo"), rs.getString("imei"), rs.getString("cor"), rs.getLong("data_cadastro"), rs.getLong("data_previsao"), rs.getLong("data_entrega"), rs.getFloat("preco"), rs.getBoolean("finalizado"), formaPagamento));
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
            Cidade cidadeAdm = new Cidade(rs.getLong("id_cidade_administrador"), rs.getString("nome_cidadea_dministrador"));
            Bairro bairroAdm = new Bairro(rs.getLong("id_bairro_administrador"), rs.getString("nome_bairro_administrador"), cidadeAdm);
            Endereco enderecoAdm = new Endereco(rs.getLong("id_endereco_administrador"), bairroAdm, rs.getString("rua_administrador"), rs.getString("numero_administrador"));

            Administrador adm = new Administrador(rs.getLong("id_administrador"), rs.getString("nome_administrador"),
                    null, null, enderecoAdm, null, rs.getString("cpf_administrador"), null, null, true);

            Cidade cidadeC = new Cidade(rs.getLong("id_cidade_clienter"), rs.getString("nome_cidadea_cliente"));
            Bairro bairroC = new Bairro(rs.getLong("id_bairro_cliente"), rs.getString("nome_bairro_cliente"), cidadeAdm);
            Endereco enderecoC = new Endereco(rs.getLong("id_endereco_cliente"), bairroAdm, rs.getString("rua_cliente"), rs.getString("numero_cliente"));

            Cliente cliente = new Cliente(rs.getLong("id_cliente"), rs.getString("nome_cliente"), enderecoC,
                    rs.getString("cpf_cliente"), rs.getString("rg_cliente"), rs.getString("telefone_cliente"), null, true);

            FormaPagamento formaPagamento = new FormaPagamento(rs.getLong("id_forma_pagamento"), rs.getString("descricao_forma_pagamento"), 1);

            manuntencoes.add(new Manutencao(rs.getLong("id"), rs.getString("descricao"), cliente, adm, rs.getString("marca"), rs.getString("modelo"), rs.getString("imei"), rs.getString("cor"), rs.getLong("data_cadastro"), rs.getLong("data_previsao"), rs.getLong("data_entrega"), rs.getFloat("preco"), rs.getBoolean("finalizado"), formaPagamento));
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
            Administrador adm = new Administrador(rs.getLong("id_administrador"), rs.getString("nome_administrador"), null, null, null, null, null, null, null, true);
            Cliente cliente = new Cliente(rs.getLong("id_cliente"), rs.getString("nome_cliente"), null, null, null, null, null, true);

            FormaPagamento formaPagamento = new FormaPagamento(rs.getLong("id_forma_pagamento"), rs.getString("descricao_forma_pagamento"), 1);

            manuntencoes.add(new Manutencao(rs.getLong("id"), rs.getString("descricao"), cliente, adm, rs.getString("marca"), rs.getString("modelo"), rs.getString("imei"), rs.getString("cor"), rs.getLong("data_cadastro"), rs.getLong("data_previsao"), rs.getLong("data_entrega"), rs.getFloat("preco"), rs.getBoolean("finalizado"), formaPagamento));
        }

        stm.close();
        rs.close();

        return manuntencoes;
    }

}
