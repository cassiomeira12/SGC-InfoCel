package banco.dao;

import banco.ControleDAO;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Administrador;
import model.CategoriaSaida;
import model.Celular;
import model.Cliente;
import model.Marca;
import model.Produto;
import model.Receita;
import model.Saida;
import util.DateUtils;

/**
 * DAO responsável pela ações realizadas na base de dados referentes as saida
 */
public class SaidaDAO extends DAO {

    public SaidaDAO() {
        super();
    }

    /**
     * Inserir marca na base de dados
     */
    public Long inserir(Saida saida) throws Exception {
        String sql = "INSERT INTO saida ( id_categoria_saida, id_administrador, descricao, data, valor ) VALUES (?, ?, ?, ?, ?)";

        stm = getConector().prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

        stm.setInt(1, saida.getCategoria().getId().intValue());
        stm.setInt(2, saida.getAdministrador().getId().intValue());
        stm.setString(3, saida.getDescricao());
        stm.setLong(4, saida.getData());
        stm.setFloat(5, saida.getValor());

        return super.inserir();
    }

    /**
     * Atualizar dados saida na base de dados
     */
    public boolean editar(Saida saida) throws SQLException {
        String sql = "UPDATE saida SET id_categoria =?, id_administrador =?, descricao =?, valor =?, data =? WHERE id =?";

        stm = getConector().prepareStatement(sql);

        stm.setInt(1, saida.getCategoria().getId().intValue());
        stm.setInt(2, saida.getAdministrador().getId().intValue());
        stm.setString(3, saida.getDescricao());
        stm.setFloat(4, saida.getValor());
        stm.setLong(5, saida.getData());

        stm.setInt(6, saida.getId().intValue());

        stm.executeUpdate();
        stm.close();
        return true;
    }

    /**
     * Excluir marca na base de dados
     */
    public boolean excluir(int id) throws SQLException {
        String sql = "DELETE FROM saida WHERE id=?";

        stm = getConector().prepareStatement(sql);

        stm.setInt(1, id);
        stm.execute();

        stm.close();

        return true;
    }

    /**
     * Consultar todas receita cadastradas na base de dados
     */
    private List<Saida> listar() throws SQLException {
        List<Saida> saidas = new ArrayList<>();

        String sql = "SELECT * FROM view_saida";
        stm = getConector().prepareStatement(sql);
        rs = stm.executeQuery(sql);

        while (rs.next()) {
            Administrador adm = new Administrador(rs.getLong("id_administrador"), rs.getString("nome_administrador"), null, null, null, null, null, null, null, 1);
            CategoriaSaida categoria = new CategoriaSaida(rs.getLong("id_categoria_saida"), rs.getString("descricao_categoria_saida"));

            Saida saida = new Saida(rs.getLong("id"), adm, rs.getString("descricao"), categoria, rs.getFloat("valor"), rs.getLong("data"));

            saidas.add(saida);
        }

        stm.close();
        rs.close();

        return saidas;
    }

    public List<Saida> buscarPorIntervalo(String dataInicio, String dataFinal) throws SQLException {
        Long inicio = DateUtils.getLongFromDate(dataInicio);
        Long finall = DateUtils.getLongFromDate(dataFinal);

        List<Saida> saidas = new ArrayList<>();

        String sql = "SELECT * FROM view_saida"
                + "\nWHERE data>= " + inicio + " AND data < " + finall;;
        stm = getConector().prepareStatement(sql);
        rs = stm.executeQuery(sql);

        while (rs.next()) {
            Administrador adm = new Administrador(rs.getLong("id_administrador"), rs.getString("nome_administrador"), null, null, null, null, null, null, null, 1);
            CategoriaSaida categoria = new CategoriaSaida(rs.getLong("id_categoria_saida"), rs.getString("descricao_categoria_saida"));

            Saida saida = new Saida(rs.getLong("id"), adm, rs.getString("descricao"), categoria, rs.getFloat("valor"), rs.getLong("data"));

            saidas.add(saida);
        }

        stm.close();
        rs.close();

        return saidas;
    }

}
