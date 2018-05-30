package banco.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.FormaPagamento;

/**
 * DAO responsável pela ações realizadas na base de dados referentes as
 * categoria
 */
public class FormaPagamentoDAO extends DAO {

    public FormaPagamentoDAO() {
        super();
    }

    /**
     * Inserir categoria na base de dados
     */
    public Long inserir(FormaPagamento formaPagamento) throws Exception {
        String sql = "INSERT INTO forma_pagamento ( descricao, maximo_parcelas ) VALUES (?, ?)";

        stm = getConector().prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

        stm.setString(1, formaPagamento.getDescricao());
        stm.setInt(2, formaPagamento.getMaximoParcelas());

        return super.inserir();

    }

    /**
     * Atualizar dados categoria na base de dados
     */
    public boolean editar(FormaPagamento formaPagamento) throws SQLException {
        String sql = "UPDATE forma_pagamento SET descricao =?, maximo_parcelas WHERE id =?";

        stm = getConector().prepareStatement(sql);

        stm.setString(1, formaPagamento.getDescricao());
        stm.setInt(2, formaPagamento.getMaximoParcelas());

        stm.setInt(3, formaPagamento.getId().intValue());

        stm.executeUpdate();
        stm.close();

        return true;
    }

    /**
     * Excluir categoria na base de dados
     */
    public boolean excluir(int id) throws SQLException {
        String sql = "DELETE FROM forma_pagamento WHERE id=?";

        stm = getConector().prepareStatement(sql);

        stm.setInt(1, id);
        stm.execute();

        stm.close();

        return true;
    }

    /**
     * Consultar todas categoria cadastradas na base de dados
     */
    public List<FormaPagamento> listar() throws SQLException {

        List<FormaPagamento> formaPagamentos = new ArrayList<>();

        String sql = "SELECT forma_pagamento.* FROM forma_pagamento";

        stm = getConector().prepareStatement(sql);
        rs = stm.executeQuery(sql);

        while (rs.next()) {
            FormaPagamento formaPagamento = new FormaPagamento((long) rs.getInt(1), rs.getString("descricao"), rs.getInt("maximo_parcelas"));

            formaPagamentos.add(formaPagamento);
        }

        stm.close();
        rs.close();

        return formaPagamentos;
    }

    public List<FormaPagamento> buscarPorDescricao(String descricao) throws SQLException {

        List<FormaPagamento> formaPagamentos = new ArrayList<>();

        String sql = "SELECT forma_pagamento.* FROM forma_pagamento WHERE descricao LIKE '%" + descricao + "%'";

        stm = getConector().prepareStatement(sql);
        rs = stm.executeQuery(sql);

        while (rs.next()) {
            FormaPagamento formaPagamento = new FormaPagamento((long) rs.getInt(1), rs.getString(2), rs.getInt("maximo_parcelas"));

            formaPagamentos.add(formaPagamento);
        }

        stm.close();
        rs.close();

        return formaPagamentos;
    }

}
