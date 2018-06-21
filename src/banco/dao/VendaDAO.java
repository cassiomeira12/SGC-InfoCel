package banco.dao;

import banco.ControleDAO;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Administrador;
import model.Bairro;
import model.CategoriaProduto;
import model.Cidade;
import model.Cliente;
import model.Endereco;
import model.FormaPagamento;
import model.Marca;
import model.Produto;
import model.UnidadeMedida;
import model.Venda;
import model.VendaProduto;
import util.DateUtils;

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
     *
     * @param venda
     * @return
     */
    public Long inserir(Venda venda) throws Exception {
        ResultSet rs;
        PreparedStatement stm;

        Long idVenda;
        Long idCliente = venda.getCliente().getId();

        if (idCliente == null) {//null = cliente não cadastrado
            idCliente = ControleDAO.getBanco().getClienteDAO().inserir(venda.getCliente());
        }

        String sql = "INSERT INTO venda ( id_administrador, id_cliente, preco_total, id_forma_pagamento, quantidade_parcela, data ) VALUES (?, ?, ?, ?, ?, ?)";

        stm = getConector().prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

        stm.setInt(1, venda.getAdministrador().getId().intValue());
        stm.setInt(2, idCliente.intValue());
        stm.setFloat(3, venda.getPrecoTotal());
        stm.setInt(4, venda.getFormaPagamento().getId().intValue());
        stm.setInt(5, venda.getQuantidadeParcelas());
        stm.setLong(6, venda.getData());
        idVenda = super.inserir(stm);

        //cadastrar vendaProduto
        sql = "INSERT INTO venda_produto ( id_produto, id_venda, quantidade, preco_total ) VALUES";
        for (VendaProduto vp : venda.getVendaProdutos()) {
            sql = sql + "\n(" + vp.getProduto().getId() + "," + idVenda + "," + vp.getQuantidade() + "," + vp.getPrecoTotal() + "),";
        }
        sql = sql.substring(0, sql.length() - 1);   //remove última vírgula
        stm = getConector().prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

        //inserindo vendas produtos
        stm.execute();
        stm.close();

        //atualiza estoque
        for (VendaProduto vp : venda.getVendaProdutos()) {
            Produto produto = ControleDAO.getBanco().getProdutoDAO().buscarPorId(vp.getProduto().getId());
            produto.setEstoque(produto.getEstoque() - vp.getQuantidade());
            ControleDAO.getBanco().getProdutoDAO().editar(produto);
        }

        return idVenda;
    }

    /**
     * Excluir venda na base de dados
     */
    public boolean excluir(Long id) throws SQLException {
        ResultSet rs;
        PreparedStatement stm;
        
        excluirVendasProdutosDaVenda(id);

        String sql = "DELETE FROM venda WHERE id=?";

        stm = getConector().prepareStatement(sql);

        stm.setInt(1, id.intValue());
        stm.execute();

        stm.close();
        
        return true;
    }

    // Consultar todas vendas cadastradas na base de dados
    public List<Venda> listar() throws SQLException {
        ResultSet rs;
        PreparedStatement stm;

        List<Venda> vendas = new ArrayList<>();

        String sql = "SELECT * FROM view_venda";

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

            Venda venda = new Venda(rs.getLong("id"), adm, cliente, null, formaPagamento, rs.getInt("quantidade_parcela"), rs.getLong("data"));
            venda.setPrecoTotal(rs.getFloat("preco_total"));

            vendas.add(venda);
        }

        stm.close();
        rs.close();

        return vendas;
    }

    public List<Venda> buscarPorIntervalo(String dataInicio, String dataFinal) throws SQLException {
        ResultSet rs;
        PreparedStatement stm;

        Long inicio = DateUtils.getLongFromDate(dataInicio);
        Long finall = DateUtils.getLongFromDate(dataFinal);

        List<Venda> vendas = new ArrayList<>();

        String sql = "SELECT * FROM view_venda"
                + "\nWHERE view_venda.data >= " + inicio + " AND view_venda.data < " + finall;

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

            Venda venda = new Venda(rs.getLong("id"), adm, cliente, null, formaPagamento, rs.getInt("quantidade_parcela"), rs.getLong("data"));
            venda.setPrecoTotal(rs.getFloat("preco_total"));

            vendas.add(venda);
        }

        stm.close();
        rs.close();

        return vendas;
    }

    public Venda buscarPorId(Long id) throws SQLException {
        ResultSet rs;
        PreparedStatement stm;

        Venda venda = null;

        String sql = "SELECT * FROM view_venda WHERE id = " + id;

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

            venda = new Venda(rs.getLong("id"), adm, cliente, null, formaPagamento, rs.getInt("quantidade_parcela"), rs.getLong("data"));
            venda.setPrecoTotal(rs.getFloat("preco_total"));
        }

        stm.close();
        rs.close();

        return venda;
    }

    public List<VendaProduto> buscarVendaProduto(Venda venda) throws SQLException {
        ResultSet rs;
        PreparedStatement stm;

        List<VendaProduto> vendaProdutos = new ArrayList<>();

        String sql = "SELECT * FROM view_venda_produto WHERE id_venda = " + venda.getId();

        stm = getConector().prepareStatement(sql);
        rs = stm.executeQuery(sql);

        while (rs.next()) {
            CategoriaProduto categoria = new CategoriaProduto(null, rs.getString("descricao_categoria"));
            Marca marca = new Marca(null, rs.getString("descricao_marca"));
            UnidadeMedida unidadeMedida = new UnidadeMedida(null, null, rs.getString("abreviacao"));

            Produto produto = (new Produto(rs.getLong("id_produto"), marca, rs.getString("descricao_produto"), categoria, 0, rs.getFloat("preco_venda"), 0, unidadeMedida));
            produto.setPrecoVenda(rs.getFloat("preco_total") / rs.getFloat("quantidade"));

            vendaProdutos.add(new VendaProduto(rs.getFloat("quantidade"), venda, produto));
        }

        stm.close();
        rs.close();

        return vendaProdutos;
    }

    public List<Venda> buscarPorCliente(Cliente cliente) throws SQLException {
        ResultSet rs;
        PreparedStatement stm;

        List<Venda> vendas = new ArrayList<>();

        String sql = "SELECT * FROM view_venda WHERE id_cliente = " + cliente.getId();

        stm = getConector().prepareStatement(sql);
        rs = stm.executeQuery(sql);

        while (rs.next()) {
            Cidade cidadeAdm = new Cidade(rs.getLong("id_cidade_administrador"), rs.getString("nome_cidade_administrador"));
            Bairro bairroAdm = new Bairro(rs.getLong("id_bairro_administrador"), rs.getString("nome_bairro_administrador"), cidadeAdm);
            Endereco enderecoAdm = new Endereco(rs.getLong("id_endereco_administrador"), bairroAdm, rs.getString("rua_administrador"), rs.getString("numero_administrador"));

            Administrador adm = new Administrador(rs.getLong("id_administrador"), rs.getString("nome_administrador"),
                    null, null, enderecoAdm, null, rs.getString("cpf_administrador"), null, null, true);

            FormaPagamento formaPagamento = new FormaPagamento(rs.getLong("id_forma_pagamento"), rs.getString("descricao_forma_pagamento"), 1);

            Venda venda = new Venda(rs.getLong("id"), adm, cliente, null, formaPagamento, rs.getInt("quantidade_parcela"), rs.getLong("data"));
            venda.setPrecoTotal(rs.getFloat("preco_total"));

            vendas.add(venda);
        }

        stm.close();
        rs.close();

        return vendas;
    }

    public void excluirVendasProdutosDaVenda(Long id) throws SQLException {
        ResultSet rs;
        PreparedStatement stm;

        String sql = "DELETE FROM venda_produto WHERE id_venda=?";

        stm = getConector().prepareStatement(sql);

        stm.setInt(1, id.intValue());
        stm.execute();

        stm.close();
    }

}
