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
    private MarcaDAO marcaDAO = new MarcaDAO();
    private CategoriaProdutoDAO categoriaProdutoDAO = new CategoriaProdutoDAO();
    private CategoriaSaidaDAO categoriaSaidaDAO = new CategoriaSaidaDAO();
    private ManutencaoDAO manutencaoDAO = new ManutencaoDAO();
    private ReceitaDAO receitaDAO = new ReceitaDAO();
    private SaidaDAO saidaDAO = new SaidaDAO();
    private VendaDAO vendaDAO = new VendaDAO();

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

    public MarcaDAO getMarcaDAO() {
        return marcaDAO;
    }

    public CategoriaProdutoDAO getCategoriaProdutoDAO() {
        return categoriaProdutoDAO;
    }

    public CategoriaSaidaDAO getCategoriaSaidaDAO() {
        return categoriaSaidaDAO;
    }

    public ManutencaoDAO getManutencaoDAO() {
        return manutencaoDAO;
    }

    public ReceitaDAO getReceitaDAO() {
        return receitaDAO;
    }

    public SaidaDAO getSaidaDAO() {
        return saidaDAO;
    }

    public VendaDAO getVendaDAO() {
        return vendaDAO;
    }

}//Fim class
