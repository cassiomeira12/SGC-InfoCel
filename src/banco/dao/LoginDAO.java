package banco.dao;

import java.sql.SQLException;
import model.Administrador;

/**
 * DAO responsável pela ações realizadas na base de dados referentes as login do usuário
 */
public class LoginDAO extends DAO {

    public LoginDAO() {
        super();
    }

    /**
     * Autenticar e validar nome do usuário informado
     */
    public boolean autenticarLogin(String nome) {

        try {
            String sql = "SELECT login FROM administrador WHERE login=? AND status = 1 ";

            stm = conector.prepareStatement(sql);
            stm.setString(1, nome);
            rs = stm.executeQuery();

            if (rs.next()) {
                return nome.equals(rs.getString(1));
            }

            stm.close();
            rs.close();

        } catch (SQLException ex) {
           // Mensagem.erro("Erro ao autenticar nome usuário na base de dados! \n" + ex);
           System.out.println("Erro ao autenticar nome usuário na base de dados!");
        }catch(Exception e){
            System.out.println("Erro ao autenticar nome usuário na base de dados!\n" + e);
        }

        return false;
    }

    /**
     * Autenticar e validar senha do usuário informada
     */
    public boolean autenticarSenha(String nome, String senha) {

      //  String chave = Criptografia.converter(senha);

        try {
            String sql = "SELECT login, senha FROM administrador WHERE login=? AND senha=? ";

            stm = conector.prepareStatement(sql);
            stm.setString(1, nome);
            stm.setString(2, senha);
            rs = stm.executeQuery();

            while (rs.next()) {
                return rs.getString(1).equals(nome) && rs.getString(2).equals(senha);
            }

            stm.close();
            rs.close();

        } catch (SQLException ex) {
           // Mensagem.erro("Erro ao autenticar senha usuário na base de dados! \n" + ex);
            System.out.println("Erro ao autenticar senha usuário na base de dados! \n" + ex);
        }

        return false;
    }

    /**
     * Consultar informações do usuário logado na base de dados
     */
    public Administrador administradorLogado(String login) {

        Administrador admin = null;

        try {
            String sql = "SELECT id, nome, login, senha, status FROM administrador WHERE login = ?";

            stm = conector.prepareStatement(sql);
            stm.setString(1, login);
            rs = stm.executeQuery();

            while (rs.next()) {
                admin = new Administrador(rs.getLong(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getInt(5));
            }
            stm.close();
            rs.close();

        } catch (SQLException ex) {
           // Mensagem.erro("Erro ao consultar usuário logado na base de dados! \n" + ex);
           System.out.println("Erro ao consultar usuário logado na base de dados! \n" + ex);
        }

        return admin;
    }
    
}
