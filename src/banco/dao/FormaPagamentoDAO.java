package banco.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.CategoriaProduto;
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
    public Long inserir(FormaPagamento formaPagamento) {
        try {
            String sql = "INSERT INTO forma_pagamento ( descricao_forma_pagamento, parcelas ) VALUES (?, ?)";

            stm = getConector().prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

            stm.setString(1, formaPagamento.getDescricao());
            stm.setInt(1, formaPagamento.getParcelas());

            return super.inserir();
        } catch (Exception ex) {
            chamarAlertaErro("Erro ao inserir categoria na base de dados", ex.toString());
        }

        return null;
    }

    /**
     * Atualizar dados categoria na base de dados
     */
    public boolean editar(FormaPagamento formaPagamento) {
        try {
            String sql = "UPDATE forma_pagamento SET descricao_forma_pagamento =?, parcelas =? WHERE id_forma_pagamento =?";

            stm = getConector().prepareStatement(sql);

            stm.setString(1, formaPagamento.getDescricao());
            stm.setInt(2, formaPagamento.getParcelas());

            stm.setInt(3, formaPagamento.getId().intValue());

            stm.executeUpdate();
            stm.close();

        } catch (SQLException ex) {
            chamarAlertaErro("Erro ao atualizar categoria na base de dados!", ex.toString());
            return false;
        }

        return true;
    }

    /**
     * Excluir categoria na base de dados
     */
    public boolean excluir(int id) {
        try {
            String sql = "DELETE FROM forma_pagamento WHERE id_forma_pagamento=?";

            stm = getConector().prepareStatement(sql);

            stm.setInt(1, id);
            stm.execute();

            stm.close();
        } catch (SQLException ex) {
            chamarAlertaErro("Erro ao excluir forma_pagamento na base de dados!", ex.toString());
            return false;
        }

        return true;
    }

    /**
     * Consultar todas categoria cadastradas na base de dados
     */
    public List<FormaPagamento> listar() {

        List<FormaPagamento> formaPagamentos = new ArrayList<>();

        try {
            String sql = "SELECT forma_pagamento.* FROM forma_pagamento";

            stm = getConector().prepareStatement(sql);
            rs = stm.executeQuery(sql);

            while (rs.next()) {
                FormaPagamento formaPagamento = new FormaPagamento((long) rs.getInt(1), rs.getString("descricao_forma_pagamento"), rs.getInt("parcelas"));

                formaPagamentos.add(formaPagamento);
            }

            stm.close();
            rs.close();

        } catch (SQLException ex) {
            chamarAlertaErro("Erro ao consultar categoria na base de dados!", ex.toString());
        }

        return formaPagamentos;
    }

    public List<FormaPagamento> buscarPorDescricao(String descricao) {

        List<FormaPagamento> formaPagamentos = new ArrayList<>();

        try {
            String sql = "SELECT forma_pagamento.* FROM forma_pagamento WHERE descricao_forma_pagamento LIKE '%" + descricao + "%'";

            stm = getConector().prepareStatement(sql);
            rs = stm.executeQuery(sql);

            while (rs.next()) {
                FormaPagamento formaPagamento = new FormaPagamento((long) rs.getInt(1), rs.getString(2), rs.getInt(3));

                formaPagamentos.add(formaPagamento);
            }

            stm.close();
            rs.close();

        } catch (SQLException ex) {
            chamarAlertaErro("Erro ao consultar categoria na base de dados!", ex.toString());
        }

        return formaPagamentos;
    }

}
