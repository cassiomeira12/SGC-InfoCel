package banco.dao;

import banco.ControleDAO;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Administrador;
import model.Bairro;
import model.Cidade;
import model.Endereco;

/**
 * DAO responsável pela ações realizadas na base de dados referentes as login do
 * usuário
 */
public class AdministradorDAO extends DAO {

    public AdministradorDAO() {
        super();
    }

    /**
     * Cadastrar um novo administrador na base de dados
     */
    /**
     * Inserir usuário na base de dados
     */
    public Long inserir(Administrador adm) throws Exception {
        ResultSet rs;
        PreparedStatement stm;

        String sql = "INSERT INTO administrador ( nome, login, senha, id_endereco, email, cpf, rg, data_cadastro, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Long id_endereco = ControleDAO.getBanco().getEnderecoDAO().inserir(adm.getEndereco());
        if (id_endereco == null) {
            return null;
        }

        stm = getConector().prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

        stm.setString(1, adm.getNome());
        stm.setString(2, adm.getLogin());
        stm.setString(3, adm.getSenha());
        stm.setLong(4, id_endereco);
        stm.setString(5, adm.getEmail());
        stm.setString(6, adm.getCpf());
        stm.setString(7, adm.getRg());
        stm.setLong(8, System.currentTimeMillis());
        stm.setBoolean(9, adm.getStatus());

        return super.inserir(stm);

    }

    /**
     * Atualizar dados administrador na base de dados
     */
    public boolean editar(Administrador adm) throws SQLException {
        ResultSet rs;
        PreparedStatement stm;

        String sql = "UPDATE administrador SET nome =?, login =?, senha =?, id_endereco = ?, email =?, cpf =?, rg =?, status =? WHERE id =?";

        //caso haja alterações no endereço
        ControleDAO.getBanco().getEnderecoDAO().editar(adm.getEndereco());

        stm = getConector().prepareStatement(sql);

        stm.setString(1, adm.getNome());
        stm.setString(2, adm.getLogin());
        stm.setString(3, adm.getSenha());
        stm.setLong(4, adm.getEndereco().getId());
        stm.setString(5, adm.getEmail());
        stm.setString(6, adm.getCpf());
        stm.setString(7, adm.getRg());
        stm.setBoolean(8, adm.getStatus());

        stm.setInt(9, adm.getId().intValue());

        stm.executeUpdate();
        stm.close();

        return true;
    }

    /**
     * Excluir administrador na base de dados
     */
    public boolean excluir(Administrador adm) throws SQLException {
        ResultSet rs;
        PreparedStatement stm;

        try {
            String sql = "DELETE from administrador WHERE id=" + adm.getId();

            stm = getConector().prepareStatement(sql);

            stm.execute();

            stm.close();
        } catch (MySQLIntegrityConstraintViolationException e) {
            adm.setStatus(false);
            editar(adm);
        }

        return true;
    }

    /**
     * Consultar todos administrador cadastrados na base de dados
     */
    public List<Administrador> listar() throws SQLException {
        ResultSet rs;
        PreparedStatement stm;

        List<Administrador> administradores = new ArrayList<>();

        String sql = "SELECT * FROM view_administrador WHERE status = 1";

        stm = getConector().prepareStatement(sql);
        rs = stm.executeQuery(sql);

        while (rs.next()) {
            Cidade cidade = new Cidade(rs.getLong("id_cidade"), rs.getString("nome_cidade"));
            Bairro bairro = new Bairro(rs.getLong("id_bairro"), rs.getString("nome_bairro"), cidade);
            Endereco endereco = new Endereco(rs.getLong("id_endereco"), bairro, rs.getString("rua"), rs.getString("numero"));

            Administrador admin = new Administrador((long) rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), endereco, rs.getString(5), rs.getString(6), rs.getString(7), rs.getLong(8), rs.getBoolean(9));
            administradores.add(admin);
        }

        stm.close();
        rs.close();

        return administradores;
    }

}
