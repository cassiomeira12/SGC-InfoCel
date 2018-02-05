package banco.dao;

import banco.ControleDAO;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Marca;
import model.Produto;
import model.Venda;
import model.VendaProduto;

/**
 * DAO responsável pela ações realizadas na base de dados referentes as vendas
 * (Venda e VendaProduto)
 */
public class VendaDAO extends DAO {

    public VendaDAO() {
        super();
    }

    /**
     * Inserir venda na base de dados
     */
    public Long inserir(Venda venda) {
        Long idVenda = null;

        try {
            String sql = "INSERT INTO venda ( id_administrador, id_cliente, preco_total, forma_pagamento, data ) VALUES (?, ?, ?, ?, ?)";

            stm = getConector().prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

            stm.setInt(1, venda.getAdministrador().getId().intValue());
            stm.setInt(2, venda.getCliente().getId().intValue());
            stm.setFloat(3, venda.getPrecoTotal());
            stm.setInt(4, venda.getFormaPagamento());
            stm.setLong(5, System.currentTimeMillis());

            idVenda = super.inserir();

            //cadastrar vendaProduto
            sql = "INSERT INTO venda_produto ( id_produto, id_venda, quantidade, preco_total ) VALUES";
            for (VendaProduto vp : venda.getVendaProdutos()) {
                sql = sql + "\n(" + vp.getProduto().getId() + "," + idVenda + "," + vp.getQuantidade() + "," + vp.getPrecoTotal() + "),";
            }
            sql = sql.substring(0, sql.length() - 1);   //remove última vírgula
            stm = getConector().prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

            super.inserir();

            //atualiza estoque
            for (VendaProduto vp : venda.getVendaProdutos()) {
                Produto produto = vp.getProduto();
                produto.setEstoque(produto.getEstoque() - vp.getQuantidade());
                ControleDAO.getBanco().getProdutoDAO().editar(produto);
            }
        } catch (Exception ex) {
            chamarAlertaErro("Erro ao inserir venda na base de dados", ex.toString());
        }

        return idVenda;
    }

    /**
     * Atualizar dados marca na base de dados
     */
    private boolean editar(Marca marca) {
        try {
            String sql = "UPDATE marca SET descricao_marca =? WHERE id_marca =?";

            stm = getConector().prepareStatement(sql);

            stm.setString(1, marca.getDescricao());

            stm.setInt(2, marca.getId().intValue());

            stm.executeUpdate();
            stm.close();

        } catch (SQLException ex) {
            chamarAlertaErro("Erro ao atualizar marca na base de dados!", ex.toString());
            return false;
        }

        return true;
    }

    /**
     * Excluir marca na base de dados
     */
    private boolean excluir(int id) {
        try {
            String sql = "DELETE FROM marca WHERE id_marca=?";

            stm = getConector().prepareStatement(sql);

            stm.setInt(1, id);
            stm.execute();

            stm.close();
        } catch (SQLException ex) {
            chamarAlertaErro("Erro ao excluir marca na base de dados!", ex.toString());
            return false;
        }

        return true;
    }

    /**
     * Consultar todas marcas cadastradas na base de dados
     */
    private List<Marca> listar() {

        List<Marca> marcas = new ArrayList<>();

        try {
            String sql = "SELECT marca.* FROM marca";

            stm = getConector().prepareStatement(sql);
            rs = stm.executeQuery(sql);

            while (rs.next()) {
                Marca marca = new Marca((long) rs.getInt(1), rs.getString(2));

                marcas.add(marca);
            }

            stm.close();
            rs.close();

        } catch (SQLException ex) {
            chamarAlertaErro("Erro ao consultar marcas na base de dados!", ex.toString());
        }

        return marcas;
    }

    private Marca buscarPorId(int aInt) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
