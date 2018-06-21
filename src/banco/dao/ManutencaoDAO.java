package banco.dao;

import banco.ControleDAO;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
    public Long inserir(Manutencao m) throws Exception {
        ResultSet rs;
        PreparedStatement stm;

        if (m.getCliente().getId() == null) {
            Long id = ControleDAO.getBanco().getClienteDAO().inserir(m.getCliente());
            m.getCliente().setId(id);
        }

        String sql = "INSERT INTO manutencao (data_cadastro, data_previsao, data_entrega, id_cliente, id_administrador,id_forma_pagamento, descricao, preco, finalizado, marca, modelo, imei, cor, quantidade_parcelas) "
                + ""
                + "VALUES (" + m.getDataCadastro() + "," + m.getDataPrevisaoEntrega() + ", " + m.getDataEntrega() + ", ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        stm = getConector().prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

        /* stm.setLong(1, m.getDataCadastro());
        stm.setLong(2, m.getDataPrevisaoEntrega());
        stm.setLong(3, m.getDataEntrega());*/
        stm.setInt(1, m.getCliente().getId().intValue());
        stm.setInt(2, m.getAdministrador().getId().intValue());
        stm.setLong(3, m.getFormaPagamento().getId());
        stm.setString(4, m.getDescricao());
        stm.setFloat(5, m.getPreco());
        stm.setBoolean(6, m.isFinalizado());
        stm.setString(7, m.getMarca());
        stm.setString(8, m.getModelo());
        stm.setString(9, m.getImei());
        stm.setString(10, m.getCor());
        stm.setInt(11, m.getQuantidadeParcelas());

        return super.inserir(stm);

    }

    /**
     * Atualizar dados marca na base de dados
     */
    public boolean editar(Manutencao manutencao) throws SQLException {
        ResultSet rs;
        PreparedStatement stm;

        String sql = "UPDATE manutencao SET id_cliente =?, id_administrador =?, id_forma_pagamento = ?, descricao =?, data_previsao =" + manutencao.getDataPrevisaoEntrega() + ", data_entrega =" + manutencao.getDataEntrega() + ", preco =?, finalizado =?, marca =?, modelo =?, imei =?, cor =?, quantidade_parcelas =? WHERE id =?";

        stm = getConector().prepareStatement(sql);

        stm.setInt(1, manutencao.getCliente().getId().intValue());
        stm.setInt(2, manutencao.getAdministrador().getId().intValue());
        stm.setInt(3, manutencao.getFormaPagamento().getId().intValue());
        stm.setString(4, manutencao.getDescricao());
        stm.setFloat(5, manutencao.getPreco());
        stm.setBoolean(6, manutencao.isFinalizado());
        stm.setString(7, manutencao.getMarca());
        stm.setString(8, manutencao.getModelo());
        stm.setString(9, manutencao.getImei());
        stm.setString(10, manutencao.getCor());
        stm.setInt(11, manutencao.getQuantidadeParcelas());

        stm.setInt(12, manutencao.getId().intValue());

        stm.executeUpdate();
        stm.close();

        return true;
    }

    /**
     * Excluir manutencao na base de dados
     */
    public boolean excluir(int id) throws SQLException {
        ResultSet rs;
        PreparedStatement stm;

        String sql = "DELETE FROM manutencao WHERE id=?";

        stm = getConector().prepareStatement(sql);

        stm.setInt(1, id);
        stm.execute();

        stm.close();

        return true;
    }

    //Consultar todas Manutencao cadastradas na base de dados
    public List<Manutencao> listar() throws SQLException {
        ResultSet rs;
        PreparedStatement stm;

        List<Manutencao> manuntencoes = new ArrayList<>();

        String sql = "SELECT * FROM view_manutencao";

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

            manuntencoes.add(new Manutencao(rs.getLong("id"), rs.getString("descricao"), cliente, adm, rs.getString("marca"), rs.getString("modelo"), rs.getString("imei"), rs.getString("cor"), rs.getLong("data_cadastro"), rs.getLong("data_previsao"), rs.getLong("data_entrega"), rs.getFloat("preco"), rs.getBoolean("finalizado"), formaPagamento, rs.getInt("quantidade_parcelas")));
        }

        stm.close();
        rs.close();

        return manuntencoes;
    }

    public List<Manutencao> buscarPorCliente(Cliente c) throws SQLException {
        ResultSet rs;
        PreparedStatement stm;

        List<Manutencao> manuntencoes = new ArrayList<>();

        String sql = "SELECT * FROM view_manutencao WHERE id_cliente = " + c.getId();

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

            manuntencoes.add(new Manutencao(rs.getLong("id"), rs.getString("descricao"), cliente, adm, rs.getString("marca"), rs.getString("modelo"), rs.getString("imei"), rs.getString("cor"), rs.getLong("data_cadastro"), rs.getLong("data_previsao"), rs.getLong("data_entrega"), rs.getFloat("preco"), rs.getBoolean("finalizado"), formaPagamento, rs.getInt("quantidade_parcelas")));
        }

        stm.close();
        rs.close();

        return manuntencoes;
    }

    public Manutencao buscarPorId(Long id) throws SQLException {

        ResultSet rs;
        PreparedStatement stm;
        Manutencao manutencao = null;

        String sql = "SELECT * FROM view_manutencao WHERE id = " + id;

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

            manutencao = new Manutencao(rs.getLong("id"), rs.getString("descricao"), cliente, adm, rs.getString("marca"), rs.getString("modelo"), rs.getString("imei"), rs.getString("cor"), rs.getLong("data_cadastro"), rs.getLong("data_previsao"), rs.getLong("data_entrega"), rs.getFloat("preco"), rs.getBoolean("finalizado"), formaPagamento, rs.getInt("quantidade_parcelas"));
        }

        stm.close();
        rs.close();

        return manutencao;
    }

    public List<Manutencao> buscarFinalizadas() throws SQLException {
        ResultSet rs;
        PreparedStatement stm;

        List<Manutencao> manuntencoes = new ArrayList<>();

        String sql = "SELECT * FROM view_manutencao WHERE finalizado = true";

        stm = getConector().prepareStatement(sql);
        rs = stm.executeQuery(sql);

        while (rs.next()) {
            Cidade cidadeAdm = new Cidade(rs.getLong("id_cidade_administrador"), rs.getString("nome_cidade_administrador"));
            Bairro bairroAdm = new Bairro(rs.getLong("id_bairro_administrador"), rs.getString("nome_bairro_administrador"), cidadeAdm);
            Endereco enderecoAdm = new Endereco(rs.getLong("id_endereco_administrador"), bairroAdm, rs.getString("rua_administrador"), rs.getString("numero_administrador"));

            Administrador adm = new Administrador(rs.getLong("id_administrador"), rs.getString("nome_administrador"),
                    null, null, enderecoAdm, null, rs.getString("cpf_administrador"), null, null, true);

            Cidade cidadeC = new Cidade(rs.getLong("id_cidade_clienter"), rs.getString("nome_cidade_cliente"));
            Bairro bairroC = new Bairro(rs.getLong("id_bairro_cliente"), rs.getString("nome_bairro_cliente"), cidadeAdm);
            Endereco enderecoC = new Endereco(rs.getLong("id_endereco_cliente"), bairroAdm, rs.getString("rua_cliente"), rs.getString("numero_cliente"));

            Cliente cliente = new Cliente(rs.getLong("id_cliente"), rs.getString("nome_cliente"), enderecoC,
                    rs.getString("cpf_cliente"), rs.getString("rg_cliente"), rs.getString("telefone_cliente"), null, true);

            FormaPagamento formaPagamento = new FormaPagamento(rs.getLong("id_forma_pagamento"), rs.getString("descricao_forma_pagamento"), 1);

            manuntencoes.add(new Manutencao(rs.getLong("id"), rs.getString("descricao"), cliente, adm, rs.getString("marca"), rs.getString("modelo"), rs.getString("imei"), rs.getString("cor"), rs.getLong("data_cadastro"), rs.getLong("data_previsao"), rs.getLong("data_entrega"), rs.getFloat("preco"), rs.getBoolean("finalizado"), formaPagamento, rs.getInt("quantidade_parcelas")));
        }

        stm.close();
        rs.close();

        return manuntencoes;
    }

    public List<Manutencao> buscarPendentes() throws SQLException {
        ResultSet rs;
        PreparedStatement stm;

        List<Manutencao> manuntencoes = new ArrayList<>();

        String sql = "SELECT * FROM view_manutencao WHERE finalizado = false";

        stm = getConector().prepareStatement(sql);
        rs = stm.executeQuery(sql);

        while (rs.next()) {
            Cidade cidadeAdm = new Cidade(rs.getLong("id_cidade_administrador"), rs.getString("nome_cidade_administrador"));
            Bairro bairroAdm = new Bairro(rs.getLong("id_bairro_administrador"), rs.getString("nome_bairro_administrador"), cidadeAdm);
            Endereco enderecoAdm = new Endereco(rs.getLong("id_endereco_administrador"), bairroAdm, rs.getString("rua_administrador"), rs.getString("numero_administrador"));

            Administrador adm = new Administrador(rs.getLong("id_administrador"), rs.getString("nome_administrador"),
                    null, null, enderecoAdm, null, rs.getString("cpf_administrador"), null, null, true);

            Cidade cidadeC = new Cidade(rs.getLong("id_cidade_clienter"), rs.getString("nome_cidade_cliente"));
            Bairro bairroC = new Bairro(rs.getLong("id_bairro_cliente"), rs.getString("nome_bairro_cliente"), cidadeAdm);
            Endereco enderecoC = new Endereco(rs.getLong("id_endereco_cliente"), bairroAdm, rs.getString("rua_cliente"), rs.getString("numero_cliente"));

            Cliente cliente = new Cliente(rs.getLong("id_cliente"), rs.getString("nome_cliente"), enderecoC,
                    rs.getString("cpf_cliente"), rs.getString("rg_cliente"), rs.getString("telefone_cliente"), null, true);

            FormaPagamento formaPagamento = new FormaPagamento(rs.getLong("id_forma_pagamento"), rs.getString("descricao_forma_pagamento"), 1);

            manuntencoes.add(new Manutencao(rs.getLong("id"), rs.getString("descricao"), cliente, adm, rs.getString("marca"), rs.getString("modelo"), rs.getString("imei"), rs.getString("cor"), rs.getLong("data_cadastro"), rs.getLong("data_previsao"), rs.getLong("data_entrega"), rs.getFloat("preco"), rs.getBoolean("finalizado"), formaPagamento, rs.getInt("quantidade_parcelas")));
        }

        stm.close();
        rs.close();

        return manuntencoes;
    }

    //datas no formato dd/MM/yyyy
    public List<Manutencao> buscarPorIntervalo(String dataInicio, String dataFinal) throws SQLException {
        ResultSet rs;
        PreparedStatement stm;

        List<Manutencao> manuntencoes = new ArrayList<>();

        Long inicio = DateUtils.getLongFromDate(dataInicio);
        Long finall = DateUtils.getLongFromDate(dataFinal);

        String sql = "SELECT * FROM view_manutencao"
                + "\nWHERE view_manutencao.data_cadastro >= " + inicio + " AND view_manutencao.data_cadastro < " + finall;

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

            manuntencoes.add(new Manutencao(rs.getLong("id"), rs.getString("descricao"), cliente, adm, rs.getString("marca"), rs.getString("modelo"), rs.getString("imei"), rs.getString("cor"), rs.getLong("data_cadastro"), rs.getLong("data_previsao"), rs.getLong("data_entrega"), rs.getFloat("preco"), rs.getBoolean("finalizado"), formaPagamento, rs.getInt("quantidade_parcelas")));
        }

        stm.close();
        rs.close();

        return manuntencoes;
    }

}
