package banco.dao;

import banco.ControleDAO;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Administrador;
import model.Bairro;
import model.CategoriaSaida;
import model.Cidade;
import model.Endereco;
import model.Saida;
import util.DateUtils;

/**
 * DAO responsável pela ações realizadas na base de dados referentes as saida
 */
public class SaidaDAO extends DAO {

    ;
    
    public SaidaDAO() {
        super();
    }

    /**
     * Inserir marca na base de dados
     */
    public Long inserir(Saida saida) throws Exception {
        ResultSet rs;
        PreparedStatement stm;

        if (saida.getCategoria().getId() == null) {
            Long id = ControleDAO.getBanco().getCategoriaSaidaDAO().inserir(saida.getCategoria());
            saida.getCategoria().setId(id);
        }

        String sql = "INSERT INTO saida ( id_categoria_saida, id_administrador, descricao, data, valor ) VALUES (?, ?, ?, ?, ?)";

        stm = getConector().prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

        stm.setInt(1, saida.getCategoria().getId().intValue());
        stm.setInt(2, saida.getAdministrador().getId().intValue());
        stm.setString(3, saida.getDescricao());
        stm.setLong(4, saida.getData());
        stm.setFloat(5, saida.getValor());

        return super.inserir(stm);
    }

    /**
     * Atualizar dados saida na base de dados
     */
    public boolean editar(Saida saida) throws SQLException {
        ResultSet rs;
        PreparedStatement stm;

        String sql = "UPDATE saida SET id_categoria_saida =?, id_administrador =?, descricao =?, valor =?, data =? WHERE id =?";

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
        ResultSet rs;
        PreparedStatement stm;

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
    public List<Saida> listar() throws SQLException {
        ResultSet rs;
        PreparedStatement stm;

        List<Saida> saidas = new ArrayList<>();

        String sql = "SELECT * FROM view_saida";
        stm = getConector().prepareStatement(sql);
        rs = stm.executeQuery(sql);

        while (rs.next()) {
            Cidade cidadeAdm = new Cidade(rs.getLong("id_cidade_administrador"), rs.getString("nome_cidade_administrador"));
            Bairro bairroAdm = new Bairro(rs.getLong("id_bairro_administrador"), rs.getString("nome_bairro_administrador"), cidadeAdm);
            Endereco enderecoAdm = new Endereco(rs.getLong("id_endereco_administrador"), bairroAdm, rs.getString("rua_administrador"), rs.getString("numero_administrador"));

            Administrador adm = new Administrador(rs.getLong("id_administrador"), rs.getString("nome_administrador"),
                    null, null, enderecoAdm, null, rs.getString("cpf_administrador"), null, null, true);

            CategoriaSaida categoria = new CategoriaSaida(rs.getLong("id_categoria_saida"), rs.getString("descricao_categoria"));

            Saida saida = new Saida(rs.getLong("id"), adm, rs.getString("descricao"), categoria, rs.getFloat("valor"), rs.getLong("data"));

            saidas.add(saida);
        }

        stm.close();
        rs.close();

        return saidas;
    }

    public List<Saida> buscarPorIntervalo(String dataInicio, String dataFinal) throws SQLException {
        ResultSet rs;
        PreparedStatement stm;

        Long inicio = DateUtils.getLongFromDate(dataInicio);
        Long finall = DateUtils.getLongFromDate(dataFinal);

        List<Saida> saidas = new ArrayList<>();

        String sql = "SELECT * FROM view_saida"
                + "\nWHERE data>= " + inicio + " AND data < " + finall;;
        stm = getConector().prepareStatement(sql);
        rs = stm.executeQuery(sql);

        while (rs.next()) {
            Cidade cidadeAdm = new Cidade(rs.getLong("id_cidade_administrador"), rs.getString("nome_cidade_administrador"));
            Bairro bairroAdm = new Bairro(rs.getLong("id_bairro_administrador"), rs.getString("nome_bairro_administrador"), cidadeAdm);
            Endereco enderecoAdm = new Endereco(rs.getLong("id_endereco_administrador"), bairroAdm, rs.getString("rua_administrador"), rs.getString("numero_administrador"));

            Administrador adm = new Administrador(rs.getLong("id_administrador"), rs.getString("nome_administrador"),
                    null, null, enderecoAdm, null, rs.getString("cpf_administrador"), null, null, true);

            CategoriaSaida categoria = new CategoriaSaida(rs.getLong("id_categoria_saida"), rs.getString("descricao_categoria"));

            Saida saida = new Saida(rs.getLong("id"), adm, rs.getString("descricao"), categoria, rs.getFloat("valor"), rs.getLong("data"));

            saidas.add(saida);
        }

        stm.close();
        rs.close();

        return saidas;
    }

}
