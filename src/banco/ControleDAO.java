/***********************************************************************
* Autor: Cassio Meira Silva
* Nome: Classe
* Funcao: 
***********************************************************************/

package banco;

import banco.dao.*;


public class ControleDAO {

    private static ControleDAO banco = new ControleDAO();
    
    private CelularDAO celularDAO = new CelularDAO();
    private ClienteDAO clienteDAO = new ClienteDAO();
    private ProdutoDAO produtoDAO = new ProdutoDAO();
    
    
    
    public static ControleDAO getBanco() {
        return banco;
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