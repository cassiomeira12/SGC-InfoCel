/** *********************************************************************
 * Autor: Cassio Meira Silva
 * Nome: Classe
 * Funcao:
 ********************************************************************** */
package banco;

import banco.dao.*;

public class ControleDAO {

    private static ControleDAO banco = new ControleDAO();

    private LoginDAO loginDAO = new LoginDAO();

    public static ControleDAO getBanco() {
        return banco;
    }

    public LoginDAO getLoginDAO() {
        return loginDAO;
    }

}//Fim class
