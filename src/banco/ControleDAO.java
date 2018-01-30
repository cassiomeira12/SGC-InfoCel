/***********************************************************************
 * Autor: Cassio Meira Silva
 * Nome: ControleDAO
 * Funcao: Controlar o aceeso ao Banco de Dados
 ***********************************************************************/
package banco;

import banco.dao.*;

public class ControleDAO {

    private static ControleDAO banco = new ControleDAO();

    private LoginDAO loginDAO = new LoginDAO();
    private AdministradorDAO administradorDAO = new AdministradorDAO();

    public static ControleDAO getBanco() {
        return banco;
    }

    public LoginDAO getLoginDAO() {
        return loginDAO;
    }

    public AdministradorDAO getAdministradorDAO() {
        return administradorDAO;
    }

}//Fim class
