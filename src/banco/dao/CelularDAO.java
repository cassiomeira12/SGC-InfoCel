package banco.dao;

import banco.ControleDAO;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Administrador;
import model.CategoriaProduto;
import model.Celular;
import model.Marca;

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
    public boolean inserir(Celular celular) {
        try {
            String sql = "INSERT INTO produto ( descricao_produto, id_categoria, id_marca, preco_compra, preco_venda, estoque, modelo, imei, cor ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

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

            stm.executeUpdate();
            stm.close();

        } catch (SQLException ex) {
            chamarAlertaErro("Erro ao inserir celular na base de dados", ex.toString());
            return false;
        }

        return true;
    }

    /**
     * Atualizar dados celular na base de dados
     */
    public boolean editar(Celular celular) {
        try {
            String sql = "UPDATE produto SET  descricao_produto =?, id_categoria =?, id_marca =?, preco_compra =?, preco_venda =?, estoque =?, modelo =?, imei =?, cor =? WHERE id_produto =?";

            stm = getConector().prepareStatement(sql);

            stm.setString(1, celular.getDescricao());
            stm.setInt(3, celular.getCategoria().getId().intValue());
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
            String sql = "DELETE FROM produto WHERE id_produto=?";

            stm = getConector().prepareStatement(sql);

            stm.setInt(1, id);
            stm.execute();

            stm.close();
        } catch (SQLException ex) {
            chamarAlertaErro("Erro ao excluir celular na base de dados!", ex.toString());
            return false;
        }

        return true;
    }

    /**
     * Consultar todos celulares cadastrados na base de dados
     */
    private List<Celular> listar() {

        List<Celular> celulares = new ArrayList<>();

        try {
            String sql = "SELECT produto.* FROM produto WHERE produto.descricao_categoria = 'CELULAR'";

            stm = getConector().prepareStatement(sql);
            rs = stm.executeQuery(sql);

            while (rs.next()) {
                Marca marca = ControleDAO.getBanco().getMarcaDAO().buscarPorId(rs.getInt(2));
                CategoriaProduto categoria = ControleDAO.getBanco().getCategoriaProdutoDAO().buscarPorId(rs.getInt(4));

                Celular celular = new Celular((long) rs.getInt(1), marca, rs.getString(3), categoria, rs.getFloat(5), rs.getFloat(6), rs.getFloat(7));
                celular.setModelo(rs.getString(8));
                celular.setImei(rs.getString(9));
                celular.setCor(rs.getString(10));

                celulares.add(celular);
            }

            stm.close();
            rs.close();

        } catch (SQLException ex) {
            chamarAlertaErro("Erro ao consultar celulares na base de dados!", ex.toString());
        }

        return celulares;
    }

}
