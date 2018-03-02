package banco.dao;

import banco.ControleDAO;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.CategoriaProduto;
import model.Celular;
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
    public Long inserir(Produto produto) {
        try {
            String sql = "INSERT INTO produto ( descricao_produto, categoria_produto_id, marca_id, preco_compra, preco_venda, estoque, unidade_medida_id ) VALUES (?, ?, ?, ?, ?, ?)";

            stm = getConector().prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

            stm.setString(1, produto.getDescricao());
            stm.setInt(2, produto.getCategoria().getId().intValue());
            stm.setInt(3, produto.getMarca().getId().intValue());
            stm.setFloat(4, produto.getPrecoCompra());
            stm.setFloat(5, produto.getPrecoVenda());
            stm.setFloat(6, produto.getEstoque());
            stm.setInt(7, produto.getUnidadeMedida().getId().intValue());

            return super.inserir();
        } catch (Exception ex) {
            chamarAlertaErro("Erro ao inserir produto na base de dados", ex.toString());
        }

        return null;
    }

    /**
     * Atualizar dados produto na base de dados
     */
    public boolean editar(Produto produto) {
        try {
            String sql = "UPDATE produto SET  descricao_produto =?, categoria_produto_id =?, marca_id =?, preco_compra =?, preco_venda =?, estoque =?, unidade_medida_id WHERE id_produto =?";

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

        } catch (SQLException ex) {
            chamarAlertaErro("Erro ao atualizar produto na base de dados!", ex.toString());
            return false;
        }

        return true;
    }

    /**
     * Excluir produto na base de dados
     */
    public boolean excluir(int id) {
        try {
            String sql = "DELETE FROM produto WHERE id_produto=?";

            stm = getConector().prepareStatement(sql);

            stm.setInt(1, id);
            stm.execute();

            stm.close();
        } catch (SQLException ex) {
            chamarAlertaErro("Erro ao excluir produto na base de dados!", ex.toString());
            return false;
        }

        return true;
    }

    /**
     * Consultar todos produtos cadastrados na base de dados
     */
    public List<Produto> listar() {

        List<Produto> produtos = new ArrayList<>();

        try {
            String sql = "SELECT produto.*, marca.descricao_marca, categoria_produto.descricao_categoria, unidade_medida.descricao_unidade "
                    + "FROM produto"
                    + "\nINNER JOIN marca marca ON produto.marca_id = marca.id_marca"
                    + "\nINNER JOIN unidade_medida unidade_medida ON produto.unidade_medida_id = unidade_medida.id_unidade_medida"
                    + "\nINNER JOIN categoria_produto categoria_produto ON produto.categoria_produto_id = categoria_produto.id_categoria_produto";

            //System.out.println(sql);
            stm = getConector().prepareStatement(sql);
            rs = stm.executeQuery(sql);

            while (rs.next()) {
                CategoriaProduto categoria = new CategoriaProduto(rs.getLong("categoria_id"), rs.getString("descricao_categoria"));
                Marca marca = new Marca(rs.getLong("marca_id"), rs.getString("descricao_marca"));
                UnidadeMedida unidadeMedida = new UnidadeMedida(rs.getLong("unidade_medida_id"), rs.getString("descricao_unidade"));

                produtos.add(new Produto(rs.getLong("id_produto"), marca, rs.getString("descricao_produto"), categoria, rs.getFloat("preco_compra"), rs.getFloat("preco_venda"), rs.getFloat("estoque"), unidadeMedida));
            }

            stm.close();
            rs.close();

            return produtos;
        } catch (Exception ex) {
            chamarAlertaErro("Erro ao consultar produtos na base de dados!", ex.toString());
        }

        return produtos;
    }

    /* public List<Produto> listarParaVender() {

        List<Produto> produtos = new ArrayList<>();

        try {
            String sql = "SELECT produto.*, marca.descricao_marca, categoria_produto.descricao_categoria "
                    + "FROM produto"
                    + "\nINNER JOIN marca marca ON produto.id_marca = marca.id_marca"
                    + "\nINNER JOIN categoria_produto categoria_produto ON produto.id_categoria = categoria_produto.id_categoria";

            //System.out.println(sql);
            stm = getConector().prepareStatement(sql);
            rs = stm.executeQuery(sql);

            while (rs.next()) {
                CategoriaProduto categoria = new CategoriaProduto(rs.getLong("id_categoria"), rs.getString("descricao_categoria"));
                Marca marca = new Marca(rs.getLong("id_marca"), rs.getString("descricao_marca"));
                Produto produto = new Produto(rs.getLong("id_produto"), marca, rs.getString("descricao_produto"), categoria, rs.getFloat("preco_compra"), rs.getFloat("preco_venda"), rs.getFloat("estoque"));

                if (produto.getEstoque() > 0) {
                    produtos.add(new Produto(rs.getLong("id_produto"), marca, rs.getString("descricao_produto"), categoria, rs.getFloat("preco_compra"), rs.getFloat("preco_venda"), rs.getFloat("estoque")));
                }

            }

            stm.close();
            rs.close();

            return produtos;
        } catch (Exception ex) {
            chamarAlertaErro("Erro ao consultar produtos na base de dados!", ex.toString());
        }

        return produtos;
    }
     */
    public List<Produto> buscarPorDescricaoModelo(String busca) {

        List<Produto> produtos = new ArrayList<>();

        try {
            String sql = "SELECT produto.*, marca.descricao_marca, categoria_produto.descricao_categoria, unidade_medida.descricao_unidade "
                    + "FROM produto"
                    + "\nINNER JOIN marca marca ON produto.marca_id = marca.id_marca"
                    + "\nINNER JOIN unidade_medida unidade_medida ON produto.unidade_id = unidade_medida.id_unidade_medida"
                    + "\nINNER JOIN categoria_produto categoria_produto ON produto.categoria_id = categoria_produto.id_categoria_produto"
                    + "\nWHERE produto.descricao_produto LIKE '%" + busca + "%'"
                    + "\n OR produto.modelo LIKE '%" + busca + "%'";

            System.out.println(sql);
            stm = getConector().prepareStatement(sql);
            rs = stm.executeQuery(sql);

            while (rs.next()) {
                CategoriaProduto categoria = new CategoriaProduto(rs.getLong("categoria_id"), rs.getString("descricao_categoria"));
                Marca marca = new Marca(rs.getLong("marca_id"), rs.getString("descricao_marca"));
                UnidadeMedida unidadeMedida = new UnidadeMedida(rs.getLong("unidade_medida_id"), rs.getString("descricao_unidade"));

                produtos.add(new Produto(rs.getLong("id_produto"), marca, rs.getString("descricao_produto"), categoria, rs.getFloat("preco_compra"), rs.getFloat("preco_venda"), rs.getFloat("estoque"), unidadeMedida));
            }

            stm.close();
            rs.close();

            return produtos;
        } catch (Exception ex) {
            chamarAlertaErro("Erro ao consultar produtos na base de dados!", ex.toString());
        }

        return produtos;
    }

    public List<Produto> buscarPorCategoria(CategoriaProduto categoria) {

        List<Produto> produtos = new ArrayList<>();

        try {
            String sql = "SELECT produto.*, marca.descricao_marca, categoria_produto.descricao_categoria, unidade_medida.descricao_unidade "
                    + "FROM produto"
                    + "\nINNER JOIN marca marca ON produto.marca_id = marca.id_marca"
                    + "\nINNER JOIN unidade_medida unidade_medida ON produto.unidade_id = unidade_medida.id_unidade_medida"
                    + "\nINNER JOIN categoria_produto categoria_produto ON produto.categoria_id = categoria_produto.id_categoria_produto"
                    + "\nWHERE categoria_produto.descricao_categoria LIKE '%" + categoria.getDescricao() + "%'";

            System.out.println(sql);
            stm = getConector().prepareStatement(sql);
            rs = stm.executeQuery(sql);

            while (rs.next()) {
                Marca marca = new Marca(rs.getLong("marca_id"), rs.getString("descricao_marca"));
                UnidadeMedida unidadeMedida = new UnidadeMedida(rs.getLong("unidade_medida_id"), rs.getString("descricao_unidade"));

                produtos.add(new Produto(rs.getLong("id_produto"), marca, rs.getString("descricao_produto"), categoria, rs.getFloat("preco_compra"), rs.getFloat("preco_venda"), rs.getFloat("estoque"), unidadeMedida));
            }

            stm.close();
            rs.close();

            return produtos;
        } catch (Exception ex) {
            chamarAlertaErro("Erro ao consultar produtos na base de dados!", ex.toString());
        }

        return produtos;
    }

    public Produto buscarPorId(Long id) {
        Produto produto = null;
        try {
            String sql = "SELECT produto.*, marca.descricao_marca, categoria_produto.descricao_categoria, unidade_medida.descricao_unidade "
                    + "FROM produto"
                    + "\nINNER JOIN marca marca ON produto.marca_id = marca.id_marca"
                    + "\nINNER JOIN unidade_medida unidade_medida ON produto.unidade_medida_id = unidade_medida.id_unidade_medida"
                    + "\nINNER JOIN categoria_produto categoria_produto ON produto.categoria_id = categoria_produto.id_categoria"
                    + "\nWHERE produto.id_produto = " + id;

            System.out.println(sql);
            stm = getConector().prepareStatement(sql);
            rs = stm.executeQuery(sql);

            while (rs.next()) {
                CategoriaProduto categoria = new CategoriaProduto(rs.getLong("categoria_id"), rs.getString("descricao_categoria"));
                Marca marca = new Marca(rs.getLong("marca_id"), rs.getString("descricao_marca"));
                UnidadeMedida unidadeMedida = new UnidadeMedida(rs.getLong("unidade_medida_id"), rs.getString("descricao_unidade"));

                produto = new Produto(rs.getLong("id_produto"), marca, rs.getString("descricao_produto"), categoria, rs.getFloat("preco_compra"), rs.getFloat("preco_venda"), rs.getFloat("estoque"), unidadeMedida);

            }

            stm.close();
            rs.close();

            return produto;
        } catch (Exception ex) {
            chamarAlertaErro("Erro ao consultar produtos na base de dados!", ex.toString());
        }

        return null;
    }

}
