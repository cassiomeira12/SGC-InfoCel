package banco.dao;

import banco.ControleDAO;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.CategoriaProduto;
import model.Celular;
import model.Marca;
import model.Produto;

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
    public void inserir(Produto produto) {
        try {
            String sql = "INSERT INTO produto ( descricao_produto, id_categoria, id_marca, preco_compra, preco_venda, estoque ) VALUES (?, ?, ?, ?, ?, ?)";

            stm = getConector().prepareStatement(sql);

            stm.setString(1, produto.getDescricao());
            stm.setInt(3, produto.getCategoria().getId().intValue());
            stm.setInt(3, produto.getMarca().getId().intValue());
            stm.setFloat(4, produto.getPrecoCompra());
            stm.setFloat(5, produto.getPrecoVenda());
            stm.setFloat(6, produto.getEstoque());

            stm.executeUpdate();
            stm.close();

        } catch (SQLException ex) {
            chamarAlertaErro("Erro ao inserir produto na base de dados", ex.toString());
        }
    }

    /**
     * Atualizar dados produto na base de dados
     */
    public void editar(Produto produto) {
        try {
            String sql = "UPDATE produto SET  descricao_produto =?, id_categoria =?, id_marca =?, preco_compra =?, preco_venda =?, estoque =? WHERE id_produto =?";

            stm = getConector().prepareStatement(sql);

            stm.setString(1, produto.getDescricao());
            stm.setInt(3, produto.getCategoria().getId().intValue());
            stm.setInt(3, produto.getMarca().getId().intValue());
            stm.setFloat(4, produto.getPrecoCompra());
            stm.setFloat(5, produto.getPrecoVenda());
            stm.setFloat(6, produto.getEstoque());
            stm.setInt(2, produto.getId().intValue());

            stm.executeUpdate();
            stm.close();

        } catch (SQLException ex) {
            chamarAlertaErro("Erro ao atualizar produto na base de dados!", ex.toString());
        }
    }

    /**
     * Excluir produto na base de dados
     */
    public void excluir(int id) {
        try {
            String sql = "DELETE FROM produto WHERE id=?";

            stm = getConector().prepareStatement(sql);

            stm.setInt(1, id);
            stm.execute();

            stm.close();
        } catch (SQLException ex) {
            chamarAlertaErro("Erro ao excluir produto na base de dados!", ex.toString());
        }
    }

    /**
     * Consultar todos produtos cadastrados na base de dados
     */
    public List<Produto> listar() {

        List<Produto> produtos = new ArrayList<>();

        try {
            String sql = "SELECT produto.* FROM produto";

            stm = getConector().prepareStatement(sql);
            rs = stm.executeQuery(sql);

            while (rs.next()) {
                Marca marca = ControleDAO.getBanco().getMarcaDAO().buscarPorId(rs.getInt(2));
                CategoriaProduto categoria = ControleDAO.getBanco().getCategoriaProdutoDAO().buscarPorId(rs.getInt(4));

                Produto produto = new Produto((long) rs.getInt(1), marca, rs.getString(3), categoria, rs.getFloat(5), rs.getFloat(6), rs.getFloat(7));

                produtos.add(produto);
            }

            stm.close();
            rs.close();

        } catch (SQLException ex) {
            chamarAlertaErro("Erro ao consultar produtos na base de dados!", ex.toString());
        }

        return produtos;
    }

}
