package org.ifsp.agenda.exception;

/**
 *
 * @author Ton
 */
public class ContatoDuplicadoException extends AgendaDAOException {

    private static final long serialVersionUID = 1L;

    /**
     *Está classe extende as Classe AgendaDAOException para verificar o contatos duplicados
     */
    public ContatoDuplicadoException() {
        super("Erro, Contato Duplicado!");
    }
}
