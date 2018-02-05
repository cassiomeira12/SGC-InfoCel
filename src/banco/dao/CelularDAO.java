package banco.dao;

import banco.ControleDAO;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Administrador;
import model.CategoriaProduto;
import model.Celular;
import model.Marca;
import model.Produto;

/**
 * DAO responsável pela ações realizadas na base de dados referentes a celulares
 */
public class CelularDAO extends DAO {

    public CelularDAO() {
        super();
    }

    /**
     * Inserir celular na base de dados
     */
    public Long inserir(Celular celular) {
        try {
            String sql = "INSERT INTO produto ( descricao_produto, id_categoria, id_marca, preco_compra, preco_venda, estoque, modelo, imei, cor, eh_celular) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            stm = getConector().prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

            stm.setString(1, celular.getDescricao());
            stm.setInt(2, celular.getCategoria().getId().intValue());
            stm.setInt(3, celular.getMarca().getId().intValue());
            stm.setFloat(4, celular.getPrecoCompra());
            stm.setFloat(5, celular.getPrecoVenda());
            stm.setFloat(6, celular.getEstoque());
            stm.setString(7, celular.getModelo());
            stm.setString(8, celular.getImei());
            stm.setString(9, celular.getCor());
            stm.setBoolean(10, true);

            return super.inserir();
        } catch (Exception ex) {
            chamarAlertaErro("Erro ao inserir celular na base de dados", ex.toString());
        }

        return null;
    }

    /**
     * Atualizar dados celular na base de dados
     */
    public boolean editar(Celular celular) {
        try {
            String sql = "UPDATE produto SET  descricao_produto =?, id_categoria =?, id_marca =?, preco_compra =?, preco_venda =?, estoque =?, modelo =?, imei =?, cor =? WHERE id_produto =?";

            stm = getConector().prepareStatement(sql);

            stm.setString(1, celular.getDescricao());
            stm.setInt(2, celular.getCategoria().getId().intValue());
            stm.setInt(3, celular.getMarca().getId().intValue());
            stm.setFloat(4, celular.getPrecoCompra());
            stm.setFloat(5, celular.getPrecoVenda());
            stm.setFloat(6, celular.getEstoque());
            stm.setString(7, celular.getModelo());
            stm.setString(8, celular.getImei());
            stm.setString(9, celular.getCor());

            stm.setInt(10, celular.getId().intValue());

            stm.executeUpdate();
            stm.close();

        } catch (SQLException ex) {
            chamarAlertaErro("Erro ao atualizar celular na base de dados!", ex.toString());
            return false;
        }

        return true;
    }

    /**
     * Excluir celular na base de dados
     */
    public boolean excluir(int id) {
        try {
            ControleDAO.getBanco().getProdutoDAO().excluir(id);
        } catch (Exception ex) {
            chamarAlertaErro("Erro ao excluir celular na base de dados!", ex.toString());
            return false;
        }

        return true;
    }

    /**
     * Consultar todos produtos cadastrados na base de dados
     */
    public List<Celular> listar() {

        List<Celular> celulares = new ArrayList<>();

        try {
            String sql = "SELECT produto.*, marca.descricao_marca, categoria_produto.descricao_categoria "
                    + "FROM produto"
                    + "\nINNER JOIN marca marca ON produto.id_marca = marca.id_marca"
                    + "\nINNER JOIN categoria_produto categoria_produto ON produto.id_categoria = categoria_produto.id_categoria"
                    + "\nWHERE produto.eh_celular = true";

            System.out.println(sql);
            stm = getConector().prepareStatement(sql);
            rs = stm.executeQuery(sql);

            while (rs.next()) {
                CategoriaProduto categoria = new CategoriaProduto(rs.getLong("id_categoria"), rs.getString("descricao_categoria"));
                Marca marca = new Marca(rs.getLong("id_marca"), rs.getString("descricao_marca"));

                Celular celular = new Celular(rs.getLong("id_produto"), marca, rs.getString("descricao_produto"), categoria, rs.getFloat("preco_compra"), rs.getFloat("preco_venda"), rs.getFloat("estoque"));
                celular.setCor(rs.getString("cor"));
                celular.setImei(rs.getString("imei"));
                celular.setModelo("modelo");

                celulares.add(celular);
            }

            stm.close();
            rs.close();

            return celulares;
        } catch (Exception ex) {
            chamarAlertaErro("Erro ao consultar celulares na base de dados!", ex.toString());
        }

        return null;
    }

    public List<Celular> buscarPorDescricaoModelo(String busca) {

        List<Celular> celulares = new ArrayList<>();

        try {
            String sql = "SELECT produto.*, marca.descricao_marca, categoria_produto.descricao_categoria "
                    + "FROM produto"
                    + "\nINNER JOIN marca marca ON produto.id_marca = marca.id_marca"
                    + "\nINNER JOIN categoria_produto categoria_produto ON produto.id_categoria = categoria_produto.id_categoria"
                    + "\nWHERE produto.descricao_produto LIKE '%" + busca + "%'"
                    + "\n OR produto.modelo LIKE '%" + busca + "%'"
                    + "\nAND produto.eh_celular = true";

            System.out.println(sql);
            stm = getConector().prepareStatement(sql);
            rs = stm.executeQuery(sql);

            while (rs.next()) {
                CategoriaProduto categoria = new CategoriaProduto(rs.getLong("id_categoria"), rs.getString("descricao_categoria"));
                Marca marca = new Marca(rs.getLong("id_marca"), rs.getString("descricao_marca"));

                Celular celular = new Celular(rs.getLong("id_produto"), marca, rs.getString("descricao_produto"), categoria, rs.getFloat("preco_compra"), rs.getFloat("preco_venda"), rs.getFloat("estoque"));
                celular.setCor(rs.getString("cor"));
                celular.setImei(rs.getString("imei"));
                celular.setModelo("modelo");

                celulares.add(celular);
            }

            stm.close();
            rs.close();

            return celulares;
        } catch (Exception ex) {
            chamarAlertaErro("Erro ao consultar produtos na base de dados!", ex.toString());
        }

        return null;
    }

    public Celular buscarPorId(Long id) {
        Celular celular = null;
        try {
            String sql = "SELECT produto.*, marca.descricao_marca, categoria_produto.descricao_categoria "
                    + "FROM produto"
                    + "\nINNER JOIN marca marca ON produto.id_marca = marca.id_marca"
                    + "\nINNER JOIN categoria_produto categoria_produto ON produto.id_categoria = categoria_produto.id_categoria"
                    + "\nWHERE produto.id_produto = " + id;

            System.out.println(sql);
            stm = getConector().prepareStatement(sql);
            rs = stm.executeQuery(sql);

            while (rs.next()) {
                CategoriaProduto categoria = new CategoriaProduto(rs.getLong("id_categoria"), rs.getString("descricao_categoria"));
                Marca marca = new Marca(rs.getLong("id_marca"), rs.getString("descricao_marca"));

                celular = new Celular(id, marca, rs.getString("descricao_produto"), categoria, rs.getFloat("preco_compra"), rs.getFloat("preco_venda"), rs.getFloat("estoque"));
                celular.setCor(rs.getString("cor"));
                celular.setImei(rs.getString("imei"));
                celular.setModelo("modelo");
            }

            stm.close();
            rs.close();

            return celular;
        } catch (Exception ex) {
            chamarAlertaErro("Erro ao consultar produtos na base de dados!", ex.toString());
        }

        return null;
    }
}
