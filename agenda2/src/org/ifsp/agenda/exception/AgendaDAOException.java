package org.ifsp.agenda.exception;

/**
 *
 * @author Ton
 */
public class AgendaDAOException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * Esta classe gerencia as exceção geradas pela agenda
     */
    public AgendaDAOException() {
    }

    /**
     *
     * @param arg
     */
    public AgendaDAOException(String arg) {
        super(arg);
    }

    /**
     *
     * @param arg
     */
    public AgendaDAOException(Throwable arg) {
        super(arg);
    }

    /**
     *
     * @param arg
     * @param arg1
     */
    public AgendaDAOException(String arg, Throwable arg1) {
        super(arg, arg1);
    }
}
