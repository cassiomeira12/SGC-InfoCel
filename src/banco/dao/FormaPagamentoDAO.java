package banco.dao;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
        ResultSet rs;
        PreparedStatement stm;

        String sql = "INSERT INTO forma_pagamento ( descricao, maximo_parcelas, status) VALUES (?, ?, ?)";

        stm = getConector().prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

        stm.setString(1, formaPagamento.getDescricao());
        stm.setInt(2, formaPagamento.getMaximoParcelas());
        stm.setBoolean(3, formaPagamento.getStatus());

        return super.inserir(stm);

    }

    /**
     * Atualizar dados categoria na base de dados
     */
    public boolean editar(FormaPagamento fm) throws SQLException {
        ResultSet rs;
        PreparedStatement stm;

        String sql = "UPDATE forma_pagamento SET descricao =?, maximo_parcelas =?, status =? WHERE id =?";
        stm = getConector().prepareStatement(sql);

        stm.setString(1, fm.getDescricao());
        stm.setInt(2, fm.getMaximoParcelas());
        stm.setBoolean(3, fm.getStatus());

        stm.setInt(4, fm.getId().intValue());

        stm.executeUpdate();
        stm.close();

        return true;
    }

    /**
     * Excluir categoria na base de dados
     */
    public boolean excluir(FormaPagamento fp) throws SQLException {
        ResultSet rs;
        PreparedStatement stm;

        try {
            String sql = "DELETE FROM forma_pagamento WHERE id=?";

            stm = getConector().prepareStatement(sql);

            stm.setInt(1, fp.getId().intValue());
            stm.execute();

            stm.close();
        } catch (MySQLIntegrityConstraintViolationException e) {
            fp.setStatus(false);
            editar(fp);
        }

        return true;
    }

    /**
     * Consultar todas categoria cadastradas na base de dados
     */
    public List<FormaPagamento> listar() throws SQLException {
        ResultSet rs;
        PreparedStatement stm;

        List<FormaPagamento> formaPagamentos = new ArrayList<>();

        String sql = "SELECT forma_pagamento.* FROM forma_pagamento WHERE status =1";

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
        ResultSet rs;
        PreparedStatement stm;

        List<FormaPagamento> formaPagamentos = new ArrayList<>();

        String sql = "SELECT forma_pagamento.* FROM forma_pagamento WHERE descricao LIKE '%" + descricao + "%' AND status =1";

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
