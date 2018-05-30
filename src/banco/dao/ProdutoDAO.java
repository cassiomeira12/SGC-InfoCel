package banco.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.CategoriaProduto;
import model.Marca;
import model.Produto;
import model.UnidadeMedida;

/**
 * DAO responsável pela ações realizadas na base de dados referentes aos
 * produtos
 */
public class ProdutoDAO extends DAO {

    public ProdutoDAO() {
        super();
    }

    /**
     * Inserir produto na base de dados
     */
    public Long inserir(Produto produto) throws Exception {
        String sql = "INSERT INTO produto ( descricao, id_categoria_produto, id_marca, preco_compra, preco_venda, estoque, id_unidade_medida ) VALUES (?, ?, ?, ?,?, ?, ?)";

        stm = getConector().prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

        stm.setString(1, produto.getDescricao());
        stm.setInt(2, produto.getCategoria().getId().intValue());
        stm.setInt(3, produto.getMarca().getId().intValue());
        stm.setFloat(4, produto.getPrecoCompra());
        stm.setFloat(5, produto.getPrecoVenda());
        stm.setFloat(6, produto.getEstoque());
        stm.setInt(7, produto.getUnidadeMedida().getId().intValue());

        return super.inserir();
    }

    /**
     * Atualizar dados produto na base de dados
     */
    public boolean editar(Produto produto) throws SQLException {
        String sql = "UPDATE produto SET  descricao =?, id_categoria_produto =?, id_marca =?, preco_compra =?, preco_venda =?, estoque =?, id_unidade_medida WHERE id =?";

        stm = getConector().prepareStatement(sql);

        stm.setString(1, produto.getDescricao());
        stm.setInt(2, produto.getCategoria().getId().intValue());
        stm.setInt(3, produto.getMarca().getId().intValue());
        stm.setFloat(4, produto.getPrecoCompra());
        stm.setFloat(5, produto.getPrecoVenda());
        stm.setFloat(6, produto.getEstoque());
        stm.setInt(7, produto.getUnidadeMedida().getId().intValue());

        stm.setInt(7, produto.getId().intValue());

        stm.executeUpdate();
        stm.close();

        return true;
    }

    /**
     * Excluir produto na base de dados
     */
    public boolean excluir(int id) throws SQLException {
        String sql = "DELETE FROM produto WHERE id=?";

        stm = getConector().prepareStatement(sql);

        stm.setInt(1, id);
        stm.execute();

        stm.close();
        return true;
    }

    /**
     * Consultar todos produtos cadastrados na base de dados
     */
    public List<Produto> listar() throws SQLException {

        List<Produto> produtos = new ArrayList<>();

        String sql = "SELECT * FROM view_produto";

        stm = getConector().prepareStatement(sql);
        rs = stm.executeQuery(sql);

        while (rs.next()) {
            CategoriaProduto categoria = new CategoriaProduto(null, rs.getString("descricao_categoria"));
            Marca marca = new Marca(null, rs.getString("descricao_marca"));
            UnidadeMedida unidadeMedida = new UnidadeMedida(null, rs.getString("descricao_unidade"), null);

            Produto produto = new Produto(rs.getLong("id"), marca, rs.getString("descricao"), categoria, rs.getFloat("preco_compra"), rs.getFloat("preco_venda"), rs.getFloat("estoque"), unidadeMedida);
            produtos.add(produto);
        }

        stm.close();
        rs.close();

        return produtos;
    }

    public List<Produto> buscarPorDescricaoModelo(String busca) throws SQLException {
        List<Produto> produtos = new ArrayList<>();

        String sql = "SELECT * FROM view_produto"
                + "\nWHERE produto.descricao LIKE '%" + busca + "%'"
                + "\n OR produto.modelo LIKE '%" + busca + "%'";;

        stm = getConector().prepareStatement(sql);
        rs = stm.executeQuery(sql);

        while (rs.next()) {
            CategoriaProduto categoria = new CategoriaProduto(null, rs.getString("descricao_categoria"));
            Marca marca = new Marca(null, rs.getString("descricao_marca"));
            UnidadeMedida unidadeMedida = new UnidadeMedida(null, rs.getString("descricao_unidade"), null);

            Produto produto = new Produto(rs.getLong("id"), marca, rs.getString("descricao"), categoria, rs.getFloat("preco_compra"), rs.getFloat("preco_venda"), rs.getFloat("estoque"), unidadeMedida);
            produtos.add(produto);
        }

        stm.close();
        rs.close();

        return produtos;
    }

    public List<Produto> buscarPorCategoria(CategoriaProduto categoria) throws SQLException {
        List<Produto> produtos = new ArrayList<>();

        String sql = "SELECT * FROM view_produto"
                + "\nWHERE produto.id_categoria_produto = " + categoria.getId();

        stm = getConector().prepareStatement(sql);
        rs = stm.executeQuery(sql);

        while (rs.next()) {
            Marca marca = new Marca(null, rs.getString("descricao_marca"));
            UnidadeMedida unidadeMedida = new UnidadeMedida(null, rs.getString("descricao_unidade"), null);

            Produto produto = new Produto(rs.getLong("id"), marca, rs.getString("descricao"), categoria, rs.getFloat("preco_compra"), rs.getFloat("preco_venda"), rs.getFloat("estoque"), unidadeMedida);
            produtos.add(produto);
        }

        stm.close();
        rs.close();

        return produtos;
    }

    public Produto buscarPorId(Long id) throws SQLException {
        Produto produto = null;

        String sql = "SELECT * FROM view_produto WHERE id = " + id;

        stm = getConector().prepareStatement(sql);
        rs = stm.executeQuery(sql);

        while (rs.next()) {
            CategoriaProduto categoria = new CategoriaProduto(null, rs.getString("descricao_categoria"));
            Marca marca = new Marca(null, rs.getString("descricao_marca"));
            UnidadeMedida unidadeMedida = new UnidadeMedida(null, rs.getString("descricao_unidade"), null);

            produto = new Produto(rs.getLong("id"), marca, rs.getString("descricao"), categoria, rs.getFloat("preco_compra"), rs.getFloat("preco_venda"), rs.getFloat("estoque"), unidadeMedida);
        }

        stm.close();
        rs.close();

        return produto;
    }

}
