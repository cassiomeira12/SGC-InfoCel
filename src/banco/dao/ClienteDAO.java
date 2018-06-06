package banco.dao;

import banco.ControleDAO;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Bairro;
import model.Cidade;
import model.Cliente;
import model.Endereco;

/**
 * DAO responsável pela ações realizadas na base de dados referentes aos
 * clientes
 */
public class ClienteDAO extends DAO {

    public ClienteDAO() {
        super();
    }

    /**
     * Inserir cliente na base de dados
     */
    public Long inserir(Cliente cliente) throws Exception {
        ResultSet rs;
        PreparedStatement stm;

        String sql = "INSERT INTO cliente ( nome, id_endereco, cpf, rg, telefone, data_cadastro, status ) VALUES (?, ?, ?, ?, ?, ?, ?)";

        Long id_endereco = ControleDAO.getBanco().getEnderecoDAO().inserir(cliente.getEndereco());
        if (id_endereco == null) {
            return null;
        } else {
            cliente.getEndereco().setId(id_endereco);
        }

        stm = getConector().prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

        stm.setString(1, cliente.getNome());
        stm.setLong(2, cliente.getEndereco().getId());
        stm.setString(3, cliente.getCpf());
        stm.setString(4, cliente.getRg());
        stm.setString(5, cliente.getTelefone());
        stm.setLong(6, System.currentTimeMillis());
        stm.setBoolean(7, cliente.getStatus());

        return super.inserir(stm);

    }

    /**
     * Atualizar dados cliente na base de dados
     */
    public boolean editar(Cliente cliente) throws SQLException {
        ResultSet rs;
        PreparedStatement stm;

        String sql = "UPDATE cliente SET nome =?, id_endereco =?, cpf =?, rg =?, telefone =?, status =? WHERE id =?";

        //caso haja alterações no endereço
        ControleDAO.getBanco().getEnderecoDAO().editar(cliente.getEndereco());

        stm = getConector().prepareStatement(sql);

        stm.setString(1, cliente.getNome());
        stm.setLong(2, cliente.getEndereco().getId());
        stm.setString(3, cliente.getCpf());
        stm.setString(4, cliente.getRg());
        stm.setString(5, cliente.getTelefone());
        stm.setBoolean(6, cliente.getStatus());

        stm.setInt(7, cliente.getId().intValue());

        stm.executeUpdate();
        stm.close();

        return true;
    }

    /**
     * Excluir cliente na base de dados
     */
    public boolean excluir(int id) throws SQLException {
        ResultSet rs;
        PreparedStatement stm;

        String sql = "UPDATE cliente SET status = 0 WHERE id =" + id;

        stm = getConector().prepareStatement(sql);

        stm.execute();

        stm.close();

        return true;
    }

    /**
     * Consultar todos clientes cadastrados na base de dados
     */
    public List<Cliente> listar() throws SQLException {
        ResultSet rs;
        PreparedStatement stm;

        List<Cliente> clientes = new ArrayList<>();

        String sql = "SELECT * FROM view_cliente WHERE status = 1";

        stm = getConector().prepareStatement(sql);
        rs = stm.executeQuery(sql);

        while (rs.next()) {
            Cidade cidade = new Cidade(rs.getLong("id_cidade"), rs.getString("nome_cidade"));
            Bairro bairro = new Bairro(rs.getLong("id_bairro"), rs.getString("nome_bairro"), cidade);
            Endereco endereco = new Endereco(rs.getLong("id_endereco"), bairro, rs.getString("rua"), rs.getString("numero"));

            Cliente cliente = new Cliente((long) rs.getInt(1), rs.getString(2), endereco, rs.getString(3), rs.getString(4), rs.getString(5), rs.getLong(6), rs.getBoolean(7));

            clientes.add(cliente);
        }

        stm.close();
        rs.close();

        return clientes;
    }

    public List<Cliente> buscarPorNome(String busca) throws SQLException {
        ResultSet rs;
        PreparedStatement stm;

        List<Cliente> clientes = new ArrayList<>();

        String sql = "SELECT view_cliente.* FROM view_cliente WHERE nome LIKE '%" + busca + "%'";

        stm = getConector().prepareStatement(sql);
        rs = stm.executeQuery(sql);

        while (rs.next()) {
            Cidade cidade = new Cidade(rs.getLong("id_cidade"), rs.getString("nome_cidade"));
            Bairro bairro = new Bairro(rs.getLong("id_bairro"), rs.getString("nome_bairro"), cidade);
            Endereco endereco = new Endereco(rs.getLong("id_endereco"), bairro, rs.getString("rua"), rs.getString("numero"));

            Cliente cliente = new Cliente((long) rs.getInt(1), rs.getString(2), endereco, rs.getString(3), rs.getString(4), rs.getString(5), rs.getLong(6), rs.getBoolean(7));

            clientes.add(cliente);
        }

        stm.close();
        rs.close();

        return clientes;
    }

}
