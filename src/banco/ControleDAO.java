/** *********************************************************************
 * Autor: Cassio Meira Silva
 * Nome: ControleDAO
 * Funcao: Controlar o aceeso ao Banco de Dados
 ********************************************************************** */
package banco;

import banco.dao.*;

public class ControleDAO {

    private static ControleDAO banco = new ControleDAO();

    private LoginDAO loginDAO = new LoginDAO();
    private AdministradorDAO administradorDAO = new AdministradorDAO();
    private CelularDAO celularDAO = new CelularDAO();
    private ClienteDAO clienteDAO = new ClienteDAO();
    private ProdutoDAO produtoDAO = new ProdutoDAO();

    public static ControleDAO getBanco() {
        return banco;
    }

    public LoginDAO getLoginDAO() {
        return loginDAO;
    }

    public AdministradorDAO getAdministradorDAO() {
        return administradorDAO;
    }

    public CelularDAO getCelularDAO() {
        return celularDAO;
    }

    public ClienteDAO getClienteDAO() {
        return clienteDAO;
    }

    public ProdutoDAO getProdutoDAO() {
        return produtoDAO;
    }

}//Fim class
