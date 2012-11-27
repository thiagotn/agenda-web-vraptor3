package org.ifsp.agenda.exception;

/**
 *
 * @author Ton
 */
public class CompromissoInexistenteException extends AgendaDAOException {

    private static final long serialVersionUID = 1L;

    /**Está classe extende as Classe AgendaDAOException para verificar o compromissos inexistentes
     */
    public CompromissoInexistenteException() {
        super("Erro, Compromisso nao existe");
    }
}
