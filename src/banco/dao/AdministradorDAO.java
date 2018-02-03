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
    public void inserir(Administrador adm) {
        try {
            String sql = "INSERT INTO administrador ( nome, login, senha, status) VALUES (?, ?, ?, ?)";
            
            stm = getConector().prepareStatement(sql);
            
            stm.setString(1, adm.getNome());
            stm.setString(2, adm.getLogin());
            stm.setString(3, adm.getSenha());
            stm.setInt(4, adm.getStatus());
            
            stm.executeUpdate();
            stm.close();
            
        } catch (SQLException ex) {
            chamarAlertaErro("Erro ao inserir administrador na base de dados", ex.toString());
        }
    }

    /**
     * Atualizar dados administrador na base de dados
     */
    public void editar(Administrador adm) {
        try {
            String sql = "UPDATE administrador SET nome =?, login =?, senha =?, status =?, WHERE id =?";
            
            stm = getConector().prepareStatement(sql);
            
            stm.setString(1, adm.getNome());
            stm.setString(2, adm.getLogin());
            stm.setString(3, adm.getSenha());
            stm.setInt(4, adm.getStatus());
            
            stm.setInt(5, adm.getId().intValue());
            
            stm.executeUpdate();
            stm.close();
            
        } catch (SQLException ex) {
            chamarAlertaErro("Erro ao atualizar administrador na base de dados!", ex.toString());
        }
    }

    /**
     * Excluir administrador na base de dados
     */
    public void excluir(int id) {
        try {
            String sql = "DELETE FROM administrador WHERE id=?";
            
            stm = getConector().prepareStatement(sql);
            
            stm.setInt(1, id);
            stm.execute();
            
            stm.close();
        } catch (SQLException ex) {
            chamarAlertaErro("Erro ao excluir usuário na base de dados!", ex.toString());
        }
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
                Administrador adm = new Administrador((long) rs.getInt(1), rs.getNString(2), rs.getNString(3), rs.getNString(4), rs.getNString(5), rs.getNString(6), rs.getNString(7), rs.getLong(8), rs.getInt(9));
                administradores.add(adm);
            }
            
            stm.close();
            rs.close();
            
        } catch (SQLException ex) {
            chamarAlertaErro("Erro ao consultar administradoes na base de dados!", ex.toString());
        }
        
        return administradores;
    }
    
}
