package banco.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Administrador;

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
    public boolean inserir(Administrador adm) {
        try {
            String sql = "INSERT INTO administrador ( nome, login, senha, endereco, email, cpf, rg, data_cadastro, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

            stm = getConector().prepareStatement(sql);

            stm.setString(1, adm.getNome());
            stm.setString(2, adm.getLogin());
            stm.setString(3, adm.getSenha());
            stm.setString(4, adm.getEndereco());
            stm.setString(5, adm.getEmail());
            stm.setString(6, adm.getCpf());
            stm.setString(7, adm.getRg());
            stm.setLong(8, System.currentTimeMillis());
            stm.setInt(9, adm.getStatus());

            stm.executeUpdate();
            stm.close();

        } catch (SQLException ex) {
            chamarAlertaErro("Erro ao inserir administrador na base de dados", ex.toString());
            return false;
        }

        return true;
    }

    /**
     * Atualizar dados administrador na base de dados
     */
    public boolean editar(Administrador adm) {
        try {
            String sql = "UPDATE administrador SET nome =?, login =?, senha =?, endereco = ?, email =?, cpf =?, rg =?, status =?, WHERE id_administrador =?";

            stm = getConector().prepareStatement(sql);

            stm.setString(1, adm.getNome());
            stm.setString(2, adm.getLogin());
            stm.setString(3, adm.getSenha());
            stm.setString(4, adm.getEndereco());
            stm.setString(5, adm.getEmail());
            stm.setString(6, adm.getCpf());
            stm.setString(7, adm.getRg());
            stm.setInt(8, adm.getStatus());

            stm.setInt(9, adm.getId().intValue());

            stm.executeUpdate();
            stm.close();

        } catch (SQLException ex) {
            chamarAlertaErro("Erro ao atualizar administrador na base de dados!", ex.toString());
            return false;
        }

        return true;
    }

    /**
     * Excluir administrador na base de dados
     */
    public boolean excluir(int id) {
        try {
            String sql = "DELETE FROM administrador WHERE id_administrador=?";

            stm = getConector().prepareStatement(sql);

            stm.setInt(1, id);
            stm.execute();

            stm.close();
        } catch (SQLException ex) {
            chamarAlertaErro("Erro ao excluir administrador na base de dados!", ex.toString());
            return false;
        }

        return true;
    }

    /**
     * Consultar todos administrador cadastrados na base de dados
     */
    public List<Administrador> listar() {

        List<Administrador> administradores = new ArrayList<>();

        try {
            String sql = "SELECT administrador.* FROM administrador";

            stm = getConector().prepareStatement(sql);
            rs = stm.executeQuery(sql);

            while (rs.next()) {
                Administrador admin = new Administrador((long) rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getLong(9), rs.getInt(10));
                administradores.add(admin);
            }

            stm.close();
            rs.close();

        } catch (SQLException ex) {
            chamarAlertaErro("Erro ao consultar administradoes na base de dados!", ex.toString());
        }

        return administradores;
    }

}
