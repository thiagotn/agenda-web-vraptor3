package org.ifsp.agenda.exception;

/**
 *
 * @author Ton
 */
public class ContatoInexistenteException extends AgendaDAOException {

    private static final long serialVersionUID = 1L;

    /**
     *Está classe extende as Classe AgendaDAOException para verificar o contatos inexistentes
     */
    public ContatoInexistenteException() {
        super("Erro, Contato nao existe");
    }
}
