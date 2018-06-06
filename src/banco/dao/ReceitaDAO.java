package banco.dao;

import banco.ControleDAO;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Administrador;
import model.Bairro;
import model.Cidade;
import model.Cliente;
import model.Endereco;
import model.Receita;
import util.DateUtils;

/**
 * DAO responsável pela ações realizadas na base de dados referentes as receitas
 */
public class ReceitaDAO extends DAO {

    public ReceitaDAO() {
        super();
    }

    /**
     * Inserir marca na base de dados
     */
    public Long inserir(Receita receita) throws Exception {
        ResultSet rs;
        PreparedStatement stm;

        if (receita.getCliente().getId() == null) {
            Long id = ControleDAO.getBanco().getClienteDAO().inserir(receita.getCliente());
            receita.getCliente().setId(id);

        }

        String sql = "INSERT INTO receita ( id_cliente, id_administrador, descricao, data, valor ) VALUES (?, ?, ?, ?, ?)";

        stm = getConector().prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

        stm.setInt(1, receita.getCliente().getId().intValue());
        stm.setInt(2, receita.getAdministrador().getId().intValue());
        stm.setString(3, receita.getDescricao());
        stm.setLong(4, receita.getData());
        stm.setFloat(5, receita.getValor());

        return super.inserir(stm);
    }

    /**
     * Atualizar dados receita na base de dados
     */
    public boolean editar(Receita receita) throws SQLException {
        ResultSet rs;
        PreparedStatement stm;

        String sql = "UPDATE receita SET id_cliente =?, id_administrador =?, descricao =?, valor =?, data =? WHERE id=?";

        stm = getConector().prepareStatement(sql);

        stm.setInt(1, receita.getCliente().getId().intValue());
        stm.setInt(2, receita.getAdministrador().getId().intValue());
        stm.setString(3, receita.getDescricao());
        stm.setFloat(4, receita.getValor());
        stm.setLong(5, receita.getData());

        stm.setInt(6, receita.getId().intValue());

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

        String sql = "DELETE FROM receita WHERE id=?";

        stm = getConector().prepareStatement(sql);

        stm.setInt(1, id);
        stm.execute();

        stm.close();

        return true;
    }

    /**
     * Consultar todas receita cadastradas na base de dados
     */
    public List<Receita> listar() throws SQLException {
        ResultSet rs;
        PreparedStatement stm;

        List<Receita> receitas = new ArrayList<>();

        String sql = "SELECT * FROM view_receita";
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

            Receita receita = new Receita(rs.getLong(1), cliente, adm, rs.getString(4), rs.getLong(5), rs.getFloat(6));

            receitas.add(receita);
        }

        stm.close();
        rs.close();

        return receitas;
    }

    public List<Receita> buscarPorIntervalo(String dataInicio, String dataFinal) throws SQLException {
        ResultSet rs;
        PreparedStatement stm;

        Long inicio = DateUtils.getLongFromDate(dataInicio);
        Long finall = DateUtils.getLongFromDate(dataFinal);

        List<Receita> receitas = new ArrayList<>();

        String sql = "SELECT * FROM view_receita"
                + "\nWHERE data >= " + inicio + " AND data < " + finall;;
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

            Receita receita = new Receita(rs.getLong(1), cliente, adm, rs.getString(4), rs.getLong(5), rs.getFloat(6));

            receitas.add(receita);
        }

        stm.close();
        rs.close();

        return receitas;
    }

}
