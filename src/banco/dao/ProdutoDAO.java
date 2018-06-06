package banco.dao;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
        ResultSet rs;
        PreparedStatement stm;

        String sql = "INSERT INTO produto ( descricao, id_categoria_produto, id_marca, preco_compra, preco_venda, estoque, id_unidade_medida, status ) VALUES (?, ?, ?, ?, ?,?, ?, ?)";

        stm = getConector().prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

        stm.setString(1, produto.getDescricao());
        stm.setInt(2, produto.getCategoria().getId().intValue());
        stm.setInt(3, produto.getMarca().getId().intValue());
        stm.setFloat(4, produto.getPrecoCompra());
        stm.setFloat(5, produto.getPrecoVenda());
        stm.setFloat(6, produto.getEstoque());
        stm.setInt(7, produto.getUnidadeMedida().getId().intValue());
        stm.setBoolean(8, produto.getStatus());

        return super.inserir(stm);
    }

    /**
     * Atualizar dados produto na base de dados
     */
    public boolean editar(Produto produto) throws SQLException {
        ResultSet rs;
        PreparedStatement stm;

        String sql = "UPDATE produto SET  descricao =?, id_categoria_produto =?, id_marca =?, preco_compra =?, preco_venda =?, estoque =?, id_unidade_medida =?, status =? WHERE id =?";

        stm = getConector().prepareStatement(sql);

        stm.setString(1, produto.getDescricao());
        stm.setInt(2, produto.getCategoria().getId().intValue());
        stm.setInt(3, produto.getMarca().getId().intValue());
        stm.setFloat(4, produto.getPrecoCompra());
        stm.setFloat(5, produto.getPrecoVenda());
        stm.setFloat(6, produto.getEstoque());
        stm.setInt(7, produto.getUnidadeMedida().getId().intValue());
        stm.setBoolean(8, produto.getStatus());

        stm.setInt(9, produto.getId().intValue());

        stm.executeUpdate();
        stm.close();

        return true;
    }

    /**
     * Excluir produto na base de dados
     */
    public boolean excluir(Produto produto) throws SQLException {
        ResultSet rs;
        PreparedStatement stm;

        try {
            String sql = "DELETE FROM produto WHERE id=?";

            stm = getConector().prepareStatement(sql);

            stm.setInt(1, produto.getId().intValue());
            stm.execute();

            stm.close();
        } catch (MySQLIntegrityConstraintViolationException e) {
            produto.setStatus(false);
            editar(produto);
        }

        return true;
    }

    /**
     * Consultar todos produtos cadastrados na base de dados
     */
    public List<Produto> listar() throws SQLException {
        ResultSet rs;
        PreparedStatement stm;

        List<Produto> produtos = new ArrayList<>();

        String sql = "SELECT * FROM view_produto WHERE status = 1";

        stm = getConector().prepareStatement(sql);
        rs = stm.executeQuery(sql);

        while (rs.next()) {
            CategoriaProduto categoria = new CategoriaProduto(rs.getLong("id_categoria_produto"), rs.getString("descricao_categoria"));
            Marca marca = new Marca(rs.getLong("id_marca"), rs.getString("descricao_marca"));
            UnidadeMedida unidadeMedida = new UnidadeMedida(rs.getLong("id_unidade_medida"), rs.getString("descricao_unidade"), rs.getString("abreviacao_unidade"));

            Produto produto = new Produto(rs.getLong("id"), marca, rs.getString("descricao"), categoria, rs.getFloat("preco_compra"), rs.getFloat("preco_venda"), rs.getFloat("estoque"), unidadeMedida);
            produtos.add(produto);
        }

        stm.close();
        rs.close();

        return produtos;
    }

    public List<Produto> buscarPorDescricaoModelo(String busca) throws SQLException {
        ResultSet rs;
        PreparedStatement stm;

        List<Produto> produtos = new ArrayList<>();

        String sql = "SELECT * FROM view_produto"
                + "\nWHERE produto.descricao LIKE '%" + busca + "%'"
                + "\n OR produto.modelo LIKE '%" + busca + "%' AND status = 1";

        stm = getConector().prepareStatement(sql);
        rs = stm.executeQuery(sql);

        while (rs.next()) {
            CategoriaProduto categoria = new CategoriaProduto(rs.getLong("id_categoria_produto"), rs.getString("descricao_categoria"));
            Marca marca = new Marca(rs.getLong("id_marca"), rs.getString("descricao_marca"));
            UnidadeMedida unidadeMedida = new UnidadeMedida(rs.getLong("id_unidade_medida"), rs.getString("descricao_unidade"), rs.getString("abreviacao_unidade"));

            Produto produto = new Produto(rs.getLong("id"), marca, rs.getString("descricao"), categoria, rs.getFloat("preco_compra"), rs.getFloat("preco_venda"), rs.getFloat("estoque"), unidadeMedida);
            produtos.add(produto);
        }

        stm.close();
        rs.close();

        return produtos;
    }

    public List<Produto> buscarPorCategoria(CategoriaProduto categoria) throws SQLException {
        ResultSet rs;
        PreparedStatement stm;

        List<Produto> produtos = new ArrayList<>();

        String sql = "SELECT * FROM view_produto"
                + "\nWHERE produto.id_categoria_produto = " + categoria.getId() + " AND WHERE status = 1";

        stm = getConector().prepareStatement(sql);
        rs = stm.executeQuery(sql);

        while (rs.next()) {
            Marca marca = new Marca(rs.getLong("id_marca"), rs.getString("descricao_marca"));
            UnidadeMedida unidadeMedida = new UnidadeMedida(rs.getLong("id_unidade_medida"), rs.getString("descricao_unidade"), rs.getString("abreviacao_unidade"));

            Produto produto = new Produto(rs.getLong("id"), marca, rs.getString("descricao"), categoria, rs.getFloat("preco_compra"), rs.getFloat("preco_venda"), rs.getFloat("estoque"), unidadeMedida);
            produtos.add(produto);
        }

        stm.close();
        rs.close();

        return produtos;
    }

    public Produto buscarPorId(Long id) throws SQLException {
        ResultSet rs;
        PreparedStatement stm;

        Produto produto = null;

        String sql = "SELECT * FROM view_produto WHERE id = " + id + " AND status = 1";

        stm = getConector().prepareStatement(sql);
        rs = stm.executeQuery(sql);

        while (rs.next()) {
            CategoriaProduto categoria = new CategoriaProduto(rs.getLong("id_categoria_produto"), rs.getString("descricao_categoria"));
            Marca marca = new Marca(rs.getLong("id_marca"), rs.getString("descricao_marca"));
            UnidadeMedida unidadeMedida = new UnidadeMedida(rs.getLong("id_unidade_medida"), rs.getString("descricao_unidade"), rs.getString("abreviacao_unidade"));

            produto = new Produto(rs.getLong("id"), marca, rs.getString("descricao"), categoria, rs.getFloat("preco_compra"), rs.getFloat("preco_venda"), rs.getFloat("estoque"), unidadeMedida);
        }

        stm.close();
        rs.close();

        return produto;
    }

}
