package banco.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Administrador;
import model.Celular;

/**
 * DAO responsável pela ações realizadas na base de dados referentes a celulares
 */
public class CelularDAO extends DAO {

    public CelularDAO() {
        super();
    }

    /**
     * Inserir celular na base de dados
     */
    public void inserir(Celular celular) {
        try {
            String sql = "INSERT INTO celular ( descricao ) VALUES (?)";

            stm = getConector().prepareStatement(sql);

            stm.setString(1, celular.getDescricao());

            stm.executeUpdate();
            stm.close();

        } catch (SQLException ex) {
            chamarAlertaErro("Erro ao inserir celular na base de dados", ex.toString());
        }
    }

    /**
     * Atualizar dados celular na base de dados
     */
    public void editar(Celular celular) {
        try {
            String sql = "UPDATE celular SET descricao =? WHERE id =?";

            stm = getConector().prepareStatement(sql);

            stm.setString(1, celular.getDescricao());

            stm.setInt(2, celular.getId().intValue());

            stm.executeUpdate();
            stm.close();

        } catch (SQLException ex) {
            chamarAlertaErro("Erro ao atualizar celular na base de dados!", ex.toString());
        }
    }

    /**
     * Excluir celular na base de dados
     */
    public void excluir(int id) {
        try {
            String sql = "DELETE FROM celular WHERE id=?";

            stm = getConector().prepareStatement(sql);

            stm.setInt(1, id);
            stm.execute();

            stm.close();
        } catch (SQLException ex) {
            chamarAlertaErro("Erro ao excluir celular na base de dados!", ex.toString());
        }
    }

    /**
     * Consultar todos celulares cadastrados na base de dados
     */
    public List<Celular> listar() {

        List<Celular> celulares = new ArrayList<>();

        try {
            String sql = "SELECT celular.* FROM celular";

            stm = getConector().prepareStatement(sql);
            rs = stm.executeQuery(sql);

            while (rs.next()) {
                Celular celular = new Celular((long) rs.getInt(1), rs.getString(2));

                celulares.add(celular);
            }

            stm.close();
            rs.close();

        } catch (SQLException ex) {
            chamarAlertaErro("Erro ao consultar celulares na base de dados!", ex.toString());
        }

        return celulares;
    }

}
