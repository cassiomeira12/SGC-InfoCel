package banco.dao;

import banco.ControleDAO;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Cliente;
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
        Long idCliente = venda.getCliente().getId();

        try {
            if (idCliente == null) //null = cliente não cadastrado
            {
                idCliente = ControleDAO.getBanco().getClienteDAO().inserir(venda.getCliente());
            }

            String sql = "INSERT INTO venda ( id_administrador, id_cliente, preco_total, forma_pagamento, data ) VALUES (?, ?, ?, ?, ?)";

            stm = getConector().prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

            stm.setInt(1, venda.getAdministrador().getId().intValue());
            stm.setInt(2, idCliente.intValue());
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
                Produto produto = ControleDAO.getBanco().getProdutoDAO().buscarPorId(vp.getProduto().getId());
                produto.setEstoque(produto.getEstoque() - vp.getQuantidade());
                ControleDAO.getBanco().getProdutoDAO().editar(produto);
            }
        } catch (Exception ex) {
            chamarAlertaErro("Erro ao inserir venda na base de dados", ex.toString());
        }

        return idVenda;
    }

    /**
     * Excluir venda na base de dados
     */
    private boolean excluir(Long id) {
        try {
            String sql = "DELETE FROM venda WHERE id_venda=?";

            stm = getConector().prepareStatement(sql);

            stm.setInt(1, id.intValue());
            stm.execute();

            stm.close();

            excluirVendasProdutosDaVenda(id);
        } catch (Exception ex) {
            chamarAlertaErro("Erro ao excluir venda na base de dados!", ex.toString());
            return false;
        }

        return true;
    }

    /**
     * Consultar todas vendas cadastradas na base de dados
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

    private List<Marca> buscarPorCliente(Cliente cliente) {

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

    private void excluirVendasProdutosDaVenda(Long id) throws SQLException {
        String sql = "DELETE FROM venda_produto WHERE id_venda=?";

        stm = getConector().prepareStatement(sql);

        stm.setInt(1, id.intValue());
        stm.execute();

        stm.close();
    }

}
