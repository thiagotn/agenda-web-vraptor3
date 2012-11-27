package org.ifsp.agenda.exception;

/**
 *
 * @author Ton
 */
public class UsuarioInexistenteException extends AgendaDAOException {

    private static final long serialVersionUID = 1L;

    /**
     *Está classe extende as Classe AgendaDAOException para verificar o usuarios inexistentes
     */
    public UsuarioInexistenteException() {
        super("Erro, Usuario nao existe");
    }
}
