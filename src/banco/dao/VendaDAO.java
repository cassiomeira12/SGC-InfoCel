package banco.dao;

import banco.ControleDAO;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Administrador;
import model.CategoriaProduto;
import model.Celular;
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
            stm.setLong(5, venda.getData());

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
    public List<Venda> listar() {

        List<Venda> vendas = new ArrayList<>();

        try {
            String sql = "SELECT venda.*, cliente.*, administrador.* "
                    + "FROM venda"
                    + "\nINNER JOIN cliente cliente ON venda.id_cliente = cliente.id_cliente"
                    + "\nINNER JOIN administrador administrador ON venda.id_administrador = administrador.id_administrador";

            stm = getConector().prepareStatement(sql);
            rs = stm.executeQuery(sql);

            while (rs.next()) {
                Administrador adm = new Administrador(rs.getLong("id_administrador"), rs.getString("nome_administrador"), "", "", rs.getString("endereco_administrador"), rs.getString("email_administrador"), rs.getString("cpf_administrador"), rs.getString("rg_administrador"), null, rs.getInt("status_administrador"));
                Cliente cliente = new Cliente(rs.getLong("id_cliente"), rs.getString("nome_cliente"), rs.getString("endereco_cliente"), rs.getString("cpf_cliente"), rs.getString("rg_cliente"), rs.getString("telefone_cliente"), rs.getString("cidade_cliente"), null, rs.getInt("status_cliente"));
                Venda venda = new Venda(rs.getLong("id_venda"), adm, cliente, null, rs.getInt("forma_pagamento"), rs.getLong("data"));
                venda.setPrecoTotal(rs.getFloat("preco_total"));

                vendas.add(venda);
            }

            stm.close();
            rs.close();

        } catch (SQLException ex) {
            chamarAlertaErro("Erro ao consultar marcas na base de dados!", ex.toString());
        }

        return vendas;
    }

    public List<VendaProduto> buscarVendaProduto(Venda venda) {

        List<VendaProduto> vendaProdutos = new ArrayList<>();

        try {
            String sql = "SELECT venda_produto.*, produto.*, marca.*, categoria_produto.*"
                    + "FROM venda_produto"
                    + "\nINNER JOIN produto produto ON venda_produto.id_produto = venda_produto.id_produto"
                    + "\nINNER JOIN categoria_produto categoria_produto ON produto.id_categoria = categoria_produto.id_categoria"
                    + "\nINNER JOIN marca ON produto.id_marca = marca.id_marca";

            stm = getConector().prepareStatement(sql);
            rs = stm.executeQuery(sql);

            while (rs.next()) {
                Produto produto = new Produto(rs.getLong("id_produto"), new Marca(null, rs.getString("descricao_marca")), rs.getString("descricao_produto"), new CategoriaProduto(null, rs.getString("descricao_categoria")), 0, 0, 0);
                produto.setPrecoVenda(rs.getFloat("preco_total") / rs.getFloat("quantidade"));

                vendaProdutos.add(new VendaProduto(rs.getFloat("quantidade"), venda, produto));
            }

            stm.close();
            rs.close();

        } catch (SQLException ex) {
            chamarAlertaErro("Erro ao consultar marcas na base de dados!", ex.toString());
        }

        return vendaProdutos;
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
