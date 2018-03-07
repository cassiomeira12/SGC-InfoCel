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
import model.UnidadeMedida;

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
    public Long inserir(Celular celular) throws Exception {
        String sql = "INSERT INTO produto ( descricao, id_categoria_produto, id_marca, preco_compra, preco_venda, estoque, modelo, imei, cor, eh_celular, id_unidade_medida) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

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
        stm.setInt(11, celular.getUnidadeMedida().getId().intValue());

        return super.inserir();
    }

    /**
     * Atualizar dados celular na base de dados
     */
    public boolean editar(Celular celular) throws SQLException {
        String sql = "UPDATE produto SET  descricao =?, id_categoria_produto =?, id_marca =?, preco_compra =?, preco_venda =?, estoque =?, modelo =?, imei =?, cor =?, id_unidade_medida =  WHERE id =?";

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
        stm.setInt(10, celular.getUnidadeMedida().getId().intValue());

        stm.setInt(10, celular.getId().intValue());

        stm.executeUpdate();
        stm.close();

        return true;
    }

    /**
     * Excluir celular na base de dados
     */
    public boolean excluir(int id) throws SQLException {
        ControleDAO.getBanco().getProdutoDAO().excluir(id);
        return true;
    }

    /**
     * Consultar todos produtos cadastrados na base de dados
     */
    public List<Celular> listar() throws SQLException {

        List<Celular> celulares = new ArrayList<>();

        String sql = "SELECT * FROM view_produto WHERE eh_celular = true";

        stm = getConector().prepareStatement(sql);
        rs = stm.executeQuery(sql);

        while (rs.next()) {
            CategoriaProduto categoria = new CategoriaProduto(rs.getLong("categoria_produto_id"), rs.getString("descricao_categoria"));
            Marca marca = new Marca(rs.getLong("marca_id"), rs.getString("descricao_marca"));
            UnidadeMedida unidadeMedida = new UnidadeMedida(rs.getLong(""), rs.getString(""), "");

            Celular celular = new Celular(rs.getLong("id_produto"), marca, rs.getString("descricao_produto"), categoria, rs.getFloat("preco_compra"), rs.getFloat("preco_venda"), rs.getFloat("estoque"), unidadeMedida);
            celular.setCor(rs.getString("cor"));
            celular.setImei(rs.getString("imei"));
            celular.setModelo("modelo");

            celulares.add(celular);
        }

        stm.close();
        rs.close();

        return celulares;
    }

}
