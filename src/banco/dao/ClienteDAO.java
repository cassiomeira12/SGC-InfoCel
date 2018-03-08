package banco.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Cliente;

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
        String sql = "INSERT INTO cliente ( nome, endereco, cpf, rg, telefone, cidade, data_cadastro, status ) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        stm = getConector().prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

        stm.setString(1, cliente.getNome());
        stm.setString(2, cliente.getEndereco());
        stm.setString(3, cliente.getCpf());
        stm.setString(4, cliente.getRg());
        stm.setString(5, cliente.getTelefone());
        stm.setString(6, cliente.getCidade());
        stm.setLong(7, System.currentTimeMillis());
        stm.setBoolean(8, cliente.getStatus());

        return super.inserir();

    }

    /**
     * Atualizar dados cliente na base de dados
     */
    public boolean editar(Cliente cliente) throws SQLException {
        String sql = "UPDATE cliente SET nome =?, endereco =?, cpf =?, rg =?, telefone =?, cidade =?, status =? WHERE id =?";

        stm = getConector().prepareStatement(sql);

        stm.setString(1, cliente.getNome());
        stm.setString(2, cliente.getEndereco());
        stm.setString(3, cliente.getCpf());
        stm.setString(4, cliente.getRg());
        stm.setString(5, cliente.getTelefone());
        stm.setString(6, cliente.getCidade());
        stm.setBoolean(7, cliente.getStatus());

        stm.setInt(8, cliente.getId().intValue());

        stm.executeUpdate();
        stm.close();

        return true;
    }

    /**
     * Excluir cliente na base de dados
     */
    public boolean excluir(int id) throws SQLException {
        String sql = "DELETE FROM cliente WHERE id=?";

        stm = getConector().prepareStatement(sql);

        stm.setInt(1, id);
        stm.execute();

        stm.close();

        return true;
    }

    /**
     * Consultar todos clientes cadastrados na base de dados
     */
    public List<Cliente> listar() throws SQLException {

        List<Cliente> clientes = new ArrayList<>();

        String sql = "SELECT cliente.* FROM cliente";

        stm = getConector().prepareStatement(sql);
        rs = stm.executeQuery(sql);

        while (rs.next()) {
            Cliente cliente = new Cliente((long) rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getLong(8), rs.getBoolean(9));

            clientes.add(cliente);
        }

        stm.close();
        rs.close();

        return clientes;
    }

    public List<Cliente> buscarPorNome(String busca) throws SQLException {

        List<Cliente> clientes = new ArrayList<>();

        String sql = "SELECT cliente.* FROM cliente WHERE nome LIKE '%" + busca + "%'";

        stm = getConector().prepareStatement(sql);
        rs = stm.executeQuery(sql);

        while (rs.next()) {
            Cliente cliente = new Cliente((long) rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getLong(8), rs.getBoolean(9));

            clientes.add(cliente);
        }

        stm.close();
        rs.close();

        return clientes;
    }

}
