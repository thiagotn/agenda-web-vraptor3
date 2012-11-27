package org.ifsp.agenda.exception;


/**
 *
 * @author Ton
 */
public class CompromissoDAOException extends AgendaDAOException  {

	private static final long serialVersionUID = 1L;

    /**
     * Esta Classe estende a classe AgendaDAOException para gerenciar as exceções da Calsse CompromissoDAO 
     */
    public CompromissoDAOException( ) {
    }
    
    /**
     *
     * @param arg
     */
    public CompromissoDAOException(String arg) {
        super(arg);
    }
    
    /**
     *
     * @param arg
     */
    public CompromissoDAOException(Throwable arg) {
        super(arg);
    }
    
    /**
     *
     * @param arg
     * @param arg1
     */
    public CompromissoDAOException(String arg, Throwable arg1) {
        super(arg, arg1);
    }
}
