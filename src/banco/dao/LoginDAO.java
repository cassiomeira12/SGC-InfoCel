package banco.dao;

import java.sql.SQLException;
import model.Administrador;

/**
 * Criado por Pedro Cordeiro DAO responsável pela ações realizadas na base de
 * dados referentes as login do usuário
 */
public class LoginDAO extends DAO {

    public LoginDAO() {
        super();
    }

    /**
     * Autenticar e validar nome do usuário informado
     */
    public boolean autenticarLogin(String nome) throws SQLException {

        String sql = "SELECT login FROM administrador WHERE login=? AND status = 1 ";

        stm = getConector().prepareStatement(sql);
        stm.setString(1, nome);
        rs = stm.executeQuery();

        if (rs.next()) {
            return nome.equals(rs.getString(1));
        }

        stm.close();
        rs.close();

        return false;
    }

    /**
     * Autenticar e validar senha do usuário informada
     */
    public boolean autenticarSenha(String nome, String senha) throws SQLException {

        String sql = "SELECT login, senha FROM administrador WHERE login=? AND senha=? ";

        stm = getConector().prepareStatement(sql);
        stm.setString(1, nome);
        stm.setString(2, senha);
        rs = stm.executeQuery();

        while (rs.next()) {
            return rs.getString(1).equals(nome) && rs.getString(2).equals(senha);
        }

        stm.close();
        rs.close();

        return false;
    }

    /**
     * Consultar informações do usuário logado na base de dados
     */
    public Administrador administradorLogado(String login) throws SQLException {

        Administrador admin = null;

        String sql = "SELECT administrador.* FROM administrador WHERE login = ?";

        stm = getConector().prepareStatement(sql);
        stm.setString(1, login);
        rs = stm.executeQuery();

        while (rs.next()) {
            admin = new Administrador((long) rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getLong(9), rs.getInt(10));
        }
        stm.close();
        rs.close();

        return admin;
    }

}
